public class CafeState {

    private int waitingOrders;
    private int brewingTeas;
    private int brewingCoffees;
    private int trayTeas;
    private int trayCoffees;

    public CafeState() {
        // Initialize state as needed
    }

    public CafeState(int waitingOrders, int brewingTeas, int brewingCoffees, int trayTeas, int trayCoffees) {
        this.waitingOrders = waitingOrders;
        this.brewingTeas = brewingTeas;
        this.brewingCoffees = brewingCoffees;
        this.trayTeas        = trayTeas;
        this.trayCoffees = trayCoffees;
    }

    public int getWaitingOrders() {
        return waitingOrders;
    }

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
}

