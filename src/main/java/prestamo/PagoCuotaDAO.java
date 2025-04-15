package prestamo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PagoCuotaDAO {
    private Connection conexion;

    public PagoCuotaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public void registrarPago(PagoCuota pago) throws SQLException {
        String sql = "INSERT INTO pago_cuota (idPrestamo, numeroCuota, montoPagado, fechaPago) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, pago.getIdPrestamo());
            stmt.setInt(2, pago.getNumeroCuota());
            stmt.setDouble(3, pago.getMontoPagado());
            stmt.setDate(4, Date.valueOf(pago.getFechaPago()));
            stmt.executeUpdate();
        }
    }

    public double obtenerTotalPagado(int idPrestamo) throws SQLException {
        String sql = "SELECT SUM(montoPagado) FROM pago_cuota WHERE idPrestamo = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    public List<PagoCuota> obtenerPagos(int idPrestamo) throws SQLException {
        List<PagoCuota> pagos = new ArrayList<>();
        String sql = "SELECT * FROM pago_cuota WHERE idPrestamo = ? ORDER BY numeroCuota";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pagos.add(new PagoCuota(
                        rs.getInt("idPrestamo"),
                        rs.getInt("numeroCuota"),
                        rs.getDouble("montoPagado"),
                        rs.getDate("fechaPago").toLocalDate()
                ));
            }
        }
        return pagos;
    }
}


