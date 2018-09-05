package com.power.sql.generator.builder;

import java.io.*;

/**
 * 代码输出
 * 
 * @author sunyu
 *
 */
public class CodeOuter {

	/**
	 * 将字符串写入文件中
	 * 
	 * @param str
	 * @param file
	 * @return
	 */
	public static boolean writeFile(String str, File file) {
		boolean flag;
		BufferedWriter output = null;
		try {
			file.createNewFile();
			OutputStream out=new FileOutputStream(file);
			output = new BufferedWriter(new OutputStreamWriter(out,"utf-8"));
			output.write(str);
			flag = true;
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}






}
