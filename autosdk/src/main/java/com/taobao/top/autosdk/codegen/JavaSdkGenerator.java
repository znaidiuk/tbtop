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
 * JAVA SDK生成 器。
 * 
 * @author fengsheng
 */
public class JavaSdkGenerator extends SdkGenerator {

	public JavaSdkGenerator(SdkParser parser, SdkCompiler compiler) throws Exception {
		super(parser, compiler);
	}

	protected String getProjectSourcePath() {
		return null;
	}

	protected String getDomainSourcePath(ApiDomain domain) throws Exception {
		return "com/taobao/api/domain/" + domain.getClassName() + ".java";
	}

	protected String getRequestSourcePath(ApiRequest request) throws Exception {
		return "com/taobao/api/request/" + request.getRequestClassName() + ".java";
	}

	protected String getResponseSourcePath(ApiResponse response) throws Exception {
		return "com/taobao/api/response/" + response.getClassName() + ".java";
	}

	protected String getSdkVersionFilePath() throws Exception {
		return "com/taobao/api/Constants.java";
	}

	protected String getProjectSourceCode(List<ApiDomain> domains, List<ApiRequest> reqs, List<ApiResponse> rsps) throws Exception {
		return null;
	}

	protected String getDomainSourceCode(ApiDomain domain) throws Exception {
		// build fields
		StringBuilder fsb = new StringBuilder();
		for (ApiField field : domain.getFields()) {
			if (fsb.length() > 0) {
				fsb.append("\r\n\r\n\t");
			}

			fsb.append("/**\r\n\t * ");
			fsb.append(field.getDesc());
			fsb.append("\r\n\t */\r\n\t");

			if (field.isListField()) {
				fsb.append("@ApiListField(\"");
				fsb.append(field.getName());
				fsb.append("\")");
				fsb.append("\r\n\t@ApiField(\"");
				fsb.append(StringKit.toLowercaseWithUnderscore(field.getLangType()));
				fsb.append("\")");
			} else {
				fsb.append("@ApiField(\"");
				fsb.append(field.getName());
				fsb.append("\")");
			}

			fsb.append("\r\n\t");
			fsb.append("private ");
			fsb.append(field.getDefineLangType("List<", ">"));
			fsb.append(" ");
			fsb.append(field.getCamelCaseName());
			fsb.append(";");
		}

		// build methods
		StringBuilder msb = new StringBuilder();
		for (ApiField field : domain.getFields()) {
			if (msb.length() > 0) {
				msb.append("\r\n\r\n\t");
			}

			msb.append("public ");
			msb.append(field.getDefineLangType("List<", ">"));
			msb.append(" ");
			msb.append(field.getGetMethodName());
			msb.append("() {\r\n\t\treturn this.");
			msb.append(field.getCamelCaseName());
			msb.append(";\r\n\t}\r\n\t");

			msb.append("public void ");
			msb.append(field.getSetMethodName());
			msb.append("(");
			msb.append(field.getDefineLangType("List<", ">"));
			msb.append(" ");
			msb.append(field.getCamelCaseName());
			msb.append(") {\r\n\t\tthis.");
			msb.append(field.getCamelCaseName());
			msb.append(" = ");
			msb.append(field.getCamelCaseName());
			msb.append(";\r\n\t}");
		}

		// build imports
		StringBuilder isb = new StringBuilder();
		if (domain.isContainDateField()) {
			isb.append("import java.util.Date;\r\n");
		}
		if (domain.isContainListField()) {
			isb.append("import java.util.List;\r\n");
		}

		if (isb.length() > 0) {
			isb.append("\r\n");
		}
		isb.append("import com.taobao.api.TaobaoObject;");

		if (domain.isContainField()) {
			isb.append("\r\nimport com.taobao.api.internal.mapping.ApiField;");
		}
		if (domain.isContainListField()) {
			isb.append("\r\nimport com.taobao.api.internal.mapping.ApiListField;");
		}

		// build description
		StringBuilder dsb = new StringBuilder();
		dsb.append(domain.getDesc());

		String input = getResourceAsString("Domain.txt");
		String result = MessageFormat.format(input, domain.getClassName(), fsb.toString(), msb.toString(),
				isb.toString(), dsb.toString());

		return result;
	}

