package com.taobao.top.autosdk;

/**
 * SDK通知异常。
 * 
 * @author fengsheng
 */
public class SdkException extends Exception {

	private static final long serialVersionUID = -5377959635282864604L;

	public SdkException() {
		super();
	}

	public SdkException(String message, Throwable cause) {
		super(message, cause);
	}

	public SdkException(String message) {
		super(message);
	}

	public SdkException(Throwable cause) {
		super(cause);
	}

}
