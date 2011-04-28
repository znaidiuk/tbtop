package com.taobao.top.autosdk.mapper;

/**
 * PHP语言类型映射器。
 * 
 * @author fengsheng
 */
public class PhpTypeMapper implements TypeMapper {

	public String getDomainLangType(String apiType) {
		return apiType;
	}

	public String getRequestLangType(String apiType) {
		return apiType;
	}

	public String getResponseLangType(String apiType) {
		return apiType;
	}

}
