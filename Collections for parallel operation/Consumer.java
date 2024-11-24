import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {
    private final char targetChar;
    private final BlockingQueue<String> queue;
    private final AtomicInteger maxCount;

    public Consumer(char targetChar, BlockingQueue<String> queue, AtomicInteger maxCount) {
        this.targetChar = targetChar;
        this.queue = queue;
        this.maxCount = maxCount;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String text = queue.take();
                int count = (int) text.chars().filter(ch -> ch == targetChar).count();
                synchronized (maxCount) {
                    if (count > maxCount.get()) {
                        maxCount.set(count);
                        if (targetChar == 'a') {
                            Main.maxTextA = text;
                        } else if (targetChar == 'b') {
                            Main.maxTextB = text;
                        } else if (targetChar == 'c') {
                            Main.maxTextC = text;
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

