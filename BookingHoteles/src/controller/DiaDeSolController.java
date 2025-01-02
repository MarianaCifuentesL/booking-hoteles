package src.controller;

import src.model.Alojamiento;
import src.model.Cliente;
import src.model.Reserva;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DiaDeSolController {

    private List<Alojamiento> alojamientos;
    private List<Reserva> reservas;

    public DiaDeSolController(List<Alojamiento> alojamientos, List<Reserva> reservas) {
        this.alojamientos = alojamientos;
        this.reservas = reservas;
    }

    public boolean buscarDiaDeSol(String ciudad, int cantidadAdultos, int cantidadNiños) {
        int totalPersonas = calcularTotalPersonas(cantidadAdultos, cantidadNiños);
        boolean encontrado = false;

        for (Alojamiento alojamiento : alojamientos) {
            if (esDiaDeSolEnCiudad(alojamiento, ciudad)) {
                mostrarInformacionDiaDeSol(alojamiento, totalPersonas);
                encontrado = true;
            }
        }

        return encontrado;
    }

    private int calcularTotalPersonas(int cantidadAdultos, int cantidadNiños) {
        return cantidadAdultos + cantidadNiños;
    }

    private boolean esDiaDeSolEnCiudad(Alojamiento alojamiento, String ciudad) {
        return alojamiento.getCiudad().equalsIgnoreCase(ciudad) && alojamiento.getTipo().equalsIgnoreCase("Día de Sol");
    }

    private void mostrarInformacionDiaDeSol(Alojamiento alojamiento, int totalPersonas) {
        mostrarDetallesGenerales(alojamiento);
        mostrarServiciosIncluidos(alojamiento);
        mostrarCostos(alojamiento, totalPersonas);
    }

    private void mostrarDetallesGenerales(Alojamiento alojamiento) {
        System.out.println("Nombre: " + alojamiento.getNombre());
        System.out.println("Calificación: " + alojamiento.getCalificacion() + " estrellas");
        System.out.println("Actividades: " + String.join(", ", alojamiento.getActividades()));
    }

    private void mostrarServiciosIncluidos(Alojamiento alojamiento) {
        System.out.println("Incluye Almuerzo: " + convertirBooleanoTexto(alojamiento.isIncluyeAlmuerzo()));
        System.out.println("Incluye Refrigerio: " + convertirBooleanoTexto(alojamiento.isIncluyeRefrigerio()));
    }

    private void mostrarCostos(Alojamiento alojamiento, int totalPersonas) {
        double precioPorPersona = alojamiento.getPrecioPorNoche();
        System.out.println("Precio por persona: " + precioPorPersona);
        System.out.println("Costo total para " + totalPersonas + " personas: " + calcularCostoTotal(precioPorPersona, totalPersonas));
    }

    private String convertirBooleanoTexto(boolean valor) {
        return valor ? "Sí" : "No";
    }

    private double calcularCostoTotal(double precioPorPersona, int totalPersonas) {
        return precioPorPersona * totalPersonas;
    }

    public void reservarDiaDeSol(Cliente cliente, Alojamiento alojamiento, LocalDate fechaInicio, Integer cantidadAdultos, Integer cantidadNiños) {

        // Validar que el alojamiento no sea nulo
        if (alojamiento == null) {
            System.out.println("El alojamiento proporcionado no es válido.");
            return;
        }
        // Crear la reserva
        Reserva reserva = new Reserva(cliente, alojamiento, fechaInicio, cantidadAdultos, cantidadNiños);
        // Registrar la reserva en la lista global
        reservas.add(reserva);
        System.out.println("Se ha realizado la reserva con éxito.");
    }




}
