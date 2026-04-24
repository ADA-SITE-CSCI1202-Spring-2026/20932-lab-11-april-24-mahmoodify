// Step 1: Shared Buffer
class SharedResource {
    private int value;
    private boolean bChanged = false;

    public synchronized int get() {
        while (!bChanged) {
            try {
                wait(); // Step 2: Wait logic
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // restore interrupt status
                System.out.println("Consumer interrupted");
            }
        }
        bChanged = false; // reset flag after consuming
        notify();         // wake up producer if waiting
        return value;
    }

    public synchronized void set(int newValue) {
        while (bChanged) { // wait until consumer has taken the old value
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Producer interrupted");
            }
        }
        value = newValue;
        bChanged = true;
        notify(); // Step 3: Notify consumer
    }
}

class Producer implements Runnable {
    private final SharedResource resource;

    Producer(SharedResource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            resource.set(i);
            System.out.println("Produced: " + i);
        }
    }
}

class Consumer implements Runnable {
    private final SharedResource resource;

    Consumer(SharedResource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            int val = resource.get();
            System.out.println("Consumed: " + val);
        }
    }
}

public class ProducerConsumerDemo {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        Thread producerThread = new Thread(new Producer(resource));
        Thread consumerThread = new Thread(new Consumer(resource));

        producerThread.start();
        consumerThread.start();
    }
}
