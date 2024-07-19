package com.test.demo;

import com.test.demo.support.BaseResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    private final TestService testService;

    TestController(TestService testService) {
        this.testService = testService;
    }


    @GetMapping("/test")
    public BaseResultVO<String> test(@RequestHeader(value = "mobile", required = false) String mobile, @RequestParam(defaultValue = "Android") String deviceType) {
        BaseResultVO<String> baseResultVO = new BaseResultVO<>();
        baseResultVO.setCode("000000");
        baseResultVO.setMsg("success");
        log.info("mobile:{},deviceType:{}", mobile, deviceType);
        baseResultVO.setData(testService.test(mobile, deviceType));
        return baseResultVO;
    }
}
