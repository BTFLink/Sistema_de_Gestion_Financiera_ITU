/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Connections;

import static Connections.DDBBConnection.*;
import static Utils.ResultSetUtils.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import prestamo.Cuota;
import prestamo.Prestamo;
import prestamo.Pago;

/**
 *
 * @author BTF
 */
public class PrestamosDAO {

    public PrestamosDAO() {
    }

    
    //Seccion Pagos
    public static boolean CreatePrestamoDB(Prestamo prestamo) {

        String query = String.format("INSERT INTO prestamos (`cliente_idCliente`, `idPrestamo`, `tipoPrestamo`, `montoPrestamo`, `cuotasPrestamo`, `cuotasFijas`, `interesInicial`, `fechaPedido`) "
                + "VALUES ('"+prestamo.getIdCliente()+"', generar_idPrestamo(), '"+prestamo.getTipoPrestamo()+"', '"+prestamo.getMonto()+"', '"+prestamo.getNumeroCuotas()+"', '"+(prestamo.isTipoCuota() ? 1:0)+"', '"+prestamo.getInteresInicial()+"', CURRENT_TIMESTAMP());");
        String Respuesta = SendQuery(query);
        System.out.println("Resultado de registro: " + Respuesta);
        return Respuesta.equals("OK");
    }

