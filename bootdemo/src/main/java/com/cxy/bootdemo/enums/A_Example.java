package com.cxy.bootdemo.enums;

public enum A_Example {

	a("a", "name", "value", "desc"), 
	b("b", "name1", "value1", "desc1"), 
	c("c", "name2", "value2", "desc2");

	// 成员变量
	private String code; // 编码
	private String name; // 名称
	private String value; // 值
	private String desc; // 描述

	private A_Example(String code, String name, String value, String desc) {
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
		return A_Example.class.getSimpleName() + " [code=" + code + ", name=" + name + ", value=" + value + ", desc=" + desc + "]";
	}

	/**
	 * 根据 code 查询 name
	 * 
	 * @param code
	 * @return
	 */
	public static String getNameByCode(String code) {
		for (A_Example c : A_Example.values()) {
			if (c.getCode() == code) {
				return c.name;
			}
		}
		return null;
	}

	/**
	 * 通过 value 获取 name
	 * 
	 * @param value
	 * @return
	 */
	public static String getNameByValue(String value) {
		for (A_Example c : A_Example.values()) {
			if (c.getValue() == value) {
				return c.name;
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
	public static A_Example getEnumTypeByCode(String code) {
		for (A_Example c : A_Example.values()) {
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
	public static A_Example getEnumTypeByName(String name) {
		for (A_Example c : A_Example.values()) {
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
	public static A_Example getEnumTypeByValue(String value) {
		for (A_Example c : A_Example.values()) {
			if (c.getValue() == value) {
				return c;
			}
		}
		return null;
	}

}
