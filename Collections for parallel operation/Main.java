import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final int TEXTS_TO_GENERATE = 10_000;
    private static final int TEXT_LENGTH = 100_000;
    private static final int QUEUE_CAPACITY = 100;

    public static final BlockingQueue<String> queueA = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
    public static final BlockingQueue<String> queueB = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
    public static final BlockingQueue<String> queueC = new LinkedBlockingQueue<>(QUEUE_CAPACITY);

    public static final AtomicInteger maxCountA = new AtomicInteger(0);
    public static final AtomicInteger maxCountB = new AtomicInteger(0);
    public static final AtomicInteger maxCountC = new AtomicInteger(0);

    public static String maxTextA = null;
    public static String maxTextB = null;
    public static String maxTextC = null;

    public static void main(String[] args) {
        Thread producer = new Thread(new Producer(TEXTS_TO_GENERATE, TEXT_LENGTH));

        Thread consumerA = new Thread(new Consumer('a', queueA, maxCountA));
        Thread consumerB = new Thread(new Consumer('b', queueB, maxCountB));
        Thread consumerC = new Thread(new Consumer('c', queueC, maxCountC));

        producer.start();
        consumerA.start();
        consumerB.start();
        consumerC.start();

        try {
            producer.join();
            consumerA.interrupt();
            consumerB.interrupt();
            consumerC.interrupt();

            consumerA.join();
            consumerB.join();
            consumerC.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Max 'a' count: " + maxCountA.get());
        System.out.println("Max 'b' count: " + maxCountB.get());
        System.out.println("Max 'c' count: " + maxCountC.get());
    }
}
