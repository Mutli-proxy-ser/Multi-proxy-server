import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

public class ProxyClient {
    public static void main(String[] args) {
        // Defining Colors
        String RESET = "\u001B[0m";
        String RED = "\u001B[31m";
        String GREEN = "\u001B[32m";
        String YELLOW = "\u001B[33m";

        // Proxy server address
        String proxyHost = "localhost";

        // Proxy server port
        int proxyPort = 8888;
        // add extra comment
        // Get the user's website
        Scanner scanner = new Scanner(System.in);
        System.out.print("What website do you want to visit (include https): ");
        String targetURL = scanner.nextLine(); // Target URL to access through the proxy

        try {
            URI uri = new URI(targetURL);
            Desktop.getDesktop().browse(uri);

            Socket proxySocket = new Socket(proxyHost, proxyPort);
            PrintWriter proxyOut = new PrintWriter(proxySocket.getOutputStream());

            // Send an HTTP GET request to the proxy server
            proxyOut.println("GET " + targetURL + " HTTP/1.1");
            proxyOut.println("Host: " + targetURL);
            proxyOut.println();
            proxyOut.flush();
            // Read and print the response from the proxy server
            BufferedReader proxyIn = new BufferedReader(new InputStreamReader(proxySocket.getInputStream()));
            String responseLine;
            while ((responseLine = proxyIn.readLine()) != null) {
                System.out.println(YELLOW + responseLine + RESET);
            }

            // Close the proxy socket
            proxySocket.close();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}
