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

    public List<TaskHostVo> getHosts(TaskHostVo search, Date startDate, Date endDate, final PageVO page);

    public default String getApiUrl(TaskHostVo taskHostVo) {
        String url = taskHostVo.getIp();
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        return url + ":" + taskHostVo.getPort();
    }

    public default RouteApiVo getRouteApi(String taskHostCode) {
        TaskHostVo taskHostVo = this.getTaskHost(taskHostCode);
        String apiUrl = getApiUrl(taskHostVo);
        return RouteApiVo.of(apiUrl, taskHostVo.getAuthKey(), "tasktool");
    }
}
