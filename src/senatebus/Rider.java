package senatebus;


public class Rider extends Thread {

    private final int id;       // unique identifier of the rider

    public Rider(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        super.run();

        try {
            System.out.println("Rider " + this.id + " arrived at bus stop");
            Main.getMultiplexSemaphore().acquire();     // reduce the riders who can enter the boarding area after this particular rider enters

                Main.getMutexSemaphore().acquire();     // get mutex semaphore to stop concurrent access to the riders count variable
                    Main.incrementRiders();             // increment the no. of waiting riders in the boarding area
                    System.out.println("Rider " + this.id + " entered boarding area and waiting");
                Main.getMutexSemaphore().release();     // release mutex semaphore after incrementing riders count variable

                Main.getBusSemaphore().acquire();       // wait till the bus arrives and wake up one of the riders so it can board

            Main.getMultiplexSemaphore().release();     // allow another rider to enter the boarding area after this particular rider boards to the bus

            boardBus();                                 // notifying when rider is boarding to the bus

            Main.decrementRiders();                     // decrement the no. of riders waiting in the boarding area after boarding to the bus

            if (Main.getRiders() == 0) {
                Main.getAllBoardSemaphore().release();  // after the final rider is boarded it releases getAllBoard semaphore to wake the bus thread
            } else {
                Main.getBusSemaphore().release();       // if the rider is not the final waiting rider it wakes up another rider so that it also can board
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void boardBus() {
        System.out.println("Rider " + this.id + " boarded the bus");
    }


}
