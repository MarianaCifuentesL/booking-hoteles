package src.controller;

import src.model.Alojamiento;
import src.model.Cliente;
import src.model.Habitacion;
import src.model.Reserva;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class HotelController {

    private List<Alojamiento> alojamientos;
    public static final String HABITACIONES_RESERVADAS = "Habitaciones reservadas:";
    private Cliente cliente;
    private List<Reserva> reservas;
    private HabitacionController habitacionController;

    public HotelController(List<Alojamiento> alojamientos, HabitacionController habitacionController, List<Reserva> reservas) {
        this.alojamientos = alojamientos; // Usa la lista compartida
        this.habitacionController = habitacionController; // Usa el controlador pasado
        this.reservas = reservas;
    }


    public boolean buscarHotel(String ciudad, LocalDate fechaInicio, LocalDate fechaFin, int cantidadAdultos, int cantidadNiños, int cantidadHabitaciones) {
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

    private double calcularPrecioBase(Alojamiento alojamiento, int cantidadHabitaciones, long diasEstadia) {
        return obtenerPrecioHabitacionMasSimple(alojamiento) * cantidadHabitaciones * diasEstadia;
    }

    private double obtenerPrecioHabitacionMasSimple(Alojamiento alojamiento) {
        return habitacionController.obtenerPrecioHabitacionMasSimple(alojamiento);
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

    private boolean verificarCapacidad(Alojamiento alojamiento, int cantidadAdultos, int cantidadNiños, int cantidadHabitaciones) {
        int totalPersonas = calcularTotalPersonas(cantidadAdultos, cantidadNiños);
        List<Habitacion> habitaciones = alojamiento.getHabitaciones();

        if (!validarCantidadHabitaciones(habitaciones, cantidadHabitaciones)) {
            return false;
        }

        List<List<Habitacion>> combinaciones = generarCombinaciones(habitaciones, cantidadHabitaciones);
        return existeCombinacionValida(combinaciones, totalPersonas);
    }

    private int calcularTotalPersonas(int cantidadAdultos, int cantidadNiños) {
        return cantidadAdultos + cantidadNiños;
    }

    // Método auxiliar para validar si hay suficientes habitaciones disponibles
    private boolean validarCantidadHabitaciones(List<Habitacion> habitaciones, int cantidadHabitaciones) {
        return habitaciones.size() >= cantidadHabitaciones;
    }

    // Método auxiliar para verificar si existe al menos una combinación válida
    private boolean existeCombinacionValida(List<List<Habitacion>> combinaciones, int totalPersonas) {
        for (List<Habitacion> combinacion : combinaciones) {
            if (esCombinacionValida(combinacion, totalPersonas)) {
                return true;
            }
        }
        return false;
    }

    // Método auxiliar para verificar si una combinación específica cumple con la capacidad requerida
    private boolean esCombinacionValida(List<Habitacion> combinacion, int totalPersonas) {
        int capacidadTotal = calcularCapacidadTotal(combinacion);
        return capacidadTotal >= totalPersonas;
    }

    // Método auxiliar para calcular la capacidad total de una combinación de habitaciones
    private int calcularCapacidadTotal(List<Habitacion> combinacion) {
        return combinacion.stream().mapToInt(Habitacion::getCapacidad).sum();
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

    private boolean esHotelEnCiudad(Alojamiento alojamiento, String ciudad) {
        return alojamiento.getCiudad().equalsIgnoreCase(ciudad) && alojamiento.getTipo().equalsIgnoreCase("Hotel");
    }

    private long calcularDiasEstadia(LocalDate fechaInicio, LocalDate fechaFin) {
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
    }

    private void mostrarInformacionHotel(Alojamiento alojamiento, double precioBase, double precioFinal, double ajuste) {
        mostrarDetallesGeneralesHotel(alojamiento);
        mostrarPreciosHotel(precioBase, precioFinal, ajuste);
    }

    // Método auxiliar para mostrar los detalles generales del hotel
    private void mostrarDetallesGeneralesHotel(Alojamiento alojamiento) {
        System.out.println("Nombre: " + alojamiento.getNombre());
        System.out.println("Calificación: " + alojamiento.getCalificacion());
        System.out.println("Precio por noche: " + obtenerPrecioHabitacionMasSimple(alojamiento));
    }

    // Método auxiliar para mostrar los precios del hotel
    private void mostrarPreciosHotel(double precioBase, double precioFinal, double ajuste) {
        System.out.println("Precio total (sin ajustes): " + precioBase);
        System.out.println((ajuste >= 0 ? "Aumento aplicado: " : "Descuento aplicado: ") + Math.abs(ajuste));
        System.out.println("Precio total (con ajustes): " + precioFinal);
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

    private void mostrarDetallesAlojamiento(Alojamiento alojamiento, double precioBase, double ajuste, double precioFinal) {
        mostrarInformacionBasicaAlojamiento(alojamiento);
        mostrarInformacionPrecios(precioBase, ajuste, precioFinal);
    }

    // Método auxiliar para mostrar la información básica del alojamiento
    private void mostrarInformacionBasicaAlojamiento(Alojamiento alojamiento) {
        System.out.println("Nombre: " + alojamiento.getNombre());
        System.out.println("Calificación: " + alojamiento.getCalificacion());
        System.out.println("Precio por noche: " + obtenerPrecioHabitacionMasSimple(alojamiento));
    }

    // Método auxiliar para mostrar los precios del alojamiento
    private void mostrarInformacionPrecios(double precioBase, double ajuste, double precioFinal) {
        System.out.println("Precio total (sin ajustes): " + precioBase);
        System.out.println((ajuste >= 0 ? "Aumento aplicado: " : "Descuento aplicado: ") + Math.abs(ajuste));
        System.out.println("Precio total (con ajustes): " + precioFinal);
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
            if (!procesarCambioDeHabitacion(reserva, scanner)) {
                break;
            }
        }
    }

    private boolean procesarCambioDeHabitacion(Reserva reserva, Scanner scanner) {
        mostrarHabitacionesActuales(reserva);
        String tipoSeleccionado = seleccionarHabitacionActual(reserva, scanner);
        if (tipoSeleccionado == null) {
            return false; // Salir del bucle
        }
        List<Habitacion> habitacionesDisponibles = obtenerHabitacionesDisponibles(reserva);
        if (validarDisponibilidadHabitaciones(habitacionesDisponibles)) {
            gestionarSeleccionNuevaHabitacion(reserva, tipoSeleccionado, habitacionesDisponibles, scanner);
        }

        return true; // Continuar el bucle
    }

    private boolean validarDisponibilidadHabitaciones(List<Habitacion> habitacionesDisponibles) {
        if (habitacionesDisponibles.isEmpty()) {
            mostrarMensajeNoHayHabitaciones();
            return false;
        }
        return true;
    }

    private void gestionarSeleccionNuevaHabitacion(Reserva reserva, String tipoSeleccionado,
                                                   List<Habitacion> habitacionesDisponibles, Scanner scanner) {
        mostrarHabitacionesDisponibles(habitacionesDisponibles, reserva);

        Habitacion nuevaHabitacion = seleccionarNuevaHabitacion(habitacionesDisponibles, scanner);
        if (nuevaHabitacion == null) {
            return;
        }
        int nuevaCantidad = validarCantidad(scanner, nuevaHabitacion, reserva);
        if (nuevaCantidad != -1) {
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

    private String obtenerEntrada(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
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

    private int validarSeleccion(String input, int maxOpciones) {
        int index = parsearEntrada(input);
        return esSeleccionValida(index, maxOpciones) ? index : manejarSeleccionInvalida();
    }

    // Método auxiliar para parsear la entrada como un entero
    private int parsearEntrada(String input) {
        try {
            return Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            return -1; // Valor inválido
        }
    }

    // Método auxiliar para verificar si la selección está dentro del rango válido
    private boolean esSeleccionValida(int index, int maxOpciones) {
        return index >= 0 && index < maxOpciones;
    }

    // Método auxiliar para manejar selecciones inválidas
    private int manejarSeleccionInvalida() {
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

    private void cambiarAlojamiento(Reserva reserva, Scanner scanner) {
        System.out.println("\nLa reserva actual será eliminada. Por favor, cree una nueva reserva desde el menú principal.");
        eliminarReserva(reserva);
    }

    public void eliminarReserva(Reserva reserva) {
        reservas.remove(reserva);
        System.out.println("La reserva ha sido eliminada.");
    }

    public void mostrarDetalleReserva(Reserva reserva) {
        if (!validarReserva(reserva)) {
            return;
        }
        mostrarInformacionCliente(reserva.getCliente());
        mostrarInformacionAlojamiento(reserva);
        mostrarHabitacionesReservadas(reserva.getHabitacionesPorTipo());
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

    // Método auxiliar para mostrar información del cliente
    private void mostrarInformacionCliente(Cliente cliente) {
        System.out.println("----------------------------------------------------");
        System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
        System.out.println("Email: " + cliente.getEmail());
    }

    // Método auxiliar para mostrar información del alojamiento y fechas
    private void mostrarInformacionAlojamiento(Reserva reserva) {
        System.out.println("Alojamiento: " + reserva.getAlojamiento().getNombre());
        System.out.println("Fechas: " + reserva.getFechaInicio() + " a " + reserva.getFechaFin());
    }

    // Método auxiliar para mostrar las habitaciones reservadas
    private void mostrarHabitacionesReservadas(Map<String, Integer> habitacionesPorTipo) {
        if (habitacionesPorTipo != null && !habitacionesPorTipo.isEmpty()) {
            System.out.println(HABITACIONES_RESERVADAS);
            habitacionesPorTipo.forEach((habitacionId, cantidad) ->
                    System.out.println("- Habitacion ID: " + habitacionId + ": " + cantidad + " habitación(es)")
            );
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

    public void reservarHotel(Cliente cliente, Alojamiento alojamiento, Integer cantidadAdultos, Integer cantidadNiños,
                              LocalDate fechaInicio, LocalDate fechaFin, Map<String, Integer> habitacionesPorTipo, String horaLlegada) {

        // Validar datos de entrada
        if (!validarEntrada(cliente, alojamiento)) return;
        // Map para almacenar habitaciones reservadas
        Map<Habitacion, Integer> habitacionesReservadas = new HashMap<>();
        // Validar y procesar cada tipo de habitación
        if (!habitacionController.procesarTiposDeHabitaciones(alojamiento, habitacionesPorTipo, fechaInicio, fechaFin, habitacionesReservadas))
            return;
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

    // Método auxiliar para crear una reserva general
    private void crearReservaGeneral(Cliente cliente, Alojamiento alojamiento, Integer cantidadAdultos, Integer cantidadNiños,
                                     LocalDate fechaInicio, LocalDate fechaFin, Map<String, Integer> habitacionesPorTipo, String horaLlegada) {
        Reserva reservaGeneral = new Reserva(cliente, alojamiento, fechaInicio, fechaFin, cantidadAdultos, cantidadNiños, habitacionesPorTipo, horaLlegada);
        reservas.add(reservaGeneral);
    }

    // Método auxiliar para mostrar la confirmación de reserva
    private void mostrarConfirmacion(Map<Habitacion, Integer> habitacionesReservadas) {
        System.out.println("Se ha realizado la reserva con éxito.");
//        System.out.println(HABITACIONES_RESERVADAS);
//        habitacionesReservadas.forEach((habitacion, cantidad) ->
//                System.out.println("- Tipo: " + habitacion.getTipo() + " | Cantidad: " + cantidad));
    }
}


