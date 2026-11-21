# Trabajo Práctico Final - POO 2026 (Grupo 6)

Sistema backend desarrollado con **Spring Boot** para la gestión comercial, orientado a la administración de clientes, productos y ventas.

## 👥 Integrantes del Grupo 6
* Apaza, Jorge
* Kujarchik, Candela
* Revoux, Jorge
* Singh, Federico

---

## 🚀 Tecnologías Utilizadas
* **Java** (JDK 17 o superior)
* **Spring Boot** (3.x)
* **Spring Data JPA / Hibernate** (Con auto-DDL habilitado)
* **Maven** (Gestor de dependencias y compilación)
* **MySQL** (Base de datos relacional)
* **Postman** (Pruebas de endpoints REST)

---

## 🗄️ Configuración y Acceso a la Base de Datos

Para que la aplicación pueda conectarse correctamente a MySQL en tu entorno local, seguí estos pasos:

1. **Crear la Base de Datos:**
   Abrí tu gestor de base de datos (como *MySQL Workbench*) y ejecutá la siguiente sentencia SQL:
   ```sql
   CREATE DATABASE IF NOT EXISTS poo2026;
   USE poo2026;
   ```

2. **Configurar Credenciales:**
   Verificá que el archivo `src/main/resources/application.properties` contenga los datos de acceso correctos a tu servidor MySQL local:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/poo2026?allowPublicKeyRetrieval=true&useSSL=false
   spring.datasource.username=tu_usuario_mysql
   spring.datasource.password=tu_contraseña_mysql

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

---

## ⚙️ Instrucciones de Ejecución

1. **Clonar o descargar el repositorio** en tu computadora.
2. Abrir el proyecto en **Eclipse / Spring Tool Suite** como un proyecto *Existing Maven Project*.
3. Ejecutar la limpieza y compilación inicial si es necesario, o iniciar la aplicación haciendo clic derecho sobre la clase principal > **Run As** > **Spring Boot App**.
4. Alternativamente, podés ejecutarlo desde la terminal con Maven Wrapper:
   ```powershell
   ./mvnw spring-boot:run
   ```

---

## 🧪 Pruebas de la API (Endpoints Principales)

La aplicación corre por defecto en el puerto `http://localhost:8080`.

### Módulo de Clientes (`/api/clientes`)
* **Listar Clientes (GET):**
  `http://localhost:8080/api/clientes`
* **Registrar Cliente (POST):**
  `http://localhost:8080/api/clientes`
  *Ejemplo de Body (JSON):*
  ```json
  {
    "identificacion": "43000111",
    "nombre": "Federico",
    "apellidoRazonSocial": "Singh",
    "telefono": "3884000000",
    "email": "federico@email.com",
    "direccion": "San Pedro de Jujuy",
    "tipoCliente": "EMPRESA"
  }
  ```