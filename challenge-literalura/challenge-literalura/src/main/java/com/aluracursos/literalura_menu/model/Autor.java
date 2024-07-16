package com.aluracursos.literalura_menu.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String autor;
    private Integer fechaDeNacimiento;
    private Integer fechaDeFallecimiento;
    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER) //cascade = CascadeType.ALL
    private List<Libro> libros;

    public Autor() {}

    public Autor(DatosLibro datosLibro) {
        if (datosLibro.autor().isEmpty()) {
            this.autor = null;
            this.fechaDeNacimiento = null;
            this.fechaDeFallecimiento = null;
        } else {
            this.autor = datosLibro.autor().get(0).nombre();
            this.fechaDeNacimiento = datosLibro.autor().get(0).fechaDeNacimiento();
            this.fechaDeFallecimiento = datosLibro.autor().get(0).fechaDeFallecimiento();
        }
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(Libro libro) {
        libro.setAuthor(this);
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

//    public String regresar() {
//        String nombreAutor = autor == null ? "Autor: Dato no registrado" : "Autor: " + autor;
//        String fechaInicial = fechaDeNacimiento == null ? "Fecha de nacimiento: Dato no registrado" : "Fecha de nacimiento: " + fechaDeNacimiento;
//        String fechaFinal = fechaDeFallecimiento == null ? "Fecha de fallecimiento: Dato no registrado" : "Fecha de fallecimiento: " + fechaDeFallecimiento;
//    }

    @Override
    public String toString() {
        String nombreAutor = autor == null ? "Autor: Dato no registrado" : "Autor: " + autor;
        String fechaInicial = fechaDeNacimiento == null ? "Fecha de nacimiento: Dato no registrado" : "Fecha de nacimiento: " + fechaDeNacimiento;
        String fechaFinal = fechaDeFallecimiento == null ? "Fecha de fallecimiento: Dato no registrado" : "Fecha de fallecimiento: " + fechaDeFallecimiento;
        return "\n" + nombreAutor +
                "\n" + fechaInicial +
                "\n" + fechaFinal +
                "\nLibros: " + libros.stream().map(l -> l.getTitulo()).collect(Collectors.toList());
    }
}
