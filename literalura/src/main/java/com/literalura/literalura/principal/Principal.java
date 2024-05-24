package com.literalura.literalura.principal;

import com.literalura.literalura.models.*;
import com.literalura.literalura.repository.AutorRepository;

import com.literalura.literalura.service.ConsultarAPI;
import com.literalura.literalura.service.ConvierteJson;
import com.literalura.literalura.service.ProcesarUrlApi;
import org.springframework.dao.DataIntegrityViolationException;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.floor;

public class Principal {
    private Scanner ingresoTeclado = new Scanner(System.in);
    private ConsultarAPI consultaApi = new ConsultarAPI();
    private ProcesarUrlApi procesarUrlApi = new ProcesarUrlApi();
    private ConvierteJson conversor = new ConvierteJson();
    private AutorRepository autorRepositorio;
    private List<Autor> autoresBaseDatos;
    private List<Libro> librosBaseDatos;
    private String menu = """

            ******* Menú *******

            1- Buscar libro por título.
            2- Listar libros buscados.
            3- Listar autores.
            4- Listar autores vivos en determinado año.
            5- Listar libros por idioma.
            6- Estadísticas.
            7- Top 10 libros más descargados.
            8- Buscar autor por nombre.
            9- Sugerir libros. 
            0- Salir.

            ********************
            """;

    public Principal(AutorRepository repository){
        this.autorRepositorio = repository;
    }

    public void correrMenu(){
        int opcion = 1;

        while (opcion != 0) {

            try {
                System.out.println(menu);
                System.out.println("Ingresa el número de la opción");
                opcion = ingresoTeclado.nextInt();
                ingresoTeclado.nextLine();

                switch (opcion) {
                    case 1:
                        obtenerNombreLibro();
                        break;
                    case 2:
                        obtenerLibrosBuscados();
                        break;
                    case 3:
                        obtenerAutores();
                        break;
                    case 4:
                        obtenerAutoresVivos();
                        break;
                    case 5:
                        obtenerIdioma();
                        break;
                    case 6:
                        obtenerEstadisticas();
                        break;
                    case 7:
                        obtenerTop10LibrosDescargados();
                        break;
                    case 8:
                        obtenerAutorPorNombre();
                        break;
                    case 9:
                        obtenerSugerenciasDeLibros();
                        break;
                    case 0:
                        opcion = 0;
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción incorrecta, intenta de nuevo \n");
                }
            }
            catch (InputMismatchException e){
                System.out.println("Opción incorrecta, intenta de nuevo");
                ingresoTeclado.nextLine();
            }
        }
    }

    private void obtenerNombreLibro() {
        System.out.println("Ingresa el titulo del libro que quieres buscar");
        String solicitudUsuario = ingresoTeclado.nextLine();
        System.out.println("");

        //Obtener url para enviar solicitud a la API
        String url = procesarUrlApi.urlConsultaPorTituloOAutor(solicitudUsuario);

        //Obtener la respuesta de la API en formato json
        String json = consultaApi.obtenerJson(url);

        //Deserializar los datos del archivo json
        Resultados resultado = conversor.convierteJsonAClase(json, Resultados.class);
        //Comprobar si se enontró algún libro en los resultados
        if (resultado.numeroResultados() > 0){
            //Comprobar que el libro obtenido tenga datos de autor validos
            validarDatosAutor(resultado);
        } else {
            System.out.println("*** Libro no encontrado  *** \n");
        }
    }

    private void validarDatosAutor(Resultados resultado) {
        Autor autorRevisado;

        //Obtiener datos del libro
        DatosLibro libroBuscadoWeb = resultado.listaLibros().get(0);
        //Obtener datos del autor
        Optional<DatosAutor> autorResultado = libroBuscadoWeb.autores().stream().findFirst();
        //Comprobar que los datos de autor no estén vacios
        if (autorResultado.isPresent()) {
            autorRevisado = new Autor(autorResultado.get());
            autorRevisado.setLibros(libroBuscadoWeb);
            guardarEnBaseDatos(autorRevisado, libroBuscadoWeb);
        } else {
            //en caso que el libro no tenga autor
            autorRevisado = new Autor("Anonymous", 0, 0);
            autorRevisado.setLibros(libroBuscadoWeb);
            guardarEnBaseDatos(autorRevisado, libroBuscadoWeb);
        }
        System.out.println("\n" + autorRevisado);
    }

    private Optional<Autor> revisarExistenciaAutorBaseDatos(Autor autorARevisar){
        autoresBaseDatos = autorRepositorio.findAll();
        Optional<Autor> autorBaseDatos = autoresBaseDatos.stream()
                .filter(a -> a.getNombre().toLowerCase()
                        .equals(autorARevisar.getNombre().toLowerCase()))
                .findAny();

        return autorBaseDatos;
    }

