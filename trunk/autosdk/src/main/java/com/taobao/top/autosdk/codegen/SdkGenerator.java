package com.taobao.top.autosdk.codegen;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.taobao.top.autosdk.SdkConstants;
import com.taobao.top.autosdk.compile.SdkCompiler;
import com.taobao.top.autosdk.domain.ApiDomain;
import com.taobao.top.autosdk.domain.ApiRequest;
import com.taobao.top.autosdk.domain.ApiResponse;
import com.taobao.top.autosdk.parser.SdkParser;
import com.taobao.top.autosdk.util.ZipUtils;

/**
 * SDK生成器抽象类。
 *
 * @author fengsheng
 */
public abstract class SdkGenerator {

	protected File sources;

	private SdkParser parser;
	private SdkCompiler compiler;

	private List<ApiDomain> domains;
	private List<ApiRequest> requests;
	private List<ApiResponse> responses;

	public SdkGenerator(SdkParser parser, SdkCompiler compiler) throws Exception {
		this.parser = parser;
		this.compiler = compiler;
	}

	private void init() throws Exception {
		// parse meta data
		this.domains = parser.getApiDomains();
		this.requests = parser.getApiRequests();
		this.responses = parser.getApiResponses();

		// unzip basic sources
		String basic = getSdkBasicSourcePath();
		if (StringUtils.isNotBlank(basic)) {
			File zipFile = new File(this.getClass().getResource(basic).getFile());
			ZipUtils.unzip(zipFile, sources);
		}
	}

	public void generate(File target) throws Exception {
		this.sources = new File(target, SdkConstants.SOURCES_DIR);
		this.init();
		this.process();
		if (compiler != null) {
			File classes = new File(target, SdkConstants.CLASSES_DIR);
			compiler.compile(sources, classes);
			compiler.pack(target, target);
		}
	}

	protected void process() throws Exception {
		generateProjectFile();
		generateDomains();
		generateRequests();
		generateResponses();
		generateSdkVersion();
	}

	protected void generateProjectFile() throws Exception {
		String data = getProjectSourceCode(domains, requests, responses);
		if (data != null) {
			File outFile = new File(sources, getProjectSourcePath());
			FileUtils.writeStringToFile(outFile, data, SdkConstants.CHARSET_UTF8);
		}
	}

	protected void generateDomains() throws Exception {
		for (ApiDomain domain : domains) {
			String sourceCode = getDomainSourceCode(domain);
			String templatePath = getDomainSourcePath(domain);
			File outFile = new File(sources, templatePath);
			FileUtils.writeStringToFile(outFile, sourceCode, SdkConstants.CHARSET_UTF8);
		}
	}

	protected void generateRequests() throws Exception {
		for (ApiRequest request : requests) {
			String sourceCode = getRequestSourceCode(request);
			String templatePath = getRequestSourcePath(request);
			File outFile = new File(sources, templatePath);
			FileUtils.writeStringToFile(outFile, sourceCode, SdkConstants.CHARSET_UTF8);
		}
	}

	protected void generateResponses() throws Exception {
		for (ApiResponse response : responses) {
			String sourceCode = getResponseSourceCode(response);
			String templatePath = getResponseSourcePath(response);
			File outFile = new File(sources, templatePath);
			FileUtils.writeStringToFile(outFile, sourceCode, SdkConstants.CHARSET_UTF8);
		}
	}

	protected void generateSdkVersion() throws Exception {
		File outFile = new File(sources, getSdkVersionFilePath());
		String oldStr = FileUtils.readFileToString(outFile, SdkConstants.CHARSET_UTF8);
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String newStr = oldStr.replace("dynamicVersionNo", df.format(new Date()));
		FileUtils.writeStringToFile(outFile, newStr, SdkConstants.CHARSET_UTF8);
	}

	/**
	 * 项目工程文件源码，用于生成IDE工程文件。
	 */
	abstract protected String getProjectSourceCode(List<ApiDomain> domains, List<ApiRequest> requests, List<ApiResponse> responses) throws Exception;
	abstract protected String getProjectSourcePath();

	/**
	 * 生成数据结构源码。
	 */
	abstract protected String getDomainSourceCode(ApiDomain domain) throws Exception;
	abstract protected String getDomainSourcePath(ApiDomain domain) throws Exception;

	/**
	 * 生成API请求源码。
	 */
	abstract protected String getRequestSourceCode(ApiRequest request) throws Exception;
	abstract protected String getRequestSourcePath(ApiRequest request) throws Exception;

	/**
	 * 生成API响应源码。
	 */
	abstract protected String getResponseSourceCode(ApiResponse response) throws Exception;
	abstract protected String getResponseSourcePath(ApiResponse response) throws Exception;

	/**
	 * SDK版本文件，用于替换版本号。
	 */
	abstract protected String getSdkVersionFilePath() throws Exception;

	/**
	 * SDK基础框架源文件所在路径。
	 */
	abstract protected String getSdkBasicSourcePath() throws Exception;

}
