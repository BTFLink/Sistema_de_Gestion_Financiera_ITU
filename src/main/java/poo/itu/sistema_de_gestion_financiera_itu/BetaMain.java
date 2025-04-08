/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo.itu.sistema_de_gestion_financiera_itu;

import Entity.cliente;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author BTF
 */
public class BetaMain {
    
    static List<cliente> listOfClientes = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    
    
    public static void main(String[] args) {
        AutomaticAdd();
        String decision;
        String HiAnw;
        int Hi=0;
        do {            
            scanner.nextLine();
            Menu();
            decision=scanner.nextLine();
            
            switch (decision) {
                case "1":
                    AddCliente();
                    break;
                case "2":
                    ModCliente();
                    break;
                case "3":
                    DelCliente();
                    break;
                case "4":
                    switch(Hi){
                        case 0: HiAnw="Hi! Nice to Meet ya :D";
                        case 1: HiAnw="Hello! :3";
                        case 2: HiAnw="Hello? :/";
                        case 3: HiAnw=". . . º-º)";
                        default: HiAnw="No body answered";
                    }
                    System.out.println(HiAnw);
                    Hi++;
                    break;
                case "5":
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Opcion Invalida");
            }
        } while (true);
    }
    
    private static void Menu(){
        System.out.print("""
                           \tMenú
                           1) Añadir Cliente
                           2) Modificar Cliente
                           3) Eliminar Cliente
                           4) HALLO :D
                           5) Salir
                           >
                           """);
    }
    
    private static void AutomaticAdd(){
        String nombre ="Bruno Olguin",
               direccion = "Nueva Esperanza S/N, La colonia, Junin",
                email="Brunoloco63@gmail.com";
        int dni=41272531;
        long telefono;
        telefono = 2634336877L;
        cliente A01= new cliente(nombre, direccion, email, dni, telefono);
        listOfClientes.add(A01);
    }
    
    private static void AddCliente(){
        String nombre, direccion, email;
        int dni;
        long telefono;
        
        System.out.println("Ingrese el DNI del cliente\n>");
        try {
            dni=scanner.nextInt();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        for (cliente c : listOfClientes) {
            if(c.getDni()==dni){
                System.out.println("Se ha encontrado un cliente registrado bajo el mismo DNI");
                System.out.println("Nombre: "+c.getNombre()+"\nDireccion: "+c.getDireccion());
                return;
            }
        }
        scanner.nextLine();
        System.out.print("Ingrese el nombre completo del cliente\n>");
        nombre=scanner.nextLine();
        System.out.print("Ingrese la direccion del cliente\n>");
        direccion=scanner.nextLine();
        System.out.print("Ingrese el email del cliente\n>");
        email=scanner.nextLine();
        System.out.println("Ingrese un numero de telefono\n>");
        try {
            telefono=scanner.nextLong();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        cliente newCliente = new cliente(nombre, direccion, email, dni, telefono);
        
        listOfClientes.add(newCliente);
    }
    
    private static void ModCliente(){
        int dnisearch,index;
        boolean found=false;
        System.out.print("Ingrese el dni del cliente a modificar\n>");
        dnisearch=scanner.nextInt();
        for (cliente c : listOfClientes) {
            if(c.getDni()==dnisearch){
                found=true;
                index=listOfClientes.indexOf(c);
            }
        }
        if(found){
            System.out.println("Cliente encontrado");
        }else{
            System.out.println("No se encontro el cliente solicitado");
        }
    }
    
    private static void DelCliente(){
        int dnisearch,index;
        boolean found=false;
        System.out.print("Ingrese el dni del cliente a eliminar\n>");
        dnisearch=scanner.nextInt();
        for (cliente c : listOfClientes) {
            if(c.getDni()==dnisearch){
                found=true;
                index=listOfClientes.indexOf(c);
            }
        }
        if(found){
            System.out.println("Cliente encontrado");
        }else{
            System.out.println("No se encontro el cliente solicitado");
        }
    }
    
}
