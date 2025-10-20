# GoVyReel

This is a monorepo containing the backend and frontend code for the GoVyReel application.

## Setup

Before running the application, you need to set up the environment variables for both the frontend and backend.

### Backend (govyreel-backend)

Create a `.env` file in the `govyreel-backend` directory and add the following environment variables:

```
DB_HOST=your_database_host
DB_PORT=your_database_port
DB_NAME=your_database_name
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password
JWT_SECRET_KEY=your_jwt_secret_key
FRONTEND_URL=http://localhost:3000
```

### Frontend (govyreel-frontend)

Create a `.env` file in the `govyreel-frontend` directory and add the following environment variables:

```
NEXT_PUBLIC_API_URL=http://localhost:8080
```

## Running the Application

### Backend (govyreel-backend)

To start the backend server, run the following command in the `govyreel-backend` directory:

```
./mvnw spring-boot:run
```

### Frontend (govyreel-frontend)

To start the frontend server, run the following commands in the `govyreel-frontend` directory:

```
npm i
npm run dev
```

To build the frontend application, run the following command:

```
npm run build
```

To start the frontend application in production mode, run the following command:

```
npm start
```
