/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo.itu.sistema_de_gestion_financiera_itu;

import Connections.InformesGeneralesDAO;
import Entity.InformesGeneralesEntidad;
import java.util.Scanner;

/**
 *
 * @author BTF
 */
public class InformesGenerales {

    private Scanner sc = new Scanner(System.in);
    private String menu, respuesta;

    public static void main() {
        InformesGenerales ig = new InformesGenerales();
        ig.MenuInformesGenerales();
    }

    public InformesGenerales() {
    }

    public void MenuInformesGenerales() {
        do {
            menu = """
                    \tMenu de Informes
                    1) Informes con Codigo Identificador
                    2) Informes Generales
                    0) Salir
                    """;
            System.out.println(menu);
            respuesta = "";
            respuesta = sc.nextLine();
            switch (respuesta) {
                case "1":
                    InformeConCodigo();
                    break;
                case "2":
                    break;
                case "0":
                    break;
                default:
                    throw new AssertionError();
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
            respuesta = sc.nextLine();
            if (revisorUUID(respuesta)) {
            }
            switch (respuesta.substring(0, 4)) {
                case "CLI-":
                    ICCCliente(respuesta);
                    break;
                case "PRE-":
                    break;
                case "CUO-":
                    break;
                case "PAG-":
                    break;
                case "EXT-":
                    return;
                default:
                    throw new AssertionError();
            }
        } while (true);
    }

    private void ICCCliente(String UUID) {
        if (UUID.equals("CLI-*")) {
            menu = """
                 Ingrese los datos que desea obtener y luego confirmelos
                 1) Clientes con prestamos activos: %s
                 2) Clientes sin prestamos activos: %s
                 3) Clientes con prestamos en Mora: %s
                 4) Clientes sin prestamos en Mora: %s
                 0) Confirmar y traer
                 ex) Salir
                 """;
        } else {
            menu = """
                 Ingrese los datos que desa obtener y luego confirmelos
                 1) Prestamos activos:   %s
                 2) Prestamos inactivos: %s
                 3) Prestamos con Mora:  %s
                 4) Prestamos sin Mora:  %s
                 0) Confirmar y traer
                 ex) Salir
                 """;
        }
        boolean conActivo = false, sinActivo = false, conMora = false, sinMora = false;
        respuesta = "";
        do {
            System.out.printf(menu,
                    conActivo ? "Traer" : "Descartado", sinActivo ? "Traer" : "Descartado",
                    conMora ? "Traer" : "Descartado", sinMora ? "Traer" : "Descartado");
            respuesta = sc.nextLine();
            switch (respuesta) {
                case "1":
                    conActivo = !conActivo;
                    break;
                case "2":
                    sinActivo = !sinActivo;
                    break;
                case "3":
                    conMora = !conMora;
                    break;
                case "4":
                    sinMora = !sinMora;
                    break;
                case "0":
                    TraerDatosICC(UUID, conActivo, sinActivo, conMora, sinMora);
                    return;
                case "ex":
                    return;
                default:
                    throw new AssertionError();
            }
        } while (true);
    }

    private void TraerDatosICC(String UUID, boolean conActivo, boolean sinActivo, boolean conMora, boolean sinMora) {

        switch (UUID.substring(0, 4)) {
            case "CLI-":
                if (UUID.equals("CLI-*")) {
                    InformesGeneralesDAO.IGCGeneralCliente(conActivo, sinActivo, conMora, sinMora);
                } else {
                    InformesGeneralesEntidad IGE = InformesGeneralesDAO.IGCUUID(UUID, conActivo, sinActivo, conMora, sinMora);
                }
                break;
            case "PRE-":
                if (UUID.equals("PRE-*")) {
                    InformesGeneralesDAO.IGCGeneralCliente(conActivo, sinActivo, conMora, sinMora);
                } else {
                    InformesGeneralesEntidad IGE = InformesGeneralesDAO.IGCUUID(UUID, conActivo, sinActivo, conMora, sinMora);
                }
                break;
            case "CUO-":
                if (UUID.equals("CUO-*")) {
                    InformesGeneralesDAO.IGCGeneralCliente(conActivo, sinActivo, conMora, sinMora);
                } else {
                    InformesGeneralesEntidad IGE = InformesGeneralesDAO.IGCUUID(UUID, conActivo, sinActivo, conMora, sinMora);
                }
                break;
            case "PAG-":
                if (UUID.equals("PAG-*")) {
                    InformesGeneralesDAO.IGCGeneralCliente(conActivo, sinActivo, conMora, sinMora);
                } else {
                    InformesGeneralesEntidad IGE = InformesGeneralesDAO.IGCUUID(UUID, conActivo, sinActivo, conMora, sinMora);
                }
                break;
            default:
                throw new AssertionError();
        }

    }

    private boolean revisorUUID(String UUID) {
        String RegExCli = "^CLI-[0-9A-Fa-f]{16,}$",
                RegExPre = "^PRE-[0-9A-Fa-f]{16,}$",
                RegExCuo = "^CUO-[0-9A-Fa-f]{16,}$",
                RegExPag = "^PAG-[0-9A-Fa-f]{16,}$";
        return UUID.matches(RegExCli) || UUID.matches(RegExPre) || UUID.matches(RegExCuo) || UUID.matches(RegExPag) || UUID.contains("EXT-");
    }

    private void ICCPrestamo(String UUIDPrestamo) {
        if (UUIDPrestamo.equals("PRE-*")) {
            menu = """
                 Ingrese los datos que desea obtener y luego confirmelos
                 1) Prestamos activos:     %s
                 2) Prestamos inactivos:   %s
                 3) Prestamos en Mora:     %s
                 4) Prestamos en Sin Mora: %s
                 0) Confirmar y traer
                 ex) Salir
                 """;
        } else {
            menu = """
                 Ingrese los datos que desa obtener y luego confirmelos
                 3) Cuotas con Mora:  %s
                 4) Cuotas sin Mora:  %s
                 0) Confirmar y traer
                 ex) Salir
                 """;
        }
        boolean conActivo = false, sinActivo = false, conMora = false, sinMora = false;
        respuesta = "";
        do {
            System.out.printf(menu,
                    conActivo ? "Traer" : "Descartado", sinActivo ? "Traer" : "Descartado",
                    conMora ? "Traer" : "Descartado", sinMora ? "Traer" : "Descartado");
            respuesta = sc.nextLine();
            switch (respuesta) {
                case "1":
                    conActivo = !conActivo;
                    break;
                case "2":
                    sinActivo = !sinActivo;
                    break;
                case "3":
                    conMora = !conMora;
                    break;
                case "4":
                    sinMora = !sinMora;
                    break;
                case "0":
                    TraerDatosICPr(UUIDPrestamo, conActivo, sinActivo, conMora, sinMora);
                    return;
                case "ex":
                    return;
                default:
                    throw new AssertionError();
            }
        } while (true);
    }

    private void ICCCuota(String UUIDCuota) {
        if (UUIDCuota.equals("CUO-*")) {
            menu = """
                 Ingrese los datos que desea obtener y luego confirmelos
                 1) Cuotas de Prestamos activos:     %s
                 2) Cuotas de Prestamos inactivos:   %s
                 3) Cuotas en/con Mora:              %s
                 4) Cuotas sin Mora:                 %s
                 0) Confirmar y traer
                 ex) Salir
                 """;
        } else {
        }
        boolean conActivo = false, sinActivo = false, conMora = false, sinMora = false;
        respuesta = "";
        do {
            System.out.printf(menu,
                    conActivo ? "Traer" : "Descartado", sinActivo ? "Traer" : "Descartado",
                    conMora ? "Traer" : "Descartado", sinMora ? "Traer" : "Descartado");
            respuesta = sc.nextLine();
            switch (respuesta) {
                case "1":
                    conActivo = !conActivo;
                    break;
                case "2":
                    sinActivo = !sinActivo;
                    break;
                case "3":
                    conMora = !conMora;
                    break;
                case "4":
                    sinMora = !sinMora;
                    break;
                case "0":
                    TraerDatosICCu(UUIDCuota, conActivo, sinActivo, conMora, sinMora);
                    return;
                case "ex":
                    return;
                default:
                    throw new AssertionError();
            }
        } while (true);
    }

    private void ICCPago(String UUIDPago) {
        if (UUIDPago.equals("PAG-*")) {
            menu = """
                 Ingrese los datos que desea obtener y luego confirmelos
                 1) Pagos de Prestamos activos:     %s
                 2) Pagos de Prestamos inactivos:   %s
                 3) Pagos en Mora:                  %s
                 4) Pagos sin Mora:                 %s
                 0) Confirmar y traer
                 ex) Salir
                 """;
        } else {
        }
        boolean conActivo = false, sinActivo = false, conMora = false, sinMora = false;
        respuesta = "";
        do {
            System.out.printf(menu,
                    conActivo ? "Traer" : "Descartado", sinActivo ? "Traer" : "Descartado",
                    conMora ? "Traer" : "Descartado", sinMora ? "Traer" : "Descartado");
            respuesta = sc.nextLine();
            switch (respuesta) {
                case "1":
                    conActivo = !conActivo;
                    break;
                case "2":
                    sinActivo = !sinActivo;
                    break;
                case "3":
                    conMora = !conMora;
                    break;
                case "4":
                    sinMora = !sinMora;
                    break;
                case "0":
                    TraerDatosICPa(UUIDPago, conActivo, sinActivo, conMora, sinMora);
                    return;
                case "ex":
                    return;
                default:
                    throw new AssertionError();
            }
        } while (true);
    }
}
