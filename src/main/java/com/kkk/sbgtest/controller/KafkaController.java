package com.kkk.sbgtest.controller;

import com.kkk.sbgtest.model.ApiResult;
import com.kkk.sbgtest.model.SearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("collector.AlarmController")
@RequestMapping(value="/collect/v1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Collector API 컨트롤러" )
public class KafkaController {

    @RequestMapping(value="/user/alarmInfo")
    @ResponseBody
    @ApiOperation(value = " 알람 정보 수집 API ", notes = " 알람 정보를 TOPIC-ALARM-INFO 토픽에 저장합니다. ")
    public ApiResult alarm(@RequestBody @Valid SearchVO searchVo) throws Exception {

        return ApiResult.setSuccess();
    }
}
