package src.model;

import java.time.LocalDate;
import java.util.List;

public class DiaDeSol extends Alojamiento {
    private List<String> actividades;
    private boolean incluyeAlmuerzo;
    private boolean incluyeRefrigerio;

    public DiaDeSol(String nombre, String ciudad, int calificacion, double precioPorDia, List<String> actividades,
                    boolean incluyeAlmuerzo, boolean incluyeRefrigerio) {
        super(nombre, ciudad, calificacion, precioPorDia);
        this.actividades = actividades;
        this.incluyeAlmuerzo = incluyeAlmuerzo;
        this.incluyeRefrigerio = incluyeRefrigerio;
    }

    @Override
    public boolean estaDisponible(LocalDate fechaInicio, LocalDate fechaFin) {
        // Implementar lógica de disponibilidad si es necesario
        return true;
    }

    // Getters y Setters específicos


    public List<String> getActividades() {
        return actividades;
    }

    public void setActividades(List<String> actividades) {
        this.actividades = actividades;
    }

    public boolean isIncluyeAlmuerzo() {
        return incluyeAlmuerzo;
    }

    public void setIncluyeAlmuerzo(boolean incluyeAlmuerzo) {
        this.incluyeAlmuerzo = incluyeAlmuerzo;
    }

    public boolean isIncluyeRefrigerio() {
        return incluyeRefrigerio;
    }

    public void setIncluyeRefrigerio(boolean incluyeRefrigerio) {
        this.incluyeRefrigerio = incluyeRefrigerio;
    }
}
