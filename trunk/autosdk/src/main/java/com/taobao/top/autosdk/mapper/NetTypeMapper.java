package com.taobao.top.autosdk.mapper;


/**
 * .NET语言类型映射器。
 * 
 * @author fengsheng
 */
public class NetTypeMapper implements TypeMapper {

	public String getDomainLangType(String apiType) {
		String langType = apiType;
		if ("Number".equals(apiType)) {
			langType = "long";
		} else if ("Date".equals(apiType)) {
			langType = "string";
		} else if ("Boolean".equals(apiType)) {
			langType = "bool";
		} else if ("String".equals(apiType)) {
			langType = "string";
		} else if ("Field List".equals(apiType)) {
			langType = "string";
		} else if ("byte[]".equals(apiType)) {
			langType = "FileItem";
		} else if ("Price".equals(apiType)) {
			langType = "string";
		} else if ("".equals(apiType) || null == apiType) {
			langType = "string";
		}
		return langType;
	}

	public String getRequestLangType(String apiType) {
		String langType = apiType;
		if ("Number".equals(apiType)) {
			langType = "Nullable<long>";
		} else if ("Date".equals(apiType)) {
			langType = "Nullable<DateTime>";
		} else if ("Boolean".equals(apiType)) {
			langType = "Nullable<bool>";
		} else if ("byte[]".equals(apiType)) {
			langType = "FileItem";
		} else if ("String".equals(apiType)) {
			langType = "string";
		} else if ("Field List".equals(apiType)) {
			langType = "string";
		} else if ("Price".equals(apiType)) {
			langType = "string";
		}
		return langType;
	}

	public String getResponseLangType(String apiType) {
		return getDomainLangType(apiType);
	}

}
