# Open Flow Coworking — API Backend

API REST para la gestión de ingresos, salidas, clientes, sedes y operadores en una red de coworking. Construida con Spring Boot 3 y PostgreSQL.

---

## Tabla de contenidos

1. [Requisitos](#requisitos)
2. [Ejecución local](#ejecución-local)
3. [Ejecución con Docker Compose](#ejecución-con-docker-compose)
4. [Compartir externamente con URL pública](#compartir-externamente-con-url-pública)
5. [Perfiles de entorno](#perfiles-de-entorno)
6. [Documentación](#documentación)

---

## Requisitos

| Herramienta | Versión mínima |
|---|---|
| Java (JDK) | 17 |
| Maven | 3.9 |
| PostgreSQL | 15 |
| Docker + Docker Compose | 24 |

> Docker es opcional, puedes ejecutar la aplicación localmente sin Docker.

---

## Ejecución local

### 1. Crear la base de datos

Crea la base de datos y el usuario en PostgreSQL:

```sql
CREATE DATABASE open_flow_coworking;
CREATE USER nelumbo WITH PASSWORD '4LfTW6Om7u65qzOsvNpRZfRTqTioC';
GRANT ALL PRIVILEGES ON DATABASE open_flow_coworking TO nelumbo;
```

### 2. Clonar el repositorio

```bash
git clone https://github.com/IngeEduar/open-flow-coworking.git
cd open-flow-coworking
```

### 3. Iniciar la aplicación

El perfil `local` se conecta a `localhost:5432` con las credenciales anteriores y habilita el log de SQL.

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

La API estará disponible en `http://localhost:8080`.

---

## Ejecución con Docker Compose

Este es el método recomendado. Un solo comando construye la imagen e inicia tanto la aplicación como la base de datos.

### 1. Iniciar todos los servicios

```bash
docker compose up --build
```

Esto levanta:
- **`openflow-backoffice`** — la API de Spring Boot en el puerto `8080`
- **`nelumbo_postgres`** — PostgreSQL 15 en el puerto `5432` (solo accesible localmente)

### 2. Detener y eliminar los contenedores

```bash
docker compose down
```

Para eliminar también el volumen de la base de datos (todos los datos):

```bash
docker compose down -v
```

### Notas

- Las credenciales de la base de datos están preconfiguradas en `docker-compose.yaml`.
- Los datos se persisten en un volumen Docker llamado `nelumbo_data`.
- La aplicación inicia con el perfil `docker` (`SPRING_PROFILES_ACTIVE: docker`).

---

## Compartir externamente con URL pública

Para exponer la aplicación en ejecución a internet, puedes usar Cloudflare Tunnel. No se requiere cuenta.

### 1. Asegúrate de que la aplicación esté corriendo

Ya sea de forma local o con Docker Compose (debe estar en el puerto `8080`).

### 2. Iniciar el túnel

```bash
docker run --rm \
  --network host -d \
  cloudflare/cloudflared:latest \
  tunnel --url http://localhost:8080
```

### 3. Obtener la URL pública

```bash
docker logs $(docker ps -q --filter ancestor=cloudflare/cloudflared) 2>&1 | grep "trycloudflare.com"
```

La salida contendrá una línea similar a esta:

```
INF |  Your quick Tunnel has been created! Visit it at:           |
INF |  https://nombre-aleatorio.trycloudflare.com                 |
```

Comparte el enlace `https://nombre-aleatorio.trycloudflare.com`. El túnel permanece activo mientras el contenedor esté corriendo.

### 4. Detener el túnel

```bash
docker stop $(docker ps -q --filter ancestor=cloudflare/cloudflared)
```

> La URL es temporal y cambia cada vez que se inicia un nuevo túnel.

---

## Perfiles de entorno

La aplicación usa perfiles de Spring Boot para gestionar la configuración por entorno.

| Perfil | Activación | Descripción |
|---|---|---|
| `local` | `./mvnw ... -Dspring-boot.run.profiles=local` | Desarrollo local, se conecta a `localhost:5432`, log SQL habilitado |
| `docker` | Configurado automáticamente por `docker-compose.yaml` | Se conecta al servicio `postgres` por nombre de host |
| `preproduction` | Variable de entorno `SPRING_PROFILES_ACTIVE=preproduction` | Usa variables de entorno para todas las credenciales |
| `production` | Variable de entorno `SPRING_PROFILES_ACTIVE=production` | Usa variables de entorno para todas las credenciales |

Para `preproduction` y `production`, las siguientes variables de entorno deben estar configuradas antes de iniciar la aplicación:

```
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
NOTIFICATIONS_URL
```

---

## Documentación

### Swagger UI (Interactivo)

Disponible mientras la aplicación esté en ejecución:

```
http://localhost:8080/swagger-ui/index.html
```

Los endpoints protegidos requieren un token Bearer. Usa el botón **Authorize** en la parte superior de Swagger UI e ingresa:

```
Bearer <tu_token>
```

### Colección de Postman

Una colección completa de Postman con todos los endpoints, ejemplos de solicitudes y variables de entorno está disponible en:

[https://documenter.getpostman.com/view/52872949/2sBXcKDdyN](https://documenter.getpostman.com/view/52872949/2sBXcKDdyN)

---

## Credenciales del administrador por defecto

Un usuario administrador se crea automáticamente al primer inicio:

| Campo | Valor |
|---|---|
| Email | `admin@nelumbo.com` |
| Contraseña | `admin123` |

Se recomienda cambiar estas credenciales inmediatamente después del primer inicio de sesión en cualquier entorno que no sea local.
