package com.youtubesearch.web.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * A simple POJO for representation of the MetaData of Video 
 * Attributes: ID, Title, Description, URL
 * 
 * @author RaviBabu Vutla
 * @version 1.0
 * @since 2020-10-24
 */

@Component
@JacksonXmlRootElement(localName = "YTVideoMetaData")
public class YTVideoMetaData {

	@JacksonXmlProperty
	private String id;
	@JacksonXmlProperty
	private String title;
	@JacksonXmlProperty
	private String description;
	@JacksonXmlProperty
	private String url;
	
	public YTVideoMetaData() {
	}

	public YTVideoMetaData(String title, String url) {
		this.title = title;
		this.url = url;
	}

	public YTVideoMetaData(String id, String title, String desc, String url) {
		this.id = id;
		this.title = title;
		this.description = desc;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return String.format("Video{Id=%s, Title=%s, Description=%s, Url=%s}", getId(), getTitle(), getDescription(), getUrl());
	}

}
