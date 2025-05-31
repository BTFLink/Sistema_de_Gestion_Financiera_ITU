package Connections;

import static Connections.DDBBConnection.fetchData;
import Entity.Cliente;
import Entity.InformesGeneralesEntidad;
import static Utils.ResultSetUtils.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import prestamo.Cuota;
import prestamo.Pago;
import prestamo.Prestamo;

/**
 *
 * @author BTF
 */
public class InformesGeneralesDAO {

    public static void IGCGeneralCliente(boolean conActivo, boolean sinActivo, boolean conMora, boolean sinMora) {
        String query = "SELECT cl.* ";
        if (conActivo) {
            //-- Cantidad de préstamos activos (estaPagado = 0)
            query += ",(SELECT COUNT(*) "
                    + "FROM prestamos pr "
                    + "WHERE pr.cliente_idCliente = cl.idCliente AND pr.estaPagado = 0) "
                    + "AS prestamos_activos ";
        }
        if (sinActivo) {
            //-- Cantidad de préstamos activos (estaPagado = 1)
            query += ",(SELECT COUNT(*) "
                    + "FROM prestamos pr "
                    + "WHERE pr.cliente_idCliente = cl.idCliente AND pr.estaPagado = 1) "
                    + "AS prestamos_inactivos ";
        }
        if (conMora) {
            //-- Cantidad de préstamos con mora (al menos una cuota no pagada y vencida)
            query += ",(SELECT COUNT(DISTINCT pr.idPrestamo) "
                    + "FROM prestamos pr "
                    + "JOIN cuotas c ON c.prestamos_idPrestamo = pr.idPrestamo "
                    + "WHERE pr.cliente_idCliente = cl.idCliente AND c.pagado = 0 AND c.vencimiento < CURDATE()) "
                    + "AS prestamos_con_mora ";
        }
        if (sinMora) {
            //-- Cantidad de préstamos sin mora (todos sus cuotas vencidas están pagadas)
            query += ", (SELECT COUNT(DISTINCT pr.idPrestamo) "
                    + "FROM prestamos pr "
                    + "WHERE pr.cliente_idCliente = cl.idCliente AND "
                    + "NOT EXISTS (SELECT 1 "
                    + "FROM cuotas c "
                    + "WHERE c.prestamos_idPrestamo = pr.idPrestamo AND c.pagado = 0 AND c.vencimiento < CURDATE())"
                    + ") AS prestamos_sin_mora";
        }
        query += "FROM cliente cl";
        List<String> listaDeDatos = new ArrayList();
        try {
            ResultSet rs = fetchData(query);
            if (rs != null) {
                while (rs.next()) {
                    StringBuilder DataBuilder = new StringBuilder();
                    DataBuilder.append(String.format("%-50s ", getStringSafe(rs, "nombre")));
                    DataBuilder.append(String.format("%-15s ", getLongSafe(rs, "telefono")));
                    DataBuilder.append(String.format("%-30s ", getStringSafe(rs, "direccion")));
                    DataBuilder.append(String.format("%-30s ", getStringSafe(rs, "correoElectronico")));
                    DataBuilder.append(conActivo ? String.format("%-10s ", getStringSafe(rs, "prestamos_activos")) : "");
                    DataBuilder.append(sinActivo ? String.format("%-10s ", getStringSafe(rs, "prestamos_inactivos")) : "");
                    DataBuilder.append(conMora ? String.format("%-10s ", getStringSafe(rs, "prestamos_con_mora")) : "");
                    DataBuilder.append(sinMora ? String.format("%-10s ", getStringSafe(rs, "prestamos_sin_mora")) : "");
                    String Datos = DataBuilder.toString();
                    listaDeDatos.add(Datos);
                }
            }
            StringBuilder filtros = new StringBuilder();
            if (conActivo) {
                filtros.append("Activos ");
            }
            if (sinActivo) {
                filtros.append("Inactivos ");
            }
            if (conMora) {
                filtros.append("Con Mora ");
            }
            if (sinMora) {
                filtros.append("Sin Mora ");
            }
            System.out.println(String.format(
                    "%-50s %-15s %-30s %-30s %s",
                    "Nombre", "Telefono", "Direccion", "Email", filtros.toString().trim()
            ));
            for (String Dato : listaDeDatos) {
                System.out.println(Dato);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Inesperado al traer la informacion");
        }
    }

    public static InformesGeneralesEntidad IGCUUID(String UUID, boolean conActivo, boolean sinActivo, boolean conMora, boolean sinMora) {
        InformesGeneralesEntidad IGE = new InformesGeneralesEntidad();

        List<Prestamo> listaPrestamos = new ArrayList<>();
        List<Cuota> listaCuotas = new ArrayList<>();
        List<Pago> listaPagosMora = new ArrayList<>();

        Cliente cliente = new Cliente();

        switch (UUID.substring(0, 4)) {
            case "CLI-":
                cliente = Cliente.searchAClient(UUID);
                if (!cliente.getIdCliente().equals(UUID)) {
                    System.out.println("Cliente No Encontrado");
                    return IGE;
                }
            case "PRE-":

                if (UUID.equals("PRE-*")) {
                    String CLI = "CLI-*";
                    listaPrestamos.addAll(BuscarPrestamos(CLI, conActivo, sinActivo, conMora, sinMora));
                } else if (UUID.contains("PRE-")) {
                    listaPrestamos.add(PrestamosDAO.TraerPrestamo(UUID));
                    if (!listaPrestamos.getFirst().getIdPrestamo().equals(UUID)) {
                        System.out.println("Prestamo No Encontrado");
                        return IGE;
                    }
                } else {
                    listaPrestamos.addAll(BuscarPrestamos(UUID, conActivo, sinActivo, conMora, sinMora));
                }
            case "CUO-":
                if (UUID.equals("CUO-*")) {
                    return IGCUUID("PRE-*", conActivo, sinActivo, conMora, sinMora);
                }
                if (conMora && sinMora) {
                    for (Prestamo prestamo : listaPrestamos) {
                        listaCuotas.addAll(PrestamosDAO.ListaDeCuotas(prestamo.getIdPrestamo()));
                    }
                } else {
                    if (conMora) {
                        List<Cuota> listaCuotasAUX = new ArrayList<>();
                        List<Pago> listaPagosAUX = new ArrayList<>();
                        for (Prestamo prestamo : listaPrestamos) {
                            listaCuotasAUX.addAll(PrestamosDAO.ListaDeCuotas(prestamo.getIdPrestamo()));
                            listaPagosAUX.addAll(PrestamosDAO.ListaDePagos(prestamo.getIdPrestamo()));
                        }
                        if (conMora) {
                            for (Pago pago : listaPagosAUX) {
                                if (pago.isPenalidad()) {
                                    listaPagosMora.add(pago);
                                }
                            }
                            for (Cuota cuota : listaCuotasAUX) {
                                if (!cuota.isPagado() && cuota.getVencimiento().isBefore(LocalDateTime.now())) {
                                    listaCuotas.add(cuota);
                                }
                                if (cuota.isPagado()) {
                                    for (Pago pago : listaPagosMora) {
                                        if (pago.getCuota_idCuota().equals(cuota.getIdCuota())) {
                                            listaCuotas.add(cuota);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (sinMora) {
                        }
                    }
                }

            case "PAG-":
                break;
            default:
        }

        if (conMora && sinMora) { //Sirve para que no descarte ninguna y muestre las cuotas con mora
            for (Prestamo prestamo : listaPrestamos) {
                listaCuotas.addAll(PrestamosDAO.ListaDeCuotas(prestamo.getIdPrestamo()));
                listaPagosMora.addAll(PrestamosDAO.ListaDePagos(prestamo.getIdPrestamo(), conMora));
            }
        } else {
            List<Cuota> listaCuotasAUX = new ArrayList<>();
            List<Pago> listaPagosAUX = new ArrayList<>();
            for (Prestamo prestamo : listaPrestamos) {
                listaCuotasAUX.addAll(PrestamosDAO.ListaDeCuotas(prestamo.getIdPrestamo()));
                listaPagosAUX.addAll(PrestamosDAO.ListaDePagos(prestamo.getIdPrestamo()));
            }
            if (conMora) {
                for (Pago pago : listaPagosAUX) {
                    if (pago.isPenalidad()) {
                        listaPagosMora.add(pago);
                    }
                }
                for (Cuota cuota : listaCuotasAUX) {
                    if (!cuota.isPagado() && cuota.getVencimiento().isBefore(LocalDateTime.now())) {
                        listaCuotas.add(cuota);
                    }
                    if (cuota.isPagado()) {
                        for (Pago pago : listaPagosMora) {
                            if (pago.getCuota_idCuota().equals(cuota.getIdCuota())) {
                                listaCuotas.add(cuota);
                                break;
                            }
                        }
                    }
                }
            }
            if (sinMora) {
            }
        }
        IGE.addListaDeClientes(cliente);
        IGE.addAllListaDePrestamos(listaPrestamos);
        IGE.addAllListaDeCuotas(listaCuotas);
        IGE.addAllListaDePagos(listaPagosMora);
        return IGE;
    }

    private static List<Prestamo> BuscarPrestamos(String CLI, boolean conActivo, boolean sinActivo, boolean conMora, boolean sinMora) {
        List<Prestamo> listaPrestamos = new ArrayList();
        if (((!conActivo && !sinActivo) && (conMora || sinMora)) || (conActivo && sinActivo)) {
            listaPrestamos = PrestamosDAO.ListaDePrestamos(CLI);
        } else {
            if (conActivo) {
                listaPrestamos.addAll(PrestamosDAO.ListaDePrestamos(CLI, false));
            }
            if (sinActivo) {
                listaPrestamos.addAll(PrestamosDAO.ListaDePrestamos(CLI, true));
            }
        }
        return listaPrestamos;
    }

    public static String IGCClienteGeneral() {
        String Query
                = """
                SELECT 
    COUNT(DISTINCT cl.idCliente) AS total_clientes,

    COUNT(DISTINCT CASE WHEN p.estaPagado = 0 THEN cl.idCliente END) AS clientes_con_prestamos_activos,
    COUNT(DISTINCT CASE WHEN p.estaPagado = 1 THEN cl.idCliente END) AS clientes_con_prestamos_inactivos,

    COUNT(DISTINCT CASE 
        WHEN cu.vencimiento < NOW() AND cu.pagado = 0 THEN cl.idCliente 
    END) AS clientes_con_prestamos_en_mora,

    COUNT(DISTINCT CASE 
        WHEN p.idPrestamo IS NOT NULL 
             AND cl.idCliente NOT IN (
                SELECT cliente_idCliente 
                FROM prestamos pr
                JOIN cuotas cu2 ON pr.idPrestamo = cu2.prestamos_idPrestamo
                WHERE cu2.vencimiento < NOW() AND cu2.pagado = 0
             )
        THEN cl.idCliente
    END) AS clientes_con_prestamos_sin_mora
                  
    FROM cliente cl
    LEFT JOIN prestamos p ON cl.idCliente = p.cliente_idCliente
    LEFT JOIN cuotas cu ON p.idPrestamo = cu.prestamos_idPrestamo;
    """;

        StringBuilder Datos = new StringBuilder();
        try {
            ResultSet rs = fetchData(Query);
            if (rs != null) {
                while (rs.next()) {
                    Datos.append(String.format("%s: %s%n", "Cantidad de Clientes", getStringSafe(rs, "total_clientes")));
                    Datos.append(String.format("%s: %s%n", "Clientes con Prestamos Activos", getStringSafe(rs, "clientes_con_prestamos_activos")));
                    Datos.append(String.format("%s: %s%n", "Clientes con Prestamos Finalizados", getStringSafe(rs, "clientes_con_prestamos_inactivos")));
                    Datos.append(String.format("%s: %s%n", "Clientes con Prestamos en Mora", getStringSafe(rs, "clientes_con_prestamos_en_mora")));
                    Datos.append(String.format("%s: %s%n", "Clientes con Prestamos sin Mora", getStringSafe(rs, "clientes_con_prestamos_sin_mora")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Datos.toString();
    }

    public static InformesGeneralesEntidad IGCCliente(String UUID) {
        InformesGeneralesEntidad IGE = new InformesGeneralesEntidad();
        Cliente cliente = Cliente.searchAClient(UUID);
        if (!cliente.getIdCliente().equals(UUID)) {
            System.out.println("No se encontro el cliente solicitado");
            return IGE;
        }

        List<Prestamo> listaPrestamos = PrestamosDAO.ListaDePrestamos(UUID);
        List<Cuota> listaCuotas = new ArrayList(), listaCuotasAUX = new ArrayList();

        for (Prestamo prestamo : listaPrestamos) {
            listaCuotasAUX.addAll(PrestamosDAO.ListaDeCuotas(prestamo.getIdPrestamo()));
        }

        for (Cuota cuota : listaCuotasAUX) {
            if (!cuota.isPagado() && cuota.getVencimiento().isBefore(LocalDateTime.now())) {
                listaCuotas.add(cuota);
            }
        }
        IGE.addListaDeClientes(cliente);
        IGE.addAllListaDeCuotas(listaCuotas);
        IGE.addAllListaDePrestamos(listaPrestamos);
        return IGE;
    }

    public static String IGCPrestamoGeneral() {
        String Query = """
                       SELECT 
                           COUNT(DISTINCT p.idPrestamo) AS cantidad_prestamos,
                       
                           SUM(p.montoPrestamo) AS dinero_prestado,
                       
                           SUM(pg.montopago) AS dinero_recaudado,
                       
                           SUM(CASE 
                               WHEN cu.vencimiento BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 3 MONTH)
                                    AND cu.pagado = 0
                               THEN cu.montoCuota + cu.interesCuota
                               ELSE 0
                           END) AS recaudacion_esperada_trimestre,
                       
                           COUNT(DISTINCT CASE WHEN p.estaPagado = 1 THEN p.idPrestamo END) AS prestamos_completados,
                       
                           COUNT(DISTINCT CASE WHEN p.estaPagado = 0 THEN p.idPrestamo END) AS prestamos_vigentes,
                       
                           COUNT(DISTINCT CASE 
                               WHEN EXISTS (
                                   SELECT 1 
                                   FROM cuotas cu_mora 
                                   WHERE cu_mora.prestamos_idPrestamo = p.idPrestamo 
                                     AND cu_mora.vencimiento < NOW() 
                                     AND cu_mora.pagado = 0
                               )
                               THEN p.idPrestamo
                           END) AS prestamos_con_mora,
                       
                           COUNT(DISTINCT CASE 
                               WHEN NOT EXISTS (
                                   SELECT 1 
                                   FROM cuotas cu_mora 
                                   WHERE cu_mora.prestamos_idPrestamo = p.idPrestamo 
                                     AND cu_mora.vencimiento < NOW() 
                                     AND cu_mora.pagado = 0
                               )
                               THEN p.idPrestamo
                           END) AS prestamos_sin_mora
                       
                       FROM prestamos p
                       LEFT JOIN cuotas cu ON p.idPrestamo = cu.prestamos_idPrestamo
                       LEFT JOIN pagos pg ON cu.idcuota = pg.cuotas_idcuota;
                       """;
        StringBuilder Datos = new StringBuilder();
        try {
            ResultSet rs = fetchData(Query);
            if (rs != null) {
                while (rs.next()) {
                    Datos.append(String.format("%s: %s%n", "Cantidad de Prestamos", getStringSafe(rs, "cantidad_prestamos")));
                    Datos.append(String.format("%s: %s%n", "Total de Dinero Prestado", getStringSafe(rs, "dinero_prestado")));
                    Datos.append(String.format("%s: %s%n", "Total de Dinero Recaudado", getStringSafe(rs, "dinero_recaudado")));
                    Datos.append(String.format("%s: %s%n", "Recaudacion estimada para el siguiente trimestre", getStringSafe(rs, "recaudacion_esperada_trimestre")));
                    Datos.append(String.format("%s: %s%n", "Prestamos Completados", getStringSafe(rs, "prestamos_completados")));
                    Datos.append(String.format("%s: %s%n", "Prestamos Vigentes", getStringSafe(rs, "prestamos_vigentes")));
                    Datos.append(String.format("%s: %s%n", "Prestamos con Mora", getStringSafe(rs, "prestamos_con_mora")));
                    Datos.append(String.format("%s: %s%n", "Prestamos sin Mora", getStringSafe(rs, "prestamos_sin_mora")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Datos.toString();
    }

    public static InformesGeneralesEntidad IGCPrestamo(String UUID) {
        InformesGeneralesEntidad IGE = new InformesGeneralesEntidad();
        Prestamo prestamo = PrestamosDAO.TraerPrestamo(UUID);
        if (!UUID.equals(prestamo.getIdPrestamo())) {
            System.out.println("No se encontro el prestamo solicitado");
            return IGE;
        }
        
        Cliente cliente = Cliente.searchAClient(prestamo.getIdCliente());
        List<Cuota> listaCuotasAUX = PrestamosDAO.ListaDeCuotas(prestamo.getIdPrestamo(), false);
        if (listaCuotasAUX.isEmpty()) {
            IGE.addExtraData("No se ha detectado cuota en mora");
        } else {
            for (Cuota cuota : listaCuotasAUX) {
                if (cuota.getVencimiento().isBefore(LocalDateTime.now())) {
                    IGE.addListaDeCuotas(cuota);
                }
            }
        }
        IGE.addListaDePrestamos(prestamo);
        IGE.addListaDeClientes(cliente);
        return IGE;
    }
    
    public static String IGCCuotaGeneral(){
        String Query = """
                       SELECT
                           COUNT(cu.idcuota) AS cuotas_totales,
                       
                           SUM(CASE 
                                   WHEN cu.vencimiento BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 3 MONTH)
                                   THEN cu.montoCuota + cu.interesCuota 
                                   ELSE 0 
                               END) AS recaudacion_esperada,
                       
                           COUNT(CASE WHEN cu.pagado = 1 THEN 1 END) AS cuotas_pagadas,
                       
                           COUNT(CASE WHEN cu.pagado = 0 THEN 1 END) AS cuotas_impagas,
                       
                           COUNT(CASE 
                                   WHEN cu.pagado = 1 AND p.fechapago > cu.vencimiento THEN 1 
                                END) AS cuotas_pagadas_con_mora,
                       
                           COUNT(CASE 
                                   WHEN cu.pagado = 1 AND p.fechapago <= cu.vencimiento THEN 1 
                                END) AS cuotas_pagadas_sin_mora,
                       
                           COUNT(CASE 
                                   WHEN cu.pagado = 0 AND cu.vencimiento < CURDATE() THEN 1 
                                END) AS cuotas_impagas_con_mora,
                       
                           COUNT(CASE 
                                   WHEN cu.pagado = 0 AND cu.vencimiento >= CURDATE() THEN 1 
                                END) AS cuotas_impagas_sin_mora
                       
                       FROM cuotas cu
                       LEFT JOIN pagos p ON cu.idcuota = p.cuotas_idcuota;
                       """;
        
        StringBuilder Datos = new StringBuilder();
        try {
            ResultSet rs = fetchData(Query);
            if(rs != null){
                while (rs.next()) {
                    Datos.append(String.format("%s: %s%n","Total de Cuotas", getStringSafe(rs, "cuotas_totales")));
                    Datos.append(String.format("%s: %s%n","Recaudacion esperada del siguiente trimestre", getStringSafe(rs, "recaudacion_esperada")));
                    Datos.append(String.format("%s: %s%n","Cuotas Pagadas", getStringSafe(rs, "cuotas_pagadas")));
                    Datos.append(String.format("%s: %s%n","Cuotas Impagas", getStringSafe(rs, "cuotas_impagas")));
                    Datos.append(String.format("%s: %s%n","Cuotas Pagadas con mora", getStringSafe(rs, "cuotas_pagadas_con_mora")));
                    Datos.append(String.format("%s: %s%n","Cuotas Pagadas sin mora", getStringSafe(rs, "cuotas_pagadas_sin_mora")));
                    Datos.append(String.format("%s: %s%n","Cuotas Impagas con mora", getStringSafe(rs, "cuotas_impagas_con_mora")));
                    Datos.append(String.format("%s: %s%n","Cuotas Impagas sin mora", getStringSafe(rs, "cuotas_impagas_sin_mora")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Datos.toString();
    }
    
    public static InformesGeneralesEntidad IGCCuota(String UUID){
        InformesGeneralesEntidad IGE = new InformesGeneralesEntidad();
        
        Cuota cuota = PrestamosDAO.TraerCuota(UUID);
        if(!UUID.equals(cuota.getIdCuota())){
            System.out.println("No se encontró la cuota solicitada");
            return IGE;
        }
        
        Prestamo prestamo = PrestamosDAO.TraerPrestamo(cuota.getIdPrestamo());
        
        if(cuota.isPagado()){
            Pago pago = PrestamosDAO.TraerPago(cuota.getIdCuota());
            IGE.addListaDePagos(pago);
        }
        IGE.addListaDeCuotas(cuota);
        IGE.addListaDePrestamos(prestamo);
        return IGE;
    }
    
    public static String IGCPagoGeneral(){
        String Query ="""
                      
                      """;
        StringBuilder Datos = new StringBuilder();
        try {
            ResultSet rs = fetchData(Query);
            if(rs != null){
                while(rs.next()){
                    Datos.append(String.format("%s: %s%n", "",getStringSafe(rs, "")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Datos.toString();
    }
    
    public static InformesGeneralesEntidad IGCPago(String UUID){
        InformesGeneralesEntidad IGE = new InformesGeneralesEntidad();
        
        Pago pago = PrestamosDAO.TraerPago(UUID);
        if(!UUID.equals(pago.getIdPago())){
            System.out.println("No se encontró el pago solicitado");
            return IGE;
        }
        
        Cuota cuota = PrestamosDAO.TraerCuota(pago.getCuota_idCuota());
        if(cuota.getIdCuota().isEmpty() || cuota.getIdCuota().isBlank()){
            System.out.println("No se encontro la cuota perteneciente del pago");
            return IGE;
        }
        
        Prestamo prestamo = PrestamosDAO.TraerPrestamo(cuota.getIdPrestamo());
        IGE.addListaDePagos(pago);
        IGE.addListaDePrestamos(prestamo);
        return IGE;
    }
}
