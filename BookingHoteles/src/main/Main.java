package main;

import model.Alojamiento;
import model.DiaDeSol;
import model.Habitacion;

import java.util.List;
import java.util.Scanner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Main {
	public static void main(String[] args) {
		BookingApp app = new BookingApp();
		inicializarAlojamientos(app);

		Scanner scanner = new Scanner(System.in);
		int opcion;

		do {
			mostrarMenu();
			opcion = scanner.nextInt();
			scanner.nextLine(); // Consumir el salto de línea

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
				realizarReserva(app, scanner);
			    break;
			case 6:
				System.out.println("Gracias por usar la aplicación. ¡Adiós!");
			    break;
			default:
				System.out.println("Opción no válida. Intente de nuevo.");
			}
		} while (opcion != 6);
	}

	private static void mostrarMenu() {
		System.out.println("\n--- Menú Principal ---");
		System.out.println("1. Buscar alojamientos");
		System.out.println("2. Realizar una reserva");
		System.out.println("3. Mostrar reservas realizadas");
		System.out.println("4. Confirmar habitaciones");
		System.out.println("5. Realizar reservas");
		System.out.println("6. Salir");
		System.out.print("Seleccione una opción: ");
	}

	private static void inicializarAlojamientos(BookingApp app) {
		Alojamiento hotel1 = new Alojamiento("Hotel Quindío", "Armenia", "Hotel", 4, 200000);
		hotel1.agregarHabitacion(new Habitacion("Simple", 100000, "Cama sencilla, WiFi, Aire Acondicionado, TV", 2));
		hotel1.agregarHabitacion(new Habitacion("Simple", 100000, "Cama sencilla, WiFi, Aire Acondicionado, TV", 2));
		hotel1.agregarHabitacion(new Habitacion("Doble", 150000, "2 camas dobles, Vista al mar, Aire acondicionado, Cafetera, TV de pantalla plana, ducha, escritorio", 4));
		hotel1.agregarHabitacion(new Habitacion("Triple", 200000, "3 camas, Balcón, Mini bar, WiFi, Caja fuerte, TV", 3));
		hotel1.agregarHabitacion(new Habitacion("Suite", 350000, "1 cama King Size, Jacuzzi, Cocina equipada, Sala de estar, Vista panorámica", 2));
		hotel1.agregarHabitacion(new Habitacion("Familiar", 250000, "4 camas, Espacio amplio, Área de juegos, Aire acondicionado, Cocina pequeña", 6));


		Alojamiento finca1 = new Alojamiento("Finca Campestre", "Armenia", "Finca", 5, 250000);
		finca1.agregarHabitacion(new Habitacion("Cabaña", 300000, "Cabaña completa, Piscina privada, Cocina equipada", 6));

		app.agregarAlojamiento(hotel1);
		app.agregarAlojamiento(finca1);
		
	    // Instancias de Día de Sol
	    DiaDeSol diaDeSol1 = new DiaDeSol("Resort Caribe Día de Sol", "Cartagena", 5, 120000, 
	            List.of("Natación", "Kayak", "Spa", "Senderismo"), true, true);

	    DiaDeSol diaDeSol2 = new DiaDeSol("EcoParque Aventura", "Medellín", 4, 80000, 
	            List.of("Tirolesa", "Caminata ecológica", "Paintball"), false, true);

	    DiaDeSol diaDeSol3 = new DiaDeSol("Club Campestre Día Relax", "Cali", 3, 90000, 
	            List.of("Piscina", "Yoga al aire libre", "Tenis"), true, false);

	    app.agregarAlojamiento(diaDeSol1);
	    app.agregarAlojamiento(diaDeSol2);
	    app.agregarAlojamiento(diaDeSol3);
	}


	private static void buscarAlojamientos(BookingApp app, Scanner scanner) {
	    System.out.print("Ingrese la ciudad: ");
	    String ciudad = scanner.nextLine();

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
	        System.out.print("Ingrese la cantidad de adultos: ");
	        int cantidadAdultos = scanner.nextInt();
	        System.out.print("Ingrese la cantidad de niños: ");
	        int cantidadNiños = scanner.nextInt();

	        app.buscarAlojamientos(ciudad, tipo, null, null, cantidadAdultos, cantidadNiños, 0);
	    } else {
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
	}
	
	// Método auxiliar para confirmar habitaciones
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
	    System.out.print("Ingrese el nombre del hotel: ");
	    String nombreHotel = scanner.nextLine();

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    System.out.print("Ingrese la fecha de inicio (dd/MM/yyyy): ");
	    LocalDate fechaInicio = LocalDate.parse(scanner.nextLine(), formatter);

	    System.out.print("Ingrese la fecha de finalización (dd/MM/yyyy): ");
	    LocalDate fechaFin = LocalDate.parse(scanner.nextLine(), formatter);

	    System.out.print("Ingrese el tipo de habitación: ");
	    String tipoHabitacion = scanner.nextLine();

	    System.out.print("Ingrese la cantidad de habitaciones: ");
	    int cantidadHabitaciones = scanner.nextInt();
	    scanner.nextLine(); // Consumir salto de línea

	    System.out.print("Ingrese el nombre del cliente: ");
	    String nombre = scanner.nextLine();

	    System.out.print("Ingrese el apellido del cliente: ");
	    String apellido = scanner.nextLine();

	    System.out.print("Ingrese el email del cliente: ");
	    String email = scanner.nextLine();

	    System.out.print("Ingrese la nacionalidad del cliente: ");
	    String nacionalidad = scanner.nextLine();

	    System.out.print("Ingrese el teléfono del cliente: ");
	    String telefono = scanner.nextLine();

	    app.reservar(nombreHotel, fechaInicio, fechaFin, tipoHabitacion, cantidadHabitaciones, nombre, apellido, email, nacionalidad, telefono);
	}

	private static void mostrarReservas(BookingApp app) {
		// Lógica para mostrar reservas
	}
}





