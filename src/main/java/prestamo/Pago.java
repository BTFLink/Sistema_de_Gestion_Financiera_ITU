package prestamo;

import java.time.LocalDateTime;

public class Pago {

    private String cuota_idCuota;
    private String idPago;
    private boolean penalidad;
    private int numeroCuota;
    private double montoPagado;
    private LocalDateTime fechaPago;

    public Pago() {
        this.cuota_idCuota = "";
        this.idPago = "";
        this.penalidad = true;
        this.numeroCuota = 0;
        this.montoPagado = 0;
        this.fechaPago = LocalDateTime.MIN;
    }

    public Pago(String cuota_idCuota, String idPago, boolean penalidad, int numeroCuota, double montoPagado, LocalDateTime fechaPago) {
        this.cuota_idCuota = cuota_idCuota;
        this.idPago = idPago;
        this.penalidad = penalidad;
        this.numeroCuota = numeroCuota;
        this.montoPagado = montoPagado;
        this.fechaPago = fechaPago;
    }

    public Pago(String cuota_idCuota, String idPago, boolean penalidad, double montoPagado, LocalDateTime fechaPago) {
        this.cuota_idCuota = cuota_idCuota;
        this.idPago = idPago;
        this.penalidad = penalidad;
        this.montoPagado = montoPagado;
        this.fechaPago = fechaPago;
    }

    public String getCuota_idCuota() {
        return cuota_idCuota;
    }

    public String getIdPago() {
        return idPago;
    }

    public boolean isPenalidad() {
        return penalidad;
    }

    public int getNumeroCuota() {
        return numeroCuota;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setCuota_idCuota(String cuota_idCuota) {
        this.cuota_idCuota = cuota_idCuota;
    }

    public void setIdPago(String idPago) {
        this.idPago = idPago;
    }

    public void setPenalidad(boolean penalidad) {
        this.penalidad = penalidad;
    }

    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

}
