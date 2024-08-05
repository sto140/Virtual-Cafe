import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Barista cafeServer;
    private String clientName;

    private int brewingTeas;
    private int brewingCoffees;
    private int trayTeas;
    private int trayCoffees;

    public int getBrewingTeas() {
        return brewingTeas;
    }

    public int getBrewingCoffees() {
        return brewingCoffees;
    }

    public int getTrayTeas() {
        return trayTeas;
    }

    public int getTrayCoffees() {
        return trayCoffees;
    }

    public ClientHandler(Socket clientSocket, Barista cafeServer) {
        this.clientSocket = clientSocket;
        this.cafeServer = cafeServer;
        try {
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            writer.println("Welcome to the Virtual Cafe! Please enter your name:");
            clientName = reader.readLine();
            cafeServer.broadcast(clientName + " has joined the cafe.");

            String input;
            while ((input = reader.readLine()) != null) {
                processCommand(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cafeServer.removeClient(this);
            cafeServer.broadcast(clientName + " has left the cafe.");
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    private void processCommand(String command) {
        StringTokenizer tokenizer = new StringTokenizer(command);
        String action = tokenizer.nextToken();

        switch (action.toLowerCase()) {
            case "order":
                handleOrderCommand(tokenizer);
                break;
            case "order status":
                handleOrderStatusCommand();
                break;
            case "exit":
                // Handle client exit
                break;
            default:
                sendMessage("Error: Unknown command");
        }
    }

    private void handleOrderCommand(StringTokenizer tokenizer) {
        int numTeas = 0;
        int numCoffees = 0;

        while (tokenizer.hasMoreTokens()) {
            int quantity = Integer.parseInt(tokenizer.nextToken());
            String type = tokenizer.nextToken();

            if (type.equalsIgnoreCase("tea")) {
                numTeas += quantity;
            } else if (type.equalsIgnoreCase("coffee")) {
                numCoffees += quantity;
            }
        }

        brewingTeas += numTeas;
        brewingCoffees += numCoffees;

        cafeServer.broadcast("order received for " + clientName + " (" + numTeas + " teas and " + numCoffees + " coffees)");

        cafeServer.updateCafeState(cafeServer.getUpdatedCafeState());
        cafeServer.broadcastState();
    }

    private void handleOrderStatusCommand() {
        sendMessage("Order status for " + clientName + ":");
        sendMessage("- " + brewingTeas + " teas and " + brewingCoffees + " coffees in waiting area");
        sendMessage("- " + trayTeas + " teas and " + trayCoffees + " coffees currently in the tray");
    }

	public boolean isWaiting() {
		// TODO Auto-generated method stub
		return false;
	}
}
