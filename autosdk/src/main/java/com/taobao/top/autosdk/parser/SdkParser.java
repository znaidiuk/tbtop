package com.taobao.top.autosdk.parser;

import java.util.List;

import com.taobao.top.autosdk.domain.ApiDomain;
import com.taobao.top.autosdk.domain.ApiRequest;
import com.taobao.top.autosdk.domain.ApiResponse;

/**
 * TOPAPI解释器。
 * 
 * @author fengsheng
 */
public interface SdkParser {

	/**
	 * 获得所有API实体对象
	 */
	public List<ApiDomain> getApiDomains() throws Exception;

	/**
	 * 获得API的Request对象
	 */
	public List<ApiRequest> getApiRequests() throws Exception;

	/**
	 * 获得API的Response对象
	 */
	public List<ApiResponse> getApiResponses() throws Exception;

}
