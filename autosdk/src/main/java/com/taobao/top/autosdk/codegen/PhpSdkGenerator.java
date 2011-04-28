package com.taobao.top.autosdk.codegen;

import java.io.File;
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

/**
 * PHP SDK生成 器。
 * 
 * @author fengsheng
 */
public class PhpSdkGenerator extends SdkGenerator {

	public PhpSdkGenerator(SdkParser parser, SdkCompiler compiler) throws Exception {
		super(parser, compiler);
	}

	public void process() throws Exception {
		this.sources = new File(this.sources, "top");
		generateRequests();
		generateSdkVersion();
	}

	protected String getProjectSourcePath() {
		return null;
	}

	protected String getDomainSourcePath(ApiDomain domain) throws Exception {
		return null;
	}

	protected String getRequestSourcePath(ApiRequest request) throws Exception {
		return "request/" + request.getRequestClassName() + ".php";
	}

	protected String getResponseSourcePath(ApiResponse response) throws Exception {
		return null;
	}

	protected String getSdkVersionFilePath() throws Exception {
		return "TopClient.php";
	}

	protected String getProjectSourceCode(List<ApiDomain> domains, List<ApiRequest> reqs, List<ApiResponse> rsps)
			throws Exception {
		return null;
	}

	protected String getDomainSourceCode(ApiDomain domain) throws Exception {
		return null;
	}

	protected String getRequestSourceCode(ApiRequest request) throws Exception {
		// build fields
		StringBuilder fsb = new StringBuilder();
		for (ApiField field : request.getFields()) {
			if (fsb.length() > 0) {
				fsb.append("\r\n\t");
			}
			fsb.append("\r\n\t/** \r\n\t * ");
			fsb.append(field.getDesc());
			fsb.append("\r\n\t **/\r\n\t");
			fsb.append("private $");
			fsb.append(field.getCamelCaseName());
			fsb.append(";");
		}

		// build methods
		StringBuilder msb = new StringBuilder();
		for (ApiField field : request.getFields()) {
			// set method
			msb.append("\r\n\tpublic function ");
			msb.append(field.getSetMethodName());
			msb.append("($");
			msb.append(field.getCamelCaseName());
			msb.append(")\n\t{\r\n\t\t$this->");
			msb.append(field.getCamelCaseName());
			msb.append(" = $");
			msb.append(field.getCamelCaseName());
			msb.append(";\r\n\t\t$this->apiParas[\"");
			msb.append(field.getName());
			msb.append("\"] = $");
			msb.append(field.getCamelCaseName());
			msb.append(";\r\n\t}\n");
			// get method
			msb.append("\r\n\tpublic function ");
			msb.append(field.getGetMethodName());
			msb.append("()\n\t{\r\n\t\treturn $this->");
			msb.append(field.getCamelCaseName());
			msb.append(";\r\n\t}\n");
		}

		String text = getResourceAsString("TopRequest.txt");
		String result = MessageFormat.format(text, request.getName(), request.getRequestClassName(), fsb.toString(), msb.toString());
		return result;
	}

	protected String getResponseSourceCode(ApiResponse response) throws Exception {
		return null;
	}

	private String getResourceAsString(String fileName) throws IOException {
		InputStream is = this.getClass().getResourceAsStream(SdkConstants.SDK_TEMPLATE_DIR + "/php/" + fileName);
		return IOUtils.toString(is, SdkConstants.CHARSET_UTF8);
	}

	protected String getSdkBasicSourcePath() throws Exception {
		return "/taobao-sdk-php-source.zip";
	}

}
