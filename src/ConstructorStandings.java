import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConstructorStandings {

    public static void crearTablaConstructorStandings(Connection conexion) {
        try (Statement statement = conexion.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS constructor_standings ("
                    + "constructorStandingsId INTEGER PRIMARY KEY,"
                    + "raceId INTEGER,"
                    + "constructorId INTEGER,"
                    + "points FLOAT,"
                    + "position INTEGER,"
                    + "positionText TEXT,"
                    + "wins INTEGER"
                    + ")";
            statement.executeUpdate(sql);
            System.out.println("Tabla constructor_standings creada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla constructor_standings: " + e.getMessage());
        }
    }

    public static void insertarConstructorStanding(Connection conexion, int constructorStandingsId, int raceId,
                                                   int constructorId, float points, int position, String positionText, int wins) {
        try (PreparedStatement statement = conexion.prepareStatement("INSERT INTO constructor_standings (constructorStandingsId, raceId, constructorId, points, position, positionText, wins) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, constructorStandingsId);
            statement.setInt(2, raceId);
            statement.setInt(3, constructorId);
            statement.setFloat(4, points);
            statement.setInt(5, position);
            statement.setString(6, positionText);
            statement.setInt(7, wins);
            statement.executeUpdate();
            System.out.println("Registro insertado correctamente en constructor_standings");
        } catch (SQLException e) {
            System.err.println("Error al insertar el registro en constructor_standings: " + e.getMessage());
        }
    }

    public static void mostrarConstructorStandings(Connection conexion) {
        try (Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM constructor_standings")) {

            System.out.println("Datos de la tabla constructor_standings:");

            while (resultSet.next()) {
                int constructorStandingsId = resultSet.getInt("constructorStandingsId");
                int raceId = resultSet.getInt("raceId");
                int constructorId = resultSet.getInt("constructorId");
                float points = resultSet.getFloat("points");
                int position = resultSet.getInt("position");
                String positionText = resultSet.getString("positionText");
                int wins = resultSet.getInt("wins");

                System.out.println("constructorStandingsId: " + constructorStandingsId
                        + ", raceId: " + raceId
                        + ", constructorId: " + constructorId
                        + ", points: " + points
                        + ", position: " + position
                        + ", positionText: " + positionText
                        + ", wins: " + wins);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los datos de la tabla constructor_standings: " + e.getMessage());
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

                int constructorStandingsId;
                int raceId;
                int constructorId;
                float points;
                int position;
                String positionText;
                int wins;

                try {
                    constructorStandingsId = datos[0].equals("\\N") ? 0 : Integer.parseInt(datos[0]);
                    raceId = datos[1].equals("\\N") ? 0 : Integer.parseInt(datos[1]);
                    constructorId = datos[2].equals("\\N") ? 0 : Integer.parseInt(datos[2]);
                    points = datos[3].equals("\\N") ? 0.0f : Float.parseFloat(datos[3]);
                    position = datos[4].equals("\\N") ? 0 : Integer.parseInt(datos[4]);
                    positionText = datos[5].equals("\\N") ? "" : datos[5];
                    wins = datos[6].equals("\\N") ? 0 : Integer.parseInt(datos[6]);
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir valores en la línea: " + linea);
                    continue;
                }

                insertarConstructorStanding(conexion, constructorStandingsId, raceId, constructorId, points, position, positionText, wins);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection conexion = App.obtenerConexion();
        crearTablaConstructorStandings(conexion);

        // Importar datos desde el archivo CSV
        String rutaArchivo = "\\Users\\Kev54\\Documents\\Proyecto PAG\\Archivos\\constructor_standings.csv";
        importarDatosDesdeCSV(conexion, rutaArchivo);

        // Mostrar los datos de la tabla constructor_standings
        mostrarConstructorStandings(conexion);

        App.cerrarConexion();
    }
}