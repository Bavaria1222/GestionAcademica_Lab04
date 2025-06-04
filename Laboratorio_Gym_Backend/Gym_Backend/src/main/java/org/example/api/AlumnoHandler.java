package org.example.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.ControlAlumno;
import org.example.Entity.Alumno;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;
import org.example.util.LocalDateAdapter;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collection;

public class AlumnoHandler implements HttpHandler {

    private final ControlAlumno control = new ControlAlumno();

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (ex.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            addCorsHeaders(ex);
            ex.sendResponseHeaders(204, -1);
            return;
        }

        String method = ex.getRequestMethod();
        URI uri = ex.getRequestURI();
        String path = uri.getPath();
        String[] parts = path.split("/");

        try {
            switch (method) {
                case "GET" -> {
                    if (parts.length == 4) {
                        String cedula = parts[3];
                        Alumno alumno = control.buscarAlumno(cedula);
                        sendJson(ex, 200, gson.toJson(alumno));
                    } else {
                        try {
                            Collection<Alumno> alumnos = control.listarAlumno();
                            sendJson(ex, 200, gson.toJson(alumnos));
                        } catch (NoDataException nd) {
                            sendJson(ex, 200, "[]");
                        }
                    }
                }

                case "POST" -> {
                    String body = readBody(ex);
                    Alumno nuevo = gson.fromJson(body, Alumno.class);
                    control.insertarAlumno(nuevo, nuevo.getClave() != null ? nuevo.getClave() : "clave123");
                    sendJson(ex, 201, "{\"mensaje\":\"Alumno insertado\"}");
                }

                case "PUT" -> {
                    String body = readBody(ex);
                    Alumno actualizado = gson.fromJson(body, Alumno.class);
                    control.modificarAlumno(actualizado, actualizado.getClave() != null ? actualizado.getClave() : "clave123");
                    sendJson(ex, 200, "{\"mensaje\":\"Alumno modificado\"}");
                }

                case "DELETE" -> {
                    if (parts.length == 4) {
                        String cedula = parts[3].trim(); // <- Esto es clave
                        System.out.println("DEBUG → Eliminando cédula: [" + cedula + "]");
                        control.eliminarAlumno(cedula);
                        sendJson(ex, 200, "{\"mensaje\":\"Alumno eliminado\"}");
                    } else {
                        sendJson(ex, 400, "{\"error\":\"Se requiere cédula en la URL\"}");
                    }
                }
                default -> ex.sendResponseHeaders(405, -1); // Method Not Allowed
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendJson(ex, 500,
                    "{\"error\":\"" + e.getClass().getSimpleName() + ": " +
                            (e.getMessage() != null ? e.getMessage().replace("\"", "'") : "") + "\"}");
        }
    }

    private void sendJson(HttpExchange ex, int code, String json) throws IOException {
        addCorsHeaders(ex);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] data = json.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(code, data.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(data);
        }
    }

    private void addCorsHeaders(HttpExchange ex) {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    private String readBody(HttpExchange ex) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(ex.getRequestBody(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }
}
