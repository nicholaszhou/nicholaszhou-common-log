package com.test.demo.support;

import io.github.nicholaszhou.desensitization.DesensitizationLogMessageConverter;
import io.github.nicholaszhou.desensitization.PatternReplaceConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class CustomDesensitizationExtendConfigurer implements PatternReplaceConfig {
    private static final Map<String, DesensitizationLogMessageConverter.PatternReplace> FILED_PATTERN_MAP = new HashMap<>();
    @Override
    public void addPatternReplace(Map<String, DesensitizationLogMessageConverter.PatternReplace> patternReplaceMap) {
        PatternReplaceConfig.super.addPatternReplace(patternReplaceMap);
        Pattern certIdPattern = Pattern.compile("(certId|idCard|idNo)(\"\\s*:\\s*\"|=)(\\d{6})\\d{8,11}(\\w{1})");
        DesensitizationLogMessageConverter.PatternReplace certIdPatternReplace = new DesensitizationLogMessageConverter.PatternReplace(certIdPattern, "$1$2$3**************$4");
        FILED_PATTERN_MAP.put("certId", certIdPatternReplace);
        FILED_PATTERN_MAP.put("idCard", certIdPatternReplace);
        FILED_PATTERN_MAP.put("idNo", certIdPatternReplace);
    }
}
