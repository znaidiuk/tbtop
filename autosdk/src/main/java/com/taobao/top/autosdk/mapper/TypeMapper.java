package com.taobao.top.autosdk.mapper;

/**
 * 类型映射器。
 * 
 * @author fengsheng
 */
public interface TypeMapper {

	/**
	 * API类型转化为.NET数据结构类型。
	 */
	public String getDomainLangType(String apiType);

	/**
	 * API类型转化为.NET请求类型。
	 */
	public String getRequestLangType(String apiType);

	/**
	 * API类型转化为.NET响应类型。
	 */
	public String getResponseLangType(String apiType);

}
