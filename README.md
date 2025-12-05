# FPC

A playground repository with a Spring Boot backend and Vue 3 frontend.

## Projects

- **backend**: Spring Boot 3 application exposing a sample `/api/hello` endpoint.
- **frontend**: Vite + Vue 3 SPA with a simple UI that calls the backend endpoint.

## Getting Started

### Backend

```bash
cd backend
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend expects the backend to be available at the same origin. When running
locally with the default Vite dev server (port 5173) and Spring Boot (port 8080),
consider using a proxy or running the frontend through the Spring Boot server in
production.
