package com.nicholaszhou.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import org.slf4j.TtlMDCAdapter;

/**
 * 在logback.xml中配置监听，替换默认mdcAdapter
 */
public class TtlMdcListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {

    /**
     * 存储日志路径标识
     */
    public static final String LOG_PAHT_KEY = "LOG_PATH";

    @Override
    public void start() {
        // 替换MDC实现
        if (userTtl()) {
            this.addInfo("load TtlMDCAdapter...");
            TtlMDCAdapter adapter = TtlMDCAdapter.getInstance();
        }
        // 初始化雪花算法
        // 创建 IdGeneratorOptions 对象，可在构造函数中输入 WorkerId：
        IdGeneratorOptions options = new IdGeneratorOptions();
        // 保存参数（务必调用，否则参数设置不生效）：
        YitIdHelper.setIdGenerator(options);
    }

    private boolean userTtl() {
        try {
            Class.forName("com.alibaba.ttl.TransmittableThreadLocal");
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public boolean isResetResistant() {
        return false;
    }

    @Override
    public void onStart(LoggerContext loggerContext) {
        // todo 动态加载日志路径,参考https://www.cnblogs.com/donfaquir/p/12198570.html
        String logPath = "/opt/logs/";
        System.setProperty(LOG_PAHT_KEY, logPath);
        Context context = getContext();
        context.putProperty(LOG_PAHT_KEY, logPath);
    }

    @Override
    public void onReset(LoggerContext loggerContext) {
    }

    @Override
    public void onStop(LoggerContext loggerContext) {
    }

    @Override
    public void onLevelChange(Logger logger, Level level) {
    }
}
