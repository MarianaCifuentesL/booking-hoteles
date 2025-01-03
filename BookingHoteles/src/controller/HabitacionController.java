package src.controller;

import src.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HabitacionController {

    private List<Alojamiento> alojamientos;

    public HabitacionController(List<Alojamiento> alojamientos) {
        this.alojamientos = alojamientos;
    }

    public void confirmarHabitaciones(String nombreHotel, LocalDate fechaInicio, LocalDate fechaFin, int cantidadAdultos, int cantidadNiños, int cantidadHabitaciones) {
        System.out.println("\n--- Confirmar habitaciones para: " + nombreHotel + " ---");
        Alojamiento alojamiento = buscarAlojamientoPorNombre(nombreHotel);
        if (alojamiento == null) {
            System.out.println("No se encontró un hotel con el nombre indicado.");
            return;
        }
        int totalPersonas = cantidadAdultos + cantidadNiños;
        boolean disponibilidad = mostrarHabitacionesDisponibles(alojamiento, fechaInicio, fechaFin, totalPersonas, cantidadHabitaciones);
        if (!disponibilidad) {
            System.out.println("No hay habitaciones disponibles para las fechas indicadas.");
        }
    }

    public Alojamiento buscarAlojamientoPorNombre(String nombreAlojamiento) {
        return alojamientos.stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombreAlojamiento))
                .findFirst()
                .orElse(null);
    }

    // Método auxiliar para mostrar habitaciones disponibles
//    private boolean mostrarHabitacionesDisponibles(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, int totalPersonas, int cantidadHabitaciones) {
//        boolean disponibilidad = false;
//
//        for (Habitacion habitacion : alojamiento.getHabitaciones()) {
//            int disponiblesParaFechas = calcularDisponibilidad(habitacion, fechaInicio, fechaFin);
//
//            if (esHabitacionAdecuada(habitacion, totalPersonas, disponiblesParaFechas, cantidadHabitaciones)) {
//                mostrarDetallesHabitacion(habitacion, disponiblesParaFechas);
//                disponibilidad = true;
//            }
//        }
//        return disponibilidad;
//    }

    private boolean mostrarHabitacionesDisponibles(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, int totalPersonas, int cantidadHabitaciones) {
        boolean disponibilidad = false;
        if (alojamiento instanceof Hotel) { // Verificamos que el alojamiento sea un Hotel
            Hotel hotel = (Hotel) alojamiento;
            for (HabitacionComponent habitacion : hotel.getHabitaciones()) {  // Habitaciones solo disponibles en hoteles
                int disponiblesParaFechas = habitacion.getDisponibilidad(fechaInicio, fechaFin);
                if (esHabitacionAdecuada(habitacion, totalPersonas, disponiblesParaFechas, cantidadHabitaciones)) {
                    habitacion.mostrarDetalles();  // Mostrar detalles de la habitación
                    disponibilidad = true;
                }
            }
        }
        return disponibilidad;
    }



    // Método auxiliar para verificar si una habitación es adecuada
