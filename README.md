# 🧪 Prueba Técnica DevOps – Microservicio /DevOps

Este repositorio contiene la implementación completa de un Reto Técnico de DevOps, desarrollando un microservicio y aplicando DevOps para su construcción, containerización, balanceo de carga y automatización del pipeline CI/CD.

## ✨ Características Principales

  * **Microservicio REST:** Implementación de un *endpoint* `/DevOps` con **Spring Boot 3**.
  * **Containerización:** Empaquetado de la aplicación con **Docker**.
  * **Balanceo de Carga:** Uso de **Nginx** como *Load Balancer* distribuyendo tráfico entre dos nodos de la aplicación.
  * **Pipeline CI/CD como Código:** Automatización de la integración y el despliegue mediante **GitHub Actions**.
  * **Versionado Dinámico:** Etiquetado de imágenes Docker con la versión `1.0.${{ github.run_number }}`.
  * **Pruebas Automáticas:** Cobertura de la funcionalidad del *endpoint* con **JUnit 5** y **MockMvc**.
  * **Seguridad:** Gestión de **API Key** y **JWT** mediante un servicio de autenticación dedicado.
  * **Despliegue Continuo:** Publicación automática de imágenes Docker en **GitHub Container Registry (GHCR)**.

-----

## 📚 Tecnologías Utilizadas

| Área | Tecnología |
| :--- | :--- |
| **Lenguaje** | Java 17 |
| **Framework** | Spring Boot 3 (REST) |
| **Build** | Maven |
| **Seguridad** | API Key + JWT simple (mock) |
| **Pruebas** | JUnit 5 + Spring MockMvc |
| **Contenedores** | Docker |
| **Orquestación Local** | Docker Compose |
| **Balanceo de Carga** | Nginx |
| **CI/CD** | GitHub Actions (`.github/workflows/ci-cd.yml`) |
| **Registro de Imágenes** | GitHub Container Registry (GHCR) |

-----

## 🧵 Arquitectura General

La solución utiliza un balanceador de carga Nginx para distribuir peticiones entre dos instancias idénticas del microservicio (estrategia Round Robin).

```mermaid
graph TD
    A[Usuario / API] --> B(http://host:8080/DevOps)
    B --> C{Nginx LB<br>(docker 8080)}
    C -- Round Robin --> D[App Node 1<br>(devops-app-1)]
    C -- Round Robin --> E[App Node 2<br>(devops-app-2)]
    D[Spring Boot /DevOps<br>Port 8080 internal]
    E[Spring Boot /DevOps<br>Port 8080 internal]
```

Cada solicitud al puerto `8080` es manejada por **Nginx**, que la redirige a uno de los dos contenedores del microservicio (`devops-app-1` o `devops-app-2`).

-----

## 🛠️ Funcionalidad del Endpoint `/DevOps`

### 📌 Método Permitido: `POST`

| Detalle | Descripción |
| :--- | :--- |
| **Path** | `/DevOps` |
| **Método** | `POST` |
| **Otros Métodos** | Cualquier otro método (`GET`, `PUT`, `DELETE`, etc.) devuelve **`ERROR`** |

#### Payload de Entrada

```json
{
  "message": "This is a test",
  "to": "Juan Perez",
  "from": "Rita Asturia",
  "timeToLifeSec": 45
}
```

#### Encabezados Requeridos

| Header | Descripción | Valor por Defecto de API Key |
| :--- | :--- | :--- |
| `X-Parse-REST-API-Key` | API Key obligatoria | `2f5ae96c-b558-4c7b-a590-a501ae1c36f6` |
| `X-JWT-KWT` | JWT simulado obligatorio (configurable) | `N/A` |

#### Respuesta Esperada (Caso Exitoso)

```json
{
  "message": "Hello Juan Perez your message will be send"
}
```

-----

## 🔐 Gestión de API Key y JWT

La lógica de autorización está centralizada en la clase **`ApiAuthService`**.

### Funciones del Servicio

  * Lectura de API Key desde configuración (`application.properties` o variables de entorno).
  * Validación de API Key exacta.
  * Validación de JWT simple (solo se verifica la presencia del header si `security.jwt-required=true`).
  * Centralización de la lógica de autorización.

### Configuración (application.properties / Env Vars)

```properties
security.api-key-value=2f5ae96c-b558-4c7b-a590-a501ae1c36f6
security.jwt-required=true
```

