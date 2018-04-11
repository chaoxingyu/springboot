package com.cxy.baseboot2.parser.html;

import com.cxy.baseboot2.base.enums.interfaces.Description;

@Description("中国地区代码地区类型")
public enum ChinaRegion {

	PROVINCE("PROVINCE", 1, "省级"), 
	CITY("CITY", 2, "地级"), 
	COUNTY("COUNTY", 3, "县级"), 
	TOWN("TOWN", 4, "乡级"), 
	VILLAGE("VILLAGE", 5, "村级");

	private String code; // 编码
	private int level; // 等级
	private String desc; // 描述

	private ChinaRegion(String code, int level, String desc) {
		this.code = code;
		this.level = level;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
