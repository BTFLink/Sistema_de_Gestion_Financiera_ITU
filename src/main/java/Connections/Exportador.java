package Connections;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Exportador {

    private static final Scanner sc = new Scanner(System.in);

    public Exportador() {

    }

    public static void FileExporter(String texto) {
        FileExporter(texto, false);
    }

    public static void FileExporter(String texto, boolean tipolista) {
        String nombreArchivo = FileName();

        String csv = Arrays.stream(texto.split("\\R")) // "\\R" detecta saltos de línea
                .map(line -> {
                    line = line.trim();
                    // Si contiene '|', reemplaza los separadores con punto y coma
                    if (line.contains("|")) {
                        return line.replaceAll(" *\\| *", ";");
                    }

                    // Si contiene "clave: valor" (soporta letras latinas con acentos y ñ)
                    if (line.matches("(?i)^[\\p{L}0-9ÁÉÍÓÚÜÑáéíóúüñ ]+:[ ].+")) {
                        String[] parts = line.split(":", 2); // solo divide en dos partes
                        String clave = parts[0].trim();
                        String valor = parts[1].trim();

                        // Si el valor es numérico o tiene punto decimal, lo encerramos en comillas
                        if (valor.matches("-?\\d+(\\.\\d+)?([eE][+-]?\\d+)?")) {
                            valor = "\"" + valor + "\"";
                        }

                        return clave + ";" + valor;
                    }

                    // Si no coincide con ningún patrón, se deja tal cual
                    return line;
                })
                .collect(Collectors.joining("\n"));

        try (FileOutputStream fos = new FileOutputStream(nombreArchivo); OutputStreamWriter os = new OutputStreamWriter(fos, StandardCharsets.UTF_8); PrintWriter pw = new PrintWriter(os)) {

            fos.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}); // BOM para Excel

            pw.print(csv);
            System.out.println("Archivo creado exitosamente: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        }
    }

    private static String FileName() {
        System.out.println("Ingrese el nombre del archivo");
        String RegExFileName = "^[^\\\\/:*?\"<>|]+\\.csv$"; // Asegura nombre válido con .txt
        String nombreArchivo;

        do {
            nombreArchivo = sc.nextLine();

            // Añadir ".txt" solo si el usuario no lo escribió
            if (!nombreArchivo.toLowerCase().endsWith(".csv")) {
                nombreArchivo = nombreArchivo + ".csv";
            }

            // Validar después de añadir la extensión
            if (!nombreArchivo.matches(RegExFileName)) {
                System.out.println("Nombre no válido. Intente de nuevo:");
                continue;
            }

            File archivo = new File(nombreArchivo);
            if (archivo.exists()) {
                System.out.println("El archivo ya existe. ¿Desea sobrescribirlo? (s/n)");
                String respuesta = sc.nextLine().trim().toLowerCase();
                if (respuesta.equals("s")) {
                    break;
                } else {
                    System.out.println("Ingrese un nuevo nombre de archivo:");
                }
            } else {
                break;
            }

        } while (true);

        return nombreArchivo;
    }
}
