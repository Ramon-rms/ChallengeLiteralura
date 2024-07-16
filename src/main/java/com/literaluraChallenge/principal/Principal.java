package com.literaluraChallenge.principal;

import com.literaluraChallenge.model.Autor;
import com.literaluraChallenge.model.Libro;
import com.literaluraChallenge.model.LibroDTO;
import com.literaluraChallenge.repository.LibroRepository;
import com.literaluraChallenge.service.ConsumoAPI;
import com.literaluraChallenge.service.ConvierteDatos;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Principal {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private ConsumoAPI consumoAPI;

    @Autowired
    private ConvierteDatos convierteDatos;

    private final Scanner lectura = new Scanner(System.in);

    public Principal(LibroRepository libroRepository, ConsumoAPI consumoAPI, ConvierteDatos convierteDatos) {
        this.libroRepository = libroRepository;
        this.consumoAPI = consumoAPI;
        this.convierteDatos = convierteDatos;
    }

    public void ejecutar() {
        boolean corriendo = true;
        while (corriendo) {
            mostrarMenu();
            var opcion = lectura.nextInt();
            lectura.nextLine();

            switch (opcion) {
                case 1 -> buscarLibroPorTitulo();
                case 2 -> librosRegistrados();
                case 3 -> autoresRegistrados();
                case 4 -> listaAutoresVivosEnAnio();
                case 5 -> listaAutoresPorFechaNacimiento();
                //OPCIONES EXTRA POR IMPLEMENTAR
//                case 6 -> listarAutoresPorAnoDeMorte();
//                case 7 -> listarLivrosPorIdioma();
                case 0 -> {
                    System.out.println("...FINALIZANDO APLICACIÓN...");
                    corriendo = false;
                }
                default -> System.out.println("¡OPCIÓN NO VÁLIDA!");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("""
             ¡BIENVENIDO A LITERALURA! Seleccione una opción:
             
                       1- Buscar libro por título
                       2- Lista de libros
                       3- Lista de autores
                       4- Lista autores vivos por año dado
                       5- Lista autores por año de nacimiento
                      
                      ¡PROXIMAMENTE EN LITERALURA!
                      
                       6- Lista autores por fecha de deceso
                       7- Lista libros por idioma
                       
                       Pulsar 0 para salir de la aplicación
                       
            """);
    }

    private void guardarLibros(List<Libro> libros) {
        libros.forEach(libroRepository::save);
    }

// 1 -BUSCANDO LIBRO POR TÍTULO.
    private void buscarLibroPorTitulo() {
        String baseURL = "https://gutendex.com/books?search=";

        try {
            System.out.println("¿Cuál es el título del libro?");
            String titulo = lectura.nextLine();
            String complemento = baseURL + titulo.replace(" ", "%20");
            System.out.println("Dirección de solicitud: " + complemento);

            String jsonResponse = consumoAPI.obtenerDatos(complemento);
            System.out.println("Respuesta: " + jsonResponse);

            if (jsonResponse.isEmpty()) {
                System.out.println("Respuesta fallida");
                return;
            }
            // Si no se encuentra el libro en la API solicitada
            JsonNode rootNode = convierteDatos.getObjectMapper().readTree(jsonResponse);
            JsonNode resultsNode = rootNode.path("results");
            if (resultsNode.isEmpty()) {
                System.out.println("LIBRO NO ENCONTRADO");
                return;
            }

            // Mandando la respuesta a la DTO de libros:
            List<LibroDTO> librosDTO = convierteDatos.getObjectMapper()
                    .readerForListOf(LibroDTO.class)
                    .readValue(resultsNode);

            // Guardando los libros encontrados en la BDD
            if (!librosDTO.isEmpty()) {
                System.out.println("Guardando...");
                List<Libro> libroNuevo = librosDTO.stream().map(Libro::new).collect(Collectors.toList());
                guardarLibros(libroNuevo);
                System.out.println("Guardado completo.");
            } else {
                System.out.println("Guardado actualizado");
            }

            // Para mostrar la lista
            if (!librosDTO.isEmpty()) {
                System.out.println("Libros guardados:");
                Set<String> titulosExibidos = new HashSet<>();
                for (LibroDTO libro : librosDTO) {
                    if (!titulosExibidos.contains(libro.titulo())) {
                        System.out.println(libro);
                        titulosExibidos.add(libro.titulo());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar libro. Intente nuevamente con otro código:   " + e.getMessage());
        }
    }


// 2 - LISTANDO LOS LIBROS REGISTRADOS.
    private void librosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("Lista de libros vacía");
        } else {
            libros.forEach(System.out::println);
        }
    }
// 3 - LISTANDO AUTORES REGISTRADOS
    private void autoresRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("Lista de autores vacía");
        } else {
            libros.stream()
                    .map(Libro::getAutor)
                    .distinct()
                    .forEach(autor -> System.out.println(autor.getAutor()));
        }
    }
// 4 - AUTORES VIVOS EN AÑO DADO
    private void listaAutoresVivosEnAnio() {
        System.out.println("¿En qué año desea buscar?: ");
        Integer anio = lectura.nextInt();
        lectura.nextLine();

        Year year = Year.of(anio);

        List<Autor> autores = libroRepository.findAutoresVivos(year);
        if (autores.isEmpty()) {
            System.out.println("Ningún autor encontrado con ese año");
        } else {
            System.out.println("Lista de autores vivos en " + anio + ":\n");

            autores.forEach(autor -> {
                if(Autor.Anio(autor.getAnioNacimiento()) && Autor.Anio(autor.getAnioDeceso())){
                    String nombreAutor = autor.getAutor();
                    String anioNacimiento = autor.getAnioNacimiento().toString();
                    String anioDeceso = autor.getAnioDeceso().toString();
                    System.out.println(nombreAutor + " (" + anioNacimiento + " - " + anioDeceso + ")");
                }
            }
            );
        }
    }
// 5 - AUTORES POR FECHA DE NACIMIENTO
    private void listaAutoresPorFechaNacimiento() {
        System.out.println("¿En qué año desea buscar?");
        Integer anio = lectura.nextInt();
        lectura.nextLine();
        Year year = Year.of(anio);
        List<Autor> autores = libroRepository.findAutoresPorFechaNacimiento(year);
        if (autores.isEmpty()) {
            System.out.println("No hay autores de esa fecha");
        } else {
            System.out.println("Lista de autores nacidos en " + anio + ":\n");

            autores.forEach(autor -> {
                if(Autor.Anio(autor.getAnioNacimiento()) && Autor.Anio(autor.getAnioDeceso())){
                    String nombreAutor = autor.getAutor();
                    String anioNacimiento = autor.getAnioNacimiento().toString();
                    String anioDeceso = autor.getAnioDeceso().toString();
                    System.out.println(nombreAutor + " (" + anioNacimiento + " - " + anioDeceso + ")");

                }
            }
            );
        }
    }
}
