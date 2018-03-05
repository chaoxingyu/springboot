package com.cxy.baseBoot.ws.server.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

@Component
public class GlobalEndpointInterceptor implements EndpointInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalEndpointInterceptor.class);

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
        LOGGER.info("Handle BeerEndpoint Request");
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
        LOGGER.info("Handle BeerEndpoint Response");
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
        LOGGER.info("Handle BeerEndpoint Exceptions");
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
        LOGGER.info("Handle BeerEndpoint After Completion");
    }
}
