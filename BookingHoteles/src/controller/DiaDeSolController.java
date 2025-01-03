package src.controller;

import src.model.Alojamiento;
import src.model.Cliente;
import src.model.DiaDeSol;
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
        return alojamiento instanceof DiaDeSol && alojamiento.getCiudad().equalsIgnoreCase(ciudad);
    }


    private void mostrarInformacionDiaDeSol(Alojamiento alojamiento, int totalPersonas) {
        mostrarDetallesGenerales(alojamiento);
        mostrarServiciosIncluidos(alojamiento);
        mostrarCostos(alojamiento, totalPersonas);
    }

    private void mostrarDetallesGenerales(Alojamiento alojamiento) {
        if (alojamiento instanceof DiaDeSol) {
            DiaDeSol diaDeSol = (DiaDeSol) alojamiento;
            System.out.println("Nombre: " + diaDeSol.getNombre());
            System.out.println("Calificación: " + diaDeSol.getCalificacion() + " estrellas");
            System.out.println("Actividades: " + String.join(", ", diaDeSol.getActividades()));
        } else {
            System.out.println("El alojamiento no es un Día de Sol.");
        }
    }

    private void mostrarServiciosIncluidos(Alojamiento alojamiento) {
        if (alojamiento instanceof DiaDeSol) {
            DiaDeSol diaDeSol = (DiaDeSol) alojamiento;
            System.out.println("Incluye Almuerzo: " + convertirBooleanoTexto(diaDeSol.isIncluyeAlmuerzo()));
            System.out.println("Incluye Refrigerio: " + convertirBooleanoTexto(diaDeSol.isIncluyeRefrigerio()));
        } else {
            System.out.println("El alojamiento no es un Día de Sol.");
        }
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
