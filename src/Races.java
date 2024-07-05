import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Races {

    public static void crearTablaRaces(Connection conexion) {
        try (Statement statement = conexion.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS races ("
                    + "raceId INTEGER PRIMARY KEY,"
                    + "year INTEGER,"
                    + "round INTEGER,"
                    + "circuitId INTEGER,"
                    + "name TEXT,"
                    + "date TEXT,"
                    + "time TEXT,"
                    + "url TEXT,"
                    + "fp1_date TEXT,"
                    + "fp1_time TEXT,"
                    + "fp2_date TEXT,"
                    + "fp2_time TEXT,"
                    + "fp3_date TEXT,"
                    + "fp3_time TEXT,"
                    + "quali_date TEXT,"
                    + "quali_time TEXT,"
                    + "sprint_date TEXT,"
                    + "sprint_time TEXT"
                    + ")";
            statement.executeUpdate(sql);
            System.out.println("Tabla races creada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla races: " + e.getMessage());
        }
    }

    public static void insertarRace(Connection conexion, int raceId, int year, int round, int circuitId, String name, String date, String time, String url, String fp1_date, String fp1_time, String fp2_date, String fp2_time, String fp3_date, String fp3_time, String quali_date, String quali_time, String sprint_date, String sprint_time) {
        try (PreparedStatement statement = conexion.prepareStatement("INSERT INTO races (raceId, year, round, circuitId, name, date, time, url, fp1_date, fp1_time, fp2_date, fp2_time, fp3_date, fp3_time, quali_date, quali_time, sprint_date, sprint_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, raceId);
            statement.setInt(2, year);
            statement.setInt(3, round);
            statement.setInt(4, circuitId);
            statement.setString(5, name);
            statement.setString(6, date);
            statement.setString(7, time);
            statement.setString(8, url);
            statement.setString(9, fp1_date);
            statement.setString(10, fp1_time);
            statement.setString(11, fp2_date);
            statement.setString(12, fp2_time);
            statement.setString(13, fp3_date);
            statement.setString(14, fp3_time);
            statement.setString(15, quali_date);
            statement.setString(16, quali_time);
            statement.setString(17, sprint_date);
            statement.setString(18, sprint_time);
            statement.executeUpdate();
            System.out.println("Race insertada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al insertar la Race: " + e.getMessage());
        }
    }

    public static void mostrarRaces(Connection conexion) {
        try (Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM races")) {

            System.out.println("Datos de la tabla races:");

            while (resultSet.next()) {
                int raceId = resultSet.getInt("raceId");
                int year = resultSet.getInt("year");
                int round = resultSet.getInt("round");
                int circuitId = resultSet.getInt("circuitId");
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                String url = resultSet.getString("url");
                String fp1_date = resultSet.getString("fp1_date");
                String fp1_time = resultSet.getString("fp1_time");
                String fp2_date = resultSet.getString("fp2_date");
                String fp2_time = resultSet.getString("fp2_time");
                String fp3_date = resultSet.getString("fp3_date");
                String fp3_time = resultSet.getString("fp3_time");
                String quali_date = resultSet.getString("quali_date");
                String quali_time = resultSet.getString("quali_time");
                String sprint_date = resultSet.getString("sprint_date");
                String sprint_time = resultSet.getString("sprint_time");

                System.out.println("raceId: " + raceId
                        + ", year: " + year
                        + ", round: " + round
                        + ", circuitId: " + circuitId
                        + ", name: " + name
                        + ", date: " + date
                        + ", time: " + time
                        + ", url: " + url
                        + ", fp1_date: " + fp1_date
                        + ", fp1_time: " + fp1_time
                        + ", fp2_date: " + fp2_date
                        + ", fp2_time: " + fp2_time
                        + ", fp3_date: " + fp3_date
                        + ", fp3_time: " + fp3_time
                        + ", quali_date: " + quali_date
                        + ", quali_time: " + quali_time
                        + ", sprint_date: " + sprint_date
                        + ", sprint_time: " + sprint_time);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los datos de la tabla races: " + e.getMessage());
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

                int raceId;
                int year;
                int round;
                int circuitId;
                String name;
                String date;
                String time;
                String url;
                String fp1_date;
                String fp1_time;
                String fp2_date;
                String fp2_time;
                String fp3_date;
                String fp3_time;
                String quali_date;
                String quali_time;
                String sprint_date;
                String sprint_time;

                try {
                    raceId = datos[0].equals("\\N") ? 0 : Integer.parseInt(datos[0]);
                    year = datos[1].equals("\\N") ? 0 : Integer.parseInt(datos[1]);
                    round = datos[2].equals("\\N") ? 0 : Integer.parseInt(datos[2]);
                    circuitId = datos[3].equals("\\N") ? 0 : Integer.parseInt(datos[3]);
                    name = datos[4].equals("\\N") ? "" : datos[4];
                    date = datos[5].equals("\\N") ? "" : datos[5];
                    time = datos[6].equals("\\N") ? "" : datos[6];
                    url = datos[7].equals("\\N") ? "" : datos[7];
                    fp1_date = datos[8].equals("\\N") ? "" : datos[8];
                    fp1_time = datos[9].equals("\\N") ? "" : datos[9];
                    fp2_date = datos[10].equals("\\N") ? "" : datos[10];
                    fp2_time = datos[11].equals("\\N") ? "" : datos[11];
                    fp3_date = datos[12].equals("\\N") ? "" : datos[12];
                    fp3_time = datos[13].equals("\\N") ? "" : datos[13];
                    quali_date = datos[14].equals("\\N") ? "" : datos[14];
                    quali_time = datos[15].equals("\\N") ? "" : datos[15];
                    sprint_date = datos[16].equals("\\N") ? "" : datos[16];
                    sprint_time = datos[17].equals("\\N") ? "" : datos[17];
               } catch (NumberFormatException e) {
                   System.err.println("Error al convertir valores en la línea: " + linea);
                   continue;
               }

               insertarRace(conexion, raceId, year, round, circuitId, name, date, time, url, fp1_date, fp1_time, fp2_date, fp2_time, fp3_date, fp3_time, quali_date, quali_time, sprint_date, sprint_time);
           }
       } catch (IOException e) {
           System.err.println("Error al leer el archivo CSV: " + e.getMessage());
       }
   }

   public static void main(String[] args) {
       Connection conexion = App.obtenerConexion();
       crearTablaRaces(conexion);

       // Importar datos desde el archivo CSV
       String rutaArchivo = "\\Users\\Kev54\\Documents\\Proyecto PAG\\Archivos\\races.csv";
       importarDatosDesdeCSV(conexion, rutaArchivo);

       // Mostrar los datos de la tabla races
       mostrarRaces(conexion);

       App.cerrarConexion();
   }
}