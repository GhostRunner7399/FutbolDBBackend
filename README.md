# Proyecto Java Spring Boot + React

Este repositorio contiene un proyecto estructurado para trabajar con un backend desarrollado en **Java Spring Boot** y un frontend desarrollado en **React**. Está diseñado para facilitar la colaboración entre equipos trabajando en el backend y el frontend simultáneamente.

---

## **Estructura del Repositorio**

```plaintext
/raiz-del-proyecto
│
├── /backend                  # Carpeta para el backend (Spring Boot)
│   ├── /src
│   │   ├── /main
│   │   │   ├── /java         # Código fuente principal en Java
│   │   │   ├── /resources    # Archivos de configuración y templates
│   │   └── /test             # Tests unitarios y de integración
│   ├── pom.xml               # Archivo de configuración de Maven
│   └── README.md             # Instrucciones específicas del backend
│
├── /frontend                 # Carpeta para el frontend (React)
│   ├── /public               # Archivos estáticos
│   ├── /src                  # Código fuente principal de React
│   │   ├── /components       # Componentes reutilizables
│   │   ├── /pages            # Páginas principales
│   │   ├── /services         # Servicios para llamadas al backend
│   │   ├── /styles           # Estilos globales o específicos
│   │   └── App.js            # Componente raíz de React
│   ├── package.json          # Configuración de Node.js y dependencias
│   └── README.md             # Instrucciones específicas del frontend
│
├── /docs                     # Documentación del proyecto
│   ├── /api                  # Especificación de API (por ejemplo, OpenAPI)
│   ├── /frontend             # Guías del frontend
│   └── /backend              # Guías del backend
│
├── /scripts                  # Scripts útiles para automatización
│   ├── start-backend.sh      # Script para levantar el backend
│   ├── start-frontend.sh     # Script para levantar el frontend
│   └── setup-env.sh          # Script para inicializar variables de entorno
│
├── .gitignore                # Configuración para ignorar archivos en Git
├── README.md                 # Instrucciones generales del proyecto
└

```
Requisitos previos
Backend

    Java 17+
    Maven 3.6+
    IDE compatible con Java (IntelliJ, Eclipse, VSCode)

Frontend

    Node.js 16+
    npm o yarn

Instalación
1. Clona el repositorio

git clone https://github.com/tu-usuario/proyecto-spring-react.git
cd proyecto-spring-react

2. Configuración del backend

cd backend
mvn clean install

3. Configuración del frontend

cd frontend
npm install

Ejecución
Levantar el backend

    Ve a la carpeta del backend:

cd backend

Ejecuta el servidor Spring Boot:

    mvn spring-boot:run

    El backend estará disponible en: http://localhost:8080

Levantar el frontend

    Ve a la carpeta del frontend:

cd frontend

Ejecuta la aplicación de React:

    npm start

    El frontend estará disponible en: http://localhost:3000

    Nota: El frontend está configurado para usar un proxy hacia el backend en desarrollo. Puedes modificarlo en el archivo package.json:

"proxy": "http://localhost:8080"

Documentación

Toda la documentación relacionada con el proyecto se encuentra en la carpeta /docs. Esto incluye:

    Especificación de la API (OpenAPI/Swagger).
    Guías de estilo para backend y frontend.
    Información de configuración y despliegue.

Flujo de Trabajo
Ramas de Git

    main: Rama principal para producción.
    develop: Rama para desarrollo conjunto.
    feature/backend-xxx: Funcionalidades específicas del backend.
    feature/frontend-xxx: Funcionalidades específicas del frontend.

Buenas prácticas

    Crea una rama nueva para cada funcionalidad o bugfix.
    Realiza pruebas antes de hacer un pull request.
    Asegúrate de que el código pase los pipelines de CI/CD.
