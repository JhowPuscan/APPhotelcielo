package com.example.hotelcielo;
public class Reserva {
    private int id;
    private int clienteId;
    private int habitacionId;
    private String fechaReserva;  // Fecha y hora de la reserva
    private String fechaEntrada;  // Fecha y hora de entrada
    private String fechaSalida;   // Fecha y hora de salida
    private String estado;        // Estado de la reserva
    private double montoTotal;    // Monto total de la reserva

    // Constructor
    public Reserva(int id, int clienteId, int habitacionId, String fechaReserva, String fechaEntrada, String fechaSalida, String estado, double montoTotal) {
        this.id = id;
        this.clienteId = clienteId;
        this.habitacionId = habitacionId;
        this.fechaReserva = fechaReserva;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estado = estado;
        this.montoTotal = montoTotal;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getHabitacionId() {
        return habitacionId;
    }

    public void setHabitacionId(int habitacionId) {
        this.habitacionId = habitacionId;
    }

    public String getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }
}