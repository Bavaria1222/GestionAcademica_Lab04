package org.example.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Servicio {
    protected Connection conexion = null;

    public Servicio() {
    }

    protected void conectar() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        this.conexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "system");
    }

    protected void desconectar() throws SQLException {
        if (!this.conexion.isClosed()) {
            this.conexion.close();
        }

    }
}
