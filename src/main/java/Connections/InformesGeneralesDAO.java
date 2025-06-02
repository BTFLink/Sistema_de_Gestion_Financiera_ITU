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
                       
                           (SELECT SUM(montoPrestamo) FROM prestamos) AS dinero_prestado,
                       
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
        boolean tieneCuotasVencidas = false;
        for (Cuota cuota : listaCuotasAUX) {
            if (cuota.getVencimiento().isBefore(LocalDateTime.now())) {
                IGE.addListaDeCuotas(cuota);
                tieneCuotasVencidas = true;
            }
        }
        if (!tieneCuotasVencidas && !listaCuotasAUX.isEmpty()) {
            IGE.addExtraData("Cuotas sin mora detectadas");
        }
        IGE.addListaDePrestamos(prestamo);
        IGE.addListaDeClientes(cliente);
        return IGE;
    }

    public static String IGCCuotaGeneral() {
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
            if (rs != null) {
                while (rs.next()) {
                    Datos.append(String.format("%s: %s%n", "Total de Cuotas", getStringSafe(rs, "cuotas_totales")));
                    Datos.append(String.format("%s: %s%n", "Recaudacion esperada del siguiente trimestre", getStringSafe(rs, "recaudacion_esperada")));
                    Datos.append(String.format("%s: %s%n", "Cuotas Pagadas", getStringSafe(rs, "cuotas_pagadas")));
                    Datos.append(String.format("%s: %s%n", "Cuotas Impagas", getStringSafe(rs, "cuotas_impagas")));
                    Datos.append(String.format("%s: %s%n", "Cuotas Pagadas con mora", getStringSafe(rs, "cuotas_pagadas_con_mora")));
                    Datos.append(String.format("%s: %s%n", "Cuotas Pagadas sin mora", getStringSafe(rs, "cuotas_pagadas_sin_mora")));
                    Datos.append(String.format("%s: %s%n", "Cuotas Impagas con mora", getStringSafe(rs, "cuotas_impagas_con_mora")));
                    Datos.append(String.format("%s: %s%n", "Cuotas Impagas sin mora", getStringSafe(rs, "cuotas_impagas_sin_mora")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Datos.toString();
    }

    public static InformesGeneralesEntidad IGCCuota(String UUID) {
        InformesGeneralesEntidad IGE = new InformesGeneralesEntidad();

        Cuota cuota = PrestamosDAO.TraerCuota(UUID);
        if (!UUID.equals(cuota.getIdCuota())) {
            System.out.println("No se encontró la cuota solicitada");
            return IGE;
        }

        Prestamo prestamo = PrestamosDAO.TraerPrestamo(cuota.getIdPrestamo());

        if (cuota.isPagado()) {
            Pago pago = PrestamosDAO.TraerPago(cuota.getIdCuota());
            IGE.addListaDePagos(pago);
        }
        IGE.addListaDeCuotas(cuota);
        IGE.addListaDePrestamos(prestamo);
        return IGE;
    }

    public static String IGCPagoGeneral() {
        String Query = """
                      SELECT
                          COUNT(p.id) AS Pagos_Totales,
                          SUM(p.montopago) AS Total_Recaudado,
                          SUM(CASE WHEN p.fechapago > c.vencimiento THEN 1 ELSE 0 END) AS Pagos_en_Mora,
                          SUM(CASE WHEN p.fechapago <= c.vencimiento THEN 1 ELSE 0 END) AS Pagos_sin_Mora
                      FROM pagos p
                      JOIN cuotas c ON p.cuotas_idcuota = c.idcuota;
                      """;
        StringBuilder Datos = new StringBuilder();
        try {
            ResultSet rs = fetchData(Query);
            if (rs != null) {
                while (rs.next()) {
                    Datos.append(String.format("%s: %s%n", "Pagos Totales", getStringSafe(rs, "Pagos_Totales")));
                    Datos.append(String.format("%s: %s%n", "Total Recaudado", getStringSafe(rs, "Total_Recaudado")));
                    Datos.append(String.format("%s: %s%n", "Pagos en mora", getStringSafe(rs, "Pagos_en_Mora")));
                    Datos.append(String.format("%s: %s%n", "Pagos sin mora", getStringSafe(rs, "Pagos_sin_mora")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Datos.toString();
    }

    public static InformesGeneralesEntidad IGCPago(String UUID) {
        InformesGeneralesEntidad IGE = new InformesGeneralesEntidad();

        Pago pago = PrestamosDAO.TraerPago(UUID);
        if (!UUID.equals(pago.getIdPago())) {
            System.out.println("No se encontró el pago solicitado");
            return IGE;
        }

        Cuota cuota = PrestamosDAO.TraerCuota(pago.getCuota_idCuota());
        if (cuota.getIdCuota().isEmpty() || cuota.getIdCuota().isBlank()) {
            System.out.println("No se encontro la cuota perteneciente del pago");
            return IGE;
        }

        Prestamo prestamo = PrestamosDAO.TraerPrestamo(cuota.getIdPrestamo());
        IGE.addListaDePagos(pago);
        IGE.addListaDePrestamos(prestamo);
        return IGE;
    }
}
