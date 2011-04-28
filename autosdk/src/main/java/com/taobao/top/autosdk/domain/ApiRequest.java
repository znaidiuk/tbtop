package com.taobao.top.autosdk.domain;

import com.taobao.top.autosdk.util.StringKit;

/**
 * API请求。
 * 
 * @author fengsheng
 */
public class ApiRequest extends BaseObject {

	/**
	 * 获得请求类名称
	 */
	public String getRequestClassName() {
		return StringKit.capitalize(StringKit.toCamelCase(this.getName().substring(7))) + "Request";
	}

	/**
	 * 获得响应类名称
	 */
	public String getResponseClassName() {
		return StringKit.capitalize(StringKit.toCamelCase(this.getName().substring(7))) + "Response";
	}

	/**
	 * 判断是否是带有文件上传的接口
	 */
	public Boolean isUploadRequest() {
		for (ApiField field : getFields()) {
			if ("byte[]".equals(field.getApiType())) {
				return true;
			}
		}
		return false;
	}

}
