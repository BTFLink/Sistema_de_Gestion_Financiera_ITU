package prestamo;

public class PrestamoHipotecario extends Prestamo {
    public PrestamoHipotecario(String idPrestamo, double monto, double tasaInteres, int numeroCuotas, String tipoCuota) {
        super(idPrestamo, monto, tasaInteres, numeroCuotas, tipoCuota);
        this.tipoPrestamo = "HIPOTECARIO";
    }

    @Override
    public double calcularCuota(int numeroCuota) {
        // Obtener la tasa mensual ya calculada
        double tasaMensual = getTasaMensual(numeroCuota) / 100; // Convertir a decimal

        // Fórmula de amortización francesa (cuota constante)
        return monto * (tasaMensual * Math.pow(1 + tasaMensual, numeroCuotas))
                / (Math.pow(1 + tasaMensual, numeroCuotas) - 1);
    }
}