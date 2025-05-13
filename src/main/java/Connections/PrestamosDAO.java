/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Connections;

import static Connections.DDBBConnection.*;
import static Utils.ResultSetUtils.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import prestamo.Cuota;
import prestamo.NewPrestamo;

/**
 *
 * @author BTF
 */
public class PrestamosDAO {

    public PrestamosDAO() {
    }

    public static boolean CreatePrestamoDB(NewPrestamo prestamo) {

        String query = String.format("INSERT INTO `thirdbase`.`prestamos` (`cliente_idCliente`, `idPrestamo`, `tipoPrestamo`, `montoPrestamo`, `cuotasPrestamo`, `interesFijo`, `fechaPedido`, `estaPagado`) "
                + "VALUES ('IDC', generar_idPrestamo(), 'TP', 'MP', 'CP', 'IF', CURRENT_TIMESTAMP(), 'EP');");
        String Respuesta = SendQuery(query);
        System.out.println("Resultado de registro: " + Respuesta);
        return Respuesta.equals("OK");
    }

    public static List<NewPrestamo> ListaDePrestamos(String cliente, boolean pagado) {
        String WhereID = "WHERE cliente_idCliente = '" + cliente + "' AND";
        if (cliente.equals("CLI-*")) {
            WhereID = "WHERE";
        }
        String Query = "SELECT * FROM `prestamos` " + WhereID + " estaPagado = '" + pagado + "'";
        List<NewPrestamo> ListPrestamo = new ArrayList<>();
        try {
            ResultSet rs = fetchData(Query);
            if (rs != null) {
                while (rs.next()) {
                    NewPrestamo prestamo = new NewPrestamo();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ListPrestamo;
    }

    public static List<NewPrestamo> ListaDePrestamos(String cliente) {
        String WhereID = "WHERE cliente_idCliente = '" + cliente + "'";
        if (!cliente.equals("CLI-*")) {
            WhereID = "";
        }
        String Query = "SELECT * FROM `prestamos` " + WhereID;
        List<NewPrestamo> ListPrestamo = new ArrayList<>();
        try {
            ResultSet rs = fetchData(Query);
            if (rs != null) {
                while (rs.next()) {
                    NewPrestamo prestamo = new NewPrestamo();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ListPrestamo;
    }

    public static List<Cuota> ListaDeCuotas(String prestamo, boolean pagado) {
        String WhereID = "WHERE prestamo_idPrestamo = '" + prestamo + "' AND";
        if (prestamo.equals("PRE-*")) {
            WhereID = "WHERE";
        }
        String Query = "SELECT * FROM `prestamos` " + WhereID + " estaPagado = '" + pagado + "'";
        List<Cuota> listCuotas = new ArrayList<>();
        try {
            ResultSet rs = fetchData(Query);
            if (rs != null) {
                while (rs.next()) {
                    Cuota cuota = new Cuota();
                    cuota.setIdPrestamo(getStringSafe(rs, "prestamo_idPrestamo"));
                    cuota.setIdCuota(getStringSafe(rs, "idCuota"));
                    cuota.setMonto(getDoubleSafe(rs, "montoCuota"));
                    cuota.setInteres(getDoubleSafe(rs, "interesCuota"));
                    cuota.setVencimiento(getLocalDateTimeSafe(rs, "vencimiento"));
                    cuota.setPagado(getBooleanSafe(rs, "pagado"));
                    listCuotas.add(cuota);
                }
            }
        } catch (Exception e) {
        }
        return listCuotas;
    }

    public static List<Cuota> ListaDeCuotas(String prestamo) {
        String WhereID = "WHERE prestamo_idPrestamo = '" + prestamo + "'";
        if (!prestamo.equals("PRE-*")) {
            WhereID = "";
        }
        String Query = "SELECT * FROM `prestamos` " + WhereID;
        List<Cuota> listCuotas = new ArrayList<>();
        try {
            ResultSet rs = fetchData(Query);
            if (rs != null) {
                while (rs.next()) {
                    Cuota cuota = new Cuota();
                    cuota.setIdPrestamo(getStringSafe(rs, "prestamo_idPrestamo"));
                    cuota.setIdCuota(getStringSafe(rs, "idCuota"));
                    cuota.setMonto(getDoubleSafe(rs, "montoCuota"));
                    cuota.setInteres(getDoubleSafe(rs, "interesCuota"));
                    cuota.setVencimiento(getLocalDateTimeSafe(rs, "vencimiento"));
                    cuota.setPagado(getBooleanSafe(rs, "pagado"));
                    listCuotas.add(cuota);
                }
            }
        } catch (Exception e) {
        }
        return listCuotas;
    }
    
    public static boolean RegistrarPago(Cuota cuota){
        String Query="UPDATE `thirdbase`.`cuotas` SET `pagado` = 1 WHERE idCuota = '"+cuota.getIdCuota()+"';";
        String Respuesta = SendQuery(Query);
        System.out.println("Resultado de registro: " + Respuesta);
        return Respuesta.equals("OK");
    }
}
