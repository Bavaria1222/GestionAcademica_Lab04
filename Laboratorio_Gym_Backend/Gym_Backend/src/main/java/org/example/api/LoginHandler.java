package org.example.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.ControlUsuario;
import org.example.Entity.Usuario;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class LoginHandler implements HttpHandler {

    private final ControlUsuario control = new ControlUsuario();
    private final Gson gson = new GsonBuilder().create();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!ex.getRequestMethod().equalsIgnoreCase("POST")) {
            ex.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        String body = readBody(ex);
        Usuario loginRequest = gson.fromJson(body, Usuario.class);

        try {
            Usuario usuarioDB = control.buscarUsuario(loginRequest.getCedula());

            if (usuarioDB != null && usuarioDB.getClave().equals(loginRequest.getClave())) {
                // Autenticación exitosa
                sendJson(ex, 200, gson.toJson(usuarioDB));
            } else {
                sendJson(ex, 401, "{\"error\":\"Credenciales inválidas\"}");
            }
        } catch (Exception e) {
            sendJson(ex, 500, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private void sendJson(HttpExchange ex, int code, String json) throws IOException {
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        byte[] data = json.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(code, data.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(data);
        }
    }

    private String readBody(HttpExchange ex) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(ex.getRequestBody(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }
}
