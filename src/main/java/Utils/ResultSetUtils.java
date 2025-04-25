/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.sql.ResultSet;
import java.sql.SQLException;

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
}

