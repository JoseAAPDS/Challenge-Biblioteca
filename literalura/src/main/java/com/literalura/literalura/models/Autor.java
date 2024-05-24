package com.literalura.literalura.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String nombre;
    private Integer anioNacimiento;
    private Integer anioFallecimiento;
    @OneToMany (mappedBy = "autor", cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    //Constructores
    public Autor() {
    }

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.anioNacimiento = (datosAutor.anioNacimiento() == null) ? 0 : Integer.valueOf(datosAutor.anioNacimiento());
        this.anioFallecimiento = (datosAutor.anioFallecimiento() == null) ? 0 : Integer.valueOf(datosAutor.anioFallecimiento());
    }

    public Autor(String anonimo, int anioNacimiento, int anioFallecimiento) {
        this.nombre = anonimo;
        this.anioNacimiento = anioNacimiento;
        this.anioFallecimiento = anioFallecimiento;
    }

    //Métodos
    @Override
    public String toString() {
        List<String> libros = getLibros().stream()
                .map(Libro::getTitulo).collect(Collectors.toList());

        return "Autor: " + getNombre() + "\n" +
               "Año de Nacimiento: " + getAnioNacimiento() + "\n" +
               "Año de Fallecimiento: " + getAnioFallecimiento() + "\n" +
               "Libros: " + libros + "\n";
    }

    //Getters & Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(Integer anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public Integer getAnioFallecimiento() {
        return anioFallecimiento;
    }

    public void setAnioFallecimiento(Integer anioFallecimiento) {
        this.anioFallecimiento = anioFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(DatosLibro libro) {
        Libro libroAGuardar = new Libro(libro);
        libroAGuardar.setAutor(this);
        this.libros.add(libroAGuardar);
    }
}
