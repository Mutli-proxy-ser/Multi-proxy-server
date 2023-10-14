import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.*;

public class ProxyServer {
    public static void main(String[] args) {
        // Defining Colors
        String RESET = "\u001B[0m";
        String RED = "\u001B[31m";
        String GREEN = "\u001B[32m";
        String YELLOW = "\u001B[33m";
        int port = 8888; // Port on which the proxy server listens
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println(GREEN + "Proxy server is listening on port " + port + " 🚀" + RESET);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ProxyThread proxyThread = new ProxyThread(clientSocket);
                Thread thread = new Thread(proxyThread);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println(RED);
            e.printStackTrace();
            System.out.println(RESET);
        }
    }
}

class ProxyThread implements Runnable {
    private Socket clientSocket;
    String GREEN = "\u001B[32m";
    String RESET = "\u001B[0m";
    String RED = "\u001B[31m";

    public ProxyThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            // Read client request
            BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String requestLine = clientIn.readLine();
            if (requestLine == null) {
                clientSocket.close();
                return;
            }

            // Extract target server and port from the request
            String[] parts = requestLine.split(" ");
            String targetHost = parts[1];
            int targetPort = 80; // Default HTTP port

            // Create a socket to connect to the target server
            //Socket targetSocket = new Socket(targetHost.strip(), targetPort); // statement causing the error
            System.out.println(GREEN + "Successfully connected to client server 👩🏾‍💻" + RESET);

            //Create a client writer to output the information to the client
            PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream());
            String responseLine;

            // connect to the website for the user and return the response to the user
            URL ur = new URL(targetHost);
            URLConnection conn = ur.openConnection();

            // create an inputstreamreader stream to get information from the target server
            BufferedReader serverIn = new BufferedReader( new InputStreamReader(conn.getInputStream()));

            while ((responseLine = serverIn.readLine()) != null) {
                clientOut.println(responseLine);
            }
            clientOut.flush();

            // Close sockets
            clientSocket.close();

        } catch (IOException e) {
            System.out.println(RED);
            e.printStackTrace();
            System.out.println(RESET);
        }
    }
}
