package model;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class Alojamiento {
    private String nombre;
    private String ciudad;
    private String tipo; // Hotel, Apartamento, Finca, Día de Sol
    private int calificacion; // Del 1 al 5
    private double precioPorNoche;
    private List<Habitacion> habitaciones;

    public Alojamiento(String nombre, String ciudad, String tipo, int calificacion, double precioPorNoche) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.tipo = tipo;
        this.calificacion = calificacion;
        this.precioPorNoche = precioPorNoche;
        this.habitaciones = new ArrayList<>();
    }
    

    public Alojamiento(String nombre, String ciudad, String tipo, int calificacion) {
		super();
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.tipo = tipo;
		this.calificacion = calificacion;
	}



	// Métodos getters y setters (añadido `ciudad`)
    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    // Métodos para agregar y mostrar habitaciones
    public void agregarHabitacion(Habitacion habitacion) {
        this.habitaciones.add(habitacion);
    }

    public void mostrarInformacion() {
        System.out.println("Nombre: " + nombre + " | Tipo: " + tipo + " | Calificación: " + calificacion);
        System.out.println("Precio por noche: " + precioPorNoche);
        System.out.println("Habitaciones disponibles:");
        for (Habitacion h : habitaciones) {
            h.mostrarDetalles();
        }
    }

    // Métodos Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public double getPrecioPorNoche() {
        return precioPorNoche;
    }

    public void setPrecioPorNoche(double precioPorNoche) {
        this.precioPorNoche = precioPorNoche;
    }

    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(List<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }
}
