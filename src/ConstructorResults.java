import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConstructorResults {

    public static void crearTablaConstructorResults(Connection conexion) {
        try (Statement statement = conexion.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS constructor_results ("
                    + "constructorResultsId INTEGER PRIMARY KEY,"
                    + "raceId INTEGER,"
                    + "constructorId INTEGER,"
                    + "points FLOAT,"
                    + "status TEXT"
                    + ")";
            statement.executeUpdate(sql);
            System.out.println("Tabla constructor_results creada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla constructor_results: " + e.getMessage());
        }
    }

    public static void insertarConstructorResult(Connection conexion, int constructorResultsId, int raceId,
                                                 int constructorId, float points, String status) {
        try (PreparedStatement statement = conexion.prepareStatement("INSERT INTO constructor_results (constructorResultsId, raceId, constructorId, points, status) VALUES (?, ?, ?, ?, ?)")) {
            statement.setInt(1, constructorResultsId);
            statement.setInt(2, raceId);
            statement.setInt(3, constructorId);
            statement.setFloat(4, points);
            statement.setString(5, status);
            statement.executeUpdate();
            System.out.println("Registro insertado correctamente en constructor_results");
        } catch (SQLException e) {
            System.err.println("Error al insertar el registro en constructor_results: " + e.getMessage());
        }
    }

    public static void mostrarConstructorResults(Connection conexion) {
        try (Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM constructor_results")) {

            System.out.println("Datos de la tabla constructor_results:");

            while (resultSet.next()) {
                int constructorResultsId = resultSet.getInt("constructorResultsId");
                int raceId = resultSet.getInt("raceId");
                int constructorId = resultSet.getInt("constructorId");
                float points = resultSet.getFloat("points");
                String status = resultSet.getString("status");

                System.out.println("constructorResultsId: " + constructorResultsId
                        + ", raceId: " + raceId
                        + ", constructorId: " + constructorId
                        + ", points: " + points
                        + ", status: " + status);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los datos de la tabla constructor_results: " + e.getMessage());
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
                if (datos.length != 5) {
                    System.err.println("Línea inválida en el archivo CSV: " + linea);
                    continue;
                }

                int constructorResultsId;
                int raceId;
                int constructorId;
                float points;
                String status;

                try {
                    constructorResultsId = datos[0].equals("\\N") ? 0 : Integer.parseInt(datos[0]);
                    raceId = datos[1].equals("\\N") ? 0 : Integer.parseInt(datos[1]);
                    constructorId = datos[2].equals("\\N") ? 0 : Integer.parseInt(datos[2]);
                    points = datos[3].equals("\\N") ? 0.0f : Float.parseFloat(datos[3]);
                    status = datos[4].equals("\\N") ? "" : datos[4];
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir valores en la línea: " + linea);
                    continue;
                }

                insertarConstructorResult(conexion, constructorResultsId, raceId, constructorId, points, status);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection conexion = App.obtenerConexion();
        crearTablaConstructorResults(conexion);

        // Importar datos desde el archivo CSV
        String rutaArchivo = "\\Users\\Kev54\\Documents\\Proyecto PAG\\Archivos\\constructor_results.csv";
        importarDatosDesdeCSV(conexion, rutaArchivo);

        // Mostrar los datos de la tabla constructor_results
        mostrarConstructorResults(conexion);

        App.cerrarConexion();
    }
}