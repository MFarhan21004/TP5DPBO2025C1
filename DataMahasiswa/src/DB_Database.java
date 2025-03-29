import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB_Database {
    private Connection connection;

    // Constructor
    public DB_Database() {
        try {

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_mahasiswa", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException("Koneksi database gagal!", e);
        }
    }

    // Digunakan untuk SELECT
    public ResultSet selectQuery(String sql) {
        try {
            java.sql.Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Kesalahan saat menjalankan SELECT query", e);
        }
    }

    // Digunakan untuk INSERT, UPDATE, dan DELETE
    public int insertUpdateDeleteQuery(String sql) {
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sql);
            statement.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Kesalahan saat menjalankan INSERT/UPDATE/DELETE query", e);
        }
    }

    // Menutup koneksi database
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menutup koneksi database", e);
        }
    }
}