    private void guardarEnBaseDatos(Autor autorRevisado, DatosLibro libroBuscadoWeb){

        //Comprobar si el autor ya están en la base de datos
        Optional<Autor> autorEnBaseDatos = revisarExistenciaAutorBaseDatos(autorRevisado);

        //El bloque try detecta si se trata de guardar en la base de datos un libro que ya existe.
        try{
            if (autorEnBaseDatos.isPresent()){
                //Si el autor ya están en la base de datos únicamente se agrega el libro nuevo
                Autor autorExistente = autorEnBaseDatos.get();
                autorExistente.setLibros(libroBuscadoWeb);
                autorRepositorio.save(autorExistente);
                System.out.println("************************");
                System.out.println("Libro guardado con exito");
            } else {
                //El autor no está en la base de datos, entonces se agrega el libro y autor
                autorRepositorio.save(autorRevisado);
                System.out.println("*********************************");
                System.out.println("Libro y autor guardados con exito");
            }
        }
        catch(DataIntegrityViolationException e){
            System.out.println();
            System.out.println("**************************************");
            System.out.println("El libro ya existe en la base de datos");
        }

    }

    private void obtenerLibrosBuscados() {
        librosBaseDatos = autorRepositorio.obtenerListadoLibros();
        System.out.println();
        System.out.println("Lista de libros guardados en la Base de Datos");
        System.out.println();
        librosBaseDatos.forEach(System.out::println);

    }

    private void obtenerAutores() {
        autoresBaseDatos = autorRepositorio.findAll();
        System.out.println();
        System.out.println("Listado de Autores en la Base de Datos");
        System.out.println("**************************************");
        autoresBaseDatos.forEach(System.out::println);

    }

    private void obtenerAutoresVivos() {

        try {
            System.out.println("Ingrese el año para el cuál desea buscar autores vivos");
            int anio = ingresoTeclado.nextInt();
            List<Autor> autoresBaseDatos = autorRepositorio.autorVivoEnDeterminadoAnio(anio);

            //Excluir autores con nacimiento o fallecimiento cero
            var autoresFiltrados = autoresBaseDatos.stream()
                    .filter(a -> a.getAnioNacimiento() != 0 && a.getAnioFallecimiento() != 0)
                    .collect(Collectors.toList());

            if (autoresFiltrados.isEmpty()){
                System.out.println();
                System.out.println("*** No se encontró ningún autor vivo en el año " + anio + " ***");
            } else {
                System.out.println();
                System.out.println("Autores vivos en el año " + anio);
                System.out.println("******************************");
                autoresFiltrados.forEach(System.out::println);
            }
        }
        catch (InputMismatchException e){
            System.out.println();
            System.out.println("Año ingresado no es válido");
            ingresoTeclado.nextLine();
        }

    }

    private void obtenerIdioma() {

        try {
            System.out.println("Ingrese la opcion para el idioma que desea buscar");
            System.out.println(("""
                    1- Inglés
                    2- Español
                    3- Portugués
                    """));
            int opcionIdioma = ingresoTeclado.nextInt();
            switch (opcionIdioma){
                case 1:
                    obtenerLibrosPorIdioma("en", "Inglés");
                    break;
                case 2:
                    obtenerLibrosPorIdioma("es", "Español");
                    break;
                case 3:
                    obtenerLibrosPorIdioma("pt", "Portugués");
                    break;
                default:
                    System.out.println("*** opción incorrecta ***");
            }

        }
        catch (InputMismatchException e){
            System.out.println();
            System.out.println("*** opción incorrecta ***");
            ingresoTeclado.nextLine();
        }

    }

    private void obtenerLibrosPorIdioma(String idiomaAbreviado, String idioma){
        librosBaseDatos = autorRepositorio.obtenerListadoLibros();
        List<Libro> librosPorIdioma = librosBaseDatos.stream()
                .filter(l-> l.getIdioma().equals(idiomaAbreviado))
                .collect(Collectors.toList());
        //List<Libro> librosPorIdioma = libroRepositorio.buscarLibroPorIdioma(idiomaAbreviado);

        if (librosPorIdioma.isEmpty()){
            System.out.println();
            System.out.println("*** No se encontró ningún libro con el idioma " + idioma + " ***");
        } else {
            System.out.println();
            System.out.println("Listado de libros en " + idioma);
            librosPorIdioma.forEach(System.out::println);
        }

    }

