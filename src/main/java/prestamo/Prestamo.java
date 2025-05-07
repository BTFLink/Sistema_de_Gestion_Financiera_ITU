package prestamo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public abstract class Prestamo {
    protected String idPrestamo;
    protected double monto;
    protected double tasaInteresAnual;
    protected int numeroCuotas;
    protected String tipoPrestamo;
    protected String tipoCuota;
    protected ArrayList<Pago> pagos;
    protected Date fechaCreacion;
    protected static final double PENALIDAD_POR_MORA = 0.05; // 5%
    protected ArrayList<Double> tasasMensuales;

    public Prestamo(String idPrestamo, double monto, double tasaInteresAnual, int numeroCuotas, String tipoCuota) {
        this.idPrestamo = idPrestamo;
        this.monto = monto;
        this.tasaInteresAnual = tasaInteresAnual;
        this.numeroCuotas = numeroCuotas;
        this.tipoCuota = tipoCuota;
        this.pagos = new ArrayList<>();
        this.fechaCreacion = new Date();
        this.tasasMensuales = new ArrayList<>();
        calcularTasasVariables();
    }

    public double calcularTotalIntereses() {
        double totalIntereses = 0;
        for (int i = 1; i <= numeroCuotas; i++) {
            totalIntereses += calcularCuota(i) - (monto / numeroCuotas);
        }
        return totalIntereses;
    }

    private void calcularTasasVariables() {
        double tasaBase = tasaInteresAnual / 12; // Tasa mensual inicial

        for (int i = 0; i < numeroCuotas; i++) {
            if (tipoCuota.equals("VARIABLE") && i > 0 && i % 3 == 0) {
                // Ajuste cada 3 cuotas (ejemplo: variación entre -0.5% y +1.5%)
                double variacion = (Math.random() * 2.0) - 0.5;
                tasaBase += variacion;
            }
            tasasMensuales.add(tasaBase); // Almacenar tasa para esta cuota
        }
    }

    public double calcularCuota(int numeroCuota) {
        if (numeroCuota < 1 || numeroCuota > numeroCuotas) {
            return 0;
        }
        double tasaMensual = getTasaMensual(numeroCuotas) / 100;
        return monto * (tasaMensual * Math.pow(1 + tasaMensual, numeroCuotas))
                / (Math.pow(1 + tasaMensual, numeroCuotas) - 1);
    }

    public void registrarPago(int numeroCuota, double montoPagado, Date fechaPago) {
        Pago pago = new Pago(numeroCuota, montoPagado, fechaPago);
        pagos.add(pago);
    }

    public boolean verificarMora(Date fechaPago, int numeroCuota) {
        Calendar calPago = Calendar.getInstance();
        calPago.setTime(fechaPago);

        Calendar calVencimiento = Calendar.getInstance();
        calVencimiento.setTime(fechaCreacion);
        calVencimiento.add(Calendar.MONTH, numeroCuota);
        calVencimiento.set(Calendar.DAY_OF_MONTH, 10);

        return calPago.after(calVencimiento);
    }

    public double calcularPenalidad(double montoCuota) {
        return montoCuota * PENALIDAD_POR_MORA;
    }

    public double getTasaMensual(int numeroCuota){
        if (numeroCuotas < 1 || numeroCuota > numeroCuotas) {
            return 0;
        }
        return tasasMensuales.get(numeroCuotas - 1); // Las cuotas empiezan en 1
    }

    // Getters
    public String getIdPrestamo() { return idPrestamo; }
    public double getMonto() { return monto; }
    public double getTasaInteres() { return tasaInteresAnual; }
    public int getNumeroCuotas() { return numeroCuotas; }
    public String getTipoPrestamo() { return tipoPrestamo; }
    public String getTipoCuota() { return tipoCuota; }
    public ArrayList<Pago> getPagos() { return pagos; }
    public Date getFechaCreacion() { return fechaCreacion; }
}