import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Customer {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Read initial welcome message from the server
            String welcomeMessage = reader.readLine();
            System.out.println(welcomeMessage);

            // Read user input and send to the server
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;

            while (true) {
                System.out.print("> ");
                userInput = userInputReader.readLine();

                if (userInput == null || userInput.equalsIgnoreCase("exit")) {
                    // Leave the cafe if the user types "exit" or closes the terminal
                    writer.println("exit");
                    break;
                }

                writer.println(userInput);

                // Read and print server responses
                String serverResponse = reader.readLine();
                System.out.println(serverResponse);

                // Handle order status command
                if (userInput.equalsIgnoreCase("order status")) {
                    // Print the detailed order status
                    while (!(serverResponse = reader.readLine()).isEmpty()) {
                        System.out.println(serverResponse);
                    }
                }
            }

            // Close the socket when done
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
