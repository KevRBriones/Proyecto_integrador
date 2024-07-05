import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Circuits {

    public static void crearTablaCircuitos(Connection conexion) {
        try (Statement statement = conexion.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS circuitos ("
                    + "circuitId INTEGER PRIMARY KEY,"
                    + "circuitRef TEXT,"
                    + "name TEXT,"
                    + "location TEXT,"
                    + "country TEXT,"
                    + "lat FLOAT,"
                    + "lng FLOAT,"
                    + "alt INTEGER,"
                    + "url TEXT"
                    + ")";
            statement.executeUpdate(sql);
            System.out.println("Tabla circuitos creada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla circuitos: " + e.getMessage());
        }
    }

    public static void insertarCircuito(Connection conexion, int circuitId, String circuitRef, String name,
                                        String location, String country, float lat, float lng, int alt, String url) {
        try (PreparedStatement statement = conexion.prepareStatement("INSERT INTO circuitos (circuitId, circuitRef, name, location, country, lat, lng, alt, url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, circuitId);
            statement.setString(2, circuitRef);
            statement.setString(3, name);
            statement.setString(4, location);
            statement.setString(5, country);
            statement.setFloat(6, lat);
            statement.setFloat(7, lng);
            statement.setInt(8, alt);
            statement.setString(9, url);
            statement.executeUpdate();
            System.out.println("Circuito insertado correctamente");
        } catch (SQLException e) {
            System.err.println("Error al insertar el circuito: " + e.getMessage());
        }
    }

    public static void mostrarCircuitos(Connection conexion) {
    try (Statement statement = conexion.createStatement();
         ResultSet resultSet = statement.executeQuery("SELECT * FROM circuitos")) {

        System.out.println("Datos de la tabla circuitos:");

        while (resultSet.next()) {
            int circuitId = resultSet.getInt("circuitId");
            String circuitRef = resultSet.getString("circuitRef");
            String name = resultSet.getString("name");
            String location = resultSet.getString("location");
            String country = resultSet.getString("country");
            float lat = resultSet.getFloat("lat");
            float lng = resultSet.getFloat("lng");
            int alt = resultSet.getInt("alt");
            String url = resultSet.getString("url");

            System.out.println("circuitId: " + circuitId
                    + ", circuitRef: " + circuitRef
                    + ", name: " + name
                    + ", location: " + location
                    + ", country: " + country
                    + ", lat: " + lat
                    + ", lng: " + lng
                    + ", alt: " + alt
                    + ", url: " + url);
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener los datos de la tabla circuitos: " + e.getMessage());
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
    
                int circuitId;
                String circuitRef;
                String name;
                String location;
                String country;
                float lat;
                float lng;
                int alt;
                String url;
    
                try {
                    circuitId = datos[0].equals("\\N") ? 0 : Integer.parseInt(datos[0]);
                    circuitRef = datos[1].equals("\\N") ? "" : datos[1];
                    name = datos[2].equals("\\N") ? "" : datos[2];
                    location = datos[3].equals("\\N") ? "" : datos[3];
                    country = datos[4].equals("\\N") ? "" : datos[4];
                    lat = datos[5].equals("\\N") ? 0.0f : Float.parseFloat(datos[5]);
                    lng = datos[6].equals("\\N") ? 0.0f : Float.parseFloat(datos[6]);
                    alt = datos[7].equals("\\N") ? 0 : Integer.parseInt(datos[7]);
                    url = datos[8].equals("\\N") ? "" : datos[8];
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir valores en la línea: " + linea);
                    continue;
                }
    
                insertarCircuito(conexion, circuitId, circuitRef, name, location, country, lat, lng, alt, url);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection conexion = App.obtenerConexion();
        crearTablaCircuitos(conexion);
    
        // Importar datos desde el archivo CSV
        String rutaArchivo = "\\Users\\Kev54\\Documents\\Proyecto PAG\\Archivos\\circuits.csv";
        importarDatosDesdeCSV(conexion, rutaArchivo);
    
        // Mostrar los datos de la tabla circuitos
        mostrarCircuitos(conexion);
    
        App.cerrarConexion();
    }
}