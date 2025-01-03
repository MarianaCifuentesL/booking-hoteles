package src.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlojamientoComposite implements AlojamientoComponent {

    private List<AlojamientoComponent> alojamientos;

    public AlojamientoComposite() {
        this.alojamientos = new ArrayList<>();
    }

    @Override
    public boolean estaDisponible(LocalDate fechaInicio, LocalDate fechaFin) {
        // Verifica disponibilidad en todos los alojamientos
        return alojamientos.stream().allMatch(a -> a.estaDisponible(fechaInicio, fechaFin));
    }


    // Método para obtener los alojamientos del composite
    public List<AlojamientoComponent> getAlojamientos() {
        return alojamientos;
    }
}
