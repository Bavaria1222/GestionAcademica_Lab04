package org.example.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.ControlGrupo;
import org.example.Entity.Grupo;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class GrupoHandler implements HttpHandler {

    private final ControlGrupo control = new ControlGrupo();
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
                    if (parts.length == 7 && "profesor".equalsIgnoreCase(parts[3]) &&
                            "ciclo".equalsIgnoreCase(parts[5])) {
                        String cedula = parts[4];
                        int idCiclo = Integer.parseInt(parts[6]);
                        try {
                            Collection<Grupo> lista = control.listarGrupoProfesor(idCiclo, cedula);
                            sendJson(ex, 200, gson.toJson(lista));
                        } catch (NoDataException nd) {
                            sendJson(ex, 200, "[]");
                        }
                    } else if (parts.length == 4 && isNumeric(parts[3])) {
                        int id = Integer.parseInt(parts[3]);
                        Grupo grupo = control.buscarGrupo(id);
                        sendJson(ex, 200, gson.toJson(grupo));
                    } else {
                        try {
                            Collection<Grupo> grupos = control.listarGrupo();
                            sendJson(ex, 200, gson.toJson(grupos));
                        } catch (NoDataException nd) {
                            sendJson(ex, 200, "[]");
                        }
                    }
                }

                case "POST" -> {
                    String body = readBody(ex);
                    Grupo nuevo = gson.fromJson(body, Grupo.class);
                    control.insertarGrupo(nuevo);
                    sendJson(ex, 201, "{\"mensaje\":\"Grupo insertado\"}");
                }

                case "PUT" -> {
                    String body = readBody(ex);
                    Grupo actualizado = gson.fromJson(body, Grupo.class);
                    control.modificarGrupo(actualizado);
                    sendJson(ex, 200, "{\"mensaje\":\"Grupo modificado\"}");
                }

                case "DELETE" -> {
                    if (parts.length == 4 && isNumeric(parts[3])) {
                        int id = Integer.parseInt(parts[3]);
                        control.eliminarGrupo(id);
                        sendJson(ex, 200, "{\"mensaje\":\"Grupo eliminado\"}");
                    } else {
                        sendJson(ex, 400, "{\"error\":\"ID de grupo inválido o no proporcionado\"}");
                    }
                }

                default -> ex.sendResponseHeaders(405, -1);
            }

        } catch (NoDataException nde) {
            sendJson(ex, 404, "{\"error\":\"" + nde.getMessage().replace("\"", "'") + "\"}");
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
        try (BufferedReader br = new BufferedReader(new InputStreamReader(ex.getRequestBody(), StandardCharsets.UTF_8))) {
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
