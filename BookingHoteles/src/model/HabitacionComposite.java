package src.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HabitacionComposite implements HabitacionComponent {
    private List<HabitacionComponent> habitaciones = new ArrayList<>();

    // Agregar una habitación al composite
    public void agregarHabitacion(HabitacionComponent habitacion) {
        habitaciones.add(habitacion);
    }

    // Mostrar los detalles de todas las habitaciones contenidas
    @Override
    public void mostrarDetalles() {
        for (HabitacionComponent habitacion : habitaciones) {
            habitacion.mostrarDetalles();
        }
    }

    // Verificar la disponibilidad para todas las habitaciones contenidas
    @Override
    public int getDisponibilidad(LocalDate fechaInicio, LocalDate fechaFin) {
        int disponibilidadTotal = 0;
        for (HabitacionComponent habitacion : habitaciones) {
            disponibilidadTotal += habitacion.getDisponibilidad(fechaInicio, fechaFin);
        }
        return disponibilidadTotal;
    }

    @Override
    public int getCapacidad() {
        return habitaciones.stream()
                .mapToInt(HabitacionComponent::getCapacidad)  // Llamar al método getCapacidad() de cada componente
                .sum();  // Sumar las capacidades de todas las habitaciones en el composite
    }

    // Método para obtener las habitaciones (esto es útil para la lógica de negocio)
    public List<HabitacionComponent> getHabitaciones() {
        return habitaciones;
    }

    // Método para obtener la cantidad de habitaciones en el composite
    public int getCantidad() {
        return habitaciones.size();
    }
}
