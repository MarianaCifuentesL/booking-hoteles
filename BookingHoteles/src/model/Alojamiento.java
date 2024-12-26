package model;

public class Alojamiento {
    private String nombre;
    private String tipo; // Ejemplo: "Hotel", "Apartamento", "Finca", "Día de Sol"
    private int calificacion; // Del 1 al 5
    private double precioBasePorNoche;
    private String ciudad;

    public Alojamiento(String nombre, String tipo, String ciudad, int calificacion, double precioBasePorNoche) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.calificacion = calificacion;
        this.precioBasePorNoche = precioBasePorNoche;
        this.ciudad = ciudad;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public double getPrecioBasePorNoche() {
        return precioBasePorNoche;
    }

    public void setPrecioBasePorNoche(double precioBasePorNoche) {
        this.precioBasePorNoche = precioBasePorNoche;
    }

    @Override
    public String toString() {
        return "Alojamiento: " + nombre + " | Tipo: " + tipo + " | Calificación: " + calificacion + " estrellas";
    }
}
