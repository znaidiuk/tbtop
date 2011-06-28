package com.taobao.top.autosdk.mapper;

/**
 * JAVA语言类型映射器。
 * 
 * @author fengsheng
 */
public class JavaTypeMapper implements TypeMapper {

	public String getDomainLangType(String apiType) {
		String langType = apiType;
		if ("Number".equals(apiType)) {
			langType = "Long";
		} else if ("Field List".equals(apiType)) {
			langType = "String";
		} else if ("byte[]".equals(apiType)) {
			langType = "FileItem";
		} else if ("Price".equals(apiType)) {
			langType = "String";
		} else if ("".equals(apiType) || null == apiType) {
			langType = "String";
		}
		return langType;
	}

	public String getRequestLangType(String apiType) {
		return getDomainLangType(apiType);
	}

	public String getResponseLangType(String apiType) {
		return getDomainLangType(apiType);
	}

}
