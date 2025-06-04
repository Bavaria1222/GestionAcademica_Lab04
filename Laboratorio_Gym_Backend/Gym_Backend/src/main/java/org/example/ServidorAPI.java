package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.api.*;
//import org.example.api.ProfesorHandler;   // ⇦ añade handlers extra
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class ServidorAPI {

    public static void iniciarServidor() throws Exception {
        /* 0.0.0.0 = acepta conexiones de tu PC, emulador y teléfonos en la misma red */
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);

        // 1️⃣ Registra todos los endpoints REST
        server.createContext("/api/alumnos",    new AlumnoHandler());
        server.createContext("/api/profesores", new ProfesorHandler());
        server.createContext("/api/carreras",  new CarreraHandler());
        server.createContext("/api/ciclos", new CicloHandler());
        server.createContext("/api/cursos", new CursoHandler());
        server.createContext("/api/grupos", new GrupoHandler());
        server.createContext("/api/matriculas", new MatriculaHandler());
        server.createContext("/api/planEstudios", new PlanEstudioHandler());
        server.createContext("/api/usuarios", new UsuarioHandler());
        server.createContext("/api/login", new LoginHandler());

        // 2️⃣ Pool de hilos sencillo (4 peticiones simultáneas)
        server.setExecutor(Executors.newFixedThreadPool(4));

        server.createContext("/ping", ex -> {
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            byte[] ok = "pong".getBytes();
            ex.sendResponseHeaders(200, ok.length);
            ex.getResponseBody().write(ok);
            ex.close();
        });



        server.start();
        System.out.println("✅  API escuchando en http://localhost:8080/");
    }
}
