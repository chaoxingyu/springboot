package com.cxy.baseBoot.ws.client;

import com.cxy.baseBoot.ws.server.model.GetCountryRequest;
import com.cxy.baseBoot.ws.server.model.GetCountryResponse;
import com.cxy.baseBoot.ws.server.model.GetEmployeeRequest;
import com.cxy.baseBoot.ws.server.model.GetEmployeeResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class WsClient extends WebServiceGatewaySupport {

    public GetCountryResponse getCountry(String name) {
        GetCountryRequest request = new GetCountryRequest();
        request.setName(name);
        GetCountryResponse response = (GetCountryResponse) getWebServiceTemplate().marshalSendAndReceive(
                "http://localhost:8080/bootApi/ws/countries.wsdl", request);
        return response;
    }

    public GetEmployeeResponse getEmployee(String name) {
        GetEmployeeRequest request = new GetEmployeeRequest();
        request.setName(name);
        GetEmployeeResponse  response = (GetEmployeeResponse) getWebServiceTemplate().marshalSendAndReceive(
                "http://localhost:8080/bootApi/ws/employee.wsdl", request);
        return response;
    }
}