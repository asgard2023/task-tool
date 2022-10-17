package cn.org.opendfl.tasktool.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 细粒度-分段锁
 *
 * @param <T>
 */
public class SegmentLock<T> {
    /**
     * 默认预先创建的锁数量.
     */
    private static final int DEFAULT_LOCK_COUNT = 32;


    private int lockCount = DEFAULT_LOCK_COUNT;

    private final Map<Integer, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public SegmentLock() {
        init(null, false);
    }

    public SegmentLock(Integer count, boolean isFair) {
        init(count, isFair);
    }

    private void init(Integer count, boolean isFair) {
        if (count != null && count != 0) {
            this.lockCount = count;
        }
        // 预先初始化指定数量的锁
        for (int i = 0; i < this.lockCount; i++) {
            this.lockMap.put(i, new ReentrantLock(isFair));
        }
    }

    public ReentrantLock get(T key) {
        return this.lockMap.get((key.hashCode() >>> 1) % lockCount);
    }

    public void lock(T key) {
        ReentrantLock lock = this.get(key);
        lock.lock();
    }

    public void unlock(T key) {
        ReentrantLock lock = this.get(key);
        lock.unlock();
    }
}