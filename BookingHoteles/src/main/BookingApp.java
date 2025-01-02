package src.main;

import java.util.*;

import src.controller.*;
import src.model.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class BookingApp {
	public static final String HABITACIONES_RESERVADAS = "Habitaciones reservadas:";
	public static final String AUMENTO_APLICADO = "Aumento aplicado: ";
	public static final String DESCUENTO_APLICADO = "Descuento aplicado: ";
	public static final String PRECIO_TOTAL_CON_AJUSTES = "Precio total (con ajustes): ";
	public static final String ESTRELLAS = " estrellas";
	private List<Alojamiento> alojamientos;
	private List<Reserva> reservas;
	private Cliente cliente;
	private DiaDeSolController diaDeSolController;
	private HotelController hotelController;
	private ApartamentoFincaController fincaApartamentoController;
	private AlojamientoController alojamientoController;
	private HabitacionController habitacionController;

	public BookingApp() {
		this.reservas = new ArrayList<>();
		this.alojamientos = new ArrayList<>();

		this.habitacionController = new HabitacionController(this.alojamientos);
		this.hotelController = new HotelController(this.alojamientos, this.habitacionController, this.reservas);
		this.alojamientoController = new AlojamientoController(this.alojamientos, this.hotelController, this.reservas);
		this.diaDeSolController = new DiaDeSolController(this.alojamientos, this.reservas);
		this.fincaApartamentoController = new ApartamentoFincaController(this.alojamientos, this.reservas);

	}

	public void agregarAlojamiento(Alojamiento alojamiento) {
		this.alojamientos.add(alojamiento);
	}

	public void buscarAlojamientos(String ciudad, String tipo, LocalDate fechaInicio, LocalDate fechaFin, int cantidadAdultos, int cantidadNiños, int cantidadHabitaciones) {
		alojamientoController.buscarAlojamientos(ciudad, tipo, fechaInicio, fechaFin, cantidadAdultos, cantidadNiños, cantidadHabitaciones);
	}

	private void mostrarInfoAlojamiento(Alojamiento alojamiento, int totalPersonas) {
		System.out.println("Nombre: " + alojamiento.getNombre());
		System.out.println("Calificación: " + alojamiento.getCalificacion() + " estrellas");
		System.out.println("Precio por noche: " + alojamiento.getPrecioPorNoche());
		System.out.println("Costo total para " + totalPersonas + " personas: " + (alojamiento.getPrecioPorNoche() * totalPersonas));
	}

	public void confirmarHabitaciones(String nombreHotel, LocalDate fechaInicio, LocalDate fechaFin, int cantidadAdultos, int cantidadNiños, int cantidadHabitaciones) {
		habitacionController.confirmarHabitaciones(nombreHotel, fechaInicio, fechaFin, cantidadAdultos, cantidadNiños, cantidadHabitaciones);
	}

	public static String eliminarTildes(String texto) {
		String normalized = Normalizer.normalize(texto, Normalizer.Form.NFD);
		return normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	}

	// Verificar si una habitación está reservada en las fechas indicadas
	private boolean estaReservada(Habitacion habitacion, LocalDate fechaInicio, LocalDate fechaFin) {
		// Iterar sobre las reservas para verificar conflictos de fechas
		for (Reserva reserva : habitacion.getReservas()) {
			if (!(fechaFin.isBefore(reserva.getFechaInicio()) || fechaInicio.isAfter(reserva.getFechaFin()))) {
				// Hay un conflicto de fechas con esta reserva
				return true;
			}
		}
		return false; // No hay conflictos
	}

	private static boolean validarAlojamiento(Alojamiento alojamiento) {
		if (alojamiento == null) {
			System.out.println("El alojamiento proporcionado no es válido.");
			return true;
		}
		return false;
	}

	public List<Alojamiento> buscarAlojamientosPorCiudadYTipo(String ciudad, String tipo) {
		return alojamientoController.buscarAlojamientosPorCiudadYTipo(ciudad, tipo);
	}

	public boolean confirmarDisponibilidad(String nombreAlojamiento, LocalDate fechaInicio, LocalDate fechaFin, Map<String, Integer> habitacionesPorTipo) {
		// Buscar el alojamiento por nombre
		Alojamiento alojamiento = alojamientoController.buscarAlojamientoPorNombre(nombreAlojamiento);
		if (alojamiento == null) {
			System.out.println("No se encontró el alojamiento indicado.");
			return false;
		}

		// Verificar disponibilidad del alojamiento para el rango de fechas
		if (!alojamiento.estaDisponible(fechaInicio, fechaFin)) {
			System.out.println("El alojamiento no está disponible para las fechas indicadas.");
			return false;
		}

		// Verificar disponibilidad de habitaciones por tipo
		return habitacionController.verificarDisponibilidadHabitaciones(alojamiento, fechaInicio, fechaFin, habitacionesPorTipo);
	}


	// Método principal para verificar disponibilidad de habitaciones
	public boolean verificarDisponibilidad(String tipoHabitacion, LocalDate fechaInicio, LocalDate fechaFin, int cantidadSolicitada) {
		for (Alojamiento alojamiento : alojamientos) {
			int habitacionesDisponibles = contarHabitacionesDisponibles(alojamiento, tipoHabitacion, fechaInicio, fechaFin);
			if (habitacionesDisponibles >= cantidadSolicitada) {
				return true; // Hay suficientes habitaciones disponibles
			}
		}
		return false; // No hay suficientes habitaciones disponibles
	}

	// Método auxiliar para contar las habitaciones disponibles en un alojamiento
	private int contarHabitacionesDisponibles(Alojamiento alojamiento, String tipoHabitacion, LocalDate fechaInicio, LocalDate fechaFin) {
		return (int) alojamiento.getHabitaciones().stream()
				.filter(h -> h.getTipo().equalsIgnoreCase(tipoHabitacion) && h.estaDisponible(fechaInicio, fechaFin))
				.count();
	}


	public void actualizarReserva(Scanner scanner) {
		hotelController.actualizarReserva(scanner);
	}

	public void mostrarTodasLasReservas() {
		if (reservas.isEmpty()) {
			mostrarMensajeSinReservas();
			return;
		}
		mostrarEncabezadoReservas();
		for (Reserva reserva : reservas) {
			if (!validarReserva(reserva)) continue;
			mostrarDetalleCompletoReserva(reserva);
		}
	}

	private void mostrarMensajeSinReservas() {
		System.out.println("No hay reservas realizadas.");
	}

	private void mostrarEncabezadoReservas() {
		System.out.println("Todas las reservas realizadas:");
	}

	private boolean validarReserva(Reserva reserva) {
		Cliente cliente = reserva.getCliente();
		Alojamiento alojamiento = reserva.getAlojamiento();
		if (cliente == null || alojamiento == null) {
			System.out.println("Error: La reserva no tiene cliente o alojamiento asociado.");
			return false;
		}
		return true;
	}

	private void mostrarDetalleCompletoReserva(Reserva reserva) {
		Cliente cliente = reserva.getCliente();
		Alojamiento alojamiento = reserva.getAlojamiento();
		System.out.println("----------------------------------------------------");
		mostrarDetalleCliente(cliente);
		mostrarDetalleAlojamiento(alojamiento);
		mostrarDetallesReserva(reserva, alojamiento);
		System.out.println("----------------------------------------------------");
	}

	private void mostrarDetalleCliente(Cliente cliente) {
		System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
		System.out.println("Email: " + cliente.getEmail());
	}

	private void mostrarDetalleAlojamiento(Alojamiento alojamiento) {
		System.out.println("Alojamiento: " + alojamiento.getNombre());
		System.out.println("Tipo: " + alojamiento.getTipo());
	}


	private void mostrarDetallesReserva(Reserva reserva, Alojamiento alojamiento) {
		switch (alojamiento.getTipo().toLowerCase()) {
			case "finca", "apartamento" -> mostrarDetallesBasicos(reserva);
			case "hotel" -> mostrarDetallesHotel(reserva);
			case "día de sol" -> mostrarDetallesDiaDeSol(reserva);
			default -> System.out.println("Tipo de alojamiento no reconocido.");
		}
	}

	private void mostrarDetallesBasicos(Reserva reserva) {
		System.out.println("Fechas: " + reserva.getFechaInicio() + " a " + reserva.getFechaFin());
		System.out.println("Cantidad de adultos: " + reserva.getCantidadAdultos());
		System.out.println("Cantidad de niños: " + reserva.getCantidadNiños());
	}

	private void mostrarDetallesHotel(Reserva reserva) {
		mostrarDetallesBasicos(reserva);
		System.out.println(HABITACIONES_RESERVADAS);
		for (Map.Entry<String, Integer> entry : reserva.getHabitacionesPorTipo().entrySet()) {
			System.out.println("- Tipo: " + entry.getKey() + " | Cantidad: " + entry.getValue());
		}
	}

	private void mostrarDetallesDiaDeSol(Reserva reserva) {
		System.out.println("Fecha: " + reserva.getFechaInicio());
		System.out.println("Cantidad de adultos: " + reserva.getCantidadAdultos());
		System.out.println("Cantidad de niños: " + reserva.getCantidadNiños());
	}


	public void reservarHotel(Cliente cliente, Alojamiento alojamiento, Integer cantidadAdultos, Integer cantidadNiños,
							  LocalDate fechaInicio, LocalDate fechaFin, Map<String, Integer> habitacionesPorTipo, String horaLlegada) {
		hotelController.reservarHotel(cliente, alojamiento, cantidadAdultos, cantidadNiños, fechaInicio, fechaFin, habitacionesPorTipo, horaLlegada);
	}

	public void reservarFincaApartamento(Cliente cliente, Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, Integer cantidadAdultos, Integer cantidadNiños, String horaLlegada) {

		fincaApartamentoController.reservarFincaApartamento(cliente, alojamiento, fechaInicio, fechaFin, cantidadAdultos, cantidadNiños, horaLlegada);
	}

	public Boolean validarFincaApartamento(LocalDate fechaInicio, LocalDate fechaFin, Alojamiento alojamiento)
	{
		return fincaApartamentoController.validarFincaApartamento(fechaInicio, fechaFin, alojamiento);
	}

	public void reservarDiaDeSol(Cliente cliente, Alojamiento alojamiento, LocalDate fechaInicio, Integer cantidadAdultos, Integer cantidadNiños) {
		diaDeSolController.reservarDiaDeSol(cliente, alojamiento, fechaInicio, cantidadAdultos, cantidadNiños);
	}
}

