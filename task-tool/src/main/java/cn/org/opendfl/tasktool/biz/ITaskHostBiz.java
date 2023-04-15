package cn.org.opendfl.tasktool.biz;

import cn.org.opendfl.tasktool.base.PageVO;
import cn.org.opendfl.tasktool.task.RouteApiVo;
import cn.org.opendfl.tasktool.task.TaskHostVo;

import java.util.Date;
import java.util.List;

public interface ITaskHostBiz {
    public void addHost(TaskHostVo taskHost);
    public void save(TaskHostVo taskHost);

    public boolean delete(String code);

    public TaskHostVo getTaskHost(String code);

    public RouteApiVo getRouteApi(String taskHostCode);

    public List<TaskHostVo> getHosts(TaskHostVo search, Date startDate, Date endDate, final PageVO page);
}
