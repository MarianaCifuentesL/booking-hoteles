package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Reserva {
    private String nombreCliente;
    private String apellidoCliente;
    private String email;
    private String nacionalidad;
    private String telefono;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String horaLlegada; // Propiedad existente
    private LocalDate fechaNacimiento; // Propiedad existente
    private Alojamiento alojamiento; // Propiedad existente
    private Map<String, Integer> habitacionesPorTipo; // Tipos y cantidades de habitaciones
    private List<String> idsHabitacionesReservadas; // Lista de IDs únicos de habitaciones reservadas

    public Reserva(String nombreCliente, String apellidoCliente, LocalDate fechaNacimiento, String email,
                   String nacionalidad, String telefono, Alojamiento alojamiento, LocalDate fechaInicio,
                   LocalDate fechaFin, Map<String, Integer> habitacionesPorTipo, String horaLlegada) {
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
        this.nacionalidad = nacionalidad;
        this.telefono = telefono;
        this.alojamiento = alojamiento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.habitacionesPorTipo = habitacionesPorTipo;
        this.horaLlegada = horaLlegada;
        this.idsHabitacionesReservadas = new ArrayList<>(); // Inicializa la lista de IDs
    }
    
    

    public Reserva(String nombreCliente, String apellidoCliente, LocalDate fechaNacimiento, String email, String nacionalidad, String telefono,
    		Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, String horaLlegada 
			) {
		super();
		this.nombreCliente = nombreCliente;
		this.apellidoCliente = apellidoCliente;
		this.email = email;
		this.nacionalidad = nacionalidad;
		this.telefono = telefono;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.horaLlegada = horaLlegada;
		this.fechaNacimiento = fechaNacimiento;
		this.alojamiento = alojamiento;
	}



	// Métodos para gestionar las habitaciones reservadas

    public void agregarHabitacionReservada(String idHabitacion) {
        if (!idsHabitacionesReservadas.contains(idHabitacion)) {
            idsHabitacionesReservadas.add(idHabitacion);
            System.out.println("Habitación con ID " + idHabitacion + " añadida a la reserva.");
        }
    }

    public void eliminarHabitacionReservada(String idHabitacion) {
        if (idsHabitacionesReservadas.remove(idHabitacion)) {
            System.out.println("Habitación con ID " + idHabitacion + " eliminada de la reserva.");
        }
    }

    public List<String> getIdsHabitacionesReservadas() {
        return idsHabitacionesReservadas;
    }

    public void setIdsHabitacionesReservadas(List<String> idsHabitacionesReservadas) {
        this.idsHabitacionesReservadas = idsHabitacionesReservadas;
    }

    // Otros Getters y Setters existentes

    public Map<String, Integer> getHabitacionesPorTipo() {
        return habitacionesPorTipo;
    }

    public void setHabitacionesPorTipo(Map<String, Integer> habitacionesPorTipo) {
        this.habitacionesPorTipo = habitacionesPorTipo;
    }

    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(String horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
