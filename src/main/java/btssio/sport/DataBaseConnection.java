package btssio.sport;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    public static Connection getConnection() throws SQLException {
        try {
            // Charge le pilote JDBC MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL JDBC driver not found", e);
        }
        // Retourne la connexion établie à la base de données
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/m2l", "root", "Root1234");
    }
}