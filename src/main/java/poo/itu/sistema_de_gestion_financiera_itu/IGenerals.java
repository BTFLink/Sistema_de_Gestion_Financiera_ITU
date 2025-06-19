/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo.itu.sistema_de_gestion_financiera_itu;

import Connections.Exportador;
import Connections.InformesGeneralesDAO;
import Connections.PrestamosDAO;
import Entity.Cliente;
import Entity.InformesGeneralesEntidad;
import Utils.LeerDataType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import prestamo.Cuota;
import prestamo.Pago;
import prestamo.Prestamo;

/**
 *
 * @author BTF
 */
public class IGenerals {

    private Scanner sc = new Scanner(System.in);
    private String menu, respuesta;
    private boolean exportar;

    public static void main() {
        IGenerals ig = new IGenerals();
        ig.MenuInformesGenerales();

    }

    public IGenerals() {
        this.exportar = false;
    }

    public void MenuInformesGenerales() {
        do {
            menu = """
                    \tMenu de Informes
                    1) Informes con Codigo Identificador
                    2) Exportador de Tablas
                    3) Modo de exportacion: %s
                    0) Salir
                    """;
            System.out.printf(menu, exportar ? "Activado" : "Desactivado");
            respuesta = "";
            respuesta = sc.nextLine();
            switch (respuesta) {
                case "1":
                    InformeConCodigo();
                    break;
                case "2":
                    InformeGeneral();
                    break;
                case "3":
                    exportar = !exportar;
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opcion Invalida");
            }
        } while (true);
    }

    private void InformeConCodigo() { //ICC
        do {
            menu = """
                 Ingrese el codigo de informe que desea obtener
                 \"CLI-\" Informes de usuarios
                 \"PRE-\" Informes de prestamos
                 \"CUO-\" Informes de cuotas
                 \"PAG-\" Informes de pagos
                 \"EXT-\" Salir
                 """;
            System.out.println(menu);
            respuesta = "";
            respuesta = LeerDataType.LeerUUID(sc.nextLine());

            if (respuesta.length() >= 4) {
                switch (respuesta.substring(0, 4)) {
                    case "CLI-":
                        ICCCliente(respuesta);
                        return;
                    case "PRE-":
                        ICCPrestamo(respuesta);
                        return;
                    case "CUO-":
                        ICCCuota(respuesta);
                        return;
                    case "PAG-":
                        ICCPago(respuesta);
                        return;
                    case "EXT-":
                        return;
                    default:
                        System.out.println("Opcion Invalida");
                }
            }else{System.out.println("Codigo Invalido");}

        } while (true);
    }

    /*
    
    Codigo inutilizado por LeerUUID
    
    private boolean revisorUUID(String UUID) {
        String RegExCli = "^CLI-(\\*|[0-9A-Fa-f]{16,})$",
                RegExPre = "^PRE-(\\*|[0-9A-Fa-f]{16,})$",
                RegExCuo = "^CUO-(\\*|[0-9A-Fa-f]{16,})$",
                RegExPag = "^PAG-(\\*|[0-9A-Fa-f]{16,})$";

        return UUID.matches(RegExCli) || UUID.matches(RegExPre) || UUID.matches(RegExCuo) || UUID.matches(RegExPag) || UUID.contains("EXT-");
    }
     */
    private void ICCCliente(String UUID) {
        StringBuilder Datos = new StringBuilder();
        if (UUID.equals("CLI-*")) {
            Datos.append(InformesGeneralesDAO.IGCClienteGeneral());
        } else {
            InformesGeneralesEntidad IGE = InformesGeneralesDAO.IGCCliente(UUID);

            if (IGE.getListaDeClientes().isEmpty()) {
                return;
            }
            Datos.append("Datos del Cliente\n");
            Datos.append(IGE.getListaDeClientes().getFirst().printClientData(false));

            Datos.append("\nCantidad de Prestamos Solicitados: ");
            Datos.append(IGE.getListaDePrestamos().size());

            StringBuilder DataCuotas = new StringBuilder(), DataPrestamo = new StringBuilder();

            int prestamoE = 1, cuotaE = 1;

            if (!IGE.getListaDeCuotas().isEmpty()) {
                Datos.append("\nPrestamos y cuotas en Mora\n");
                List<String> prestamoID = new ArrayList();
                for (Cuota cuota : IGE.getListaDeCuotas()) {
                    if (!cuota.isPagado() && cuota.getVencimiento().isBefore(LocalDateTime.now())) {
                        if (!prestamoID.contains(cuota.getIdPrestamo())) {
                            prestamoID.add(cuota.getIdPrestamo());
                            for (Prestamo prestamo : IGE.getListaDePrestamos()) {
                                if (prestamo.getIdPrestamo().equals(cuota.getIdPrestamo())) {
                                    DataPrestamo.append(String.format("%s%n", prestamo.printPrestamo(prestamoE == 1)));
                                    prestamoE++;
                                }
                            }
                        }
                        DataCuotas.append(String.format("%s%n", cuota.printCuotaConMora(cuota, cuotaE == 1)));
                        cuotaE++;
                    }

                }
                Datos.append(String.format("%s%n%s%n", DataPrestamo.toString(), DataCuotas.toString()));
            }
        }
        System.out.println(Datos.toString());
        if (exportar) {
            Exportador.FileExporter(Datos.toString(), !UUID.equals("CLI-*"));
        }

    }

