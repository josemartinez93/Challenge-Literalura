package com.aluracursos.literalura_menu.service;

public interface IConvierteDatos {
    <T> T convertirDatos(String json, Class<T> clase);
}
