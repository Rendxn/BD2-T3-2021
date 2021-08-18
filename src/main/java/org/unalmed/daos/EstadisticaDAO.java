package org.unalmed.daos;

import com.mongodb.MongoBulkWriteException;
import com.mongodb.client.*;
import com.mongodb.client.model.BsonField;
import com.mongodb.client.result.InsertManyResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.slf4j.LoggerFactory;
import org.unalmed.config.MongoClientInstance;
import org.unalmed.config.OracleClientInstance;
import org.unalmed.models.EstadisticaDepartamento;
import org.unalmed.models.EstadisticaGlobal;
import org.unalmed.models.Venta;
import org.slf4j.Logger;

import static com.mongodb.client.model.Accumulators.first;
import static com.mongodb.client.model.Accumulators.last;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Accumulators.push;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Aggregates.*;

import java.sql.*;
import java.util.*;

public class EstadisticaDAO {
    // TODO: Refactor en más DAOS y cosas así, pero no vale la pena.

    final String GETSTATS = "SELECT * FROM ( SELECT cc, ciudad, departamento, total, rank() over (partition by ciudad order by total desc) rank FROM ( SELECT cc, e.miciu.nom AS ciudad, e.miciu.midep.nom AS departamento, SUM(v.miprod.precio_unitario * v.nro_unidades) total FROM empleado e, TABLE (e.ventas) v GROUP BY e.cc, e.miciu.midep.nom, e.miciu.nom ORDER BY total desc ) ) WHERE rank = 1";
    final String GETTOTALCITIES = "SELECT e.miciu.nom AS ciudad, SUM(v.nro_unidades*v.miprod.precio_unitario) AS total FROM empleado e, TABLE(e.ventas) v GROUP BY e.miciu.nom";

    private Map<String, EstadisticaDepartamento> estadisticas;
    private Map<String, Integer> totalCiudad;
    private List<EstadisticaDepartamento> estadisticaDepartamentos;
    private List<EstadisticaDepartamento> estadisticasGlobales;

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
    public EstadisticaDepartamento transformOracle(ResultSet rs) throws SQLException {
        try {
            // Always create a new Venta.
            Venta venta = new Venta();
            venta.setCcVendedor(rs.getString("cc"));
            venta.setNombreCiudad(rs.getString("ciudad"));
            venta.setTotalVendedor(rs.getInt("total"));
            venta.setTotalCiudad(this.totalCiudad.get(rs.getString("ciudad")));

            String depto = rs.getString("departamento");

            EstadisticaDepartamento est;

            if (this.estadisticas.containsKey(depto)) {
                est = this.estadisticas.get(depto);
                List<Venta> estVentas = est.getMisVentas();
                estVentas.add(venta);
            } else {
                est = new EstadisticaDepartamento();
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
    public Map<String, EstadisticaDepartamento> generate() throws SQLException {
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
                transformOracle(rs);
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
     * @param estadisticaDepartamentos - Estadisticas a ser insertadas en Mongo
     * @return InsertManyResult
     * @throws MongoBulkWriteException
     */
    public InsertManyResult storeInMongo(List<EstadisticaDepartamento> estadisticaDepartamentos) throws MongoBulkWriteException {
        List<Document> documents = new ArrayList<>();
        for (EstadisticaDepartamento est : estadisticaDepartamentos) {
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
        this.estadisticasCollection.deleteMany(new Document());
        for (EstadisticaDepartamento est : this.estadisticas.values()) {
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
     * Trae las estadísticas por departamento en MongoDB y mappea a objetos.
     * @return List<Estadistica>
     */
    // TODO: Generar estadísticas globales.
    public List<EstadisticaDepartamento> getEstadisticasDepartamentos() {
        Bson unwind1 = unwind("$misventas");
        Bson sort1 = sort(descending("misventas.total_vendedor"));
        BsonField sum1 = sum("total_departamento", "$misventas.total_ciudad");
        BsonField first1 = first("mejor_vendedor", "$misventas");
        BsonField last1 = last("peor_vendedor", "$misventas");
        BsonField push1 = push("misventas", "$misventas");
        Bson group1 = group("$departamento", sum1, first1, last1, push1);
        Bson unwind2 = unwind("$misventas");
        Bson sort2 = sort(descending("misventas.total_ciudad"));
        BsonField first2 = first("mejor_vendedor", "$mejor_vendedor");
        BsonField first3 = first("peor_vendedor", "$peor_vendedor");
        BsonField first4 = first("mejor_ciudad", "$misventas");
        BsonField first5 = first("total_departamento", "$total_departamento");
        Bson group2 = group("$_id", first2, first3, first4, first5);

        AggregateIterable<Document> result = this.estadisticasCollection.aggregate(Arrays.asList(unwind1, sort1, group1, unwind2, sort2, group2));

        this.estadisticaDepartamentos = transformMongo(result);

        return estadisticaDepartamentos;
    }

    /**
     * Trae las estadísticas globales de MongoDB.
     * - Nombre y valor del departamento que tuvo el valor total de ventas más alto.
     * - Nombre y valor de la ciudad que tuvo el valor total de ventas más alto.
     * - CC y valor del vendedor que tuvo el valor total de ventas más alto.
     * - CC y valor del vendedor que tuvo el valor total de ventas más bajo.
     * @return List
     */
    public EstadisticaGlobal getEstadisticasGlobales() {
        Bson unwind = unwind("$misventas");
        Bson sort = sort(descending("misventas.total_vendedor"));
        return new EstadisticaGlobal();
    }

    /**
     * Transforma queries de Mongo en POJO.
     * @param documents - Documentos a ser convertidos en objetos
     * @return
     */
    public List<EstadisticaDepartamento> transformMongo(AggregateIterable<Document> documents) {
        List<EstadisticaDepartamento> estadisticaDepartamentos = new ArrayList<>();
        for (Document doc : documents) {
            EstadisticaDepartamento est = new EstadisticaDepartamento();
            est.setDepartamento(doc.getString("_id"));
            est.setTotalDepartamento(doc.getInteger("total_departamento"));
            Venta bestSeller = transformVentaMongo((Document) doc.get("mejor_vendedor"));
            Venta worstSeller = transformVentaMongo((Document) doc.get("peor_vendedor"));
            Venta bestCity = transformVentaMongo((Document) doc.get("mejor_ciudad"));
            est.setMejorVendedor(bestSeller);
            est.setPeorVendedor(worstSeller);
            est.setMejorCiudad(bestCity);

            estadisticaDepartamentos.add(est);
        }
        return estadisticaDepartamentos;
    }

    /**
     * Transforma una venta que viene de Mongo en Objeto
     * TODO: Refactor into own DAO
     * @param v Document
     * @return Venta
     */
    public Venta transformVentaMongo(Document v) {
        Venta venta = new Venta();
        venta.setNombreCiudad(v.getString("nombre_ciudad"));
        venta.setTotalVendedor(v.getInteger("total_vendedor"));
        venta.setTotalCiudad(v.getInteger("total_ciudad"));
        venta.setCcVendedor(v.getString("cc_vendedor"));
        return venta;
    }
}
