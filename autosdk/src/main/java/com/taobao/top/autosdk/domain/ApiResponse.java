package com.taobao.top.autosdk.domain;

import com.taobao.top.autosdk.util.StringKit;

/**
 * API响应。
 * 
 * @author fengsheng
 */
public class ApiResponse extends BaseObject {

	/**
	 * 获得Response类名称
	 */
	public String getClassName() {
		return StringKit.capitalize(StringKit.toCamelCase(this.getName().substring(7))) + "Response";
	}

}
