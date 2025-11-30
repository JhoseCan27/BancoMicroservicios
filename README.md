SISTEMA BANCARIO PARA LA PRUEBA EN DEVSU

El proyecto fue creado utilizando Java 17, Maven, Docker, PstgreSQL y Postman.

Para ejecutar el proyecto se tiene que tener Docker y docker compose instalados localmente y corriendo en el sistema, junto con Postman para hacer las pruebas. Lo primero
es iniciar el proyecto utilizando los siguientes comandos dentro de la carpeta del proyecto:

docker-compose up --build

Después, en Postman, importar el archivo Banco-Microservicios.postman_collections.json. En esa colección de pruebas están todas las transacciones que se pueden
hacer con el sistema.
