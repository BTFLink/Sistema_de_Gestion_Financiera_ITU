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
public class Direccion {

    private int id;
    private String cliente_UUID, calle, numeracion, piso, codigo_postal,
            ciudad, departamento, provincia, pais;
    private boolean activo;

    public Direccion() {
        cliente_UUID = "";
        calle = "";
        numeracion = "";
        piso = "";
        codigo_postal = "";
        ciudad = "";
        departamento = "";
        provincia = "";
        pais = "";
        activo = false;
    }

    public Direccion(String cliente_UUID, String calle, String numeracion, String piso, String codigo_postal, String ciudad, String departamento, String provincia, String pais, boolean activo) {
        this.cliente_UUID = cliente_UUID;
        this.calle = calle;
        this.numeracion = numeracion;
        this.piso = piso;
        this.codigo_postal = codigo_postal;
        this.ciudad = ciudad;
        this.departamento = departamento;
        this.provincia = provincia;
        this.pais = pais;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente_UUID() {
        return cliente_UUID;
    }

    public void setCliente_UUID(String cliente_UUID) {
        this.cliente_UUID = cliente_UUID;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean registrarDireccion() {
        if (cliente_UUID.length() != 45 || calle.equals("") || numeracion.equals("") || codigo_postal.equals("")) {
            System.out.println("Datos Minimos no encontrados, cancelando registro de datos");
            return false;
        }
        String Query = "INSERT INTO `sistema_financiero`.`direcciones` "
                + "(`cliente_idUnicoUsuario`, `calle`, `numeracion`, `piso`,"
                + " `codigo_postal`, `ciudad`, `departamento`, `provincia`, `pais`) "
                + "VALUES ('" + cliente_UUID + "', '" + calle + "', '" + numeracion + "', '" + piso
                + "', '" + codigo_postal + "', '" + ciudad + "', '" + departamento + "', '" + provincia
                + "', '" + pais + "');";
        String Respuesta = SendQuery(Query);
        System.out.println("Resultado de registro: " + Respuesta);
        return Respuesta.equals("OK");
    }

    public static List<Direccion> searchDireccionPorUUID(String cliente_UUID) {
        return searchDireccion("WHERE cliente_idUnicoUsuario = '" + cliente_UUID + "'");
    }

    public static List<Direccion> searchDireccionPorCalle(String calle) {
        return searchDireccion("WHERE calle = '" + calle + "'");
    }

    public static List<Direccion> searchDireccionPorNumeracion(String numeracion) {
        return searchDireccion("WHERE numeracion = '" + numeracion + "'");
    }

    public static List<Direccion> searchDireccionPorPiso(String piso) {
        return searchDireccion("WHERE piso = '" + piso + "'");
    }

    public static List<Direccion> searchDireccionPorCodigoPostal(String codigo_postal) {
        return searchDireccion("WHERE codigo_postal = '" + codigo_postal + "'");
    }

    public static List<Direccion> searchDireccionPorCiudad(String ciudad) {
        return searchDireccion("WHERE ciudad = '" + ciudad + "'");
    }

    public static List<Direccion> searchDireccionPorDepartamento(String departamento) {
        return searchDireccion("WHERE departamento = '" + departamento + "'");
    }

    public static List<Direccion> searchDireccionPorProvincia(String provincia) {
        return searchDireccion("WHERE provincia = '" + provincia + "'");
    }

    public static List<Direccion> searchDireccionPorPais(String pais) {
        return searchDireccion("WHERE pais = '" + pais + "'");
    }

    private static List<Direccion> searchDireccion(String WHERE) {
        List<Direccion> direcciones = new ArrayList<>();
        String query = "SELECT * FROM direccion " + WHERE;

        try {
            ResultSet rs = fetchData(query);
            while (rs.next()) {
                Direccion direccion = new Direccion();
                direccion.setId(rs.getInt("id"));
                direccion.setCliente_UUID(rs.getString("cliente_idUnicoUsuario"));
                direccion.setCalle(rs.getString("calle"));
                direccion.setNumeracion(rs.getString("numeracion"));
                direccion.setPiso(rs.getString("piso"));
                direccion.setCodigo_postal(rs.getString("codigo_postal"));
                direccion.setCiudad(rs.getString("ciudad"));
                direccion.setDepartamento(rs.getString("departamento"));
                direccion.setProvincia(rs.getString("provincia"));
                direccion.setPais(rs.getString("pais"));
                direccion.setActivo(rs.getBoolean("activo"));

                direcciones.add(direccion);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // O podrías usar un logger
        }

        return direcciones;
    }

}
