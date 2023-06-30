import java.util.HashMap;
import java.util.Map;

/**
 * A simulated database class that allows multiple readers and a single writer to access the data.
 */
public class Database {
    private Map<String, String> data;
    private int howManyReaders = 0;
    private int maxReaders;
    private boolean writer = false;

    /**
     * Constructs a new Database instance with the specified maximum number of readers.
     *
     * @param maxNumOfReaders The maximum number of readers allowed to access the database concurrently.
     */
    public Database(int maxNumOfReaders) {
        data = new HashMap<>();
        maxReaders = maxNumOfReaders;
    }

    /**
     * Puts the specified key-value pair into the database.
     *
     * @param key   The key to be inserted.
     * @param value The value to be associated with the key.
     */
    public void put(String key, String value) {
        data.put(key, value);
    }

    /**
     * Retrieves the value associated with the specified key from the database.
     *
     * @param key The key whose associated value is to be retrieved.
     * @return The value associated with the key, or null if the key is not found.
     */
    public String get(String key) {
        return data.get(key);
    }

    /**
     * Tries to acquire a read lock on the database.
     * This method allows a thread to check if it can acquire a read lock without blocking.
     *
     * @return true if the read lock is acquired, false otherwise.
     */
    public boolean readTryAcquire() {
        synchronized (this) {
            if (howManyReaders < maxReaders && !writer) {
                howManyReaders++;
                return true;
            }
            return false;
        }
    }

    /**
     * Acquires a read lock on the database.
     * This method blocks the calling thread until it can acquire the read lock.
     */
    public void readAcquire() {
        synchronized (this) {
            while (writer || howManyReaders >= maxReaders) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            howManyReaders++;
        }
    }

    /**
     * Releases the read lock on the database.
     * This method must be called by a thread that previously acquired the read lock.
     * It notifies waiting threads if there are no more readers.
     */
    public void readRelease() {
        synchronized (this) {
            if (howManyReaders <= 0) {
                throw new IllegalMonitorStateException("Illegal read release attempt");
            }
            howManyReaders--;
            if (howManyReaders == 0) {
                notifyAll();
            }
        }
    }

    /**
     * Acquires a write lock on the database.
     * This method blocks the calling thread until it can acquire the write lock.
     */
    public void writeAcquire() {
        synchronized (this) {
            while (writer || howManyReaders > 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            writer = true;
        }
    }

    /**
     * Tries to acquire a write lock on the database.
     * This method allows a thread to check if it can acquire a write lock without blocking.
     *
     * @return true if the write lock is acquired, false otherwise.
     */
    public boolean writeTryAcquire() {
        synchronized (this) {
            if (!writer && howManyReaders == 0) {
                writer = true;
                return true;
            }
            return false;
        }
    }

    /**
     * Releases the write lock on the database.
     * This method must be called by a thread that previously acquired the write lock.
     * It notifies waiting threads that the writer has released the lock.
     */
    public void writeRelease() {
        synchronized (this) {
            if (!writer) {
                throw new IllegalMonitorStateException("Illegal write release attempt");
            }
            writer = false;
            notifyAll();
        }
    }
}
