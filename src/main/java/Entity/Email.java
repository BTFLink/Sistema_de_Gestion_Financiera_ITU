/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import static Connections.DDBBConnection.SendQuery;
import static Connections.DDBBConnection.fetchData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BTF
 */
public class Email {

    private String cliente_UUID, email;
    private boolean pricipal, activo;

    public Email() {
        cliente_UUID = "";
        email = "";
        pricipal = false;
        activo = false;
    }

    public Email(String email, boolean pricipal, boolean activo) {
        this.email = email;
        this.pricipal = pricipal;
        this.activo = activo;
    }

    public String getCliente_UUID() {
        return cliente_UUID;
    }

    public void setCliente_UUID(String cliente_UUID) {
        this.cliente_UUID = cliente_UUID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPricipal() {
        return pricipal;
    }

    public void setPricipal(boolean pricipal) {
        this.pricipal = pricipal;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void showInformation() {
        System.out.println(String.format("Email: %s\nPrincipal: %s\nActivo: %s\nCodigo del dueño: %s", email, pricipal, activo, cliente_UUID));
    }

    public boolean registrarEmail() {
        if (cliente_UUID.length() != 45 || email == null || email.length() < 5) {
            System.out.println("Datos mínimos no encontrados, cancelando registro de datos");
            return false;
        }
        String query = "INSERT INTO `sistema_financiero`.`email` (`cliente_idUnicoUsuario`, `email`, `principal`) VALUES ('"
                + cliente_UUID + "', '" + email + "', " + pricipal + ");";
        String respuesta = SendQuery(query);
        System.out.println("Resultado de registro: " + respuesta);
        return respuesta.equals("OK");
    }

    public static List<Email> searchListEmailByEmail(String email){
        return searchListEmail("WHERE email = '" + email + "';");
    }
    
    public static List<Email> searchListEmailByUUID(String UUID){
        return searchListEmail("WHERE cliente_idUnicoUsuario = '" + UUID + "';");
    }
    
    public static List<Email> searchListEmail(String WHERE) {
        String query = "SELECT * FROM email "+WHERE;
        List<Email> emails = new ArrayList<>();

        try {
            ResultSet rs = fetchData(query);
            while (rs.next()) {
                Email emailObj = new Email();
                emailObj.setEmail(rs.getString("email"));
                emailObj.setPricipal(rs.getBoolean("principal"));
                emailObj.setActivo(rs.getBoolean("activo"));
                emailObj.setCliente_UUID(rs.getString("cliente_idUnicoUsuario"));
                emails.add(emailObj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return emails;
    }

}
