package model;

import java.util.List;

public class Hotel extends Alojamiento {
    private List<Habitacion> habitaciones;

    public Hotel(String nombre, String ciudad, int calificacion, double precioBasePorNoche, List<Habitacion> habitaciones) {
        super(nombre, "Hotel", ciudad, calificacion, precioBasePorNoche); // Llama al constructor de Alojamiento
        this.habitaciones = habitaciones;
    }

    // Getters y Setters
    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(List<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }

    @Override
    public String toString() {
        return super.toString() + " | Número de habitaciones: " + habitaciones.size();
    }
}

