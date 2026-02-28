import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

class Fork{
    private final int id;
    private final ReentrantLock lock;

    public Fork(int id){
        this.id = id;
        lock = new ReentrantLock();
    }

    public void pickup(){
        lock.lock();
    }

    public void putDown(){
        lock.unlock();
    }

    public int getId(){
        return id;
    }
}

class Philosopher implements Runnable{
    private final int id;
    private final Fork firstFork;
    private final Fork secondFork;
    private final Semaphore tableSemaphore;
    private final long startTime;
    private final ReentrantLock printLock;

    public Philosopher(int id, Fork leftFork, Fork rightFork, Semaphore tableSemaphore, long startTime, ReentrantLock printLock){
        this.id = id;
        this.tableSemaphore = tableSemaphore;
        this.startTime = startTime;
        this.printLock = printLock;

        if (leftFork.getId() < rightFork.getId()) {
            this.firstFork = leftFork;
            this.secondFork = rightFork;
        } else {
            this.firstFork = rightFork;
            this.secondFork = leftFork;
        }
    }

    public void log(String message){
        printLock.lock();
        try {
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            int minutes = (int) (elapsedTime / 60);
            int seconds = (int) (elapsedTime % 60);
            System.out.printf("[%02d:%02d] Philosopher %d %s%n", minutes, seconds, id, message);
        } finally {
            printLock.unlock();
        }
    }

    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                long sleepTime = (long)(Math.random() * 1500) + 500;

                // Think
                log("is thinking.");
                Thread.sleep(sleepTime);

                // Hungry, waiting for a seat at the table
                tableSemaphore.acquire();

                // Pick up forks
                firstFork.pickup();
                log("picked up Fork " + firstFork.getId());
                secondFork.pickup();
                log("picked up Fork " + secondFork.getId());

                // Eat
                log("is eating.");
                Thread.sleep(sleepTime);

                // Put down forks
                secondFork.putDown();
                log("put down Fork " + secondFork.getId());
                firstFork.putDown();
                log("put down Fork " + firstFork.getId());

                // Back to thinking
                tableSemaphore.release();
                log("is thinking.");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

}
public class Main {
    static void main() throws InterruptedException {
        Fork[] forks = new Fork[5];
        Thread[] philosophers = new Thread[5];

        for (int i = 0; i < 5; i++) {
            forks[i] = new Fork(i);
        }

        Semaphore tableSemaphore = new Semaphore(4);
        ReentrantLock printLock = new ReentrantLock();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 5; i++) {
            philosophers[i] = new Thread(new Philosopher(i, forks[i], forks[(i+1)%5], tableSemaphore, startTime, printLock));
        }

        for (Thread philosopher : philosophers) {
            philosopher.start();
        }

        for (Thread philosopher : philosophers) {
            philosopher.join();
        }
    }
}

