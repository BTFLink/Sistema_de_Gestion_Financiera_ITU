/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.util.HashMap;
import java.util.Map;
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

    public static String LeerUUID(String ingresado) {
        return LeerUUID("", ingresado);
    }

    public static String LeerUUID(String porDefault, String ingresado) {
        HashMap<String, String> RegExs = new HashMap();
        RegExs.put("CLI", "^CLI-(\\*|[0-9A-Fa-f]{1,})$");
        RegExs.put("PRE", "^PRE-(\\*|[0-9A-Fa-f]{1,})$");
        RegExs.put("CUO", "^CUO-(\\*|[0-9A-Fa-f]{1,})$");
        RegExs.put("PAG", "^PAG-(\\*|[0-9A-Fa-f]{1,})$");
        if (ingresado.contains("EXT-")) {
            return ingresado;
        }
        if (ingresado.equals("0")) {
            return ingresado;
        }
        if (porDefault.equals("")) {
            if (ingresado.length() <= 4) {
                return "";
            }
            for (Map.Entry<String, String> RegEx : RegExs.entrySet()) {
                if (ingresado.matches(RegEx.getValue())) {
                    if(ingresado.contains("*")){return ingresado;}
                    return RellenarUUID(RegEx.getKey(), ingresado);
                }
            }
        } else {
            if (ingresado.matches(RegExs.get(porDefault)) || (ingresado.matches("^(\\*|[0-9A-Fa-f]{1,})$") && !ingresado.equals("0"))) {
                if(ingresado.contains("*")){return porDefault+"-"+ingresado;}
                return RellenarUUID(porDefault, ingresado);
            }
        }
        return "";
    }

    private static String RellenarUUID(String clave, String data) {
        String prefijo = clave + "-";

        // Si ya tiene el prefijo, separarlo. Si no, agregarlo.
        if (data.startsWith(prefijo)) {
            data = data.substring(prefijo.length());
        }

        // Ahora data no tiene el prefijo. Verificamos si hay que rellenar.
        int totalLength = prefijo.length() + data.length();

        if (totalLength < 20) {
            int cerosNecesarios = 20 - totalLength;
            StringBuilder relleno = new StringBuilder();
            for (int i = 0; i < cerosNecesarios; i++) {
                relleno.append("0");
            }
            data = relleno + data;
        }

        return prefijo + data;
    }
}
