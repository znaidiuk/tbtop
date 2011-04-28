package com.taobao.top.autosdk.domain;

/**
 * 名字对象。
 * 
 * @author fengsheng
 */
public class NameObject {

	private String name;

	private String desc;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean equals(Object obj) {
		if (obj instanceof NameObject) {
			NameObject base = (NameObject) obj;
			return this.name.equalsIgnoreCase(base.name);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return name.hashCode();
	}

}
