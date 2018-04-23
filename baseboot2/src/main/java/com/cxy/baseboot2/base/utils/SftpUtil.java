package com.cxy.baseboot2.base.utils;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * 
 * @package com.cxy.baseboot2.base.utils
 * @type SftpUtil
 * @description 使用jsch处理Sftp文件上传与下载
 * @author cxy
 * @date 2018年4月19日 上午10:20:28
 * @version 1.0.0
 */
public class SftpUtil {

	private static final Logger LOG = LoggerFactory.getLogger(SftpUtil.class);

	private ChannelSftp sftp; // sftp主服务
	private Channel channel;// 通道
	private Session sshSession; // 会话

	private String host; // 远程服务器地址
	private int port;// 端口
	private String username;// 用户名
	private String password;// 密码

	/**
	 * 构造函数，根据私钥产生对象
	 * 
	 * @param host
	 *            远程服务器地址
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @throws Exception
	 */
	public SftpUtil(String host, int port, String username, String password) throws Exception {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		init();
	}

	/**
	 * 初始化连接
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
		this.sftp = getConnect();
	}

	/**
	 * 连接sftp服务器
	 * 
	 * @return ChannelSftp
	 * @throws Exception
	 */
	private ChannelSftp getConnect() throws Exception {
		JSch jsch = new JSch();
		this.sshSession = jsch.getSession(username, host, port);
		sshSession.setPassword(password);
		// 设置第一次登陆的时候提示，可选值:(ask | yes | no)
		sshSession.setConfig("StrictHostKeyChecking", "no");
		sshSession.connect();
		channel = sshSession.openChannel("sftp");
		channel.connect();
		LOG.info("SFTP 服务器[{}] 连接成功！", host);
		return (ChannelSftp) channel;
	}

	/**
	 * 关闭连接
	 */
	public void disconnect() {
		if (null != this.sftp && this.sftp.isConnected()) {
			this.sftp.disconnect();
		}
		if (null != this.channel && this.channel.isConnected()) {
			this.channel.disconnect();
		}
		if (null != this.sshSession && this.sshSession.isConnected()) {
			this.sshSession.disconnect();
		}
	}