//    private boolean esHabitacionAdecuada(Habitacion habitacion, int totalPersonas, int disponiblesParaFechas, int cantidadHabitaciones) {
//        return habitacion.getCapacidad() >= totalPersonas && disponiblesParaFechas >= cantidadHabitaciones;
//    }
    private boolean esHabitacionAdecuada(HabitacionComponent habitacion, int totalPersonas, int disponiblesParaFechas, int cantidadHabitaciones) {
        return habitacion.getCapacidad() >= totalPersonas && disponiblesParaFechas >= cantidadHabitaciones;
    }


    // Método auxiliar para mostrar los detalles de una habitación
    private void mostrarDetallesHabitacion(Habitacion habitacion, int disponiblesParaFechas) {
        System.out.println("\nTipo de habitación: " + habitacion.getTipo());
        System.out.println("Características: " + habitacion.getCaracteristicas());
        System.out.println("Capacidad: " + habitacion.getCapacidad() + " personas");
        System.out.println("Precio por noche: " + habitacion.getPrecio());
        System.out.println("Cantidad disponible en las fechas seleccionadas: " + disponiblesParaFechas);
    }

    private int calcularDisponibilidad(Habitacion habitacion, LocalDate fechaInicio, LocalDate fechaFin) {
        int cantidadDisponible = habitacion.getCantidad();

        // Iterar sobre las reservas para verificar conflictos de fechas
        for (Reserva reserva : habitacion.getReservas()) {
            if (!(fechaFin.isBefore(reserva.getFechaInicio()) || fechaInicio.isAfter(reserva.getFechaFin()))) {
                // Obtener cuántas habitaciones de este tipo están ocupadas en esta reserva
                Map<String, Integer> habitacionesReservadas = reserva.getHabitacionesPorTipo();
                int cantidadReservada = habitacionesReservadas.getOrDefault(habitacion.getTipo(), 0);
                cantidadDisponible -= cantidadReservada; // Reducir por la cantidad reservada
            }
        }
        return Math.max(cantidadDisponible, 0); // Asegurarse de no retornar valores negativos
    }

    // Método principal para verificar la disponibilidad de habitaciones por tipo
    public boolean verificarDisponibilidadHabitaciones(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, Map<String, Integer> habitacionesPorTipo) {
        for (Map.Entry<String, Integer> entry : habitacionesPorTipo.entrySet()) {
            String tipoHabitacion = entry.getKey();
            int cantidadSolicitada = entry.getValue();

            if (!verificarDisponibilidadTipoHabitacion(alojamiento, tipoHabitacion, cantidadSolicitada, fechaInicio, fechaFin)) {
                return false;
            }
        }
        System.out.println("Hay disponibilidad para todas las habitaciones solicitadas.");
        return true;
    }

    // Método auxiliar para verificar la disponibilidad de un tipo de habitación
    private boolean verificarDisponibilidadTipoHabitacion(Alojamiento alojamiento, String tipoHabitacion, int cantidadSolicitada, LocalDate fechaInicio, LocalDate fechaFin) {
        int cantidadDisponible = calcularHabitacionesDisponibles(alojamiento, tipoHabitacion, fechaInicio, fechaFin);
        if (cantidadDisponible < cantidadSolicitada) {
            System.out.println("No hay suficientes habitaciones disponibles para el tipo " + tipoHabitacion + ".");
            return false;
        }
        return true;
    }

    // Método auxiliar para calcular la cantidad de habitaciones disponibles para un tipo específico
    private int calcularHabitacionesDisponibles(Alojamiento alojamiento, String tipoHabitacion, LocalDate fechaInicio, LocalDate fechaFin) {
        if (alojamiento instanceof Hotel) {  // Solo accedemos a habitaciones si es un Hotel
            Hotel hotel = (Hotel) alojamiento;
            return hotel.getHabitaciones().stream()
                    .filter(h -> h.getTipo().equalsIgnoreCase(tipoHabitacion) && h.estaDisponible(fechaInicio, fechaFin))
                    .mapToInt(Habitacion::getCantidad)
                    .sum();
        }
        return 0;  // No hay habitaciones disponibles si no es un Hotel
    }


    // Método principal para verificar disponibilidad de habitaciones
    public boolean verificarDisponibilidad(String tipoHabitacion, LocalDate fechaInicio, LocalDate fechaFin, int cantidadSolicitada) {
        for (Alojamiento alojamiento : alojamientos) {
            int habitacionesDisponibles = contarHabitacionesDisponibles(alojamiento, tipoHabitacion, fechaInicio, fechaFin);
            if (habitacionesDisponibles >= cantidadSolicitada) {
                return true; // Hay suficientes habitaciones disponibles
            }
        }
        return false; // No hay suficientes habitaciones disponibles
    }

    // Método auxiliar para contar las habitaciones disponibles en un alojamiento
    private int contarHabitacionesDisponibles(Alojamiento alojamiento, String tipoHabitacion, LocalDate fechaInicio, LocalDate fechaFin) {
        if (alojamiento instanceof Hotel) {  // Solo accedemos a habitaciones si es un Hotel
            Hotel hotel = (Hotel) alojamiento;
            return (int) hotel.getHabitaciones().stream()
                    .filter(h -> h.getTipo().equalsIgnoreCase(tipoHabitacion) && h.estaDisponible(fechaInicio, fechaFin))
                    .count();
        }
        return 0;  // No hay habitaciones disponibles si no es un Hotel
    }


    private void reservarHabitaciones(Reserva reserva, Habitacion habitacion, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            habitacion.agregarReserva(reserva);
        }
        // Actualizar el mapa de la reserva con el nuevo tipo de habitación
        reserva.getHabitacionesPorTipo().put(habitacion.getTipo(), cantidad);
        System.out.println("Se han reservado " + cantidad + " habitación(es) del tipo " + habitacion.getTipo() + ".");
    }

    // Método para obtener el precio de la habitación más simple en un alojamiento
    public double obtenerPrecioHabitacionMasSimple(Alojamiento alojamiento) {
        if (alojamiento instanceof Hotel) {  // Solo accedemos a habitaciones si es un Hotel
            Hotel hotel = (Hotel) alojamiento;
            return hotel.getHabitaciones().stream()
                    .mapToDouble(Habitacion::getPrecio)
                    .min()
                    .orElse(0);
        }
        return 0;  // Si no es un Hotel, no tiene habitaciones
    }


    // Método auxiliar para procesar y validar habitaciones
    public boolean procesarTiposDeHabitaciones(Alojamiento alojamiento, Map<String, Integer> habitacionesPorTipo,
                                               LocalDate fechaInicio, LocalDate fechaFin, Map<Habitacion, Integer> habitacionesReservadas) {
        for (Map.Entry<String, Integer> entry : habitacionesPorTipo.entrySet()) {
            String tipoHabitacion = entry.getKey();
            int cantidadSolicitada = entry.getValue();

            // Validar y reservar habitaciones por tipo
            if (!validarYReservarHabitaciones(alojamiento, tipoHabitacion, cantidadSolicitada, fechaInicio, fechaFin, habitacionesReservadas)) {
                System.out.println("No hay suficientes habitaciones disponibles para el tipo " + tipoHabitacion + ".");
                return false;
            }
        }
        return true;
    }

    // Método auxiliar para validar y reservar habitaciones por tipo
    private boolean validarYReservarHabitaciones(Alojamiento alojamiento, String tipoHabitacion, int cantidadSolicitada,
                                                 LocalDate fechaInicio, LocalDate fechaFin, Map<Habitacion, Integer> habitacionesReservadas) {
        // Obtener habitaciones disponibles
        List<Habitacion> habitacionesDisponibles = obtenerHabitacionesDisponibles(alojamiento, tipoHabitacion, fechaInicio, fechaFin);
        // Validar cantidad total disponible
        int cantidadTotalDisponible = habitacionesDisponibles.stream().mapToInt(Habitacion::getCantidad).sum();
        if (cantidadTotalDisponible < cantidadSolicitada) return false;
        // Reservar habitaciones necesarias
        reservarHabitaciones(habitacionesDisponibles, cantidadSolicitada, habitacionesReservadas, fechaInicio, fechaFin);
        return true;
    }

    // Método auxiliar para obtener habitaciones disponibles
    private List<Habitacion> obtenerHabitacionesDisponibles(Alojamiento alojamiento, String tipoHabitacion,
                                                            LocalDate fechaInicio, LocalDate fechaFin) {
        if (alojamiento instanceof Hotel) {  // Solo accedemos a habitaciones si es un Hotel
            Hotel hotel = (Hotel) alojamiento;
            return hotel.getHabitaciones().stream()
                    .filter(h -> h.getTipo().equalsIgnoreCase(tipoHabitacion) && h.estaDisponible(fechaInicio, fechaFin))
                    .toList();
        }
        return new ArrayList<>();  // Si no es un Hotel, devolvemos una lista vacía
    }


    // Método auxiliar para reservar habitaciones
    private void reservarHabitaciones(List<Habitacion> habitacionesDisponibles, int cantidadSolicitada,
                                      Map<Habitacion, Integer> habitacionesReservadas, LocalDate fechaInicio, LocalDate fechaFin) {
        for (Habitacion habitacion : habitacionesDisponibles) {
            int cantidadAReservar = Math.min(cantidadSolicitada, habitacion.getCantidad());
            habitacion.setCantidad(habitacion.getCantidad() - cantidadAReservar);
            habitacionesReservadas.put(habitacion, cantidadAReservar);
            cantidadSolicitada -= cantidadAReservar;
            if (cantidadSolicitada == 0) break;
        }
    }

}