package com.taobao.top.autosdk.util;

import org.apache.commons.lang.StringUtils;

/**
 * 字符串操作帮助类
 * 
 * @author fengsheng
 */
public class StringKit {

	/**
	 * 生成JAVA的get/set方法的方法名。
	 */
	public static String getSetMethod(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		char[] chars = str.toCharArray();
		char c1 = chars[0];
		if (c1 >= 97 && c1 <= 122) {
			if (chars.length > 1) {
				char c2 = chars[1];
				if (!(c2 >= 65 && c2 <= 90)) {
					chars[0] = (char) (c1 - 32);
				}
			} else {
				chars[0] = (char) (c1 - 32);
			}
		}
		return new String(chars);
	}

	/**
	 * 将字符串的首字符传唤为大写
	 */
	public static String capitalize(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		char[] chars = str.toCharArray();
		char c = chars[0];
		if (c >= 97 && c <= 122)
			chars[0] = (char) (c - 32);
		return new String(chars);
	}

	/**
	 * 将字符串由驼峰转换为下划线形式的
	 */
	public static String toLowercaseWithUnderscore(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		StringBuffer hs = new StringBuffer();
		char[] chars = str.toCharArray();
		char char0 = chars[0];

		if (char0 >= 65 && char0 <= 90)
			hs.append((char) (char0 + 32));
		else
			hs.append(char0);
		for (int i = 1; i < chars.length; i++) {
			char c = chars[i];
			if (c >= 65 && c <= 90)
				hs.append('_').append((char) (c + 32));
			else
				hs.append(c);
		}
		return hs.toString();
	}

	/**
	 * 将字符串转换为驼峰风格。
	 */
	public static String toCamelCase(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		StringBuffer hs = new StringBuffer();
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '.' || c == '_') {
				if (++i < chars.length) {
					c = chars[i];
					if (c >= 97 && c <= 122)
						hs.append((char) (c - 32));
					else
						hs.append(c);
				}
			} else
				hs.append(c);
		}
		return hs.toString();
	}

	/**
	 * 将字符串转换为帕斯卡风格。
	 */
	public static String toPascalCase(String str) {
		str = toCamelCase(str);
		char first = str.charAt(0);
		return Character.toUpperCase(first) + str.substring(1, str.length());
	}

	public static String createSerialUID() {
		StringBuffer UID = new StringBuffer();
		UID.append(new Double(Math.ceil(Math.random() * 8)).intValue());
		UID.append(new Double(Math.ceil(Math.random() * 8)).intValue());
		for (int i = 0; i < 17; i++)
			UID.append(new Double(Math.ceil(Math.random() * 9)).intValue());
		UID.append("L");
		return UID.toString();
	}

	/**
	 * 过滤不可见字符
	 */
	public static String escapeInvalidXml(String input) {
		if (StringUtils.isBlank(input)) {
			return null;
		}
		StringBuilder out = new StringBuilder();
		char current;
		for (int i = 0; i < input.length(); i++) {
			current = input.charAt(i);
			if ((current == 0x9) || (current == 0xA)
					|| (current == 0xD) || ((current >= 0x20) && (current <= 0xD7FF))
					|| ((current >= 0xE000) && (current <= 0xFFFD))
					|| ((current >= 0x10000) && (current <= 0x10FFFF)))
				out.append(current);
		}
		return out.toString();
	}
}
