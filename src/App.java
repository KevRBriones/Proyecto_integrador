import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {

    private static Connection conexion = null;

    public static Connection obtenerConexion() {
        if (conexion == null) {
            try {
                // Registrar el controlador JDBC de PostgreSQL
                Class.forName("org.postgresql.Driver");

                // Establecer la conexión
                String url = "jdbc:postgresql://localhost:5432/Formula1";
                String usuario = "postgres";
                String contraseña = "1234";
                conexion = DriverManager.getConnection(url, usuario, contraseña);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método de prueba para verificar la conexión
    public static void probarConexion() {
        Connection conexionPrueba = obtenerConexion();
        if (conexionPrueba != null) {
            System.out.println("Conexión exitosa a PostgreSQL");
        } else {
            System.out.println("Error al conectar a PostgreSQL");
        }
        cerrarConexion();
    }

    public static void main(String[] args) {
        probarConexion(); // Llamar al método de prueba
    }
}