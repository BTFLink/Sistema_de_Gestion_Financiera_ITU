/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModification;

import Entity.Cliente;
import static Connections.DDBBConnection.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author BTF
 */
public class ModCliente {

    private static final Scanner scanner = new Scanner(System.in);
    private static String respuesta;
    private static final String INVALIDOPTION = "Opcion Invalida", INVALIDDATA = "Entrada Invalida", REGEXUUID = "^[0-9A-F]{45}$", REGEXNA = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s%_]{1,50}$", REGEXDNI = "^\\d{7,8}$";

    public static void ModCliente() {
        int searchActivo;
        do {
            searchActivo = 1;
            System.out.println("""
                           \tElegir Modo de busqueda del cliente
                           1) Busqueda Por ID Usuario Unico
                           2) Busqueda Por Nombre y Apellido
                           3) Busqueda Por DNI
                           4) Salir
                           """);
            respuesta = scanner.nextLine();
            switch (respuesta) {
                case "2", "3":
                    System.out.println("""
                                       \tElegir filtrar clientes por estado
                                       1) Activo
                                       2) Inactivo
                                       3) No Filtrar
                                       4) Cancelar
                                       """);
                    do {
                        try {
                            searchActivo = scanner.nextInt();
                            scanner.nextLine();
                        } catch (Exception e) {
                            System.out.println(INVALIDDATA);
                            searchActivo = 0;
                        }
                        if (searchActivo > 4 || searchActivo < 1) {
                            System.out.println(INVALIDOPTION);
                        }
                    } while (searchActivo > 4 || searchActivo < 1);
                    if (searchActivo == 4) {
                        break;
                    }
                case "1":
                    ModClienteP2(Integer.parseInt(respuesta), searchActivo);
                    break;
                case "4":
                    return;
                default:
                    System.out.println(INVALIDOPTION);
            }
        } while (true);

    }

    private static void ModClienteP2(int TypeSearch, int TruthState) {

        boolean validToContinue = false;
        String valueToSearch = "";
        String nombre = "", apellido = "";
        int attempts = 0;
        do {
            switch (TypeSearch) {
                case 1:
                    System.out.println("Ingrese el UUID del usuario");
                    break;
                case 2:
                    System.out.println("Ingrese el Nombre del usuario");
                    nombre = scanner.nextLine();
                    System.out.println("Ingrese el Apellido del usuario");
                    apellido = scanner.nextLine();

                    break;
                case 3:
                    System.out.println("Ingrese el DNI del usuario");
            }
            if (TypeSearch != 2) {
                valueToSearch = scanner.nextLine();
            }

            if ((valueToSearch.matches(REGEXUUID) && TypeSearch == 1)
                    || ((nombre.matches(REGEXNA) && apellido.matches(REGEXNA)) && TypeSearch == 2)
                    || (valueToSearch.matches(REGEXDNI) && TypeSearch == 3)) {
                validToContinue = true;
            }//^[A-Za-zÁÉÍÓÚáéíóúÑñÜü% ]{2,50}$
            if (!validToContinue && attempts < 3) {
                System.out.println(INVALIDDATA);
                attempts++;
            } else if (!validToContinue) {
                System.out.println("Muchos intentos fallidos, Regresando a menu anterior");
                return;
            }
        } while (!validToContinue);
        if (TypeSearch == 1) {
            try {
                ModClienteP4(Cliente.searchOneClient(valueToSearch));
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        }

        if (TruthState == 1) {
            if (TypeSearch == 2) {
                try {
                    ModClienteP4(Cliente.searchOneClient(nombre, apellido));
                    return;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
            }
            try {
                ModClienteP4(Cliente.searchOneClient(Integer.parseInt(valueToSearch)));
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        } else {
            if (TypeSearch == 2) {
                try {
                    ModClienteP3(Cliente.searchListOfClients(nombre, apellido, TruthState));
                    return;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
            }
            try {
                ModClienteP3(Cliente.searchListOfClients(Integer.parseInt(valueToSearch), TruthState));
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    private static void ModClienteP3(List<Cliente> listOfClients) {
        String menu = """
                    \t Menu Modificacion Telefonos
                    (1) Elegir Telefono a modificar
                    (2) Mostrar lista
                    (3) Salir
                    """;
        int index;
        Cliente.showIndexedListInformation(listOfClients);
        do {
            System.out.println(menu);
            respuesta = scanner.nextLine();
            switch (respuesta) {
                case "1":
                    System.out.println("Ingrese el indice del telefono que desea editar (Index: indice)");
                    try {
                        index = scanner.nextInt();
                        scanner.nextLine();
                        if (index <= listOfClients.size() && index > 0) {
                            listOfClients.set(index - 1, ModClienteP4(listOfClients.get(index - 1)));
                        } else {
                            System.out.println(INVALIDDATA);
                        }
                    } catch (Exception e) {
                        System.out.println(INVALIDDATA);
                    }
                    break;
                case "2":
                    Cliente.showIndexedListInformation(listOfClients);
                    break;
                case "3":
                    return;
                default:
                    System.out.println(INVALIDOPTION);
            }
        } while (true);
    }

    private static Cliente ModClienteP4(Cliente previousCliente) {
        Cliente CambioCliente = SetCCliente(previousCliente);
        do {
            CambioCliente.showInformation();
            System.out.println("""
                               ¿Que va a cambiar?
                               (1) Nombre
                               (2) Apellido
                               (3) DNI
                               (4) Salir e Implementar
                               """);
            respuesta = scanner.nextLine();
            switch (respuesta) {
                case "1":
                    System.out.println("Ingrese el nuevo nombre");
                    CambioCliente.setNombre(scanner.nextLine());
                    if (CambioCliente.getNombre().matches(REGEXNA)) {
                        System.out.println("Cambio realizado");
                    } else {
                        System.out.println(INVALIDDATA + "\nCancelando cambio");
                        CambioCliente.setNombre(previousCliente.getNombre());
                    }
                    break;
                case "2":
                    System.out.println("Ingrese el nuevo apellido");
                    CambioCliente.setApellido(scanner.nextLine());
                    if (CambioCliente.getApellido().matches(REGEXNA)) {
                        System.out.println("Cambio realizado");
                    } else {
                        System.out.println(INVALIDDATA + "\nCancelando cambio");
                        CambioCliente.setApellido(previousCliente.getApellido());
                    }
                    break;
                case "3":
                    System.out.println("Ingrese el nuevo DNI");
                    try {
                        CambioCliente.setDni(scanner.nextInt());
                        if (String.valueOf(CambioCliente.getDni()).matches(REGEXDNI)) {
                            System.out.println("Cambio realizado");
                        } else {
                            System.out.println(INVALIDDATA + "\nCancelando cambio");
                            CambioCliente.setDni(previousCliente.getDni());
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println(INVALIDDATA + "\nCancelando cambio");
                        CambioCliente.setDni(previousCliente.getDni());
                    }
                    break;
                case "4":
                    if (CambioCliente.equals(previousCliente)) {
                        System.out.println("No se realizaron cambios");
                        return previousCliente;
                    }
                    try {
                        for (int i = 0; i < 3; i++) {
                            if (CambioCliente.actualizarCliente()) {
                                System.out.println("Cambios implementados con exito");
                                return CambioCliente;
                            }
                        }
                        System.out.println("No se pudo implementar los cambios, cancelando los mismos");
                        return previousCliente;

                    } catch (Exception e) {
                        System.out.println("Hubo un error al intentar implementar los cambios, cancelando los mismos");
                        return previousCliente;
                    }
                default:
                    System.out.println(INVALIDOPTION);
            }
        } while (true);
    }

    private static Cliente SetCCliente(Cliente cliente) {
        Cliente cliente2 = new Cliente();
        cliente2.setNombre(cliente.getNombre());
        cliente2.setApellido(cliente.getApellido());
        cliente2.setDni(cliente.getDni());
        cliente2.setIdUnicoUsuario(cliente.getIdUnicoUsuario());
        cliente2.setActivo(cliente.isActivo());
        cliente2.setFechaDeRegistro(cliente.getFechaDeRegistro());
        cliente2.setId(cliente.getId());
        return cliente2;
    }

    public static void deleteCliente() {
        String clienteUUID = "";
        System.out.println("Ingrese el UUID del cliente");
        for (int i = 0; i < 3; i++) {
            clienteUUID = scanner.nextLine();
            if (clienteUUID.matches(REGEXUUID)) {
                break;
            }
            System.out.println(INVALIDDATA);
        }

        if (!clienteUUID.matches(REGEXUUID)) {
            System.out.println("Intentos agotados");
            return;
        }

        List<String> tablas = Arrays.asList("cliente", "direcciones", "telefono", "email");
        List<String> campos = Arrays.asList("idUnicoUsuario", "cliente_idUnicoUsuario", "cliente_idUnicoUsuario", "cliente_idUnicoUsuario");

        List<String> tablasANegar = buscarTablasConValor(clienteUUID, tablas, campos);
        
        if(tablasANegar.isEmpty()){
            System.out.println("No se encontraron datos para eliminar");
            return;
        }
        List<String> queries = new ArrayList<>();
        
        queries.add("UPDATE `sistema_financiero`.`cliente` SET `activo` = '0' WHERE `idUnicoUsuario` = '"+clienteUUID+"';");
        if(tablasANegar.contains("direcciones")){
            queries.add("UPDATE `sistema_financiero`.`direcciones` SET `activo` = '0' WHERE `cliente_idUnicoUsuario` = '"+clienteUUID+"';");
        }
        if(tablasANegar.contains("email")){
            queries.add("UPDATE `sistema_financiero`.`email` SET `activo` = '0' WHERE `cliente_idUnicoUsuario` = '"+clienteUUID+"';");
        }
        if(tablasANegar.contains("telefono")){
            queries.add("UPDATE `sistema_financiero`.`telefono` SET `activo` = '0' WHERE `cliente_idUnicoUsuario` = '"+clienteUUID+"';");
        }
        
        System.out.println(ejecutarTransaccion(queries));
        //System.out.println("Resultado de Actualizacion: " + Respuesta);
        
    }

}
