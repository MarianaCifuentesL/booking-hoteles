package src.model;

import java.time.LocalDate;
import java.util.List;

public interface AlojamientoComponent {
//    void mostrarInfo();
    boolean estaDisponible(LocalDate fechaInicio, LocalDate fechaFin);
}
