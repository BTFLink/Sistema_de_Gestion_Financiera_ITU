/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.util.Scanner;

/**
 *
 * @author BTF
 */
public class LeerDataType {

    static final Scanner scanner = new Scanner(System.in);
    private static final String INVALIDDATA = "Dato Ingresado Invalido";

    /*
    *LeerInt siempre devolvera un valor, si no se asigna un valor minimo
    *devolvera Integer.MIN_VALUE, si se le ha asignado un valor minimo
    *devolvera minValue(Valor minimo) - 1 (menos uno)
    */
    public static int LeerInt() {
        return LeerInt("", Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int LeerInt(String message) {
        return LeerInt(message, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int LeerInt(int minValue, int maxValue) {
        return LeerInt("", minValue, maxValue);
    }

    public static int LeerInt(String message, int minValue, int maxValue) {
        int value;
        try {
            if (!message.equals("")) {
                System.out.println(message);
            }
            value = scanner.nextInt();
            scanner.nextLine();
            if (value >= minValue && value <= maxValue) {
                return value;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(INVALIDDATA);
            scanner.nextLine();
        }
        if (minValue == Integer.MIN_VALUE) {
            return minValue;
        }
        return minValue - 1;
    }
    
    /*
    *LeerLong siempre devolvera un valor, si no se asigna un valor minimo
    *devolvera Long.MIN_VALUE, si se le ha asignado un valor minimo
    *devolvera minValue(Valor minimo) - 1 (menos uno)
    */
    
    public static long LeerLong() {
        return LeerLong("", Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public static long LeerLong(String message) {
        return LeerLong(message, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public static long LeerLong(long minValue, long maxValue) {
        return LeerLong("", minValue, maxValue);
    }

    public static long LeerLong(String message, long minValue, long maxValue) {
        long value;
        try {
            if (!message.equals("")) {
                System.out.println(message);
            }
            value = scanner.nextLong();
            if (value >= minValue && value <= maxValue) {
                return value;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(INVALIDDATA);
        }
        if (minValue == Long.MIN_VALUE) {
            return minValue;
        }
        return minValue - 1;
    }
    
    
    
    public static double LeerDouble() {
        return LeerDouble("", Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public static double LeerDouble(String message) {
        return LeerDouble(message, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public static double LeerDouble(double minValue, double maxValue) {
        return LeerDouble("", minValue, maxValue);
    }

    /*
    *LeerDouble siempre devolvera un valor, si el valor ingresado es invalido
    *devolvera Double.NaN (Not a Number, no un numero)
    */
    public static double LeerDouble(String message, double minValue, double maxValue) {
        double value;
        try {
            if (!message.equals("")) {
                System.out.println(message);
            }
            value = scanner.nextDouble();
            if (value >= minValue && value <= maxValue) {
                return value;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(INVALIDDATA);
        }
        
        return Double.NaN;
    }
}
