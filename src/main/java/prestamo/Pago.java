package prestamo;
import java.util.Date;

public class Pago {
    private int numeroCuota;
    private double montoPagado;
    private Date fechaPago;

    public Pago(int numeroCuota, double montoPagado, Date fechaPago) {
        this.numeroCuota = numeroCuota;
        this.montoPagado = montoPagado;
        this.fechaPago = fechaPago;
    }

    public int getNumeroCuota() { return numeroCuota; }
    public double getMontoPagado() { return montoPagado; }
    public Date getFechaPago() { return fechaPago; }
}
