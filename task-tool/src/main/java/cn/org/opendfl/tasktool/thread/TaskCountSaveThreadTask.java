package cn.org.opendfl.tasktool.thread;

import cn.org.opendfl.tasktool.constant.DateTimeConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 方法调用次数保存线程管理器
 *
 * @author chenjh
 */
public class TaskCountSaveThreadTask {
    static Logger logger = LoggerFactory.getLogger(TaskCountSaveThreadTask.class);
    private Thread cacheThread = null;
    private TaskCountSaveThread taskCountSaveThread = null;
    private AtomicInteger aCounter = new AtomicInteger();

    private TaskCountSaveThreadTask() {

    }

    private static TaskCountSaveThreadTask instance = new TaskCountSaveThreadTask();

    private static ITaskCountSaveBiz taskCountSaveBiz;

    public static void setTaskCountSaveBiz(ITaskCountSaveBiz taskCountSaveBiz) {
        TaskCountSaveThreadTask.taskCountSaveBiz = taskCountSaveBiz;
    }

    private static TaskCountSaveThreadTask taskCountSaveThreadTask = null;
    private static volatile Long saveTime = 0L;
    /**
     * 30秒保存一次
     */
    public static final int SAVE_INTERVAL = 30 * DateTimeConstant.SECOND_MILLIS;

    /**
     * 通知异步线程进行保存，每30秒一次
     */
    public static void saveTaskCount() {
        Long time = System.currentTimeMillis();
        if (time - saveTime > SAVE_INTERVAL) {
            saveTime = time;
            if (taskCountSaveThreadTask == null) {
                taskCountSaveThreadTask = TaskCountSaveThreadTask.getInstance();
                taskCountSaveThreadTask.start();
            } else {
                taskCountSaveThreadTask.notifyRun();
            }
        }
    }

    public static TaskCountSaveThreadTask getInstance() {
        if (instance.cacheThread==null) {
            synchronized (TaskCountSaveThreadTask.class) {
                if (instance.cacheThread == null) {
                    instance.init();
                }
            }
        }
        return instance;
    }

    private void init() {
        if (taskCountSaveThread == null) {
            taskCountSaveThread = new TaskCountSaveThread(taskCountSaveBiz);
        }
        if (cacheThread == null) {
            cacheThread = new Thread(taskCountSaveThread);
        }
    }

    public void start() {
        cacheThread.start();
    }

    public void notifyRun() {
        Thread.State state = cacheThread.getState();
        int count = 0;
        try {
            if (state == Thread.State.WAITING || state == Thread.State.TIMED_WAITING) {
                count = aCounter.incrementAndGet();
                this.taskCountSaveThread.notifyRun();
            }
        } catch (Exception e) {
            logger.error("-----notifyRun--count={} error={}", count, e.getMessage(), e);
        }
        //前30次每次都日志，后面则每20次输出一次日志
        if (count>0 && (count < 30 || count % 20 == 0)) {
            logger.debug("----notifyRun--count={} state={}/{}", count, state, cacheThread.getState());
        }
    }
}
