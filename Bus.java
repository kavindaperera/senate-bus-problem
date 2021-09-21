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

            if (Main.getRiders() > 0) {
                System.out.println("Waiting for " + Main.getRiders() + " riders");
                Main.getBusSemaphore().release();
                Main.getAllBoardSemaphore().acquire();
            }

            Main.getMutexSemaphore().release();

            System.out.println("Bus " + this.id + " Departed");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
