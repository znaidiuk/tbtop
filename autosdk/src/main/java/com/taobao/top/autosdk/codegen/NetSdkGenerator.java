package com.taobao.top.autosdk.codegen;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.taobao.top.autosdk.SdkConstants;
import com.taobao.top.autosdk.compile.SdkCompiler;
import com.taobao.top.autosdk.domain.ApiDomain;
import com.taobao.top.autosdk.domain.ApiField;
import com.taobao.top.autosdk.domain.ApiRequest;
import com.taobao.top.autosdk.domain.ApiResponse;
import com.taobao.top.autosdk.parser.SdkParser;
import com.taobao.top.autosdk.util.StringKit;

/**
 * .NET SDK生成 器。
 * 
 * @author fengsheng
 */
public class NetSdkGenerator extends SdkGenerator {

	public NetSdkGenerator(SdkParser parser, SdkCompiler compiler) throws Exception {
		super(parser, compiler);
	}

	protected String getProjectSourcePath() {
		return "TopSdk.csproj";
	}

	protected String getDomainSourcePath(ApiDomain domain) throws Exception {
		return "Domain/" + domain.getClassName() + ".cs";
	}

	protected String getRequestSourcePath(ApiRequest request) throws Exception {
		return "Request/" + request.getRequestClassName() + ".cs";
	}

	protected String getResponseSourcePath(ApiResponse response) throws Exception {
		return "Response/" + response.getClassName() + ".cs";
	}

	protected String getSdkVersionFilePath() throws Exception {
		return "DefaultTopClient.cs";
	}

	protected String getProjectSourceCode(List<ApiDomain> domains, List<ApiRequest> reqs, List<ApiResponse> rsps) throws Exception {
		StringBuilder dsb = new StringBuilder();
		for (ApiDomain domain : domains) {
			dsb.append("\r\n    <Compile Include=\"Domain\\");
			dsb.append(domain.getClassName());
			dsb.append(".cs\" />");
		}

		StringBuilder rsb = new StringBuilder();
		for (ApiRequest req : reqs) {
			rsb.append("\r\n    <Compile Include=\"Request\\");
			rsb.append(req.getRequestClassName());
			rsb.append(".cs\" />");
		}
		for (ApiResponse rsp : rsps) {
			rsb.append("\r\n    <Compile Include=\"Response\\");
			rsb.append(rsp.getClassName());
			rsb.append(".cs\" />");
		}

		String input = getResourceAsString("TopSdk.csproj");
		String result = MessageFormat.format(input, dsb.toString(), rsb.toString());
		return result;
	}

	protected String getDomainSourceCode(ApiDomain domain) throws Exception {
		String className = domain.getClassName();
		// build properties
		StringBuilder psb = new StringBuilder();
		for (ApiField field : domain.getFields()) {
			if (psb.length() > 0) {
				psb.append("\r\n");
			}

			psb.append("\r\n        /// <summary>\r\n        /// ");
			psb.append((field.getDesc() + "").replaceAll("\\s", " "));
			psb.append("\r\n        /// </summary>\r\n        ");

			if (field.isListField()) {
				psb.append("[XmlArray(\"");
				psb.append(field.getName());
				psb.append("\")]");
				psb.append("\r\n        [XmlArrayItem(\"");
				psb.append(StringKit.toLowercaseWithUnderscore(field.getLangType()));
				psb.append("\")]");
			} else {
				psb.append("[XmlElement(\"");
				psb.append(field.getName());
				psb.append("\")]");
			}

			psb.append("\r\n        ");
			psb.append("public ");
			psb.append(field.getDefineLangType("List<", ">"));
			psb.append(" ");
			String fieldName = field.getPascalCaseName();
			if (className.equals(fieldName)) {
				psb.append(fieldName + "_");
			} else {
				psb.append(fieldName);
			}
			psb.append(" { get; set; }");
		}

		// build using
		StringBuilder usb = new StringBuilder("");
		if (domain.isContainListField()) {
			usb.append("using System.Collections.Generic;\r\n");
		}

		String input = getResourceAsString("Domain.txt");
		String result = MessageFormat.format(input, usb.toString(), domain.getClassName(), psb.toString());
		return result;
	}

