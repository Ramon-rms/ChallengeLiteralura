package com.literaluraChallenge.model;

import com.fasterxml.jackson.annotation.JsonAlias;


public record AutorDTO(@JsonAlias("name") String autor,
                       @JsonAlias("birth_year") Integer anioNacimiento,
                       @JsonAlias("death_year") Integer anioDesceso){
    @Override
    public String toString() {
        return "Autor: " + autor;
    }

}



