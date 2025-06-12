package prestamo;

import Connections.PrestamosDAO;
import static Connections.PrestamosDAO.*;
import Entity.Cliente;
import java.util.Scanner;
import static Utils.LeerDataType.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MenuPrestamo {

    private final Scanner scanner;
    private String clienteActual;
    private final String INVALIDOPTION = "Opcion Invalida", ClaveSim = "SIM-";
    private boolean volverAPrincipal;
    private boolean ModuloRegistro;

    public MenuPrestamo() {
        scanner = new Scanner(System.in);
        clienteActual = "";
        volverAPrincipal = true;
        ModuloRegistro = false;
    }

    public void mostrarMenuPrincipal() {
        int opcion;
        String menu = """
                    \n=== SISTEMA DE PRÉSTAMOS ===
                    1. Seleccionar cliente
                    2. Crear nuevo préstamo
                    3. Registrar pago
                    4. Mostrar plan de cuotas
                    0. Salir
                    Seleccione una opción: """;
        do {
            opcion = LeerInt(menu, 1, 5);
            //Selecciona un cliente para trabajar exclusivamente con ese o ingresar el codigo una vez por modulo
            switch (opcion) {
                case 1:
                    seleccionarCliente(false);
                    break;
                case 2:
                    ModuloRegistro = true;
                    crearPrestamo();
                    ModuloRegistro = false;
                    break;
                case 3:
                    registrarPago();
                    break;
                case 4:
                    mostrarPlanCuotas();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
            clienteActual = "";
        } while (opcion != 0);
    }

    private void seleccionarCliente(boolean desvio) {
        if (ModuloRegistro) {
            System.out.println("Utilice \"SIM-\" para iniciar el modo de simulacion");
        }
        System.out.print("\nIngrese UUID del cliente: ");
        clienteActual = scanner.nextLine();//Buscar Cliente en BD //Si no lo encuentra retornar
        if (!clienteActual.equals(ClaveSim)) {
            clienteActual = LeerUUID("CLI", clienteActual);
        }

        if (Cliente.existeCliente(clienteActual)) {
            System.out.println("Cliente seleccionado: " + clienteActual);
        } else if (clienteActual.equalsIgnoreCase(ClaveSim) && ModuloRegistro) {
            System.out.println("Iniciando el Modo Simulacion de Registro");
        } else {
            System.out.println("No se ha encontrado el cliente");
            clienteActual = "";
            return;
        }

        if (desvio) {
            return;
        }
        volverAPrincipal = false;
        mostrarMenuCliente();
    }

    private void crearPrestamo() {

        if (clienteActual.isEmpty() || clienteActual.isBlank()) {
            seleccionarCliente(true);
            if (clienteActual.isEmpty() || clienteActual.isBlank()) {
                return;
            }
        }

        System.out.println("""
                           Para cancelar el registro, ingresando el valor "0" en cualquier momento
                           Excepto en los lugares marcados con *""");
        System.out.println("\n=== CREAR NUEVO PRÉSTAMO ===");
        System.out.println("Cliente: " + clienteActual);

        Prestamo PRESTAPAKA = new Prestamo();
        PRESTAPAKA.setIdCliente(clienteActual);
        int tipoPrestamo;
        do {
            tipoPrestamo = LeerInt("Tipo de préstamo (1=Personal, 2=Hipotecario): ", 0, 2);
            if (tipoPrestamo == 1 || tipoPrestamo == 2) {
                break;
            }
            if (tipoPrestamo == 0) {
                return;
            }
            System.out.println(INVALIDOPTION);
        } while (true);
        PRESTAPAKA.setTipoPrestamo(tipoPrestamo == 1 ? "PERSONAL" : "HIPOTECARIO");

        double monto;
        do {
            if (tipoPrestamo == 1) {
                monto = LeerDouble("Monto del préstamo (MIN: 10K; MAX : 20M)*", 10000, 20000000);
            } else {
                monto = LeerDouble("Monto del préstamo:(MIN: 5M;  MAX : 70M)*", 5000000, 70000000);
            }
            if (!Double.isNaN(monto)) {
                break;
            }
            System.out.println("Monto Invalido");
        } while (true);
        PRESTAPAKA.setMonto(monto);

        double tasa;
        do {
            tasa = LeerDouble("Tasa de interés inicial anual (%): ", 0, 100);
            if (tasa == 0) {
                return;
            }
            if (!Double.isNaN(tasa)) {
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
            if (tipoCuota == 0) {
                return;
            }
            System.out.println(INVALIDOPTION);
        } while (true);
        PRESTAPAKA.setTipoCuota(tipoCuota == 1);

        //P-1-72 H-12-360
        int cuotas = leerNumeroCuotas(tipoPrestamo);
        PRESTAPAKA.setNumeroCuotas(cuotas);

        //Modificar y añadir a base de datos
        //prestamos.add(prestamo);//Enviar Prestamo a BD
        if (clienteActual.equals(ClaveSim)) {
            mostrarPlanCuotas(PRESTAPAKA);
            System.out.println("Terminando Simulación");
            return;
        }
        for (int i = 0; i < 3; i++) {
            //if(CreatePrestamoDB(prestamo))
            if (PrestamosDAO.CreatePrestamoDB(PRESTAPAKA)) {
                System.out.println("Prestamo registrado con exito");
                if (volverAPrincipal) {
                    clienteActual = "";
                }
                return;
            }
        }

        if (volverAPrincipal) {
            clienteActual = "";
        }  //en caso de no haber usado selector de cliente reinicia la variable
        System.out.println("No se pudo registrar el prestamo, intentelo mas tarde");
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

        Prestamo prestamo = seleccionarPrestamo();
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

            pagarCuotas(cuotasVencidas, "cuotas vencidas", true);

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
                if (volverAPrincipal) {
                    clienteActual = "";
                }
                return;
            }
        }

        pagarCuotas(todasCuotas, "cuotas restantes", false);
        if (volverAPrincipal) {
            clienteActual = "";
        }
    }

    private boolean pagarCuotas(List<Cuota> cuotas, String mensaje, boolean conMora) {
        while (true) {
            long pendientes = cuotas.stream().filter(c -> !c.isPagado()).count();
            if (pendientes == 0) {
                System.out.println("No quedan " + mensaje + " que pagar");
                return true;
            }

            if (conMora) {
                Cuota.showListCuotasConMora(cuotas);
            } else {
                Cuota.showListCuotas(cuotas);
            }
            System.out.println("Ingrese el número de Indice de una de las " + mensaje + " para seleccionarla\nIngrese -1 para salir sin realizar pagos");
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

        List<Cuota> listaCuotas;
        List<Pago> listaPagos = new ArrayList<>();
        if (clienteActual.equals(ClaveSim)) {
            listaCuotas = GenerarListaCuotas(prestamo);
        } else {
            listaCuotas = ListaDeCuotas(prestamo.getIdPrestamo());
            listaPagos = ListaDePagos(prestamo.getIdPrestamo());
        }
        if (listaCuotas.isEmpty()) {
            System.out.println("No es posible mostrar datos de Cuotas");
            return;
        }
        // Mostrar cuadro de cuotas detallado
        System.out.println("\n=== DETALLE DE CUOTAS ===");
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.printf("| %-22s | %-15s | %-20s | %-8s | %-20s | %-20s | %-15s |%n",
                "Cuota", "Vencimiento", "Monto Cuota", "Tasa Mes", "Estado", "Penalidad", "Fecha Pago");
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
            estado = estado + (((pagoDeCuota.getFechaPago().isBefore(cuota.getVencimiento()) && !pagoDeCuota.getFechaPago().isEqual(LocalDateTime.MIN)) || LocalDateTime.now().isBefore(cuota.getVencimiento())) ? "" : " con Mora");

            penalidad = estado.contains("Mora") ? (cuota.getMonto() - cuota.CalcularMora()) : 0.0;

            System.out.printf("| %-22s | %-15s | $%-20.2f | %-8.2f%% | %-20s | %-20.2f | %-15s |%n",
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
        if (volverAPrincipal) {
            clienteActual = "";
        }
    }

    private List<Cuota> GenerarListaCuotas(Prestamo prestamo) {
        List<Cuota> Generando = new ArrayList<>();
        LocalDateTime hoy = LocalDateTime.now();
        double TasaInteresMensual = prestamo.getInteresInicial() / 12;

        for (int i = 1; i <= prestamo.getNumeroCuotas(); i++) {
            if (prestamo.isTipoCuota()) {
                Generando.add(new Cuota(ClaveSim, ClaveSim + "0" + i, prestamo.calcularCuotaFija(), TasaInteresMensual, false, hoy.plusMonths(i).withDayOfMonth(10).withHour(0).withMinute(0).withSecond(0).withNano(0)));
            } else {
                if (((i - 1) % 3 == 0) && (i - 1 != 0)) {
                    TasaInteresMensual += 1;
                }
                Generando.add(new Cuota(ClaveSim, ClaveSim + "0" + i, prestamo.calcularCuota(i), TasaInteresMensual, false, hoy.plusMonths(i).withDayOfMonth(10).withHour(0).withMinute(0).withSecond(0).withNano(0)));
            }
        }
        return Generando;
    }

    private void mostrarMenuCliente() {
        int opcion;
        String menu = """
                    \n=== MENÚ CLIENTE: " + clienteActual + " ===
                    1. Crear nuevo préstamo
                    2. Registrar pago
                    3. Mostrar plan de cuotas
                    0. Volver al menú principal
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
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
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
        try {
            List<Prestamo> Nprestamos = ListaDePrestamos(clienteActual, false);
            if (Nprestamos.isEmpty()) {
                System.out.println("No se encontraron datos de préstamos de esta persona");
                return null;
            }
            System.out.println("Ingrese el número de ID de uno de los préstamos para seleccionarlo");
            Prestamo.ShowPrestamos(Nprestamos);
            int seleccionPrestamo = LeerInt("Ingrese su selección", 1, Nprestamos.size()) - 1;
            if (seleccionPrestamo < 0 || seleccionPrestamo >= Nprestamos.size()) {
                return null;
            }
            return Nprestamos.get(seleccionPrestamo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main() {
        MenuPrestamo menu = new MenuPrestamo();
        menu.mostrarMenuPrincipal();
    }
}
