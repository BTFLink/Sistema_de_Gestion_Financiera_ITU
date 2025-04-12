package prestamo;

public class PrestamoPersonal extends Prestamo {

    public PrestamoPersonal(double monto, double tasaInteresAnual, int cuotas, TipoCuota tipoCuota) {
        super(monto, "Personal", tasaInteresAnual, cuotas, tipoCuota);
    }

    @Override
    protected void calcularMontoCuotas() {
        double interesTotal = getMonto() * (getTasaInteresAnual() / 100.0) * ((double) getCuotas() / 12.0);
        this.totalADevolver = getMonto() + interesTotal;

        double cuotaBase = totalADevolver / getCuotas();
        this.montoCuotas = cuotaBase;

        System.out.println("Detalle de cuotas (Préstamo Personal - " + getTipoCuota() + "):");
        for (int i = 1; i <= getCuotas(); i++) {
            double cuota;
            if (getTipoCuota() == TipoCuota.VARIABLE) {
                double variacion = cuotaBase * 0.1;
                cuota = cuotaBase + Math.sin(i) * variacion;
            } else {
                cuota = cuotaBase;
            }
            System.out.printf("Cuota %2d: $%.2f%n", i, cuota);
        }
    }
}
