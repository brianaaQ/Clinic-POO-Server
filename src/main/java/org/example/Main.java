package org.example;

import org.example.Model.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Server server = new Server();
        server.run(5555);
    }
}