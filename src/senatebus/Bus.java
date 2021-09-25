package senatebus;

public class Bus extends Thread {

    private final int id;     // unique identifier of the bus

    public Bus(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        super.run();

        try {

            Main.getMutexSemaphore().acquire();      // get the mutex semaphore so that riders can't increment their count after the bus arrives

            System.out.println("Bus " + this.id + " Arrived");
            System.out.println("Waiting for " + Main.getRiders() + " riders");

            if (Main.getRiders() > 0) {
                Main.getBusSemaphore().release();        // waking up one of the blocked rider threads
                Main.getAllBoardSemaphore().acquire();   // waiting until all the waiting riders board to the bus
            }

            Main.getMutexSemaphore().release();          // release the mutex semaphore when the bus is ready to leave (all waiting riders have boarded)

            depart();                                    // notifying the bus is leaving

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void depart() {
        System.out.println("Bus " + this.id + " Departed");
    }
}
