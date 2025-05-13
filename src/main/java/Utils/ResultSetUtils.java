/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 *
 * @author BTF
 */
public class ResultSetUtils {

    public static String getStringSafe(ResultSet rs, String column) {
        try {
            Object value = rs.getObject(column);
            return value != null ? String.valueOf(value) : "";
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getIntSafe(ResultSet rs, String column) {
        try {
            Object value = rs.getObject(column);
            return value != null ? Integer.parseInt(value.toString()) : 0;
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static double getDoubleSafe(ResultSet rs, String column) {
        try {
            Object value = rs.getObject(column);
            return value != null ? Double.parseDouble(value.toString()) : 0.0;
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public static long getLongSafe(ResultSet rs, String column) {
        try {
            Object value = rs.getObject(column);
            return value != null ? Long.parseLong(value.toString()) : 0L;
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static boolean getBooleanSafe(ResultSet rs, String column) {
        try {
            Object value = rs.getObject(column);
            return value != null && Boolean.parseBoolean(value.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static LocalDateTime getLocalDateTimeSafe(ResultSet rs, String column) {
        try {
            Object value = rs.getObject(column);
            if (value instanceof Timestamp) {
                return ((Timestamp) value).toLocalDateTime();
            } else if (value instanceof LocalDateTime) {
                return (LocalDateTime) value;
            } else if (value != null) {
                // Intentar parsear si viene como String
                return LocalDateTime.parse(value.toString());
            } else {
                return null;
            }
        } catch (SQLException | DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
