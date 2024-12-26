package model;

public class Reserva {
    private String nombreCliente;
    private String email;
    private String nacionalidad;
    private int cantidadHabitaciones;
    private String alojamientoReservado;
    private double precioTotal;

    public Reserva(String nombreCliente, String email, String nacionalidad, int cantidadHabitaciones, String alojamientoReservado, double precioTotal) {
        this.nombreCliente = nombreCliente;
        this.email = email;
        this.nacionalidad = nacionalidad;
        this.cantidadHabitaciones = cantidadHabitaciones;
        this.alojamientoReservado = alojamientoReservado;
        this.precioTotal = precioTotal;
    }


    // Métodos Getters y Setters
    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public int getCantidadHabitaciones() {
        return cantidadHabitaciones;
    }

    public void setCantidadHabitaciones(int cantidadHabitaciones) {
        this.cantidadHabitaciones = cantidadHabitaciones;
    }

    public String getAlojamientoReservado() {
        return alojamientoReservado;
    }

    public void setAlojamientoReservado(String alojamientoReservado) {
        this.alojamientoReservado = alojamientoReservado;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
    

    public void mostrarReserva() {
        System.out.println("Reserva realizada por: " + nombreCliente);
        System.out.println("Alojamiento: " + alojamientoReservado + " | Habitaciones: " + cantidadHabitaciones);
        System.out.println("Precio total: " + precioTotal);
    }
}
