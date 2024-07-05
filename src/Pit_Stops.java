import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Pit_Stops {

    public static void crearTablaPitStops(Connection conexion) {
        try (Statement statement = conexion.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS pit_stops ("
                    + "raceId INTEGER,"
                    + "driverId INTEGER,"
                    + "stop INTEGER,"
                    + "lap INTEGER,"
                    + "time TEXT,"
                    + "duration TEXT,"
                    + "milliseconds INTEGER"
                    + ")";
            statement.executeUpdate(sql);
            System.out.println("Tabla pit_stops creada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla pit_stops: " + e.getMessage());
        }
    }

    public static void insertarPitStop(Connection conexion, int raceId, int driverId, int stop, int lap, String time, String duration, int milliseconds) {
        try (PreparedStatement statement = conexion.prepareStatement("INSERT INTO pit_stops (raceId, driverId, stop, lap, time, duration, milliseconds) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, raceId);
            statement.setInt(2, driverId);
            statement.setInt(3, stop);
            statement.setInt(4, lap);
            statement.setString(5, time);
            statement.setString(6, duration);
            statement.setInt(7, milliseconds);
            statement.executeUpdate();
            System.out.println("Pit Stop insertado correctamente");
        } catch (SQLException e) {
            System.err.println("Error al insertar el Pit Stop: " + e.getMessage());
        }
    }

    public static void mostrarPitStops(Connection conexion) {
        try (Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM pit_stops")) {

            System.out.println("Datos de la tabla pit_stops:");

            while (resultSet.next()) {
                int raceId = resultSet.getInt("raceId");
                int driverId = resultSet.getInt("driverId");
                int stop = resultSet.getInt("stop");
                int lap = resultSet.getInt("lap");
                String time = resultSet.getString("time");
                String duration = resultSet.getString("duration");
                int milliseconds = resultSet.getInt("milliseconds");

                System.out.println("raceId: " + raceId
                        + ", driverId: " + driverId
                        + ", stop: " + stop
                        + ", lap: " + lap
                        + ", time: " + time
                        + ", duration: " + duration
                        + ", milliseconds: " + milliseconds);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los datos de la tabla pit_stops: " + e.getMessage());
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
                if (datos.length != 7) {
                    System.err.println("Línea inválida en el archivo CSV: " + linea);
                    continue;
                }

                int raceId;
                int driverId;
                int stop;
                int lap;
                String time;
                String duration;
                int milliseconds;

                try {
                    raceId = datos[0].equals("\\N") ? 0 : Integer.parseInt(datos[0]);
                    driverId = datos[1].equals("\\N") ? 0 : Integer.parseInt(datos[1]);
                    stop = datos[2].equals("\\N") ? 0 : Integer.parseInt(datos[2]);
                    lap = datos[3].equals("\\N") ? 0 : Integer.parseInt(datos[3]);
                    time = datos[4].equals("\\N") ? "" : datos[4];
                    duration = datos[5].equals("\\N") ? "" : datos[5];
                    milliseconds = datos[6].equals("\\N") ? 0 : Integer.parseInt(datos[6]);
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir valores en la línea: " + linea);
                    continue;
                }

                insertarPitStop(conexion, raceId, driverId, stop, lap, time, duration, milliseconds);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection conexion = App.obtenerConexion();
        crearTablaPitStops(conexion);

        // Importar datos desde el archivo CSV
        String rutaArchivo = "\\Users\\Kev54\\Documents\\Proyecto PAG\\Archivos\\pit_stops.csv";
        importarDatosDesdeCSV(conexion, rutaArchivo);

        // Mostrar los datos de la tabla pit_stops
        mostrarPitStops(conexion);

        App.cerrarConexion();
    }
}