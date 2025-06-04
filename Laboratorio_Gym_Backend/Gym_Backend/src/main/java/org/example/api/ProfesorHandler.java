package org.example.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.ControlProfesor;
import org.example.Entity.Profesor;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class    ProfesorHandler implements HttpHandler {

    private final ControlProfesor control = new ControlProfesor();
    private final Gson gson = new GsonBuilder().create();

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
                        Profesor profesor = control.buscarProfesor(cedula);
                        sendJson(ex, 200, gson.toJson(profesor));
                    } else {
                        try {
                            Collection<Profesor> profesores = control.listarProfesor();
                            sendJson(ex, 200, gson.toJson(profesores));
                        } catch (NoDataException nd) {
                            sendJson(ex, 200, "[]");
                        }
                    }
                }

                case "POST" -> {
                    String body = readBody(ex);
                    Profesor nuevo = gson.fromJson(body, Profesor.class);
                    control.insertarProfesor(nuevo, nuevo.getClave() != null ? nuevo.getClave() : "clave123");
                    sendJson(ex, 201, "{\"mensaje\":\"Profesor insertado\"}");
                }

                case "PUT" -> {
                    String body = readBody(ex);
                    Profesor actualizado = gson.fromJson(body, Profesor.class);
                    control.modificarProfesor(actualizado, actualizado.getClave() != null ? actualizado.getClave() : "clave123");
                    sendJson(ex, 200, "{\"mensaje\":\"Profesor modificado\"}");
                }

                case "DELETE" -> {
                    if (parts.length == 4) {
                        String cedula = parts[3].trim();
                        try {
                            control.eliminarProfesor(cedula);
                            sendJson(ex, 200, "{\"mensaje\":\"Profesor eliminado\"}");
                        } catch (NoDataException nd) {
                            sendJson(ex, 404, "{\"error\":\"" + nd.getMessage() + "\"}");
                        }
                    } else {
                        sendJson(ex, 400, "{\"error\":\"Se requiere cédula en la URL\"}");
                    }
                }


                default -> ex.sendResponseHeaders(405, -1); // Method Not Allowed
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendJson(ex, 500, "{\"error\":\"" + e.getClass().getSimpleName() + ": " +
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
