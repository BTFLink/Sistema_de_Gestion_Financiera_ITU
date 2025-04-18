/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModification;

import Entity.Direccion;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author BTF
 */
public class ModDireccion {

    private static final Scanner scanner = new Scanner(System.in);
    private static String respuesta;
    private static final String INVALIDOPTION = "Opcion Invalida", INVALIDDATA = "Dato/s Invalido/s", REGEXUUID = "^[0-9A-F]{45}$";

    public static void ModDireccion() {
        String UUID;
        do {
            System.out.println("""
                           \tMenú Direccion
                           1) Buscar Direcciones por UUID del Cliente
                           2) Salir
                           """);
            respuesta = scanner.nextLine();
            switch (respuesta) {
                case "1":
                    System.out.println("Ingrese el codigo UUID del Cliente");
                    UUID = scanner.nextLine();
                    if (UUID.matches(REGEXUUID)) {
                        try {
                            ModDireccionP2(Direccion.searchDireccionPorUUID(UUID));
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            System.out.println("Problemas al intentar buscar datos");
                        }
                    } else {
                        System.out.println(INVALIDDATA);
                    }
                    break;
                case "2":
                    return;
                default:
                    System.out.println(INVALIDOPTION);
            }
        } while (true);
    }

    private static void ModDireccionP2(List<Direccion> listOfDireccitions) {
        int index;
        Direccion.showListOfDireccion(listOfDireccitions);
        do {
            System.out.println("""
                               \tMenu Modificacion Direccion
                               1) Elegir Direccion a modificar
                               2) Mostrar Lista
                               3) Salir
                               """);
            respuesta = scanner.nextLine();
            switch (respuesta) {
                case "1":
                    System.out.println("Ingrese el indice de la direccion que desea editar (Index: indice)");
                    try {
                        index = scanner.nextInt();
                        scanner.nextLine();
                        if (index <= listOfDireccitions.size() && index > 0) {
                            listOfDireccitions.set(index-1, ModDireccionP3(listOfDireccitions.get(index-1)));
                        } else {
                            System.out.println(INVALIDDATA);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println(INVALIDDATA);
                    }
                    break;
                case "2":
                    Direccion.showListOfDireccion(listOfDireccitions);
                    break;
                case "3":
                    return;
                default:
                    System.out.println(INVALIDOPTION);
            }
        } while (true);
    }

    public static Direccion ModDireccionP3(Direccion direccion){
        return null;
    }
    
}
