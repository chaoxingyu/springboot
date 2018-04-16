package com.cxy.baseboot2.base.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

	/**
	 * 获取文件绝对路径
	 * 
	 * @param path
	 *            文件路径
	 * @return String
	 */
	public static String getAbsolutePath(String path) {
		if (StringUtil.isNullOrBlock(path)) {
			return null;
		}
		if (path.contains("\\")) {
			path = path.replaceAll("\\\\", "/");
		}
		if (!path.endsWith("/")) {
			path = path + "/";
		}
		return path;
	}

	/**
	 * 获取文件目录路径或文件名称
	 * 
	 * @param isDir
	 *            true-获取文件目录路径
	 * @param filePath
	 *            文件路径
	 * @return String
	 */
	public static String getDirPathOrFileName(boolean isDir, String filePath) {
		if (StringUtil.isNullOrBlock(filePath)) {
			return null;
		}
		if (filePath.contains("\\")) {
			filePath = filePath.replaceAll("\\\\", "/");
		}
		if (filePath.contains("/")) {
			if (isDir) {
				return filePath.substring(0, filePath.lastIndexOf("/") + 1);
			} else {
				String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
				if (fileName.contains(".")) {
					return fileName;
				}
			}
		}
		return null;
	}

	/**
	 * 获取文件后缀名
	 * 
	 * @param fileName
	 *            文件名称
	 * @return
	 */
	public static String getFileType(String fileName) {
		if (StringUtil.isNotNullOrBlock(fileName) && fileName.contains(".")) {
			return fileName.substring(fileName.lastIndexOf("."), fileName.length());
		}
		return null;
	}

	/**
	 * 判断文件是否为图片文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @param imageType
	 *            图片类型
	 * @return boolean
	 */
	public static boolean isImageFile(String fileName, String imageType) {
		if (StringUtil.isNullOrBlock(fileName)) {
			return false;
		}
		String fileType = getFileType(fileName);
		if (StringUtil.isNullOrBlock(fileType)) {
			return false;
		}
		fileType = fileType.toLowerCase();
		if (StringUtil.isNullOrBlock(imageType)) {
			String[] img_type = new String[] { ".jpg", ".jpeg", ".png", ".gif", ".bmp" };
			for (String type : img_type) {
				if (fileType.equals(type)) {
					return true;
				}
			}
		} else {
			imageType = imageType.toLowerCase();
			if (fileType.equals(imageType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断文件是否为pdf文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @return boolean
	 */
	public static boolean isPdfFile(String fileName) {
		String fileType = getFileType(fileName);
		if (StringUtil.isNullOrBlock(fileType)) {
			return false;
		}
		fileType = fileType.toUpperCase();
		if (fileType.equals(".PDF")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断已存在的文件目录下 是否可创建文件/文件夹及可删除
	 * 
	 * @param dir 目录
	 * @return boolean
	 */
	public static boolean isWritableDirectory(Path dir) {
		if (null == dir) {
			throw new IllegalArgumentException("the argument 'dir' must not be null");
		}
		if (!Files.isDirectory(dir)) {
			throw new IllegalArgumentException("the argument 'dir' must be a exist directory");
		}
		try {
			Path tmpDir = Files.createTempDirectory(dir, null); // 随机生成文件夹名称
			Files.delete(tmpDir);
			Path tmpFile = Files.createTempFile(dir, null, null); // 随机生成文件名称.tmp
			Files.delete(tmpFile);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 文件路径
	 * 
	 * @param filePath 文件路径
	 * @return Path
	 * @throws IOException 
	 */
	public static Path getPath4WriteFile(String filePath) throws IOException {
		String dirPath = getDirPathOrFileName(true, filePath);
		Path dir = Paths.get(dirPath);
		if (!Files.exists(dir)) {
			Files.createDirectories(dir);
		}else {
			if(!isWritableDirectory(dir)) {
				throw new IOException("待写入文件的内容为空！");
			}	
		}
		return Paths.get(filePath);
	}
	
	/**
	 * 写文件
	 * 
	 * @param str
	 *            字符串
	 * @param filePath
	 *            文件路径
	 * @throws IOException
	 */
	public static void writeFileByString(String str, String filePath) throws IOException {
		Path path = getPath4WriteFile(filePath);
		Files.write(path, str.getBytes());
	}

	/**
	 * 写文件
	 * 
	 * @param content
	 *            待写入文件的内容
	 * @param filePath
	 *            文件路径
	 * @throws IOException
	 */
	public static void writeFileByByte(byte[] content, String filePath) throws IOException {
		Path path = getPath4WriteFile(filePath);
		Files.write(path, content);
	}


	/**
	 * 写文件
	 * 
	 * @param str
	 *            字符串
	 * @param fileDirPath
	 *            文件目录路径
	 * @param fileName
	 *            文件名称
	 * @throws IOException
	 */
	public static void writeFileByString(String str, String fileDirPath, String fileName) throws IOException {
		if (StringUtil.isNullOrBlock(fileDirPath) || StringUtil.isNullOrBlock(fileName)) {
			throw new IOException("文件目录路径[" + fileDirPath + fileName + "] 错误！");
		}
		if (StringUtil.isNullOrBlock(fileName)) {

		}
		if (StringUtil.isNullOrBlock(str)) {
			throw new IOException("待写入文件的内容为空！");
		}
		String filePath = getAbsolutePath(fileDirPath) + fileName;
		writeFileByString(str, filePath);
	}
	

}
