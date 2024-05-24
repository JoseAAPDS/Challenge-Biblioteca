# Challenge-Biblioteca

## Descripción
El proyecto consiste en consumir una API para obtener datos de libros y luego persistir dicha información en una base de datos.   El proyecto se centra en la recuperación de información a través de la API y de la conexión a una base de datos relacional, por lo tanto es un proyecto de línea de comandos . El proyecto es un desafio que es parte de la formación **Java y Spring Boot**, impartido por **Alura Latam** como parte del programa **Oracle Next Education**.  El objetivo es profundizar y poner en practia los conocimentos adquirido en los cursos como:
  - Uso de Spring Boot.
  - Uso de los repositorios Spring Data JPA y el driver de Postgresql.
  - Modelar relaciones entre entidades, uso de Hibernate para el mapeo.  - 
  - Uso de la biblioteca Jackson para deserializar los datos.
  - Uso de funciones lambda y streams.
  - Uso de querries JPQL.

## Funcionalidades básicas (requeridas para pasar el desafío)
  - Buscar libro por título, se busca la información del libro a través de la API y se registra en la base de datos.  Los libros se registran una vez, no se permiten duplicados. Cada 
    autor sin embargo, si puede tener varios libros asociados.
  - Listar los libros registrados en la base de datos.
  - Listar los autores registrados.
  - Listar los autores vivos en un año especificado por el usuario.
  - Listar los libros por idioma.
  
  ### Funcionalidades adicionales
  - Generar estadísticas. Muestra el promedio de descargas, y el libro con más descargas y el libro con menos descargas.
  - Lista de los diez libro más descargados.
  - Buscar el autor por nombre.  Busca en la base de datos o en la API, si no encuentra el autor en la base de datos.
  - Sugerir libros.  Busca en la API y muestra una lista de cinco libros aleatorios.  

## Entidades y Tablas

![literalura_tables](https://github.com/JoseAAPDS/Challenge-Biblioteca/assets/147453435/0ef01590-b802-403c-90db-dda7d5d42a80)

## Autor

  ### José Armando Acevedo
  
  www.linkedin.com/in/josé-acevedo-pilz-136179246
