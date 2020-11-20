package com.youtubesearch.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * This program implements an application that simply queries Youtube
 * with the input KeyWord and applies below steps. Search Results are put into a
 * JMSQueueA in the form of XML messages. Note that MetaData of the Videos in
 * Search Results like Title and URL are only used. And a listener then receives
 * these XML messages, removes the last letter of the Keyword and places these
 * XML messages in another JMSQueueB.
 *
 * @author RaviBabu Vutla
 * @version 1.0
 * @since 2020-10-24
 */

@SpringBootApplication
@ComponentScan("com.youtubesearch")
public class YouTubeSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(YouTubeSearchApplication.class, args);
	}

}

