package src.controller;

import src.model.Alojamiento;
import src.model.Cliente;
import src.model.Habitacion;
import src.model.Reserva;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ApartamentoFincaController {

    private List<Alojamiento> alojamientos;
    private List<Reserva> reservas;

    public ApartamentoFincaController(List<Alojamiento> alojamientos, List<Reserva> reservas) {
        this.alojamientos = alojamientos;
        this.reservas = reservas;
    }

    public boolean buscarApartamentoFinca(String ciudad, String tipo, LocalDate fechaInicio, LocalDate fechaFin) {
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

    private long calcularDiasEstadia(LocalDate fechaInicio, LocalDate fechaFin) {
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
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
        double precioBase = calcularPrecioBase(alojamiento, diasEstadia);
        double ajuste = calcularAjuste(precioBase, precioFinal);

        mostrarDetallesAlojamiento(alojamiento, precioBase, ajuste, precioFinal);
    }

    // Cálculo del precio base
    private double calcularPrecioBase(Alojamiento alojamiento, long diasEstadia) {
        return alojamiento.getPrecioPorNoche() * diasEstadia;
    }

    // Cálculo del ajuste (diferencia entre precio final y base)
    private double calcularAjuste(double precioBase, double precioFinal) {
        return precioFinal - precioBase;
    }

    private void mostrarDetallesAlojamiento(Alojamiento alojamiento, double precioBase, double ajuste, double precioFinal) {
        mostrarInformacionBasicaAlojamiento(alojamiento);
        mostrarInformacionPrecios(precioBase, ajuste, precioFinal);
    }

    // Método auxiliar para mostrar la información básica del alojamiento
    private void mostrarInformacionBasicaAlojamiento(Alojamiento alojamiento) {
        System.out.println("Nombre: " + alojamiento.getNombre());
        System.out.println("Calificación: " + alojamiento.getCalificacion());
        System.out.println("Precio por noche: " + alojamiento.getPrecioPorNoche());
    }

    // Método auxiliar para mostrar los precios del alojamiento
    private void mostrarInformacionPrecios(double precioBase, double ajuste, double precioFinal) {
        System.out.println("Precio total (sin ajustes): " + precioBase);
        System.out.println((ajuste >= 0 ? "Aumento aplicado: " : "Descuento aplicado: ") + Math.abs(ajuste));
        System.out.println("Precio total (con ajustes): " + precioFinal);
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
}
