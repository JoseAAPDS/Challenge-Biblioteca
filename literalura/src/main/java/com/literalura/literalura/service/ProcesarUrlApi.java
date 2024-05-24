package com.literalura.literalura.service;

import java.util.Random;

public class ProcesarUrlApi {
    private final String URL_BASE = "https://gutendex.com/books/";


    public String urlConsultaPorTituloOAutor(String titulo){
        return URL_BASE + "?search=" + titulo.replace(" ", "%20");
    }

    public String urlCantidadTotalLibros(){
        return URL_BASE + "?languages=en%2Ces%2Cpt";
    }

    public String urlSugerenciaLibros(int numero){
        Random numeroAleatorio = new Random();
        int pagina = numeroAleatorio.nextInt(numero) + 1;
        return URL_BASE + "?languages=en%2Ces%2Cpt&page=" + pagina;
    }
}
