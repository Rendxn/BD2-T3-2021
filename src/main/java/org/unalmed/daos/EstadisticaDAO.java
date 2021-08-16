package org.unalmed.daos;

import org.unalmed.config.OracleClientInstance;
import org.unalmed.models.Estadistica;
import org.unalmed.models.Venta;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstadisticaDAO {

    final String GETSTATS = "SELECT * from EMPLEADO";

    private Connection oracleConn;

    public EstadisticaDAO() {
        this.oracleConn = OracleClientInstance.oracleClient();
    }

    public Estadistica transform(ResultSet rs) throws SQLException {
        try {
            Estadistica est = new Estadistica();

            // TODO:
            Venta venta = new Venta();
            venta.setCcVendedor(rs.getString("cc"));
            venta.setNombreCiudad(rs.getString("miciu"));
            Array v = rs.getArray("ventas");
            Object[] nullable = (Object[])v.getArray();
            for (Object obj : nullable) {
                for (Field field : obj.getClass().getDeclaredFields()) {
                    System.out.println("field: " + field.getName());
                }
            }

            return est;
        } catch (SQLException e) {
            e.getMessage();
            return null;
        }
    }

    public List<Estadistica> generate() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Estadistica> estadisticas = new ArrayList<>();

        try {
            pstmt = oracleConn.prepareStatement(GETSTATS);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                estadisticas.add(transform(rs));
            }
            return estadisticas;
        } catch(SQLException ex) {
            ex.getMessage();
            return null;
        }
    }

}
