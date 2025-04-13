/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo.itu.sistema_de_gestion_financiera_itu;

import static Connections.DDBBConnection.fetchData;
import Entity.Cliente;
import Entity.Telefono;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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

    public static void main(String[] args) {
        //AutomaticAdd();
        getLIDUC();
        String decision;
        String HiAnw;
        int Hi = 0;
        System.out.println("LIDUC = " + LastIDUCreated);
        boolean Running = true, OTO = true;//One Time Only - Solo una vez
        do {
            if (!OTO) {
                scanner.nextLine();
            }
            OTO = false;
            Menu();
            decision = scanner.nextLine();

            switch (decision) {
                case "1":
                    AddCliente();
                    break;
                case "2":
                    ModCliente();
                    break;
                case "3":
                    DelCliente();
                    break;
                case "4":
                    switch (Hi) {
                        case 0:
                            HiAnw = "Hi! Nice to Meet ya :D";
                        case 1:
                            HiAnw = "Hello! :3";
                        case 2:
                            HiAnw = "Hello? :/";
                        case 3:
                            HiAnw = ". . . º-º)";
                        default:
                            HiAnw = "No body answered";
                    }
                    System.out.println(HiAnw);
                    Hi++;
                    break;
                case "5":
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

    private static void Menu() {
        System.out.print("""
                           \tMenú
                           1) Añadir Cliente
                           2) Modificar Cliente
                           3) Eliminar Cliente
                           4) HALLO :D
                           5) Salir
                           >
                           """);
    }

    private static void AddCliente() {
        String Respuestas = "";
        String nombre, apellido;
        int dni;
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
            return;
        }
        //Revision de Antecedentes (Si extiste el DNI registrado previamente
        try {
            listOfClientes = Cliente.searchListOfClients(nclient.getDni());
            if (!listOfClientes.isEmpty()) {
                System.out.println("\nSe encontraron clientes con el mismo DNI");
                for (Cliente c : listOfClientes) {
                    c.showData();
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
                        System.out.println("Respuesta Invalida");
                    }
                } while (!Respuestas.equals("1"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Intentar Registrar el Cliente
        try {
            do {
                flag = nclient.registrarCliente();
                if (!flag) {
                    System.out.print("\nRegistro Fallido\n¿Reintentar registro del cliente?\n (1) Si   (2) No\n>");
                    do {
                        Respuestas = scanner.nextLine();
                        if (Respuestas.equals("2")) {
                            return;
                        } else if (!Respuestas.equals("1")) {
                            System.out.println("Respuesta Invalida");
                        }
                    } while (!Respuestas.equals("1"));
                }
            } while (!flag);
            for(int I=0; I<3;I++){
                nclient.setIdUnicoUsuario(Cliente.solicitarUUID(nclient.getNombre(), nclient.getApellido(), nclient.getDni()));
                if(!nclient.getIdUnicoUsuario().equals("")){
                    break;
                }
            }
            if(!nclient.getIdUnicoUsuario().equals("")){
                System.out.println("No se ha podido solicitar el UUID, no se pueden agregar mas datos");
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
                        System.out.println("Respuesta Invalida");
                    }
                } while (!Respuestas.equals("1"));
            } while (!Respuestas.equals("2"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        
        
        
        
        
    }

    private static Telefono addTelefono(boolean flag) {
        String Respuestas;
        Telefono telefono = new Telefono();
        System.out.println("Ingrese el número del telefono");
        telefono.setNumero(scanner.nextLong());
        scanner.nextLine();
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

    private static Cliente SearchCliente() {
        int dnisearch, index;
        boolean found = false;
        Cliente client = null;
        System.out.print("Ingrese el dni del cliente a modificar\n>");
        dnisearch = scanner.nextInt();
        for (Cliente c : listOfClientes) {
            if (c.getDni() == dnisearch) {
                found = true;
                client = c;
            }
        }
        if (found) {
            System.out.println("Cliente encontrado");
        } else {
            System.out.println("No se encontro el cliente solicitado");
        }
        return client;
    }

    private static void ModCliente() {
        Cliente mdcliente = SearchCliente();
        if (mdcliente != null) {

        }
    }

    private static void DelCliente() {
        int dnisearch, index;
        boolean found = false;
        System.out.print("Ingrese el dni del cliente a eliminar\n>");
        dnisearch = scanner.nextInt();
        for (Cliente c : listOfClientes) {
            if (c.getDni() == dnisearch) {
                found = true;
                index = listOfClientes.indexOf(c);
            }
        }
        if (found) {
            System.out.println("Cliente encontrado");
        } else {
            System.out.println("No se encontro el cliente solicitado");
        }
    }

    private static void searchListOfClientsS(String WHERE) {
        try {
            System.out.println("\nBuscando Registros\n");
            String previousSearch = "SELECT * FROM cliente " + WHERE;
            ResultSet rs = fetchData(previousSearch);
            if (rs != null) {
                if (rs.next()) {
                    do {
                        Cliente A01 = new Cliente(rs.getString("nombre"), rs.getString("apellido"), rs.getString("idUnicoUsuario"), rs.getInt("dni"), rs.getBoolean("activo"), rs.getTimestamp("fecha_registro").toLocalDateTime());
                        listOfClientes.add(A01);
                    } while (rs.next());
                }
            } else {
                System.out.println("Error al solicitar datos");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
