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
        boolean registrated = false;
        //Ingresos de datos basicos del Cliente
        try {
            System.out.print("\nIngrese el Nombre del cliente\n> ");
            nombre = scanner.nextLine();
            System.out.print("\nIngrese el Apellido del cliente\n> ");
            apellido = scanner.nextLine();
            System.out.print("\nIngrese el DNI del cliente\n> ");
            dni = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        //Busqueda de registros previos de clientes
        try {
            System.out.println("\nBuscando Registros Previos\n");
            String previousSearch = "SELECT nombre,apellido,activo,idUnicoUsuario,fecha_registro FROM cliente WHERE dni = '" + dni + "'";
            ResultSet rs = fetchData(previousSearch);
            if (rs != null) {
                if (rs.next()) {
                    System.out.println("Datos encontrados\nPreparando Lista\n");
                    do {
                        Cliente A01 = new Cliente(rs.getString("nombre"), rs.getString("apellido"), rs.getString("idUnicoUsuario"), dni, rs.getBoolean("activo"), rs.getTimestamp("fecha_registro").toLocalDateTime());
                        listOfClientes.add(A01);
                    } while (rs.next());
                    for (Cliente c : listOfClientes) {
                        c.showData();
                        if (c.isActivo()) {
                            registrated = true;
                        }
                    }
                    System.out.print("\n>");
                    scanner.nextLine();
                    if (registrated) {
                        System.out.println("""
                                           Cuenta activa detectada
                                           No se puede continuar el registro del cliente
                                           """);
                        return;
                    }
                    do {
                        System.out.print("""
                                            No se detectaron cuentas activas
                                            ¿Desea continuar?
                                            (1) Si   (2) No
                                        >""");
                        Respuestas = scanner.nextLine();
                        if (Respuestas.equals("2")) {
                            System.out.println("Saliendo");
                            return;
                        } else if (!Respuestas.equals("1")) {
                            System.out.println("Respuesta Invalida");
                        }
                    } while (!Respuestas.equals("1"));
                } else {
                    System.out.println("Datos no encontrados");
                }
            } else {
                System.out.println("""
                                   Se obtuvo un nulo del servidor
                                   Se recomienda negar el registro del cliente
                                   y reintentarlo mas tarde
                                   """);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //Continuacion de Registro de Cliente
        System.out.println("\nContinuando Registro de Cliente\n");

        Respuestas="";
        try {
            do {
                Telefono telefono = new Telefono();
                System.out.println("Ingrese el numero del telefono");
                telefono.setNumero(scanner.nextLong());
                scanner.nextLine();
                do {                    
                    
                } while (registrated);
            } while (registrated);
        } catch (Exception e) {
        }

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

}
