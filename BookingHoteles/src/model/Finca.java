package src.model;

import java.time.LocalDate;

public class Finca extends Alojamiento {
    public Finca(String nombre, String ciudad, int calificacion, double precioPorNoche) {
        super(nombre, ciudad, calificacion, precioPorNoche);
    }

    @Override
    public boolean estaDisponible(LocalDate fechaInicio, LocalDate fechaFin) {
        // Implementar lógica específica para fincas
        return true;
    }
}
