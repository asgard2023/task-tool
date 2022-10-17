package cn.org.opendfl.tasktool.thread;

import cn.hutool.core.thread.ThreadUtil;
import cn.org.opendfl.tasktool.constant.DateTimeConstant;
import cn.org.opendfl.tasktool.task.TaskToolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 方法调用次数保存线程
 *
 * @author chenjh
 */
public class TaskCountSaveThread implements Runnable {
    static Logger logger = LoggerFactory.getLogger(TaskCountSaveThread.class);
    private static volatile Long saveTime = 0L;
    private static final int INTERVAL_SAVE_TIME = TaskCountSaveThreadTask.SAVE_INTERVAL/5;
    private Object lock = new Object();

    private ITaskCountSaveBiz taskCountSaveBiz;

    public TaskCountSaveThread(ITaskCountSaveBiz taskCountSaveBiz) {
        this.taskCountSaveBiz = taskCountSaveBiz;
    }

    public void saveCount() {
        Long time = System.currentTimeMillis();
        long nextRunTime = INTERVAL_SAVE_TIME - (time - saveTime) - 100;
        if (nextRunTime > DateTimeConstant.SECOND_MILLIS) {
            ThreadUtil.sleep(2000L);
        }
        if (time - saveTime > INTERVAL_SAVE_TIME) {
            synchronized (lock) {
                saveTime = time;
                saveCountToDb();
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    logger.error("----saveCountJob error={}", e.getMessage(), e);
                }
            }
        }
    }

    public void saveCountToDb() {
        try {
            TaskToolUtils.saveTaskCounts(taskCountSaveBiz);
        } catch (Exception e) {
            logger.warn("-----saveCountToDb--error={}", e.getMessage(), e);
        }
    }

    public void notifyRun() {
        synchronized (lock) {
            this.lock.notify();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                saveCount();
            } catch (Exception e) {
                logger.error("----saveUserVisitTimeBatchJob error={}", e.getMessage(), e);
            }
        }
    }
}
