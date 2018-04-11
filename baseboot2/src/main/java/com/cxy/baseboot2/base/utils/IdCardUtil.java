package com.cxy.baseboot2.base.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @package com.cxy.baseboot2.base.utils
 * @type IdCardUtil
 * @description 身份证公共类
 * @author cxy
 * @date 2018年3月30日 下午4:43:13
 * @version 1.0.0
 */
public class IdCardUtil {

	private static final String FORMAT_DATE = "yyyyMMdd";

	/**
	 * 设置地区编码
	 * 
	 * @return
	 */
	private static HashMap<String, String> getAreaCode() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("11", "北京");
		map.put("12", "天津");
		map.put("13", "河北");
		map.put("14", "山西");
		map.put("15", "内蒙古");
		map.put("21", "辽宁");
		map.put("22", "吉林");
		map.put("23", "黑龙江");
		map.put("31", "上海");
		map.put("32", "江苏");
		map.put("33", "浙江");
		map.put("34", "安徽");
		map.put("35", "福建");
		map.put("36", "江西");
		map.put("37", "山东");
		map.put("41", "河南");
		map.put("42", "湖北");
		map.put("43", "湖南");
		map.put("44", "广东");
		map.put("45", "广西");
		map.put("46", "海南");
		map.put("50", "重庆");
		map.put("51", "四川");
		map.put("52", "贵州");
		map.put("53", "云南");
		map.put("54", "西藏");
		map.put("61", "陕西");
		map.put("62", "甘肃");
		map.put("63", "青海");
		map.put("64", "宁夏");
		map.put("65", "新疆");
		map.put("71", "台湾");
		map.put("81", "香港");
		map.put("82", "澳门");
		map.put("91", "国外");
		return map;
	}

	/**
	 * 功能：判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能：判断字符串是否全为英文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEnglish(String str) {
		Pattern pattern = Pattern.compile("^[a-zA-Z]*");
		Matcher isEng = pattern.matcher(str);
		if (isEng.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 校验 省份代码
	 * 
	 * @param provinceid
	 * @return
	 */
	private static boolean valiateProvinceCode(String provinceid) {
		boolean flag = false;
		if (getAreaCode().containsKey(provinceid)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 校验日期
	 * 
	 * @param str
	 *            日期
	 * @param formatDate
	 *            日期转换格式
	 * @param lenient
	 *            是否宽松验证日期
	 * @return boolean
	 */
	public static boolean isValidDate(String str, String formatDate, boolean lenient) {
		boolean convertSuccess = true;
		// 指定日期格式为四位年两位月份两位日期，注意yyyyMMdd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat(formatDate);
		try {
			// 设置lenient为false,否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(lenient);
			format.parse(str);
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			e.printStackTrace();
			convertSuccess = false;
		}
		return convertSuccess;
	}

	/**
	 * 18位身份证校验最后一位
	 * 
	 * @param numSub
	 * @return
	 */
	private static boolean validateLastNumber(String idcard, String numSub) {
		boolean bool = false;
		// 每位加权因子
		int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		// 第18位校检码
		String verifyCode[] = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
		int total = 0;
		for (int i = 0; i < 17; i++) {
			total = total + Integer.parseInt(String.valueOf(numSub.charAt(i))) * power[i];
		}
		int modValue = total % 11;
		String strVerifyCode = verifyCode[modValue];
		numSub = numSub + strVerifyCode;
		if (idcard.equals(numSub)) {
			bool = true;
		}
		return bool;
	}

	/**
	 * 验证所有的身份证的合法性
	 * 
	 * @param idcard
	 * @return
	 */
	public static boolean isValidatedAllIdcard(String idcard) {
		if (null == idcard || "".equals(idcard)) {
			return false;
		}
		String numSub = "";
		if (idcard.length() == 18) {
			numSub = idcard.substring(0, 17);
			// 不强制要求用户输入身份证中最后一位为X时 X是否大小写
			if (isEnglish(idcard.substring(17, 18))) {
				idcard = idcard.substring(0, 17) + idcard.substring(17, 18).toUpperCase();
			}
		} else if (idcard.length() == 15) {
			numSub = idcard.substring(0, 6) + "19" + idcard.substring(6, 15);
		} else {
			return false;
		}
		// 校验 证件号码的数字性
		if (!isNumeric(numSub)) {
			return false;
		}
		// 校验省份
		String provinceid = numSub.substring(0, 2);
		if (!valiateProvinceCode(provinceid)) {
			return false;
		}
		// 校验日期
		String strdate = numSub.substring(6, 14);// 年月日
		if (!isValidDate(strdate, FORMAT_DATE, false)) {
			return false;
		}
		// 校验18位身份证最后一位
		if (idcard.length() == 18) {
			if (!validateLastNumber(idcard, numSub)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 随机出生日期
	 * 
	 * @param year
	 *            小于1950年则年范围为1950到当前服务器日期的前十年
	 * @return String
	 */
	public String randomBirthday(int year) {
		Calendar birthday = Calendar.getInstance();
		if (1950 > year) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
			String nowYear = dateFormat.format(new Date());
			int num = Integer.parseInt(nowYear) - 1950 - 10; // 年份范围为1950～2010年
			birthday.set(Calendar.YEAR, (int) (Math.random() * num) + 1950);
		} else {
			birthday.set(Calendar.YEAR, year);
		}
		birthday.set(Calendar.MONTH, (int) (Math.random() * 12));
		birthday.set(Calendar.DATE, (int) (Math.random() * 31));

		StringBuilder builder = new StringBuilder();
		builder.append(birthday.get(Calendar.YEAR));
		long month = birthday.get(Calendar.MONTH) + 1;
		if (month < 10) {
			builder.append("0");
		}
		builder.append(month);
		long date = birthday.get(Calendar.DATE);
		if (date < 10) {
			builder.append("0");
		}
		builder.append(date);
		return builder.toString();
	}

	/**
	 * 随机产生3位数
	 * 
	 * @return
	 */
	public String randomCode() {
		int code = (int) (Math.random() * 1000);
		if (code < 10) {
			return "00" + code;
		} else if (code < 100) {
			return "0" + code;
		} else {
			return "" + code;
		}
	}

	/**
	 * 生成身份证最后一位
	 * <p>
	 * 18位身份证验证
	 * </p>
	 * 根据〖中华人民共和国国家标准 GB 11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
	 * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。 第十八位数字(校验码)的计算方法为：
	 * 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
	 * 2.将这17位数字和系数相乘的结果相加。 3.用加出来和除以11，看余数是多少？ 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10
	 * 这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2。
	 * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
	 * 
	 * @param chars
	 *            身份证前17位
	 * @return char
	 */
	public char calcTrailingNumber(char[] chars) {
		if (null == chars || chars.length < 17) {
			return ' ';
		}
		int[] c = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		char[] r = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		int[] n = new int[17];
		int result = 0;
		for (int i = 0; i < n.length; i++) {
			n[i] = Integer.parseInt(chars[i] + "");
		}
		for (int i = 0; i < n.length; i++) {
			result += c[i] * n[i];
		}
		return r[result % 11];
	}

	public String formatDate(String dateStr, String formatDate) {
		SimpleDateFormat format = new SimpleDateFormat(formatDate);
		try {
			Date date = format.parse(dateStr);
			dateStr = format.format(date);
			return dateStr;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		IdCardUtil id = new IdCardUtil();
		for (int i = 0; i < 10; i++) {
			System.err.println(id.randomBirthday(0));
		}
	}
}
