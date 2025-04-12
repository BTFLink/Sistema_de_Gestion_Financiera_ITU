package prestamo;

public class Main {
    public static void main(String[] args) {
        PrestamoPersonal p1 = new PrestamoPersonal(10000, 12, 12, TipoCuota.VARIABLE);
        p1.mostrarInfoPrestamo();

        System.out.println("\n---\n");

        PrestamoHipotecario p2 = new PrestamoHipotecario(100000, 3, 36, TipoCuota.FIJA);
        p2.mostrarInfoPrestamo();
    }
}
