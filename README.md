项目介绍(目前处于不稳定迭代开发中,当前基于springboo2.X开发)：
-------------------------------------

common-log-utils是一个springboot使用logback的log日志组件<br>
包含接口请求的输入和响应、ip记录、日志脱敏及日志文件记录  


有哪些特性？  
-----
* 对接口http请求的输入和响应进行日志记录，屏蔽特定日志(base64或者心跳请求等)
* 开箱即用，只需要引入依赖即可使用
* 日志链路追踪，支持MDC
* 日志策略分环境，可以自己对不同环境日志策略进行定制
* 对请求日志部分字段脱敏

极速开始
-------------------------------------
### 添加Maven依赖
直接添加以下maven依赖即可

```xml
<dependency>
    <groupId>io.github.nicholaszhou</groupId>
    <artifactId>common-log-starter</artifactId>
    <version>0.0.1.13</version>
</dependency>
```
完成以上步骤，即可实现上述功能
### 配置方法
#### 例： 关闭整体功能,默认开启
```
common:
  log:
    enable: false
```
#### 详细配置方法
```
common:
  log:
    enable: true  #是否开启日志
    trace:
      enable: true   #是否开启跟踪日志
    disable-path:
      - log:
          disable-req: false  #是否禁用请求日志输出
          disable-resp: false #是否禁用响应日志输出
        path: /test #需要过滤的匹配路径
      - log:
          disable-req: false
          disable-resp: false
        path: /do* #需要过滤的匹配路径
    sensitization-properties:
      sensitization-fields:
        - mobile
        - idCard
```

| Ok?                | 状态  | 功能            | 描述                                                                                                                            |
|:-------------------|:----|:--------------|:------------------------------------------------------------------------------------------------------------------------------|
| &nbsp;&nbsp;&nbsp; | 进行中 | 输入输出日志记录      | 对接口的http输入和输出请求记录，并且可以屏蔽指定规则的接口,代码屏蔽方式正在完善                                                                                    | 
| :white_check_mark: | 已完成 | 请求trace追踪     | 基于logback的mdc实现，MDC属性为: tid,基于 [transmittable-thread-local](https://github.com/alibaba/transmittable-thread-local)实现异步上下文传递问题 | 
| :white_check_mark: | 已完成 | 日志请求ip记录      | 依赖nginx向后传递原始请求ip                                                                                                             | 
| &nbsp;&nbsp;&nbsp; | 进行中 | 日志脱敏          | 基于ClassicConverter实现，目前内置了几个关键字段的脱敏，灵活配置待完善                                                                                   |
| :white_check_mark: | 已完成 | 日志策略区分环境      | 内置logback-spring.xml，可以参照修改，如果不指定                                                                                             |
| :white_check_mark: | 已完成 | 普通日志及错误日志记录   | 内置logback-spring.xml，可以参照修改                                                                                                   |
| &nbsp;&nbsp;&nbsp; | 未开始 | 支持springboot3 | 适配springboo3版本                                                                                                                |
| &nbsp;&nbsp;&nbsp; | 未开始 | 异步性能测试        | 异步AsyncAppender对性能的提升                                                                                                   |
