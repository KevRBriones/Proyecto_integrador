import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Qualifying {

    public static void crearTablaQualifying(Connection conexion) {
        try (Statement statement = conexion.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS qualifying ("
                    + "qualifyId INTEGER PRIMARY KEY,"
                    + "raceId INTEGER,"
                    + "driverId INTEGER,"
                    + "constructorId INTEGER,"
                    + "number INTEGER,"
                    + "position INTEGER,"
                    + "q1 TEXT,"
                    + "q2 TEXT,"
                    + "q3 TEXT"
                    + ")";
            statement.executeUpdate(sql);
            System.out.println("Tabla qualifying creada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla qualifying: " + e.getMessage());
        }
    }

    public static void insertarQualifying(Connection conexion, int qualifyId, int raceId, int driverId, int constructorId, int number, int position, String q1, String q2, String q3) {
        try (PreparedStatement statement = conexion.prepareStatement("INSERT INTO qualifying (qualifyId, raceId, driverId, constructorId, number, position, q1, q2, q3) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, qualifyId);
            statement.setInt(2, raceId);
            statement.setInt(3, driverId);
            statement.setInt(4, constructorId);
            statement.setInt(5, number);
            statement.setInt(6, position);
            statement.setString(7, q1);
            statement.setString(8, q2);
            statement.setString(9, q3);
            statement.executeUpdate();
            System.out.println("Qualifying insertado correctamente");
        } catch (SQLException e) {
            System.err.println("Error al insertar el Qualifying: " + e.getMessage());
        }
    }

    public static void mostrarQualifying(Connection conexion) {
        try (Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM qualifying")) {

            System.out.println("Datos de la tabla qualifying:");

            while (resultSet.next()) {
                int qualifyId = resultSet.getInt("qualifyId");
                int raceId = resultSet.getInt("raceId");
                int driverId = resultSet.getInt("driverId");
                int constructorId = resultSet.getInt("constructorId");
                int number = resultSet.getInt("number");
                int position = resultSet.getInt("position");
                String q1 = resultSet.getString("q1");
                String q2 = resultSet.getString("q2");
                String q3 = resultSet.getString("q3");

                System.out.println("qualifyId: " + qualifyId
                        + ", raceId: " + raceId
                        + ", driverId: " + driverId
                        + ", constructorId: " + constructorId
                        + ", number: " + number
                        + ", position: " + position
                        + ", q1: " + q1
                        + ", q2: " + q2
                        + ", q3: " + q3);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los datos de la tabla qualifying: " + e.getMessage());
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
                if (datos.length != 9) {
                    System.err.println("Línea inválida en el archivo CSV: " + linea);
                    continue;
                }

                int qualifyId;
                int raceId;
                int driverId;
                int constructorId;
                int number;
                int position;
                String q1;
                String q2;
                String q3;

                try {
                    qualifyId = datos[0].equals("\\N") ? 0 : Integer.parseInt(datos[0]);
                    raceId = datos[1].equals("\\N") ? 0 : Integer.parseInt(datos[1]);
                    driverId = datos[2].equals("\\N") ? 0 : Integer.parseInt(datos[2]);
                    constructorId = datos[3].equals("\\N") ? 0 : Integer.parseInt(datos[3]);
                    number = datos[4].equals("\\N") ? 0 : Integer.parseInt(datos[4]);
                    position = datos[5].equals("\\N") ? 0 : Integer.parseInt(datos[5]);
                    q1 = datos[6].equals("\\N") ? "" : datos[6];
                    q2 = datos[7].equals("\\N") ? "" : datos[7];
                    q3 = datos[8].equals("\\N") ? "" : datos[8];
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir valores en la línea: " + linea);
                    continue;
                }

                insertarQualifying(conexion, qualifyId, raceId, driverId, constructorId, number, position, q1, q2, q3);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection conexion = App.obtenerConexion();
        crearTablaQualifying(conexion);

        // Importar datos desde el archivo CSV
        String rutaArchivo = "\\Users\\Kev54\\Documents\\Proyecto PAG\\Archivos\\qualifying.csv";
        importarDatosDesdeCSV(conexion, rutaArchivo);

        // Mostrar los datos de la tabla qualifying
        mostrarQualifying(conexion);

        App.cerrarConexion();
    }
}