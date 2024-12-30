package src.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Reserva {
    private Cliente cliente;
    private Alojamiento alojamiento; // Propiedad existente
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer cantidadAdultos;
    private Integer cantidadNiños;
    private Map<String, Integer> habitacionesPorTipo; // Tipos y cantidades de habitaciones
    private String horaLlegada; // Propiedad existente
    private List<String> idsHabitacionesReservadas; // Lista de IDs únicos de habitaciones reservadas

    //Constructor para Hotel
    public Reserva(Cliente cliente, Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, Integer cantidadAdultos, Integer cantidadNiños, Map<String, Integer> habitacionesPorTipo, String horaLlegada) {
        this.cliente = cliente;
        this.alojamiento = alojamiento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cantidadAdultos = cantidadAdultos;
        this.cantidadNiños = cantidadNiños;
        this.habitacionesPorTipo = habitacionesPorTipo;
        this.horaLlegada = horaLlegada;
    }

    //    Constructor para Finca y Apartamento
    public Reserva(Cliente cliente, Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, Integer cantidadAdultos, Integer cantidadNiños, String horaLlegada) {
        this.cliente = cliente;
        this.alojamiento = alojamiento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cantidadAdultos = cantidadAdultos;
        this.cantidadNiños = cantidadNiños;
        this.horaLlegada = horaLlegada;
    }


    //    Constructor para Día de Sol
    public Reserva(Cliente cliente, Alojamiento alojamiento, LocalDate fechaInicio, Integer cantidadAdultos, Integer cantidadNiños) {
        this.cliente = cliente;
        this.alojamiento = alojamiento;
        this.fechaInicio = fechaInicio;
        this.cantidadAdultos = cantidadAdultos;
        this.cantidadNiños = cantidadNiños;
    }

    public Integer getCantidadAdultos() {
        return cantidadAdultos;
    }

    public void setCantidadAdultos(Integer cantidadAdultos) {
        this.cantidadAdultos = cantidadAdultos;
    }

    public Integer getCantidadNiños() {
        return cantidadNiños;
    }

    public void setCantidadNiños(Integer cantidadNiños) {
        this.cantidadNiños = cantidadNiños;
    }

    public List<String> getIdsHabitacionesReservadas() {
        return idsHabitacionesReservadas;
    }

    public void setIdsHabitacionesReservadas(List<String> idsHabitacionesReservadas) {
        this.idsHabitacionesReservadas = idsHabitacionesReservadas;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Map<String, Integer> getHabitacionesPorTipo() {
        return habitacionesPorTipo;
    }

    public void setHabitacionesPorTipo(Map<String, Integer> habitacionesPorTipo) {
        this.habitacionesPorTipo = habitacionesPorTipo;
    }

    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public String getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(String horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    // Métodos para gestionar las habitaciones reservadas

    public void agregarHabitacionReservada(String idHabitacion) {
        if (!idsHabitacionesReservadas.contains(idHabitacion)) {
            idsHabitacionesReservadas.add(idHabitacion);
            System.out.println("Habitación con ID " + idHabitacion + " añadida a la reserva.");
        }
    }

    public void eliminarHabitacionReservada(String idHabitacion) {
        if (idsHabitacionesReservadas.remove(idHabitacion)) {
            System.out.println("Habitación con ID " + idHabitacion + " eliminada de la reserva.");
        }
    }
}
