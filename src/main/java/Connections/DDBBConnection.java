/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 *
 * @author BTF
 */
public abstract class DDBBConnection {

    private static String DB = "localhost";
    private static String URL = "jdbc:mysql://localhost:3306/" + DB
            + "?zeroDateTimeBehavior=round&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static String User = "root";
    private static String Password = "1111";
    private static String Driver = "com.mysql.cj.jdbc.Driver";

    private static Connection Conection;

    private DDBBConnection() {
    }

    protected static Connection Conectar() {
        try {
            Class.forName(Driver);
            Conection = DriverManager.getConnection(URL, User, Password);
            return Conection;
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error en: DDBBConnection \nError: " + ex.getMessage());
            System.out.println("No se conectó a BD " + DB);
        }
        return null;
    }

    protected static void Disconect() {
        try {
            Conection.close();
        } catch (SQLException ex) {
            System.out.println("Conexion terminada");
        }
    }
    
    public static String SendQuery(String query){
        String respuesta="Procesing";
        if (QueryVerification(query)) {
            Conection = Conectar();
            PreparedStatement statement = null;
            boolean result = false;

            try {
                Conection = Conectar();
                statement = Conection.prepareStatement(query);
                result = statement.execute();
                logConnection("Conexión exitosa", query);
                Disconect();
                respuesta="OK";

            } catch (SQLException e) {
                logConnection("Conexión fallida", query);
                e.printStackTrace();
                respuesta=e.getMessage();
            }
        }
        return respuesta;
    }
    /*
    public static ResultSetIES9021 SendQuery(String query) {

        ResultSetIES9021 RSIES9021 = new ResultSetIES9021();
        if (QueryVerification(query)) {
            Conection = Conectar();
            PreparedStatement statement = null;
            boolean result = false;

            try {
                Conection = Conectar();
                statement = Conection.prepareStatement(query);
                result = statement.execute();
                RSIES9021.setState(true);
                RSIES9021.setClarification("Consulta ejecutada satisfactoriamente");
                logConnection("Conexión exitosa", query);
                Disconect();

            } catch (SQLException e) {
                logConnection("Conexión fallida", query);
                e.printStackTrace();
                RSIES9021.setState(false);
                RSIES9021.setClarification(e.getMessage());
            }

        }
        return RSIES9021;
    }
    */
    private static void logConnection(String title, String description) {
        String insertQuery = "INSERT INTO logs (title, description, id_user, date) VALUES (?, ?, ?, ?)";

        try (Connection connection = Conectar(); PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, 6);
            preparedStatement.setString(4, LocalDate.now().toString());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet fetchData(String query) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            connection = Conectar();
            statement = connection.prepareStatement(query);
            result = statement.executeQuery();
            logConnection("Conexión exitosa", query);
            Disconect();

            return result;
        } catch (SQLException e) {
            logConnection("Conexión fallida", query);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * La función getCount recupera el recuento de filas de una tabla
     * especificada en una base de datos, opcionalmente filtrada por una
     * cláusula WHERE.
     *
     * @param tableName El parámetro tableName es una cadena que representa el
     * nombre de la tabla en la base de datos desde la cual desea contar el
     * número de filas.
     * @param whereClause El parámetro `whereClause` es una cadena que
     * representa la condición que se aplicará en la consulta SQL. Se utiliza
     * para filtrar las filas devueltas por la consulta según criterios
     * específicos. Por ejemplo, si `whereClause` es `"edad > 18"`, la consulta
     * solo contará el
     * @return El método devuelve un valor entero, que representa el recuento de
     * filas en la tabla especificada que coinciden con la cláusula donde dada.
     */
    public static int getCount(String tableName, String whereClause) {
        String query = "SELECT COUNT(*) FROM " + tableName;
        if (whereClause != null && !whereClause.isEmpty()) {
            query += " WHERE " + whereClause;
        }
        int conteo = 0;

        ResultSet resultSet = null;
        try {

            resultSet = fetchData(query);
            if (resultSet.next()) {
                conteo = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logConnection("Conexión fallida", query);
        }
        return conteo;
    }

    //Verifica el tipo de Query que se manda
    public static boolean QueryVerification(String query) {
        
        boolean verification = false;
        String selectPattern = "SELECT .* FROM .*";
            String insertPattern = "INSERT INTO .* VALUES .*";
            String updatePattern = "UPDATE .* SET .* WHERE .*";
            String deletePattern = "DELETE FROM .* WHERE .*";

        if (query.matches(selectPattern) || query.matches(insertPattern) || query.matches(updatePattern) || query.matches(deletePattern)) {
            logConnection("Valid Connection attempt", query);
            verification = true;
        } else {
            logConnection("Invalid Connection attempt", query);
        }

        return verification;
    }

    //Cierra la coneccion
    public static void closeResources(Connection connection, PreparedStatement statement, ResultSet result) {
        try {
            if (result != null) {
                result.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
