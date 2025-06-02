/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package poo.itu.sistema_de_gestion_financiera_itu;

import java.util.Scanner;
import prestamo.MenuPrestamo;

/**
 *
 * @author BTF
 */
public class Sistema_de_Gestion_Financiera_ITU {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Scanner sc = new Scanner(System.in);
        String menu="""
                    \tMenu Principal
                    1) Menu de Clientes
                    2) Menu de Prestamos
                    3) Menu de Informes
                    0) Salir
                    """;
        String respuesta;
        do {
            System.out.println(menu);
            respuesta = sc.nextLine();
            switch (respuesta) {
                case "1":
                    MenuCliente.main();
                    break;
                case "2":
                    MenuPrestamo.main();
                    break;
                case "3":
                    IGenerals.main();
                    break;
                case "0":
                    System.out.println("SALE BYE");
                    return;
                default:
                    System.out.println("Respuesta invalida");
            }
        } while (true);
    }
}