    private void ICCPrestamo(String UUID) {
        StringBuilder Datos = new StringBuilder();
        if (UUID.equals("PRE-*")) {
            Datos.append(InformesGeneralesDAO.IGCPrestamoGeneral());
        } else {
            InformesGeneralesEntidad IGE = InformesGeneralesDAO.IGCPrestamo(UUID);
            if (IGE.getListaDePrestamos().isEmpty()) {
                return;
            }
            if (!IGE.getListaDeClientes().isEmpty()) {
                Datos.append("Datos del Cliente\n");
                Datos.append(IGE.getListaDeClientes().getFirst().printClientData(false));
            }

            Datos.append("\nDatos del Prestamo\n");
            Datos.append(IGE.getListaDePrestamos().getFirst().printPrestamo(true));

            if (IGE.getListaDeCuotas().isEmpty()) {
                Datos.append(IGE.getExtraData().getFirst());
            } else {
                int X = 1;
                for (Cuota cuota : IGE.getListaDeCuotas()) {
                    if (!cuota.isPagado() && cuota.getVencimiento().isBefore(LocalDateTime.now())) {
                        if (X == 1) {
                            Datos.append("\nCuotas en mora\n");
                        }
                        Datos.append(String.format("%s%n", cuota.printCuotaConMora(cuota, X == 1)));
                    }
                }
            }
        }
        System.out.println(Datos.toString());
        if (exportar) {
            Exportador.FileExporter(Datos.toString(), !UUID.equals("PRE-*"));
        }

    }

    private void ICCCuota(String UUID) {
        StringBuilder Datos = new StringBuilder();
        if (UUID.equals("CUO-*")) {
            Datos.append(InformesGeneralesDAO.IGCCuotaGeneral());
        } else {
            InformesGeneralesEntidad IGE = InformesGeneralesDAO.IGCCuota(UUID);
            if (IGE.getListaDeCuotas().isEmpty()) {
                return;
            }
            if (!IGE.getListaDePrestamos().isEmpty()) {
                Datos.append("Datos del Prestamo\n");
                Datos.append(IGE.getListaDePrestamos().getFirst().printPrestamo(true));
            }

            Datos.append("\nDatos de la cuota\n");
            Datos.append(IGE.getListaDeCuotas().getFirst().printCuotaConMora(IGE.getListaDeCuotas().getFirst(), true));

            if (!IGE.getListaDePagos().isEmpty()) {
                Datos.append("\nDatos del Pago\n");
                Datos.append(IGE.getListaDePagos().getFirst().printPago(true));
            }
        }
        System.out.println(Datos.toString());
        if (exportar) {
            Exportador.FileExporter(Datos.toString(), !UUID.equals("CUO-*"));
        }
    }

