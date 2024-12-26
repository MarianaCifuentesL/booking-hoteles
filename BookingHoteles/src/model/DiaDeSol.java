package model;

import java.util.List;

public class DiaDeSol extends Alojamiento {
    private List<String> actividades; // Lista de actividades disponibles
    private boolean incluyeAlmuerzo;
    private boolean incluyeRefrigerio;
    private double precioDia;

    public DiaDeSol(String nombre, String ciudad, int calificacion, double precioDia, 
                    List<String> actividades, boolean incluyeAlmuerzo, boolean incluyeRefrigerio) {
        super(nombre, ciudad, "Día de Sol", calificacion);
        this.actividades = actividades;
        this.incluyeAlmuerzo = incluyeAlmuerzo;
        this.incluyeRefrigerio = incluyeRefrigerio;
        this.precioDia = precioDia;
    }

    // Getters y Setters
    
    
    public List<String> getActividades() {
        return actividades;
    }

    public double getPrecioDia() {
		return precioDia;
	}

	public void setPrecioDia(double precioDia) {
		this.precioDia = precioDia;
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

    @Override
    public String toString() {
        return super.toString() + 
               "\nActividades: " + String.join(", ", actividades) +
               "\nIncluye Almuerzo: " + (incluyeAlmuerzo ? "Sí" : "No") +
               "\nIncluye Refrigerio: " + (incluyeRefrigerio ? "Sí" : "No") +
               "\nPrecio Base: " + getPrecioPorNoche();
    }
}
