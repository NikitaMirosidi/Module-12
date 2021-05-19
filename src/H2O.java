import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class H2O {
    private int i = 0;
    private final Lock LOCK = new ReentrantLock();
    private final CyclicBarrier BARRIER = new CyclicBarrier(3, () -> {
        i ++;
        System.out.println("\nВодичка пошла " + i + ": ");
    });

    private long oxyCount;
    private long hydroCount;

    public H2O(String inputString) {
        this.oxyCount = inputString.chars().
                filter(s -> s == (int) 'O').
                count();
        this.hydroCount = inputString.chars().
                filter(s -> s == (int) 'H').
                count();
    }

    public void process() {
        for (int i = 0; i <= 1; i++) {
            new Thread(() -> {
                while (hydroCount > 0) {
                    try {
                        BARRIER.await(100, TimeUnit.MILLISECONDS);
                        releaseHydrogen();
                    } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                        System.out.println();
                        break;
                    }
                    LOCK.lock();
                    hydroCount--;
                    LOCK.unlock();
                }
            }).start();
        }

        new Thread(() -> {
            while (oxyCount > 0) {
                try {
                    BARRIER.await(100, TimeUnit.MILLISECONDS);
                    releaseOxygen();
                } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                    System.out.println();
                    break;
                }
                oxyCount--;
            }
        }).start();
    }

    private void releaseOxygen() {
        System.out.print("O");
    }

    private void releaseHydrogen() {
        System.out.print("H");
    }
}