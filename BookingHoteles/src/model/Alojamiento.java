package src.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Alojamiento {
    private String nombre;
    private String ciudad;
    private String tipo; // Hotel, Apartamento, Finca, Día de Sol
    private Integer calificacion; // Del 1 al 5
    private Double precioPorNoche;
    private List<Habitacion> habitaciones;
    private List<Reserva> reservas;

    private List<String> actividades; // Lista de actividades disponibles
    private Boolean incluyeAlmuerzo;
    private Boolean incluyeRefrigerio;


    public Alojamiento(String nombre, String ciudad, String tipo, int calificacion, double precioPorNoche) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.tipo = tipo;
        this.calificacion = calificacion;
        this.precioPorNoche = precioPorNoche;
        this.habitaciones = new ArrayList<>();
        this.reservas = new ArrayList<>();
    }

    public List<String> getActividades() {
        return actividades;
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

    public void setActividades(List<String> actividades) {
        this.actividades = actividades;
    }

    public Boolean getIncluyeAlmuerzo() {
        return incluyeAlmuerzo;
    }

    public void setIncluyeAlmuerzo(Boolean incluyeAlmuerzo) {
        this.incluyeAlmuerzo = incluyeAlmuerzo;
    }

    public Boolean getIncluyeRefrigerio() {
        return incluyeRefrigerio;
    }

    public void setIncluyeRefrigerio(Boolean incluyeRefrigerio) {
        this.incluyeRefrigerio = incluyeRefrigerio;
    }

    public Alojamiento(String nombre, String ciudad, String tipo, int calificacion) {
		super();
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.tipo = tipo;
		this.calificacion = calificacion;
	}
    
    
    
    public Alojamiento(String nombre, String ciudad, String tipo, int calificacion, List<Reserva> reservas) {
		super();
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.tipo = tipo;
		this.calificacion = calificacion;
		this.reservas = reservas;
	}

//    Constructor Día de Sol
    public Alojamiento(String nombre, String ciudad, String tipo, Integer calificacion, Double precioDia,
                    List<String> actividades, Boolean incluyeAlmuerzo, Boolean incluyeRefrigerio) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.tipo = tipo;
        this.calificacion = calificacion;
        this.precioPorNoche = precioDia;
        this.actividades = actividades;
        this.incluyeAlmuerzo = incluyeAlmuerzo;
        this.incluyeRefrigerio = incluyeRefrigerio;
    }

    public void agregarHabitacion(String tipo, Double precio, String caracteristicas, Integer capacidad, Integer cantidad) {

        Habitacion nuevaHabitacion = new Habitacion(tipo, precio, caracteristicas, capacidad, cantidad);
        habitaciones.add(nuevaHabitacion);

    }



	// Métodos getters y setters (añadido ciudad)
    
    public void agregarReserva(Reserva reserva) {
        this.reservas.add(reserva);
    }

    public List<Reserva> getReservas() {
        return reservas;
    }
    
    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }


    public void mostrarInformacion() {
        System.out.println("Nombre: " + nombre + " | Tipo: " + tipo + " | Calificación: " + calificacion);
        System.out.println("Precio por noche: " + precioPorNoche);
        System.out.println("Habitaciones disponibles:");
        for (Habitacion h : habitaciones) {
            h.mostrarDetalles();
        }
    }
    
    // Método general para verificar si el alojamiento está disponible
    public boolean estaDisponible(LocalDate fechaInicio, LocalDate fechaFin) {
        // Si el alojamiento tiene habitaciones, se verifican las habitaciones
        if (habitaciones != null && !habitaciones.isEmpty()) {
            return habitaciones.stream()
                .allMatch(habitacion -> habitacion.estaDisponible(fechaInicio, fechaFin));
        }
        
        // Si no tiene habitaciones (es una finca o un día de sol), se verifica que no haya reservas en esas fechas
        return true; // Para fincas y días de sol siempre está disponible, a menos que agregues lógica específica
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