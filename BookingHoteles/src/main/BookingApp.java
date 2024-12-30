package src.main;

import java.util.*;

import src.model.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

	public BookingApp() {
		this.alojamientos = new ArrayList<>();
		this.reservas = new ArrayList<>();  // Asegúrate de inicializar esta lista
	}

	public void agregarAlojamiento(Alojamiento alojamiento) {
		this.alojamientos.add(alojamiento);
	}

	public void buscarAlojamientos(String ciudad, String tipo, LocalDate fechaInicio, LocalDate fechaFin, int cantidadAdultos, int cantidadNiños, int cantidadHabitaciones) {
		System.out.println("\n--- Resultados de búsqueda para: " + tipo + " en " + ciudad + " ---");
		boolean encontrado = false;

		switch (tipo.toLowerCase()) {
			case "día de sol" -> encontrado = buscarDiaDeSol(ciudad, cantidadAdultos, cantidadNiños);
			case "apartamento", "finca" -> encontrado = buscarApartamentoOFinca(ciudad, tipo, fechaInicio, fechaFin);
			case "hotel" -> encontrado = buscarHotel(ciudad, fechaInicio, fechaFin, cantidadAdultos, cantidadNiños, cantidadHabitaciones);
			default -> System.out.println("Tipo de alojamiento no reconocido.");
		}

		if (!encontrado) {
			System.out.println("No se encontraron alojamientos que cumplan con los criterios.");
		}
	}

	private boolean buscarDiaDeSol(String ciudad, int cantidadAdultos, int cantidadNiños) {
		int totalPersonas = calcularTotalPersonas(cantidadAdultos, cantidadNiños);
		boolean encontrado = false;

		for (Alojamiento alojamiento : alojamientos) {
			if (esDiaDeSolEnCiudad(alojamiento, ciudad)) {
				mostrarInformacionDiaDeSol(alojamiento, totalPersonas);
				encontrado = true;
			}
		}

		return encontrado;
	}

	private int calcularTotalPersonas(int cantidadAdultos, int cantidadNiños) {
		return cantidadAdultos + cantidadNiños;
	}

	private boolean esDiaDeSolEnCiudad(Alojamiento alojamiento, String ciudad) {
		return alojamiento.getCiudad().equalsIgnoreCase(ciudad) && alojamiento.getTipo().equalsIgnoreCase("Día de Sol");
	}

	private void mostrarInformacionDiaDeSol(Alojamiento alojamiento, int totalPersonas) {
		System.out.println("Nombre: " + alojamiento.getNombre());
		System.out.println("Calificación: " + alojamiento.getCalificacion() + ESTRELLAS);
		System.out.println("Actividades: " + String.join(", ", alojamiento.getActividades()));
		System.out.println("Incluye Almuerzo: " + (alojamiento.isIncluyeAlmuerzo() ? "Sí" : "No"));
		System.out.println("Incluye Refrigerio: " + (alojamiento.isIncluyeRefrigerio() ? "Sí" : "No"));
		System.out.println("Precio por persona: " + alojamiento.getPrecioPorNoche());
		System.out.println("Costo total para " + totalPersonas + " personas: " + (alojamiento.getPrecioPorNoche() * totalPersonas));
	}


	private boolean buscarApartamentoOFinca(String ciudad, String tipo, LocalDate fechaInicio, LocalDate fechaFin) {
		long diasEstadia = calcularDiasEstadia(fechaInicio, fechaFin);
		boolean encontrado = false;

		for (Alojamiento alojamiento : alojamientos) {
			if (esAlojamientoDeTipoEnCiudad(alojamiento, ciudad, tipo)) {
				double precioFinal = calcularPrecioConAjustes(alojamiento.getPrecioPorNoche(), diasEstadia, fechaInicio, fechaFin);

				mostrarInformacionAlojamiento(alojamiento, diasEstadia, precioFinal, fechaInicio, fechaFin);

				encontrado = true;
			}
		}

		return encontrado;
	}

	// Método auxiliar para verificar si un alojamiento es del tipo y ciudad especificados
	private boolean esAlojamientoDeTipoEnCiudad(Alojamiento alojamiento, String ciudad, String tipo) {
		return alojamiento.getCiudad().equalsIgnoreCase(ciudad) && alojamiento.getTipo().equalsIgnoreCase(tipo);
	}

	// Método auxiliar para calcular el precio final con ajustes
	private double calcularPrecioConAjustes(double precioPorNoche, long diasEstadia, LocalDate fechaInicio, LocalDate fechaFin) {
		double precioBase = precioPorNoche * diasEstadia;
		return aplicarDescuentosYAumentos(precioBase, fechaInicio, fechaFin);
	}

	// Método auxiliar para mostrar información del alojamiento
	private void mostrarInformacionAlojamiento(Alojamiento alojamiento, long diasEstadia, double precioFinal, LocalDate fechaInicio, LocalDate fechaFin) {
		double precioBase = alojamiento.getPrecioPorNoche() * diasEstadia;
		double ajuste = precioFinal - precioBase;

		System.out.println("Nombre: " + alojamiento.getNombre());
		System.out.println("Calificación: " + alojamiento.getCalificacion());
		System.out.println("Precio por noche: " + alojamiento.getPrecioPorNoche());
		System.out.println("Precio total (sin ajustes): " + precioBase);
		System.out.println((ajuste >= 0 ? AUMENTO_APLICADO : DESCUENTO_APLICADO) + Math.abs(ajuste));
		System.out.println(PRECIO_TOTAL_CON_AJUSTES + precioFinal);
	}

	private boolean buscarHotel(String ciudad, LocalDate fechaInicio, LocalDate fechaFin, int cantidadAdultos, int cantidadNiños, int cantidadHabitaciones) {
		long diasEstadia = calcularDiasEstadia(fechaInicio, fechaFin);
		boolean encontrado = false;

		for (Alojamiento alojamiento : alojamientos) {
			if (esHotelEnCiudad(alojamiento, ciudad)) {
				if (!verificarCapacidad(alojamiento, cantidadAdultos, cantidadNiños, cantidadHabitaciones)) continue;

				double precioBase = calcularPrecioBase(alojamiento, cantidadHabitaciones, diasEstadia);
				double precioFinal = aplicarDescuentosYAumentos(precioBase, fechaInicio, fechaFin);
				double ajuste = precioFinal - precioBase;

				mostrarInformacionHotel(alojamiento, precioBase, precioFinal, ajuste);
				encontrado = true;
			}
		}
		return encontrado;
	}

	private long calcularDiasEstadia(LocalDate fechaInicio, LocalDate fechaFin) {
		return ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
	}

	private boolean esHotelEnCiudad(Alojamiento alojamiento, String ciudad) {
		return alojamiento.getCiudad().equalsIgnoreCase(ciudad) && alojamiento.getTipo().equalsIgnoreCase("Hotel");
	}

	private double calcularPrecioBase(Alojamiento alojamiento, int cantidadHabitaciones, long diasEstadia) {
		return obtenerPrecioHabitacionMasSimple(alojamiento) * cantidadHabitaciones * diasEstadia;
	}

	private void mostrarInformacionHotel(Alojamiento alojamiento, double precioBase, double precioFinal, double ajuste) {
		System.out.println("Nombre: " + alojamiento.getNombre());
		System.out.println("Calificación: " + alojamiento.getCalificacion());
		System.out.println("Precio por noche: " + obtenerPrecioHabitacionMasSimple(alojamiento));
		System.out.println("Precio total (sin ajustes): " + precioBase);
		System.out.println((ajuste >= 0 ? "Aumento aplicado: " : "Descuento aplicado: ") + Math.abs(ajuste));
		System.out.println("Precio total (con ajustes): " + precioFinal);
	}

	// Métodos auxiliares para mostrar información específica
	private void mostrarInfoDiaDeSol(Alojamiento alojamiento, int totalPersonas) {
		System.out.println("Nombre: " + alojamiento.getNombre());
		System.out.println("Calificación: " + alojamiento.getCalificacion() + " estrellas");
		System.out.println("Actividades: " + String.join(", ", alojamiento.getActividades()));
		System.out.println("Incluye Almuerzo: " + (alojamiento.getIncluyeAlmuerzo() ? "Sí" : "No"));
		System.out.println("Incluye Refrigerio: " + (alojamiento.getIncluyeRefrigerio() ? "Sí" : "No"));
		System.out.println("Precio por persona: " + alojamiento.getPrecioPorNoche());
		System.out.println("Costo total para " + totalPersonas + " personas: " + (alojamiento.getPrecioPorNoche() * totalPersonas));
	}

	private void mostrarInfoAlojamiento(Alojamiento alojamiento, int totalPersonas) {
		System.out.println("Nombre: " + alojamiento.getNombre());
		System.out.println("Calificación: " + alojamiento.getCalificacion() + " estrellas");
		System.out.println("Precio por noche: " + alojamiento.getPrecioPorNoche());
		System.out.println("Costo total para " + totalPersonas + " personas: " + (alojamiento.getPrecioPorNoche() * totalPersonas));
	}

	private void mostrarInfoHotel(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin,
								  int cantidadAdultos, int cantidadNiños, int cantidadHabitaciones) {

		long diasEstadia = calcularDiasEstadia(fechaInicio, fechaFin);
		if (!tieneCapacidad(alojamiento, cantidadAdultos, cantidadNiños, cantidadHabitaciones)) {
			mostrarMensajeCapacidadInsuficiente(alojamiento);
			return;
		}
		double precioBase = calcularPrecioBase(alojamiento, cantidadHabitaciones, diasEstadia);
		double precioFinal = calcularPrecioFinal(precioBase, fechaInicio, fechaFin);
		double ajuste = precioFinal - precioBase;

		mostrarDetallesAlojamiento(alojamiento, precioBase, ajuste, precioFinal);
	}

	private boolean tieneCapacidad(Alojamiento alojamiento, int cantidadAdultos, int cantidadNiños, int cantidadHabitaciones) {
		return verificarCapacidad(alojamiento, cantidadAdultos, cantidadNiños, cantidadHabitaciones);
	}

	private void mostrarMensajeCapacidadInsuficiente(Alojamiento alojamiento) {
		System.out.println("El alojamiento " + alojamiento.getNombre() + " no tiene capacidad suficiente.");
	}

	private double calcularPrecioFinal(double precioBase, LocalDate fechaInicio, LocalDate fechaFin) {
		return aplicarDescuentosYAumentos(precioBase, fechaInicio, fechaFin);
	}

	private void mostrarDetallesAlojamiento(Alojamiento alojamiento, double precioBase, double ajuste, double precioFinal) {
		System.out.println("Nombre: " + alojamiento.getNombre());
		System.out.println("Calificación: " + alojamiento.getCalificacion());
		System.out.println("Precio por noche: " + obtenerPrecioHabitacionMasSimple(alojamiento));
		System.out.println("Precio total (sin ajustes): " + precioBase);
		System.out.println((ajuste >= 0 ? "Aumento aplicado: " : "Descuento aplicado: ") + Math.abs(ajuste));
		System.out.println("Precio total (con ajustes): " + precioFinal);
	}

	private boolean verificarCapacidad(Alojamiento alojamiento, int cantidadAdultos, int cantidadNiños, int cantidadHabitaciones) {
		int totalPersonas = cantidadAdultos + cantidadNiños;

		// Obtener todas las habitaciones disponibles
		List<Habitacion> habitaciones = alojamiento.getHabitaciones();

		// Validar que hay suficientes habitaciones para la cantidad solicitada
		if (habitaciones.size() < cantidadHabitaciones) {
			return false; // No hay suficientes habitaciones disponibles
		}

		// Generar todas las combinaciones posibles de habitaciones de tamaño "cantidadHabitaciones"
		List<List<Habitacion>> combinaciones = generarCombinaciones(habitaciones, cantidadHabitaciones);

		// Verificar si alguna combinación cumple con la capacidad requerida
		for (List<Habitacion> combinacion : combinaciones) {
			int capacidadTotal = combinacion.stream().mapToInt(Habitacion::getCapacidad).sum();
			if (capacidadTotal >= totalPersonas) {
				return true; // Hay una combinación válida
			}
		}

		return false; // No se encontró una combinación que cumpla
	}

	private List<List<Habitacion>> generarCombinaciones(List<Habitacion> habitaciones, int cantidad) {
		List<List<Habitacion>> resultado = new ArrayList<>();
		generarCombinacionesRecursivo(habitaciones, new ArrayList<>(), 0, cantidad, resultado);
		return resultado;
	}

	private void generarCombinacionesRecursivo(List<Habitacion> habitaciones, List<Habitacion> combinacionActual, 
			int indice, int cantidad, List<List<Habitacion>> resultado) {
		if (combinacionActual.size() == cantidad) {
			resultado.add(new ArrayList<>(combinacionActual)); // Agregar combinación válida
			return;
		}

		for (int i = indice; i < habitaciones.size(); i++) {
			combinacionActual.add(habitaciones.get(i));
			generarCombinacionesRecursivo(habitaciones, combinacionActual, i + 1, cantidad, resultado);
			combinacionActual.remove(combinacionActual.size() - 1); // Retroceder
		}
	}

	private double obtenerPrecioHabitacionMasSimple(Alojamiento alojamiento) {
		return alojamiento.getHabitaciones().stream()
				.mapToDouble(Habitacion::getPrecio)
				.min()
				.orElse(0);
	}

	private double aplicarDescuentosYAumentos(double precioBase, LocalDate fechaInicio, LocalDate fechaFin) {
		if (esUltimosDiasMes(fechaFin)) {
			return aplicarAumento(precioBase, 15);
		} else if (esRangoFechas(fechaInicio, fechaFin, 10, 15)) {
			return aplicarAumento(precioBase, 10);
		} else if (esRangoFechas(fechaInicio, fechaFin, 5, 10)) {
			return aplicarDescuento(precioBase, 8);
		}
		return precioBase;
	}

	private boolean esUltimosDiasMes(LocalDate fecha) {
		return fecha.getDayOfMonth() >= 25;
	}

	private boolean esRangoFechas(LocalDate fechaInicio, LocalDate fechaFin, int inicio, int fin) {
		return fechaInicio.getDayOfMonth() >= inicio && fechaFin.getDayOfMonth() <= fin;
	}

	private double aplicarAumento(double precio, double porcentaje) {
		return precio * (1 + porcentaje / 100);
	}

	private double aplicarDescuento(double precio, double porcentaje) {
		return precio * (1 - porcentaje / 100);
	}


	public void confirmarHabitaciones(String nombreHotel, LocalDate fechaInicio, LocalDate fechaFin, int cantidadAdultos, int cantidadNiños, int cantidadHabitaciones) {
		System.out.println("\n--- Confirmar habitaciones para: " + nombreHotel + " ---");

		Alojamiento alojamiento = buscarAlojamientoPorNombre(nombreHotel);

		if (alojamiento == null) {
			System.out.println("No se encontró un hotel con el nombre indicado.");
			return;
		}

		int totalPersonas = cantidadAdultos + cantidadNiños;

		boolean disponibilidad = mostrarHabitacionesDisponibles(alojamiento, fechaInicio, fechaFin, totalPersonas, cantidadHabitaciones);

		if (!disponibilidad) {
			System.out.println("No hay habitaciones disponibles para las fechas indicadas.");
		}
	}

	// Método auxiliar para mostrar habitaciones disponibles
	private boolean mostrarHabitacionesDisponibles(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, int totalPersonas, int cantidadHabitaciones) {
		boolean disponibilidad = false;

		for (Habitacion habitacion : alojamiento.getHabitaciones()) {
			int disponiblesParaFechas = calcularDisponibilidad(habitacion, fechaInicio, fechaFin);

			if (esHabitacionAdecuada(habitacion, totalPersonas, disponiblesParaFechas, cantidadHabitaciones)) {
				mostrarDetallesHabitacion(habitacion, disponiblesParaFechas);
				disponibilidad = true;
			}
		}
		return disponibilidad;
	}

	// Método auxiliar para verificar si una habitación es adecuada
	private boolean esHabitacionAdecuada(Habitacion habitacion, int totalPersonas, int disponiblesParaFechas, int cantidadHabitaciones) {
		return habitacion.getCapacidad() >= totalPersonas && disponiblesParaFechas >= cantidadHabitaciones;
	}

	// Método auxiliar para mostrar los detalles de una habitación
	private void mostrarDetallesHabitacion(Habitacion habitacion, int disponiblesParaFechas) {
		System.out.println("\nTipo de habitación: " + habitacion.getTipo());
		System.out.println("Características: " + habitacion.getCaracteristicas());
		System.out.println("Capacidad: " + habitacion.getCapacidad() + " personas");
		System.out.println("Precio por noche: " + habitacion.getPrecio());
		System.out.println("Cantidad disponible en las fechas seleccionadas: " + disponiblesParaFechas);
	}


	private int calcularDisponibilidad(Habitacion habitacion, LocalDate fechaInicio, LocalDate fechaFin) {
		int cantidadDisponible = habitacion.getCantidad();

		// Iterar sobre las reservas para verificar conflictos de fechas
		for (Reserva reserva : habitacion.getReservas()) {
			if (!(fechaFin.isBefore(reserva.getFechaInicio()) || fechaInicio.isAfter(reserva.getFechaFin()))) {
				// Obtener cuántas habitaciones de este tipo están ocupadas en esta reserva
				Map<String, Integer> habitacionesReservadas = reserva.getHabitacionesPorTipo();
				int cantidadReservada = habitacionesReservadas.getOrDefault(habitacion.getTipo(), 0);
				cantidadDisponible -= cantidadReservada; // Reducir por la cantidad reservada
			}
		}
		return Math.max(cantidadDisponible, 0); // Asegurarse de no retornar valores negativos
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
		return alojamientos.stream()
				.filter(alojamiento -> alojamiento.getCiudad().equalsIgnoreCase(ciudad)
						&& alojamiento.getTipo().equalsIgnoreCase(tipo))
				.collect(Collectors.toList());
	}

	public boolean confirmarDisponibilidad(String nombreAlojamiento, LocalDate fechaInicio, LocalDate fechaFin, Map<String, Integer> habitacionesPorTipo) {
		// Buscar el alojamiento por nombre
		Alojamiento alojamiento = buscarAlojamientoPorNombre(nombreAlojamiento);
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
		return verificarDisponibilidadHabitaciones(alojamiento, fechaInicio, fechaFin, habitacionesPorTipo);
	}

	// Método auxiliar para verificar la disponibilidad de habitaciones por tipo
	private boolean verificarDisponibilidadHabitaciones(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, Map<String, Integer> habitacionesPorTipo) {
		for (Map.Entry<String, Integer> entry : habitacionesPorTipo.entrySet()) {
			String tipoHabitacion = entry.getKey();
			int cantidadSolicitada = entry.getValue();

			// Calcular el total de habitaciones disponibles para el tipo solicitado
			int cantidadDisponible = alojamiento.getHabitaciones().stream()
					.filter(h -> h.getTipo().equalsIgnoreCase(tipoHabitacion) && h.estaDisponible(fechaInicio, fechaFin))
					.mapToInt(Habitacion::getCantidad)
					.sum();
			if (cantidadDisponible < cantidadSolicitada) {
				System.out.println("No hay suficientes habitaciones disponibles para el tipo " + tipoHabitacion + ".");
				return false;
			}
		}
		System.out.println("Hay disponibilidad para todas las habitaciones solicitadas.");
		return true;
	}

	public boolean verificarDisponibilidad(String tipoHabitacion, LocalDate fechaInicio, LocalDate fechaFin, int cantidadSolicitada) {
		int habitacionesDisponibles = 0;

		// Verificar habitaciones disponibles en todos los alojamientos
		for (Alojamiento alojamiento : alojamientos) {
			for (Habitacion habitacion : alojamiento.getHabitaciones()) {
				// Filtrar por tipo de habitación y disponibilidad
				if (habitacion.getTipo().equalsIgnoreCase(tipoHabitacion) && habitacion.estaDisponible(fechaInicio, fechaFin)) {
					habitacionesDisponibles++;
					if (habitacionesDisponibles >= cantidadSolicitada) {
						return true; // Hay suficientes habitaciones disponibles
					}
				}
			}
		}
		return false; // No hay suficientes habitaciones disponibles
	}

	public void actualizarReserva(Scanner scanner) {
		System.out.print("Ingrese el email del cliente: ");
		String email = scanner.nextLine();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		System.out.print("Ingrese la fecha de nacimiento del cliente (dd/MM/yyyy): ");
		LocalDate fechaNacimiento = LocalDate.parse(scanner.nextLine(), formatter);

		// Autenticación
		Reserva reserva = autenticarUsuario(email, fechaNacimiento);
		if (reserva == null) {
			System.out.println("Autenticación fallida. Verifique el correo y la fecha de nacimiento.");
			return;
		}

		System.out.println("\nReserva actual:");
		mostrarDetalleReserva(reserva);
		System.out.println("\n¿Qué desea actualizar?");
		System.out.println("1. Cambiar habitación");
		System.out.println("2. Cambiar alojamiento");
		System.out.print("Seleccione una opción: ");
		int opcion = scanner.nextInt();
		scanner.nextLine(); // Consumir el salto de línea

		switch (opcion) {
		case 1:
			cambiarHabitaciones(reserva, scanner);
			break;
		case 2:
			cambiarAlojamiento(reserva, scanner);
			break;
		default:
			System.out.println("Opción no válida.");
		}
	}

	private void cambiarHabitaciones(Reserva reserva, Scanner scanner) {
		while (true) {
			mostrarHabitacionesActuales(reserva);
			String tipoSeleccionado = seleccionarHabitacionActual(reserva, scanner);
			if (tipoSeleccionado == null) break;
			List<Habitacion> habitacionesDisponibles = obtenerHabitacionesDisponibles(reserva);
			if (habitacionesDisponibles.isEmpty()) {
				mostrarMensajeNoHayHabitaciones();
				continue;
			}
			mostrarHabitacionesDisponibles(habitacionesDisponibles, reserva);
			Habitacion nuevaHabitacion = seleccionarNuevaHabitacion(habitacionesDisponibles, scanner);
			if (nuevaHabitacion == null) continue;
			int nuevaCantidad = validarCantidad(scanner, nuevaHabitacion, reserva);
			if (nuevaCantidad == -1) continue;
			actualizarReserva(reserva, tipoSeleccionado, nuevaHabitacion, nuevaCantidad);
		}
	}

	private void mostrarMensajeNoHayHabitaciones() {
		System.out.println("No hay habitaciones disponibles en el alojamiento para las fechas seleccionadas.");
	}

	// Solicitar selección de habitación actual
	private String seleccionarHabitacionActual(Reserva reserva, Scanner scanner) {
		String input = obtenerEntrada(scanner, "\nSeleccione el número de la habitación que desea cambiar (o escriba 'fin' para terminar): ");
		if (input.equalsIgnoreCase("fin")) return null;

		int index = validarSeleccion(input, reserva.getHabitacionesPorTipo().keySet().size());
		if (index == -1) return null;

		return obtenerTipoSeleccionado(reserva, index);
	}

	// Solicitar selección de nueva habitación
	private Habitacion seleccionarNuevaHabitacion(List<Habitacion> habitacionesDisponibles, Scanner scanner) {
		int nuevaHabitacionIndex = validarSeleccion(obtenerEntrada(scanner, "\nSeleccione el número de la nueva habitación: "), habitacionesDisponibles.size());
		if (nuevaHabitacionIndex == -1) return null;
		return habitacionesDisponibles.get(nuevaHabitacionIndex);
	}

	// Métodos auxiliares
	private void mostrarHabitacionesActuales(Reserva reserva) {
		System.out.println("\nHabitaciones actuales en la reserva:");
		List<String> tiposActuales = new ArrayList<>(reserva.getHabitacionesPorTipo().keySet());
		for (int i = 0; i < tiposActuales.size(); i++) {
			String tipo = tiposActuales.get(i);
			int cantidad = reserva.getHabitacionesPorTipo().get(tipo);
			System.out.println((i + 1) + ". " + tipo + " - " + cantidad + " habitación(es)");
		}
	}

	private String obtenerEntrada(Scanner scanner, String mensaje) {
		System.out.print(mensaje);
		return scanner.nextLine();
	}

	private int validarSeleccion(String input, int maxOpciones) {
		try {
			int index = Integer.parseInt(input) - 1;
			if (index >= 0 && index < maxOpciones) return index;
		} catch (NumberFormatException e) {
			// Ignorar excepciones de entrada no válida
		}
		System.out.println("Selección no válida. Intente de nuevo.");
		return -1;
	}

	private String obtenerTipoSeleccionado(Reserva reserva, int index) {
		List<String> tiposActuales = new ArrayList<>(reserva.getHabitacionesPorTipo().keySet());
		return tiposActuales.get(index);
	}

	private List<Habitacion> obtenerHabitacionesDisponibles(Reserva reserva) {
		return reserva.getAlojamiento().getHabitaciones().stream()
				.filter(h -> h.estaDisponible(reserva.getFechaInicio(), reserva.getFechaFin()))
				.toList();
	}

	private void mostrarHabitacionesDisponibles(List<Habitacion> habitacionesDisponibles, Reserva reserva) {
		System.out.println("\nHabitaciones disponibles en el alojamiento:");
		for (int i = 0; i < habitacionesDisponibles.size(); i++) {
			Habitacion habitacion = habitacionesDisponibles.get(i);
			System.out.println((i + 1) + ". Tipo: " + habitacion.getTipo() + " | Precio: " + habitacion.getPrecio() +
					" | Capacidad: " + habitacion.getCapacidad() + " | Cantidad disponible: " + habitacion.getCantidad());
		}
	}

	private int validarCantidad(Scanner scanner, Habitacion nuevaHabitacion, Reserva reserva) {
		String input = obtenerEntrada(scanner, "Ingrese la cantidad de habitaciones de este tipo: ");
		try {
			int cantidad = Integer.parseInt(input);
			if (cantidad > 0 && cantidad <= nuevaHabitacion.getCantidad()) return cantidad;
		} catch (NumberFormatException e) {
			// Ignorar excepciones de entrada no válida
		}
		System.out.println("Cantidad no válida. Intente de nuevo.");
		return -1;
	}

	private void actualizarReserva(Reserva reserva, String tipoSeleccionado, Habitacion nuevaHabitacion, int nuevaCantidad) {
		int cantidadActual = reserva.getHabitacionesPorTipo().get(tipoSeleccionado);

		// Liberar habitación seleccionada
		reserva.getHabitacionesPorTipo().put(tipoSeleccionado, cantidadActual - 1);
		if (reserva.getHabitacionesPorTipo().get(tipoSeleccionado) == 0) {
			reserva.getHabitacionesPorTipo().remove(tipoSeleccionado);
		}

		// Reservar nuevas habitaciones
		reserva.getHabitacionesPorTipo().put(
				nuevaHabitacion.getTipo(),
				reserva.getHabitacionesPorTipo().getOrDefault(nuevaHabitacion.getTipo(), 0) + nuevaCantidad
		);
		nuevaHabitacion.agregarReserva(reserva);

		System.out.println("\nHabitación(es) actualizada(s) con éxito.");
	}

	public void mostrarTodasLasReservas() {
		if (reservas.isEmpty()) {
			System.out.println("No hay reservas realizadas.");
			return;
		}

		System.out.println("Todas las reservas realizadas:");
		for (Reserva reserva : reservas) {
			Cliente cliente = reserva.getCliente();
			Alojamiento alojamiento = reserva.getAlojamiento();

			if (cliente == null || alojamiento == null) {
				System.out.println("Error: La reserva no tiene cliente o alojamiento asociado.");
				continue;
			}

			System.out.println("----------------------------------------------------");
			System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
			System.out.println("Email: " + cliente.getEmail());
			System.out.println("Alojamiento: " + alojamiento.getNombre());
			System.out.println("Tipo: " + alojamiento.getTipo());
			mostrarDetallesReserva(reserva, alojamiento);
			System.out.println("----------------------------------------------------");
		}
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

	private void reservarHabitaciones(Reserva reserva, Habitacion habitacion, int cantidad) {
		for (int i = 0; i < cantidad; i++) {
			habitacion.agregarReserva(reserva);
		}

		// Actualizar el mapa de la reserva con el nuevo tipo de habitación
		reserva.getHabitacionesPorTipo().put(habitacion.getTipo(), cantidad);
		System.out.println("Se han reservado " + cantidad + " habitación(es) del tipo " + habitacion.getTipo() + ".");
	}

	public Alojamiento buscarAlojamientoPorReserva(Reserva reserva) {
		// Buscar el alojamiento a partir de la reserva, considerando tanto las habitaciones como el alojamiento
		return alojamientos.stream()
				.filter(a -> a.equals(reserva.getAlojamiento()) && // Comparar directamente con el objeto Alojamiento
						a.getHabitaciones().stream().anyMatch(h -> h.getReservas().contains(reserva)))
				.findFirst()
				.orElse(null);
	}

	private void cambiarAlojamiento(Reserva reserva, Scanner scanner) {
		System.out.println("\nLa reserva actual será eliminada. Por favor, cree una nueva reserva desde el menú principal.");
		eliminarReserva(reserva);
	}

	public void mostrarDetalleReserva(Reserva reserva) {
		if (reserva.getCliente() == null) {
			System.out.println("La reserva no tiene un cliente asociado.");
			return;
		}

		Cliente cliente = reserva.getCliente(); // Obtener el cliente desde la reserva
		System.out.println("----------------------------------------------------");
		System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
		System.out.println("Email: " + cliente.getEmail());
		System.out.println("Alojamiento: " + reserva.getAlojamiento().getNombre());
		System.out.println("Fechas: " + reserva.getFechaInicio() + " a " + reserva.getFechaFin());

		// Mostrar las habitaciones reservadas
		if (reserva.getHabitacionesPorTipo() != null && !reserva.getHabitacionesPorTipo().isEmpty()) {
			System.out.println(HABITACIONES_RESERVADAS);
			for (String habitacionId : reserva.getHabitacionesPorTipo().keySet()) {
				int cantidad = reserva.getHabitacionesPorTipo().get(habitacionId);
				System.out.println("- Habitacion ID: " + habitacionId + ": " + cantidad + " habitación(es)");
			}
		} else {
			System.out.println("No hay habitaciones reservadas.");
		}
	}

	public Reserva autenticarUsuario(String email, LocalDate fechaNacimiento) {
		return reservas.stream()
				.filter(r -> r.getCliente().getEmail().equalsIgnoreCase(email) && r.getCliente().getFechaNacimiento().equals(fechaNacimiento))
				.findFirst()
				.orElse(null); // Retorna la reserva si la autenticación es exitosa
	}

	public Alojamiento buscarAlojamientoPorNombre(String nombreAlojamiento) {
		return alojamientos.stream()
				.filter(a -> a.getNombre().equalsIgnoreCase(nombreAlojamiento))
				.findFirst()
				.orElse(null);
	}

	public void eliminarReserva(Reserva reserva) {
		reservas.remove(reserva);
		System.out.println("La reserva ha sido eliminada.");
	}

	public void reservarHotel(Cliente cliente, Alojamiento alojamiento, Integer cantidadAdultos, Integer cantidadNiños,
							  LocalDate fechaInicio, LocalDate fechaFin, Map<String, Integer> habitacionesPorTipo, String horaLlegada) {

		// Validar datos de entrada
		if (!validarEntrada(cliente, alojamiento)) return;

		// Map para almacenar habitaciones reservadas
		Map<Habitacion, Integer> habitacionesReservadas = new HashMap<>();

		// Validar y procesar cada tipo de habitación
		if (!procesarTiposDeHabitaciones(alojamiento, habitacionesPorTipo, fechaInicio, fechaFin, habitacionesReservadas)) return;

		// Crear la reserva general
		crearReservaGeneral(cliente, alojamiento, cantidadAdultos, cantidadNiños, fechaInicio, fechaFin, habitacionesPorTipo, horaLlegada);

		// Confirmación de la reserva
		mostrarConfirmacion(habitacionesReservadas);
	}

	// Método auxiliar para validar datos de entrada
	private boolean validarEntrada(Cliente cliente, Alojamiento alojamiento) {
		if (cliente == null || alojamiento == null) {
			System.out.println("Los datos del cliente o alojamiento no son válidos.");
			return false;
		}
		return true;
	}

	// Método auxiliar para procesar y validar habitaciones
	private boolean procesarTiposDeHabitaciones(Alojamiento alojamiento, Map<String, Integer> habitacionesPorTipo,
												LocalDate fechaInicio, LocalDate fechaFin, Map<Habitacion, Integer> habitacionesReservadas) {
		for (Map.Entry<String, Integer> entry : habitacionesPorTipo.entrySet()) {
			String tipoHabitacion = entry.getKey();
			int cantidadSolicitada = entry.getValue();

			// Validar y reservar habitaciones por tipo
			if (!validarYReservarHabitaciones(alojamiento, tipoHabitacion, cantidadSolicitada, fechaInicio, fechaFin, habitacionesReservadas)) {
				System.out.println("No hay suficientes habitaciones disponibles para el tipo " + tipoHabitacion + ".");
				return false;
			}
		}
		return true;
	}

	// Método auxiliar para validar y reservar habitaciones por tipo
	private boolean validarYReservarHabitaciones(Alojamiento alojamiento, String tipoHabitacion, int cantidadSolicitada,
												 LocalDate fechaInicio, LocalDate fechaFin, Map<Habitacion, Integer> habitacionesReservadas) {
		// Obtener habitaciones disponibles
		List<Habitacion> habitacionesDisponibles = obtenerHabitacionesDisponibles(alojamiento, tipoHabitacion, fechaInicio, fechaFin);

		// Validar cantidad total disponible
		int cantidadTotalDisponible = habitacionesDisponibles.stream().mapToInt(Habitacion::getCantidad).sum();
		if (cantidadTotalDisponible < cantidadSolicitada) return false;

		// Reservar habitaciones necesarias
		reservarHabitaciones(habitacionesDisponibles, cantidadSolicitada, habitacionesReservadas, fechaInicio, fechaFin);
		return true;
	}

	// Método auxiliar para obtener habitaciones disponibles
	private List<Habitacion> obtenerHabitacionesDisponibles(Alojamiento alojamiento, String tipoHabitacion,
															LocalDate fechaInicio, LocalDate fechaFin) {
		return alojamiento.getHabitaciones().stream()
				.filter(h -> h.getTipo().equalsIgnoreCase(tipoHabitacion) && h.estaDisponible(fechaInicio, fechaFin))
				.toList();
	}

	// Método auxiliar para reservar habitaciones
	private void reservarHabitaciones(List<Habitacion> habitacionesDisponibles, int cantidadSolicitada,
									  Map<Habitacion, Integer> habitacionesReservadas, LocalDate fechaInicio, LocalDate fechaFin) {
		for (Habitacion habitacion : habitacionesDisponibles) {
			int cantidadAReservar = Math.min(cantidadSolicitada, habitacion.getCantidad());
			habitacion.setCantidad(habitacion.getCantidad() - cantidadAReservar);
			habitacionesReservadas.put(habitacion, cantidadAReservar);
			cantidadSolicitada -= cantidadAReservar;
			if (cantidadSolicitada == 0) break;
		}
	}

	// Método auxiliar para crear una reserva general
	private void crearReservaGeneral(Cliente cliente, Alojamiento alojamiento, Integer cantidadAdultos, Integer cantidadNiños,
									 LocalDate fechaInicio, LocalDate fechaFin, Map<String, Integer> habitacionesPorTipo, String horaLlegada) {
		Reserva reservaGeneral = new Reserva(cliente, alojamiento, fechaInicio, fechaFin, cantidadAdultos, cantidadNiños, habitacionesPorTipo, horaLlegada);
		reservas.add(reservaGeneral);
	}

	// Método auxiliar para mostrar la confirmación de reserva
	private void mostrarConfirmacion(Map<Habitacion, Integer> habitacionesReservadas) {
		System.out.println("Se ha realizado la reserva con éxito.");
		System.out.println(HABITACIONES_RESERVADAS);
		habitacionesReservadas.forEach((habitacion, cantidad) ->
				System.out.println("- Tipo: " + habitacion.getTipo() + " | Cantidad: " + cantidad));
	}

	public void reservarFincaApartamento(Cliente cliente, Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, Integer cantidadAdultos, Integer cantidadNiños, String horaLlegada) {

		// Validar que el alojamiento no sea nulo
		if (alojamiento == null) {
			System.out.println("El alojamiento proporcionado no es válido.");
			return;
		}

		// Crear la reserva
		Reserva reserva = new Reserva(cliente, alojamiento, fechaInicio, fechaFin, cantidadAdultos, cantidadNiños, horaLlegada);

		// Registrar la reserva en la lista global
		reservas.add(reserva);
		System.out.println("Se ha realizado la reserva con éxito.");
	}

	public Boolean validarFincaApartamento(LocalDate fechaInicio, LocalDate fechaFin, Alojamiento alojamiento)
	{
		boolean hayConflicto = reservas.stream()
				.anyMatch(r -> r.getAlojamiento().equals(alojamiento) &&
						!(fechaFin.isBefore(r.getFechaInicio()) || fechaInicio.isAfter(r.getFechaFin())));

		if (hayConflicto) {
			System.out.println("No se puede realizar la reserva. Ya existe una reserva para este alojamiento en las fechas seleccionadas.");
			return false;
		}
		return true;
	}

	public void reservarDiaDeSol(Cliente cliente, Alojamiento alojamiento, LocalDate fechaInicio, Integer cantidadAdultos, Integer cantidadNiños) {

		// Validar que el alojamiento no sea nulo
		if (alojamiento == null) {
			System.out.println("El alojamiento proporcionado no es válido.");
			return;
		}

		// Crear la reserva
		Reserva reserva = new Reserva(cliente, alojamiento, fechaInicio, cantidadAdultos, cantidadNiños);

		// Registrar la reserva en la lista global
		reservas.add(reserva);
		System.out.println("Se ha realizado la reserva con éxito.");
	}
}

