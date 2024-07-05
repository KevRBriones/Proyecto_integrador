import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Results {

    public static void crearTablaResults(Connection conexion) {
        try (Statement statement = conexion.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS results ("
                    + "resultId INTEGER PRIMARY KEY,"
                    + "raceId INTEGER,"
                    + "driverId INTEGER,"
                    + "constructorId INTEGER,"
                    + "number INTEGER,"
                    + "grid INTEGER,"
                    + "position INTEGER,"
                    + "positionText TEXT,"
                    + "positionOrder INTEGER,"
                    + "points INTEGER,"
                    + "laps INTEGER,"
                    + "time TEXT,"
                    + "milliseconds INTEGER,"
                    + "fastestLap INTEGER,"
                    + "rank INTEGER,"
                    + "fastestLapTime TEXT,"
                    + "fastestLapSpeed TEXT,"
                    + "statusId INTEGER"
                    + ")";
            statement.executeUpdate(sql);
            System.out.println("Tabla results creada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla results: " + e.getMessage());
        }
    }

    public static void insertarResult(Connection conexion, int resultId, int raceId, int driverId, int constructorId, int number, int grid, int position, String positionText, int positionOrder, int points, int laps, String time, int milliseconds, int fastestLap, int rank, String fastestLapTime, String fastestLapSpeed, int statusId) {
        try (PreparedStatement statement = conexion.prepareStatement("INSERT INTO results (resultId, raceId, driverId, constructorId, number, grid, position, positionText, positionOrder, points, laps, time, milliseconds, fastestLap, rank, fastestLapTime, fastestLapSpeed, statusId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, resultId);
            statement.setInt(2, raceId);
            statement.setInt(3, driverId);
            statement.setInt(4, constructorId);
            statement.setInt(5, number);
            statement.setInt(6, grid);
            statement.setInt(7, position);
            statement.setString(8, positionText);
            statement.setInt(9, positionOrder);
            statement.setInt(10, points);
            statement.setInt(11, laps);
            statement.setString(12, time);
            statement.setInt(13, milliseconds);
            statement.setInt(14, fastestLap);
            statement.setInt(15, rank);
            statement.setString(16, fastestLapTime);
            statement.setString(17, fastestLapSpeed);
            statement.setInt(18, statusId);
            statement.executeUpdate();
            System.out.println("Result insertado correctamente");
        } catch (SQLException e) {
            System.err.println("Error al insertar el Result: " + e.getMessage());
        }
    }

    public static void mostrarResults(Connection conexion) {
        try (Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM results")) {

            System.out.println("Datos de la tabla results:");

            while (resultSet.next()) {
                int resultId = resultSet.getInt("resultId");
                int raceId = resultSet.getInt("raceId");
                int driverId = resultSet.getInt("driverId");
                int constructorId = resultSet.getInt("constructorId");
                int number = resultSet.getInt("number");
                int grid = resultSet.getInt("grid");
                int position = resultSet.getInt("position");
                String positionText = resultSet.getString("positionText");
                int positionOrder = resultSet.getInt("positionOrder");
                int points = resultSet.getInt("points");
                int laps = resultSet.getInt("laps");
                String time = resultSet.getString("time");
                int milliseconds = resultSet.getInt("milliseconds");
                int fastestLap = resultSet.getInt("fastestLap");
                int rank = resultSet.getInt("rank");
                String fastestLapTime = resultSet.getString("fastestLapTime");
                String fastestLapSpeed = resultSet.getString("fastestLapSpeed");
                int statusId = resultSet.getInt("statusId");

                System.out.println("resultId: " + resultId
                        + ", raceId: " + raceId
                        + ", driverId: " + driverId
                        + ", constructorId: " + constructorId
                        + ", number: " + number
                        + ", grid: " + grid
                        + ", position: " + position
                        + ", positionText: " + positionText
                        + ", positionOrder: " + positionOrder
                        + ", points: " + points
                        + ", laps: " + laps
                        + ", time: " + time
                        + ", milliseconds: " + milliseconds
                        + ", fastestLap: " + fastestLap
                        + ", rank: " + rank
                        + ", fastestLapTime: " + fastestLapTime
                        + ", fastestLapSpeed: " + fastestLapSpeed
                        + ", statusId: " + statusId);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los datos de la tabla results: " + e.getMessage());
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
                if (datos.length != 18) {
                    System.err.println("Línea inválida en el archivo CSV: " + linea);
                    continue;
                }

                int resultId;
                int raceId;
                int driverId;
                int constructorId;
                int number;
                int grid;
                int position;
                String positionText;
                int positionOrder;
                int points;
                int laps;
                String time;
                int milliseconds;
                int fastestLap;
                int rank;
                String fastestLapTime;
                String fastestLapSpeed;
                int statusId;

                try {
                    resultId = datos[0].equals("\\N") ? 0 : Integer.parseInt(datos[0]);
                    raceId = datos[1].equals("\\N") ? 0 : Integer.parseInt(datos[1]);
                    driverId = datos[2].equals("\\N") ? 0 : Integer.parseInt(datos[2]);
                    constructorId = datos[3].equals("\\N") ? 0 : Integer.parseInt(datos[3]);
                    number = datos[4].equals("\\N") ? 0 : Integer.parseInt(datos[4]);
                    grid = datos[5].equals("\\N") ? 0 : Integer.parseInt(datos[5]);
                    position = datos[6].equals("\\N") ? 0 : Integer.parseInt(datos[6]);
                    positionText = datos[7].equals("\\N") ? "" : datos[7];
                    positionOrder = datos[8].equals("\\N") ? 0 : Integer.parseInt(datos[8]);
                    points = datos[9].equals("\\N") ? 0 : Integer.parseInt(datos[9]);
                    laps = datos[10].equals("\\N") ? 0 : Integer.parseInt(datos[10]);
                    time = datos[11].equals("\\N") ? "" : datos[11];
                    milliseconds = datos[12].equals("\\N") ? 0 : Integer.parseInt(datos[12]);
                    fastestLap = datos[13].equals("\\N") ? 0 : Integer.parseInt(datos[13]);
                    rank = datos[14].equals("\\N") ? 0 : Integer.parseInt(datos[14]);
                    fastestLapTime = datos[15].equals("\\N") ? "" : datos[15];
                    fastestLapSpeed = datos[16].equals("\\N") ? "" : datos[16];
                    statusId = datos[17].equals("\\N") ? 0 : Integer.parseInt(datos[17]);
               } catch (NumberFormatException e) {
                   System.err.println("Error al convertir valores en la línea: " + linea);
                   continue;
               }

               insertarResult(conexion, resultId, raceId, driverId, constructorId, number, grid, position, positionText, positionOrder, points, laps, time, milliseconds, fastestLap, rank, fastestLapTime, fastestLapSpeed, statusId);
           }
       } catch (IOException e) {
           System.err.println("Error al leer el archivo CSV: " + e.getMessage());
       }
   }

   public static void main(String[] args) {
       Connection conexion = App.obtenerConexion();
       crearTablaResults(conexion);

       // Importar datos desde el archivo CSV
       String rutaArchivo = "\\Users\\Kev54\\Documents\\Proyecto PAG\\Archivos\\results.csv";
       importarDatosDesdeCSV(conexion, rutaArchivo);

       // Mostrar los datos de la tabla results
       mostrarResults(conexion);

       App.cerrarConexion();
   }
}