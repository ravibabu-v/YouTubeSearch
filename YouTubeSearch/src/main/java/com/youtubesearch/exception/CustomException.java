package com.youtubesearch.exception;

/**
 * Simple Custom Exception
 * 
 * @author RaviBabu Vutla
 * @version 1.0
 * @since 2020-10-25
 */

public class CustomException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public CustomException(String message) {
		super (message);
	}
}
