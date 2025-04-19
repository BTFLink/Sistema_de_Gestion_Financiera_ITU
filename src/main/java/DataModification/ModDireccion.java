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
                            listOfDireccitions.set(index - 1, ModDireccionP3(listOfDireccitions.get(index - 1)));
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

    public static Direccion ModDireccionP3(Direccion direccion) {
        Direccion CambioDireccion = setDireccion(direccion);
        int changes=0;
        System.out.println("Ingrese la nueva Calle");
        CambioDireccion.setCalle(scanner.nextLine());
        if (CambioDireccion.getCalle().equals("")) {
            CambioDireccion.setCalle(direccion.getCalle());
            changes++;
        }

        System.out.println("Ingrese la nueva Numeracion");
        CambioDireccion.setNumeracion(scanner.nextLine());
        if (CambioDireccion.getNumeracion().equals("")) {
            CambioDireccion.setNumeracion(direccion.getNumeracion());
            changes++;
        }

        System.out.println("Ingrese el nuevo Piso");
        CambioDireccion.setPiso(scanner.nextLine());
        if (CambioDireccion.getPiso().equals("")) {
            CambioDireccion.setPiso(direccion.getPiso());
            changes++;
        }

        System.out.println("Ingrese el nuevo Codigo Postal ");
        CambioDireccion.setCodigo_postal(scanner.nextLine());
        if (CambioDireccion.getCodigo_postal().equals("")) {
            CambioDireccion.setCodigo_postal(direccion.getCodigo_postal());
            changes++;
        }

        System.out.println("Ingrese la nueva Ciudad");
        CambioDireccion.setCiudad(scanner.nextLine());
        if (CambioDireccion.getCiudad().equals("")) {
            CambioDireccion.setCiudad(direccion.getCiudad());
            changes++;
        }

        System.out.println("Ingrese el nuevo Departamento");
        CambioDireccion.setDepartamento(scanner.nextLine());
        if (CambioDireccion.getDepartamento().equals("")) {
            CambioDireccion.setDepartamento(direccion.getDepartamento());
            changes++;
        }

        System.out.println("Ingrese la nueva Provincia");
        CambioDireccion.setProvincia(scanner.nextLine());
        if (CambioDireccion.getProvincia().equals("")) {
            CambioDireccion.setProvincia(direccion.getProvincia());
            changes++;
        }

        System.out.println("Ingrese el nuevo Pais");
        CambioDireccion.setPais(scanner.nextLine());
        if (CambioDireccion.getPais().equals("")) {
            CambioDireccion.setPais(direccion.getPais());
            changes++;
        }

        if(changes>0){
            try {
                for (int i = 0; i < 3; i++) {
                    if(CambioDireccion.actuallizarDireccion()){
                        System.out.println("Actualizacion Completada con Exito");
                        return CambioDireccion;
                    }
                }
            } catch (Exception e) {
            }
            System.out.println("No se pudo completar la actualizacion");
            return direccion;
        }
        System.out.println("No se detectaron cambios");
        return direccion;
    }

    private static Direccion setDireccion(Direccion direccion) {
        Direccion CambioDireccion = new Direccion();
        CambioDireccion.setActivo(direccion.isActivo());
        CambioDireccion.setCalle(direccion.getCalle());
        CambioDireccion.setCiudad(direccion.getCiudad());
        CambioDireccion.setCliente_UUID(direccion.getCliente_UUID());
        CambioDireccion.setCodigo_postal(direccion.getCodigo_postal());
        CambioDireccion.setDepartamento(direccion.getDepartamento());
        CambioDireccion.setId(direccion.getId());
        CambioDireccion.setNumeracion(direccion.getNumeracion());
        CambioDireccion.setPais(direccion.getPais());
        CambioDireccion.setPiso(direccion.getPiso());
        CambioDireccion.setProvincia(direccion.getProvincia());
        return CambioDireccion;
    }
}
