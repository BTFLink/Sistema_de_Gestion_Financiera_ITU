/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import static Connections.DDBBConnection.*;
import static Utils.ResultSetUtils.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BTF
 */
public class Cliente {

    private String nombre, idCliente, direccion, correoElectronico;
    private Long telefono;

    public Cliente() {
        this.nombre = "";
        this.idCliente = "";
        this.direccion = "";
        this.correoElectronico = "";
        this.telefono = 0L;
    }

    public Cliente(String nombre, String idCliente, String direccion, String correoElectronico, Long telefono) {
        this.nombre = nombre;
        this.idCliente = idCliente;
        this.direccion = direccion;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public void showClientData(boolean listType) {
        if (idCliente.equals("")) {
            return;
        }
        if (listType) {
            System.out.println(String.format("%-20s | %-50s | %-15s | %-100s | %-255s",
                    this.idCliente, this.nombre, String.valueOf(this.telefono), this.direccion, this.correoElectronico));
        } else {
            System.out.println(String.format("""
                                         UUID:      %s
                                         Nombre:    %s
                                         Telefono:  %s
                                         Direccion: %s
                                         Email:     %s
                                         """, this.idCliente, this.nombre, String.valueOf(this.telefono), this.direccion, this.correoElectronico));
        }
    }

    public boolean registrarCliente() {
        if (nombre.equals("") || direccion.equals("") || telefono <= 1000000000L || telefono >= 6000000000L) {
            System.out.println("Datos Minimos no encontrados, cancelando registro de datos");
            return false;
        }
        String Query = String.format("INSERT INTO `seconddatabase`.`cliente` (`idCliente`, `nombre`, `direccion`, `telefono`, `correoElectronico`) VALUES (generar_idCliente(), '%s', '%s', %d, '%s');", nombre, direccion, telefono, correoElectronico);
        String Respuesta = SendQuery(Query);
        System.out.println("Resultado de registro: " + Respuesta);
        return Respuesta.equals("OK");
    }

    public boolean actualizarCliente() {
        if (nombre.equals("") || direccion.equals("") || telefono <= 1000000000L || telefono >= 6000000000L) {
            System.out.println("Datos Minimos no encontrados, cancelando registro de datos");
            return false;
        }
        String Query = String.format("UPDATE `seconddatabase`.`cliente` SET `nombre` = '%s', `direccion`= '%s', `telefono`= %d, `correoElectronico`= '%s' WHERE idCliente = '%s';", nombre, direccion, telefono, correoElectronico, idCliente);
        String Respuesta = SendQuery(Query);
        System.out.println("Resultado de registro: " + Respuesta);
        return Respuesta.equals("OK");
    }

    public static Cliente searchAClient(String UUID) {

        if (UUID.equals("")) {
            System.out.println("El UUID es invalido");
            return new Cliente();
        }

        String query = "SELECT * FROM cliente WHERE idCliente = '" + UUID + "';";
        Cliente cliente = new Cliente();
        try {
            ResultSet rs = fetchData(query);
            if (rs != null) {
                while (rs.next()) {

                    cliente.setIdCliente(getStringSafe(rs, "idCliente"));
                    cliente.setNombre(getStringSafe(rs, "nombre"));
                    cliente.setDireccion(getStringSafe(rs, "direccion"));
                    cliente.setCorreoElectronico(getStringSafe(rs, "correoElectronico"));
                    cliente.setTelefono(getLongSafe(rs, "telefono"));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    public static void traerTodos() {
        String query = "SELECT * FROM cliente;";
        List<Cliente> listClientes = new ArrayList<>();
        try {
            ResultSet rs = fetchData(query);
            if (rs != null) {
                while (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(getStringSafe(rs, "idCliente"));
                    cliente.setNombre(getStringSafe(rs, "nombre"));
                    cliente.setDireccion(getStringSafe(rs, "direccion"));
                    cliente.setCorreoElectronico(getStringSafe(rs, "correoElectronico"));
                    cliente.setTelefono(getLongSafe(rs, "telefono"));
                    listClientes.add(cliente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("%-20s | %-50s | %-15s | %-100s | %-255s",
                "UUID", "Nombre", "Telefono", "Direccion", "Email"));
        System.out.println("--------------------------------");
        for (Cliente lC : listClientes) {
            lC.showClientData(true);
            System.out.println("--------------------------------");
        }
    }

    public static boolean existeCliente(String UUID) {
        if(!UUID.matches("^CLI-[0-9A-Fa-f]{16}$")){
            System.out.println("Identificador Invalido");
            return false;
        }
        
        String query = "SELECT EXISTS (SELECT 1 FROM usuarios WHERE email = '" + UUID + "') AS existe";
        try {
            ResultSet rs = fetchData(query);
            if (rs.next()) {
                return getBooleanSafe(rs, "existe");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
