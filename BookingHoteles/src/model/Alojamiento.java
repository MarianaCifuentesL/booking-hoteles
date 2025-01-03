package src.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Alojamiento {
    protected String nombre;
    protected String ciudad;
    protected int calificacion; // Del 1 al 5
    protected double precioPorNoche;
    protected List<Reserva> reservas;

    public Alojamiento(String nombre, String ciudad, int calificacion, double precioPorNoche) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.calificacion = calificacion;
        this.precioPorNoche = precioPorNoche;
        this.reservas = new ArrayList<>();
    }

    public abstract boolean estaDisponible(LocalDate fechaInicio, LocalDate fechaFin);

    public void agregarReserva(Reserva reserva) {
        this.reservas.add(reserva);
    }

    // Métodos Getters y Setters comunes
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

    public List<Reserva> getReservas() {
        return reservas;
    }
}
