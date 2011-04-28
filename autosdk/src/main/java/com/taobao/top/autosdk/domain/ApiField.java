package com.taobao.top.autosdk.domain;

import com.taobao.top.autosdk.SdkConstants;
import com.taobao.top.autosdk.util.StringKit;

/**
 * API属性。
 * 
 * @author fengsheng
 */
public class ApiField extends NameObject {

	/**
	 * 属性语言类型名称（针对不同的语言）
	 */
	private String langType;

	/**
	 * 属性API类型名称（所有语言一样）
	 */
	private String apiType;

	/**
	 * 属性API类型级别：Basic, Basic Array, Object, Object Array
	 */
	private String apiLevel;

	public String getLangType() {
		return this.langType;
	}

	public void setLangType(String langType) {
		this.langType = langType;
	}

	/**
	 * 获取定义类型：属性定义，方法定义，返回值。
	 * 
	 * @param prefix 列表类型的前缀
	 * @param suffix 列表类型的后缀
	 */
	public String getDefineLangType(String prefix, String suffix) {
		StringBuilder sb = new StringBuilder();
		if (prefix != null && isListField()) {
			sb.append(prefix);
		}
		sb.append(this.langType);
		if (suffix != null && isListField()) {
			sb.append(suffix);
		}
		return sb.toString();
	}

	public String getApiType() {
		return this.apiType;
	}

	public void setApiType(String apiType) {
		this.apiType = apiType;
	}

	public String getApiLevel() {
		return this.apiLevel;
	}

	public void setApiLevel(String apiLevel) {
		this.apiLevel = apiLevel;
	}

	/**
	 * 获取驼峰风格名称。
	 */
	public String getCamelCaseName() {
		return StringKit.toCamelCase(this.getName());
	}

	/**
	 * 获取帕斯卡风格名称。
	 */
	public String getPascalCaseName() {
		return StringKit.toPascalCase(this.getName());
	}

	/**
	 * 获得属性的set方法名称
	 */
	public String getSetMethodName() {
		return "set" + getMethodName();
	}

	/**
	 * 获得属性的get方法名称
	 */
	public String getGetMethodName() {
		return "get" + getMethodName();
	}

	/**
	 * 获得属性的MethodName
	 */
	private String getMethodName() {
		return StringKit.getSetMethod(this.getCamelCaseName());
	}

	/**
	 * 判断属性是否是列表类型
	 */
	public boolean isListField() {
		return SdkConstants.TYPE_BASIC_ARRAY.equals(this.apiLevel) || SdkConstants.TYPE_OBJECT_ARRAY.equals(this.apiLevel);
	}

	/**
	 * 判断属性是否是复杂类型
	 */
	public boolean isObjectField() {
		return SdkConstants.TYPE_OBJECT.equals(this.apiLevel) || SdkConstants.TYPE_OBJECT_ARRAY.equals(this.apiLevel);
	}

}
