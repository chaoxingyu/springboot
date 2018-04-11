package com.cxy.baseboot2.base.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassRefUtil {

	@SuppressWarnings("rawtypes")
	public static Field[] getFields(Class clazz) {
		List<Field> listFields = new ArrayList<Field>();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			Field[] fields = clazz.getDeclaredFields();
			if (fields != null) {
				for (Field field : fields) {
					listFields.add(field);
				}
			}
		}
		Field[] fields = new Field[listFields.size()];
		return listFields.toArray(fields);
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, Class> getFieldTypeClassFilter(Class clazz) {
		Map<String, Class> hashmap = new HashMap<String, Class>();
		Field[] fields = getFields(clazz);
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if ("serialversionuid".equals(field.getName().toLowerCase()) || "__".startsWith(field.getName()))
					continue;
				hashmap.put(field.getName(), field.getType());
			}
		}
		return hashmap;
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, Class> getFieldTypeClass(Class clazz) {
		Map<String, Class> hashmap = new HashMap<String, Class>();
		Field[] fields = getFields(clazz);
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				hashmap.put(field.getName(), field.getType());
			}
		}
		return hashmap;
	}

	/**
	 * 将平级数据转换成map形式。对list等对象无处理。
	 * @param object
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, Object> getValue(Object object) throws Exception {
		Map<String, Object> hashmap = new HashMap<String, Object>();
		Field[] fields = getFields(object.getClass());
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if ("serialversionuid".equals(field.getName().toLowerCase()) || "__".startsWith(field.getName()))
					continue;
				String methodName = "get" + ("" + field.getName().charAt(0)).toUpperCase() + field.getName().substring(1);
				Object obj = invokeMethod(object, methodName, null, null);
				if (obj == null)
					hashmap.put(field.getName(), formatValue(field.getType().getName(), null));
				else
					hashmap.put(field.getName(), obj);
			}
		}
		return hashmap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object setValue(Class clazz, Map<String, Object> mapValue) throws Exception {
		Field[] fields = getFields(clazz);
		if (fields != null) {
			Constructor con = clazz.getConstructor();
			Object object = con.newInstance();
			for (int i = 0; i < fields.length; i++) {
				String fieldName = fields[i].getName();
				if ("serialversionuid".equals(fieldName.toLowerCase()) || fieldName.startsWith("__"))
					continue;
				String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				if (mapValue.containsKey(fieldName)) {
					Class filedClass = fields[i].getType();
					invokeMethod(object, methodName, new Class[] { filedClass }, new Object[] { formatValue(filedClass.getName(), mapValue.get(fieldName)) });
				}
			}
			return object;
		}
		return null;
	}

	public static Object formatValue(String type, Object value) {
		if ("java.lang.String".equals(type)) {
			return isNullOrKong(value) ? null : (String)value;
		} else if ("java.math.BigDecimal".equals(type)) {
			return isNullOrKong(value) ? null : (BigDecimal)value;
		} else if ("java.math.BigInteger".equals(type)) {
			return isNullOrKong(value) ? null : (BigInteger)value;
		} else if ("boolean".equals(type) || "java.lang.Boolean".equals(type)) {
			return isNullOrKong(value) ? false : (Boolean)value;
		} else if ("int".equals(type) || "java.lang.Integer".equals(type)) {
			return isNullOrKong(value) ? 0 : new Integer(value.toString());
		} else if ("float".equals(type) || "java.lang.Float".equals(type)) {
			return isNullOrKong(value) ? 0 : new Float(value.toString());
		} else if ("double".equals(type) || "java.lang.Double".equals(type)) {
			return isNullOrKong(value) ? 0 : new Double(value.toString());
		} else if ("long".equals(type) || "java.lang.Long".equals(type)) {
			return isNullOrKong(value) ? 0 : new Long(value.toString());
		} else if ("short".equals(type) || "java.lang.Short".equals(type)) {
			return isNullOrKong(value) ? 0 : new Short(value.toString());
		} else if ("byte".equals(type) || "java.lang.Byte".equals(type)) {
			return isNullOrKong(value) ? 0 : new Byte(value.toString());
		} else {
			return value;
		}
	}

	private static boolean isNullOrKong(Object value) {
		if (value == null || "".equals(value.toString()))
			return true;
		return false;
	}

	/**
	 * 循环向上转型, 获取对象的 DeclaredMethod
	 * 
	 * @param object子类对象
	 * @param methodName父类中的方法名
	 * @param parameterTypes父类中的方法参数类型
	 * @return 父类中的方法对象
	 */
	public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
		for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				return clazz.getDeclaredMethod(methodName, parameterTypes);
			} catch (Exception e) {
				// 如果这里的异常打印或者往外抛，则就不会执行clazz=clazz.getSuperclass(),最后就不会进入到父类中了
			}
		}
		return null;
	}

	/**
	 * 直接调用对象方法, 而忽略修饰符(private, protected, default)
	 * 
	 * @param object子类对象
	 * @param methodName父类中的方法名
	 * @param parameterTypes父类中的方法参数类型
	 * @param parameters父类中的方法参数
	 * @return 父类中方法的执行结果
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Exception {
		// 根据 对象、方法名和对应的方法参数 通过反射 调用上面的方法获取 Method 对象
		Method method = getDeclaredMethod(object, methodName, parameterTypes);
		if (method == null)
			return null;
		// 抑制Java对方法进行检查,主要是针对私有方法而言
		method.setAccessible(true);
		try {
			if (null != method) {
				// 调用object 的 method 所代表的方法，其方法的参数是 parameters
				return method.invoke(object, parameters);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的 DeclaredField
	 * 
	 * @param object子类对象
	 * @param fieldName父类中的属性名
	 * @return 父类中的属性对象
	 */
	public static Field getDeclaredField(Object object, String fieldName) {
		Class<?> clazz = object.getClass();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				return clazz.getDeclaredField(fieldName);
			} catch (Exception e) {
				// 如果这里的异常打印或者往外抛，则就不会执行clazz=clazz.getSuperclass(),最后就不会进入到父类中了
			}
		}
		return null;
	}

	/**
	 * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
	 * 
	 * @param object子类对象
	 * @param fieldName父类中的属性名
	 * @param value将要设置的值
	 * @throws Exception 
	 */
	public static void setFieldValue(Object object, String fieldName, Object value) throws Exception {
		// 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
		Field field = getDeclaredField(object, fieldName);
		// 抑制Java对其的检查
		field.setAccessible(true);
		try {
			// 将 object 中 field 所代表的值 设置为 value
			field.set(object, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw e;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
	 * 
	 * @param object子类对象
	 * @param fieldName父类中的属性名
	 * @return : 父类中的属性值
	 * @throws Exception 
	 */
	public static Object getFieldValue(Object object, String fieldName) throws Exception {
		// 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
		Field field = getDeclaredField(object, fieldName);
		// 抑制Java对其的检查
		field.setAccessible(true);
		try {
			// 获取 object 中 field 所代表的属性值
			return field.get(object);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
