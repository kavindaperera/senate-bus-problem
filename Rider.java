public class Rider extends Thread {

    private final int id;

    public Rider(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        super.run();

        try {

            Main.getMultiplexSemaphore().acquire();
            Main.getMutexSemaphore().acquire();
            Main.incrementRiders();
            Main.getMutexSemaphore().release();

            System.out.println("Rider " + this.id + " is Waiting");

            Main.getBusSemaphore().acquire();
            Main.getMultiplexSemaphore().release();

            System.out.println("Rider " + this.id + " is Boarding");

            Main.decrementRiders();

            if (Main.getRiders() == 0) {
                Main.getAllBoardSemaphore().release();
            } else {
                Main.getBusSemaphore().release();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
