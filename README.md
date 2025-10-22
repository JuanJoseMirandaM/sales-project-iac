# Microservicios con Docker Compose

## Arquitectura

Cada microservicio es un **proyecto Maven independiente** con su propio:
- `pom.xml` completo
- Maven wrapper (`mvnw`)
- Dockerfile
- Configuración de aplicación
- Base de datos MongoDB compartida
- **DTOs (Data Transfer Objects)** para inputs y responses
- **Lombok** para código más limpio

## Servicios

### 1. User Service (Puerto 8081)
- **Entidad**: User
- **Atributos**: username, password, firstName, lastName
- **Endpoints**:
  - `GET /api/users` - Obtener todos los usuarios
  - `GET /api/users/{id}` - Obtener usuario por ID
  - `POST /api/users` - Crear usuario
  - `GET /api/users/username/{username}` - Obtener usuario por username

### 2. Product Service (Puerto 8082)
- **Entidad**: Product
- **Atributos**: code, name, description, category, price
- **Endpoints**:
  - `GET /api/products` - Obtener todos los productos
  - `GET /api/products/{id}` - Obtener producto por ID
  - `POST /api/products` - Crear producto
  - `GET /api/products/code/{code}` - Obtener producto por código
  - `GET /api/products/category/{category}` - Obtener productos por categoría

### 3. Sale Service (Puerto 8083)
- **Entidad**: Sale
- **Atributos**: customer, date, total, items (List<DetailSale>)
- **DetailSale**: quantity, productName, productCode, price, subtotal
- **Endpoints**:
  - `GET /api/sales` - Obtener todas las ventas
  - `GET /api/sales/{id}` - Obtener venta por ID
  - `POST /api/sales` - Crear venta
  - `GET /api/sales/customer/{customer}` - Obtener ventas por cliente
  - `GET /api/sales/date-range?startDate=...&endDate=...` - Obtener ventas por rango de fechas

## Base de Datos

Todos los servicios se conectan a MongoDB. Tienes 3 opciones:

| Opción | Archivo | Descripción |
|--------|---------|-------------|
| **MongoDB Local** | `docker-compose.yml` | Incluye MongoDB local en Docker |
| **MongoDB Atlas** | `docker-compose-atlas.yml` | Solo microservicios, conecta a Atlas |
| **MongoDB Local con Auth** | `docker-compose.yml` + `.env` | MongoDB local con autenticación |

## Configuración

### 1. Usando MongoDB Local (Docker Compose)

```bash
# Clonar el repositorio
git clone <repository-url>
cd proyecto

# Levantar todos los servicios (incluye MongoDB local)
docker-compose up --build
```

### 2. Usando MongoDB Atlas

1. Crear un archivo `.env` basado en `env.example`
2. Configurar la variable `MONGODB_URI` con tu connection string de Atlas
3. Ejecutar:

```bash
# Usar docker-compose-atlas.yml (sin MongoDB local)
docker-compose -f docker-compose-atlas.yml up --build
```

### 3. Usando MongoDB con autenticación local

1. Crear un archivo `.env` basado en `env.example`
2. Configurar las variables de MongoDB:

```bash
# Opción A: URI completa (recomendado)
MONGODB_URI=mongodb://username:password@localhost:27017/microservices_db

# Opción B: Configuración por partes
MONGODB_HOST=localhost
MONGODB_PORT=27017
MONGODB_DATABASE=microservices_db
MONGODB_USERNAME=myuser
MONGODB_PASSWORD=mypassword
MONGODB_AUTH_DATABASE=admin
```

3. Ejecutar:

```bash
docker-compose up --build
```

## Endpoints de Prueba

Una vez que los servicios estén ejecutándose, puedes probar los siguientes endpoints:

### User Service
```bash
# Crear usuario
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juanmiranda",
    "password": "password123",
    "firstName": "Juan",
    "lastName": "Miranda"
  }'

# Obtener usuarios
curl http://localhost:8081/api/users
```

### Product Service
```bash
# Crear producto
curl -X POST http://localhost:8082/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "code": "PROD001",
    "name": "Laptop",
    "description": "Lenovo Laptop",
    "category": "Electronics",
    "price": 7500.00
  }'

# Obtener productos
curl http://localhost:8082/api/products
```

### Sale Service
```bash
# Crear venta
curl -X POST http://localhost:8083/api/sales \
  -H "Content-Type: application/json" \
  -d '{
    "customer": "juanmiranda",
    "date": "2025-10-21T10:30:00",
    "total": 7500.00,
    "items": [
      {
        "quantity": 1,
        "productName": "Laptop",
        "productCode": "PROD001",
        "price": 7500.00,
        "subtotal": 7500.00
      }
    ]
  }'

# Obtener ventas
curl http://localhost:8083/api/sales
```

## Comandos Útiles

### 🐳 Docker Compose
```bash
# Levantar todos los servicios
docker-compose up --build

# Levantar en segundo plano
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar servicios
docker-compose down

# Parar y eliminar volúmenes
docker-compose down -v
```

### 🔧 Ejecutar microservicios individualmente

```bash
# User Service
cd user-service
./mvnw spring-boot:run

# Product Service  
cd product-service
./mvnw spring-boot:run

# Sale Service
cd sale-service
./mvnw spring-boot:run
```

### 🐳 Construir imágenes Docker individuales

```bash
# User Service
cd user-service
docker build -t user-service .

# Product Service
cd product-service  
docker build -t product-service .

# Sale Service
cd sale-service
docker build -t sale-service .
```

## Estructura del Proyecto

```
proyecto/
├── user-service/                    # Microservicio independiente
│   ├── src/main/java/com/microservices/user/
│   ├── src/main/resources/application.yml
│   ├── pom.xml                      # POM independiente
│   ├── Dockerfile
│   ├── mvnw                         # Maven wrapper
│   └── .mvn/wrapper/
├── product-service/                 # Microservicio independiente
│   ├── src/main/java/com/microservices/product/
│   ├── src/main/resources/application.yml
│   ├── pom.xml                      # POM independiente
│   ├── Dockerfile
│   ├── mvnw                         # Maven wrapper
│   └── .mvn/wrapper/
├── sale-service/                    # Microservicio independiente
│   ├── src/main/java/com/microservices/sale/
│   ├── src/main/resources/application.yml
│   ├── pom.xml                      # POM independiente
│   ├── Dockerfile
│   ├── mvnw                         # Maven wrapper
│   └── .mvn/wrapper/
├── docker-compose.yml               # Para MongoDB local
├── docker-compose-atlas.yml         # Para MongoDB Atlas
├── test-services.sh                 # Script de pruebas
├── env.example                      # Variables de entorno
└── README.md
```

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Data MongoDB**
- **Docker & Docker Compose**
- **MongoDB**
- **Maven**
- **Lombok** - Para reducir código boilerplate
- **DTOs** - Para separar entidades de requests/responses