	protected String getRequestSourceCode(ApiRequest request) throws Exception {
		// build fields
		StringBuilder fsb = new StringBuilder();
		for (ApiField field : request.getFields()) {
			fsb.append("\r\n");
			fsb.append("\r\n\t/** \r\n\t* ");
			fsb.append(field.getDesc());
			fsb.append("\r\n\t */\r\n\t");
			fsb.append("private ");
			fsb.append(field.getLangType());
			fsb.append(" ");
			fsb.append(field.getCamelCaseName());
			fsb.append(";");
		}

		// build methods
		StringBuilder msb = new StringBuilder();
		for (ApiField field : request.getFields()) {
			msb.append("\r\n\r\n");
			if (msb.length() > 0) {
				msb.append("\t");
			}
			// set method
			msb.append("public void ");
			msb.append(field.getSetMethodName());
			msb.append("(");
			msb.append(field.getLangType());
			msb.append(" ");
			msb.append(field.getCamelCaseName());
			msb.append(") {\r\n\t\tthis.");
			msb.append(field.getCamelCaseName());
			msb.append(" = ");
			msb.append(field.getCamelCaseName());
			msb.append(";\r\n\t}");
			// get method
			msb.append("\r\n\tpublic ");
			msb.append(field.getLangType());
			msb.append(" ");
			msb.append(field.getGetMethodName());
			msb.append("() {\r\n\t\treturn this.");
			msb.append(field.getCamelCaseName());
			msb.append(";\r\n\t}");
		}

		// build text parameters
		StringBuilder tpsb = new StringBuilder();
		for (ApiField field : request.getFields()) {
			if ("FileItem".equals(field.getLangType())) {
				continue;
			}

			tpsb.append("\r\n\t\t");
			tpsb.append("txtParams.put(\"");
			tpsb.append(field.getName());
			tpsb.append("\", this.");
			tpsb.append(field.getCamelCaseName());
			tpsb.append(");");
		}

		// build file parameters
		StringBuilder fpsb = new StringBuilder();
		for (ApiField field : request.getFields()) {
			if (!"FileItem".equals(field.getLangType())) {
				continue;
			}
			fpsb.append("\r\n\t\t");
			fpsb.append("params.put(\"");
			fpsb.append(field.getName());
			fpsb.append("\", this.");
			fpsb.append(field.getCamelCaseName());
			fpsb.append(");");
		}

		// build imports
		StringBuilder isb = new StringBuilder();
		if (request.isContainDateField()) {
			isb.append("import java.util.Date;\r\n");
		}

		String result;
		if (request.isUploadRequest()) {
			String text = getResourceAsString("TopUploadRequest.txt");
			result = MessageFormat.format(text, request.getName(), request.getRequestClassName(), fsb.toString(),
					msb.toString(), tpsb.toString(), fpsb.toString(), isb.toString(), request.getResponseClassName());
		} else {
			String text = getResourceAsString("TopRequest.txt");
			result = MessageFormat.format(text, request.getName(), request.getRequestClassName(), fsb.toString(),
					msb.toString(), tpsb.toString(), isb.toString(), request.getResponseClassName());
		}
		return result;
	}

	protected String getResponseSourceCode(ApiResponse response) throws Exception {
		// build fields
		StringBuilder fsb = new StringBuilder();
		for (ApiField field : response.getFields()) {
			if (fsb.length() > 0) {
				fsb.append("\r\n\r\n\t");
			}

			fsb.append("/** \r\n\t * ");
			fsb.append(field.getDesc());
			fsb.append("\r\n\t */\r\n\t");

			if (field.isListField()) {
				fsb.append("@ApiListField(\"");
				fsb.append(field.getName());
				fsb.append("\")");
				fsb.append("\r\n\t@ApiField(\"");
				fsb.append(StringKit.toLowercaseWithUnderscore(field.getLangType()));
				fsb.append("\")");
			} else {
				fsb.append("@ApiField(\"");
				fsb.append(field.getName());
				fsb.append("\")");
			}

			fsb.append("\r\n\t");

			fsb.append("private ");
			fsb.append(field.getDefineLangType("List<", ">"));
			fsb.append(" ");
			fsb.append(field.getCamelCaseName());
			fsb.append(";");
		}

		// build methods
		StringBuilder msb = new StringBuilder();
		for (ApiField field : response.getFields()) {
			if (msb.length() > 0) {
				msb.append("\r\n\r\n\t");
			}
			msb.append("public void ");
			msb.append(field.getSetMethodName());
			msb.append("(");
			msb.append(field.getDefineLangType("List<", ">"));
			msb.append(" ");
			msb.append(field.getCamelCaseName());
			msb.append(") {\r\n\t\tthis.");
			msb.append(field.getCamelCaseName());
			msb.append(" = ");
			msb.append(field.getCamelCaseName());
			msb.append(";\r\n\t}\r\n\t");

			msb.append("public ");
			msb.append(field.getDefineLangType("List<", ">"));
			msb.append(" ");
			msb.append(field.getGetMethodName());
			msb.append("( ) {\r\n\t\treturn this.");
			msb.append(field.getCamelCaseName());
			msb.append(";\r\n\t}");
		}

		// build imports
		StringBuilder isb = new StringBuilder();
		if (response.isContainDateField())
			isb.append("import java.util.Date;\r\n");

		if (response.isContainListField())
			isb.append("import java.util.List;\r\n");

		if (response.isContainField())
			isb.append("import com.taobao.api.internal.mapping.ApiField;\r\n");

		if (response.isContainListField())
			isb.append("import com.taobao.api.internal.mapping.ApiListField;\r\n");

		for (int i = 0; response.getFields() != null && i < response.getFields().size(); i++) {
			ApiField field = response.getFields().get(i);
			if (field.isObjectField()) {
				isb.append("import com.taobao.api.domain.");
				isb.append(field.getLangType());
				isb.append(";\r\n");
			}
		}

		String text = getResourceAsString("TopResponse.txt");
		String result = MessageFormat.format(text, response.getName(), response.getClassName(), fsb.toString(),
				msb.toString(), isb.toString());

		return result;
	}

	private String getResourceAsString(String fileName) throws IOException {
		InputStream is = this.getClass().getResourceAsStream(SdkConstants.SDK_TEMPLATE_DIR + "/java/" + fileName);
		return IOUtils.toString(is, SdkConstants.CHARSET_UTF8);
	}

	protected String getSdkBasicSourcePath() throws Exception {
		return "/taobao-sdk-java-source.zip";
	}

}
