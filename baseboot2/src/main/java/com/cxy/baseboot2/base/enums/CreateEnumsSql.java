package com.cxy.baseboot2.base.enums;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.net.URL;

import com.cxy.baseboot2.base.enums.interfaces.Description;
import com.cxy.baseboot2.base.utils.ClassRefUtil;


public class CreateEnumsSql {
	public static void main(String[] args) throws Exception {
		StringBuffer stringBuffer = new StringBuffer();
		StringBuffer stringBufferClass = new StringBuffer();

		// TODO 待处理
		URL url = CreateEnumsSql.class.getClassLoader().getResource("");
		String abspath = url.getPath();
		String classPath = "/" + abspath.substring(abspath.indexOf("WebContent"));
		System.err.println(classPath);
		abspath = abspath.substring(1, abspath.indexOf("WebContent"));
		String sourcepath = "src/";
		String 包 = "com/cxy/baseboot2/";
		String sql目录 = "sql/";
		File filePath = new File(abspath + sourcepath + 包);
		File[] files = filePath.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.getName().startsWith("A_C") || file.getName().startsWith("Description."))
					continue;
				String className = file.getName().substring(0, file.getName().lastIndexOf("."));
				@SuppressWarnings("rawtypes")
				Class clazz = Class.forName(包.replaceAll("/", ".") + className);
				int i = 0;
				for (Object obj : clazz.getEnumConstants()) {
					String value = (ClassRefUtil.invokeMethod(obj, "getValue", null, null).toString());
					String desc = (ClassRefUtil.invokeMethod(obj, "getDesc", null, null).toString());
					stringBuffer.append("insert into SYS_ENUM_DATA (ENUM_ID, SYS_ID, CLASS_ID, ENUM_PID, ENUM_CODE, ENUM_NAME, ENUM_DESC, ENUM_VALUE, ENUM_ORDER, ENUM_VERSION, EDIT_TYPE, STATUS, FRONT_SELECT, EDIT_LEVEL, USER_ID, MOD_TIME, APP_ID, APP_STATUS) values ");
					stringBuffer.append("('" + className + "_" + obj + "', 'InternetCredit', '" + className + "', null, '" + obj + "', '" + desc + "', '" + desc + "', '" + value + "', " + i + ", null, 0, 1, 1, 0, null, null, null, '1'); \n");
					i++;
				}
				Annotation[] anns = clazz.getAnnotations();
				String desc = className;
				for (Annotation annotation : anns) {
					if(annotation instanceof Description){
						desc = ((Description) annotation).value();
						break;
					}
				}
				stringBufferClass.append("insert into SYS_CLASS (CLASS_ID, SYS_ID, CLASS_NAME, CLASS_DESC, PARENT_ID, EDIT_LEVEL) values ");
				stringBufferClass.append("('" + className + "', 'InternetCredit', '" + desc + "',  '" + desc + "', 'EnumData', 0); \n");
				
				stringBuffer.append("\n");
			}
		}
		writeFile(abspath + sql目录 + "init_gen_enum.sql", stringBuffer.toString(), "UTF-8", false);
		writeFile(abspath + sql目录 + "init_gen_class.sql", stringBufferClass.toString(), "UTF-8", false);
	}

	public static void writeFile(String fileName, String content, String destEncoding, boolean append) throws FileNotFoundException, IOException {
		createNewFile(fileName);
		BufferedWriter out = null;
		FileOutputStream fos = new FileOutputStream(fileName, append);
		out = new BufferedWriter(new OutputStreamWriter(fos, destEncoding));
		out.write(content);
		out.flush();

		try {
			if (out != null)
				out.close();
		} catch (IOException ex) {
		}
	}

	public static void createNewFile(String fileName) throws FileNotFoundException, IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			mkDirs(getFilePath(fileName));
			file.createNewFile();
		}
		if (!file.isFile()) {
			throw new IOException("'" + fileName + "' is not a file.");
		}
		if (!file.canWrite()) {
			throw new IOException("'" + fileName + "' is a read-only file.");
		}
	}

	public static void mkDirs(String dir) throws IOException {
		File file = new File(dir);
		if (file.exists()) {
			return;
		} else if (file.mkdirs() == false) {
			throw new IOException("Cannot create directories = " + dir);
		}
	}

	public static String getFilePath(String fullFilePath) {
		if (fullFilePath == null) {
			return "";
		}
		int index1 = fullFilePath.lastIndexOf('/');
		int index2 = fullFilePath.lastIndexOf('\\');
		int index = (index1 > index2) ? index1 : index2;
		if (index == -1) {
			return null;
		}
		String filepath = fullFilePath.substring(0, index);
		return filepath;
	}
}
