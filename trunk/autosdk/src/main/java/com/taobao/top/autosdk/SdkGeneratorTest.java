package com.taobao.top.autosdk;

import java.io.File;

import com.taobao.top.autosdk.codegen.JavaSdkGenerator;
import com.taobao.top.autosdk.codegen.NetSdkGenerator;
import com.taobao.top.autosdk.codegen.PhpSdkGenerator;
import com.taobao.top.autosdk.codegen.SdkGenerator;
import com.taobao.top.autosdk.compile.JavaSdkCompiler;
import com.taobao.top.autosdk.compile.NetSdkCompiler;
import com.taobao.top.autosdk.mapper.JavaTypeMapper;
import com.taobao.top.autosdk.mapper.NetTypeMapper;
import com.taobao.top.autosdk.mapper.PhpTypeMapper;
import com.taobao.top.autosdk.parser.MetaSdkParser;

/**
 * SDK生成器。
 * 
 * @author fengsheng
 */
public class SdkGeneratorTest {

	public static void main(String[] args) throws Exception {
		createJavaSdk();
		createNetSdk();
		createPhpSdk();
	}

	private static void createJavaSdk() throws Exception {
		File target = new File("E:/Downloads/Sdk/Java");
		SdkGenerator sg = new JavaSdkGenerator(new MetaSdkParser(new JavaTypeMapper()), new JavaSdkCompiler());
		sg.generate(target);
	}

	private static void createNetSdk() throws Exception {
		File target = new File("E:/Downloads/Sdk/Net");
		SdkGenerator sg = new NetSdkGenerator(new MetaSdkParser(new NetTypeMapper()), new NetSdkCompiler());
		sg.generate(target);
	}

	private static void createPhpSdk() throws Exception {
		File target = new File("E:/Downloads/Sdk/Php");
		SdkGenerator sg = new PhpSdkGenerator(new MetaSdkParser(new PhpTypeMapper()), null);
		sg.generate(target);
	}

}
