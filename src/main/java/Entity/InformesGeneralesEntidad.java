/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.util.ArrayList;
import java.util.List;
import prestamo.Cuota;
import prestamo.Pago;
import prestamo.Prestamo;

/**
 *
 * @author BTF
 */
public class InformesGeneralesEntidad {

    private List<Cliente> listaDeClientes;
    private List<Prestamo> listaDePrestamos;
    private List<Cuota> listaDeCuotas;
    private List<Pago> listaDePagos;
    private List<String> extraData;

    public InformesGeneralesEntidad() {
        listaDeClientes = new ArrayList<>();
        listaDePrestamos = new ArrayList<>();
        listaDeCuotas = new ArrayList<>();
        listaDePagos = new ArrayList<>();
        extraData = new ArrayList<>();
    }

    public InformesGeneralesEntidad(List<Cliente> listaDeClientes, List<Prestamo> listaDePrestamos, List<Cuota> listaDeCuotas, List<Pago> listaDePagos, List<String> extraData) {
        this.listaDeClientes = listaDeClientes;
        this.listaDePrestamos = listaDePrestamos;
        this.listaDeCuotas = listaDeCuotas;
        this.listaDePagos = listaDePagos;
        this.extraData = extraData;
    }

    public List<Cliente> getListaDeClientes() {
        return listaDeClientes;
    }

    public void addListaDeClientes(Cliente cliente) {
        this.listaDeClientes.add(cliente);
    }

    public void addAllListaDeClientes(List<Cliente> listaDeClientes) {
        this.listaDeClientes.addAll(listaDeClientes);
    }

    public void setListaDeClientes(List<Cliente> listaDeClientes) {
        this.listaDeClientes = listaDeClientes;
    }

    public List<Prestamo> getListaDePrestamos() {
        return listaDePrestamos;
    }

    public void addListaDePrestamos(Prestamo prestamo) {
        this.listaDePrestamos.add(prestamo);
    }

    public void addAllListaDePrestamos(List<Prestamo> listaDePrestamos) {
        this.listaDePrestamos.addAll(listaDePrestamos);
    }

    public void setListaDePrestamos(List<Prestamo> listaDePrestamos) {
        this.listaDePrestamos = listaDePrestamos;
    }

    public List<Cuota> getListaDeCuotas() {
        return listaDeCuotas;
    }

    public void addListaDeCuotas(Cuota cuota) {
        this.listaDeCuotas.add(cuota);
    }

    public void addAllListaDeCuotas(List<Cuota> listaDeCuotas) {
        this.listaDeCuotas.addAll(listaDeCuotas);
    }

    public void setListaDeCuotas(List<Cuota> listaDeCuotas) {
        this.listaDeCuotas = listaDeCuotas;
    }

    public List<Pago> getListaDePagos() {
        return listaDePagos;
    }

    public void addListaDePagos(Pago pago) {
        this.listaDePagos.add(pago);
    }

    public void addAllListaDePagos(List<Pago> listaDePagos) {
        this.listaDePagos.addAll(listaDePagos);
    }

    public void setListaDePagos(List<Pago> listaDePagos) {
        this.listaDePagos = listaDePagos;
    }

    public List<String> getExtraData() {
        return extraData;
    }

    public void addExtraData(String dato){
        this.extraData.add(dato);
    }
    
    public void addAllExtraData(List<String> extraData){
        this.extraData.addAll(extraData);
    }
    
    public void setExtraData(List<String> extraData) {
        this.extraData = extraData;
    }
    
}
