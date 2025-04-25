# TaskManager
# Gestor de Tareas
Esta aplicación de gestión de tareas es una herramienta sencilla, pero potente diseñada para ayudar a los usuarios a organizar sus pendientes de manera eficiente. Construida con Java y Spring Boot en el backend y utilizando PostgreSQL como base de datos, ofrece una API REST que permite gestionar tareas con operaciones CRUD (crear, leer, actualizar y eliminar).

## 🚀 Instalación

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Dazaitan/TaskManager
2. Configurar base de datos:
    * Crear base de datos
      CREATE DATABASE TaskManager;
    * Tabla tasks
      CREATE TABLE tasks (
      id SERIAL PRIMARY KEY,
      title VARCHAR(255) NOT NULL,
      description TEXT,
      completed BOOLEAN DEFAULT FALSE,
      due_date TIMESTAMP,
      closed_at TIMESTAMP);
3. Pruebas
   Ejecutar todas las pruebas unitarias comando:
    ```bash
    mvn test

4. Información sobre los endpoints
   La documentación completa de los endpoints de la API, incluyendo detalles sobre cómo utilizarlos, está disponible en el directorio /postman.

   *  ¿Qué encontrarás en /postman?
      * Colección Postman: Contiene todas las rutas de la API con ejemplos de solicitudes y respuestas para facilitar pruebas y desarrollo.

      * Instrucciones de uso:
      Importa el archivo .json en Postman para acceder rápidamente a los endpoints.

      * Ejemplos prácticos:
       Cómo realizar solicitudes GET, POST, DELETE y PUT.
       Respuestas esperadas con códigos de estado HTTP y datos de muestra.