package cn.org.opendfl.tasktool.task;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 路由信息
 *
 * @author chenjh
 */
@Data
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class RouteApiVo {
    /**
     * base apiUrl
     */
    @NonNull
    private String apiUrl;
    @NonNull
    private String authKey;
    @NonNull
    private String source;
    private String ip;
}
