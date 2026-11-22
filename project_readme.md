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
  
    ## Gestión de Categorías (Nuevo Módulo)

Se incorporó el CRUD completo para la entidad **Categoría**, permitiendo administrar las categorías necesarias para el alta y clasificación de productos.

### Componentes agregados
* **DTOs (`dto/`):** `CategoriaRequestDTO` y `CategoriaResponseDTO` con validaciones de entrada.
* **Repositorio (`repository/`):** `CategoriaRepository` (extiende de `JpaRepository`).
* **Servicio (`service/` & `service/impl/`):** `CategoriaService` y `CategoriaServiceImpl` con validación de nombres duplicados y manejo de excepciones (`RecursoNoEncontradoException`, `SolicitudInvalidaException`).
* **Controlador (`controller/`):** `CategoriaController` expuesto bajo `/api/categorias`.

### Endpoints disponibles (`/api/categorias`)

| Método | Endpoint | Descripción | Cuerpo (JSON) |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/categorias` | Crea una nueva categoría | `{"nombre": "Electrónica"}` |
| `GET` | `/api/categorias` | Lista todas las categorías | *No requiere* |
| `GET` | `/api/categorias/{id}` | Obtiene una categoría por su ID | *No requiere* |
| `PUT` | `/api/categorias/{id}` | Actualiza el nombre de una categoría | `{"nombre": "Hogar"}` |
| `DELETE` | `/api/categorias/{id}` | Elimina una categoría por su ID | *No requiere* |

### Orden recomendado de prueba en Postman
1. **Crear Categoría:** `POST http://localhost:8080/api/categorias` con `{"nombre": "Electrónica"}` (devuelve `id: 1`).
2. **Crear Producto:** `POST http://localhost:8080/api/productos` asignando `"categoriaId": 1`.
3. **Crear Cliente:** `POST http://localhost:8080/api/clientes`.
4. **Registrar Venta (Contado o Financiada):** `POST http://localhost:8080/api/ventas`.
5. **Consultar / Cobrar Cuotas (si es financiada):** Endpoints en `/api/cuotas`.



## 📖 Documentación de la API (Swagger / OpenAPI)

El proyecto cuenta con configuración de **OpenAPI / Swagger** (`OpenApiConfig.java`). Con la aplicación en ejecución, podés visualizar y probar todos los endpoints directamente desde el navegador en:
* **Swagger UI:** `http://localhost:8080/swagger-ui/index.html` (o `http://localhost:8080/swagger-ui.html`)



### Módulo de Productos (`/api/productos`)
* **Crear Producto (POST):** `http://localhost:8080/api/productos`
  *Ejemplo de Body (JSON):*
  ```json
  {
    "nombre": "Notebook Lenovo",
    "precio": 850000.00,
    "stock": 10,
    "categoriaId": 1
  }
  ```
* **Listar Productos (GET):** `http://localhost:8080/api/productos`
* **Obtener por ID (GET):** `http://localhost:8080/api/productos/{id}`
* **Actualizar Producto (PUT):** `http://localhost:8080/api/productos/{id}`
* **Eliminar Producto (DELETE):** `http://localhost:8080/api/productos/{id}`

### Módulo de Ventas (`/api/ventas`)
Soporta ventas **al contado** (`CONTADO`) y **financiadas en cuotas** (`FINANCIADA`).
* **Registrar Venta (POST):** `http://localhost:8080/api/ventas`
  *Ejemplo de Body (JSON):*
  ```json
  {
    "clienteId": 1,
    "modalidadPago": "FINANCIADA",
    "cantidadCuotas": 3,
    "items": [
      {
        "productoId": 1,
        "cantidad": 2
      }
    ]
  }
  ```
* **Listar Ventas (GET):** `http://localhost:8080/api/ventas`
* **Obtener Venta por ID (GET):** `http://localhost:8080/api/ventas/{id}`

### Módulo de Cuotas y Estado de Cuenta (`/api/cuotas`)
* **Consultar Estado de Cuenta de una Venta (GET):** `http://localhost:8080/api/cuotas/venta/{ventaId}`
* **Registrar Cobro / Pago de Cuota (POST o PUT):** `http://localhost:8080/api/cuotas/{cuotaId}/pagar`



## Pruebas Automatizadas (Unitarias y de Integración)

El proyecto cuenta con una suite de pruebas automatizadas ubicadas en `src/test/java/ar/edu/unju/fi/poo/` para garantizar la calidad de la lógica de negocio y el correcto funcionamiento de los flujos completos.

### 1. Pruebas Unitarias (`service/`)
Implementadas con **JUnit 5** y **Mockito** para validar cada servicio de forma aislada simulando los repositorios:
* **`ClienteServiceTest`:** Verifica el alta, búsqueda, actualización, eliminación y validaciones de datos duplicados o inexistentes de clientes.
* **`ProductoServiceTest`:** Evalúa la creación de productos, asignación de categorías, control de stock y manejo de excepciones ante recursos no encontrados.
* **`VentaServiceTest`:** Comprueba el cálculo de totales, descuento de stock, generación de ventas al contado (`VentaContado`) y financiadas (`VentaFinanciada`), y validación de cantidades o stock insuficiente.
* **`CuotaServiceTest`:** Valida la consulta de estado de cuenta, el cobro de cuotas pendientes y el cambio de estado de las mismas (`EstadoCuota`).

### 2. Pruebas de Integración (`integration/`)
* **`GestionComercialIntegrationTest`:** Prueba el flujo completo de punta a punta levantando el contexto de Spring Boot (`@SpringBootTest`) y verificando la interacción real entre controladores, servicios y base de datos.

### Cómo ejecutar las pruebas
* **Desde Spring Tool Suite (STS / Eclipse):**
  1. Hacer clic derecho sobre el proyecto `practico_final_Grupo6` (o sobre la carpeta `src/test/java`).
  2. Seleccionar **Run As** -> **JUnit Test**.
* **Desde la consola / terminal (Maven):**
  ```bash
  ./mvnw test
  ```
  
  Aclaracion: hay un merge no hecho ya que tuvimos un inconveniente en la distribucion y subida de las partes del trabajo, al querer finalizar lo que faltaba se dividieron en dos parte el trabajo N°!2 y pero el duo primero no habian subido sus cambios haciendo un merge hacia develop, asi que dos integrantes isiseron los mismo puntos editando los mismo archivos, que al querer hacer merge generaron conflictos por ello hay uno que no se realizo....
  
  
  Funcionamiento exitosos, en pruebas unitarias, bd , y postman....