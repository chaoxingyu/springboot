package com.cxy.baseboot2.base.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

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
				if (filePath.lastIndexOf("/") == filePath.length()) {
					return filePath;
				} else {
					return filePath.substring(0, filePath.lastIndexOf("/") + 1);
				}
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
	 * 判断本地是否存在
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	public static boolean isLocalFileExist(String filePath) {
		Path path = Paths.get(filePath);
		// 检测时默认不包含符号链接文件
		if (Files.exists(path)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取本地文件大小
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static long getLocalFileSize(String filePath) throws Exception {
		Path path = Paths.get(filePath);
		return Files.size(path);
	}

	/**
	 * 遍历目录下所有文件(含子目录)
	 * 
	 * @param dir
	 *            目录
	 * @return
	 * @throws Exception
	 */
	public static LinkedList<Path> getAllPath4Dir(String dir) throws Exception {
		Path path = Paths.get(dir);
		LinkedList<Path> dirList = new LinkedList<>(); // 保存待遍历文件夹的列表
		LinkedList<Path> pathList = new LinkedList<>(); // 文件列表
		// 调用遍历文件夹根目录文件的方法
		getPath4Dir(path, dirList, pathList);
		Path temp = null;
		while (!dirList.isEmpty()) {
			temp = (Path) dirList.removeFirst();
			getPath4Dir(temp, dirList, pathList);
		}
		if (null == pathList || pathList.isEmpty()) {
			System.err.println("path--> is null!");
		}
		pathList.forEach(action -> {
			System.out.println("action-->" + action.toString());
		});
		return pathList;
	}

	/**
	 * 遍历目录
	 * 
	 * @param path
	 *            目录
	 * @param dirList
	 *            子目录List
	 * @param pathList
	 *            文件List
	 * @throws Exception
	 */
	private static void getPath4Dir(Path path, LinkedList<Path> dirList, LinkedList<Path> pathList) throws Exception {
		// 每个文件夹遍历都会调用该方法
		if (null == path || !Files.exists(path) || !Files.isDirectory(path)) {
			return;
		}
		Files.list(path)
						// .filter(filter-> !Files.isHidden(filter))
						.forEach(subPath -> {
							if (Files.isDirectory(subPath)) {
								dirList.add(subPath);
							} else {
								pathList.add(subPath);
							}
						});
	}

	public static void main(String[] args) throws Exception {
		// TODO
		String dir = "/Users/chaoxingyu/eclipse-workspace/4.7WorkSpace/test/";
		// System.out.println(getDirPathOrFileName(true,dir));
		// getAllPath4Dir(dir);
		Path path = Paths.get(dir);
		System.err.println(Files.isDirectory(path) + "----" + path.toAbsolutePath());
		System.out.println(path.getParent() + "----" + path.getFileName());
		// String str = "入场电子版资料（毕业证，学位证，身份证正反面，简历）";
		// Files.write(path, str.getBytes());
	}

	/**
	 * 判断已存在的文件目录下 是否可创建文件/文件夹及可删除
	 * 
	 * @param dir
	 *            目录
	 * @return boolean
	 */
	public static boolean isWritableDirectory(Path dir) {
		if (null == dir) {
			throw new IllegalArgumentException("the argument 'dir' must  be not null");
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
	 * 创建文件目录
	 * 
	 * @param dir
	 *            目录
	 * @return
	 * @throws Exception
	 */
	public static Path getPath4WriteFile4Dir(String dir) throws Exception {
		Path path = Paths.get(dir);
		// 判断文件(含文件夹)是否存在
		if (Files.exists(path)) {
			return path;
		}
		Files.createDirectories(path);
		return path;
	}

	/**
	 * 写文件前创建文件目录
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 * @throws Exception
	 */
	public static Path getPath4WriteFile(String filePath) throws Exception {
		Path path = Paths.get(filePath);
		// 判断文件(含文件夹)是否存在
		if (Files.exists(path)) {
			return path;
		}
		// 判断其父级目录是否存在
		return getPath4WriteFile4Dir(getAbsolutePath(path.getParent().toString()));
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
	public static void writeFileByString(String str, String filePath) throws Exception {
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
	public static void writeFileByByte(byte[] content, String filePath) throws Exception {
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
	 * @throws Exception
	 */
	public static void writeFileByString(String str, String fileDirPath, String fileName) throws Exception {
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
