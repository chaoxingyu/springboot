package com.cxy.baseBoot.ws.client;

import com.cxy.baseBoot.ws.server.model.GetCountryResponse;
import com.cxy.baseBoot.ws.server.model.GetEmployeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    /**/
    @Autowired
    private WsClient wsClient;

    @RequestMapping("callws")
    public Object callWs() {
        GetCountryResponse response = wsClient.getCountry("hello");
        return response.getCountry();
    }

    @RequestMapping("callws2")
    public Object callWs2() {
        GetEmployeeResponse response = wsClient.getEmployee("hello");
        return response.getEmployee();
    }

}
