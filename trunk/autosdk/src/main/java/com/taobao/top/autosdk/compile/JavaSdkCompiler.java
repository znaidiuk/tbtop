package com.taobao.top.autosdk.compile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.jar.Manifest;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.taobao.top.autosdk.SdkConstants;
import com.taobao.top.autosdk.SdkException;
import com.taobao.top.autosdk.util.ZipUtils;

/**
 * JAVA SDK编译器。
 * 
 * @author fengsheng
 */
public class JavaSdkCompiler implements SdkCompiler {

	public void compile(File srcDir, File destDir) throws Exception {
		if (!srcDir.isDirectory()) {
			throw new SdkException("Parameter 'srcDir' is not a directory!");
		}

		@SuppressWarnings("unchecked")
		Collection<File> files = FileUtils.listFiles(srcDir, new SuffixFileFilter(new String[] { "java" }), TrueFileFilter.INSTANCE);

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			throw new RuntimeException("JDK not found! Please check your system environment!");
		}

		StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> jfObjs = manager.getJavaFileObjectsFromFiles(files);

		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		List<String> opts = new ArrayList<String>();
		opts.add("-g");
		opts.add("-encoding");
		opts.add("utf-8");
		opts.add("-target");
		opts.add("1.5");
		opts.add("-nowarn");
		opts.add("-d");
		opts.add(destDir.getAbsolutePath());

		if (Thread.currentThread().getContextClassLoader() instanceof URLClassLoader) {
			opts.add("-cp");
			StringBuilder sb = new StringBuilder();
			URLClassLoader ucl = (URLClassLoader) Thread.currentThread().getContextClassLoader();
			for (URL url : ucl.getURLs()) {
				String filePath = URLDecoder.decode(url.getFile(), SdkConstants.CHARSET_UTF8);
				sb.append(filePath).append(File.pathSeparator);
			}
			opts.add(sb.toString());
		}

		StringWriter writer = new StringWriter();
		CompilationTask task = compiler.getTask(writer, manager, null, opts, null, jfObjs);
		if (!task.call()) {
			throw new SdkException("SDK Compile Error:\r\n" + writer.toString());
		}
	}

	public void pack(File binDir, File destDir) throws IOException {
		InputStream input = JavaSdkCompiler.class.getResourceAsStream("/SDK_MANIFEST.MF");
		DateFormat df = new SimpleDateFormat(SdkConstants.DATE_FORMAT_SHORT);
		String date = df.format(new Date());

		// generate java source code jar
		File srcJar = new File(destDir, "taobao-sdk-java-" + date + "-source.jar");
		File srcDir = new File(binDir, SdkConstants.SOURCES_DIR);
		ZipUtils.jar(srcDir, srcJar, null);

		// generate java byte code jar
		String mfstr = IOUtils.toString(input, SdkConstants.CHARSET_UTF8);
		mfstr = MessageFormat.format(mfstr, date);
		ByteArrayInputStream bais = new ByteArrayInputStream(mfstr.getBytes(SdkConstants.CHARSET_UTF8));
		Manifest manifest = new Manifest(bais);
		File clsJar = new File(destDir, "taobao-sdk-java-" + date + ".jar");
		File clsDir = new File(binDir, SdkConstants.CLASSES_DIR);
		ZipUtils.jar(clsDir, clsJar, manifest);
	}

}
