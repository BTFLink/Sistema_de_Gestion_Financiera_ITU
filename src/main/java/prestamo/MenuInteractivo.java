package prestamo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class MenuInteractivo {
    private ArrayList<Prestamo> prestamos;
    private Scanner scanner;
    private int contadorPrestamos;
    private String clienteActual;
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
        do {
            System.out.println("\n=== SISTEMA DE PRÉSTAMOS ===");
            System.out.println("1. Seleccionar cliente");
            System.out.println("2. Crear nuevo préstamo");
            System.out.println("3. Registrar pago");
            System.out.println("4. Mostrar plan de cuotas");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    seleccionarCliente();
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

    private void seleccionarCliente() {
        System.out.print("\nIngrese nombre del cliente: ");
        clienteActual = scanner.nextLine();
        System.out.println("Cliente seleccionado: " + clienteActual);
        mostrarMenuCliente();
    }

    private void crearPrestamo() {
        if (clienteActual.isEmpty()) {
            System.out.println("Primero seleccione un cliente.");
            return;
        }

        System.out.println("\n=== CREAR NUEVO PRÉSTAMO ===");
        System.out.println("Cliente: " + clienteActual);

        double monto = leerDouble("Monto del préstamo: ");
        double tasa = leerDouble("Tasa de interés inicial anual (%): ");

        System.out.print("Tipo de préstamo (1=Personal, 2=Hipotecario): ");
        int tipo = leerOpcion();

        System.out.print("Tipo de cuota (1=Fija, 2=Variable): ");
        int tipoCuota = leerOpcion();

        int cuotas = leerNumeroCuotas(tipo);

        String id = "PR-" + contadorPrestamos++;
        Prestamo prestamo = crearTipoPrestamo(id, monto, tasa, cuotas, tipoCuota, tipo);

        prestamos.add(prestamo);
        System.out.println("\nPréstamo creado exitosamente!");
        System.out.println("ID del préstamo: " + id);
    }

    private void registrarPago() {
        if (clienteActual.isEmpty()) {
            System.out.println("Primero seleccione un cliente.");
            return;
        }

        System.out.println("\n=== REGISTRAR PAGO ===");
        System.out.println("Cliente: " + clienteActual);

        Prestamo prestamo = seleccionarPrestamo();
        if (prestamo == null) return;

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
            System.out.println("Primero seleccione un cliente.");
            return;
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
        do {
            System.out.println("\n=== MENÚ CLIENTE: " + clienteActual + " ===");
            System.out.println("1. Crear nuevo préstamo");
            System.out.println("2. Registrar pago");
            System.out.println("3. Mostrar plan de cuotas");
            System.out.println("4. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            opcion = leerOpcion();

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


    private Prestamo crearTipoPrestamo(String id, double monto, double tasa, int cuotas, int tipoCuota, int tipo) {
        if (tipo == 1) {
            return new PrestamoPersonal(id, monto, tasa, cuotas,
                    tipoCuota == 1 ? "FIJA" : "VARIABLE");
        }
        return new PrestamoHipotecario(id, monto, tasa, cuotas,
                tipoCuota == 1 ? "FIJA" : "VARIABLE");
    }

    private int leerNumeroCuotas(int tipo) {
        int cuotas;
        do {
            cuotas = leerEnteroPositivo("Número de cuotas: ");
            if (tipo == 2 && (cuotas < 36 || cuotas > 72)) {
                System.out.println("Préstamo hipotecario debe tener entre 36 y 72 cuotas.");
            }
        } while (tipo == 2 && (cuotas < 36 || cuotas > 72));
        return cuotas;
    }

    private Prestamo seleccionarPrestamo() {
        ArrayList<Prestamo> prestamosCliente = new ArrayList<>();

        System.out.println("\nPréstamos del cliente " + clienteActual + ":");
        for (Prestamo p : prestamos) {
            // Asumimos que el préstamo pertenece al cliente actual
            prestamosCliente.add(p);
            System.out.println(prestamosCliente.size() + ". ID: " + p.getIdPrestamo() +
                    " - " + p.getTipoPrestamo() +
                    " - $" + p.getMonto());
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
