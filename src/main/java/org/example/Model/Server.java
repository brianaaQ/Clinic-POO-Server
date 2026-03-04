package org.example.Model;

import org.example.services.ClinicService;
import org.example.database.HibernateUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final ClinicService clinicService;

    public Server() {
        System.out.println("Initializing Medical Clinic Server...");
        
        // Initializare conexiune baza de date si creare tabele
        try {
            System.out.println("Connecting to database...");
            //creeaza tabele daca nu exista niciuna
            HibernateUtil.getEntityManager().close();
            System.out.println("Database initialized successfully!");
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        this.clinicService = new ClinicService();
        System.out.println("Clinic services initialized!");
    }

    //metoda pentru comenzi+conectare/deconectare la client
    public void run(int port) throws IOException {
        System.out.println("Medical Clinic Server has started and is waiting for connection on port " + port + "...");
        serverSocket = new ServerSocket(port);

        try {
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received command: " + inputLine);
                    
                    if (inputLine.equalsIgnoreCase("bye")) {
                        out.println("{\"status\":\"success\",\"message\":\"Goodbye!\"}");
                        break;
                    }

                    String response = clinicService.processCommand(inputLine);
                    out.println(response);
                }
                
                closeClientConnection();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        } finally {
            System.out.println("Closing server resources...");
            closeServerResources();
        }
    }

    //metoda pentru inchiderea conexiuni cu clientul
    private void closeClientConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
                System.out.println("Client disconnected");
            }
        } catch (IOException e) {
            System.err.println("Error closing client connection: " + e.getMessage());
        }
    }

    //opreste serverul din a rula
    private void closeServerResources() {
        try {
            closeClientConnection();
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            //oprire Hibernate
            HibernateUtil.shutdown();
            System.out.println("Database connection closed.");
        } catch (IOException e) {
            System.err.println("Error closing server resources: " + e.getMessage());
        }
    }

    public void stop() throws IOException {
        closeServerResources();
    }
}
