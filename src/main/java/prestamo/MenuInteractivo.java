package prestamo;

import Connections.PrestamosDAO;
import static Connections.PrestamosDAO.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import static Utils.LeerDataType.*;
import java.time.LocalDateTime;
import java.util.List;

public class MenuInteractivo {

    private ArrayList<Prestamo> prestamos;
    private Scanner scanner;
    private int contadorPrestamos;
    private String clienteActual, INVALIDOPTION = "Opcion Invalida";
    private SimpleDateFormat dateFormat;

    public MenuInteractivo() {
        prestamos = new ArrayList<>();
        scanner = new Scanner(System.in);
        contadorPrestamos = 1;
        clienteActual = "";
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    public void mostrarMenuPrincipal() {
        int opcion;
        String menu = """
                    \n=== SISTEMA DE PRÉSTAMOS ===
                    1. Seleccionar cliente
                    2. Crear nuevo préstamo
                    3. Registrar pago
                    4. Mostrar plan de cuotas
                    5. Salir
                    Seleccione una opción: """;
        do {

            opcion = LeerInt(menu, 1, 5);

            switch (opcion) {
                case 1:
                    seleccionarCliente(false);
                    break;
                case 2:
                    crearPrestamo();
                    break;
                case 3:
                    registrarPago();
                    break;
                case 4:
                    mostrarPlanCuotas();
                    break;
                case 5:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 5);
    }

    private void seleccionarCliente(boolean desvio) {
        System.out.print("\nIngrese UUID del cliente: ");
        clienteActual = scanner.nextLine();//Buscar Cliente en BD //Si no lo encuentra retornar
        System.out.println("Cliente seleccionado: " + clienteActual);
        if (desvio) {
            return;
        }
        mostrarMenuCliente();
    }

    private void crearPrestamo() {
        if (clienteActual.isEmpty()) {
            seleccionarCliente(true);
            if (clienteActual.isEmpty()) {
                return;
            }
        }

        System.out.println("\n=== CREAR NUEVO PRÉSTAMO ===");
        System.out.println("Cliente: " + clienteActual);

        NewPrestamo PRESTAPAKA = new NewPrestamo();
        int tipoPrestamo;
        do {
            tipoPrestamo = LeerInt("Tipo de préstamo (1=Personal, 2=Hipotecario): ", 1, 2);
            if (tipoPrestamo == 1 || tipoPrestamo == 2) {
                break;
            }
            System.out.println(INVALIDOPTION);
        } while (true);
        PRESTAPAKA.setTipoPrestamo(tipoPrestamo == 1 ? "PERSONAL" : "HIPOTECARIO");

        double monto;
        do {
            if (tipoPrestamo == 1) {
                monto = LeerDouble("Monto del préstamo: ", 10000, 20000000);
            } else {
                monto = LeerDouble("Monto del préstamo: ", 5000000, 70000000);
            }
            if (monto != Double.NaN) {
                break;
            }
            System.out.println("Monto Invalido");
        } while (true);
        PRESTAPAKA.setMonto(monto);

        double tasa;
        do {
            tasa = LeerDouble("Tasa de interés inicial anual (%): ", 0, 100);
            if (monto != Double.NaN) {
                break;
            }
            System.out.println("Interes Invalido");
        } while (true);
        PRESTAPAKA.setInteresInicial(tasa);

        int tipoCuota;
        do {
            tipoCuota = LeerInt("Tipo de cuota (1=Fija, 2=Variable): ", 1, 2);
            if (tipoCuota == 1 || tipoCuota == 2) {
                break;
            }
            System.out.println(INVALIDOPTION);
        } while (true);
        PRESTAPAKA.setTipoCuota(tipoCuota == 1);

        //P-1-72 H-12-360
        int cuotas = leerNumeroCuotas(tipoPrestamo);
        PRESTAPAKA.setNumeroCuotas(cuotas);

        //Modificar y añadir a base de datos
        String id = "PR-" + contadorPrestamos++;
        NewPrestamo prestamo = new NewPrestamo(clienteActual, "", monto, tasa, cuotas, String.valueOf(tipoPrestamo), true, LocalDateTime.MIN, true);

        //prestamos.add(prestamo);//Enviar Prestamo a BD
        for (int i = 0; i < 3; i++) {
            //if(CreatePrestamoDB(prestamo))
            if (PrestamosDAO.CreatePrestamoDB(prestamo)) {
                System.out.println("Prestamo registrado con exito");
                return;
            }
        }

        System.out.println("No se pudo registrar el prestamo, intentelo mas tarde");

        //System.out.println("\nPréstamo creado exitosamente!");
        //System.out.println("ID del préstamo: " + id);
    }

    private void registrarPago2() {
        if (clienteActual.isEmpty()) {
            seleccionarCliente(true);
            if (clienteActual.isEmpty()) {
                return;
            }
        }

        System.out.println("\n=== REGISTRAR PAGO ===");
        System.out.println("Cliente: " + clienteActual);

        List<NewPrestamo> listPrestamo = ListaDePrestamos(clienteActual, false);

        if (listPrestamo.isEmpty()) {
            System.out.println("No se encontraron datos de prestamos de esta persona");
            return;
        }

        System.out.println("Ingrese el numero de ID de uno de los prestamos para seleccionarlo");
        NewPrestamo.ShowPrestamos(listPrestamo);
        int ID;
        do {
            ID = LeerInt("Ingrese su seleccion", 1, listPrestamo.size());
        } while (ID < 1 || ID > listPrestamo.size());

        List<Cuota> listaCuotas = ListaDeCuotas(listPrestamo.get(ID - 1).getIdPrestamo(), false);

        if (listaCuotas.isEmpty()) {
            System.out.println("No se ha podido obtener las cuotas de este prestamo");
            return;
        }

        LocalDateTime today = LocalDateTime.now();
        List<Cuota> vencidas = Cuota.verificarCuotasEnMora(listaCuotas);
        ID = 0;
        int restantes;
        if (!vencidas.isEmpty()) {
            do {
                restantes=0;
                System.out.println("Ingrese el numero de ID de una de las cuotas para seleccionarla\nIngrese -1 para salir sin realizar pagos");
                do {
                    ID = LeerInt("Ingrese su seleccion", 1, vencidas.size());
                } while (ID < -1 || ID > vencidas.size() || ID == 0);
                if (ID == -1) {
                    break;
                }
                if (!vencidas.get(ID).isPagado()) {
                    switch (LeerInt("¿Registrar pago?\n(1) Si (2) No", 1, 2)) {
                        case 1:
                            vencidas.get(ID).setPagado(RegistrarPago(vencidas.get(ID)));
                            break;
                        case 2:
                            System.out.println("Pago cancelado");
                            break;
                        default:
                            System.out.println(INVALIDOPTION);
                    }
                } else {
                    System.out.println("La cuota ya esta pagada");
                }
                for (Cuota vencida : vencidas) {
                    if(!vencida.isPagado()){
                        restantes++;
                    }
                }
                if(restantes==0){System.out.println("No quedan cuotas vencidas que pagar");break;}
            } while (true);
            for (Cuota vencida : vencidas) {
                if (!vencida.isPagado()) {
                    System.out.println("Quedan cuotas en mora termine de pagarlas para registrar otros pagos");
                    return;
                }
            }
        }

        do {
            restantes=0;
            System.out.println("Ingrese el numero de ID de una de las cuotas para seleccionarla\nIngrese -1 para salir sin realizar pagos");
            do {
                ID = LeerInt("Ingrese su seleccion", 1, listaCuotas.size());
            } while (ID < -1 || ID > listaCuotas.size() || ID == 0);
            if (ID == -1) {
                break;
            }
            if (!listaCuotas.get(ID).isPagado()) {
                switch (LeerInt("¿Registrar pago?\n(1) Si (2) No", 1, 2)) {
                    case 1:
                        listaCuotas.get(ID).setPagado(RegistrarPago(listaCuotas.get(ID)));
                        break;
                    case 2:
                        System.out.println("Pago cancelado");
                        break;
                    default:
                        System.out.println(INVALIDOPTION);
                }
            } else {
                System.out.println("La cuota ya esta pagada");
            }
            for (Cuota cuota : listaCuotas) {
                if(!cuota.isPagado()){
                    restantes++;
                }
            }
            if(restantes==0){System.out.println("No quedan cuotas que pagar");break;}
        } while (true);
        //Pago adelantado permitido

    }

    private void registrarPago() {
        if (clienteActual.isEmpty()) {
            seleccionarCliente(true);
            if (clienteActual.isEmpty()) {
                return;
            }
        }

        System.out.println("\n=== REGISTRAR PAGO ===");
        System.out.println("Cliente: " + clienteActual);

        Prestamo prestamo = seleccionarPrestamo();
        if (prestamo == null) {
            return;
        }

        int numCuota = leerEnteroPositivo("Número de cuota a pagar: ");
        double montoCuota = prestamo.calcularCuota(numCuota);

        System.out.printf("Monto a pagar: $%.2f%n", montoCuota);
        double montoPagado = leerDouble("Monto efectivamente pagado: ");

        Date fechaPago = new Date();
        prestamo.registrarPago(numCuota, montoPagado, fechaPago);

        if (prestamo.verificarMora(fechaPago, numCuota)) {
            double penalidad = prestamo.calcularPenalidad(montoCuota);
            System.out.printf("Pago registrado con penalidad por mora: $%.2f%n", penalidad);
        } else {
            System.out.println("Pago registrado exitosamente.");
        }
        System.out.println("Fecha de pago: " + dateFormat.format(fechaPago));
        System.out.print("\n¿Desea ver el plan de cuotas actualizado? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            mostrarPlanCuotas(prestamo);
        }
    }

    private void mostrarPlanCuotas() {
        if (clienteActual.isEmpty()) {
            seleccionarCliente(true);
            if (clienteActual.isEmpty()) {
                return;
            }
        }

        Prestamo prestamo = seleccionarPrestamo();
        if (prestamo != null) {
            mostrarPlanCuotas(prestamo);
        }
    }

    private void mostrarPlanCuotas(Prestamo prestamo) {
        System.out.println("\n=== PLAN DE CUOTAS ===");
        System.out.println("Cliente: " + clienteActual);

        // Mostrar resumen del préstamo
        System.out.println("\n=== RESUMEN DEL PRÉSTAMO ===");
        System.out.printf("Monto total: $%.2f%n", prestamo.getMonto());
        System.out.printf("Tasa de interés anual inicial: %.2f%%%n", prestamo.getTasaInteres());

        // Calcular intereses aproximados
        double totalIntereses = prestamo.calcularTotalIntereses();
        System.out.printf("Intereses totales aproximados: $%.2f%n", totalIntereses);

        System.out.println("Número de cuotas: " + prestamo.getNumeroCuotas());
        System.out.println("Tipo de cuota: " + prestamo.getTipoCuota());

        // Calcular monto total a devolver
        double totalAPagar = prestamo.getMonto() + totalIntereses;
        System.out.printf("\nTOTAL A DEVOLVER APROXIMADO: $%.2f (Capital: $%.2f + Intereses: $%.2f)%n",
                totalAPagar, prestamo.getMonto(), totalIntereses);

        // Mostrar cuadro de cuotas detallado
        System.out.println("\n=== DETALLE DE CUOTAS ===");
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("| Cuota | Vencimiento  | Monto Cuota | Tasa Mes | Estado    | Penalidad | Fecha Pago    |");
        System.out.println("----------------------------------------------------------------------------------------");

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(prestamo.getFechaCreacion());

        for (int i = 1; i <= prestamo.getNumeroCuotas(); i++) {
            calendario.add(Calendar.MONTH, 1);
            calendario.set(Calendar.DAY_OF_MONTH, 10);
            Date vencimiento = calendario.getTime();

            double montoCuota = prestamo.calcularCuota(i);
            String estado = "Pendiente";
            String penalidad = "-";
            String fechaPagoStr = "-";

            for (Pago pago : prestamo.getPagos()) {
                if (pago.getNumeroCuota() == i) {
                    estado = prestamo.verificarMora(pago.getFechaPago(), i) ? "Mora" : "Pagado";
                    if (prestamo.verificarMora(pago.getFechaPago(), i)) {
                        penalidad = String.format("$%.2f", prestamo.calcularPenalidad(montoCuota));
                    }
                    fechaPagoStr = dateFormat.format(pago.getFechaPago());
                    break;
                }
            }

            System.out.printf("| %-5d | %-12s | $%-10.2f | %-8.2f%% | %-9s | %-9s | %-14s |%n", i,
                    String.format("%02d/%02d/%04d",
                            calendario.get(Calendar.DAY_OF_MONTH),
                            calendario.get(Calendar.MONTH) + 1,
                            calendario.get(Calendar.YEAR)),
                    montoCuota,
                    prestamo.getTasaMensual(i),
                    estado,
                    penalidad,
                    fechaPagoStr);
        }
        System.out.println("----------------------------------------------------------------------------------------");

        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    private void mostrarMenuCliente() {
        int opcion;
        String menu = """
                    \n=== MENÚ CLIENTE: " + clienteActual + " ===
                    1. Crear nuevo préstamo
                    2. Registrar pago
                    3. Mostrar plan de cuotas
                    4. Volver al menú principal
                    Seleccione una opción: """;
        do {
            opcion = LeerInt(menu, 1, 4);

            switch (opcion) {
                case 1:
                    crearPrestamo();
                    break;
                case 2:
                    registrarPago();
                    break;
                case 3:
                    mostrarPlanCuotas();
                    break;
                case 4:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 4);
    }

    private int leerOpcion() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor ingrese un número.");
            scanner.next();
        }
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        return opcion;
    }

    private int leerNumeroCuotas(int tipo) {
        int cuotas;
        do {
            cuotas = LeerInt("Número de cuotas: ");
            if (tipo == 2 && (cuotas < 12 || cuotas > 360)) {
                System.out.println("Préstamo hipotecario debe tener entre 12 a 360 cuotas.");
            } else if (tipo == 1 && (cuotas < 1 || cuotas > 72)) {
                System.out.println("Préstamo personal debe tener entre 1 a 72 cuotas.");
            } else {
                return cuotas;
            }
        } while (true);
    }

    private Prestamo seleccionarPrestamo() {
        ArrayList<Prestamo> prestamosCliente = new ArrayList<>();

        System.out.println("\nPréstamos del cliente " + clienteActual + ":");
        for (Prestamo p : prestamos) {
            // Asumimos que el préstamo pertenece al cliente actual
            prestamosCliente.add(p);
            System.out.println(prestamosCliente.size() + ". ID: " + p.getIdPrestamo()
                    + " - " + p.getTipoPrestamo()
                    + " - $" + p.getMonto());
        }

        if (prestamosCliente.isEmpty()) {
            System.out.println("El cliente no tiene préstamos registrados.");
            return null;
        }

        System.out.print("Seleccione préstamo (número): ");
        int seleccion = leerOpcion();

        if (seleccion < 1 || seleccion > prestamosCliente.size()) {
            System.out.println("Selección inválida.");
            return null;
        }

        return prestamosCliente.get(seleccion - 1);
    }

    private double leerDouble(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextDouble()) {
            System.out.println("Entrada inválida. Por favor ingrese un número.");
            scanner.next();
        }
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Limpiar buffer
        return valor;
    }

    private int leerEnteroPositivo(String mensaje) {
        int valor;
        do {
            System.out.print(mensaje);
            while (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida. Por favor ingrese un número entero.");
                scanner.next();
            }
            valor = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            if (valor <= 0) {
                System.out.println("Por favor ingrese un número positivo.");
            }
        } while (valor <= 0);
        return valor;
    }

    public static void main(String[] args) {
        MenuInteractivo menu = new MenuInteractivo();
        menu.mostrarMenuPrincipal();
    }
}
