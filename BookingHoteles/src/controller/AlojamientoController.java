package src.controller;

import src.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlojamientoController {

    private List<Alojamiento> alojamientos;
    private List<Reserva> reservas;
    private ApartamentoFincaController apartamentoFincaController;
    private DiaDeSolController diaDeSolController;
    private HotelController hotelController;

    public AlojamientoController(List<Alojamiento> alojamientos, HotelController hotelController, List<Reserva> reservas) {
        this.alojamientos = alojamientos;
        this.reservas = reservas;
        this.hotelController = hotelController;
        this.diaDeSolController = new DiaDeSolController(alojamientos, reservas);
        this.apartamentoFincaController = new ApartamentoFincaController(alojamientos, reservas);
    }

    public List<Alojamiento> buscarAlojamientosPorCiudadYTipo(String ciudad, String tipo) {
        return alojamientos.stream()
                .filter(alojamiento -> alojamiento.getCiudad().equalsIgnoreCase(ciudad))
                .filter(alojamiento -> esDelTipo(alojamiento, tipo))
                .collect(Collectors.toList());
    }

    private boolean esDelTipo(Alojamiento alojamiento, String tipo) {
        return switch (tipo.toLowerCase()) {
            case "hotel" -> alojamiento instanceof Hotel;
            case "día de sol" -> alojamiento instanceof DiaDeSol;
            case "apartamento" -> alojamiento instanceof Apartamento;
            case "finca" -> alojamiento instanceof Finca;
            default -> false;
        };
    }

    public void buscarAlojamientos(String ciudad, String tipo, LocalDate fechaInicio, LocalDate fechaFin, int cantidadAdultos, int cantidadNiños, int cantidadHabitaciones) {
        System.out.println("\n--- Resultados de búsqueda para: " + tipo + " en " + ciudad + " ---");
        boolean encontrado = false;
        switch (tipo.toLowerCase()) {
            case "día de sol" -> encontrado = diaDeSolController.buscarDiaDeSol(ciudad, cantidadAdultos, cantidadNiños);
            case "apartamento", "finca" -> encontrado = apartamentoFincaController.buscarApartamentoFinca(ciudad, tipo, fechaInicio, fechaFin);
            case "hotel" -> encontrado = hotelController.buscarHotel(ciudad, fechaInicio, fechaFin, cantidadAdultos, cantidadNiños, cantidadHabitaciones);
            default -> System.out.println("Tipo de alojamiento no reconocido.");
        }
        if (!encontrado) {
            System.out.println("No se encontraron alojamientos que cumplan con los criterios.");
        }
    }

    public Alojamiento buscarAlojamientoPorNombre(String nombreAlojamiento) {
        return alojamientos.stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombreAlojamiento))
                .findFirst()
                .orElse(null);
    }
//
//    public Alojamiento buscarAlojamientoPorReserva(Reserva reserva) {
//        // Buscar el alojamiento a partir de la reserva, considerando tanto las habitaciones como el alojamiento
//        return alojamientos.stream()
//                .filter(a -> a.equals(reserva.getAlojamiento()) && // Comparar directamente con el objeto Alojamiento
//                        a.getHabitaciones().stream().anyMatch(h -> h.getReservas().contains(reserva)))
//                .findFirst()
//                .orElse(null);
//    }


}