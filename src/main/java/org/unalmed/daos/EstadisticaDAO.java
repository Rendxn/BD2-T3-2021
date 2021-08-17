package org.unalmed.daos;

import com.mongodb.MongoBulkWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.slf4j.LoggerFactory;
import org.unalmed.config.MongoClientInstance;
import org.unalmed.config.OracleClientInstance;
import org.unalmed.models.Estadistica;
import org.unalmed.models.Venta;
import org.slf4j.Logger;

//import org.bson.codecs.pojo.PojoCodecProvider;
//import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
//import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticaDAO {
    // TODO: Refactor en más DAOS y cosas así, pero no vale la pena.

    final String GETSTATS = "SELECT * FROM ( SELECT cc, ciudad, departamento, total, rank() over (partition by ciudad order by total desc) rank FROM ( SELECT cc, e.miciu.nom AS ciudad, e.miciu.midep.nom AS departamento, SUM(v.miprod.precio_unitario * v.nro_unidades) total FROM empleado e, TABLE (e.ventas) v GROUP BY e.cc, e.miciu.midep.nom, e.miciu.nom ORDER BY total desc ) ) WHERE rank = 1";
    final String GETTOTALCITIES = "SELECT e.miciu.nom AS ciudad, SUM(v.nro_unidades*v.miprod.precio_unitario) AS total FROM empleado e, TABLE(e.ventas) v GROUP BY e.miciu.nom";
    final String BULKINSERT = "";

    private Map<String, Estadistica> estadisticas;
    private Map<String, Integer> totalCiudad;

    public static String ESTADISTICAS_COLLECTION = "estadisticas";
    private MongoCollection<Document> estadisticasCollection;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private final Logger log;
    private CodecRegistry pojoCodecRegistry;

    private Connection oracleConn;

    public EstadisticaDAO() {
        // Vars
        this.estadisticas = new HashMap<>();
        this.totalCiudad = new HashMap<>();
        // OracleDB
        this.oracleConn = OracleClientInstance.oracleClient();
        // MongoDB
        this.mongoClient = MongoClientInstance.mongoClient();
        this.mongoDatabase = this.mongoClient.getDatabase(System.getProperty("mongodb.database"));
        this.log = LoggerFactory.getLogger(this.getClass());
        // No quiso funcionar el codec ???
//        this.pojoCodecRegistry =
//                fromRegistries(
//                        MongoClientSettings.getDefaultCodecRegistry(),
//                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.estadisticasCollection = mongoDatabase.getCollection(ESTADISTICAS_COLLECTION);

    }

    /**
     * Transforma un ResultSet en los correspondientes POJOs (Plain old Java Object)
     *
     * @param rs - ResultSet extraído de la query GETSTATS.
     * @return Estadistica
     * @throws SQLException
     */
    public Estadistica transform(ResultSet rs) throws SQLException {
        try {
            // Always create a new Venta.
            Venta venta = new Venta();
            venta.setCcVendedor(rs.getString("cc"));
            venta.setNombreCiudad(rs.getString("ciudad"));
            venta.setTotalVendedor(rs.getInt("total"));
            venta.setTotalCiudad(this.totalCiudad.get(rs.getString("ciudad")));

            String depto = rs.getString("departamento");

            Estadistica est;

            if (this.estadisticas.containsKey(depto)) {
                est = this.estadisticas.get(depto);
                List<Venta> estVentas = est.getMisVentas();
                estVentas.add(venta);
            } else {
                est = new Estadistica();
                est.setMisVentas(new ArrayList<Venta>());
                est.setDepartamento(depto);
                est.getMisVentas().add(venta);
            }
            estadisticas.put(depto, est);
            return est;

        } catch (SQLException e) {
            e.getMessage();
            return null;
        }
    }

    /**
     * Genera las estadísticas necesarias.
     *
     * @return Map<String, Estadistica>
     * @throws SQLException
     */
    public Map<String, Estadistica> generate() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Generate totals for cities
            pstmt = oracleConn.prepareStatement(GETTOTALCITIES);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.totalCiudad.put(rs.getString("ciudad"), rs.getInt("total"));
            }

            // Generate totals for employees.
            pstmt = oracleConn.prepareStatement(GETSTATS);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                transform(rs);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error generando estadísticas", ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    throw new SQLException("Error cerrando ResultSet al generar estadísticas", ex);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    throw new SQLException("Error cerrando PreparedStatement al generar estadísticas", ex);
                }
            }
        }
        return this.estadisticas;
    }

    /**
     * Inserta en Mongo estadisticos generados en OracleDB.
     * @param estadisticas - Estadisticas a ser insertadas en Mongo
     * @return InsertManyResult
     * @throws MongoBulkWriteException
     */
    public InsertManyResult storeInMongo(List<Estadistica> estadisticas) throws MongoBulkWriteException {
        List<Document> documents = new ArrayList<>();
        for (Estadistica est : estadisticas) {
            Document estadistica = new Document();
            estadistica.append("departamento", est.getDepartamento());
            ArrayList<Document> misventas = new ArrayList<>();
            for (Venta ven : est.getMisVentas()) {
                Document venta = new Document();
                venta.append("nombre_ciudad", ven.getNombreCiudad());
                venta.append("total_ciudad", ven.getTotalCiudad());
                venta.append("cc_vendedor", ven.getCcVendedor());
                venta.append("total_vendedor", ven.getTotalVendedor());
                misventas.add(venta);
            }
            estadistica.append("misventas", misventas);
            documents.add(estadistica);
        }

        InsertManyResult result = this.estadisticasCollection.insertMany(documents);

        System.out.println(result.toString());
        return result;
    }

    public InsertManyResult storeInMongo() throws MongoBulkWriteException {
        List<Document> documents = new ArrayList<>();
        for (Estadistica est : this.estadisticas.values()) {
            Document estadistica = new Document();
            estadistica.append("departamento", est.getDepartamento());
            ArrayList<Document> misventas = new ArrayList<>();
            for (Venta ven : est.getMisVentas()) {
                Document venta = new Document();
                venta.append("nombre_ciudad", ven.getNombreCiudad());
                venta.append("total_ciudad", ven.getTotalCiudad());
                venta.append("cc_vendedor", ven.getCcVendedor());
                venta.append("total_vendedor", ven.getTotalVendedor());
                misventas.add(venta);
            }
            estadistica.append("misventas", misventas);
            documents.add(estadistica);
        }

        InsertManyResult result = this.estadisticasCollection.insertMany(documents);

        System.out.println(result.toString());
        return result;
    }

    /**
     * Trae todas las estadísticas en MongoDB y mappea a objetos.
     * @return List<Estadistica>
     */
    // TODO: Generar las demás estadísticas.
    public List<Estadistica> getEstadisticas() {
        List<Document> documents = new ArrayList<>();
        List<Estadistica> estadisticas = new ArrayList<>();
        // this.estadisticasCollection.find().iterator().forEachRemaining(documents::add);
        // TODO: Generar las demás estadísticas.
        FindIterable<Document> result = this.estadisticasCollection.find();
        for (Document doc : result) {
            Estadistica est = new Estadistica();
            est.setId(doc.getObjectId("_id"));
            est.setDepartamento(doc.getString("departamento"));

            List<Document> ventas_docs = doc.getList("misventas", Document.class);
            List<Venta> ventas = new ArrayList<>();
            for(Document v : ventas_docs) {
                Venta venta = new Venta();
                venta.setCcVendedor(v.getString("cc_vendedor"));
                venta.setNombreCiudad(v.getString("nombre_ciudad"));
                venta.setTotalCiudad(v.getInteger("total_ciudad"));
                venta.setTotalVendedor(v.getInteger("total_vendedor"));
                ventas.add(venta);
            }
            est.setMisVentas(ventas);
            estadisticas.add(est);
        }
        return estadisticas;
    }
}
