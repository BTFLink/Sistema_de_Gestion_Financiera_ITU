/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo.itu.sistema_de_gestion_financiera_itu;

import Entity.Cliente;
import static Utils.LeerDataType.*;
import java.util.Scanner;

/**
 *
 * @author BTF
 */
public class MenuCliente {
//Valores basicos para el funcionamiento y regulacion del programa

    static Scanner scanner = new Scanner(System.in);
    static String respuesta;
    private static final String UNERR = "Error Inesperado, volviendo", INVALIDOPTION = "Opcion Invalida",
            INVALIDDATA = "Entrada Invalida", REGEXUUID = "^CLI-[0-9A-Fa-f]{16}$",
            REGEXNA = "^[a-zA-Z·ÈÌÛ˙¡…Õ”⁄Ò—\\s%_]{1,50}$",
            REGEXEMAIL = "^(?=.{1,255}$)[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public static void main() {
        String menu = """
                    \tMenu Datos Cliente
                    1) Registrar Cliente
                    2) Modificar Cliente
                    3) Consultar Cliente
                    4) Traer Lista de Clientes registrados
                    0) Salir
                    """;
        do {
            System.out.println(menu);
            respuesta = scanner.nextLine();
            switch (respuesta) {//Menu de opciones
                case "1":
                    registrarCliente();
                    break;
                case "2":
                    editarCliente();
                    break;
                case "3":
                    System.out.println("Ingerse el UUID del cliente:");
                    Cliente.searchAClient(LeerUUID("CLI", scanner.nextLine())).showClientData(false);
                    break;
                case "4":
                    Cliente.showClientList(Cliente.traerTodos());
                    break;
                case "0":
                    return;
                default:
                    System.out.println(INVALIDOPTION);
            }
        } while (true);
    }

    /* Para pruebas de Codificacion
    private static void EnTE() {
         System.out.println("Encoding actual: " + System.getProperty("file.encoding"));
        String nombre = scanner.nextLine();
        for (int i = 0; i < nombre.length(); i++) {
            System.out.println("Char en posiciÛn "+i+": " + nombre.charAt(i));
            System.out.println("CÛdigo Unicode: " + (int) nombre.charAt(i));
        }
    }
    */
    private static void registrarCliente() {
        Cliente cliente = new Cliente();
        boolean nombre = false, dni = false, direccion = false, email = false, telefono = false, compleate;
        do {
            System.out.println("Puede cancelar el registro ingresando el valor 0 donde no haya *");
            if (!nombre) {
                System.out.println("Ingrese nombre y apellido del cliente (50 char m·x)");
                cliente.setNombre(scanner.nextLine());
                if(cliente.getNombre().equals("0")){return;}
            }

            if (!dni) {
                System.out.println("Ingrese el DNI del cliente*");
                cliente.setDni(LeerInt(1000000, 80000000));
            }

            if (!direccion) {
                System.out.println("Ingrese direccion del cliente");
                cliente.setDireccion(scanner.nextLine());
                if(cliente.getDireccion().equals("0")){return;}
            }

            if (!email) {
                System.out.println("Ingrese el correo electronico del cliente");
                cliente.setCorreoElectronico(scanner.nextLine());
                if(cliente.getCorreoElectronico().equals("0")){return;}
            }

            if (!telefono) {
                cliente.setTelefono(LeerLong("Ingrese el telefono del cliente"));
                if(cliente.getTelefono()==0L){return;}
            }

            //Esta seccion verifica que todos los valores ingresado para cliente sean validos antes de continuar
            nombre = cliente.getNombre().matches(REGEXNA);
            dni = cliente.getDni() >= 1000000;
            email = cliente.getCorreoElectronico().matches(REGEXEMAIL);
            direccion = !cliente.getDireccion().equals("");
            telefono = (cliente.getTelefono() > 1000000000L && cliente.getTelefono() < 6000000000L);
            compleate = nombre && email && direccion && telefono && dni;

            if (!compleate) {
                System.out.println("Hay uno o mas datos mal ingresados");
            }
        } while (!compleate);

        //Aqui se intenta registrar al cliente, tiene 3 intentos en caso de fallo, si no funciona avisa de un fallo en el registro
        try {
            for (int i = 0; i < 3; i++) {
                if (cliente.registrarCliente()) {
                    System.out.println("Registro Completado");
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println(UNERR);
            System.out.println(e.getMessage());
        }
        System.out.println("El registro ha fallado");
    }

    private static void editarCliente() {
        System.out.println("Ingrese el UUID del cliente");
        String UUID = LeerUUID("CLI", scanner.nextLine()); //Se verifica si el UUID ingresado es valido, o sale del editor en caso de no serlo
        if (UUID.equals("")||UUID.equals("0")) {
            return;
        }
        Cliente cliente = Cliente.searchAClient(UUID);
        if (cliente.getIdCliente().equals("")) {
            System.out.println("No se encontro el cliente");
            return;
        }
        String menu = String.format("""
                                  \tEditando al Cliente: %s
                                  1) Editar Nombre
                                  2) Editar Direccion
                                  3) Editar Correo Electronico
                                  4) Editar Telefono
                                  5) Editar DNI
                                  0) Salir
                                  """, cliente.getIdCliente());
        String respuesta, newString, oldString = "";
        long newLong, oldLong = 0L;
        int newInt, oldInt = 0;
        boolean change, valid;
        do {
            change = false;
            valid = true;
            System.out.println(menu);
            respuesta = scanner.nextLine();

            //Se elige lo que se desea editar, en caso de ingresar un mismo valor o uno invalido el programa no realizara ningun cambio
            switch (respuesta) {
                case "1":
                    System.out.println("Ingrese nombre y apellido del cliente (50 char max)");
                    System.out.println("Valor actual: "+cliente.getNombre());
                    newString = scanner.nextLine();
                    if (!newString.equals(cliente.getNombre()) && newString.matches(REGEXNA)) {
                        oldString = cliente.getNombre();
                        cliente.setNombre(newString);
                        change = true;
                    }
                    break;
                case "2":
                    System.out.println("Ingrese direccion del cliente");
                    System.out.println("Valor actual: "+cliente.getDireccion());
                    newString = scanner.nextLine();
                    if (!newString.equals(cliente.getDireccion()) && !newString.equals("")) {
                        oldString = cliente.getDireccion();
                        cliente.setDireccion(newString);
                        change = true;
                    }
                    break;
                case "3":
                    System.out.println("Ingrese el correo electronico del cliente");
                    System.out.println("Valor actual: "+cliente.getCorreoElectronico());
                    newString = scanner.nextLine();
                    if (!newString.equals(cliente.getCorreoElectronico()) && newString.matches(REGEXEMAIL)) {
                        oldString = cliente.getCorreoElectronico();
                        cliente.setCorreoElectronico(newString);
                        change = true;
                    }
                    break;
                case "4":
                    System.out.println("Valor actual: "+cliente.getTelefono());
                    newLong = LeerLong("Ingrese el telefono del cliente");
                    if (newLong != cliente.getTelefono() && (newLong > 1000000000L && newLong < 6000000000L)) {
                        oldLong = cliente.getTelefono();
                        cliente.setTelefono(newLong);
                        change = true;
                    }
                    break;
                case "5":
                    System.out.println("Valor actual: "+cliente.getDni());
                    newInt = LeerInt("Ingrese el DNI del cliente", 1000000, 80000000);
                    if (newInt != cliente.getDni() && newInt >= 1000000) {
                        oldInt = cliente.getDni();
                        cliente.setDni(newInt);
                        change = true;
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println(INVALIDOPTION);
                    valid = false;
            }

            if (change) { //Si se detecta un cambio se enviara a la base de datos con 3 intentos como maximo
                try {
                    for (int i = 0; i < 3; i++) {
                        if (cliente.actualizarCliente()) {
                            change = false;
                            break;
                        }
                        if (i == 2) {
                            System.out.println("No pudo actualizarse el cliente, revirtiendo cambios");
                        }
                    }
                } catch (Exception e) {
                    System.out.println(UNERR);
                    System.out.println(e.getMessage());
                    System.out.println("No pudo actualizarse el cliente, revirtiendo cambios");
                }
                if (change) { //En caso de que la actualizacion haya fallado los cambios se revierten para no mostrar valores erroneos
                    switch (respuesta) {
                        case "1":
                            cliente.setNombre(oldString);
                            break;
                        case "2":
                            cliente.setDireccion(oldString);
                            break;
                        case "3":
                            cliente.setCorreoElectronico(oldString);
                            break;
                        case "4":
                            cliente.setTelefono(oldLong);
                            break;
                        case "5":
                            cliente.setDni(oldInt);
                    }
                }
            } else if (valid) {
                System.out.println(INVALIDDATA);
            }

        } while (true);
    }

    /*
    Codigo inutilizado (Remplazado por LeerUUID en LeerDataType)
    
    private static String EnterUUID() { //Verificador de UUID
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
     */
}
