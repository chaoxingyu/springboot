package com.cxy.baseboot2.parser.html;

import java.io.Serializable;
import java.util.List;

public class RegionEntry implements Serializable {

	private static final long serialVersionUID = -2427518603248818161L;

	// 编码规则 参考：ReadMine/中国地区代码/统计用区划代码和城乡划分代码编制规则.pdf
	private String code; // 编码
	private String name; // 名称
	private String href; // 链接地址
	private String type; // 类型 111：主城区 112：城乡结合区 121：镇中心区 122：镇乡结合区 123：特殊区域 210：乡中心区 220：村庄
	private Integer level; // 等级
	private List<RegionEntry> subRegion; // 子项目

	public RegionEntry() {
		super();
	}

	public RegionEntry(String code, String name, String href, String type, Integer level) {
		super();
		this.code = code;
		this.name = name;
		this.href = href;
		this.type = type;
		this.level = level;
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

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<RegionEntry> getSubRegion() {
		return subRegion;
	}

	public void setSubRegion(List<RegionEntry> subRegion) {
		this.subRegion = subRegion;
	}

}
