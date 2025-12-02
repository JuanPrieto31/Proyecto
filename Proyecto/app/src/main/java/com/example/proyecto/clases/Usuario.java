package com.example.proyecto.clases;
public class Usuario {
    private String email;
    private String password;
    private String nombre;
    private String fechaNacimiento;

    public Usuario(String nombre, String email, String password, String fechaNacimiento) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}