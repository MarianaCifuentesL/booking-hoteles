package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Alojamiento;

public class BookingApp {
    private List<Alojamiento> alojamientos;

    public BookingApp() {
        this.alojamientos = new ArrayList<>();
        inicializarAlojamientos();
    }

    // Inicializa algunos alojamientos de ejemplo
    private void inicializarAlojamientos() {
        alojamientos.add(new Alojamiento("Hotel Sol", "Hotel", "Cartagena", 5, 120.0));
        alojamientos.add(new Alojamiento("Apartamento Mar", "Apartamento",  "Cartagena", 4, 80.0));
        alojamientos.add(new Alojamiento("Finca Bella", "Finca", "Medellín", 3, 150.0));
        alojamientos.add(new Alojamiento("Día de Sol Caribe", "Día de Sol", "Cartagena", 4, 60.0));
    }

    // Inicia el menú principal
    public void iniciar() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Bienvenido a la aplicación de Booking:");
            System.out.println("1. Buscar alojamientos");
            System.out.println("2. Salir");
            System.out.print("Elige una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir nueva línea

            switch (opcion) {
                case 1:
                    buscarAlojamientos();
                    break;
                case 2:
                    System.out.println("¡Gracias por usar la aplicación!");
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    // Método para buscar alojamientos
    public void buscarAlojamientos() {
        Scanner scanner = new Scanner(System.in);

        // Pedir datos al usuario
        System.out.println("Búsqueda de Alojamientos:");
        System.out.print("Ciudad: ");
        String ciudad = scanner.nextLine();
        System.out.print("Tipo de alojamiento (Hotel, Apartamento, Finca, Día de Sol): ");
        String tipo = scanner.nextLine();
        System.out.print("Día de inicio del hospedaje (yyyy-mm-dd): ");
        String diaInicio = scanner.nextLine();
        System.out.print("Día de finalización del hospedaje (yyyy-mm-dd): ");
        String diaFin = scanner.nextLine();
        System.out.print("Cantidad de adultos: ");
        int adultos = scanner.nextInt();
        System.out.print("Cantidad de niños: ");
        int ninos = scanner.nextInt();
        System.out.print("Cantidad de habitaciones: ");
        int habitaciones = scanner.nextInt();

        // Buscar alojamientos
        List<Alojamiento> resultados = buscarAlojamientos(ciudad, tipo, diaInicio, diaFin, adultos, ninos, habitaciones);

        // Mostrar resultados
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron alojamientos que coincidan con los criterios.");
        } else {
            System.out.println("Alojamientos encontrados:");
            for (Alojamiento alojamiento : resultados) {
                System.out.println(alojamiento);
            }
        }
    }

    // Lógica de búsqueda
 // Método para buscar alojamientos
    public List<Alojamiento> buscarAlojamientos(String ciudad, String tipo, String diaInicio, String diaFin, 
                                                int adultos, int ninos, int habitaciones) {
        List<Alojamiento> resultados = new ArrayList<>();

        for (Alojamiento alojamiento : alojamientos) {
            if (alojamiento.getCiudad().equalsIgnoreCase(ciudad) &&
                alojamiento.getTipo().equalsIgnoreCase(tipo)) {
                // Aquí puedes agregar lógica adicional para disponibilidad y capacidad
                resultados.add(alojamiento);
            }
        }
        return resultados;
    }
}
