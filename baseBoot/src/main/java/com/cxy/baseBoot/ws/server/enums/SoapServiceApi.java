package com.cxy.baseBoot.ws.server.enums;

import com.cxy.baseBoot.common.enumDesc.EnumDescription;

@EnumDescription("soap服务API")
public enum SoapServiceApi {

    WS1001001("WS1001001", "http://www.baseBoot.cxy.com/ws/server", "1.1", "测试SOAP"),
    b("b", "name1", "value1", "desc1"),
    c("c", "name2", "value2", "desc2");


    private String code;    // 编码   - soap 请求方法
    private String name;    // 名称   - soap namespace
    private String value;   // 数值   - soap 类型1.1或1.2
    private String desc;    // 描述   - soap 请求 描述

    private SoapServiceApi(String code, String name, String value, String desc) {
        this.code = code;
        this.name = name;
        this.value = value;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return SoapServiceApi.class.getSimpleName() + " [code=" + code + ", name=" + name + ", value=" + value + ", desc=" + desc + "]";
    }

    /**
     * 根据 code 查询 desc
     *
     * @param code
     * @return
     */
    public static String getNameByCode(String code) {
        for (SoapServiceApi c : SoapServiceApi.values()) {
            if (c.getCode() == code) {
                return c.desc;
            }
        }
        return null;
    }

    /**
     * 通过code获取枚举类型
     *
     * @param code
     * @return
     */
    public static SoapServiceApi getEnumTypeByCode(String code) {
        for (SoapServiceApi c : SoapServiceApi.values()) {
            if (c.getCode() == code) {
                return c;
            }
        }
        return null;
    }

    /**
     * 通过name获取枚举类型
     *
     * @param name
     * @return
     */
    public static SoapServiceApi getEnumTypeByName(String name) {
        for (SoapServiceApi c : SoapServiceApi.values()) {
            if (c.getName() == name) {
                return c;
            }
        }
        return null;
    }

    /**
     * 通过value获取枚举类型
     *
     * @param value
     * @return
     */
    public static SoapServiceApi getEnumTypeByValue(String value) {
        for (SoapServiceApi c : SoapServiceApi.values()) {
            if (c.getValue() == value) {
                return c;
            }
        }
        return null;
    }

}
