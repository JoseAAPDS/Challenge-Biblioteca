package com.literalura.literalura.repository;

import com.literalura.literalura.models.Autor;
import com.literalura.literalura.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Query("SELECT l FROM Autor a JOIN a.libros l")
    List<Libro> obtenerListadoLibros();

    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento < :anio AND :anio < a.anioFallecimiento")
    List<Autor> autorVivoEnDeterminadoAnio(int anio);

    @Query("SELECT l FROM Autor a JOIN a.libros l WHERE l.cantidadDescargas = (SELECT MAX(l2.cantidadDescargas) FROM Libro l2)")
    Libro libroMaximaCantidadDescargas();

    @Query("SELECT l FROM Autor a JOIN a.libros l WHERE l.cantidadDescargas = (SELECT MIN(l2.cantidadDescargas) FROM Libro l2)")
    Libro libroMinimaCantidadDescargas();

    @Query("SELECT l FROM Autor a JOIN a.libros l ORDER BY l.cantidadDescargas DESC LIMIT 10")
    List<Libro> top10LibrosMasDescargados();

    @Query("SELECT a FROM Autor a WHERE LOWER(a.nombre) LIKE LOWER(:nombre)")
    Optional<Autor> buscarAutorPorNombre(String nombre);

}
