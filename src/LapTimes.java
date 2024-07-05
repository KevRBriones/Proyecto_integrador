import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LapTimes {

    public static void crearTablaLapTimes(Connection conexion) {
        try (Statement statement = conexion.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS Lap_times ("
                    + "raceId INTEGER,"
                    + "driverId INTEGER,"
                    + "lap INTEGER,"
                    + "position INTEGER,"
                    + "time TEXT,"
                    + "milliseconds INTEGER"
                    + ")";
            statement.executeUpdate(sql);
            System.out.println("Tabla Lap_times creada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla Lap_times: " + e.getMessage());
        }
    }

    public static void insertarLapTime(Connection conexion, int raceId, int driverId, int lap, int position, String time, int milliseconds) {
        try (PreparedStatement statement = conexion.prepareStatement("INSERT INTO Lap_times (raceId, driverId, lap, position, time, milliseconds) VALUES (?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, raceId);
            statement.setInt(2, driverId);
            statement.setInt(3, lap);
            statement.setInt(4, position);
            statement.setString(5, time);
            statement.setInt(6, milliseconds);
            statement.executeUpdate();
            System.out.println("Lap Time insertado correctamente");
        } catch (SQLException e) {
            System.err.println("Error al insertar el Lap Time: " + e.getMessage());
        }
    }

    public static void mostrarLapTimes(Connection conexion) {
        try (Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Lap_times")) {

            System.out.println("Datos de la tabla Lap_times:");

            while (resultSet.next()) {
                int raceId = resultSet.getInt("raceId");
                int driverId = resultSet.getInt("driverId");
                int lap = resultSet.getInt("lap");
                int position = resultSet.getInt("position");
                String time = resultSet.getString("time");
                int milliseconds = resultSet.getInt("milliseconds");

                System.out.println("raceId: " + raceId
                        + ", driverId: " + driverId
                        + ", lap: " + lap
                        + ", position: " + position
                        + ", time: " + time
                        + ", milliseconds: " + milliseconds);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los datos de la tabla Lap_times: " + e.getMessage());
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
                if (datos.length != 6) {
                    System.err.println("Línea inválida en el archivo CSV: " + linea);
                    continue;
                }

                int raceId;
                int driverId;
                int lap;
                int position;
                String time;
                int milliseconds;

                try {
                    raceId = datos[0].equals("\\N") ? 0 : Integer.parseInt(datos[0]);
                    driverId = datos[1].equals("\\N") ? 0 : Integer.parseInt(datos[1]);
                    lap = datos[2].equals("\\N") ? 0 : Integer.parseInt(datos[2]);
                    position = datos[3].equals("\\N") ? 0 : Integer.parseInt(datos[3]);
                    time = datos[4].equals("\\N") ? "" : datos[4];
                    milliseconds = datos[5].equals("\\N") ? 0 : Integer.parseInt(datos[5]);
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir valores en la línea: " + linea);
                    continue;
                }

                insertarLapTime(conexion, raceId, driverId, lap, position, time, milliseconds);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection conexion = App.obtenerConexion();
        crearTablaLapTimes(conexion);

        // Importar datos desde el archivo CSV
        String rutaArchivo = "\\Users\\Kev54\\Documents\\Proyecto PAG\\Archivos\\lap_times.csv";
        importarDatosDesdeCSV(conexion, rutaArchivo);

        // Mostrar los datos de la tabla Lap_times
        mostrarLapTimes(conexion);

        App.cerrarConexion();
    }
}