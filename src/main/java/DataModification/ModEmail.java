/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModification;

import Entity.Email;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author BTF
 */
public class ModEmail {
    private static String UUID = "";
    private static String email = "";
    private static String respuesta;
    private static final Scanner scanner = new Scanner(System.in);

    public static void ModEmail() {
        System.out.println("Buscar por:\n (1) ID Cliente\n (2) Email\n (3) Cancelar");
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
                    System.out.println("Ingrese el email del cliente");
                    for (int I = 0; I < 3; I++) {
                        email = scanner.nextLine();
                        if (email.matches("^[0-9]{8,10}$")) { //<<<<ACTUALIZAR REGEX
                            break;
                        } else if (I == 2) {
                            System.out.println("Ha fallado muchos intentos, volviendo");
                            return;
                        } else {
                            System.out.println("tipo de email invalido");
                        }
                    }
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Respuesta invalida");
            }
        } while (!(respuesta.equals("1") || respuesta.equals("2")));

        List<Email> lsitaEmails;
        try {
            if (respuesta.equals("1")) {
                lsitaEmails = Email.searchListEmailByUUID(UUID);
            } else {
                lsitaEmails = Email.searchListEmailByEmail(email);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        if (lsitaEmails.isEmpty()) {
            System.out.println("No se encontraron emails bajo los datos indicados");
            return;
        }
        ModEmaiP2(lsitaEmails);
    }

    private static void ModEmaiP2(List<Email> listaEmails) {
        int index;
        String menu = """
                    \t Menu Modificacion Email
                     (1) Elegir Email a modificar
                     (2) Mostrar lista
                     (3) Salir
                    >""";
        Email.showListInformation(listaEmails);
        do {
            System.out.print(menu);
            respuesta = scanner.nextLine();
            switch (respuesta) {
                case "1":
                    System.out.println("Ingrese el indice del email que desea editar (Index: indice)");
                    try {
                        index = scanner.nextInt();
                        scanner.nextLine();
                        if (index <= listaEmails.size() && index > 0) {
                            listaEmails.set(index - 1, ModEmailP3(listaEmails.get(index - 1)));
                        } else {
                            System.out.println("Index Invalido");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "2":
                    Email.showListInformation(listaEmails);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Respuesta Invalida");
            }
        } while (true);
    }

    private static Email ModEmailP3(Email email) {
        Email CambioEmail = new Email(email.getEmail(), email.isPrincipal(), email.isActivo());
        CambioEmail.setCliente_UUID(email.getCliente_UUID());
        do {
            CambioEmail.showInformation();
            System.out.println("""
                               ¿Que va a cambiar?
                               (1) Correo del Email
                               (2) Cambiar prioridad
                               (3) Activar/Desactivar numero
                               (4) Salir e Implementar
                               """);
            respuesta = scanner.nextLine();
            switch (respuesta) {
                case "1":
                    try {
                        System.out.println("Ingrese el nuevo Email");
                        CambioEmail.setEmail(scanner.nextLine());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("No se relizo ningun cambio");
                    }
                    break;
                case "2":
                    CambioEmail.setPrincipal(!CambioEmail.isPrincipal());
                    if (CambioEmail.isPrincipal()) {
                        System.out.println("Se ha añadido a los principales");
                    } else {
                        System.out.println("Se ha quitado de la lista de principales");
                    }
                    break;
                case "3":
                    CambioEmail.setActivo(!CambioEmail.isActivo());
                    if (CambioEmail.isActivo()) {
                        System.out.println("Se ha habilitado el contacto");
                    } else {
                        System.out.println("Se ha deshabilitado el contacto");
                    }
                    break;
                case "4":
                    if (CambioEmail.equals(email)) {
                        System.out.println("No se realizaron cambios");
                        return email;
                    }
                    try {
                        for (int i = 0; i < 3; i++) {
                            if (CambioEmail.actualizarEmail()) {
                                System.out.println("Cambios implementados con exito");
                                return CambioEmail;
                            }
                        }
                        System.out.println("No se pudo implementar los cambios, cancelando los mismos");
                        return email;

                    } catch (Exception e) {
                        System.out.println("Hubo un error al intentar implementar los cambios, cancelando los mismos");
                        return email;
                    }
                default:
            }
        } while (true);
    }
}
