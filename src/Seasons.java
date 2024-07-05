import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Seasons {

    public static void crearTablaSeasons(Connection conexion) {
        try (Statement statement = conexion.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS seasons ("
                    + "year INTEGER PRIMARY KEY,"
                    + "url TEXT"
                    + ")";
            statement.executeUpdate(sql);
            System.out.println("Tabla seasons creada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla seasons: " + e.getMessage());
        }
    }

    public static void insertarSeason(Connection conexion, int year, String url) {
        try (PreparedStatement statement = conexion.prepareStatement("INSERT INTO seasons (year, url) VALUES (?, ?)")) {
            statement.setInt(1, year);
            statement.setString(2, url);
            statement.executeUpdate();
            System.out.println("Season insertada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al insertar la Season: " + e.getMessage());
        }
    }

    public static void mostrarSeasons(Connection conexion) {
        try (Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM seasons")) {

            System.out.println("Datos de la tabla seasons:");

            while (resultSet.next()) {
                int year = resultSet.getInt("year");
                String url = resultSet.getString("url");

                System.out.println("year: " + year
                        + ", url: " + url);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los datos de la tabla seasons: " + e.getMessage());
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

                int year;
                String url;

                try {
                    year = datos[0].equals("\\N") ? 0 : Integer.parseInt(datos[0]);
                    url = datos[1].equals("\\N") ? "" : datos[1];
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir valores en la línea: " + linea);
                    continue;
                }

                insertarSeason(conexion, year, url);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection conexion = App.obtenerConexion();
        crearTablaSeasons(conexion);

        // Importar datos desde el archivo CSV
        String rutaArchivo = "\\Users\\Kev54\\Documents\\Proyecto PAG\\Archivos\\seasons.csv";
        importarDatosDesdeCSV(conexion, rutaArchivo);

        // Mostrar los datos de la tabla seasons
        mostrarSeasons(conexion);

        App.cerrarConexion();
    }
}