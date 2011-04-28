package com.taobao.top.autosdk.compile;

import java.io.File;

/**
 * SDK编译器。
 * 
 * @author fengsheng
 */
public interface SdkCompiler {

	/**
	 * 执行编译。
	 * 
	 * @param srcDir 源代码所在目录
	 * @param destDir 字节码生成目录
	 */
	public void compile(File srcDir, File destDir) throws Exception;

	/**
	 * 打包发布。
	 * 
	 * @param binDir 源代码和字节码目录（要求此目录下包含sources和classes目录）
	 * @param destDir 压缩包生成目录
	 */
	public void pack(File binDir, File destDir) throws Exception;

}
