package com.literaluraChallenge.repository;

import com.literaluraChallenge.model.Autor;
import com.literaluraChallenge.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Year;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Query("SELECT l FROM Libro l WHERE LOWER(l.titulo) = LOWER(:titulo)")
    List<Libro> findByTitulo(String titulo);

    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento <= :anio AND (a.anioDeceso IS NULL OR a.anioDeceso >= :anio)")
    List<Autor> findAutoresVivos(@Param("anio") Year ano);

    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento = :anio AND (a.anioDeceso IS NULL OR a.anioDeceso >= :anio)")
    List<Autor> findAutoresPorFechaNacimiento(@Param("anio") Year anio);
}