-----

## 🧪 Pruebas Automáticas

Las pruebas están implementadas en `src/test/java/.../DevOpsControllerTest.java` y usan **MockMvc** para validar el comportamiento sin iniciar el servidor completo (Tomcat).

### Casos Cubiertos

  * **POST Válido:** Con API Key y JWT correctos (→ **200 OK**).
  * **POST sin JWT:** Devolver **401** + `"ERROR"`.
  * **POST con API Key incorrecta:** Devolver **401** + `"ERROR"`.
  * **Otros métodos:** (`GET`/`PUT`/`DELETE`) devuelven `"ERROR"`.

Para ejecutar las pruebas:

```bash
mvn test
```

-----

## 🐳 Docker y Containerización

### Build Local de la Imagen

```bash
docker build -t prueba-tecnica-devops .
```

### Ejecución Local

```bash
docker run -p 8080:8080 prueba-tecnica-devops
```

### ⚖️ Balanceador con Dos Nodos

Para probar el balanceador de carga se utiliza **Docker Compose**, que levanta dos instancias del microservicio y un Nginx como *frontend*.

1.  **Ejecutar:**

    ```bash
    docker-compose up --build
    ```

2.  **Consumir API:**

    ```bash
    http://localhost:8080/DevOps
    ```

La configuración de Nginx (`nginx.conf`) utiliza el *upstream* para balancear entre los servicios definidos en `docker-compose.yml`:

```nginx
upstream devops_backend {
    server devops-app-1:8080;
    server devops-app-2:8080;
}
```

-----

## 🚀 CI/CD con GitHub Actions

El flujo de trabajo se encuentra en **`.github/workflows/ci-cd.yml`**.

### 1️⃣ Etapa: Build & Test

  * Verifica la compilación del proyecto.
  * Ejecuta las pruebas unitarias y de integración automáticas.
  * **Objetivo:** Garantizar la salud del código en cualquier rama.

### 2️⃣ Etapa: Build & Push Docker Image (Solo `main`)

  * **Versionado Dinámico:** Asigna la versión `1.0.${{ github.run_number }}` a la imagen.
  * **Imágenes generadas y desplegadas a GHCR:**
      * `ghcr.io/alfadexters/prueba-tecnica:1.0.X`
      * `ghcr.io/alfadexters/prueba-tecnica:latest`
  * **Objetivo:** Publicar el artefacto listo para producción en el registro de contenedores.

### 🌍 Despliegue en Cualquier Entorno

Una vez la imagen está en GHCR, el microservicio puede ser desplegado fácilmente en cualquier plataforma con soporte Docker (Kubernetes, AWS ECS, Azure Container Apps, Google Cloud Run, etc.).

```bash
# 1. Pull de la imagen más reciente
docker pull ghcr.io/alfadexters/prueba-tecnica:latest

# 2. Ejecutar
docker run -p 8080:8080 ghcr.io/alfadexters/prueba-tecnica:latest
```

-----

## 📂 Estructura del Proyecto

```
.
├── src/
│   ├── main/java/com/devops/prueba_tecnica/
│   │   ├── Controller/DevOpsController.java
│   │   ├── DTO/DevOpsRequest.java
│   │   ├── DTO/DevOpsResponse.java
│   │   ├── Service/ApiAuthService.java
│   │   └── ...
│   └── test/java/.../DevOpsControllerTest.java
├── Dockerfile
├── docker-compose.yml
├── nginx.conf
├── pom.xml
├── .github/workflows/ci-cd.yml
└── README.md
```

-----

## 🧩 Comandos Útiles

| Comando | Descripción |
| :--- | :--- |
| `mvn spring-boot:run` | Correr el microservicio localmente con Spring Boot. |
| `mvn test` | Ejecutar las pruebas automáticas. |
| `docker-compose up --build` | Levantar la arquitectura completa (2 nodos + Nginx LB). |

### Probar Endpoint

Ejemplo de `curl` para una petición exitosa:

```bash
curl -X POST \
  -H "X-Parse-REST-API-Key: 2f5ae96c-b558-4c7b-a590-a501ae1c36f6" \
  -H "X-JWT-KWT: dummy-jwt-token" \
  -H "Content-Type: application/json" \
  -d '{"message":"This is a test","to":"Juan Perez","from":"Rita Asturia","timeToLifeSec":45}' \
  http://localhost:8080/DevOps
```
