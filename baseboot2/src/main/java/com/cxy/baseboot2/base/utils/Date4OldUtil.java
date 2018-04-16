package com.cxy.baseboot2.base.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @package com.cxy.baseboot2.base.utils
 * @type Date4OldUtil
 * @description Java1.7 以前的日期处理类
 * @author cxy
 * @date 2018年4月16日 下午2:02:41
 * @version 1.0.0
 */
public class Date4OldUtil {

	private static final String DATE_FORMAT = "yyyy-MM-dd"; // 日期格式：yyyy-MM-dd
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"; // 24小时制日期格式：yyyy-MM-dd HH:mm:ss

	/**
	 * 获取当前时间(日期格式：yyyy-MM-dd HH:mm:ss)
	 * 
	 * @return Date
	 */
	public static Date getNowDate() {
		return new Date();
	}

	/**
	 * 获取当前时间(默认日期格式：yyyy-MM-dd HH:mm:ss)
	 * 
	 * @return String
	 */
	public static String getNowStrDate(String format) {
		if (null == format || "".equals(format)) {
			format = DATE_TIME_FORMAT;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(new Date());
	}

	/**
	 * 判断是否润年
	 * 
	 * @param dateStr
	 *            字符串日期
	 * @return boolean
	 */
	public static boolean isLeapYear(String dateStr) {
		Date date = str2Date(dateStr, null);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		if (0 == year % 400 || (0 == year % 4 && 0 != year % 100)) {
			// 判断闰年的方法：闰年满足两个条件（满足一个即为闰年） 1、能被4整除但不能被100整除 2、能被400整除
			return true;
		}
		return false;
	}

	/**
	 * 提取一个月中的最后一天
	 * 
	 * @param day
	 * @return
	 */
	public static Date getLastDate(long day) {
		Date date = new Date();
		long date_3_hm = date.getTime() - 3600000 * 34 * day;
		Date date_3_hm_date = new Date(date_3_hm);
		return date_3_hm_date;
	}

	/**
	 * 英文月(三位) 转换 数字月(两位)
	 * 
	 * @param monthEng
	 *            英文月(三位)
	 * @return String
	 */
	public static String getMonthNumByMonthEng(String month) {
		if (null == month || 3 != month.length()) {
			return null;
		}
		// Java1.7之前大家都清楚switch的比较范围只能局限于（int 、short 、byte 、char）之间，
		// Java1.7后实现字符串比较的功能的实现机制是：将字符串之间的比较转换为其哈希值的比较。
		switch (month) {
		case "Jan":
			month = "01";
		case "Feb":
			month = "02";
		case "Mar":
			month = "03";
		case "Apr":
			month = "04";
		case "May":
			month = "05";
		case "Jun":
			month = "06";
		case "Jul":
			month = "07";
		case "Aug":
			month = "08";
		case "Sep":
			month = "09";
		case "Oct":
			month = "10";
		case "Nov":
			month = "11";
		case "Dec":
			month = "12";
		default:
			month = "";
			break;
		}
		return month;
	}


	public static Date converString2Date(String inValue) throws Exception {
		if (null == inValue || "".equals(inValue)) {
			return null;
		}
		String value = inValue.replaceAll("：", ":");
		value = value.trim();
		if (value.contains("年") && value.contains("月") && value.contains("日")) {
			if (value.contains("时") && value.contains("分") && value.contains("秒")) {
				if (value.contains("毫秒")) {
					return str2Date(value, "yyyy年MM月dd日 HH时mm分ss秒SSS毫秒");
				}
				return str2Date(value, "yyyy年MM月dd日 HH时mm分ss秒");
			}
			return str2Date(value, "yyyy年MM月dd日");
		}
		if (28 == value.length() && 20 == value.indexOf("CST")) {
			/*
			 * GMT(Greenwich Mean Time)代表格林尼治标准时间，全球都以格林威治的时间作为标准来设定时间 UTC(Coordinated
			 * Universal Time)代表世界协调时间（又称世界标准时间、世界统一时间）
			 * 是经过平均太阳时(以格林威治时间GMT为准)、地轴运动修正后的新时标以及以「秒」为单位的国际原子时所综合精算而成的时间，计算过程相当严谨精密
			 * CST却同时可以代表如下 4 个不同的时区： Central Standard Time (USA) UT-6:00 美国标准时间 Central
			 * Standard Time (Australia) UT+9:30 澳大利亚标准时间 China Standard Time UT+8:00 中国标准时间
			 * Cuba Standard Time UT-4:00 古巴标准时间
			 */
			String year = value.substring(24, 28);
			String date = value.substring(8, 10);
			String temp = value.substring(4, 7);
			value = year + "-" + getMonthNumByMonthEng(temp) + "-" + date;
		}
		if (value.contains("-")) {
			if (value.length() == 23) {
				return str2Date(value, "yyyy-MM-dd HH:mm:ss.SSS");
			}
			if (value.length() == 19) {
				return str2Date(value, DATE_TIME_FORMAT);
			}
			if (value.length() == 10) {
				return str2Date(value, DATE_FORMAT);
			}
		}
		if (value.contains("/")) {
			if (value.length() == 23) {
				return str2Date(value, "yyyy/MM/dd HH:mm:ss.SSS");
			}
			if (value.length() == 19) {
				return str2Date(value, "yyyy/MM/dd HH:mm:ss");
			}
			if (value.length() == 10) {
				return str2Date(value, "yyyy/MM/dd");
			}
		}
		if (value.contains(":")) {
			if (value.length() == 10) {
				return str2Date(value, "HH:mm:ss");
			}
			if (value.contains(".") && value.length() == 12) {
				return str2Date(value, "HH:mm:ss.SSS");
			}
		}
		if (value.contains(" ")) {
			if (value.length() == 18) {
				return str2Date(value, "yyyyMMdd HHmmssSSS");
			}
			if (value.length() == 15) {
				return str2Date(value, "yyyyMMdd HHmmss");
			}
		}
		if (value.length() == 17) {
			return str2Date(value, "yyyyMMddHHmmssSSS");
		}
		if (value.length() == 14) {
			return str2Date(value, "yyyyMMddHHmmss");
		}
		if (value.length() == 9) {
			return str2Date(value, "HHmmssSSS");
		}
		if (value.length() == 8) {
			return str2Date(value, "yyyyMMdd");
		}
		if (value.length() == 6) {
			return str2Date(value, "HHmmss");
		}
		return null;
	}

	/**
	 * 日期 转 字符串
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            日期格式
	 * @return String
	 */
	public static String date2Str(Date date, String format) {
		if (null == format || "".equals(format)) {
			format = DATE_FORMAT;
		}
		if (null == date) {
			date = getNowDate();
		}
		DateFormat dateformat = new SimpleDateFormat(format);
		return dateformat.format(date);
	}

	/**
	 * 字符串 转 日期
	 * 
	 * @param dateStr
	 *            字符串日期
	 * @param formatStr
	 *            日期格式
	 * @return Date
	 */
	public static Date str2Date(String dateStr, String formatStr) {
		if (dateStr.contains("-")) {

		}
		if (null == formatStr || "".equals(formatStr)) {
			formatStr = DATE_FORMAT;
		}
		DateFormat dateformat = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = dateformat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("!!!!!!!!input \"" + dateStr + "\" is worry! ");
		}
		return date;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param sStartdate
	 * @param bEnddate
	 * @param dateFormat
	 *            如果比较差值为天，输入yyyy-MM-dd； 如果比较差值其他，请输入正确的值
	 * @return
	 * @throws ParseException
	 */
	public static double daysBetween(Date startdate, Date enddate) throws ParseException {
		long time2 = enddate.getTime();
		long time1 = startdate.getTime();
		long diff = time2 - time1;
		return Double.parseDouble((diff / (1000 * 60 * 60 * 24)) + "." + ((diff % (1000 * 60 * 60 * 24)) / (24 * 60 * 60 * 10)));
	}

	public static double daysBetween(String startdate, String enddate, String dateFormat) throws ParseException {
		return daysBetween(str2Date(startdate, dateFormat), str2Date(enddate, dateFormat));
	}

	public static String nextDayDate(String loopDate, int addOsubNum) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Date4OldUtil.str2Date(loopDate, null));
		calendar.add(Calendar.DAY_OF_YEAR, addOsubNum);
		return Date4OldUtil.date2Str(calendar.getTime(), null);
	}

	/**
	 * @Description: 比较两个时间的分数差
	 * @param sStartdate
	 * @param bEnddate
	 * @param dateFormat
	 * @return
	 * @throws ParseException
	 * @author: zhangzhiguang
	 * @date: 2016-9-5 下午6:05:26
	 */
	public static long compareTime(String sStartdates, String bEnddates, String dateFormat) throws ParseException {
		Date sStartdate = str2Date(sStartdates, dateFormat);
		Date bEnddate = str2Date(bEnddates, dateFormat);
		if (null == dateFormat || "".equals(dateFormat)) {
			dateFormat = DATE_FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sStartdate = sdf.parse(sdf.format(sStartdate));
		bEnddate = sdf.parse(sdf.format(bEnddate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(sStartdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bEnddate);
		long time2 = cal.getTimeInMillis();
		Long between_days = (time2 - time1) / (1000);
		return between_days;
	}

	public static void main(String[] args) {
		String str = "20170123";
		// System.err.println(isLeapYear(str));
		Date date = Date4OldUtil.str2Date(str, "yyyyMMdd");
		System.err.println(date.toString());
		System.out.println("Fri Aug 28 2009 23:37:46 GMT".length());
		try {
			System.err.println(date2Str(converString2Date("2018年12月11日"), DATE_FORMAT));
			System.err.println(date2Str(converString2Date("2018/12/12 12:12:12.009"), DATE_TIME_FORMAT+".SSS"));
			System.err.println(date2Str(converString2Date("2018/12/12 12:12:12"), DATE_TIME_FORMAT));
			System.err.println(date2Str(converString2Date("2018年12月11日 12时12分12秒"), DATE_TIME_FORMAT));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(Date4OldUtil.date2Str(date, "yyyy-MM-dd"));
		// String _sys_time = "2016-9-5 18:07:10";
		// String _now_time = Date4OldUtil.date2Str(null, "yyyy-MM-dd hh:mm:ss");//
		// 当前交易时间
		// try {
		// long number = Date4OldUtil.compareTime(_sys_time, _now_time, "yyyy-MM-dd
		// hh:mm:ss");
		// System.out.println(number);
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}