    public static Prestamo TraerPrestamo(String UUID){
        if(!UUID.contains("PRE-") || UUID.equals("PRE-*")){
            return new Prestamo();
        }
        String Query = "SELECT * FROM `prestamos` WHERE idPrestamo = '"+UUID+"'";
        Prestamo prestamo = new Prestamo();
        try {
            ResultSet rs = fetchData(Query);
            if(rs != null){
                while (rs.next()) {                    
                    prestamo.setIdCliente(getStringSafe(rs, "cliente_idCliente"));
                    prestamo.setIdPrestamo(getStringSafe(rs, "idPrestamo"));
                    prestamo.setMonto(getDoubleSafe(rs, "montoPrestamo"));
                    prestamo.setInteresInicial(getDoubleSafe(rs, "interesInicial"));
                    prestamo.setNumeroCuotas(getIntSafe(rs, "cuotasPrestamo"));
                    prestamo.setTipoCuota(getBooleanSafe(rs, "cuotasFijas"));
                    prestamo.setTipoPrestamo(getBooleanSafe(rs, "tipoPrestamo") ? "PERSONAL":"HIPOTECARIO");
                    prestamo.setEstaPagado(getBooleanSafe(rs, "estaPagado"));
                    prestamo.setFechaCreacion(getLocalDateTimeSafe(rs, "fechaPedido"));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamo;
    }
    
    public static List<Prestamo> ListaDePrestamos(String cliente, boolean pagado) {
        String WhereID = "WHERE cliente_idCliente = '" + cliente + "' AND";
        if (cliente.equals("CLI-*") || cliente.equals("PRE-*")) {
            WhereID = "WHERE";
        }
        String Query = "SELECT * FROM `prestamos` " + WhereID + " estaPagado = " + pagado ;
        List<Prestamo> ListPrestamo = new ArrayList<>();
        try {
            ResultSet rs = fetchData(Query);
            if (rs != null) {
                while (rs.next()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setIdCliente(getStringSafe(rs, "cliente_idCliente"));
                    prestamo.setIdPrestamo(getStringSafe(rs, "idPrestamo"));
                    prestamo.setTipoPrestamo(getStringSafe(rs, "tipoPrestamo"));
                    prestamo.setTipoCuota(getBooleanSafe(rs, "cuotasFijas"));
                    prestamo.setNumeroCuotas(getIntSafe(rs, "cuotasPrestamo"));
                    prestamo.setInteresInicial(getDoubleSafe(rs, "interesInicial"));
                    prestamo.setMonto(getDoubleSafe(rs, "montoPrestamo"));
                    prestamo.setFechaCreacion(getLocalDateTimeSafe(rs, "fechaPedido"));
                    prestamo.setEstaPagado(getBooleanSafe(rs, "estaPagado"));
                    ListPrestamo.add(prestamo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ListPrestamo;
    }

    public static List<Prestamo> ListaDePrestamos(String cliente) {
        String WhereID = "WHERE cliente_idCliente = '" + cliente + "'";
        if (cliente.equals("CLI-*") || cliente.equals("PRE-*")) {
            WhereID = "";
        }
        String Query = "SELECT * FROM `prestamos` " + WhereID;
        List<Prestamo> ListPrestamo = new ArrayList<>();
        try {
            ResultSet rs = fetchData(Query);
            if (rs != null) {
                while (rs.next()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setIdCliente(getStringSafe(rs, "cliente_idCliente"));
                    prestamo.setIdPrestamo(getStringSafe(rs, "idPrestamo"));
                    prestamo.setTipoPrestamo(getStringSafe(rs, "tipoPrestamo"));
                    prestamo.setTipoCuota(getBooleanSafe(rs, "cuotasFijas"));
                    prestamo.setNumeroCuotas(getIntSafe(rs, "cuotasPrestamo"));
                    prestamo.setInteresInicial(getDoubleSafe(rs, "interesInicial"));
                    prestamo.setMonto(getDoubleSafe(rs, "montoPrestamo"));
                    prestamo.setFechaCreacion(getLocalDateTimeSafe(rs, "fechaPedido"));
                    prestamo.setEstaPagado(getBooleanSafe(rs, "estaPagado"));
                    ListPrestamo.add(prestamo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ListPrestamo;
    }

    
    //Seccion Cuotas
    public static Cuota TraerCuota(String UUID){
        if(!UUID.contains("CUO-") || UUID.equals("CUO-*")){
            return new Cuota();
        }
        String Query = "SELECT * FROM `cuotas` WHERE idcuota = '"+UUID+"'";
        Cuota cuota = new Cuota();
        try {
            ResultSet rs = fetchData(Query);
            if(rs != null){
                while (rs.next()) {
                    cuota.setIdCuota(getStringSafe(rs, "idcuota"));
                    cuota.setIdPrestamo(getStringSafe(rs, "prestamos_idPrestamo"));
                    cuota.setInteres(getDoubleSafe(rs, "interesCuota"));
                    cuota.setMonto(getDoubleSafe(rs, "montoCuota"));
                    cuota.setPagado(getBooleanSafe(rs, "pagado"));
                    cuota.setVencimiento(getLocalDateTimeSafe(rs, "vencimiento"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cuota;
    }
    
    public static List<Cuota> ListaDeCuotas(String prestamo, boolean pagado) {
        String WhereID = "WHERE prestamos_idPrestamo = '" + prestamo + "' AND";
        if (prestamo.equals("PRE-*") || prestamo.equals("CUO-*")) {
            WhereID = "WHERE";
        }
        String Query = "SELECT * FROM `cuotas` " + WhereID + " pagado = " + pagado;
        List<Cuota> listCuotas = new ArrayList<>();
        try {
            ResultSet rs = fetchData(Query);
            if (rs != null) {
                while (rs.next()) {
                    Cuota cuota = new Cuota();
                    cuota.setIdPrestamo(getStringSafe(rs, "prestamos_idPrestamo"));
                    cuota.setIdCuota(getStringSafe(rs, "idcuota"));
                    cuota.setMonto(getDoubleSafe(rs, "montoCuota"));
                    cuota.setInteres(getDoubleSafe(rs, "interesCuota"));
                    cuota.setVencimiento(getLocalDateTimeSafe(rs, "vencimiento"));
                    cuota.setPagado(getBooleanSafe(rs, "pagado"));
                    listCuotas.add(cuota);
                }
            }
        } catch (SQLException e) {
        }
        return listCuotas;
    }

    public static List<Cuota> ListaDeCuotas(String prestamo) {
        String WhereID = "WHERE prestamos_idPrestamo = '" + prestamo + "'";
        if (prestamo.equals("PRE-*") || prestamo.equals("CUO-*")) {
            WhereID = "";
        }
        String Query = "SELECT * FROM `cuotas` " + WhereID;
        List<Cuota> listCuotas = new ArrayList<>();
        try {
            ResultSet rs = fetchData(Query);
            if (rs != null) {
                while (rs.next()) {
                    Cuota cuota = new Cuota();
                    cuota.setIdPrestamo(getStringSafe(rs, "prestamos_idPrestamo"));
                    cuota.setIdCuota(getStringSafe(rs, "idcuota"));
                    cuota.setMonto(getDoubleSafe(rs, "montoCuota"));
                    cuota.setInteres(getDoubleSafe(rs, "interesCuota"));
                    cuota.setVencimiento(getLocalDateTimeSafe(rs, "vencimiento"));
                    cuota.setPagado(getBooleanSafe(rs, "pagado"));
                    listCuotas.add(cuota);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listCuotas;
    }

    public static boolean RegistrarPago(Cuota cuota) {
        String Query = "UPDATE `thirdbase`.`cuotas` SET `pagado` = 1 WHERE idCuota = '" + cuota.getIdCuota() + "';";
        String Respuesta = SendQuery(Query);
        System.out.println("Resultado de registro: " + Respuesta);
        return Respuesta.equals("OK");
    }

    //Seccion De Pagos
    public static Pago TraerPago(String UUID){
        String Query = "SELECT * FROM `pagos` WHERE ";
        switch (UUID.substring(0, 4)) {
            case "CUO-":
                if(UUID.equals("CUO-*")){
                    return new Pago();
                }
                Query += "cuotas_idcuota = '"+UUID+"'";
                break;
            case "PAG-":
                if(UUID.equals("PAG-*")){
                    return new Pago();
                }
                Query += "idpago = '"+UUID+"'";
                break;
            default:
                return new Pago();
        }
        Pago pago = new Pago();
        try {
            ResultSet rs = fetchData(Query);
            if(rs!= null){
                while (rs.next()) {
                    pago.setCuota_idCuota(getStringSafe(rs, "cuotas_idcuota"));
                    pago.setIdPago(getStringSafe(rs, "idpago"));
                    pago.setMontoPagado(getDoubleSafe(rs, "montopago"));
                    pago.setPenalidad(getBooleanSafe(rs, "penalidad"));
                    pago.setFechaPago(getLocalDateTimeSafe(rs, "fechapago"));
                    pago.setNumeroCuota(0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pago;
    }
    
    public static List<Pago> ListaDePagos(String idPrestamo) {
        String WHERE = (idPrestamo.equals("PRE-*")  || idPrestamo.equals("PAG-*")) ? "" : "WHERE pr.idPrestamo = '" + idPrestamo + "';";
        String Query = "SELECT p.* FROM pagos p "
                + "JOIN cuotas c ON p.cuotas_idcuota = c.idCuota "
                + "JOIN prestamos pr ON pr.idPrestamo = c.prestamos_idPrestamo " + WHERE;
        List<Pago> listaDePagos = new ArrayList<>();
        try {
            ResultSet rs = fetchData(Query);
            int i=1;
            if (rs != null) {
                while (rs.next()) {
                    Pago pago = new Pago();
                    pago.setCuota_idCuota(getStringSafe(rs, "cuotas_idCuota"));
                    pago.setIdPago(getStringSafe(rs, "idPago"));
                    pago.setMontoPagado(getDoubleSafe(rs, "montopago"));
                    pago.setPenalidad(getBooleanSafe(rs, "penalidad"));
                    pago.setFechaPago(getLocalDateTimeSafe(rs, "fechapago"));
                    pago.setNumeroCuota(i);
                    listaDePagos.add(pago);
                    if(!idPrestamo.equals("PRE-*")){i++;}
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaDePagos;
    }

    public static List<Pago> ListaDePagos(String idPrestamo, boolean mora) {
        String WHERE = (idPrestamo.equals("PRE-*")  || idPrestamo.equals("PAG-*")) ? "" : "WHERE pr.idPrestamo = '" + idPrestamo + "'";
        String Query = "SELECT p.* FROM pagos p "
                + "JOIN cuotas c ON p.cuotas_idcuota = c.idCuota "
                + "JOIN prestamos pr ON pr.idPrestamo = c.prestamos_idPrestamo " + WHERE;
        Query += " AND p.penalidad = "+mora;
        List<Pago> listaDePagos = new ArrayList<>();
        try {
            ResultSet rs = fetchData(Query);
            int i=1;
            if (rs != null) {
                while (rs.next()) {
                    Pago pago = new Pago();
                    pago.setCuota_idCuota(getStringSafe(rs, "cuotas_idCuota"));
                    pago.setIdPago(getStringSafe(rs, "idPago"));
                    pago.setMontoPagado(getDoubleSafe(rs, "montopago"));
                    pago.setPenalidad(getBooleanSafe(rs, "penalidad"));
                    pago.setFechaPago(getLocalDateTimeSafe(rs, "fechapago"));
                    pago.setNumeroCuota(i);
                    listaDePagos.add(pago);
                    if(!idPrestamo.equals("PRE-*")){i++;}
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaDePagos;
    }
}
