/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prestamo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BTF
 */
public class Cuota {

    private String idPrestamo;
    private String idCuota;
    private double monto;
    private double interes;
    private boolean pagado;
    private LocalDateTime vencimiento;
    private static final double PENALIDAD_POR_MORA = 0.05; // 5%

    public Cuota() {
    }

    public Cuota(String idPrestamo, String idCuota, double monto, double interes, boolean pagado, LocalDateTime vencimiento) {
        this.idPrestamo = idPrestamo;
        this.idCuota = idCuota;
        this.monto = monto;
        this.interes = interes;
        this.pagado = pagado;
        this.vencimiento = vencimiento;
    }

    public String getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(String idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public String getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(String idCuota) {
        this.idCuota = idCuota;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public LocalDateTime getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(LocalDateTime vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String toColumnString() {
        return String.format(
                "%-25s %10.2f %10.2f %-10s %-20s",
                idCuota,
                monto,
                interes,
                pagado ? "Sí" : "No",
                vencimiento != null ? vencimiento.toString() : "null"
        );
    }

    public static List<Cuota> verificarCuotasEnMora(List<Cuota> listaCuotas) {
        LocalDateTime today = LocalDateTime.now();
        List<Cuota> vencidas = new ArrayList<>();
        for (Cuota cuota : listaCuotas) {
            if (cuota.getVencimiento().isBefore(today)) {
                vencidas.add(cuota);
            }
        }
        if (vencidas.isEmpty()) {
            System.out.println("Esta al dia con las cuotas");
        } else {
            System.out.println("Tiene " + vencidas.size() + " cuotas vencidas");
        }
        return vencidas;
    }

    public void showListCuotas(List<Cuota> listaCuotas) {

        String encabezado = String.format(
                "%-10s %10s %10s %-10s %-20s",
                "ID Cuota",
                "Monto",
                "Interés",
                "Pagado",
                "Vencimiento"
        );
        System.out.println(encabezado);
        for (Cuota cuota : listaCuotas) {
            System.out.println(cuota.toColumnString());

        }

    }

    public String showCuotaConMora(Cuota cuota) {
        return String.format(
                "%-25s %10.2f %10.2f %-10s %-20s %-10s",
                cuota.getIdCuota(),
                cuota.getMonto(),
                cuota.getInteres(),
                cuota.isPagado() ? "Sí" : "No",
                cuota.getVencimiento() != null ? cuota.getVencimiento().toString() : "null",
                CalcularMora(cuota.getMonto()));
    }

    public void showListCuotasConMora(List<Cuota> listaCuotas) {
        String encabezado = String.format(
                "%-4s %-25s %10s %10s %-10s %-20s %-10s",
                "ID",
                "ID Cuota",
                "Monto",
                "Interés",
                "Pagado",
                "Vencimiento",
                "Total con Mora"
        );
        System.out.println(encabezado);
        int ID = 1;
        for (Cuota cuota : listaCuotas) {
            if (!cuota.isPagado() && cuota.getVencimiento().isAfter(LocalDateTime.now())) {
                System.out.println(String.format("%-4s %s", ID, showCuotaConMora(cuota)));
            } else {
                System.out.println(String.format(
                        "%-4s %-25s %10.2f %10.2f %-10s %-20s %-10s",
                        ID,
                        cuota.getIdCuota(),
                        cuota.getMonto(),
                        cuota.getInteres(),
                        cuota.isPagado() ? "Sí" : "No",
                        cuota.getVencimiento() != null ? cuota.getVencimiento().toString() : "null",
                        cuota.isPagado() ? "Pagado" : "Sin Mora"));
            }

        }
    }

    public String CalcularMora(double montoOriginal) {
        return String.valueOf(montoOriginal * PENALIDAD_POR_MORA);
    }

}
