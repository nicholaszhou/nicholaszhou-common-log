package com.test.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestService {
    public String test(String mobile, String deviceType) {
        log.info("mobile={},deviceType={}", mobile, deviceType);
        return "deviceType";
    }
}
