package com.aluracursos.literalura_menu.principal;

import com.aluracursos.literalura_menu.model.*;
import com.aluracursos.literalura_menu.repository.AutorRepository;
import com.aluracursos.literalura_menu.repository.LibroRepository;
import com.aluracursos.literalura_menu.service.ConsumoAPI;
import com.aluracursos.literalura_menu.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/";
    private final String SEARCH = "?search=";
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void ejecutarPrograma() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
            
            1 - Buscar libro por título para registrarlo en la base de datos
            2 - Listar libros registrados
            3 - Listar autores registrados
            4 - Listar autores registrados vivos en un determinado año
            5 - Listar libros registrados por idioma
            0 - Salir
            """;

            System.out.println(menu);
            try {
                opcion = Integer.valueOf(teclado.nextLine());

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresRegistradosVivosPorAno();
                        break;
                    case 5:
                        listarLibrosRegistradosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Finalizando la ejecución...");
                        break;
                    default:
                        System.out.println("Opción inválida");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el título del libro que desea buscar");
        var tituloLibro = teclado.nextLine();
        var consulta = consumoApi.obtenerDatos(URL_BASE + SEARCH + tituloLibro.replace(" ", "%20").replace("'", "%27"));
        DatosGenerales consultaDatosGenerales = conversor.convertirDatos(consulta, DatosGenerales.class);
        if (!consultaDatosGenerales.resultados().isEmpty()) {
            DatosLibro consultaDatosLibro = consultaDatosGenerales.resultados().get(0);
            Autor consultaAutor = new Autor(consultaDatosLibro);
            Libro consultaLibro = new Libro(consultaDatosLibro);
            //Basta con usar setLibros() porque en ese mismo método se usa setAuthor
            consultaAutor.setLibros(consultaLibro);
//            System.out.println(consultaDatosLibro);
            Optional<Libro> libroRegistrado = libroRepository.findByTituloContainsIgnoreCase(consultaLibro.getTitulo());
            if (libroRegistrado.isPresent()) {
                System.out.println("Libro ya registrado");
            } else {
                Optional<Autor> autorRegistrado = autorRepository.findByAutor(consultaAutor.getAutor());
                if (autorRegistrado.isPresent()) {
                    consultaLibro.setAuthor(autorRegistrado.get());
                    //Se guarda solo el Libro y se asocia a un Autor existente
                    libroRepository.save(consultaLibro);
                } else {
                    //Se guarda primero el Autor y luego el Libro
                    autorRepository.save(consultaAutor);
                    libroRepository.save(consultaLibro);
                }
                System.out.println(consultaLibro);
            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void listarLibrosRegistrados() {
        libroRepository.findAll().forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autor> listaAutores = autorRepository.findAll();
        listaAutores.forEach(System.out::println);
    }
    private void listarAutoresRegistradosVivosPorAno() {
        System.out.println("Ingrese el año que desea buscar");
        var ano = Integer.valueOf(teclado.nextLine());
        List<Autor> autoresVivos = autorRepository.listarAutoresRegistradosVivosPorAno(ano);
        if (autoresVivos.isEmpty()) {
            System.out.println("Ningún autor registrado");
        } else {
            autoresVivos.forEach(System.out::println);
        }
    }

    private void listarLibrosRegistradosPorIdioma() {
        List<String> idiomasRegistrados = libroRepository.findAll().stream()
                .map(l -> l.getIdioma())
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Seleccione uno de los idiomas registrados");
        idiomasRegistrados.forEach(System.out::println);
        var idiomaSeleccionado = teclado.nextLine();
        if (idiomasRegistrados.contains(idiomaSeleccionado)) {
            List<Libro> librosPorIdioma = libroRepository.findAll().stream()
                    .filter(l -> l.getIdioma().equals(idiomaSeleccionado))
                    .collect(Collectors.toList());
            librosPorIdioma.forEach(System.out::println);
        } else {
            System.out.println("Opción inválida");
        }
    }

}