    private void ICCPago(String UUID) {
        StringBuilder Datos = new StringBuilder();
        if (UUID.equals("PAG-*")) {
            Datos.append(InformesGeneralesDAO.IGCPagoGeneral());
        } else {
            InformesGeneralesEntidad IGE = InformesGeneralesDAO.IGCPago(UUID);
            if (IGE.getListaDePagos().isEmpty()) {
                return;
            }

            if (!IGE.getListaDePrestamos().isEmpty()) {
                Datos.append("Datos del Prestamo\n");
                Datos.append(IGE.getListaDePrestamos().getFirst().printPrestamo(true));
            }
            Datos.append("\nDatos del Pago\n");
            Datos.append(IGE.getListaDePagos().getFirst().printPago(true));
        }
        System.out.println(Datos.toString());
        if (exportar) {
            Exportador.FileExporter(Datos.toString(), !UUID.equals("PAG-*"));
        }
    }

    private void InformeGeneral() {
        do {
            menu = """
                    \tExportador De Tablas
                    1) Exportar Clientes
                    2) Exportar Prestamos
                    3) Exportar Cuotas
                    4) Exportar Pagos
                    0) Salir
                    """;
            System.out.println(menu);
            respuesta = "";
            respuesta = sc.nextLine();
            switch (respuesta) {
                case "1":
                    ExpoTablaCliente();
                    break;
                case "2":
                    ExpoTablaPrestamo();
                    break;
                case "3":
                    ExpoTablaCuota();
                    break;
                case "4":
                    ExpoTablaPago();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opcion Invalida");
            }
        } while (true);
    }

    private void ExpoTablaCliente() {
        List<Cliente> Clientes = Cliente.traerTodos();
        StringBuilder Datos = new StringBuilder();
        Datos.append(String.format("%-20s | %-50s | %-10s | %-15s | %-100s | %-255s",
                "UUID", "Nombre", "DNI", "Telefono", "Direccion", "Email"));
        for (Cliente cliente : Clientes) {
            Datos.append("\n");
            Datos.append(cliente.printClientData(true));
        }
        Exportador.FileExporter(Datos.toString(), true);
    }

    private void ExpoTablaPrestamo() {
        List<Prestamo> Prestamos = PrestamosDAO.ListaDePrestamos("PRE-*");
        StringBuilder Datos = new StringBuilder();
        Datos.append(String.format(
                "%-22s | %-22s | %10s | %14s | %13s | %-15s | %-10s | %-20s | %-10s |",
                "ID Cliente",
                "ID Préstamo",
                "Monto",
                "Interés",
                "Cuotas",
                "Tipo Préstamo",
                "Cuota",
                "Fecha Creación",
                "Estado"
        ));
        for (Prestamo prestamo : Prestamos) {
            Datos.append("\n");
            Datos.append(prestamo.printPrestamo(false));
        }
        Exportador.FileExporter(Datos.toString(), true);
    }

    private void ExpoTablaCuota() {
        List<Cuota> Cuotas = PrestamosDAO.ListaDeCuotas("CUO-*");
        StringBuilder Datos = new StringBuilder();
        Datos.append(String.format(
                "%-25s | %-25s | %10s | %10s | %-10s | %-20s | %-10s |",
                "ID Prestamo",
                "ID Cuota",
                "Monto",
                "Interés",
                "Pagado",
                "Vencimiento",
                "Contiene Mora"
        ));
        for (Cuota cuota : Cuotas) {
            Datos.append("\n");
            Datos.append(cuota.printCuotaConMora(cuota, false));
        }
        Exportador.FileExporter(Datos.toString(), true);
    }

    private void ExpoTablaPago() {
        List<Pago> Pagos = PrestamosDAO.ListaDePagos("PAG-*");
        StringBuilder Datos = new StringBuilder();
        Datos.append(String.format("%-25s | %-25s | %-10s | %-12s | %-15s |",
                "ID Cuota",
                "ID Pago",
                "Penalidad",
                "Monto Pago",
                "Fecha Del Pago"
        ));
        for (Pago pago : Pagos) {
            Datos.append("\n");
            Datos.append(pago.printPago(false));
        }
        Exportador.FileExporter(Datos.toString(), true);
    }
}
