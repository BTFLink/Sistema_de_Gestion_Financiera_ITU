/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo.itu.sistema_de_gestion_financiera_itu;

import static Connections.DDBBConnection.fetchData;
import DataModification.ModCliente;
import DataModification.ModDireccion;
import DataModification.ModEmail;
import DataModification.ModTelefono;
import Entity.Cliente;
import Entity.Direccion;
import Entity.Email;
import Entity.Telefono;
import static Utils.BringData.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author BTF
 */
public class BetaMain {

    static List<Cliente> listOfClientes = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static String LastIDUCreated;
    private static final String UNERR = "Error Inesperado, volviendo", INVALIDOPTION = "Opcion Invalida", INVALIDDATA = "Entrada Invalida", REGEXUUID = "^[0-9A-F]{45}$", REGEXNA = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s%_]{1,50}$", REGEXDNI = "^\\d{7,8}$";

    public static void main(String[] args) {
        //AutomaticAdd();
        getLIDUC();
        String menu = """
                      \tMenú De Clientes
                      1) Añadir Datos
                      2) Modificar Datos
                      3) Eliminar Datos
                      4) Mostrar Datos
                      5) HALLO :D
                      0) Salir
                      >""";
        String decision;
        String HiAnw;
        int Hi = 0;
        System.out.println("LIDUC = " + LastIDUCreated);
        boolean Running = true, OTO = true;//One Time Only - Solo una vez
        do {
            System.out.println("Welcome To The Menu");
            if (!OTO) {
                scanner.nextLine();
            }
            OTO = false;
            System.out.println(menu);
            decision = scanner.nextLine();

            switch (decision) {
                case "1":
                    AddMenu();
                    break;
                case "2":
                    ModMenu();
                    break;
                case "3":
                    DelCliente();
                    break;
                case "4":

                    break;
                case "5":
                    HiAnw = switch (Hi) {
                        case 0 ->
                            "Hi! Nice to Meet ya :D";
                        case 1 ->
                            "Hello! :3";
                        case 2 ->
                            "Hello? :/";
                        case 3 ->
                            ". . . º-º)";
                        default ->
                            "No body answered";
                    };
                    System.out.println(HiAnw);
                    Hi++;
                    break;

                case "0":
                    System.out.println("Exiting...");
                    Running = false;
                    break;
                default:
                    System.out.println("Opcion Invalida");
            }
        } while (Running);
    }

    private static void getLIDUC() {
        try {
            ResultSet rs = fetchData("SELECT idUnicoUsuario FROM cliente ORDER BY idUnicoUsuario DESC LIMIT 1");
            if (rs != null && rs.next()) {
                LastIDUCreated = rs.getString("idUnicoUsuario").toUpperCase();
                if (LastIDUCreated.matches("^[0-9A-F]{45}$")) {
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("LIDUC not found");
    }

    private static void AddCliente() {
        String Respuestas;
        boolean flag = false;

        //Ingresos de datos basicos del Cliente
        Cliente nclient = new Cliente();
        try {
            System.out.print("\nIngrese el Nombre del cliente\n> ");
            nclient.setNombre(scanner.nextLine());
            System.out.print("\nIngrese el Apellido del cliente\n> ");
            nclient.setApellido(scanner.nextLine());
            System.out.print("\nIngrese el DNI del cliente\n> ");
            nclient.setDni(scanner.nextInt());
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(UNERR);
            return;
        }

        //Revision de Antecedentes (Si extiste el DNI registrado previamente
        try {
            listOfClientes = Cliente.searchListOfClients(nclient.getDni(), 3);
            if (!listOfClientes.isEmpty()) {
                System.out.println("\nSe encontraron clientes con el mismo DNI");
                for (Cliente c : listOfClientes) {
                    c.showInformation();
                    if (c.isActivo()) {
                        flag = true;
                    }
                }
                if (flag) {
                    System.out.println("\nCuentas activas encontadas, no puede registrarse el cliente");
                    return;
                }
                System.out.print("\nNo se encontraron cuentas activas\n¿Confirmar Registro del cliente?\n (1) Si   (2) No\n>");
                do {
                    Respuestas = scanner.nextLine();
                    if (Respuestas.equals("2")) {
                        return;
                    } else if (!Respuestas.equals("1")) {
                        System.out.println(INVALIDOPTION);
                    }
                } while (!Respuestas.equals("1"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(UNERR);
            return;
        }

        //Intentar Registrar el Cliente
        try {
            do {
                for (int i = 0; i < 3; i++) {
                    flag = nclient.registrarCliente();
                    if (flag) {
                        break;
                    }
                }
                if (!flag) {
                    System.out.print("\nRegistro Fallido\n¿Reintentar registro del cliente?\n (1) Si   (2) No\n>");
                    do {
                        Respuestas = scanner.nextLine();
                        if (Respuestas.equals("2")) {
                            return;
                        } else if (!Respuestas.equals("1")) {
                            System.out.println(INVALIDOPTION);
                        }
                    } while (!Respuestas.equals("1"));
                }
            } while (!flag);
            for (int I = 0; I < 3; I++) {
                nclient.setIdUnicoUsuario(Cliente.solicitarUUID(nclient.getNombre(), nclient.getApellido(), nclient.getDni()));
                if (!nclient.getIdUnicoUsuario().equals("")) {
                    break;
                }
            }
            if (nclient.getIdUnicoUsuario().equals("")) {
                System.out.println("No se ha podido solicitar el UUID, no se pueden agregar mas datos");
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(UNERR);
            return;
        }

        //Añadir Telefono
        System.out.println("\nContinuando Registro de Cliente\n");

        Respuestas = "";
        flag = false;
        List<Telefono> listaDeTelefonos = new ArrayList<>();
        try {
            do {
                listaDeTelefonos.add(addTelefono(flag));
                System.out.println("\n¿Añadir otro telefono?\n (1) SI   (2) NO");
                do {
                    Respuestas = scanner.nextLine();
                    if (Respuestas.equals("2")) {
                        break;
                    } else if (!Respuestas.equals("1")) {
                        System.out.println(INVALIDOPTION);
                    }
                } while (!Respuestas.equals("1"));
            } while (!Respuestas.equals("2"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Añadir Direccion
        Direccion direccion;
        direccion = addDireccion();

        //Añadir Email
        Respuestas = "";
        flag = false;
        List<Email> listaDeEmails = new ArrayList<>();

        try {
            do {
                listaDeEmails.add(addEmail(flag));
                System.out.println("\n¿Añadir otro email?\n (1) SI   (2) NO");

                do {
                    Respuestas = scanner.nextLine();
                    if (Respuestas.equals("2")) {
                        break;
                    } else if (!Respuestas.equals("1")) {
                        System.out.println("Respuesta inválida");
                    }
                } while (!Respuestas.equals("1"));

            } while (!Respuestas.equals("2"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Registrando los datos
        direccion.setCliente_UUID(nclient.getIdUnicoUsuario());
        try {
            for (int I = 0; I < 3; I++) {
                if (direccion.registrarDireccion()) {
                    break;
                } else if (I == 2) {
                    System.out.println("No se logro registrar la direccion");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        for (Telefono t : listaDeTelefonos) {
            t.setcliente_UUID(nclient.getIdUnicoUsuario());
        }

        for (Email e : listaDeEmails) {
            e.setCliente_UUID(nclient.getIdUnicoUsuario());
        }

        int A = 1;
        try {
            for (Telefono t : listaDeTelefonos) {
                for (int I = 0; I < 3; I++) {
                    if (t.registrarTelefono()) {
                        break;
                    } else if (I == 2) {
                        System.out.println("No se logro registrar el " + A + "º telefono");
                    }
                }
                A++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        A = 1;
        try {
            for (Email e : listaDeEmails) {
                for (int I = 0; I < 3; I++) {
                    if (e.registrarEmail()) {
                        break;
                    } else if (I == 2) {
                        System.out.println("No se logro registrar el " + A + "º email");
                    }
                }
                A++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static Email addEmail(boolean flag) {
        String Respuestas;
        Email email = new Email();

        System.out.println("Ingrese el correo electrónico:");
        email.setEmail(scanner.nextLine());

        if (!flag) {
            while (!flag) {
                System.out.println("""
                               ¿Este es el email principal?
                                   (1) SI     (2) NO
                               """);
                Respuestas = scanner.nextLine();
                if (Respuestas.equals("2")) {
                    break;
                } else if (!Respuestas.equals("1")) {
                    System.out.println("Respuesta inválida");
                }
                flag = Respuestas.equals("1");
            }
            email.setPrincipal(flag);
        } else {
            email.setPrincipal(false);
        }

        email.setActivo(true);

        return email;
    }

    public static Direccion addDireccion() {
        Direccion direccion = new Direccion();

        System.out.println("Ingrese la calle:");
        direccion.setCalle(scanner.nextLine());

        System.out.println("Ingrese la numeración (\"S/N\" es valido):");
        direccion.setNumeracion(scanner.nextLine());

        System.out.println("Ingrese el piso:");
        direccion.setPiso(scanner.nextLine());

        System.out.println("Ingrese el código postal:");
        direccion.setCodigo_postal(scanner.nextLine());

        System.out.println("Ingrese la ciudad:");
        direccion.setCiudad(scanner.nextLine());

        System.out.println("Ingrese el departamento:");
        direccion.setDepartamento(scanner.nextLine());

        System.out.println("Ingrese la provincia:");
        direccion.setProvincia(scanner.nextLine());

        System.out.println("Ingrese el país:");
        direccion.setPais(scanner.nextLine());

        return direccion;
    }

    private static Telefono addTelefono(boolean flag) {
        String Respuestas;
        Telefono telefono = new Telefono();
        try {
            System.out.println("Ingrese el número del telefono");
            telefono.setNumero(scanner.nextLong());
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println(INVALIDDATA);
            return telefono;
        }

        if (!flag) {
            while (!flag) {
                System.out.println("""
                                   ¿Este es el número principal?
                                       (1) SI     (2) NO
                                   """);
                Respuestas = scanner.nextLine();
                if (Respuestas.equals("2")) {
                    break;
                } else if (!Respuestas.equals("1")) {
                    System.out.println("Respuesta Invalida");
                }
                flag = Respuestas.equals("1");
            }
            telefono.setPrincipal(flag);
        } else {
            telefono.setPrincipal(false);
        }
        telefono.setActivo(true);

        return telefono;
    }

    private static void AddMenu() {
        String menu = """
                    \tMenu de Adicciones
                    1) Añadir Cliente
                    2) Añadir Telefono
                    3) Añadir Email
                    4) Añadir Direccion
                    0) Volver al menu anterior
                    >""";
        String respuesta;
        String UUID;
        do {
            System.out.println(menu);
            respuesta = scanner.nextLine();
            switch (respuesta) {
                case "1":
                    AddCliente();
                    break;
                case "2":
                    System.out.println("Inserte el UUID del cliente al que agregara el telefono");
                    UUID = EnterUUID();
                    if (UUID.equals("")) {
                        break;
                    }
                    Telefono telefono = addTelefono(false);
                    telefono.setcliente_UUID(UUID);
                    try {
                        for (int i = 0; i < 3; i++) {
                            if (telefono.registrarTelefono()) {
                                System.out.println("Completado");
                                break;
                            }
                            System.out.println("Reintentando");
                        }

                    } catch (Exception e) {
                        System.out.println("Hubo un error al intentar el registro de datos");
                        System.out.println(e.getMessage());
                    }
                    break;
                case "3":
                    System.out.println("Inserte el UUID del cliente al que agregara el Email");
                    UUID = EnterUUID();
                    if (UUID.equals("")) {
                        break;
                    }
                    Email email = addEmail(false);
                    email.setCliente_UUID(UUID);
                    try {
                        for (int i = 0; i < 3; i++) {
                            if (email.registrarEmail()) {
                                System.out.println("Completado");
                                break;
                            }
                            System.out.println("Reintentando");
                        }

                    } catch (Exception e) {
                        System.out.println("Hubo un error al intentar el registro de datos");
                        System.out.println(e.getMessage());
                    }
                    break;
                case "4":
                    System.out.println("Inserte el UUID del cliente al que agregara el telefono");
                    UUID = EnterUUID();
                    if (UUID.equals("")) {
                        break;
                    }
                    Direccion direccion = addDireccion();
                    direccion.setCliente_UUID(UUID);
                    try {
                        for (int i = 0; i < 3; i++) {
                            if (direccion.registrarDireccion()) {
                                System.out.println("Completado");
                                break;
                            }
                            System.out.println("Reintentando");
                        }

                    } catch (Exception e) {
                        System.out.println("Hubo un error al intentar el registro de datos");
                        System.out.println(e.getMessage());
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println(INVALIDOPTION);
            }
        } while (true);
    }

    private static void ModMenu() {
        String menu = """
                    \tMenu de Modificaciones
                    1) Modificar Cliente
                    2) Modificar Telefono
                    3) Modificar Email
                    4) Modificar Direccion
                    5) Volver al menu anterior
                    >""";
        String respuesta;
        do {
            System.out.print(menu);
            respuesta = scanner.nextLine();
            switch (respuesta) {
                case "1":
                    ModCliente.ModCliente();
                    break;
                case "2":
                    ModTelefono.ModTelefono();
                    break;
                case "3":
                    ModEmail.ModEmail();
                    break;
                case "4":
                    ModDireccion.ModDireccion();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Opcion Invalida");
            }
        } while (!respuesta.equals("5"));
    }

    private static void DelCliente() {
        String menu = """
                      \tMenu de Eliminacion
                      1) Eliminar Cliente
                      2) Eliminar Telefono
                      3) Eliminar Email
                      4) Eliminar Direccion
                      0) Volver al menu anterior
                      """;
        String respuesta, UUID;
        do {
            respuesta = scanner.nextLine();
            switch (respuesta) {
                case "1":
                    ModCliente.deleteCliente();
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    break;
                case "0":
                    return;
                default:
                    throw new AssertionError();
            }
        } while (true);
    }

    

    private static void ShowDataMenu() {
        String menu = """
                    \tMenu de Informacion
                    1) Mostrar dato de un Cliente
                    2) Mostrar dato de un Telefono
                    3) Mostrar dato de un Email
                    4) Mostrar dato de una Direccion
                    5) Mostrar todos los clientes
                    0) Volver al menu anterior
                    >""";
        String respuesta;
        do {
            System.out.println(menu);
            respuesta = scanner.nextLine();
            switch (respuesta) {
                case "1":
                    bringAllClientData();
                    break;
                case "2":
                    bringTelefonoData();
                    break;
                case "3":
                    bringEmailData();
                    break;
                case "4":
                    bringDireccionData();
                    break;
                case "5":
                    bringALLCLIENTS();
                    break;
                case "0":
                    return;
                default:
                    System.out.println(INVALIDOPTION);
            }
        } while (true);
    }

    private static String EnterUUID() {
        String UUID = "";
        System.out.println("Ingrese el UUID del cliente");
        for (int i = 0; i < 3; i++) {
            UUID = scanner.nextLine();
            if (UUID.matches(REGEXUUID)) {
                break;
            }
            System.out.println(INVALIDDATA);
        }

        if (!UUID.matches(REGEXUUID)) {
            System.out.println("Intentos agotados");
            UUID = "";
        }
        return UUID;
    }
}
