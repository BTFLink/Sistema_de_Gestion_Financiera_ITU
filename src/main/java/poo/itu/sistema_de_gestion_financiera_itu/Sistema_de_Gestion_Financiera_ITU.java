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
//Clase del Menu Principal del Sis tema de Gestion Financiera
    public static void main(String[] args) {
        System.out.println("Sistema de Gestion Financiera");
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
                    MenuCliente.main(); //Menu para la creacion de Clientes
                    break;
                case "2":
                    MenuPrestamo.main(); //Menu para la creacion de Prestamos
                    break;
                case "3":
                    IGenerals.main(); //Menu de informes generales
                    break;
                case "0":
                    System.out.println("SALE BYE"); //SALIDA
                    return;
                default:
                    System.out.println("Respuesta invalida");
            }
        } while (true);
    }
}