	/**
	 * 判断远程目录是否存在
	 * 
	 * @param dir
	 *            远程目录
	 * @return boolean
	 */
	private boolean dirIsExist(String dir) {
		try {
			SftpATTRS attrs = this.sftp.lstat(dir);
			if (attrs.isDir()) {
				return true;
			} else {
				LOG.info("远程[{}] 不是目录！", dir);
			}
		} catch (Exception e) {
			LOG.info("远程[{}] 不存在！", dir);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断远程文件是否存在
	 * 
	 * @param remoteFile
	 *            远程文件
	 * @return
	 */
	private boolean fileIsExist(String remoteFile) {
		try {
			SftpATTRS attrs = this.sftp.lstat(remoteFile);
			if (attrs.isReg()) {
				return true;
			}
			LOG.info("远程[{}] 不是文件！", remoteFile);
		} catch (Exception e) {
			LOG.error("远程[{}] 不存在！", remoteFile);
			LOG.error("判断远程文件是否存在 异常：", e);
			;
		}
		return false;
	}

	/**
	 * 获取远程文件大小
	 * 
	 * @param remoteFile
	 *            远程文件
	 * @return
	 * @throws Exception
	 */
	private long getFileSize(String remoteFile) throws Exception {
		try {
			SftpATTRS attrs = this.sftp.lstat(remoteFile);
			if (null == attrs || !attrs.isReg()) {
				throw new Exception("远程[" + remoteFile + "] 不是文件！");
			}
			return attrs.getSize();
		} catch (Exception e) {
			LOG.error("获取远程文件大小 异常：", e);
			;
			throw new Exception("获取远程文件[" + remoteFile + "]大小 异常!");
		}
	}

	/**
	 * 创建指定文件夹
	 * 
	 * @param remotePath
	 *            文件夹绝对路径
	 * @throws SftpException
	 */
	private void createRemoteDir(String remotePath) throws SftpException {
		remotePath = FileUtil.getAbsolutePath(remotePath);
		if (this.dirIsExist(remotePath)) {
			this.sftp.cd(remotePath);
			return;
		}
		String[] str = remotePath.trim().split("/");
		StringBuffer filePath = new StringBuffer("/");
		for (String path : str) {
			if (!path.equals("")) {
				filePath.append(path + "/");
				if (this.dirIsExist(filePath.toString())) {
					this.sftp.cd(filePath.toString());
				} else {
					this.sftp.mkdir(filePath.toString());
					this.sftp.cd(filePath.toString());
				}
			}
		}
		this.sftp.cd(remotePath);
	}

	/**
	 * 
	 * 上传文件
	 * 
	 * @param localPath
	 *            本地文件路径
	 * @param fileName
	 *            文件名称
	 * @param remotePath
	 *            远程目录
	 * @param isMonitor
	 *            是否记录文件传输进度
	 * @throws Exception
	 */
	public void uploadFile(String localPath, String fileName, String remotePath, boolean isMonitor) throws Exception {
		LOG.info("Sftp文件上传--->本地文件[{},{}]，远程目录[{}]--->开始！", localPath, fileName, remotePath);
		// 判断本地文件是否存在
		String filePath = FileUtil.getAbsolutePath(localPath) + fileName;
		long fileSize = 0l;
		try {
			fileSize = FileUtil.getLocalFileSize(filePath);
		} catch (Exception e) {
			LOG.error("Sftp文件上传异常: ", e);
			throw new Exception("Sftp文件上传异常: 本地文件[" + filePath + "] 不存在！");
		}
		// 判断远程文件夹是否存在
		createRemoteDir(remotePath);
		if (!isMonitor) {
			this.sftp.put(filePath, fileName);
		} else {
			// OVERWRITE---完全覆盖模式，默认 RESUME---恢复模式，断点续传 APPEND---追加模式
			this.sftp.put(filePath, fileName, new SftpFile4ProgressMonitor(fileSize), ChannelSftp.OVERWRITE);
		}
		LOG.info("Sftp文件上传--->本地文件[{},{}]，远程目录[{}]--->成功！", localPath, fileName, remotePath);
	}

	/**
	 * 上传文件
	 * 
	 * @param input
	 *            文件流
	 * @param fileName
	 *            文件名称
	 * @param remotePath
	 *            远程目录
	 * @throws Exception
	 */
	public void uploadFile(InputStream input, String fileName, String remotePath) throws Exception {
		LOG.info("Sftp文件上传--->本地文件[{}], 远程目录[{}]--->开始！", fileName, remotePath);
		try {
			createRemoteDir(remotePath);
			this.sftp.put(input, fileName);
			LOG.info("文件上传--->本地文件[{}], 远程目录[{}]--->成功！", fileName, remotePath);
		} finally {
			if (null != input) {
				try {
					input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 批量上传文件
	 * 
	 * @param localPath
	 *            本地目录
	 * @param remotePath
	 *            远程目录
	 * @param isMonitor
	 *            是否记录文件传输进度
	 * @throws Exception
	 */
	public void uploadBatchFiles(String localPath, String remotePath, boolean isMonitor) throws Exception {
		LOG.info("Sftp批量文件上传--->本地目录[{}], 远程目录[{}]--->开始处理！", localPath, remotePath);
		// 判定本地文件
		LinkedList<Path> localList = null;
		try {
			localList = FileUtil.getAllPath4Dir(localPath);
		} catch (Exception e) {
			String msg = "Sftp批量文件上传--->本地目录[" + localPath + "] 遍历目录时 出现异常！";
			LOG.error(msg, e);
			throw new Exception(msg);
		}
		if (null == localList || localList.isEmpty()) {
			String msg = "Sftp批量文件上传--->本地目录[" + localPath + "] 无 文件！";
			throw new Exception(msg);
		}
		for (Path path : localList) {
			uploadFile(path.getParent().toString(), path.getFileName().toString(), remotePath, isMonitor);
		}
		LOG.info("Sftp批量文件上传--->本地目录[{}], 文件数量[{}], 远程目录[{}]--->上传成功！", localPath, localList.size(), remotePath);
	}

	/**
	 * SFTP下载文件
	 * 
	 * @param remotePath
	 *            远程目录
	 * @param fileName
	 *            文件名称
	 * @param localPath
	 *            本地目录
	 * @param isMonitor
	 *            是否记录文件传输进度
	 * @throws Exception
	 */
	public void downFile(String remotePath, String fileName, String localPath, boolean isMonitor) throws Exception {
		LOG.info("Sftp文件下载--->远程目录[{}], 文件名称[{}], 本地目录[{}]--->开始处理！", remotePath, fileName, localPath);
		String fileRemotePath = FileUtil.getAbsolutePath(remotePath) + fileName;
		long fileSize = getFileSize(fileRemotePath);
		String fileLocalPath = FileUtil.getAbsolutePath(localPath) + fileName;
		try {
			FileUtil.getPath4WriteFile(fileLocalPath);
		} catch (Exception e) {
			LOG.error("Sftp文件下载 异常:", e);
			throw new Exception("Sftp文件下载 异常：本地目录[" + localPath + "] 创建失败！");
		}
		if (!isMonitor) {
			this.sftp.get(fileRemotePath, fileLocalPath);
		} else {
			this.sftp.get(fileRemotePath, fileLocalPath, new SftpFile4ProgressMonitor(fileSize), ChannelSftp.OVERWRITE);
		}
		LOG.info("Sftp文件下载--->远程目录[{}], 文件名称[{}], 本地目录[{}]--->处理成功！", remotePath, fileName, localPath);
	}

	/**
	 * Sftp文件下载到本地流
	 * 
	 * @param remotePath
	 *            远程目录
	 * @param fileName
	 *            文件名称
	 * @return
	 * @throws Exception
	 */
	public InputStream downFile(String remotePath, String fileName) throws Exception {
		LOG.info("Sftp文件下载到本地流--->远程目录[{}], 文件名称[{}]--->开始处理！", remotePath, fileName);
		String fileRemotePath = FileUtil.getAbsolutePath(remotePath) + fileName;
		if (!fileIsExist(fileRemotePath)) {
			throw new Exception("Sftp文件下载到本地流 异常：远程文件[" + fileRemotePath + "] 不存在！");
		}
		InputStream ins = this.sftp.get(fileRemotePath);
		LOG.info("Sftp文件下载到本地流--->远程目录[{}], 文件名称[{}]--->处理成功！", remotePath, fileName);
		return ins;
	}

	/**
	 * 批量下载文件
	 * 
	 * @param remotePath
	 *            远程目录
	 * @param localPath
	 *            本地目录
	 * @param isMonitor
	 *            是否记录文件传输进度
	 * @throws Exception
	 */
	public void downBatchFiles(String remotePath, String localPath, boolean isMonitor) throws Exception {
		LOG.info("Sftp批量下载文件--->远程目录[{}], 本地目录[{}]--->开始处理！", remotePath, localPath);
		if (!dirIsExist(remotePath)) {
			throw new Exception("Sftp批量下载文件 异常：远程目录[" + remotePath + "] 不存在！");
		}
		LinkedList<SftpLsEntry> list = null;
		try {
			list = getAllSftpLsEntry4Dir(remotePath);
		} catch (Exception e) {
			String msg = "Sftp批量下载文件 异常：远程目录[" + remotePath + "] 遍历文件时出现异常！";
			LOG.error(msg, e);
			throw new Exception(msg);
		}
		if (null == list || list.isEmpty()) {
			throw new Exception("Sftp批量下载文件 异常：远程目录[" + remotePath + "] 不存在可下载的文件！");
		}
		try {
			FileUtil.getPath4WriteFile4Dir(localPath);
		} catch (Exception e) {
			LOG.error("Sftp批量下载文件 异常:", e);
			throw new Exception("Sftp批量下载文件 异常：本地目录[" + localPath + "] 创建失败！");
		}
		for (SftpLsEntry sftpLsEntry : list) {
			String fileName = sftpLsEntry.getFileName();
			String fileRemotePath = FileUtil.getAbsolutePath(sftpLsEntry.getPath()) + fileName;
			String fileLocalPath = FileUtil.getAbsolutePath(localPath) + fileName;
			if (!isMonitor) {
				this.sftp.get(fileRemotePath, fileLocalPath);
			} else {
				this.sftp.get(fileRemotePath, fileLocalPath, new SftpFile4ProgressMonitor(sftpLsEntry.getFileSize()),
								ChannelSftp.OVERWRITE);
			}
		}
		LOG.info("Sftp批量下载文件--->远程目录[{}], 本地目录[{}], 文件数量[{}]--->处理成功！", remotePath, localPath, list.size());
	}

	/**
	 * 遍历目录下所有文件(含子目录)
	 * 
	 * @param remotePath
	 *            远程目录
	 * @return
	 * @throws Exception
	 */
	public LinkedList<SftpLsEntry> getAllSftpLsEntry4Dir(String remotePath) throws Exception {
		Path path = Paths.get(remotePath);
		SftpLsEntry sftpLsEntry = new SftpLsEntry(path.getFileName().toString(), path.getParent().toString(), true, 0l);
		LinkedList<SftpLsEntry> dirList = new LinkedList<>(); // 保存待遍历文件夹的列表
		LinkedList<SftpLsEntry> pathList = new LinkedList<>(); // 文件列表
		// 调用遍历文件夹根目录文件的方法
		getSftpLsEntry4Dir(sftpLsEntry, dirList, pathList);
		SftpLsEntry temp = null;
		while (!dirList.isEmpty()) {
			temp = (SftpLsEntry) dirList.removeFirst();
			getSftpLsEntry4Dir(temp, dirList, pathList);
		}
		return pathList;
	}

	/**
	 * 遍历目录
	 * 
	 * @param sftpLsEntry
	 *            待处理目录Bean
	 * @param dirList
	 *            子目录List
	 * @param pathList
	 *            文件List
	 * @throws Exception
	 */
	private void getSftpLsEntry4Dir(SftpLsEntry sftpLsEntry, LinkedList<SftpLsEntry> dirList, LinkedList<SftpLsEntry> pathList)
					throws Exception {
		if (null == sftpLsEntry || !sftpLsEntry.isDir) {
			return;
		}
		String path = FileUtil.getAbsolutePath(sftpLsEntry.getPath()) + sftpLsEntry.getFileName();
		Vector<LsEntry> sftpFiles = listFiles(path);
		Iterator<LsEntry> it = sftpFiles.iterator();
		while (it.hasNext()) {
			LsEntry lsEntry = it.next();
			String fileName = lsEntry.getFilename().trim();
			if (fileName.equals(".") || fileName.equals("..")) {
				continue;// 忽略特殊文件名
			}
			SftpATTRS attr = lsEntry.getAttrs();
			SftpLsEntry newLsEntry = new SftpLsEntry(fileName, path, true, attr.getSize());
			if (attr.isDir()) {
				dirList.add(newLsEntry);
			} else if (attr.isReg()) {
				newLsEntry.setDir(false);
				pathList.add(newLsEntry);
			}
		}
	}

	/**
	 * 
	 * 获取远程目录下的文件名称列表（含文件夹）
	 * 
	 * @param directory
	 *            目录
	 * @return
	 * @throws Exception
	 */
	public List<String> getList4FileNamesByDir(String directory) throws Exception {
		Vector<LsEntry> sftpFile = listFiles(directory);
		List<String> nameList = new ArrayList<>();
		sftpFile.forEach(lsEntry -> {
			String fileName = lsEntry.getFilename() == null ? "" : lsEntry.getFilename().trim();
			if (fileName.equals("") || fileName.equals(".") || fileName.equals("..")) {
				// 特殊文件名，忽略
			} else {
				nameList.add(fileName);
			}
		});
		return nameList;
	}

	/**
	 * 
	 * 获取远程目录下的文件列表
	 * 
	 * @param directory
	 *            目录
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Vector<LsEntry> listFiles(String directory) throws Exception {
		try {
			Vector<LsEntry> vector = this.sftp.ls(directory);
			return vector;
		} catch (SftpException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param directory
	 *            要删除文件所在目录
	 * @param deleteFile
	 *            要删除的文件
	 * @return
	 * @throws SftpException
	 */
	public boolean deleteFile(String directory, String deleteFile) throws SftpException {
		this.sftp.cd(directory);
		this.sftp.rm(deleteFile);
		return true;
	}

	/**
	 * 更改文件名
	 * 
	 * @param directory
	 *            文件所在目录
	 * @param oldFileNm
	 *            原文件名
	 * @param newFileNm
	 *            新文件名
	 * 
	 * @throws Exception
	 */
	public void rename(String directory, String oldFileNm, String newFileNm) throws Exception {
		this.sftp.cd(directory);
		this.sftp.rename(oldFileNm, newFileNm);
	}

	public class SftpLsEntry {
		private String fileName; // 文件名称
		private String path;// 文件父级路径
		private boolean isDir; // 是否为目录
		private long fileSize;// 文件大小

		public SftpLsEntry(String fileName, String path, boolean isDir, long fileSize) {
			super();
			this.fileName = fileName;
			this.path = path;
			this.isDir = isDir;
			this.fileSize = fileSize;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public boolean isDir() {
			return isDir;
		}

		public void setDir(boolean isDir) {
			this.isDir = isDir;
		}

		public long getFileSize() {
			return fileSize;
		}

		public void setFileSize(long fileSize) {
			this.fileSize = fileSize;
		}

		@Override
		public String toString() {
			return "SftpLsEntry [fileName=" + fileName + ", path=" + path + ", isDir=" + isDir + ", fileSize=" + fileSize + "]";
		}

	}

}
