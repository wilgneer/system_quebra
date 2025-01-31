package new_logmais;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conn {
    private static final String URL = "jdbc:postgresql://201.55.174.26:54177/iglobal";
    private static final String USER = "postgres";
    private static final String PASSWORD = "MasterKey!!";

    public static Connection getConnection() throws SQLException {
        try {
            // Registrar o driver do PostgreSQL
            Class.forName("org.postgresql.Driver");
            // Retornar a conexão
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Driver não encontrado.");
        }
    }
}