package prestamo;

import Connections.PrestamosDAO;
import static Connections.PrestamosDAO.*;
import Entity.Cliente;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import static Utils.LeerDataType.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MenuInteractivo {

    private ArrayList<Prestamo> prestamos;
    private Scanner scanner;
    private int contadorPrestamos;
    private String clienteActual;
    private final String INVALIDOPTION = "Opcion Invalida";
    
    public MenuInteractivo() {
        prestamos = new ArrayList<>();
        scanner = new Scanner(System.in);
        contadorPrestamos = 1;
        clienteActual = "";
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
        if (Cliente.existeCliente(clienteActual)) {
            System.out.println("Cliente seleccionado: " + clienteActual);
        } else {
            System.out.println("No se ha encontrado el cliente");
            clienteActual = "";
            return;
        }

        if (desvio) {
            return;
        }
        mostrarMenuCliente();
    }

    private void crearPrestamo() {
        if (clienteActual.isEmpty() || clienteActual.isBlank()) {
            seleccionarCliente(true);
            if (clienteActual.isEmpty() || clienteActual.isBlank()) {
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

    private void registrarPago() {
        if (clienteActual == null || clienteActual.isBlank()) {
            seleccionarCliente(true);
            if (clienteActual == null || clienteActual.isBlank()) {
                return;
            }
        }

        System.out.println("\n=== REGISTRAR PAGO ===");
        System.out.println("Cliente: " + clienteActual);

        NewPrestamo prestamo = seleccionarPrestamo();
        if (prestamo == null) {
            return;
        }

        List<Cuota> todasCuotas = ListaDeCuotas(prestamo.getIdPrestamo(), false);
        if (todasCuotas.isEmpty()) {
            System.out.println("No se ha podido obtener las cuotas de este préstamo");
            return;
        }

        List<Cuota> cuotasVencidas = Cuota.verificarCuotasEnMora(todasCuotas);

        if (!cuotasVencidas.isEmpty()) {
            if (!pagarCuotas(cuotasVencidas, "cuotas vencidas")) {
                return;
            }

            // Reflejar pagos en la lista principal
            for (Cuota vencida : cuotasVencidas) {
                for (Cuota cuota : todasCuotas) {
                    if (cuota.getIdCuota().equals(vencida.getIdCuota())) {
                        cuota.setPagado(vencida.isPagado());
                    }
                }
            }

            // Verificar que no queden cuotas vencidas sin pagar
            if (cuotasVencidas.stream().anyMatch(c -> !c.isPagado())) {
                System.out.println("Quedan cuotas en mora. Termine de pagarlas para registrar otros pagos.");
                return;
            }
        }

        pagarCuotas(todasCuotas, "cuotas restantes");
    }

    private boolean pagarCuotas(List<Cuota> cuotas, String mensaje) {
        while (true) {
            long pendientes = cuotas.stream().filter(c -> !c.isPagado()).count();
            if (pendientes == 0) {
                System.out.println("No quedan " + mensaje + " que pagar");
                return true;
            }

            System.out.println("Ingrese el número de ID de una de las " + mensaje + " para seleccionarla\nIngrese -1 para salir sin realizar pagos");
            int seleccion = LeerInt("Ingrese su selección", -1, cuotas.size());
            if (seleccion == -1) {
                return false;
            }

            if (seleccion < 1 || seleccion > cuotas.size()) {
                System.out.println("Selección inválida.");
                continue;
            }

            Cuota cuota = cuotas.get(seleccion - 1);
            if (cuota.isPagado()) {
                System.out.println("La cuota ya está pagada");
                continue;
            }

            int confirmar = LeerInt("¿Registrar pago?\n(1) Sí (2) No", 1, 2);
            if (confirmar == 1) {
                cuota.setPagado(RegistrarPago(cuota));
            } else {
                System.out.println("Pago cancelado");
            }
        }
    }

    private void mostrarPlanCuotas() {
        if (clienteActual.isEmpty()) {
            seleccionarCliente(true);
            if (clienteActual.isEmpty()) {
                return;
            }
        }

        NewPrestamo prestamo = seleccionarPrestamo();
        if (prestamo != null) {
            mostrarPlanCuotas(prestamo);
        }
    }

    private void mostrarPlanCuotas(NewPrestamo prestamo) {
        System.out.println("\n=== PLAN DE CUOTAS ===");
        System.out.println("Cliente: " + clienteActual);

        // Mostrar resumen del préstamo
        System.out.println("\n=== RESUMEN DEL PRÉSTAMO ===");
        System.out.printf("Monto total: $%.2f%n", prestamo.getMonto());
        System.out.printf("Tasa de interés anual inicial: %.2f%%%n", prestamo.getInteresInicial());

        // Calcular intereses aproximados
        double totalIntereses = prestamo.calcularTotalIntereses();
        System.out.printf("Intereses totales aproximados: $%.2f%n", totalIntereses);

        System.out.println("Número de cuotas: " + prestamo.getNumeroCuotas());
        System.out.println("Tipo de cuota: " + (prestamo.isTipoCuota() ? "Fijo" : "Variable"));

        // Calcular monto total a devolver
        double totalAPagar = prestamo.getMonto() + totalIntereses;
        System.out.printf("\nTOTAL A DEVOLVER APROXIMADO: $%.2f (Capital: $%.2f + Intereses: $%.2f)%n",
                totalAPagar, prestamo.getMonto(), totalIntereses);

        List<Cuota> listaCuotas = ListaDeCuotas(prestamo.getIdPrestamo());
        List<Pago> listaPagos = ListaDePagos(prestamo.getIdPrestamo());

        if(listaCuotas.isEmpty()){
            System.out.println("No es posible mostrar datos de Cuotas");
            return;
        }
        // Mostrar cuadro de cuotas detallado
        System.out.println("\n=== DETALLE DE CUOTAS ===");
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.printf("| %-22s | %-15s | %-12s | %-8s | %-9s | %-10s | %-15s |%n",
                "Cuota","Vencimiento","Monto Cuota","Tasa Mes","Estado","Penalidad","Fecha Pago");
        System.out.println("----------------------------------------------------------------------------------------");

        Pago pagoDeCuota;
        String estado;
        double penalidad;
        for (Cuota cuota : listaCuotas) {
            pagoDeCuota = new Pago(); //Nuevo Pago fechaPago=LocalDateTime.MIN;
            for (Pago pago : listaPagos) {
                if (pago.getCuota_idCuota().equals(cuota.getIdCuota())) {
                    pagoDeCuota = pago;
                    break;
                }
            }

            estado = pagoDeCuota.getMontoPagado() > 0 ? "Pagado" : "Sin Pagar";
            estado = estado + ((pagoDeCuota.getFechaPago().isBefore(cuota.getVencimiento()) && !pagoDeCuota.getFechaPago().isEqual(LocalDateTime.MIN)) ? "" : " con Mora");

            penalidad = estado.contains("Mora") ? (cuota.CalcularMora() - cuota.getMonto()) : 0.0;

            System.out.printf("| %-22s | %-15s | $%-12.2f | %-8.2f%% | %-9s | %-10.2f | %-15s |%n",
                    cuota.getIdCuota(),
                    cuota.getVencimiento().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    cuota.getMonto(),
                    cuota.getInteres(),
                    estado,
                    penalidad,
                    estado.contains("Pagado") ? pagoDeCuota.getFechaPago().format(DateTimeFormatter.ISO_LOCAL_DATE) : "----/--/--");
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

    private NewPrestamo seleccionarPrestamo() {
        try {
            List<NewPrestamo> Nprestamos = ListaDePrestamos(clienteActual, false);
            if (Nprestamos.isEmpty()) {
                System.out.println("No se encontraron datos de préstamos de esta persona");
                return null;
            }
            System.out.println("Ingrese el número de ID de uno de los préstamos para seleccionarlo");
            NewPrestamo.ShowPrestamos(Nprestamos);
            int seleccionPrestamo = LeerInt("Ingrese su selección", 1, Nprestamos.size()) - 1;
            return Nprestamos.get(seleccionPrestamo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        MenuInteractivo menu = new MenuInteractivo();
        menu.mostrarMenuPrincipal();
    }
}
