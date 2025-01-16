package io.github.nicholaszhou.desensitization;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import io.github.nicholaszhou.log.MvcPathMappingOperator;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 脱敏日志转化器
 */
public class DesensitizationLogMessageConverter extends ClassicConverter {


    private static final Integer MAX_MESSAGE_LENGTH = 10000;

    private final Map<String, PatternReplace> filedPatternMap = new HashMap<>();
    private final ServiceLoader<PatternReplaceConfig> patternReplaceConfigLoaders = ServiceLoader.load(PatternReplaceConfig.class);


    {
        initDefaultPattern();
        initCustomConfig();
    }

    @Override
    public String convert(ILoggingEvent event) {
        return MvcPathMappingOperator.getSensitizationField().map(s -> doDesensitization(event.getFormattedMessage(), s)).orElse(event.getFormattedMessage());
    }

    private String doDesensitization(String message, Set<String> sensitizationFieldSet) {
        if (message.length() > MAX_MESSAGE_LENGTH) {
            message = message.substring(0, MAX_MESSAGE_LENGTH);
        }
        for (String filed : sensitizationFieldSet) {
            PatternReplace patternReplace = filedPatternMap.get(filed);
            if (patternReplace == null) {
                continue;
            }
            message = patternReplace.replace(message);
        }
        return message;
    }

    private void initDefaultPattern() {
        /*
         * 正则中 () 中匹配为一组
         * $1 代表从前到后中第一个匹配的组 , $2 为第二个; 不在()内的不计入组
         */
        Pattern phonePattern = Pattern.compile("(mobile|phone|phoneNo)(\"\\s*:\\s*\"|=|\\s*:)(\\d{3})\\d{4}(\\d{4})");
        PatternReplace phonePatternReplace = new PatternReplace(phonePattern, "$1$2$3****$4");
        filedPatternMap.put("mobile", phonePatternReplace);
        filedPatternMap.put("phone", phonePatternReplace);
        filedPatternMap.put("phoneNo", phonePatternReplace);

//        Pattern certIdPattern = Pattern.compile("(certId|idCard|idNo)(\"\\s*:\\s*\"|=)(\\d{6})\\d{8,11}(\\w{1})");
//        PatternReplace certIdPatternReplace = new PatternReplace(certIdPattern, "$1$2$3**************$4");
//        filedPatternMap.put("certId", certIdPatternReplace);
//        filedPatternMap.put("idCard", certIdPatternReplace);
//        filedPatternMap.put("idNo", certIdPatternReplace);


        Pattern pwd = Pattern.compile("(password|pwd|appSecret)(\"\\s*:\\s*\"|=).*?(?=(\"|,|&))");
        PatternReplace pwdPatternReplace = new PatternReplace(pwd, "$1$2******");

        filedPatternMap.put("pwd", pwdPatternReplace);
        filedPatternMap.put("password", pwdPatternReplace);
        filedPatternMap.put("appSecret", pwdPatternReplace);

        Pattern namePattern = Pattern.compile("(fullName)(\"\\s*:\\s*\"|=)([\\u4e00-\\u9fa5]{1})([\\u4e00-\\u9fa5]*)");
        PatternReplace namePatternReplace = new PatternReplace(namePattern, "$1$2$3**");
        filedPatternMap.put("fullName", namePatternReplace);

        Pattern addressPattern = Pattern.compile("(address)(\"\\s*:\\s*\"|=)([\\u4e00-\\u9fa5]{4}).*?(?=(\"|,|&))");
        PatternReplace addressPatternReplace = new PatternReplace(addressPattern, "$1$2$3***");
        filedPatternMap.put("address", addressPatternReplace);
    }

    // 参考类加载器https://blog.csdn.net/briblue/article/details/54973413，和spi动态加载机制https://qidawu.github.io/posts/java-spi/
    private void initCustomConfig() {
        for (PatternReplaceConfig config : patternReplaceConfigLoaders) {
            filedPatternMap.putAll(config.addPatternReplace());
        }
    }

    /**
     * 日志转换格式
     */
    @Data
    @AllArgsConstructor
    public static class PatternReplace {

        private Pattern pattern;

        private String replace;

        private String replace(String msg) {
            Matcher matcher = pattern.matcher(msg);
            return matcher.replaceAll(replace);
        }
    }


}
