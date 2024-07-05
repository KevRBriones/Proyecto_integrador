import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Drivers {

    public static void crearTablaDrivers(Connection conexion) {
        try (Statement statement = conexion.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS drivers ("
                    + "driverId INTEGER PRIMARY KEY,"
                    + "driverRef TEXT,"
                    + "number INTEGER,"
                    + "code TEXT,"
                    + "forename TEXT,"
                    + "surname TEXT,"
                    + "dob TEXT," // Asumiendo que la fecha de nacimiento está en formato de texto
                    + "nationality TEXT,"
                    + "url TEXT"
                    + ")";
            statement.executeUpdate(sql);
            System.out.println("Tabla drivers creada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla drivers: " + e.getMessage());
        }
    }

    public static void insertarDriver(Connection conexion, int driverId, String driverRef, int number, String code,
                                      String forename, String surname, String dob, String nationality, String url) {
        try (PreparedStatement statement = conexion.prepareStatement("INSERT INTO drivers (driverId, driverRef, number, code, forename, surname, dob, nationality, url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, driverId);
            statement.setString(2, driverRef);
            statement.setInt(3, number);
            statement.setString(4, code);
            statement.setString(5, forename);
            statement.setString(6, surname);
            statement.setString(7, dob);
            statement.setString(8, nationality);
            statement.setString(9, url);
            statement.executeUpdate();
            System.out.println("Registro insertado correctamente en drivers");
        } catch (SQLException e) {
            System.err.println("Error al insertar el registro en drivers: " + e.getMessage());
        }
    }

    public static void mostrarDrivers(Connection conexion) {
        try (Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM drivers")) {

            System.out.println("Datos de la tabla drivers:");

            while (resultSet.next()) {
                int driverId = resultSet.getInt("driverId");
                String driverRef = resultSet.getString("driverRef");
                int number = resultSet.getInt("number");
                String code = resultSet.getString("code");
                String forename = resultSet.getString("forename");
                String surname = resultSet.getString("surname");
                String dob = resultSet.getString("dob");
                String nationality = resultSet.getString("nationality");
                String url = resultSet.getString("url");

                System.out.println("driverId: " + driverId
                        + ", driverRef: " + driverRef
                        + ", number: " + number
                        + ", code: " + code
                        + ", forename: " + forename
                        + ", surname: " + surname
                        + ", dob: " + dob
                        + ", nationality: " + nationality
                        + ", url: " + url);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los datos de la tabla drivers: " + e.getMessage());
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

                int driverId;
                String driverRef;
                int number;
                String code;
                String forename;
                String surname;
                String dob;
                String nationality;
                String url;

                try {
                    driverId = datos[0].equals("\\N") ? 0 : Integer.parseInt(datos[0]);
                    driverRef = datos[1].equals("\\N") ? "" : datos[1];
                    number = datos[2].equals("\\N") ? 0 : Integer.parseInt(datos[2]);
                    code = datos[3].equals("\\N") ? "" : datos[3];
                    forename = datos[4].equals("\\N") ? "" : datos[4];
                    surname = datos[5].equals("\\N") ? "" : datos[5];
                    dob = datos[6].equals("\\N") ? "" : datos[6];
                    nationality = datos[7].equals("\\N") ? "" : datos[7];
                    url = datos[8].equals("\\N") ? "" : datos[8];
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir valores en la línea: " + linea);
                    continue;
                }

                insertarDriver(conexion, driverId, driverRef, number, code, forename, surname, dob, nationality, url);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection conexion = App.obtenerConexion();
        crearTablaDrivers(conexion);

        // Importar datos desde el archivo CSV
        String rutaArchivo = "\\Users\\Kev54\\Documents\\Proyecto PAG\\Archivos\\drivers.csv";
        importarDatosDesdeCSV(conexion, rutaArchivo);

        // Mostrar los datos de la tabla drivers
        mostrarDrivers(conexion);

        App.cerrarConexion();
    }
}