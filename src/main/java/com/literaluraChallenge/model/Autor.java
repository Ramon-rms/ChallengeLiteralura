package com.literaluraChallenge.model;

import jakarta.persistence.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String autor;
    private Year anioNacimiento;
    private Year anioDeceso;

//    @Column(nullable = false)
//    private String autor;
//
//    @Column(name = "ano_nascimento")
//    private Year anoNascimento;
//
//    @Column(name = "ano_falecimento")
//    private Year anoFalecimento;

    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Year getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(Year anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public Year getAnioDeceso() {
        return anioDeceso;
    }

    public void setAnioDeceso(Year anioDeceso) {
        this.anioDeceso = anioDeceso;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    // Constructores
    public Autor() {}

    public static boolean Anio(Year anio) {
        return anio != null && !anio.equals(Year.of(0));
    }

    public Autor(AutorDTO autorDTO) {
        this.autor = autorDTO.autor();
        this.anioNacimiento = autorDTO.anioNacimiento() != null ? Year.of(autorDTO.anioNacimiento()) : null;
        this.anioDeceso = autorDTO.anioDesceso() != null ? Year.of(autorDTO.anioDesceso()) : null;
    }


    public Autor(String autor, Year anioNacimiento, Year anioDeceso) {
        this.autor = autor;
        this.anioNacimiento = anioNacimiento;
        this.anioDeceso = anioDeceso;
    }

    @Override
    public String toString() {
        String anoNascimentoStr = anioNacimiento != null ? anioNacimiento.toString() : "Año de nacimiento del autor desconocido";
        String anoFalecimentoStr = anioDeceso != null ? anioDeceso.toString() : "Año de deceso del autor desconocido";

        return "Autor: " + autor + " (nacido el año " + anioNacimiento + ", falecido em " + anioDeceso + ")";
    }
}
