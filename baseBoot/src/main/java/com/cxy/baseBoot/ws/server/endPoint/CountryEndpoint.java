package com.cxy.baseBoot.ws.server.endPoint;

import com.cxy.baseBoot.ws.server.model.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CountryEndpoint {

    /*
    @Endpoint：是指消息的接收者，就是SpringWS的endpoint，可以一个或者多个，关键是下面的注解
    @PayloadRoot：是指soap消息中的需要被调用的方法，它包括两个参数：localPart的值实质上就是请求报文中的一个节点(AddRequest)，而namespace就是指我定义在Schema的命名空间。
    @RequestPayload:是指请求的实例。当客户端发送消息过来，服务端接收到的实际是XML报文，在调用此方法之前，SpringWS已经帮我们把XML序列化成实例了（我们在之前已经生成对应的AddRequest和AddResponse等class，并指明命名空间)。
    @ResponsePayload:是指最终返回的实例，而客户端最终也是以XML返回给客户端。
     */
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


   @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getEmployeeRequest")
   @ResponsePayload
   public GetEmployeeResponse getEmployee(@RequestPayload GetEmployeeRequest getEmployeeRequest){
       Employee employee = new Employee();
       employee.setId(1111);
       employee.setCity("BJ");
       employee.setSex("0");
       employee.setName("test");
       GetEmployeeResponse response = new GetEmployeeResponse();
       response.setEmployee(employee);
       return  response;
   }

}
