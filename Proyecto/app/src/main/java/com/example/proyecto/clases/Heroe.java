package com.example.proyecto.clases;

public class Heroe {

    private String nombre;
    private String poder;
    private String publicador;
    private String imagen;
    private String descripcion;
    private String comics;
    private String origen;

    public Heroe(String nombre, String poder, String publicador, String imagen,
                 String descripcion, String comics, String origen) {
        this.nombre = nombre;
        this.poder = poder;
        this.publicador = publicador;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.comics = comics;
        this.origen = origen;
    }

    // GETTERS
    public String getNombre() { return nombre; }
    public String getPoder() { return poder; }
    public String getPublicador() { return publicador; }
    public String getImagen() { return imagen; }
    public String getDescripcion() { return descripcion; }
    public String getComics() { return comics; }
    public String getOrigen() { return origen; }

    // SETTERS (opcional)
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPoder(String poder) { this.poder = poder; }
    public void setPublicador(String publicador) { this.publicador = publicador; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setComics(String comics) { this.comics = comics; }
    public void setOrigen(String origen) { this.origen = origen; }
}
