package com.cxy.baseBoot.ws.endPoint;

import com.cxy.baseBoot.ws.server.Country;
import com.cxy.baseBoot.ws.server.Currency;
import com.cxy.baseBoot.ws.server.GetCountryRequest;
import com.cxy.baseBoot.ws.server.GetCountryResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CountryEndpoint {

    private static final String NAMESPACE_URI = "http://www.baseBoot.cxy.com/ws/server";

    // 注意PayloadRoot注解当中的namespace和localPart需要和xsd中对应。
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
    @ResponsePayload
    public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request) {
        GetCountryResponse response = new GetCountryResponse();
        Country poland = new Country();
        poland.setName("Poland-" + request.getName());
        poland.setCapital("Warsaw");
        poland.setCurrency(Currency.PLN);
        poland.setPopulation(38186860);
        response.setCountry(poland);
        return response;
    }
}
