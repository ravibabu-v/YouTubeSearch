package com.youtubesearch.web.model;

import org.springframework.stereotype.Component;

/**
 * A simple POJO for representation of the Search Parameters
 * Attributes: keyWord
 * 
 * @author RaviBabu Vutla
 * @version 1.0
 * @since 2020-10-24
 */

@Component
public class SearchKey {
   
	private String keyWord="Teleom";

	public SearchKey() {
		this.keyWord = "Teleom";
	}
	
	public SearchKey(String keyWord) {
		this.keyWord = keyWord;
	}
	
	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
}
