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
                    System.out.println("Rider " + this.id + " is Waiting");
                Main.getMutexSemaphore().release();

                Main.getBusSemaphore().acquire();
            Main.getMultiplexSemaphore().release();

            boardBus();

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

    private void boardBus() {
        System.out.println("Rider " + this.id + " Boarded");
    }


}
