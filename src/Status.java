import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Status {

    public static void crearTablaStatus(Connection conexion) {
        try (Statement statement = conexion.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS Status ("
                    + "statusId INTEGER PRIMARY KEY,"
                    + "status TEXT"
                    + ")";
            statement.executeUpdate(sql);
            System.out.println("Tabla Status creada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla Status: " + e.getMessage());
        }
    }

    public static void insertarStatus(Connection conexion, int statusId, String status) {
        try (PreparedStatement statement = conexion.prepareStatement("INSERT INTO Status (statusId, status) VALUES (?, ?)")) {
            statement.setInt(1, statusId);
            statement.setString(2, status);
            statement.executeUpdate();
            System.out.println("Status insertado correctamente");
        } catch (SQLException e) {
            System.err.println("Error al insertar el Status: " + e.getMessage());
        }
    }

    public static void mostrarStatus(Connection conexion) {
        try (Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Status")) {

            System.out.println("Datos de la tabla Status:");

            while (resultSet.next()) {
                int statusId = resultSet.getInt("statusId");
                String status = resultSet.getString("status");

                System.out.println("statusId: " + statusId
                        + ", status: " + status);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los datos de la tabla Status: " + e.getMessage());
        }
    }

    public static void importarDatosDesdeCSV(Connection conexion, String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            // Omitir la primera línea (encabezados de columna)
            br.readLine();

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");

                // Validar que haya suficientes datos en la línea
                if (datos.length != 2) {
                    System.err.println("Línea inválida en el archivo CSV: " + linea);
                    continue;
                }

                int statusId;
                String status;

                try {
                    statusId = datos[0].equals("\\N") ? 0 : Integer.parseInt(datos[0]);
                    status = datos[1].equals("\\N") ? "" : datos[1];
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir valores en la línea: " + linea);
                    continue;
                }

                insertarStatus(conexion, statusId, status);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection conexion = App.obtenerConexion();
        crearTablaStatus(conexion);

        // Importar datos desde el archivo CSV
        String rutaArchivo = "\\Users\\Kev54\\Documents\\Proyecto PAG\\Archivos\\status.csv";
        importarDatosDesdeCSV(conexion, rutaArchivo);

        // Mostrar los datos de la tabla Status
        mostrarStatus(conexion);

        App.cerrarConexion();
    }
}
