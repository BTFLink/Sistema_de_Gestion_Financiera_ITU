/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import static Connections.DDBBConnection.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BTF
 */
public class Cliente {

    private String nombre, apellido, idUnicoUsuario;
    private int dni, id;
    private boolean activo;
    private LocalDateTime fechaDeRegistro;

    public Cliente() {
    }

    public Cliente(String nombre, String apellido, String idUnicoUsuario, int dni, boolean activo, LocalDateTime fechaDeRegistro) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.idUnicoUsuario = idUnicoUsuario;
        this.dni = dni;
        this.activo = activo;
        this.fechaDeRegistro = fechaDeRegistro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getIdUnicoUsuario() {
        return idUnicoUsuario;
    }

    public void setIdUnicoUsuario(String idUnicoUsuario) {
        this.idUnicoUsuario = idUnicoUsuario;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaDeRegistro() {
        return fechaDeRegistro;
    }

    public void setFechaDeRegistro(LocalDateTime fechaDeRegistro) {
        this.fechaDeRegistro = fechaDeRegistro;
    }

    public int getId() {
        return id;
    }

    public void showData() {
        System.out.println(String.format("""
                                         Nombre Completo: %s
                                         DNI: %d
                                         Estado Activo: %s
                                         UUID: %s
                                         Fecha del Registro: %s
                                         """, nombre + " " + apellido, dni, activo,
                idUnicoUsuario, fechaDeRegistro));
    }

    public boolean registrarCliente() {
        String Query = "INSERT INTO `sistema_financiero`.`cliente` (`nombre`, `apellido`, `dni`, `idUnicoUsuario`) VALUES ('" + nombre + "', '" + apellido + "', '" + dni + "', generar_hex_id());";
        String Respuesta = SendQuery(Query);
        System.out.println("Resultado de registro: " + Respuesta);
        return Respuesta.equals("OK");
    }

    public static List<Cliente> searchListOfClients(int DNI) {
        return searchListofClient("WHERE dni = " + DNI);
    }

    public static List<Cliente> searchListOfClients(String Nombre, String Apellido) {
        return searchListofClient("WHERE nombre LIKE '" + Nombre + "' AND apellido LIKE '" + Apellido + "'");
    }

    public static List<Cliente> searchListOfClients(String UUID) {
        return searchListofClient("WHERE idUnicoUsuario = '" + UUID + "'");
    }

    public static List<Cliente> searchListOfClients(LocalDateTime fecha_registro) {
        return searchListofClient("WHERE fecha_registro = '" + Timestamp.valueOf(fecha_registro) + "'");
    }

    private static List<Cliente> searchListofClient(String WHERE) {
        List<Cliente> clientes = new ArrayList<>();
        String query = "SELECT * FROM cliente " + WHERE;

        try {
            ResultSet rs = fetchData(query);
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setDni(rs.getInt("dni"));
                cliente.setIdUnicoUsuario(rs.getString("idUnicoUsuario"));
                cliente.setActivo(rs.getBoolean("activo"));

                Timestamp timestamp = rs.getTimestamp("fecha_registro");
                if (timestamp != null) {
                    cliente.setFechaDeRegistro(timestamp.toLocalDateTime());
                }

                // Si tenés un campo ID en la tabla:
                cliente.id = rs.getInt("id");

                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // O podrías usar un logger
        }

        return clientes;
    }

    public static Cliente searchOneClient(int DNI) {
        return searchAClient("WHERE dni = " + DNI);
    }

    public static Cliente searchOneClient(String Nombre, String Apellido) {
        return searchAClient("WHERE nombre LIKE '" + Nombre + "' AND apellido LIKE '" + Apellido + "'");
    }

    public static Cliente searchOneClient(String UUID) {
        return searchAClient("WHERE idUnicoUsuario = '" + UUID + "'");
    }

    public static Cliente searchOneClient(LocalDateTime fecha_registro) {
        return searchAClient("WHERE fecha_registro = '" + Timestamp.valueOf(fecha_registro) + "'");
    }

    private static Cliente searchAClient(String WHERE) {

        String query = "SELECT * FROM cliente " + WHERE;
        Cliente cliente = new Cliente();
        try {
            ResultSet rs = fetchData(query);
            if (rs != null) {
                while (rs.next()) {

                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setApellido(rs.getString("apellido"));
                    cliente.setDni(rs.getInt("dni"));
                    cliente.setIdUnicoUsuario(rs.getString("idUnicoUsuario"));
                    cliente.setActivo(rs.getBoolean("activo"));

                    Timestamp timestamp = rs.getTimestamp("fecha_registro");
                    if (timestamp != null) {
                        cliente.setFechaDeRegistro(timestamp.toLocalDateTime());
                    }

                    // Si tenés un campo ID en la tabla:
                    cliente.id = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // O podrías usar un logger
        }

        return cliente;
    }

    public static String solicitarUUID(String nombre, String apellido, int DNI) {
        String query = "SELECT idUnicoUsuario FROM cliente WHERE nombre = '" + nombre + "' AND apellido = '"
                + apellido + "' AND dni = " + DNI + " AND activo = 1";
        String UUID="";
        try {
            ResultSet rs = fetchData(query);
            if(rs!=null && rs.next()){
                UUID=rs.getString("idUnicoUsuario");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return UUID;
    }

}
