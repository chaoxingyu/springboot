package com.cxy.baseboot2.parser.html;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @package com.cxy.baseboot2.parser.html
 * @type ChinaRegionByJsoupParser
 * @description 使用 Jsoup 解析 HTML 获取 中国国家统计局行政编码（不含港澳台及国外）
 * @author cxy
 * @date 2018年4月4日 下午4:27:57
 * @version 1.0.0
 * 
 */
public class ChinaRegionByJsoupParser {

	/**
	 * Jsoup 官网： https://jsoup.org/
	 * 
	 */
	private static final String SITE_URL = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/index.html"; // 中国国家统计局行政编码（不含港澳台及国外）
	private static final String SITE_CHARSETCODE = "GBK"; // 中国国家统计局行政编码 页面编码为gb2312 使用GBK解析页面以免乱码

	private static void writeFile(String str, String filePath) {
		System.err.println("写文件开始时间："+LocalDateTime.now());
		if( null ==	filePath || filePath.equals("")) {
			filePath = "/Users/chaoxingyu/GitLocal/fork/springboot/baseboot2/db/mysql/initsql/datainit/chinaRegion/tt.sql";
		}
		try {
			Path path = Paths.get(filePath); 
			Files.write(path, str.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}  
		System.err.println("写文件结束时间："+LocalDateTime.now());
	}
	
	public static void main(String[] args) {
		System.out.println("抓取开始:" + LocalDateTime.now());
		getProvince();
		System.out.println("抓取完毕:" + LocalDateTime.now());
		/* 
		String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/13/01/21/130121109.html";
		getDocument(url);*/
	}
	
	private static void deal2Region(RegionEntry province) {
		StringBuffer sqlbuffer = new StringBuffer();
		sqlbuffer.append("INSERT INTO `sys_region` (`id`, `code`, `name`, `type`, `level`) VALUES (");
		sqlbuffer.append("'" + province.getCode() + "',");
		sqlbuffer.append("'" + province.getCode() + "',");
		sqlbuffer.append("'" + province.getName() + "',");
		sqlbuffer.append("'" + province.getType() + "',");
		sqlbuffer.append(province.getLevel());
		sqlbuffer.append("); \r\n");
		List<RegionEntry> cityList = province.getSubRegion();
		if (null != cityList && !cityList.isEmpty()) {
			cityList.forEach(city -> {
				sqlbuffer.append("INSERT INTO `sys_region` (`id`, `code`, `name`, `type`, `level`, `parent_id`) VALUES (");
				sqlbuffer.append("'" + city.getCode() + "',");
				sqlbuffer.append("'" + city.getCode() + "',");
				sqlbuffer.append("'" + city.getName() + "',");
				sqlbuffer.append("'" + city.getType() + "',");
				sqlbuffer.append(city.getLevel() + ",");
				sqlbuffer.append("'" + province.getCode() + "'");
				sqlbuffer.append("); \r\n");
				List<RegionEntry> countyList = city.getSubRegion();
				if (null != countyList && !countyList.isEmpty()) {
					countyList.forEach(county -> {
						sqlbuffer.append("INSERT INTO `sys_region` (`id`, `code`, `name`, `type`, `level`, `parent_id`) VALUES (");
						sqlbuffer.append("'" + county.getCode() + "',");
						sqlbuffer.append("'" + county.getCode() + "',");
						sqlbuffer.append("'" + county.getName() + "',");
						sqlbuffer.append("'" + county.getType() + "',");
						sqlbuffer.append(county.getLevel() + ",");
						sqlbuffer.append("'" + city.getCode() + "'");
						sqlbuffer.append("); \r\n");
						List<RegionEntry> townList = county.getSubRegion();
						if (null != townList && !townList.isEmpty()) {
							townList.forEach(town -> {
								sqlbuffer.append(
												"INSERT INTO `sys_region` (`id`, `code`, `name`, `type`, `level`, `parent_id`) VALUES (");
								sqlbuffer.append("'" + town.getCode() + "',");
								sqlbuffer.append("'" + town.getCode() + "',");
								sqlbuffer.append("'" + town.getName() + "',");
								sqlbuffer.append("'" + town.getType() + "',");
								sqlbuffer.append(town.getLevel() + ",");
								sqlbuffer.append("'" + county.getCode() + "'");
								sqlbuffer.append("); \r\n");
								List<RegionEntry> villageList = town.getSubRegion();
								if (null != villageList && !villageList.isEmpty()) {
									villageList.forEach(village -> {
										sqlbuffer.append(
														"INSERT INTO `sys_region` (`id`, `code`, `name`, `type`, `level`, `parent_id`) VALUES (");
										sqlbuffer.append("'" + village.getCode() + "',");
										sqlbuffer.append("'" + village.getCode() + "',");
										sqlbuffer.append("'" + village.getName() + "',");
										sqlbuffer.append("'" + village.getType() + "',");
										sqlbuffer.append(village.getLevel() + ",");
										sqlbuffer.append("'" + town.getCode() + "'");
										sqlbuffer.append("); \r\n");

									});
								}
							});
						}
					});
				}
			});
		}
		String basePath = "/Users/chaoxingyu/GitLocal/fork/springboot/baseboot2/db/mysql/initsql/datainit/chinaRegion/"+province.getCode()+ ".sql";
		writeFile(sqlbuffer.toString(), basePath);
	}
	
	private static Document getDocument(String url) {
		System.out.println("获取 Document 开始:" + LocalDateTime.now());
		Document doc =  null;
		int i = 0;
		while(null == doc) {
			try {
				doc = Jsoup.parse(new URL(url).openStream(), SITE_CHARSETCODE, url);
			} catch (IOException e) {
				i++;
				System.err.println("获取 Document 错误次数:"+i);
				e.printStackTrace();
			}
		}
		System.out.println("获取 Document 结束:" + LocalDateTime.now());
		return doc;
	}

	private static void getProvince() {
		Document doc = getDocument(SITE_URL);
		Elements links = doc.select("tr.provincetr").select("a"); // 省 页面参数
		for (Element e : links) {
			System.out.println("解析【"+e.text()+"】开始:" + LocalDateTime.now());
			String href = e.attr("href");
			String[] arr = href.split("\\.");
			String code = arr[0];
			String absHref = e.attr("abs:href"); // 使用 abs: 属性前缀来取得包含base URI的绝对路径
			RegionEntry region = new RegionEntry(code, e.text(), absHref, ChinaRegion.PROVINCE.getCode(),
							ChinaRegion.PROVINCE.getLevel());
			getCity(absHref, region);
			deal2Region(region);
			System.out.println("解析【"+e.text()+"】结束:" + LocalDateTime.now());
			//regions.add(region);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
		}
	}

	/**
	 * 获取市地址
	 * 
	 * @param url
	 * @param region
	 */
	private static void getCity(String url, RegionEntry region) {
		Document doc = getDocument(url);
			Elements links = doc.select("tr.citytr"); // 市 页面参数
			List<RegionEntry> citySubRegion = new ArrayList<>();
			for (Element e : links) {
				Elements alist = e.select("a");
				Element codeE = alist.get(0);
				Element codeN = alist.get(1);
				String code = codeE.text();
				code = code.substring(0, 4);// 1～2 为 省级代码 3～4 为 市级代码
				String name = codeN.text();
				if ("市辖区".equals(name)) {
					name = region.getName();
				}
				String absHref = codeE.attr("abs:href");
				RegionEntry city = new RegionEntry(code, name, absHref, ChinaRegion.CITY.getCode(), ChinaRegion.CITY.getLevel());
				getArea(absHref, city);
				citySubRegion.add(city);
			}
			if (!citySubRegion.isEmpty()) {
				region.setSubRegion(citySubRegion);
			}
	}

	/**
	 * 获取区县地址
	 * 
	 * @param url
	 * @param region
	 */
	private static void getArea(String url, RegionEntry region) {
		Document doc = getDocument(url);
			Elements links = doc.select("tr.countytr"); // 区县 页面参数
			List<RegionEntry> countySubRegion = new ArrayList<>();
			for (Element e : links) {
				RegionEntry county = new RegionEntry("", "", "", ChinaRegion.COUNTY.getCode(), ChinaRegion.COUNTY.getLevel());
				Elements alist = e.select("a");
				if (null != alist && alist.size() > 0) {
					Element codeE = alist.get(0);
					String code = codeE.text();
					code = code.substring(0, 6);// 1～2 为 省级代码 3～4 为 市级代码 5~6 为 县级代码
					county.setCode(code);
					Element codeN = alist.get(1);
					String name = codeN.text();
					county.setName(name);
					String absHref = codeE.attr("abs:href");
					county.setHref(absHref);
					getTown(absHref, county);
					countySubRegion.add(county);
				} else {
					alist = e.select("td");
					county.setCode(alist.get(0).text());
					county.setName(alist.get(1).text());
					countySubRegion.add(county);
				}
			}
			if (!countySubRegion.isEmpty()) {
				region.setSubRegion(countySubRegion);
			}
	}

	/**
	 * 获取 乡级
	 * 
	 * @param url
	 * @param region
	 */
	private static void getTown(String url, RegionEntry region) {
		Document doc = getDocument(url);
			Elements links = doc.select("tr.towntr"); // 乡级 页面参数
			List<RegionEntry> townSubRegion = new ArrayList<>();
			for (Element e : links) {
				RegionEntry town = new RegionEntry("", "", "", ChinaRegion.TOWN.getCode(), ChinaRegion.TOWN.getLevel());
				Elements alist = e.select("a");
				if (null != alist && alist.size() > 0) {
					Element codeE = alist.get(0);
					String code = codeE.text();
					code = code.substring(0, 9); // 1～2 为 省级代码 3～4 为 市级代码 7~9 为 乡级代码
					town.setCode(code);
					Element codeN = alist.get(1);
					String name = codeN.text();
					town.setName(name);
					String absHref = codeE.attr("abs:href");
					town.setHref(absHref);
					getVillage(absHref, town);
					townSubRegion.add(town);
				} else {
					alist = e.select("td");
					town.setCode(alist.get(0).text());
					town.setName(alist.get(1).text());
					townSubRegion.add(town);
				}
			}
			if (!townSubRegion.isEmpty()) {
				region.setSubRegion(townSubRegion);
			}
	}

	/**
	 * 获取 村级
	 * 
	 * @param url
	 * @param region
	 */
	private static void getVillage(String url, RegionEntry region) {
		Document doc = getDocument(url);
			Elements links = doc.select("tr.villagetr"); // 村级 页面参数
			List<RegionEntry> villageSubRegion = new ArrayList<>();
			for (Element e : links) {
				Elements alist = e.select("td");
				String code = alist.get(0).text(); // 1～2 为 省级代码 3～4 为 市级代码 7~9 为 乡级代码 10~12 为 村级代码
				RegionEntry village = new RegionEntry(code, alist.get(2).text(), null, alist.get(1).text(), ChinaRegion.VILLAGE.getLevel());
				villageSubRegion.add(village);
			}
			if (!villageSubRegion.isEmpty()) {
				region.setSubRegion(villageSubRegion);
			}
	}

}
