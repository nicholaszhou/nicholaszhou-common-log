package com.test.demo;

import com.test.demo.support.BaseResultVO;
import io.github.nicholaszhou.config.LogAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
        String test = null;
        if (test.equals(mobile)) {
            baseResultVO.setData("sdsf");
        }
        log.info("mobile:{},deviceType:{}", mobile, deviceType);
        baseResultVO.setData(testService.test(mobile, deviceType));
        return baseResultVO;
    }

    @PostMapping(value = "/doTestPost", produces = "application/json;charset=UTF-8")
    public BaseResultVO<String> test(@RequestBody TestEntity testEntity) {
        BaseResultVO<String> baseResultVO = new BaseResultVO<>();
        baseResultVO.setCode("000000");
        baseResultVO.setMsg("success");
//        for (int i = 0; i < 10000000; i++){
//        log.info("mobile:{},deviceType:{}", testEntity.getMobile(), testEntity.getDeviceType());
//        baseResultVO.setData(testService.test(testEntity.getMobile(), testEntity.getDeviceType()));
//        }
        return baseResultVO;
    }
}
