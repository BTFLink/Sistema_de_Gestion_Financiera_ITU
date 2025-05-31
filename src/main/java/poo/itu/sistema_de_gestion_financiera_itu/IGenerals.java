/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo.itu.sistema_de_gestion_financiera_itu;

import Connections.InformesGeneralesDAO;
import Entity.Cliente;
import Entity.InformesGeneralesEntidad;
import java.util.Scanner;

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
        this.exportar=false;
    }

    public void MenuInformesGenerales() {
        do {
            menu = """
                    \tMenu de Informes
                    1) Informes con Codigo Identificador
                    2) Informes Generales
                    3) Modo de exportacion: %s
                    0) Salir
                    """;
            System.out.printf(menu,exportar ? "Activado": "Desactivado");
            respuesta = "";
            respuesta = sc.nextLine();
            switch (respuesta) {
                case "1":
                    InformeConCodigo();
                    break;
                case "2":
                    break;
                case "3": exportar=!exportar;
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
                    ICCPrestamo(respuesta);
                    break;
                case "CUO-":
                    ICCCuota(respuesta);
                    break;
                case "PAG-":
                    ICCPago(respuesta);
                    break;
                case "EXT-":
                    return;
                default:
                    System.out.println("Opcion Invalida");
            }
        } while (true);
    }

    private boolean revisorUUID(String UUID) {
        String RegExCli = "^CLI-(\\*|[0-9A-Fa-f]{16,})$",
               RegExPre = "^PRE-(\\*|[0-9A-Fa-f]{16,})$",
               RegExCuo = "^CUO-(\\*|[0-9A-Fa-f]{16,})$",
               RegExPag = "^PAG-(\\*|[0-9A-Fa-f]{16,})$";

        return UUID.matches(RegExCli) || UUID.matches(RegExPre) || UUID.matches(RegExCuo) || UUID.matches(RegExPag) || UUID.contains("EXT-");
    }
    
    private void ICCCliente(String UUID){
        /*
        Cantidad de Clientes
        
        Table: cliente
Columns:
id int UN AI PK 
idCliente varchar(20) PK 
nombre varchar(50) 
direccion varchar(100) 
telefono bigint 
correoElectronico varchar(255)
        
        Table: cuotas
Columns:
id int UN AI PK 
prestamos_idPrestamo varchar(20) PK 
idcuota varchar(45) PK 
montoCuota double 
interesCuota double 
pagado tinyint 
vencimiento datetime
        
        Table: pagos
Columns:
id int UN AI PK 
cuotas_idcuota varchar(45) PK 
idpago varchar(45) PK 
penalidad tinyint 
montopago double 
fechapago datetime
        
        Table: prestamos
Columns:
id int UN AI PK 
cliente_idCliente varchar(20) PK 
idPrestamo varchar(20) PK 
tipoPrestamo varchar(30) 
montoPrestamo double 
cuotasPrestamo int 
cuotasFijas tinyint 
interesInicial double 
fechaPedido datetime 
estaPagado tinyint
        
        */
        if(UUID.equals("CLI-*")){
            String Datos = InformesGeneralesDAO.IGCClienteGeneral();
            System.out.println(Datos);
            if(exportar){
            }
            return;
        }
        InformesGeneralesEntidad IGE = InformesGeneralesDAO.IGCCliente(UUID);
        
        
    }

    private void ICCPrestamo(String UUID){
        if(UUID.equals("PRE-*")){
            String Datos = InformesGeneralesDAO.IGCPrestamoGeneral();
            System.out.println(Datos);
            if(exportar){}
            return;
        }
        InformesGeneralesEntidad IGE = InformesGeneralesDAO.IGCPrestamo(UUID);
    }

    private void ICCCuota(String UUID){
        if(UUID.equals("CUO-*")){
            String Datos = InformesGeneralesDAO.IGCCuotaGeneral();
            System.out.println(Datos);
            if(exportar){}
            return;
        }
        InformesGeneralesEntidad IGE = InformesGeneralesDAO.IGCCuota(UUID);
    }
    
    private void ICCPago(String UUID){
        if(UUID.equals("PAG-*")){
            String Datos = InformesGeneralesDAO.IGCPagoGeneral();
            System.out.println(Datos);
            if (exportar) {}
            return;
        }
        InformesGeneralesEntidad IGE = InformesGeneralesDAO.IGCPago(UUID);
    }
}
