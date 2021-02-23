package com.saket.aws.lambdamvn.model;

public class PersonError {
	

	private String errorMessage;
	private String errorCode;
	
	public PersonError() {
		
	}
	public PersonError(String errorMessage, String errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
