package com.literalura.literalura.models;

import jakarta.persistence.*;

import java.text.DecimalFormat;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    private String genero;
    private String idioma;
    private Long cantidadDescargas;
    @ManyToOne
    private Autor autor;

    //Constructores
    public Libro() {
    }

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.genero = datosLibro.genero().get(0);
        this.idioma = datosLibro.lenguajes().get(0);
        this.cantidadDescargas = datosLibro.cantidadDescargas();
    }

    //Métodos
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("###,###,###,###");
        return  "********** LIBRO **********" + "\n" +
                "Título: " + getTitulo() + "\n" +
                "Autor: " + getAutor().getNombre() + "\n" +
                "Idioma: " + getIdioma() + "\n" +
                "Cantidad de descargas: " + df.format(getCantidadDescargas()) + "\n";
    }

    //Getters & Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Long getCantidadDescargas() {
        return cantidadDescargas;
    }

    public void setCantidadDescargas(Long cantidadDescargas) {
        this.cantidadDescargas = cantidadDescargas;
    }
}
