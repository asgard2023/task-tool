package cn.org.opendfl.tasktool.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 分段锁工具类
 *
 * @author chenjh
 */
@Slf4j
public class LockCallUtils {
    private LockCallUtils() {

    }

    private static final SegmentLock<String> segmentLock = new SegmentLock<>();

    /**
     * 超过3秒，就显示告警日志
     */
    private static final int LOG_WARN_RUN_TIME = 3000;

    /**
     * @param methodName   方法名，用于知道是哪个方法调用了此接口
     * @param key          锁的key
     * @param lockCallback 回调接口
     * @return
     */
    public static Object lockCall(String methodName, String key, LockCallback lockCallback) {
        long time = System.currentTimeMillis();
        ReentrantLock lock = segmentLock.get(key);
        lock.lock();
        try {
            Object object = lockCallback.callback(key);
            long runTime = System.currentTimeMillis() - time;
            if (runTime > LOG_WARN_RUN_TIME) {
                log.warn("----lockCall--methodName={} key={} runTime={}", methodName, key, runTime);
            }
            return object;
        } catch (Exception e) {
            log.error("----lockCall--methodName={} key={}", methodName, key);
            return null;
        } finally {
            lock.unlock();
        }
    }
}
