package com.cxy.baseBoot.ws.server.exception;

public class ServiceFaultException extends RuntimeException {

	private static final long serialVersionUID = 5394518972951694190L;
	
	private ServiceFault serviceFault; // 错误实体Bean

    /**
     * 自定义错误信息
     *
     * @param message      对应soap-Fault-faultstring的值
     * @param serviceFault 对应soap-Fault-detail的值
     */
    public ServiceFaultException(String message, ServiceFault serviceFault) {
        super(message);
        this.serviceFault = serviceFault;
    }

    /**
     * 自定义错误信息
     *
     * @param message      对应soap-Fault-faultstring的值
     * @param e            异常错误
     * @param serviceFault 对应soap-Fault-detail的值
     */
    public ServiceFaultException(String message, Throwable e, ServiceFault serviceFault) {
        super(message, e);
        this.serviceFault = serviceFault;
    }

    public ServiceFault getServiceFault() {
        return serviceFault;
    }

    public void setServiceFault(ServiceFault serviceFault) {
        this.serviceFault = serviceFault;
    }
}
