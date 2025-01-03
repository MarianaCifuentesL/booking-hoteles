package src.model;

import java.time.LocalDate;

public class Apartamento extends Alojamiento {
    public Apartamento(String nombre, String ciudad, int calificacion, double precioPorNoche) {
        super(nombre, ciudad, calificacion, precioPorNoche);
    }

    @Override
    public boolean estaDisponible(LocalDate fechaInicio, LocalDate fechaFin) {
        // Implementar lógica específica para apartamentos
        return true;
    }
}
