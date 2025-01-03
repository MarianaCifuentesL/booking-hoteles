package src.model;

import java.time.LocalDate;
import java.util.List;

public class Hotel extends Alojamiento {
    private HabitacionComposite habitaciones;

    public Hotel(String nombre, String ciudad, int calificacion, double precioPorNoche) {
        super(nombre, ciudad, calificacion, precioPorNoche);
        this.habitaciones = new HabitacionComposite();
    }

    // Método para agregar una habitación
    public void agregarHabitacion(String tipo, Double precio, String caracteristicas, Integer capacidad, Integer cantidad) {
        Habitacion nuevaHabitacion = new Habitacion(tipo, precio, caracteristicas, capacidad, cantidad);
        habitaciones.agregarHabitacion(nuevaHabitacion);  // Se agrega a la colección composite
    }

    // Obtener las habitaciones del hotel
    public List<Habitacion> getHabitaciones() {
        return (List<Habitacion>) (List<?>) habitaciones.getHabitaciones();  // Convertir el composite a una lista de Habitaciones
    }

    // Verificar disponibilidad en el hotel
    @Override
    public boolean estaDisponible(LocalDate fechaInicio, LocalDate fechaFin) {
        return habitaciones.getDisponibilidad(fechaInicio, fechaFin) > 0;
    }

    // Mostrar detalles del hotel
//    @Override
//    public void mostrarDetalles() {
//        super.mostrarDetalles();
//        habitaciones.mostrarDetalles();
//    }
}
