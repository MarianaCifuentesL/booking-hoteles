package src.main;

import src.model.*;

import java.time.Clock;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Main {
	public static void main(String[] args) {
		BookingApp app = new BookingApp();
		inicializarAlojamientos(app);

		Scanner scanner = new Scanner(System.in);
		opcionesMenu(scanner, app);
	}

	private static void opcionesMenu(Scanner scanner, BookingApp app) {
		int opcion;
		do {
			mostrarMenu();
			opcion = obtenerOpcion(scanner);
			procesarOpcion(opcion, app, scanner);
		} while (opcion != 6);
	}

	private static int obtenerOpcion(Scanner scanner) {
		int opcion = scanner.nextInt();
		scanner.nextLine(); // Consumir el salto de línea
		return opcion;
	}

	private static void procesarOpcion(int opcion, BookingApp app, Scanner scanner) {
		switch (opcion) {
			case 1:
				buscarAlojamientos(app, scanner);
				break;
			case 2:
				realizarReserva(app, scanner);
				break;
			case 3:
				mostrarReservas(app);
				break;
			case 4:
				confirmarHabitaciones(app, scanner);
				break;
			case 5:
				actualizarReserva(app, scanner);
				break;
			case 6:
				mostrarMensajeSalida();
				break;
			default:
				mostrarOpcionInvalida();
		}
	}

	private static void mostrarMensajeSalida() {
		System.out.println("Gracias por usar la aplicación. ¡Adiós!");
	}

	private static void mostrarOpcionInvalida() {
		System.out.println("Opción no válida. Intente de nuevo.");
	}


	private static void mostrarMenu() {
		System.out.println("\n--- Menú Principal ---");
		System.out.println("1. Buscar alojamientos");
		System.out.println("2. Realizar una reserva");
		System.out.println("3. Mostrar reservas realizadas");
		System.out.println("4. Confirmar habitaciones");
		System.out.println("5. Actualizar una reserva");
		System.out.println("6. Salir");
		System.out.print("Seleccione una opción: ");
	}


	private static void inicializarAlojamientos(BookingApp app) {

		// Inicializar Hotel Quindío
		Alojamiento hotel1 = new Alojamiento("Hotel Quindío", "Armenia", "Hotel", 4, 200000.0);
		hotel1.agregarHabitacion("Sencilla", 100000.0, "Cama sencilla, WiFi, Aire Acondicionado, TV", 2, 3);
		hotel1.agregarHabitacion("Doble", 150000.0, "2 camas dobles, Vista al mar, Aire acondicionado, Cafetera, TV de pantalla plana, ducha, escritorio", 4, 2);
		hotel1.agregarHabitacion("Triple", 200000.0, "3 camas, Balcón, Mini bar, WiFi, Caja fuerte, TV", 3, 2);
		hotel1.agregarHabitacion("Suite", 350000.0, "1 cama King Size, Jacuzzi, Cocina equipada, Sala de estar, Vista panorámica", 2, 2);
		hotel1.agregarHabitacion("Familiar", 250000.0, "4 camas, Espacio amplio, Área de juegos, Aire acondicionado, Cocina pequeña", 6, 2);

		// Inicializar Finca Campestre
		Alojamiento finca1 = new Alojamiento("Finca Campestre", "Armenia", "Finca", 5, 250000.0);

		// Instancias para Día de Sol
		Alojamiento diaDeSol1 = new Alojamiento("Resort Caribe Día de Sol", "Cartagena", "Día de Sol", 5, 120000.0, List.of("Natación", "Kayak", "Spa", "Senderismo"), true, true);
		Alojamiento diaDeSol2 = new Alojamiento("EcoParque Aventura", "Medellín", "Día de Sol", 4, 80000.0, List.of("Tirolesa", "Caminata ecológica", "Paintball"), false, true);
		Alojamiento diaDeSol3 = new Alojamiento("Club Campestre Día Relax", "Cali", "Día de Sol", 3, 90000.0, List.of("Piscina", "Yoga al aire libre", "Tenis"), true, false);

		// Inicializar Apartamento
		Alojamiento apartamento = new Alojamiento("Apartamentos Cibeles", "Armenia", "Apartamento", 5, 200000);

		// Agregar alojamientos al sistema
		app.agregarAlojamiento(hotel1);
		app.agregarAlojamiento(finca1);
		app.agregarAlojamiento(diaDeSol1);
		app.agregarAlojamiento(diaDeSol2);
		app.agregarAlojamiento(diaDeSol3);
		app.agregarAlojamiento(apartamento);

		// Crear cliente
		Cliente cliente = new Cliente("Juan", "Pérez", LocalDate.of(2003, 3, 24),
				"juan", "Colombiana", "123456789");

		// Map de habitaciones reservadas
		Map<String, Integer> habitacionesReservadas = new HashMap<>();
		habitacionesReservadas.put("Sencilla", 3);
		habitacionesReservadas.put("Doble", 1);

		// Realizar reserva
		app.reservarHotel(cliente, hotel1, 2, 1, LocalDate.of(2025, 5, 25), LocalDate.of(2025, 5, 30), habitacionesReservadas, "15:00");
	}



	private static void buscarAlojamientos(BookingApp app, Scanner scanner) {

		String ciudad = solicitarCiudad(scanner);

		System.out.print("Ingrese el tipo de alojamiento (1.Hotel, 2.Apartamento, 3.Finca, 4.Día de Sol): ");
		String tipo = switch (scanner.nextInt()) {
			case 1 -> "Hotel";
			case 2 -> "Apartamento";
			case 3 -> "Finca";
			case 4 -> "Día de Sol";
			default -> throw new IllegalArgumentException("Opción no válida");
		};
		scanner.nextLine(); // Consumir el salto de línea
		if (tipo.equals("Día de Sol")) {
			buscarDiaDeSol(app, scanner, ciudad, tipo);
		} else {
			if ((tipo.equals("Finca")) || (tipo.equals("Apartamento")))
			{
				buscarFincaApartamento(app, scanner, ciudad, tipo);
			}
			else {
				buscarHotel(app, scanner, ciudad, tipo);
			}
		}
	}

	private static void buscarHotel(BookingApp app, Scanner scanner, String ciudad, String tipo) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		System.out.print("Ingrese la fecha de inicio (dd/MM/yyyy): ");
		LocalDate fechaInicio = LocalDate.parse(scanner.nextLine(), formatter);
		System.out.print("Ingrese la fecha de finalización (dd/MM/yyyy): ");
		LocalDate fechaFin = LocalDate.parse(scanner.nextLine(), formatter);
		System.out.print("Ingrese la cantidad de adultos: ");
		int cantidadAdultos = scanner.nextInt();
		System.out.print("Ingrese la cantidad de niños: ");
		int cantidadNiños = scanner.nextInt();
		System.out.print("Ingrese la cantidad de habitaciones: ");
		int cantidadHabitaciones = scanner.nextInt();

		app.buscarAlojamientos(ciudad, tipo, fechaInicio, fechaFin, cantidadAdultos, cantidadNiños, cantidadHabitaciones);
	}

	private static void buscarFincaApartamento(BookingApp app, Scanner scanner, String ciudad, String tipo) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		System.out.print("Ingrese la fecha de inicio (dd/MM/yyyy): ");
		LocalDate fechaInicio = LocalDate.parse(scanner.nextLine(), formatter);
		System.out.print("Ingrese la fecha de finalización (dd/MM/yyyy): ");
		LocalDate fechaFin = LocalDate.parse(scanner.nextLine(), formatter);
		System.out.print("Ingrese la cantidad de adultos: ");
		int cantidadAdultos = scanner.nextInt();
		System.out.print("Ingrese la cantidad de niños: ");
		int cantidadNiños = scanner.nextInt();

		app.buscarAlojamientos(ciudad, tipo, fechaInicio, fechaFin, cantidadAdultos, cantidadNiños, 0);
	}


	private static void buscarDiaDeSol(BookingApp app, Scanner scanner, String ciudad, String tipo) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		System.out.print("Ingrese la fecha de inicio (dd/MM/yyyy): ");
		LocalDate fechaInicio = LocalDate.parse(scanner.nextLine(), formatter);
		System.out.print("Ingrese la cantidad de adultos: ");
		int cantidadAdultos = scanner.nextInt();
		System.out.print("Ingrese la cantidad de niños: ");
		int cantidadNiños = scanner.nextInt();

		app.buscarAlojamientos(ciudad, tipo, fechaInicio, null, cantidadAdultos, cantidadNiños, 0);

	}

	//Método auxiliar para confirmar habitaciones
	private static void confirmarHabitaciones(BookingApp app, Scanner scanner) {

		System.out.print("Ingrese el nombre del hotel: ");
		String nombreHotel = scanner.nextLine();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		System.out.print("Ingrese la fecha de inicio (dd/MM/yyyy): ");
		LocalDate fechaInicio = LocalDate.parse(scanner.nextLine(), formatter);
		System.out.print("Ingrese la fecha de finalización (dd/MM/yyyy): ");
		LocalDate fechaFin = LocalDate.parse(scanner.nextLine(), formatter);
		System.out.print("Ingrese la cantidad de adultos: ");
		int cantidadAdultos = scanner.nextInt();
		System.out.print("Ingrese la cantidad de niños: ");
		int cantidadNiños = scanner.nextInt();
		System.out.print("Ingrese la cantidad de habitaciones: ");
		int cantidadHabitaciones = scanner.nextInt();
		scanner.nextLine(); // Consumir el salto de línea

		app.confirmarHabitaciones(nombreHotel, fechaInicio, fechaFin, cantidadAdultos, cantidadNiños, cantidadHabitaciones);
	}


	private static void realizarReserva(BookingApp app, Scanner scanner) {

		String ciudad = solicitarCiudad(scanner);

		String tipo = solicitarTipoAlojamiento(scanner);

		List<Alojamiento> alojamientos = listarAlojamientos(app, ciudad, tipo);
		if (alojamientos == null) return;

		reservar(app, scanner, alojamientos, tipo, ciudad);
	}

	private static void reservar(BookingApp app, Scanner scanner, List<Alojamiento> alojamientos, String tipo, String ciudad) {
		System.out.print("Seleccione un alojamiento: ");
		int opcion = scanner.nextInt();
		scanner.nextLine(); // Consumir el salto de línea

		Alojamiento alojamientoSeleccionado = alojamientos.get(opcion - 1);

		switch (tipo) {
			case "Hotel" -> solicitarDatosHotel(scanner, alojamientoSeleccionado, ciudad, app);
			case "Apartamento", "Finca" -> solicitarDatosFincaApartamento(scanner, alojamientoSeleccionado, app);
			case "Día de Sol" -> solicitarDatosDiaDeSol(scanner, alojamientoSeleccionado, app);
			default -> throw new IllegalArgumentException("Opción no válida");
		}
	}

	private static List<Alojamiento> listarAlojamientos(BookingApp app, String ciudad, String tipo) {
		List<Alojamiento> alojamientos = app.buscarAlojamientosPorCiudadYTipo(ciudad, tipo);
		if (alojamientos.isEmpty()) {
			System.out.println("No se encontraron alojamientos disponibles.");
			return null;
		}

		System.out.println("Alojamientos disponibles:");
		for (int i = 0; i < alojamientos.size(); i++) {
			System.out.println((i + 1) + ". " + alojamientos.get(i).getNombre());
		}
		return alojamientos;
	}

	private static String solicitarTipoAlojamiento(Scanner scanner) {
		System.out.print("Ingrese el tipo de alojamiento (1.Hotel, 2.Apartamento, 3.Finca, 4.Día de Sol): ");
		String tipo = switch (scanner.nextInt()) {
			case 1 -> "Hotel";
			case 2 -> "Apartamento";
			case 3 -> "Finca";
			case 4 -> "Día de Sol";
			default -> throw new IllegalArgumentException("Opción no válida");
		};
		scanner.nextLine(); // Consumir el salto de línea
		return tipo;
	}

	private static void solicitarDatosHotel(Scanner scanner, Alojamiento alojamiento, String ciudad, BookingApp app) {

		LocalDate fechaInicio = solicitarFecha(scanner, "Ingrese la fecha de inicio (dd/MM/yyyy): ");
		LocalDate fechaFin = solicitarFecha(scanner, "Ingrese la fecha de finalización (dd/MM/yyyy): ");
		System.out.print("Ingrese la cantidad de adultos: ");
		int cantidadAdultos = scanner.nextInt();
		System.out.print("Ingrese la cantidad de niños: ");
		int cantidadNiños = scanner.nextInt();

		// Capturar los tipos de habitaciones y cantidades
		Map<String, Integer> habitacionesPorTipo = new HashMap<>();
		System.out.println("Ingrese los tipos de habitaciones y las cantidades (Escriba 'fin' para terminar)");

		while (true) {
			System.out.print("Ingrese el tipo de habitación (1.Sencilla, 2.Doble, 3.Triple, 4.Suite, 5.Familiar, 6.Fin): ");
			String tipoHabitacion = switch (scanner.nextInt()) {
				case 1 -> "Sencilla";
				case 2 -> "Doble";
				case 3 -> "Triple";
				case 4 -> "Suite";
				case 5 -> "Familiar";
				case 6 -> "Fin";
				default -> throw new IllegalArgumentException("Opción no válida");
			};
			scanner.nextLine(); // Consumir el salto de línea
			if (tipoHabitacion.equalsIgnoreCase("fin")) break;

			System.out.print("Cantidad: ");
			int cantidad = scanner.nextInt();
			scanner.nextLine(); // Consumir salto de línea

			habitacionesPorTipo.put(tipoHabitacion, cantidad);
		}

		Cliente cliente = solicitarDatosCliente(scanner, app);
		System.out.print("Ingrese la hora aproximada de llegada (HH:mm): ");
		String horaLlegada = scanner.nextLine();

		app.reservarHotel(cliente, alojamiento, cantidadAdultos, cantidadNiños, fechaInicio, fechaFin, habitacionesPorTipo, horaLlegada);
	}

	private static Cliente solicitarDatosCliente(Scanner scanner, BookingApp app) {

		scanner.nextLine(); // Consumir cualquier salto de línea pendiente
		System.out.print("Ingrese su nombre: ");
		String nombreCliente = scanner.nextLine();
		System.out.print("Ingrese su apellido: ");
		String apellidoCliente = scanner.nextLine();
		LocalDate fechaNacimiento = solicitarFecha(scanner, "Ingrese su fecha de nacimiento (dd/MM/yyyy): ");
		System.out.print("Ingrese su email: ");
		String email = scanner.nextLine();
		System.out.print("Ingrese su nacionalidad: ");
		String nacionalidad = scanner.nextLine();
		System.out.print("Ingrese su teléfono: ");
		String telefono = scanner.nextLine();

		return new Cliente(nombreCliente, apellidoCliente, fechaNacimiento, email, nacionalidad, telefono);
	}


	private static void solicitarDatosFincaApartamento(Scanner scanner, Alojamiento alojamiento, BookingApp app) {
		LocalDate fechaInicio = solicitarFecha(scanner, "Ingrese la fecha de inicio (dd/MM/yyyy): ");
		LocalDate fechaFin = solicitarFecha(scanner, "Ingrese la fecha de finalización (dd/MM/yyyy): ");

		Boolean validacion = app.validarFincaApartamento(fechaInicio, fechaFin, alojamiento);

		if (!validacion) { return; }

		System.out.print("Ingrese la cantidad de adultos: ");
		int cantidadAdultos = scanner.nextInt();
		System.out.println("Ingrese la cantidad de niños: ");
		int cantidadNiños = scanner.nextInt();
		Cliente cliente = solicitarDatosCliente(scanner, app);
		System.out.print("Ingrese la hora aproximada de llegada (HH:mm): ");
		String horaLlegada = scanner.nextLine();

		app.reservarFincaApartamento(cliente, alojamiento, fechaInicio, fechaFin, cantidadAdultos, cantidadNiños, horaLlegada);
	}

	private static void solicitarDatosDiaDeSol(Scanner scanner, Alojamiento alojamiento, BookingApp app) {

		LocalDate fechaInicio = solicitarFecha(scanner, "Ingrese la fecha de inicio (dd/MM/yyyy): ");
		System.out.print("Ingrese la cantidad de adultos: ");
		int cantidadAdultos = scanner.nextInt();
		System.out.print("Ingrese la cantidad de niños: ");
		int cantidadNiños = scanner.nextInt();
		Cliente cliente = solicitarDatosCliente(scanner, app);

		app.reservarDiaDeSol(cliente, alojamiento, fechaInicio, cantidadAdultos, cantidadNiños);
	}


	private static String solicitarCiudad(Scanner scanner) {
		System.out.print("Ingrese la ciudad: ");
		return scanner.nextLine();
	}

	private static LocalDate solicitarFecha(Scanner scanner, String mensaje) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		System.out.print(mensaje);
		return LocalDate.parse(scanner.nextLine(), formatter);
	}

	private static void actualizarReserva(BookingApp app, Scanner scanner) {
		app.actualizarReserva(scanner);
	}

	private static void mostrarReservas(BookingApp app) {
		app.mostrarTodasLasReservas();
	}

}