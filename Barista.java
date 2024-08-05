import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Barista {

    private List<ClientHandler> clients = new ArrayList<>();
    private CafeState cafeState = new CafeState();

    public static void main(String[] args) {
    	Barista cafeServer = new Barista();
        cafeServer.start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Cafe server is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public synchronized CafeState getCafeState() {
        return cafeState;
    }

    public synchronized void updateCafeState(CafeState state) {
        this.cafeState = state;
    }

    public synchronized void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        updateCafeState(getUpdatedCafeState());
        broadcastState();
    }

    synchronized CafeState getUpdatedCafeState() {
        int waitingOrders = 0;
        int brewingTeas = 0;
        int brewingCoffees = 0;
        int trayTeas = 0;
        int trayCoffees = 0;

        for (ClientHandler client : clients) {
            waitingOrders += client.isWaiting() ? 1 : 0;
            brewingTeas += client.getBrewingTeas();
            brewingCoffees += client.getBrewingCoffees();
            trayTeas += client.getTrayTeas();
            trayCoffees += client.getTrayCoffees();
        }

        return new CafeState(waitingOrders, brewingTeas, brewingCoffees, trayTeas, trayCoffees);
    }

    synchronized void broadcastState() {
        CafeState currentState = getCafeState();
        System.out.println("Current Cafe State:");
        System.out.println("Clients in the cafe: " + clients.size());
        System.out.println("Clients waiting for orders: " + currentState.getWaitingOrders());
        System.out.println("Waiting area - Teas: " + currentState.getBrewingTeas() +
                           ", Coffees: " + currentState.getBrewingCoffees());
        System.out.println("Tray area - Teas: " + currentState.getTrayTeas() +
                           ", Coffees: " + currentState.getTrayCoffees());
        System.out.println("------------------------------");
    }
}
