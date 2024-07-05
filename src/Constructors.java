import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Constructors {

    public static void crearTablaConstructors(Connection conexion) {
        try (Statement statement = conexion.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS constructors ("
                    + "constructorId INTEGER PRIMARY KEY,"
                    + "constructorRef TEXT,"
                    + "name TEXT,"
                    + "nationality TEXT,"
                    + "url TEXT"
                    + ")";
            statement.executeUpdate(sql);
            System.out.println("Tabla constructors creada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla constructors: " + e.getMessage());
        }
    }

    public static void insertarConstructor(Connection conexion, int constructorId, String constructorRef,
                                           String name, String nationality, String url) {
        try (PreparedStatement statement = conexion.prepareStatement("INSERT INTO constructors (constructorId, constructorRef, name, nationality, url) VALUES (?, ?, ?, ?, ?)")) {
            statement.setInt(1, constructorId);
            statement.setString(2, constructorRef);
            statement.setString(3, name);
            statement.setString(4, nationality);
            statement.setString(5, url);
            statement.executeUpdate();
            System.out.println("Registro insertado correctamente en constructors");
        } catch (SQLException e) {
            System.err.println("Error al insertar el registro en constructors: " + e.getMessage());
        }
    }

    public static void mostrarConstructors(Connection conexion) {
        try (Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM constructors")) {

            System.out.println("Datos de la tabla constructors:");

            while (resultSet.next()) {
                int constructorId = resultSet.getInt("constructorId");
                String constructorRef = resultSet.getString("constructorRef");
                String name = resultSet.getString("name");
                String nationality = resultSet.getString("nationality");
                String url = resultSet.getString("url");

                System.out.println("constructorId: " + constructorId
                        + ", constructorRef: " + constructorRef
                        + ", name: " + name
                        + ", nationality: " + nationality
                        + ", url: " + url);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los datos de la tabla constructors: " + e.getMessage());
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

                int constructorId;
                String constructorRef;
                String name;
                String nationality;
                String url;

                try {
                    constructorId = datos[0].equals("\\N") ? 0 : Integer.parseInt(datos[0]);
                    constructorRef = datos[1].equals("\\N") ? "" : datos[1];
                    name = datos[2].equals("\\N") ? "" : datos[2];
                    nationality = datos[3].equals("\\N") ? "" : datos[3];
                    url = datos[4].equals("\\N") ? "" : datos[4];
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir valores en la línea: " + linea);
                    continue;
                }

                insertarConstructor(conexion, constructorId, constructorRef, name, nationality, url);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection conexion = App.obtenerConexion();
        crearTablaConstructors(conexion);

        // Importar datos desde el archivo CSV
        String rutaArchivo = "\\Users\\Kev54\\Documents\\Proyecto PAG\\Archivos\\constructors.csv";
        importarDatosDesdeCSV(conexion, rutaArchivo);

        // Mostrar los datos de la tabla constructors
        mostrarConstructors(conexion);

        App.cerrarConexion();
    }
}