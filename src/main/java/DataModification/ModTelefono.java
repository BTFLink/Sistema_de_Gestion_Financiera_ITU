/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModification;

import Entity.Telefono;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author BTF
 */
public abstract class ModTelefono {

    private static String UUID = "";
    private static String telefono = "";
    private static String respuesta;
    private static final Scanner scanner = new Scanner(System.in);

    public static void ModTelefono() {
        System.out.println("Buscar por:\n (1) ID Cliente\n (2) Telefono\n (3) Cancelar");
        do {
            respuesta = scanner.nextLine();
            switch (respuesta) {
                case "1":
                    System.out.println("Ingrese el ID unico del cliente (UUID)");
                    for (int I = 0; I < 3; I++) {
                        UUID = scanner.nextLine();
                        if (UUID.matches("^[0-9A-F]{45}$")) {
                            break;
                        } else if (I == 2) {
                            System.out.println("Ha fallado muchos intentos, volviendo");
                            return;
                        } else {
                            System.out.println("ID unico invalido");
                        }
                    }
                    break;
                case "2":
                    System.out.println("Ingrese el telefono del cliente");
                    for (int I = 0; I < 3; I++) {
                        telefono = scanner.nextLine();
                        if (telefono.matches("^[0-9]{8,10}$")) {
                            break;
                        } else if (I == 2) {
                            System.out.println("Ha fallado muchos intentos, volviendo");
                            return;
                        } else {
                            System.out.println("tipo de telefono invalido");
                        }
                    }
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Respuesta invalida");
            }
        } while (!(respuesta.equals("1") || respuesta.equals("2")));

        List<Telefono> listaTelefonos;
        try {
            if (respuesta.equals("1")) {
                listaTelefonos = Telefono.searchListTelefonoByUUID(UUID);
            } else {
                listaTelefonos = Telefono.searchListTelefonoByNumber(Long.parseLong(telefono));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        if (listaTelefonos.isEmpty()) {
            System.out.println("No se encontraron telefonos bajo los datos indicados");
            return;
        }
        ModTelefonoP2(listaTelefonos);
    }

    private static void ModTelefonoP2(List<Telefono> listaTelefonos) {
        int index;
        String menu = """
                    \t Menu Modificacion Telefonos
                     (1) Elegir Telefono a modificar
                     (2) Mostrar lista
                     (3) Salir
                    >""";
        Telefono.showIndexedListInformation(listaTelefonos);
        do {
            System.out.print(menu);
            respuesta = scanner.nextLine();
            switch (respuesta) {
                case "1":
                    System.out.println("Ingrese el indice del telefono que desea editar (Index: indice)");
                    try {
                        index = scanner.nextInt();
                        scanner.nextLine();
                        if (index <= listaTelefonos.size() && index > 0) {
                            listaTelefonos.set(index - 1, ModTelefonoP3(listaTelefonos.get(index - 1)));
                        } else {
                            System.out.println("Index Invalido");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "2":
                    Telefono.showIndexedListInformation(listaTelefonos);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Respuesta Invalida");
            }
        } while (true);
    }

    private static Telefono ModTelefonoP3(Telefono telefono) {
        Telefono CambioTelefono = new Telefono(telefono.getNumero(), telefono.isPrincipal(), telefono.isActivo(), telefono.getcliente_UUID());

        do {
            CambioTelefono.showInformation();
            System.out.println("""
                               ¿Que va a cambiar?
                               (1) Numero de telefono
                               (2) Cambiar prioridad
                               (3) Activar/Desactivar numero
                               (4) Salir e Implementar
                               """);
            respuesta = scanner.nextLine();
            switch (respuesta) {
                case "1":
                    try {
                        System.out.println("Ingrese el nuevo numero");
                        CambioTelefono.setNumero(scanner.nextLong());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("No se relizo ningun cambio");
                    }
                    break;
                case "2":
                    CambioTelefono.setPrincipal(!CambioTelefono.isPrincipal());
                    if (CambioTelefono.isPrincipal()) {
                        System.out.println("Se ha añadido a los principales");
                    } else {
                        System.out.println("Se ha quitado de la lista de principales");
                    }
                    break;
                case "3":
                    CambioTelefono.setActivo(!CambioTelefono.isActivo());
                    if (CambioTelefono.isActivo()) {
                        System.out.println("Se ha habilitado el contacto");
                    } else {
                        System.out.println("Se ha deshabilitado el contacto");
                    }
                    break;
                case "4":
                    if (CambioTelefono.equals(telefono)) {
                        System.out.println("No se realizaron cambios");
                        return telefono;
                    }
                    try {
                        for (int i = 0; i < 3; i++) {
                            if (CambioTelefono.actualizarTelefono()) {
                                System.out.println("Cambios implementados con exito");
                                return CambioTelefono;
                            }
                        }
                        System.out.println("No se pudo implementar los cambios, cancelando los mismos");
                        return telefono;

                    } catch (Exception e) {
                        System.out.println("Hubo un error al intentar implementar los cambios, cancelando los mismos");
                        return telefono;
                    }
                default:
            }
        } while (true);
    }

}
