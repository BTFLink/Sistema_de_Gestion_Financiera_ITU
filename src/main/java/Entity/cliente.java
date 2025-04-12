/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import static Connections.DDBBConnection.*;
import java.sql.ResultSet;
import java.time.LocalDateTime;

/**
 *
 * @author BTF
 */
public class Cliente {
    
    private String nombre, apellido, idUnicoUsuario;
    private int dni,id;
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
    
    public void showData(){
        System.out.println(String.format("""
                                         Nombre Completo: %s
                                         DNI: %d
                                         Estado Activo: %s
                                         UUID: %s
                                         Fecha del Registro: %s
                                         """, nombre+" "+apellido,dni,activo,
                                         idUnicoUsuario,fechaDeRegistro));
    }
    
    public boolean registrarCliente(){
        String Query= "INSERT INTO `sistema_financiero`.`cliente` (`nombre`, `apellido`, `dni`, `idUnicoUsuario`) VALUES ('"+nombre+"', '"+apellido+"', '"+dni+"', generar_hex_id());";
        String Respuesta=SendQuery(Query);
        System.out.println("Resultado de registro: "+Respuesta);
        return Respuesta.equals("OK");
    }
    
    public void SearchCliente(String IDU){
        try {
            ResultSet Search = fetchData(IDU);
            
            
        } catch (Exception e) {
        }
    }
}
