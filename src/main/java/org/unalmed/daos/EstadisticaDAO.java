package org.unalmed.daos;

import org.unalmed.config.OracleClientInstance;
import org.unalmed.models.Ciudad;
import org.unalmed.models.Estadistica;
import org.unalmed.models.Venta;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticaDAO {
    // TODO: Refactor en más DAOS y cosas así, pero no vale la pena.

    final String GETSTATS = "SELECT * FROM ( SELECT cc, ciudad, departamento, total, rank() over (partition by ciudad order by total desc) rank FROM ( SELECT cc, e.miciu.nom AS ciudad, e.miciu.midep.nom AS departamento, SUM(v.miprod.precio_unitario * v.nro_unidades) total FROM empleado e, TABLE (e.ventas) v GROUP BY e.cc, e.miciu.midep.nom, e.miciu.nom ORDER BY total desc ) ) WHERE rank = 1";
    final String GETTOTALCITIES = "SELECT e.miciu.nom AS ciudad, SUM(v.nro_unidades*v.miprod.precio_unitario) AS total FROM empleado e, TABLE(e.ventas) v GROUP BY e.miciu.nom";

    private Connection oracleConn;
    private Map<String, Estadistica> estadisticas;
    private Map<String, Integer> totalCiudad;

    public EstadisticaDAO() {
        this.oracleConn = OracleClientInstance.oracleClient();
        this.estadisticas = new HashMap<>();
        this.totalCiudad = new HashMap<>();
    }

    /**
     * Transforma un ResultSet en los correspondientes POJOs (Plain old Java Object)
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
                ArrayList<Venta> estVentas = est.getMisVentas();
                estVentas.add(venta);
                estadisticas.put(depto, est);
            } else {
                est = new Estadistica();
                est.setMisVentas(new ArrayList<Venta>());
                est.setDepartamento(depto);
                est.getMisVentas().add(venta);
                estadisticas.put(depto, est);
            }
            return est;

        } catch (SQLException e) {
            e.getMessage();
            return null;
        }
    }

    /**
     * Genera las estadísticas necesarias.
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

            return this.estadisticas;
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
    }

}
