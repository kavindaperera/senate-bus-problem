package senatebus;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {

    private static Random random = new Random();

    private static int MAX_RIDERS = 50;
    private static final float arrivalTimeBusses = 20 * 60 * 100; //10->1000
    private static final float arrivalTimeRiders = 30 * 100; //10->1000

    private static int riders = 0;
    private static final Semaphore mutex = new Semaphore(1);
    private static final Semaphore multiplex = new Semaphore(MAX_RIDERS);
    private static final Semaphore bus = new Semaphore(0);
    private static final Semaphore allBoard = new Semaphore(0);

    public static void main(String[] args) {

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


    static class RiderCreator extends Thread {

        int riderNumber = 1;

        @Override
        public void run() {
            super.run();

            System.out.println("senatebus.Rider Creator Started...");

            while (!Thread.currentThread().isInterrupted()) {

                new Rider(riderNumber).start();
                riderNumber++;

                try {
                    Thread.sleep(getExpoDistributedTimeFromMeanTime(Main.arrivalTimeRiders));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    static class BusCreator extends Thread {

        int busNumber = 1;

        @Override
        public void run() {
            super.run();

            System.out.println("senatebus.Bus Creator Started...");

            while (!Thread.currentThread().isInterrupted()) {

                try {
                    Thread.sleep(getExpoDistributedTimeFromMeanTime(Main.arrivalTimeBusses));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                new Bus(busNumber).start();
                busNumber++;

            }

        }
    }

    private static long getExpoDistributedTimeFromMeanTime(float arrivalMeanTime) {
        float lambda = 1 / arrivalMeanTime;
        float p = random.nextFloat();
        return Math.round(Math.log(1 - p) / (-lambda));
    }

}