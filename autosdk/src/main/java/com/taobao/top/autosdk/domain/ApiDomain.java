package com.taobao.top.autosdk.domain;

import com.taobao.top.autosdk.util.StringKit;

/**
 * API数据结构。
 * 
 * @author fengsheng
 */
public class ApiDomain extends BaseObject {

	private boolean inUsed; // 数据结构是否被引用

	public boolean isInUsed() {
		return this.inUsed;
	}

	public void setInUsed(boolean inUsed) {
		this.inUsed = inUsed;
	}

	public String getClassName() {
		return StringKit.capitalize(StringKit.toCamelCase(this.getName()));
	}

}
