package cn.org.opendfl.tasktool;

import cn.org.opendfl.tasktool.utils.LockCallUtils;
import cn.org.opendfl.tasktool.utils.LockCallback;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class LockCallUtilsTest {

    @Test
    public void lockCall() throws Exception {
        long time = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        final CountDownLatch countDownLatch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            final int index = i;
            final String key = "" + (i % 50);
            executorService.execute(() -> {
                Object value = LockCallUtils.lockCall("lockCall", key, new LockCallback() {
                    @Override
                    public Object callback(String lockKey) {
                        try {
                            Thread.sleep(100L);
                            countDownLatch.countDown();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        return key;
                    }
                });
                System.out.println(index + "=" + value + " " + Thread.currentThread().getId());
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("---time=" + (System.currentTimeMillis() - time));
    }

    @Test
    public void testThreadPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(() -> log.info("task:{}", index));
        }
        executorService.shutdown();
    }
}
