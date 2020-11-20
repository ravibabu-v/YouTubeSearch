package com.youtubesearch.queue.events;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.youtubesearch.service.HandleQueueService;
import com.youtubesearch.web.model.SearchKey;
import com.youtubesearch.web.model.YTVideoMetaData;

/**
 * This is a Publisher Program for the JMSQueueB It modifies the messages
 * received from JMSQueueA and uses them to publish as XML messages
 * 
 * @author RaviBabu Vutla
 * @since 2020-10-24
 */

@Component
@ComponentScan("com.youtubesearch")
public class QueueBPublisher {

	@Autowired
	JmsTemplate jmsTemplate;

	@Autowired
	XmlMapper xmlMapper;

	@Autowired
	private SearchKey searchKey;
	
	private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Replace the Title by removing the last letter of the Keyword. And publish the
	 * XML form of the MetaData Object to JMSQueueB
	 * 
	 * @param ytVideoMetaData
	 */
	public void processNSendMessage(YTVideoMetaData ytVideoMetaData) {

		 //Take the keyword and remove the last letter of that word in the Title
		 String keyWord = searchKey.getKeyWord();
		 String newTitle =
		 ytVideoMetaData.getTitle().replaceAll(keyWord,keyWord.substring(0,keyWord.length()-1));
		//String newTitle = ytVideoMetaData.getTitle().replaceAll("Telecom", "Telco");

		String xml = "";
		try {
			xml = xmlMapper.writeValueAsString(new YTVideoMetaData(ytVideoMetaData.getId(), newTitle,
					ytVideoMetaData.getDescription(), ytVideoMetaData.getUrl()));
			
			logger.debug("DEBUG: Publishing to JMSQueueB ...");
			jmsTemplate.convertAndSend(HandleQueueService.QUEUE_B, xml);
			logger.debug("DEBUG: Completed publishing to JMSQueueB ...");
			
		} catch (JsonProcessingException e1) {
			
			logger.error("ERROR: Parsing the XML failed ");
		}

	}
}