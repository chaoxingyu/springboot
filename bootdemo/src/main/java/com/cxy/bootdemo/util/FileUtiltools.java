package com.cxy.bootdemo.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * 文件上传下载公共类
 * 
 * @author cxy
 *
 */
public class FileUtiltools {

	/**
	 * 文件上传
	 * 
	 * @param file
	 *            二进制流
	 * @param filePath
	 *            上传路径
	 * @param fileName
	 *            文件名称
	 * @throws Exception
	 */
	public static void uploadFile(byte[] file, String filePath, String fileName) {
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath + fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.write(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 判断文件是否为pdf文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @return
	 */
	public Boolean isPdfFile(String fileName) {
		if (null == fileName) {
			return false;
		}
		if (fileName.endsWith(".pdf")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断文件是否为图片文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @param imageType
	 *            图片类型
	 * @return
	 */
	public Boolean isImageFile(String fileName, String imageType) {
		if (null == fileName) {
			return false;
		}
		fileName = fileName.toLowerCase();
		if (null == imageType || imageType.equals("")) {
			String[] img_type = new String[] { ".jpg", ".jpeg", ".png", ".gif", ".bmp" };
			for (String type : img_type) {
				if (fileName.endsWith(type)) {
					return true;
				}
			}
		} else {
			imageType = imageType.toLowerCase();
			if (fileName.endsWith(imageType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取文件后缀名
	 * 
	 * @param fileName
	 *            文件名称
	 * @return
	 */
	public String getFileType(String fileName) {
		if (null != fileName && fileName.indexOf(".") >= 0) {
			return fileName.substring(fileName.lastIndexOf("."), fileName.length());
		}
		return null;
	}

}