    private void obtenerEstadisticas() {
        librosBaseDatos = autorRepositorio.obtenerListadoLibros();
        List<Long> listadoDeDescargas = librosBaseDatos.stream()
                .map(Libro::getCantidadDescargas)
                .collect(Collectors.toList());

        DoubleSummaryStatistics stats = listadoDeDescargas.stream()
                .mapToDouble(Long::doubleValue).summaryStatistics();

        DecimalFormat df = new DecimalFormat("###,###,###,###.##");
        System.out.println();
        System.out.println("Resumen de Estadísticas");
        System.out.println("***********************");
        System.out.println("Promedio de Descargas: " + df.format(stats.getAverage()) + "\n" +
                "Máximo de Descargas: " + df.format(stats.getMax()) + "\n" +
                "Mínimo de Descargas: " + df.format(stats.getMin()) + "\n");

        Libro libroMasDescargado = autorRepositorio.libroMaximaCantidadDescargas();
        Double porcentajeDescargas = (stats.getMax()/stats.getSum()) * 100;
        System.out.printf("Libro más descargado (%.02f%s de las descargas)\n", porcentajeDescargas, "%");
        System.out.println(libroMasDescargado);

        Libro libroMenosDescargado = autorRepositorio.libroMinimaCantidadDescargas();
        porcentajeDescargas = (stats.getMin()/ stats.getSum()) * 100;
        System.out.printf("Libro menos descargado (%.02f%s de las descargas)\n", porcentajeDescargas, "%");
        System.out.println(libroMenosDescargado);

    }
    private void obtenerTop10LibrosDescargados() {
        librosBaseDatos = autorRepositorio.top10LibrosMasDescargados();
        System.out.println();
        System.out.println("Top 10 Libros más descargados");
        librosBaseDatos.forEach(System.out::println);
    }

    private void obtenerAutorPorNombre() {
        System.out.println("Ingresa el nombre del autor que quieres buscar");
        String solicitudUsuario = ingresoTeclado.nextLine();
        System.out.println("");

        //Revisar existencia del autor en la base de datos
        Optional<Autor> autorBaseDatos = autorRepositorio.buscarAutorPorNombre("%"+solicitudUsuario+"%");
        if (autorBaseDatos.isPresent()){
            System.out.println("*** Autor Encontrado ***");
            System.out.println("************************");
            System.out.println(autorBaseDatos.get());
        } else {
            //Buscar en la API
            //Obtener url para enviar solicitud a la API
            String url = procesarUrlApi.urlConsultaPorTituloOAutor(solicitudUsuario);
            //Obtener la respuesta de la API en formato json
            String json = consultaApi.obtenerJson(url);
            //Deserializar los datos del archivo json
            Resultados resultado = conversor.convierteJsonAClase(json, Resultados.class);

            //Comprobar si se enontró algún resultado
            if (resultado.numeroResultados() > 0){
                //Obtiener datos del libro
                DatosLibro libroBuscadoWeb = resultado.listaLibros().get(0);
                //Obtener datos del autor
                Optional<DatosAutor> autorResultado = libroBuscadoWeb.autores().stream().findFirst();
                if (autorResultado.isPresent()){
                    Autor autorBuscadoWeb = new Autor(autorResultado.get());
                    autorBuscadoWeb.setLibros(libroBuscadoWeb);
                    System.out.println("*** Autor Encontrado ***");
                    System.out.println("************************");
                    System.out.println(autorBuscadoWeb);
                    autorRepositorio.save(autorBuscadoWeb);
                    System.out.println("*********************************");
                    System.out.println("Libro y autor guardados con exito");

                } else {
                    System.out.println("*** Autor no encontrado  *** \n");
                }
            } else {
                System.out.println("*** Autor no encontrado  *** \n");
            }
        }
    }

    private void obtenerSugerenciasDeLibros() {
        System.out.println("Procesando solicitud...");
        //Obtener de la API el número total de libros
        String url = procesarUrlApi.urlCantidadTotalLibros();
        String json = consultaApi.obtenerJson(url);
        Resultados resultados = conversor.convierteJsonAClase(json, Resultados.class);
        int totalLibros = (int) floor(resultados.numeroResultados() / 128 );


        //Obtener un listado aleatorio de libros
        url = procesarUrlApi.urlSugerenciaLibros(totalLibros);
        json = consultaApi.obtenerJson(url);
        resultados = conversor.convierteJsonAClase(json, Resultados.class);

        //Mostrar 5 libros del listado de libros
        List<DatosLibro> listadoLibros = resultados.listaLibros();

        System.out.println("*** Libros Sugeridos ***");
        System.out.println("");
        for (int i = 0; i < 5 ; i++){
            Libro libroSugerido = new Libro(listadoLibros.get(i));
            if (listadoLibros.get(i).autores().isEmpty()){
                Autor autorLibroSugerido = new Autor("",0,0);
                libroSugerido.setAutor(autorLibroSugerido);
                System.out.println(libroSugerido);
            } else {
                Autor autorLibroSugerido = new Autor(listadoLibros.get(i).autores().get(0));
                libroSugerido.setAutor(autorLibroSugerido);
                System.out.println(libroSugerido);
            }
        }
    }
}
