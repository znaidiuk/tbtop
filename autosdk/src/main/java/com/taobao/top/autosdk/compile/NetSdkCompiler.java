package com.taobao.top.autosdk.compile;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.taobao.top.autosdk.SdkConstants;
import com.taobao.top.autosdk.util.ZipUtils;

public class NetSdkCompiler implements SdkCompiler {

	private String dotNetFxHome = "C:/Windows/Microsoft.NET/Framework/v4.0.30319/";

	/**
	 * 设置Microsoft .NET Framework 3.5或4.0根目录。默认为：
	 * C:/Windows/Microsoft.NET/Framework/v4.0.30319/
	 */
	public void setDotNetFxHome(String dotNetFxHome) {
		this.dotNetFxHome = dotNetFxHome;
	}

	public void compile(File srcDir, File destDir) throws Exception {
		File prj = new File(srcDir, "TopSdk.csproj");
		String cmd = "cmd /c msbuild " + prj.getAbsolutePath() + " /p:Configuration=Release";

		Runtime rt = Runtime.getRuntime();
		Process prc = rt.exec(cmd, null, new File(dotNetFxHome));
		String info = IOUtils.toString(prc.getInputStream());
		int success = prc.exitValue();
		if (success == 1) {
			throw new RuntimeException(info);
		}
	}

	public void pack(File binDir, File destDir) throws Exception {
		DateFormat df = new SimpleDateFormat(SdkConstants.DATE_FORMAT_SHORT);
		String date = df.format(new Date());

		// generate net source code ZIP
		File srcZip = new File(destDir, "taobao-sdk-net-" + date + "-source.zip");
		File srcDir = new File(binDir, SdkConstants.SOURCES_DIR);
		ZipUtils.zip(srcDir, srcZip);

		// copy net binary DLL to target
		File srcDll = new File(binDir, SdkConstants.SOURCES_DIR + "/bin/Release/TopSdk.dll");
		File dstDll = new File(destDir, "TopSdk.dll");
		FileUtils.copyFile(srcDll, dstDll);
	}

}
