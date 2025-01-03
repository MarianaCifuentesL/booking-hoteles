package src.manager;

import src.model.Alojamiento;
import src.model.Hotel;
import src.model.Finca;
import src.model.DiaDeSol;
import src.model.Apartamento;
import src.main.BookingApp;

import java.util.ArrayList;
import java.util.List;

public class AlojamientoManager {

    // 1. Instancia privada estática
    private static AlojamientoManager instancia;

    // Lista de alojamientos
    private List<Alojamiento> alojamientos;

    // 2. Constructor privado para evitar instanciación externa
    private AlojamientoManager() {
        alojamientos = new ArrayList<>();
    }

    // 3. Método público estático para obtener la instancia única
    public static AlojamientoManager getInstancia() {
        if (instancia == null) {
            instancia = new AlojamientoManager();
        }
        return instancia;
    }

    // 4. Método para inicializar los alojamientos
    public void inicializarAlojamientos(BookingApp app) {
        // Inicializar Hotel Quindío
        Hotel hotel1 = new Hotel("Hotel Quindío", "Armenia", 4, 200000.0);
        hotel1.agregarHabitacion("Sencilla", 100000.0, "Cama sencilla, WiFi, Aire Acondicionado, TV", 2, 3);
        hotel1.agregarHabitacion("Doble", 150000.0, "2 camas dobles, Vista al mar, Aire acondicionado, Cafetera, TV de pantalla plana, ducha, escritorio", 4, 2);
        hotel1.agregarHabitacion("Triple", 200000.0, "3 camas, Balcón, Mini bar, WiFi, Caja fuerte, TV", 3, 2);
        hotel1.agregarHabitacion("Suite", 350000.0, "1 cama King Size, Jacuzzi, Cocina equipada, Sala de estar, Vista panorámica", 2, 2);
        hotel1.agregarHabitacion("Familiar", 250000.0, "4 camas, Espacio amplio, Área de juegos, Aire acondicionado, Cocina pequeña", 6, 2);

        // Inicializar Finca Campestre
        Finca finca1 = new Finca("Finca Campestre", "Armenia", 5, 250000.0);

        // Instancias para Día de Sol
        DiaDeSol diaDeSol1 = new DiaDeSol("Resort Caribe Día de Sol", "Cartagena", 5, 120000.0,
                List.of("Natación", "Kayak", "Spa", "Senderismo"), true, true);
        DiaDeSol diaDeSol2 = new DiaDeSol("EcoParque Aventura", "Medellín", 4, 80000.0,
                List.of("Tirolesa", "Caminata ecológica", "Paintball"), false, true);
        DiaDeSol diaDeSol3 = new DiaDeSol("Club Campestre Día Relax", "Cali", 3, 90000.0,
                List.of("Piscina", "Yoga al aire libre", "Tenis"), true, false);

        // Inicializar Apartamento
        Apartamento apartamento = new Apartamento("Apartamentos Cibeles", "Armenia", 5, 200000.0);

        // Agregar alojamientos al sistema
        alojamientos.add(hotel1);
        alojamientos.add(finca1);
        alojamientos.add(diaDeSol1);
        alojamientos.add(diaDeSol2);
        alojamientos.add(diaDeSol3);
        alojamientos.add(apartamento);

        // Agregar alojamientos al BookingApp
        for (Alojamiento alojamiento : alojamientos) {
            app.agregarAlojamiento(alojamiento);
        }
    }

    // Método para obtener los alojamientos
    public List<Alojamiento> getAlojamientos() {
        return alojamientos;
    }
}
