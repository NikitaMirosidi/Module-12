import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Test2 {
    private static final CyclicBarrier barrier = new CyclicBarrier(3);
    private long oxyCount;
    private long hydroCount;

    public Test2(String inputString) {
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
                        barrier.await();
                        releaseHydrogen();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    hydroDecrement();
                }
            }).start();
        }

        new Thread(() -> {
            while (oxyCount > 0) {
                try {
                    barrier.await();
                    releaseOxygen();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                oxyDecrement();
            }
        }).start();
    }

    private void releaseOxygen() {
        System.out.println("O");
    }

    private void releaseHydrogen() {
        System.out.println("H");
    }

    public void oxyDecrement() {
        oxyCount--;
    }

    public void hydroDecrement() {
        hydroCount--;
    }
}