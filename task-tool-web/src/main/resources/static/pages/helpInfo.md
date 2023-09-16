## 属性说明
* 来源次数统计，此接口当前时段的不同用户的请求次数。
* 正在处理的数据，此接口当前正在执行的求，处理完成则自动消失。
## 配置
```yml
task-tool:
  controller-config:
    #是否启用web接口统计，默认true
    enable: true
    #是否记录请求来源(按请求参数分别累计)次数，默认false
    source: true
    #是否显示正在处理的请求，默认false
    processing: true
    #uri接口白名单，可忽略计算处理
    uriWhitelist: none
    # 用于获取用户ID
    userIdField: userId
    # 用于获取数据ID(异常数据dataId：优先取dataId，取不到取userId，再取不到取用户IP)
    dataIdField: dataId
    # 支持包名配置，*表示全部，多个则包名以package开头算匹配
    packages:
      - '*'
```