package cn.org.opendfl.tasktool.thread;

import cn.org.opendfl.tasktool.config.vo.TaskCountTypeVo;
import cn.org.opendfl.tasktool.task.TaskCountVo;

import java.util.List;

/**
 * 方法调用次数等信息保存
 *
 * @author chenjh
 */
public interface  ITaskCountSaveBiz {

    int saveTaskCount(TaskCountTypeVo countType, TaskCountVo taskCountVo);

    /**
     * 清理过期的缓存key
     *
     * @param expiredKeys
     * @return 清理的个数
     */
    int cleanExpirekey(List<String> expiredKeys);
}
