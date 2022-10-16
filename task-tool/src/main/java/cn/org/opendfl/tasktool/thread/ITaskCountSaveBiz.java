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

    int cleanExpirekey(List<String> expiredKeys);
}
