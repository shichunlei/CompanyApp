package com.cells.companyapp.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.os.Environment;
import android.util.Log;

/**
 * 文件操作辅助类
 * 
 * @author 师春雷
 * 
 */
public class FileUtils {

	private static String sdCardFoldersPath = "/";
	// 获取SD卡对应存储目录
	private static File sdCardDir = Environment.getExternalStorageDirectory();

	private static String sdCardPath = sdCardDir.getAbsolutePath() + sdCardFoldersPath;

	/**
	 * 在SD卡下创建文件夹
	 * 
	 * @return
	 */
	public static File createFolders(String folder) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File cellsFolder = new File(sdCardDir, folder);
			if (cellsFolder.exists())
				return cellsFolder;
			if (cellsFolder.mkdirs())
				return cellsFolder;
			return sdCardDir;
		} else {
			return null;
		}
	}

	/**
	 * 读取SDCard 文件内容
	 * 
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("resource")
	public static String read(String fileName) {

		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

				// 获取指定文件对应的输出流
				FileInputStream fis = new FileInputStream(sdCardPath + fileName);
				// 将指定输入流包装成 BufferedReader
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				StringBuilder sb = new StringBuilder("");
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 在 SDCard 写入文件
	 * 
	 * @param content
	 * @param fileName
	 * @return
	 */
	public static String write(String content, String fileName) {
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

				String sPath = sdCardPath + fileName;
				File file = new File(sPath);
				// 判断文件是否存在
				if (!file.exists()) { // 不存在返回 false,创建文件
					File targetDir = new File(sPath);
					// 以指定文件创建对象 RandomAccessFile
					RandomAccessFile raf = new RandomAccessFile(targetDir, "rw");
					// 输出文件内容
					raf.write(content.getBytes());
					raf.close();
				} else {// 存在的话，先删除之前的文件，然后重新创建一个同名文件
					// 如果文件存在则删除文件
					DeleteFile(sdCardPath + fileName);
					// 重新创建一个文件
					File targetDir = new File(sdCardPath + fileName);
					// 以指定文件创建对象 RandomAccessFile
					RandomAccessFile raf = new RandomAccessFile(targetDir, "rw");
					// 输出文件内容
					raf.write(content.getBytes());
					raf.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 修改文件名称
	 * 
	 * @param fromPathName
	 *            带路径的文件名
	 * @param toPathName
	 *            被修改的文件名（带路径）
	 */
	public static void renameToFile(String fromPathName, String toPathName) {
		File from = new File(fromPathName);
		File to = new File(toPathName);
		if (from.renameTo(to)) {
			Log.i("--------", "修改成功!");
		} else {
			Log.i("--------", "修改失败!");
		}
	}

	/***
	 * 删除本地文件
	 * 
	 * @param fileName
	 */
	public static void DeleteFile(String fileName) {
		File file = new File(fileName);
		// 判断目录或文件是否存在
		if (file.exists() && file.isFile()) { // 为文件且不为空则进行删除
			file.delete();
		}
	}

	/**
	 * 验证文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean fileExists(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 解压ZIP文件
	 * 
	 * @param folder
	 *            解压文件输出路径
	 * @param zipfile
	 *            原始ZIP压缩包（带全路径）
	 * @throws IOException
	 */
	public static boolean ZipInputStreamTest(String folder, String zipfile) throws IOException {
		long startTime = System.currentTimeMillis();

		ZipInputStream Zin = new ZipInputStream(new FileInputStream(zipfile));// 输入源zip路径
		BufferedInputStream Bin = new BufferedInputStream(Zin);
		String Parent = folder; // 输出路径（文件夹目录）
		File Fout = null;
		ZipEntry entry;

		while ((entry = Zin.getNextEntry()) != null && !entry.isDirectory()) {
			Fout = new File(Parent, entry.getName());
			if (!Fout.exists()) {
				(new File(Fout.getParent())).mkdirs();
			}
			FileOutputStream out = new FileOutputStream(Fout);
			BufferedOutputStream Bout = new BufferedOutputStream(out);
			int b;
			while ((b = Bin.read()) != -1) {
				Bout.write(b);
			}
			Bout.close();
			out.close();
			System.out.println("解压成功！");
		}
		Bin.close();
		Zin.close();

		long endTime = System.currentTimeMillis();
		System.out.println("耗费时间： " + (endTime - startTime) + " ms");
		return true;
	}
}
