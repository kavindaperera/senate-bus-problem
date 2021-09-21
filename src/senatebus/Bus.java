package senatebus;

public class Bus extends Thread {

    private final int id;

    public Bus(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        super.run();

        try {

            Main.getMutexSemaphore().acquire();

            System.out.println("Bus " + this.id + " Arrived");
            System.out.println("Waiting for " + Main.getRiders() + " riders");

            if (Main.getRiders() > 0) {
                Main.getBusSemaphore().release();
                Main.getAllBoardSemaphore().acquire();
            }

            Main.getMutexSemaphore().release();

            depart();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void depart() {
        System.out.println("Bus " + this.id + " Departed");
    }
}
