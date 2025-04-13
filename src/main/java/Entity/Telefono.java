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
public class Telefono {
    long numero;
    boolean principal, activo;
    String UUID;

    public Telefono() {
    }
    
    public Telefono(long numero, boolean principal, boolean activo) {
        this.numero = numero;
        this.principal = principal;
        this.activo = activo;
    }

    public Telefono(long numero, boolean principal, boolean activo, String UUID) {
        this.numero = numero;
        this.principal = principal;
        this.activo = activo;
        this.UUID = UUID;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
    
    public void showInformation(){
        System.out.println(String.format("Numero de telefono: %d\nPrincipal: %s\nActivo: %s\nCodigo del dueño: %s", numero,principal,activo,UUID));
    }
    
    public boolean registrarTelefono() {
        String Query = "INSERT INTO `sistema_financiero`.`telefono` (`cliente_idUnicoUsuario`, `telefono`, `principal`) VALUES ('"+UUID+"', '"+numero+"', "+principal+");";
        String Respuesta = SendQuery(Query);
        System.out.println("Resultado de registro: " + Respuesta);
        return Respuesta.equals("OK");
    }
    
    public static List<Telefono> searchListTelefono(String UUID){
        String Query = "SELECT * FROM telefono WHERE cliente_idUnicoUsuario = '"+UUID+"';";
        List<Telefono> telefonos = new ArrayList<>();
        
        try {
            ResultSet rs = fetchData(Query);
            while(rs.next()){
                Telefono telefono = new Telefono();
                telefono.setNumero(rs.getLong("numero"));
                telefono.setPrincipal(rs.getBoolean("principal"));
                telefono.setActivo(rs.getBoolean("activo"));
                telefono.setUUID(UUID);
                telefonos.add(telefono);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return telefonos;
    }
}
