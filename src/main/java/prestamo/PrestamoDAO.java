package prestamo;

import java.sql.*;

public class PrestamoDAO {
    private Connection conexion;

    public PrestamoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public int guardarPrestamo(Prestamo prestamo, int idCliente) throws SQLException {
        String sql = "INSERT INTO prestamo (idCliente, tipoPrestamo, tasaInteresAnual, monto, cuotas, montoCuota, totalADevolver, numeroPrestamo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, idCliente);
            stmt.setString(2, prestamo.getTipoPrestamo());
            stmt.setDouble(3, prestamo.getTasaInteresAnual());
            stmt.setDouble(4, prestamo.getMonto());
            stmt.setInt(5, prestamo.getCuotas());
            stmt.setDouble(6, prestamo.getMontoCuotas());
            stmt.setDouble(7, prestamo.getTotalADevolver());
            stmt.setString(8, prestamo.getNumeroPrestamo());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idPrestamo = rs.getInt(1);
                prestamo.setIdPrestamoBD(idPrestamo); // Necesitás agregar este campo en Prestamo
                return idPrestamo;
            }
        }
        return -1;
    }
}

