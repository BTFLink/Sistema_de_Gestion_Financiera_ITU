/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import static Connections.DDBBConnection.fetchData;
import Entity.Cliente;
import Entity.Direccion;
import Entity.Email;
import Entity.Telefono;
import static Utils.ResultSetUtils.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author BTF
 */
public class BringData {

    static Scanner scanner = new Scanner(System.in);
    private static final String UNERR = "Error Inesperado, volviendo", INVALIDOPTION = "Opcion Invalida", INVALIDDATA = "Entrada Invalida", REGEXUUID = "^[0-9A-F]{45}$", REGEXNA = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s%_]{1,50}$", REGEXDNI = "^\\d{7,8}$", REGEXEMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

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

    public static void bringAllClientData() {
        String clienteUUID = EnterUUID();

        Cliente cliente = Cliente.searchOneClient(clienteUUID);
        if (!cliente.getIdUnicoUsuario().equals(clienteUUID)) {
            System.out.println("No se encontro el cliente");
            return;
        }
        List<Telefono> telefonos = Telefono.searchListTelefonoByUUID(clienteUUID);
        List<Email> emails = Email.searchListEmailByUUID(clienteUUID);
        List<Direccion> direcciones = Direccion.searchDireccionPorUUID(clienteUUID);

        System.out.println("Datos del Cliente");
        cliente.showInformation();
        System.out.println("-----------------------------------");
        System.out.println("Datos de direcciones");
        for (Direccion dir : direcciones) {
            dir.showDireccion();
        }
        System.out.println("-----------------------------------");
        System.out.println("Datos de telefonos");
        for (Telefono tel : telefonos) {
            tel.showInformation();
        }
        System.out.println("-----------------------------------");
        System.out.println("Datos de emails");
        for (Email email : emails) {
            email.showInformation();
        }

    }

    public static void bringTelefonoData() {
        String respuesta;
        boolean validToContinue;
        String UUID = "";
        long numero = -1L;
        List<String> Data = new ArrayList<>();
        do {
            validToContinue = false;
            do {
                System.out.println("""
                               \tTraer Informacion de Telefonos
                               1) Por cliente (UUID)
                               2) Por numero de telefono
                               0) Volver
                               """);
                respuesta = scanner.nextLine();
                switch (respuesta) {
                    case "1":
                        UUID = EnterUUID();
                        break;
                    case "2":
                        try {
                            System.out.println("Ingrese el numero de telefono");
                            numero = scanner.nextLong();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            System.out.println(INVALIDDATA);
                            numero = -1L;
                        }
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println(INVALIDOPTION);
                }
                if (!UUID.equals("") || (numero > 1000000000L && numero < 9999999999L)) {
                    validToContinue = true;
                } else {
                    System.out.println(INVALIDDATA);
                    numero = -1L;
                }
            } while (!validToContinue);

            for (int i = 0; i < 3; i++) {
                if (!UUID.equals("")) {
                    Data = searchFullListTelefonoByUUID(UUID);
                } else {
                    Data = searchFullListTelefonoByNumber(numero);
                }

                if (!Data.isEmpty()) {
                    break;
                }
            }

            if (!Data.isEmpty()) {
                for (String dta : Data) {
                    System.out.println(dta);
                    System.out.println("-------------------");
                }
            } else {
                System.out.println("No hay datos para mostrar");
            }

        } while (true);
    }

    private static List<String> searchFullListTelefonoByNumber(long numero) {
        return searchFullListTelefono("WHERE numero = " + numero + "';");
    }

    private static List<String> searchFullListTelefonoByUUID(String UUID) {
        return searchFullListTelefono("WHERE cliente_idUnicoUsuario = '" + UUID + "';");
    }

    private static List<String> searchFullListTelefono(String WHERE) {
        String Query = "SELECT t.*, c.*, t.activo AS t_activo, c.activo AS c_activo FROM telefono t JOIN cliente c ON t.cliente_idUnicoUsuario = c.idUnicoUsuario " + WHERE;
        List<String> Datos = new ArrayList<>();

        try {
            ResultSet rs = fetchData(Query);
            while (rs.next()) {
                String nombre = getStringSafe(rs, "nombre");
                String apellido = getStringSafe(rs, "apellido");
                String UUID = getStringSafe(rs, "idUnicoUsuario");
                String numero = getStringSafe(rs, "numero");
                String cliActivo = getBooleanSafe(rs, "c_activo") ? "Si" : "No";
                String telActivo = getBooleanSafe(rs, "t_activo") ? "Si" : "No";
                String principal = getBooleanSafe(rs, "principal") ? "Si" : "No";
                String Fila = String.format("""
                                            Dueño: %s %s
                                            UUID: %s
                                            Cliente Activo: %s
                                            Numero: %s
                                            Es el Principal: %s
                                            Numero Activo: %s
                                            """, nombre, apellido, UUID, cliActivo, numero, telActivo, principal);
                Datos.add(Fila);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Datos;
    }

    public static void bringEmailData() {
        String respuesta;
        boolean validToContinue;
        String UUID = "";
        String email = "";
        List<String> Data = new ArrayList<>();
        do {
            validToContinue = false;
            do {
                System.out.println("""
                               \tTraer Informacion de Emails
                               1) Por cliente (UUID)
                               2) Por Email
                               0) Volver
                               """);
                respuesta = scanner.nextLine();
                switch (respuesta) {
                    case "1":
                        UUID = EnterUUID();
                        break;
                    case "2":
                        try {
                            System.out.println("Ingrese el email");
                            email = scanner.nextLine();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            System.out.println(INVALIDDATA);
                            email = "";
                        }
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println(INVALIDOPTION);
                }
                if (!UUID.equals("") || email.matches(REGEXEMAIL)) {
                    validToContinue = true;
                } else {
                    System.out.println(INVALIDDATA);
                    email = "";
                }
            } while (!validToContinue);

            for (int i = 0; i < 3; i++) {
                if (!UUID.equals("")) {
                    Data = searchFullListEmailByUUID(UUID);
                } else {
                    Data = searchFullListEmailByEmail(email);
                }

                if (!Data.isEmpty()) {
                    break;
                }
            }

            if (!Data.isEmpty()) {
                for (String dta : Data) {
                    System.out.println(dta);
                    System.out.println("-------------------");
                }
            } else {
                System.out.println("No hay datos para mostrar");
            }

        } while (true);
    }

    private static List<String> searchFullListEmailByEmail(String email) {
        return searchFullListEmail("WHERE email = " + email + "';");
    }

    private static List<String> searchFullListEmailByUUID(String UUID) {
        return searchFullListEmail("WHERE cliente_idUnicoUsuario = '" + UUID + "';");
    }

    private static List<String> searchFullListEmail(String WHERE) {
        String Query = "SELECT e.*, c.*, e.activo AS e_activo, c.activo AS c_activo FROM email e JOIN cliente c ON e.cliente_idUnicoUsuario = c.idUnicoUsuario " + WHERE;
        List<String> Datos = new ArrayList<>();

        try {
            ResultSet rs = fetchData(Query);
            while (rs.next()) {
                String nombre = getStringSafe(rs, "nombre");
                String apellido = getStringSafe(rs, "apellido");
                String UUID = getStringSafe(rs, "idUnicoUsuario");
                String email = getStringSafe(rs, "email");
                String cliActivo = getBooleanSafe(rs, "c_activo") ? "Si" : "No";
                String emaActivo = getBooleanSafe(rs, "e_activo") ? "Si" : "No";
                String principal = getBooleanSafe(rs, "principal") ? "Si" : "No";
                String Fila = String.format("""
                                            Dueño: %s %s
                                            UUID: %s
                                            Cliente Activo: %s
                                            Email: %s
                                            Es el Principal: %s
                                            Email Activo: %s
                                            """, nombre, apellido, UUID, cliActivo, email, emaActivo, principal);
                Datos.add(Fila);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Datos;
    }

    public static void bringDireccionData() {
        String UUID = EnterUUID();
        if (UUID.equals("")) {
            System.out.println(INVALIDDATA);
            return;
        }
        String Query = "SELECT d.*, c.*, d.activo AS d_activo, c.activo AS c_activo FROM direcciones d JOIN cliente c ON d.cliente_idUnicoUsuario = c.idUnicoUsuario WHERE cliente_idUnicoUsuario = '" + UUID + "';";
        List<String> Datos = new ArrayList<>();

        try {
            ResultSet rs = fetchData(Query);
            while (rs.next()) {
                String nombre = getStringSafe(rs, "nombre");
                String apellido = getStringSafe(rs, "apellido");
                String calle = getStringSafe(rs, "calle");
                String numeracion = getStringSafe(rs, "numeracion");
                String piso = getStringSafe(rs, "piso");
                String codigo_postal = getStringSafe(rs, "codigo_postal");
                String ciudad = getStringSafe(rs, "ciudad");
                String departamento = getStringSafe(rs, "departamento");
                String provincia = getStringSafe(rs, "provincia");
                String pais = getStringSafe(rs, "pais");
                String cliActivo = getBooleanSafe(rs, "c_activo") ? "Si" : "No";
                String dirActivo = getBooleanSafe(rs, "d_activo") ? "Si" : "No";
                String Fila = String.format("""
                                            Dueño: %s %s
                                            UUID: %s
                                            Cliente Activo:   %s
                                            Calle:            %s
                                            Numeracion:       %s
                                            Piso:             %s
                                            Codigo Postal:    %s
                                            Ciudad:           %s
                                            Departamento:     %s
                                            Provincia:        %s
                                            Pais:             %s
                                            Direccion Activa: %s
                                            """, nombre, apellido, UUID, cliActivo, calle, numeracion, piso, codigo_postal, ciudad, departamento, provincia, pais, dirActivo);
                Datos.add(Fila);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (!Datos.isEmpty()) {
            for (String dta : Datos) {
                System.out.println(dta);
                System.out.println("-------------------");
            }
        } else {
            System.out.println("No hay datos para mostrar");
        }

    }

    public static void bringALLCLIENTS() {
        int respuesta;
        String Query;
        do {
            Query = "SELECT * FROM cliente WHERE activo = ";
            System.out.println("""
                               \tIndique que clientes desea
                               1) Activos
                               2) Inactivos
                               0) Volver
                               """);
            try {
                respuesta = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(INVALIDDATA);
                respuesta = -1;
            }

            switch (respuesta) {
                case 1, 2 ->
                    Query = Query + String.valueOf(respuesta == 1);
                case 0 -> {
                    return;
                }
                default ->
                    System.out.println(INVALIDOPTION);
            }

            if (respuesta > 0 && respuesta < 3) {
                List<String> Datos = new ArrayList<>();

                try {
                    ResultSet rs = fetchData(Query);
                    while (rs.next()) {
                        String nombre = getStringSafe(rs, "nombre");
                        String apellido = getStringSafe(rs, "apellido");
                        String UUID = getStringSafe(rs, "idUnicoUsuario");
                        String DNI = getStringSafe(rs, "dni");
                        String Fila = String.format("""
                                            Cliente: %s %s
                                            UUID: %s
                                            DNI:  %s
                                            """, nombre, apellido, UUID, DNI);
                        Datos.add(Fila);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                if (!Datos.isEmpty()) {
                    for (String dta : Datos) {
                        System.out.println(dta);
                        System.out.println("-------------------");
                    }
                } else {
                    System.out.println("No hay datos para mostrar");
                }
            }
        } while (true);
    }

}
