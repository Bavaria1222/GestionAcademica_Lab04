package org.example.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.ControlCiclo;
import org.example.Entity.Ciclo;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;
import org.example.util.LocalDateAdapter;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collection;

public class CicloHandler implements HttpHandler {

    private final ControlCiclo control = new ControlCiclo();
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
                        Ciclo ciclo = control.buscarCiclo(id);
                        sendJson(ex, 200, gson.toJson(ciclo));
                    } else {
                        try {
                            Collection<Ciclo> ciclos = control.listarCiclo();
                            sendJson(ex, 200, gson.toJson(ciclos));
                        } catch (NoDataException nd) {
                            sendJson(ex, 200, "[]");
                        }
                    }
                }

                case "POST" -> {
                    String body = readBody(ex);
                    Ciclo nuevo = gson.fromJson(body, Ciclo.class);
                    control.insertarCiclo(nuevo);
                    sendJson(ex, 201, "{\"mensaje\":\"Ciclo insertado\"}");
                }

                case "PUT" -> {
                    String body = readBody(ex);
                    Ciclo actualizado = gson.fromJson(body, Ciclo.class);
                    control.modificarCiclo(actualizado);
                    sendJson(ex, 200, "{\"mensaje\":\"Ciclo modificado\"}");
                }

                case "DELETE" -> {
                    if (parts.length == 4 && isNumeric(parts[3])) {
                        int id = Integer.parseInt(parts[3]);
                        try {
                            control.eliminarCiclo(id);
                            sendJson(ex, 200, "{\"mensaje\":\"Ciclo eliminado\"}");
                        } catch (NoDataException nd) {
                            sendJson(ex, 404, "{\"error\":\"No se encontró el ciclo para eliminar\"}");
                        }
                    } else {
                        sendJson(ex, 400, "{\"error\":\"ID de ciclo inválido o no proporcionado\"}");
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

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
