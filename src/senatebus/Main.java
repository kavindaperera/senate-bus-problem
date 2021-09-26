package senatebus;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {

    private static final Random random = new Random();

    private static final int MAX_RIDERS = 50;
    private static final float arrivalTimeBusses = 20 * 60 * 10; // TODO : 10->1000
    private static final float arrivalTimeRiders = 30 * 10; //TODO : 10->1000

    private static int riders = 0;                                        // to keep track of the no. of riders waiting in the boarding area
    private static final Semaphore mutex = new Semaphore(1);       // to protect the riders variable from concurrent of riders
    private static final Semaphore multiplex = new Semaphore(MAX_RIDERS); // limit the maximum number of riders allowed in the boarding area
    private static final Semaphore bus = new Semaphore(0);         // for the riders to wait until a bus arrives
    private static final Semaphore allBoard = new Semaphore(0);    // for the bus to wait until all the riders in the boarding area are boarded

    public static void main(String[] args) {

        System.out.println("Simulation Started...");

        new RiderCreator().start();
        new BusCreator().start();

    }

    public static Semaphore getMutexSemaphore() {
        return mutex;
    }

    public static Semaphore getMultiplexSemaphore() {
        return multiplex;
    }

    public static Semaphore getBusSemaphore() {
        return bus;
    }

    public static Semaphore getAllBoardSemaphore() {
        return allBoard;
    }

    public static int getRiders() {
        return riders;
    }

    public static void incrementRiders() {
        riders++;
    }

    public static void decrementRiders() {
        riders--;
    }

    // This class is a support class to create Rider threads with their
    // inter-arrival times following an exponential distribution of given mean
    static class RiderCreator extends Thread {

        int riderNumber = 1;       // to give a unique identifier to a bus

        @Override
        public void run() {
            super.run();

            while (!Thread.currentThread().isInterrupted()) {

                new Rider(riderNumber).start();     // create a new rider at next arrival time
                riderNumber++;

                try {
                    Thread.sleep(exponential(Main.arrivalTimeRiders));  // sleep the thread for a certain time (inter arrival time)
                                                                        // until the arrival time of the next rider occurs
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    // This class is a support class to create Bus threads with their
    // inter-arrival times following an exponential distribution of given mean
    static class BusCreator extends Thread {

        int busNumber = 1;      // to give a unique identifier to a bus

        @Override
        public void run() {
            super.run();

            while (!Thread.currentThread().isInterrupted()) {

                try {
                    Thread.sleep(exponential(Main.arrivalTimeBusses)); // sleep the thread for a certain time (inter arrival time)
                                                                       // until the arrival time of the next bus occurs
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                new Bus(busNumber).start();    // create a new bus at next arrival time
                busNumber++;

            }

        }
    }

    private static long exponential(float arrivalMeanTime) { // inverse transform sampling
        float lambda = 1 / arrivalMeanTime; // rate
        float p = random.nextFloat(); // generate uniform random number in [0,1]
        return Math.round(Math.log(1 - p) / (-lambda)); // return exponentially distributed value
    }

}
