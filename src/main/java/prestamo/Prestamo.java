/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prestamo;

/**
 *
 * @author Sabrina Resca
 */
//Permitir la creación de un préstamo para un cliente específico ingresando el monto solicitado,
// tipo de préstamo (por ejemplo, personal o hipotecario), tasa de interés y número de cuotas.
//Generar automáticamente un número de préstamo único para el registro.
//Registrar múltiples préstamos por cliente, visualizándolos en la consulta detallada de cada cliente.


public class Prestamo {

    private double monto;
    private String tipoPrestamo;
    private double tasaInteresAnual;
    private int cuotas;
    private double montoCuotas;
    private double totalADevolver;
    private String numeroPrestamo;

    public Prestamo(double monto, String tipoPrestamo, double tasaInteres, int cuotas) {
        this.monto = monto;
        this.tipoPrestamo = tipoPrestamo;
        this.tasaInteresAnual = tasaInteresAnual / 100.0;
        this.cuotas = cuotas;
        this.numeroPrestamo = generarNumeroPrestamoUnico();
        calcularMontoCuotas();
    }
    private String generarNumeroPrestamoUnico(){
        return "PRE-"+ System.currentTimeMillis();
    }
    private void calcularMontoCuotas() {
        if (tipoPrestamo.equalsIgnoreCase("personal")) {
            calcularCuotasFijas();
        } else if (tipoPrestamo.equalsIgnoreCase("hipotecario")) {
            calcularCuotasVariables();
        } else {
            calcularCuotasFijas(); // Por defecto
        }
    }

    // Cuotas Fijas (Cálculo Básico - Interés Simple)
    private void calcularCuotasFijas() {
        double interesTotal = monto * tasaInteresAnual * (double) cuotas / 12.0; // Interés simple prorrateado
        this.totalADevolver = monto + interesTotal;
        if (cuotas > 0) {
            this.montoCuotas = this.totalADevolver / cuotas;
        } else {
            this.montoCuotas = this.totalADevolver;
        }
    }

    // Cuotas Variables (Cálculo Básico - Simplemente el capital dividido en cuotas)
    private void calcularCuotasVariables() {
        this.totalADevolver = monto + (monto * tasaInteresAnual * (double) cuotas / 12.0); // Incluimos el interés total para mostrar el costo
        if (cuotas > 0) {
            this.montoCuotas = monto / cuotas; // La cuota variable básica solo cubre el capital
            // En un sistema real, la parte del interés variaría según las condiciones.
            // Aquí, para simplicidad, la cuota base es solo el capital.
        } else {
            this.montoCuotas = monto;
        }
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getTipoPrestamo() {
        return tipoPrestamo;
    }

    public void setTipoPrestamo(String tipoPrestamo) {
        this.tipoPrestamo = tipoPrestamo;
    }

    public double getTasaInteresAnual() {
        return tasaInteresAnual * 100;
    }

    public void setTasaInteres(double tasaInteres) {
        this.tasaInteresAnual = tasaInteres;
    }

    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public double getMontoCuotas() {
        return montoCuotas;
    }

    public void setMontoCuotas(double montoCuotas) {
        this.montoCuotas = montoCuotas;
    }

    public double getTotalADevolver() {
        return totalADevolver;
    }

    public void setTotalADevolver(double totalADevolver) {
        this.totalADevolver = totalADevolver;
    }

    public String getNumeroPrestamo() {
        return numeroPrestamo;
    }

    public void setNumeroPrestamo(String numeroPrestamo) {
        this.numeroPrestamo = numeroPrestamo;
    }
    public void mostrarInfoPrestamo() {
        System.out.println("Número de Préstamo: " + numeroPrestamo);
        System.out.println("Tipo de Préstamo: " + tipoPrestamo);
        System.out.println("Monto Solicitado: $" + String.format("%.2f", monto));
        System.out.println("Tasa de Interés Anual: " + String.format("%.2f", getTasaInteresAnual()) + "%");
        System.out.println("Cantidad de Cuotas: " + cuotas);
        System.out.println("Monto de la Cuota: $" + String.format("%.2f", montoCuotas));
        System.out.println("Total a Devolver: $" + String.format("%.2f", totalADevolver));
    }
}
