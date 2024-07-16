package com.literaluraChallenge.model;

import jakarta.persistence.*;

@Entity
@Table
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne(cascade = CascadeType.ALL)
    private Autor autor;

    private String idioma;

    private Integer anioNacimientoAutor;

    private Integer anioDecesoAutor;

    private Double numeroDescargas;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getAnioNacimientoAutor() {
        return anioNacimientoAutor;
    }

    public void setAnioNacimientoAutor(Integer anioNacimientoAutor) {
        this.anioNacimientoAutor = anioNacimientoAutor;
    }

    public Integer getAnioDecesoAutor() {
        return anioDecesoAutor;
    }

    public void setAnioDecesoAutor(Integer anioDecesoAutor) {
        this.anioDecesoAutor = anioDecesoAutor;
    }

    public Double getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Double numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    // Constructores
    public Libro() {}

    public Libro(LibroDTO libroDTO) {
        this.titulo = libroDTO.titulo();
        Autor autor = new Autor(libroDTO.autores().get(0));
        this.autor = autor;
        this.idioma = libroDTO.idioma().get(0);
        this.numeroDescargas = libroDTO.numeroDescargas();
    }

    public Libro(Long idApi, String titulo, Autor autor, String idioma, Double numeroDescargas) {
        this.titulo = titulo;
        this.autor = autor;
        this.idioma = idioma;
        this.numeroDescargas = numeroDescargas;
    }

    @Override
    public String toString() {
        return "Título de libro: " + titulo + "\n" +
                "Autor: " + autor + "\n" +
                "Idioma: " + idioma + "\n" +
                "Total de descargas: " + numeroDescargas + "\n";
    }
}
