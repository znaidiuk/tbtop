package com.taobao.top.autosdk.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 基础对象。
 * 
 * @author fengsheng
 */
public class BaseObject extends NameObject {

	private List<ApiField> fields;
	private String lastModified; // 最后修改时间

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public void setFields(List<ApiField> fields) {
		this.fields = fields;
	}

	public List<ApiField> getFields() {
		if (this.fields == null) {
			this.fields = new ArrayList<ApiField>();
		}
		Collections.sort(this.fields, new Comparator<ApiField>() {
			public int compare(ApiField f1, ApiField f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});
		return this.fields;
	}

	public void addField(ApiField field) {
		if (!getFields().contains(field)) {
			getFields().add(field);
		}
	}

	/**
	 * 验证对象是否包含属性。
	 */
	public boolean isContainField() {
		return !getFields().isEmpty();
	}

	/**
	 * 验证对象是否包含列表属性。
	 */
	public boolean isContainListField() {
		for (ApiField field : getFields()) {
			if (field != null && field.isListField()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证是否包含日期属性。
	 */
	public boolean isContainDateField() {
		for (ApiField field : getFields()) {
			if ("Date".equals(field.getApiType())) {
				return true;
			}
		}
		return false;
	}

}
