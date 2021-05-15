import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static int hydrogenReceiver = 0;
    public static int oxygenReceiver = 0;
    public static int counter = 0;
    public static boolean releaseOxy = false;
    public static boolean releaseHydro = false;
    public static String string = "";
    public static Lock lock = new ReentrantLock();
    public static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> {
                lock.lock();
                if (oxygenReceiver < 1) {
                    oxygenReceiver++;
                }
                else if (oxygenReceiver == 1 && !releaseOxy) {
                    releaseOxy = true;
                    releaseOxygen();
                }
                lock.unlock();
            });

            executor.execute(() -> {
                lock.lock();
                if (hydrogenReceiver < 1) {
                    releaseHydrogen();
                    hydrogenReceiver++;
                }
                else if (hydrogenReceiver == 1 && !releaseHydro) {
                    releaseHydro = true;
                    releaseHydrogen();
                }
                lock.unlock();
            });
        }

        executor.shutdown();
    }

    public static void releaseHydrogen(){
        pshhhh("H");
    }

    public static void releaseOxygen(){
        pshhhh("O");
    }

    public static void pshhhh(String element) {
        if (releaseOxy && releaseHydro) {
            string += element;
            counter++;
            System.out.println("Release № " + counter + ": " + string + "\n");
            string = "";
            hydrogenReceiver = 0;
            oxygenReceiver= 0;
            releaseOxy = false;
            releaseHydro = false;
        }
        else {
            string += element;
        }
    }
}