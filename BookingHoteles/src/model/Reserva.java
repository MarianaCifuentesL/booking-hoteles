package model;

import java.time.LocalDate;

public class Reserva {
    private String nombreCliente;
    private String apellidoCliente;
    private String email;
    private String nacionalidad;
    private String telefono;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String tipoHabitacion;
    private int cantidadHabitaciones;
    private String horaLlegada; // Nueva propiedad
    private LocalDate fechaNacimiento; // Nueva propiedad
    private Alojamiento alojamiento; // Nueva propiedad que hace referencia al alojamiento

    public Reserva(String nombreCliente, String apellidoCliente, LocalDate fechaNacimiento, String email, String nacionalidad, String telefono,
    				Alojamiento alojamiento,LocalDate fechaInicio, LocalDate fechaFin, String tipoHabitacion, int cantidadHabitaciones, 
                   String horaLlegada) {
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.fechaNacimiento = fechaNacimiento; // Inicializar
        this.email = email;
        this.nacionalidad = nacionalidad;
        this.telefono = telefono;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipoHabitacion = tipoHabitacion;
        this.cantidadHabitaciones = cantidadHabitaciones;
        this.horaLlegada = horaLlegada;
        this.alojamiento = alojamiento; // Asignar el alojamiento
        
    }
    
    // Getter y Setter para alojamiento
    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    // Getter y Setter para fechaNacimiento
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }


    // Getter y Setter para horaLlegada
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

	public String getTipoHabitacion() {
		return tipoHabitacion;
	}

	public void setTipoHabitacion(String tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}

	public int getCantidadHabitaciones() {
		return cantidadHabitaciones;
	}

	public void setCantidadHabitaciones(int cantidadHabitaciones) {
		this.cantidadHabitaciones = cantidadHabitaciones;
	}

    // Getters y Setters

}
