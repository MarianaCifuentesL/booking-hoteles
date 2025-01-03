package src.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Habitacion implements HabitacionComponent {
    private String tipo;
    private Double precio;
    private String caracteristicas;
    private Integer capacidad;
    private Integer cantidad;
    private List<Reserva> reservas;

    public Habitacion(String tipo, Double precio, String caracteristicas, Integer capacidad, Integer cantidad) {
        this.tipo = tipo;
        this.precio = precio;
        this.caracteristicas = caracteristicas;
        this.capacidad = capacidad;
        this.reservas = new ArrayList<>();
        this.cantidad = cantidad;
    }


    // Método para verificar si la habitación está disponible para el rango de fechas
    public boolean estaDisponible(LocalDate fechaInicio, LocalDate fechaFin) {
        long reservasConflicto = reservas.stream()
                .filter(reserva -> !(fechaFin.isBefore(reserva.getFechaInicio()) || fechaInicio.isAfter(reserva.getFechaFin())))
                .count();
        return reservasConflicto < cantidad; // Verifica que haya habitaciones restantes
    }


    public boolean estaOcupada(LocalDate fechaInicio, LocalDate fechaFin) {
        for (Reserva reserva : reservas) {
            // Si las fechas se solapan, la habitación está ocupada
            if (!(fechaFin.isBefore(reserva.getFechaInicio()) || fechaInicio.isAfter(reserva.getFechaFin()))) {
                return true;
            }
        }
        return false; // No está ocupada
    }

    public void eliminarReserva(Reserva reserva) {
        reservas.remove(reserva);
        System.out.println("La reserva ha sido eliminada de la habitación: " + tipo);
    }

    public void agregarReserva(Reserva reserva) {
        this.reservas.add(reserva);
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public void mostrarDetalles() {
        System.out.println("Tipo: " + tipo + " | Precio: " + precio + " | Características: " + caracteristicas + " | Capacidad: " + capacidad + " | Cantidad: " + cantidad);
    }

    public int getDisponibilidad(LocalDate fechaInicio, LocalDate fechaFin) {
        int cantidadDisponible = cantidad;

        for (Reserva reserva : reservas) {
            if (!(fechaFin.isBefore(reserva.getFechaInicio()) || fechaInicio.isAfter(reserva.getFechaFin()))) {
                // Si las fechas se solapan, restar las habitaciones reservadas
                Map<String, Integer> habitacionesReservadas = reserva.getHabitacionesPorTipo();
                int cantidadReservada = habitacionesReservadas.getOrDefault(tipo, 0);
                cantidadDisponible -= cantidadReservada;
            }
        }

        return Math.max(cantidadDisponible, 0); // Asegurarse de que no retorne valores negativos
    }


    // Métodos Getters y Setters


    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    @Override
    public String toString() {
        return "Tipo: " + tipo + " | Precio: " + precio + " | Características: " + caracteristicas + " | Capacidad: " + capacidad + " | Cantidad: " + cantidad;
    }


}