	protected String getRequestSourceCode(ApiRequest request) throws Exception {
		// build properties
		StringBuilder fieldSb = new StringBuilder();
		for (ApiField field : request.getFields()) {
			fieldSb.append("\r\n");
			fieldSb.append("        /// <summary>\r\n        /// ");
			fieldSb.append((field.getDesc() + "").replaceAll("\\s", " "));
			fieldSb.append("\r\n        /// </summary>\r\n        ");
			fieldSb.append("public ");
			fieldSb.append(field.getLangType());
			fieldSb.append(" ");
			fieldSb.append(field.getPascalCaseName());
			fieldSb.append(" { get; set; }\r\n");
		}

		// build text parameters
		StringBuilder paramSb = new StringBuilder();
		for (ApiField field : request.getFields()) {
			if ("FileItem".equals(field.getLangType())) {
				continue;
			}
			paramSb.append("\r\n            ");
			paramSb.append("parameters.Add(\"");
			paramSb.append(field.getName());
			paramSb.append("\", this.");
			paramSb.append(field.getPascalCaseName());
			paramSb.append(");");
		}

		// build file parameters
		StringBuilder psb = new StringBuilder();
		for (ApiField field : request.getFields()) {
			if (!"FileItem".equals(field.getLangType())) {
				continue;
			}
			psb.append("\r\n            ");
			psb.append("parameters.Add(\"");
			psb.append(field.getName());
			psb.append("\", this.");
			psb.append(field.getPascalCaseName());
			psb.append(");");
		}

		// build using
		StringBuilder usb = new StringBuilder();

		String result;
		if (request.isUploadRequest()) {
			String text = getResourceAsString("TopUploadRequest.txt");
			result = MessageFormat.format(text, usb.toString(), request.getName(),
					request.getRequestClassName(), request.getResponseClassName(), fieldSb.toString(),
					paramSb.toString(), psb.toString());
		} else {
			String text = getResourceAsString("TopRequest.txt");
			result = MessageFormat.format(text, usb.toString(), request.getName(),
					request.getRequestClassName(), request.getResponseClassName(), fieldSb.toString(),
					paramSb.toString(), psb.toString(), psb.toString());
		}
		return result;
	}

	protected String getResponseSourceCode(ApiResponse response) throws Exception {
		// build properties
		StringBuilder psb = new StringBuilder();
		for (ApiField field : response.getFields()) {
			if (psb.length() > 0) {
				psb.append("\r\n");
			}

			psb.append("\r\n        /// <summary>\r\n        /// ");
			psb.append((field.getDesc() + "").replaceAll("\\s", " "));
			psb.append("\r\n        /// </summary>\r\n        ");

			if (field.isListField()) {
				psb.append("[XmlArray(\"");
				psb.append(field.getName());
				psb.append("\")]");
				psb.append("\r\n        [XmlArrayItem(\"");
				psb.append(StringKit.toLowercaseWithUnderscore(field.getLangType()));
				psb.append("\")]");
			} else {
				psb.append("[XmlElement(\"");
				psb.append(field.getName());
				psb.append("\")]");
			}

			psb.append("\r\n        ");
			psb.append("public ");
			psb.append(field.getDefineLangType("List<", ">"));
			psb.append(" ");
			String fieldName = field.getPascalCaseName();
			if (response.getClassName().equals(fieldName)) {
				psb.append(fieldName + "_");
			} else {
				psb.append(fieldName);
			}
			psb.append(" { get; set; }");
		}

		// build using
		StringBuilder usb = new StringBuilder("");
		if (response.isContainListField()) {
			usb.append("using System.Collections.Generic;\r\n");
		}
		if (response.isContainField()) {
			usb.append("using Top.Api.Domain;\r\n");
		}

		String input = getResourceAsString("TopResponse.txt");
		String result = MessageFormat.format(input, usb.toString(), response.getClassName(), psb.toString());
		return result;
	}

	private String getResourceAsString(String fileName) throws IOException {
		InputStream is = this.getClass().getResourceAsStream(SdkConstants.SDK_TEMPLATE_DIR + "/net/" + fileName);
		return IOUtils.toString(is, SdkConstants.CHARSET_UTF8);
	}

	protected String getSdkBasicSourcePath() throws Exception {
		return "/taobao-sdk-net-source.zip";
	}

}
