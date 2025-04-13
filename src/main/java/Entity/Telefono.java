/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

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
    
    //public void 
    
}
