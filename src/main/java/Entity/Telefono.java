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
    private long numero;
    private boolean principal, activo;
    private String cliente_UUID;

    public Telefono() {
        numero = 0L;
        principal = false;
        activo = false;
        cliente_UUID = "";
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
        this.cliente_UUID = UUID;
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

    public String getcliente_UUID() {
        return cliente_UUID;
    }

    public void setcliente_UUID(String UUID) {
        this.cliente_UUID = UUID;
    }
    
    public void showInformation(){
        System.out.println(String.format("Numero de telefono: %d\nPrincipal: %s\nActivo: %s\nCodigo del dueño: %s", numero,principal,activo,cliente_UUID));
    }
    
    public boolean registrarTelefono() {
        if(cliente_UUID.length()!=45 || numero<=999){
            System.out.println("Datos Minimos no encontrados, cancelando registro de datos");
            return false;
        }
        String Query = "INSERT INTO `sistema_financiero`.`telefono` (`cliente_idUnicoUsuario`, `numero`, `principal`) VALUES ('"+cliente_UUID+"', '"+numero+"', "+principal+");";
        String Respuesta = SendQuery(Query);
        System.out.println("Resultado de registro: " + Respuesta);
        return Respuesta.equals("OK");
    }
    
    public static List<Telefono> searchListTelefonoByNumber(long numero){
        return searchListTelefono("WHERE numero = "+numero);
    }
    
    public static List<Telefono> searchListTelefonoByUUID(String UUID){
        return searchListTelefono("WHERE cliente_idUnicoUsuario = '"+UUID+"';");
    }
    
    private static List<Telefono> searchListTelefono(String WHERE){
        String Query = "SELECT * FROM telefono "+WHERE;
        List<Telefono> telefonos = new ArrayList<>();
        
        try {
            ResultSet rs = fetchData(Query);
            while(rs.next()){
                Telefono telefono = new Telefono();
                telefono.setNumero(rs.getLong("numero"));
                telefono.setPrincipal(rs.getBoolean("principal"));
                telefono.setActivo(rs.getBoolean("activo"));
                telefono.setcliente_UUID("cliente_idUnicoUsuario");
                telefonos.add(telefono);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return telefonos;
    }
}
