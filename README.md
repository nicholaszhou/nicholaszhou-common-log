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
#### 详细使用参考项目中demo

### roadmap

| Ok?                | 状态  | 功能                                  | 描述                                                                                                                            |
|:-------------------|:----|:------------------------------------|:------------------------------------------------------------------------------------------------------------------------------|
| &nbsp;&nbsp;&nbsp; | 进行中 | 输入输出日志记录                            | 对接口的http输入和输出请求记录，并且可以屏蔽指定规则的接口,代码屏蔽方式正在完善                                                                                    | 
| :white_check_mark: | 已完成 | 请求trace追踪                           | 基于logback的mdc实现，MDC属性为: tid,基于 [transmittable-thread-local](https://github.com/alibaba/transmittable-thread-local)实现异步上下文传递问题 | 
| :white_check_mark: | 已完成 | 日志请求ip记录                            | 依赖nginx向后传递原始请求ip                                                                                                             | 
| &nbsp;&nbsp;&nbsp; | 已完成 | 日志脱敏                                | 基于ClassicConverter实现，目前内置了几个关键字段的脱敏，灵活配置待完善                                                                                   |
| :white_check_mark: | 已完成 | 日志策略区分环境                            | 内置logback-spring.xml，可以参照修改，如果不指定                                                                                             |
| :white_check_mark: | 已完成 | 普通日志及错误日志记录                         | 内置logback-spring.xml，可以参照修改                                                                                                   |
| &nbsp;&nbsp;&nbsp; | 未开始 | logback-spring.xml配置可以通过bootstrap传值 |                                                                                                    || &nbsp;&nbsp;&nbsp; | 未开始 | 支持springboot3 | 适配springboo3版本                                                                                                                |
| &nbsp;&nbsp;&nbsp; | 未开始 | 异步性能测试                              | 异步AsyncAppender对性能的提升                                                                                                   |
### 常见问题
#### 1.类加载问题
```
Caused by: java.util.ServiceConfigurationError: io.github.nicholaszhou.desensitization.PatternReplaceConfig: Provider com.test.demo.support.CustomDesensitizationExtendConfigurer not a subtype
		at java.util.ServiceLoader.fail(ServiceLoader.java:239)
		at java.util.ServiceLoader.access$300(ServiceLoader.java:185)
		at java.util.ServiceLoader$LazyIterator.nextService(ServiceLoader.java:376)
		at java.util.ServiceLoader$LazyIterator.next(ServiceLoader.java:404)
		at java.util.ServiceLoader$1.next(ServiceLoader.java:480)
		at io.github.nicholaszhou.desensitization.DesensitizationLogMessageConverter.initCustomConfig(DesensitizationLogMessageConverter.java:96)
		at io.github.nicholaszhou.desensitization.DesensitizationLogMessageConverter.<clinit>(DesensitizationLogMessageConverter.java:31)
		at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
		at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
		at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
		at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
		at java.lang.Class.newInstance(Class.java:442)
		at ch.qos.logback.core.util.OptionHelper.instantiateByClassNameAndParameter(OptionHelper.java:60)
		... 61 more
	Suppressed: ch.qos.logback.core.util.DynamicClassLoadingException: Failed to instantiate type io.github.nicholaszhou.desensitization.DesensitizationLogMessageConverter
		at ch.qos.logback.core.util.OptionHelper.instantiateByClassNameAndParameter(OptionHelper.java:68)
		at ch.qos.logback.core.util.OptionHelper.instantiateByClassName(OptionHelper.java:44)
		at ch.qos.logback.core.util.OptionHelper.instantiateByClassName(OptionHelper.java:33)
		at ch.qos.logback.core.pattern.parser.Compiler.createConverter(Compiler.java:104)
		at ch.qos.logback.core.pattern.parser.Compiler.compile(Compiler.java:63)
		at ch.qos.logback.core.pattern.parser.Parser.compile(Parser.java:87)
		at ch.qos.logback.core.pattern.PatternLayoutBase.start(PatternLayoutBase.java:84)
		at net.logstash.logback.pattern.AbstractJsonPatternParser.buildLayout(AbstractJsonPatternParser.java:366)
		at net.logstash.logback.pattern.AbstractJsonPatternParser.makeLayoutValueGetter(AbstractJsonPatternParser.java:396)
		at net.logstash.logback.pattern.AbstractJsonPatternParser.makeComputableValueGetter(AbstractJsonPatternParser.java:392)
		at net.logstash.logback.pattern.AbstractJsonPatternParser.parseChildren(AbstractJsonPatternParser.java:437)
		at net.logstash.logback.pattern.AbstractJsonPatternParser.parse(AbstractJsonPatternParser.java:471)
		at net.logstash.logback.composite.AbstractPatternJsonProvider.parse(AbstractPatternJsonProvider.java:79)
		at net.logstash.logback.composite.AbstractPatternJsonProvider.setJsonFactory(AbstractPatternJsonProvider.java:67)
		at net.logstash.logback.composite.JsonProviders.setJsonFactory(JsonProviders.java:91)
		at net.logstash.logback.composite.CompositeJsonFormatter.start(CompositeJsonFormatter.java:105)
		at net.logstash.logback.encoder.CompositeJsonEncoder.start(CompositeJsonEncoder.java:211)
		at ch.qos.logback.core.joran.action.NestedComplexPropertyIA.end(NestedComplexPropertyIA.java:161)
		at ch.qos.logback.core.joran.spi.Interpreter.callEndAction(Interpreter.java:309)
		at ch.qos.logback.core.joran.spi.Interpreter.endElement(Interpreter.java:193)
		at ch.qos.logback.core.joran.spi.Interpreter.endElement(Interpreter.java:179)
		at ch.qos.logback.core.joran.spi.EventPlayer.play(EventPlayer.java:62)
		at ch.qos.logback.core.joran.GenericConfigurator.doConfigure(GenericConfigurator.java:165)
		at ch.qos.logback.core.joran.GenericConfigurator.doConfigure(GenericConfigurator.java:152)
		at ch.qos.logback.core.joran.GenericConfigurator.doConfigure(GenericConfigurator.java:110)
		at ch.qos.logback.core.joran.GenericConfigurator.doConfigure(GenericConfigurator.java:53)
		at org.springframework.boot.logging.logback.LogbackLoggingSystem.configureByResourceUrl(LogbackLoggingSystem.java:199)
		at org.springframework.boot.logging.logback.LogbackLoggingSystem.loadConfiguration(LogbackLoggingSystem.java:165)
		... 43 more
```
解决方法: 去掉删除maven中spring-boot-devtools的依赖  
参考:https://blog.csdn.net/zhyhang/article/details/114239800

### 参考资料
https://github.com/weweibuy/weweibuy-framework