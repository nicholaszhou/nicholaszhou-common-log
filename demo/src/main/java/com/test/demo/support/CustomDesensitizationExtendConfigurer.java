package com.test.demo.support;

import io.github.nicholaszhou.desensitization.DesensitizationLogMessageConverter;
import io.github.nicholaszhou.desensitization.PatternReplaceConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class CustomDesensitizationExtendConfigurer implements PatternReplaceConfig {
    private static final Map<String, DesensitizationLogMessageConverter.PatternReplace> desensitizationLogFiledMap = new HashMap<>();

    @Override
    public Map<String, DesensitizationLogMessageConverter.PatternReplace> addPatternReplace() {
        Pattern certIdPattern = Pattern.compile("(certId|idCard|idNo)(\"\\s*:\\s*\"|=)(\\d{6})\\d{8,11}(\\w{1})");
        DesensitizationLogMessageConverter.PatternReplace certIdPatternReplace = new DesensitizationLogMessageConverter.PatternReplace(certIdPattern, "$1$2$3**************$4");
        desensitizationLogFiledMap.put("certId", certIdPatternReplace);
        desensitizationLogFiledMap.put("idCard", certIdPatternReplace);
        desensitizationLogFiledMap.put("idNo", certIdPatternReplace);
        return desensitizationLogFiledMap;
    }
}
