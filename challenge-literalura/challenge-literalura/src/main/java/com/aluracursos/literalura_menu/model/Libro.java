package com.aluracursos.literalura_menu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    private String idioma;
    private Integer descargas;
                //Necessary annotation to save Libro without saving Autor first
    @ManyToOne //(cascade = CascadeType.PERSIST)
    private Autor author;

    public Libro() {}

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.idioma = datosLibro.idiomas().get(0);
        this.descargas = datosLibro.descargas();
    }

    public Autor getAuthor() {
        return author;
    }

    public void setAuthor(Autor author) {
        this.author = author;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public int getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        String nombreAutor = author.getAutor() == null ? "Autor: Dato no registrado" : "Autor: " + author.getAutor();
        return "\n----- LIBRO -----" +
                "\nTítulo: " + titulo +
                "\n" + nombreAutor +
                "\nIdioma: " + idioma +
                "\nNúmero de descargas: " + descargas +
                "\n-----------------";
    }
}
