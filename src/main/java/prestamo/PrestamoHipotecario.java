package prestamo;

public class PrestamoHipotecario extends Prestamo {

    public PrestamoHipotecario(double monto, double tasaInteresAnual, int cuotas, TipoCuota tipoCuota) {
        super(monto, "Hipotecario", tasaInteresAnual, cuotas, tipoCuota);
    }

    @Override
    protected void calcularMontoCuotas() {
        double interesMensual = (getTasaInteresAnual() / 100.0) / 12.0;
        double principal = getMonto();
        int n = getCuotas();

        double numerador = principal * interesMensual * Math.pow(1 + interesMensual, n);
        double denominador = Math.pow(1 + interesMensual, n) - 1;

        if (denominador > 0) {
            this.montoCuotas = numerador / denominador;
            this.totalADevolver = this.montoCuotas * n;
        } else {
            this.montoCuotas = principal / n;
            this.totalADevolver = principal;
        }

        System.out.println("Detalle de cuotas (Préstamo Hipotecario - " + getTipoCuota() + "):");
        for (int i = 1; i <= getCuotas(); i++) {
            double cuota;
            if (getTipoCuota() == TipoCuota.VARIABLE) {
                double variacion = montoCuotas * 0.05;
                cuota = montoCuotas + Math.cos(i) * variacion;
            } else {
                cuota = montoCuotas;
            }
            System.out.printf("Cuota %3d: $%.2f%n", i, cuota);
        }
    }
}
