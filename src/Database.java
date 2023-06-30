import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Database extends ReentrantLock {
    private Map<String, String> data;
    private Condition canRead;
    private Condition canWrite;
    private Lock lock;
    private int howManyReaders;
    private int maxReaders;
    private boolean writer;
    private final Thread threadWriter;
    private Set<Thread> threadReaders;


    public Database(int maxNumOfReaders) {
        data = new HashMap<>();
        lock = new ReentrantLock();
        canRead = lock.newCondition();
        canWrite = lock.newCondition();
        howManyReaders = 0;
        maxReaders = maxNumOfReaders;
        writer = false;
        threadWriter = null;
        threadReaders = new HashSet<>();


    }

    public void put(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    public boolean readTryAcquire() {
        lock.lock();
        try {
            if (writer || howManyReaders >= maxReaders) {
                return false;
            } else {
                howManyReaders++;
                threadReaders.add(Thread.currentThread());
                return true;
            }
        } finally {
            lock.unlock();
        }
    }
        public void readAcquire() {
            lock.lock();
            try {
                while (writer || howManyReaders >= maxReaders ) {
                    canRead.await(); // Wait until no writer is active
                }
                howManyReaders++;
                threadReaders.add(Thread.currentThread());
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();

            }

            finally {
                lock.unlock();
            }
        }

    public void readRelease(){
        lock.lock();
        try {
            if (howManyReaders == 0 || !threadReaders.contains(Thread.currentThread())) {
                throw new IllegalMonitorStateException("Attempted release without active read");
            }
            howManyReaders--;
            threadReaders.remove(Thread.currentThread());
            if (howManyReaders < maxReaders) {
                canRead.notifyAll(); // Notify waiting readers that is a place to read
            }
        }
        catch (IllegalMonitorStateException e){
            Thread.currentThread().interrupt();

        }
        finally {
            lock.unlock();
        }
    }



    public void writeAcquire(){
        lock.lock();
        try {
            while (howManyReaders > 0 || writer) {
                canWrite.await(); // Wait until no readers or writer is active
            }
            writer = true;
        }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();

            }
        finally {
            lock.unlock();
        }
    }

    public boolean writeTryAcquire() {
        lock.lock();
        try {
            if (howManyReaders > 0 || writer) {
                return false;
            } else {
                writer = true;
                return true;
            }
        } finally {
            lock.unlock();
        }
    }



    public void writeRelease() {
        lock.lock();
        try {
            if (!writer) {
                throw new IllegalMonitorStateException("Attempted release without active write");
            }
            writer = false;
            canWrite.notifyAll(); // Notify waiting threads that a write operation has finished
            canRead.notifyAll(); // Notify waiting readers that a write operation has finished
        }
        catch (IllegalMonitorStateException e){
            Thread.currentThread().interrupt();

        }
        finally {
            lock.unlock();
        }
    }
}
