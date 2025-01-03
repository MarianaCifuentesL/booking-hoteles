package src.model;

import java.time.LocalDate;

public interface HabitacionComponent {
    void mostrarDetalles();
    int getDisponibilidad(LocalDate fechaInicio, LocalDate fechaFin);
    int getCapacidad();
}





