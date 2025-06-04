package org.example.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.ControlCurso;
import org.example.Entity.Curso;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;
import org.example.util.LocalDateAdapter;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collection;

public class CursoHandler implements HttpHandler {

    private final ControlCurso control = new ControlCurso();
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
                    if (parts.length == 4 && isNumeric(parts[3])) {
                        int id = Integer.parseInt(parts[3]);
                        Curso curso = control.buscarCurso(id);
                        sendJson(ex, 200, gson.toJson(curso));
                    } else {
                        try {
                            Collection<Curso> cursos = control.listarCurso();
                            sendJson(ex, 200, gson.toJson(cursos));
                        } catch (NoDataException nd) {
                            sendJson(ex, 200, "[]");
                        }
                    }
                }

                case "POST" -> {
                    String body = readBody(ex);
                    Curso nuevo = gson.fromJson(body, Curso.class);
                    control.insertarCurso(nuevo);
                    sendJson(ex, 201, "{\"mensaje\":\"Curso insertado\"}");
                }

                case "PUT" -> {
                    String body = readBody(ex);
                    Curso modificado = gson.fromJson(body, Curso.class);
                    control.modificarCurso(modificado);
                    sendJson(ex, 200, "{\"mensaje\":\"Curso modificado\"}");
                }

                case "DELETE" -> {
                    if (parts.length == 4 && isNumeric(parts[3])) {
                        int id = Integer.parseInt(parts[3]);
                        control.eliminarCurso(id);
                        sendJson(ex, 200, "{\"mensaje\":\"Curso eliminado\"}");
                    } else {
                        sendJson(ex, 400, "{\"error\":\"ID de curso inválido o no proporcionado\"}");
                    }
                }

                default -> ex.sendResponseHeaders(405, -1); // Method Not Allowed
            }

        } catch (NoDataException nde) {
            sendJson(ex, 404, "{\"error\":\"" + nde.getMessage() + "\"}");
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

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
