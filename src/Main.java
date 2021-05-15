import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static AtomicInteger hydrogenReceiver = new AtomicInteger(0);
    public static AtomicInteger oxygenReceiver = new AtomicInteger(0);
    public static AtomicBoolean releaseOxy = new AtomicBoolean(false);
    public static AtomicBoolean releaseHydro = new AtomicBoolean(false);
    public static String string = "";
    public static Lock lock = new ReentrantLock();

    public static void main(String[] args) {

        ExecutorService executor1 = Executors.newFixedThreadPool(10);
        ExecutorService executor2 = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 1000; i++) {
            executor1.execute(() -> {
                lock.lock();
                if (oxygenReceiver.get() < 1) {
                    oxygenReceiver.incrementAndGet();
                }
                else if (oxygenReceiver.get() == 1 && !releaseOxy.get()) {
                    releaseOxy.set(true);
                    releaseOxygen();
                }
                lock.unlock();
            });

            executor2.execute(() -> {
                lock.lock();
                if (hydrogenReceiver.get() < 2) {
                    hydrogenReceiver.incrementAndGet();
                }
                else if (hydrogenReceiver.get() == 2 && !releaseHydro.get()) {
                    releaseHydro.set(true);
                    releaseHydrogen();
                }
                lock.unlock();
            });
        }

        executor1.shutdown();
        executor2.shutdown();
    }

    public static void releaseHydrogen(){
        pshhhh("HH");
    }

    public static void releaseOxygen(){
        pshhhh("O");

    }

    public static void pshhhh(String element) {
        if (releaseOxy.get() && releaseHydro.get()) {
            string += element;
            System.out.println(string + "\n");
            string = "";
            hydrogenReceiver.set(0);
            oxygenReceiver.set(0);
            releaseOxy.set(false);
            releaseHydro.set(false);
        }
        else {
            string += element;
        }
    }
}