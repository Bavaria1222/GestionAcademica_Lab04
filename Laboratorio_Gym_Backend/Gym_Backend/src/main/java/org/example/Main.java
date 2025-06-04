package org.example;
import org.example.Entity.*;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import org.example.Controller.ControlAlumno;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;


import java.net.InetSocketAddress;
import java.util.Collection;

public class Main {
    public static void main(String[] args) throws Exception {
        ControlAlumno controlAlumno = new ControlAlumno();

        ServidorAPI.iniciarServidor();
    }
}
