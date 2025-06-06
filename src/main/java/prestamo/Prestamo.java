/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prestamo;

import Connections.PrestamosDAO;
import static Utils.LeerDataType.LeerDouble;
import static Utils.LeerDataType.LeerInt;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author BTF
 */
public class Prestamo {

    private String idCliente;
    private String idPrestamo;
    private double monto;
    private double interesInicial;
    private int numeroCuotas;
    private String tipoPrestamo;
    private boolean tipoCuota;
    private LocalDateTime fechaCreacion;
    private boolean estaPagado;

    public Prestamo() {
        this.idCliente = "";
        this.idPrestamo = "";
        this.monto = 0;
        this.interesInicial = 0;
        this.numeroCuotas = 0;
        this.tipoPrestamo = "";
        this.tipoCuota = false;
        this.estaPagado = false;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Prestamo(String idCliente, String idPrestamo, double monto, double interesInicial, int numeroCuotas, String tipoPrestamo, boolean tipoCuota, LocalDateTime fechaCreacion, boolean estaPagado) {
        this.idCliente = idCliente;
        this.idPrestamo = idPrestamo;
        this.monto = monto;
        this.interesInicial = interesInicial;
        this.numeroCuotas = numeroCuotas;
        this.tipoPrestamo = tipoPrestamo;
        this.tipoCuota = tipoCuota;
        this.fechaCreacion = fechaCreacion;
        this.estaPagado = estaPagado;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(String idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getInteresInicial() {
        return interesInicial;
    }

    public void setInteresInicial(double interesInicial) {
        this.interesInicial = interesInicial;
    }

    public int getNumeroCuotas() {
        return numeroCuotas;
    }

    public void setNumeroCuotas(int numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }

    public String getTipoPrestamo() {
        return tipoPrestamo;
    }

    public void setTipoPrestamo(String tipoPrestamo) {
        this.tipoPrestamo = tipoPrestamo;
    }

    public boolean isTipoCuota() {
        return tipoCuota;
    }

    public void setTipoCuota(boolean tipoCuota) {
        this.tipoCuota = tipoCuota;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isEstaPagado() {
        return estaPagado;
    }

    public void setEstaPagado(boolean estaPagado) {
        this.estaPagado = estaPagado;
    }

    public String printPrestamo(boolean encabezado) {
        StringBuilder prestamo = new StringBuilder();
        if (encabezado) {
            prestamo.append(String.format(
                    "%-22s | %-22s | %10s | %14s | %13s | %-15s | %-10s | %-20s | %-10s |%n",
                    "ID Cliente",
                    "ID Préstamo",
                    "Monto",
                    "Interés",
                    "Cuotas",
                    "Tipo Préstamo",
                    "Cuota",
                    "Fecha Creación",
                    "Estado"
            ));
        }
        prestamo.append(String.format(
                "%-22s | %-22s | %10.2f | %14.2f | %13d | %-15s | %-10s | %-20s | %-10s |",
                this.getIdCliente(),
                this.getIdPrestamo(),
                this.getMonto(),
                this.getInteresInicial(),
                this.getNumeroCuotas(),
                this.getTipoPrestamo(),
                this.isTipoCuota() ? "Fijo" : "Variable",
                this.getFechaCreacion() != null ? this.getFechaCreacion().toString() : "null",
                this.isEstaPagado() ? "Pagado" : "Pendiente"
        ));
        return prestamo.toString();
    }

    public static void ShowPrestamos(List<Prestamo> Prestamos) {
        String encabezado = String.format(
                "%-3s | %-22s | %-22s | %10s | %14s | %13s | %-15s | %-10s | %-20s | %-10s |",
                "ID",
                "ID Cliente",
                "ID Préstamo",
                "Monto",
                "Interés",
                "Cuotas",
                "Tipo Préstamo",
                "Cuota",
                "Fecha Creación",
                "Estado"
        );

        System.out.println(encabezado);
        int id = 1;
        for (Prestamo Prestamo : Prestamos) {
            System.out.println(String.format(
                    "%-3s | %-22s | %-22s | %10.2f | %14.2f | %13d | %-15s | %-10s | %-20s | %-10s |",
                    id,
                    Prestamo.getIdCliente(),
                    Prestamo.getIdPrestamo(),
                    Prestamo.getMonto(),
                    Prestamo.getInteresInicial(),
                    Prestamo.getNumeroCuotas(),
                    Prestamo.getTipoPrestamo(),
                    Prestamo.isTipoCuota() ? "Fijo" : "Variable",
                    Prestamo.getFechaCreacion() != null ? Prestamo.getFechaCreacion().toString() : "null",
                    Prestamo.isEstaPagado() ? "Pagado" : "Pendiente"
            ));
            id++;
        }
    }

    private void CrearPrestamo(String cliente) {
        String INVALIDOPTION = "Opcion Invalida";
        if (cliente.isEmpty()) {
            System.out.println("Cliente Ingresado Invalido");
            return;
        }
        this.idCliente = cliente;
        System.out.println("\n=== CREAR NUEVO PRÉSTAMO ===");
        System.out.println("Cliente: " + cliente);

        int prestamoTipo;
        do {
            prestamoTipo = LeerInt("Tipo de préstamo (1=Personal, 2=Hipotecario): ", 1, 2);
            if (prestamoTipo == 1 || prestamoTipo == 2) {
                break;
            }
            System.out.println(INVALIDOPTION);
        } while (true);
        this.tipoPrestamo = prestamoTipo == 1 ? "PERSONAL" : "HIPOTECARIO";

        do {
            if (prestamoTipo == 1) {
                this.monto = LeerDouble("Monto del préstamo: ", 10000, 20000000);
            } else {
                this.monto = LeerDouble("Monto del préstamo: ", 5000000, 70000000);
            }
            if (!Double.isNaN(this.monto)) {
                break;
            }
            System.out.println("Monto Invalido");
        } while (true);

        do {
            this.interesInicial = LeerDouble("Tasa de interés inicial anual (%): ", 0, 100);
            if (!Double.isNaN(this.interesInicial)) {
                break;
            }
            System.out.println("Interes Invalido");
        } while (true);

        int cuotaFija;
        do {
            cuotaFija = LeerInt("Tipo de cuota (1=Fija, 2=Variable): ", 1, 2);
            if (cuotaFija == 1 || cuotaFija == 2) {
                break;
            }
            System.out.println(INVALIDOPTION);
        } while (true);
        this.setTipoCuota(cuotaFija == 1);

        //P-1-72 H-12-360
        this.numeroCuotas = leerNumeroCuotas(prestamoTipo);

        this.fechaCreacion = LocalDateTime.now();
        this.idPrestamo = "";
        this.estaPagado = false;
        //prestamos.add(prestamo);//Enviar Prestamo a BD
        for (int i = 0; i < 3; i++) {
            //if(CreatePrestamoDB(prestamo))
            if (PrestamosDAO.CreatePrestamoDB(this)) {
                System.out.println("Prestamo registrado con exito");
                return;
            }
        }

        System.out.println("No se pudo registrar el prestamo, intentelo mas tarde");

        //System.out.println("\nPréstamo creado exitosamente!");
        //System.out.println("ID del préstamo: " + id);
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

    public double calcularTotalIntereses() {
        double tasaMensual = getInteresInicial() / 12;

        if (isTipoCuota()) {
            return (calcularCuota(tasaMensual, getNumeroCuotas(), getMonto()) * getNumeroCuotas()) - getMonto();
        } else {
            double cuota;
            double cuotaTotal = 0;
            int cuotasTotales = getNumeroCuotas();
            for (int i = 1; i <= cuotasTotales; i += 3) {
                int cuotasEnEsteBloque = Math.min(3, cuotasTotales - (i - 1)); // calcula si quedan 1, 2 o 3 cuotas
                cuota = calcularCuota(tasaMensual, cuotasTotales, getMonto());

                // Muestra para depuración
                System.out.printf("Cuota para los meses %d a %d: %.2f%n", i, (i + cuotasEnEsteBloque - 1), cuota);

                cuotaTotal += cuota * cuotasEnEsteBloque;

                tasaMensual += 1; // subir la tasa mensual en 1% (porque es porcentaje)
            }
            return cuotaTotal - getMonto();
        }
    }

    private double calcularCuota(double tasaInteresMensual, int plazo, double monto) {
        return (monto / plazo) * (1 + (tasaInteresMensual / 100));
    }

    public double calcularCuotaFija() { //Solo para cuotas fijas
        return calcularCuota(1);
    }

    public double calcularCuota(int cuota_actual) {
        double tasaMensual = getInteresInicial() / 12;
        if (isTipoCuota()) {
            return calcularCuota(tasaMensual, getNumeroCuotas(), getMonto());
        }
        tasaMensual += (int) ((cuota_actual - 1) / 3);
        return calcularCuota(tasaMensual, getNumeroCuotas(), getMonto());
    }
}
