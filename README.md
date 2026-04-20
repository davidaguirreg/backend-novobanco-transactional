# backend-novobanco-transactional

Este repositorio forma parte de una simulación de un sistema transaccional bancario

## Descripción del problema

El sistema transaccional bancario es un microservicio backend desarrollado en Spring Boot que permite gestionar cuentas bancarias y realizar transacciones financieras como depósitos, retiros y transferencias entre cuentas. El sistema debe garantizar la integridad de las transacciones, manejar saldos de manera consistente y registrar un historial completo de movimientos para auditoría y consulta. Este tipo de sistemas son críticos el manejo de errores y de concurrencia ya que involucran un alto impacto en clientes y en la imagen del banco. Por lo tanto, es fundamental que el sistema se construya con la mayor calidad y eficiencia, sin perder de vista la integridad y disponibilidad de los datos.

## Escenario
### Saldo negativo
La forma de abordar el saldo negativo se realizar a partir operadores de condicion propios del lenguaje
de programación, incluyendo una verificación previa de la cuenta existente utilizando las entidades generadas. Adicional, se retorna codigo de error http y se lanza una excepción para no proceder con las demas operaciones
### Cuena inactiva
Las cuentas inactivas se abordaron de igual manera que el saldo negativo mediante operadores de condicion, validacion de estado de cuenta y lanzamiento de excepciones.
### Transferencia parcial
La transferencia parcial se la aborda por medio de anotaciones propias de Spring. La anotación Transactional sobre un metodo permite que esta función se ejecute de manera atómica es decir, que se ejecuta toda la funcion y no solamente una parte específica.
### Concurrencia Basica
La manera de abordar este caso fue por medio de bloqueos de la fila cuenta para casos de update. Con esto se evita que se escriba la misma fila dos veces, evitando inconsistencia en el saldo de la cuenta.

## Decisiones técnicas
Para empezar una de las decisiones principales fue la tecnología sobre la cual implementar el backend. Se seleccionó Spring porque es un framework que permite manejar transacciones críticas y controlar la concurrencia para evitar inconsitencia en los datos, lo cual es primordial para este tipo de sistemas.

El patrón seleccionado para este proyecte es por Capas, divididas entre controladores, servicios, repositorios y entidades. Este patrón permite separar de manera correcta la lógica del negocio, del manejo de errores, asi como las entidades propias de los datos.

Otro punto importante sobre el desarrollo fue el manejo de errores como saldo negativo y cuenta inactiva para lo cual se decidió que una opción eficiente es utilizar un gestor global de excepciones en el cual se puede centralizar la logica de los errores en tiempo de ejecución.

En el diagrama entidad relación también fue un punto crítico generar una tabla de movimientos asociada a una transacción con el objetivo de que una transaccion pueda tener varios movimientos. Esta logica simplifica la tabla transacción y permite tener mayor trazabilidad de los movimientos realizados en cuentas de clientes.

Requisitos principales del sistema:
- Gestión de clientes y cuentas bancarias
- Realización de transacciones seguras con validaciones de saldo
- Registro de movimientos para cada transacción



### Arquitectura del Microservicio
- **Framework**: Spring Boot 3.2.0 con Java 17
- **ORM**: JPA/Hibernate para mapeo objeto-relacional
- **API**: RESTful con Spring Web
- **Manejo de Errores**: Controladores de excepciones globales
- **Patrón**: Capas (Controller -> Service -> Repository -> Entity)

## Instrucciones de ejecución

### Prerrequisitos
Tener instalado
- Java 17 o superior
- Maven 3.6+
- Docker desktop (Con la respectiva cuenta)

### Configuración de la Aplicación
 Tener en cuenta que se requiere que esten las variables de entorno PATH y JAVA_HOME configuradas adecuadamente para proceder con el resto de pasos.

# Ejecución del proyecto con Docker Compose

---

## Paso 1: Generar el archivo .jar

Antes de levantar Docker debes compilar el proyecto:

```bash
mvn clean package -DskipTests
```


---

## Paso 2: Levantar el sistema con Docker Compose

Ejecuta el siguiente comando en la raíz del proyecto (donde está el docker-compose.yml):

```bash
docker compose up --build
```

### Pruebas
Ejecutar las pruebas con:
```bash
mvn test
```
