package prestamo;

import java.time.LocalDateTime;
import java.util.List;

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

    public String printPago(boolean encabezado) {
        StringBuilder pago = new StringBuilder();
        if (encabezado) {
            pago.append(String.format("%-25s | %-25s | %-10s | %-12s | %-15s |%n" ,
                    "ID Cuota",
                    "ID Pago",
                    "Penalidad",
                    "Monto Pago",
                    "Fecha Del Pago"
            ));
        }
        pago.append(String.format("%-25s | %-25s | %-10s | %-12s | %-15s |",
                this.getCuota_idCuota(),
                this.getIdPago(),
                this.isPenalidad() ? "Si" : "No",
                this.getMontoPagado(),
                this.getFechaPago()
        ));
        return pago.toString();
    }

    public void showPago() {
        String encabezado = String.format("%-25s | %-25s | %-10s | %-12s | %-15s |",
                "ID Cuota",
                "ID Pago",
                "Penalidad",
                "Monto Pago",
                "Fecha Del Pago"
        );
        System.out.println(encabezado);
        System.out.println(String.format("%-25s | %-25s | %-10s | %-12s | %-15s |",
                this.getCuota_idCuota(),
                this.getIdPago(),
                this.isPenalidad() ? "Si" : "No",
                this.getMontoPagado(),
                this.getFechaPago()
        ));
    }

    public static void showListaPago(List<Pago> ListaDePagos) {
        String encabezado = String.format("%-4s | %-25s | %-25s | %-10s | %-12s | %-15s |",
                "ID",
                "ID Cuota",
                "ID Pago",
                "Penalidad",
                "Monto Pago",
                "Fecha Del Pago"
        );
        System.out.println(encabezado);
        int ID = 1;
        for (Pago pago : ListaDePagos) {
            System.out.println(String.format("%-4s | %-25s | %-25s | %-10s | %-12s | %-15s |",
                    ID,
                    pago.getCuota_idCuota(),
                    pago.getIdPago(),
                    pago.isPenalidad() ? "Si" : "No",
                    pago.getMontoPagado(),
                    pago.getFechaPago()
            ));
            ID++;
        }
    }
}
