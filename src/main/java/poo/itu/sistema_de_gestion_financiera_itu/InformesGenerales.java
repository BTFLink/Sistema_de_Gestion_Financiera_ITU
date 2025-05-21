/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo.itu.sistema_de_gestion_financiera_itu;

import java.util.Scanner;

/**
 *
 * @author BTF
 */
public class InformesGenerales {

    private Scanner sc = new Scanner(System.in);
    private String menu, respuesta;

    public InformesGenerales() {
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
                    break;
                case "PRE-":
                    break;
                case "CUO-":
                    break;
                case "PAG-":
                    break;
                case "EXT-":
                    break;
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

    private void TraerDatosICC(String CLI, boolean conActivo, boolean sinActivo, boolean conMora, boolean sinMora) {
        String query = "SELECT ";
        if (CLI.equals("CLI-*")) {
            query += "cl.*, ";
        }else{
            query += "SELECT cl.* ";
        }
        query += "FROM `cliente` cl JOIN `prestamos` pr ON cl.idCliente = pr.cliente_idCliente"
                + "JOIN `cuotas` cu ON cu.prestamo_idPrestamo = pr.idPrestamo"
                + "JOIN `pagos` pa ON pa.cuota_idcuota = cu.idcuota";
    }

    private boolean revisorUUID(String UUID) {
        String RegExCli = "^CLI-[0-9A-Fa-f]{16,}$",
                RegExPre = "^PRE-[0-9A-Fa-f]{16,}$",
                RegExCuo = "^CUO-[0-9A-Fa-f]{16,}$",
                RegExPag = "^PAG-[0-9A-Fa-f]{16,}$";
        return UUID.matches(RegExCli) || UUID.matches(RegExPre) || UUID.matches(RegExCuo) || UUID.matches(RegExPag) || UUID.contains("EXT-");
    }
}
