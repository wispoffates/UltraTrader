
package com.thedemgel.ultratrader.util;


public class ResponseObject<O> {
	private String message;
	private ResponseObjectType type;
	private O object = null;

	public ResponseObject(ResponseObjectType type) {
		this.message = "";
		this.type = type;
	}

	public ResponseObject(String message, ResponseObjectType type) {
		this.message = message;
		this.type = type;
	}

	public ResponseObject(String message, ResponseObjectType type, O returnObject) {
		this.message = message;
		this.type = type;
		object = returnObject;
	}

	public String getMessage() {
		return message;
	}

	public ResponseObjectType getResponseObjectType() {
		return type;
	}

	public O getReturnObject() {
		return object;
	}
}
