package com.youtubesearch.queue.events;

import java.lang.invoke.MethodHandles;

import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.youtubesearch.service.HandleQueueService;
import com.youtubesearch.web.model.YTVideoMetaData;

/**
 * This is a Listener Program for the JMSQueueA Only probe the message and pass
 * to Publisher program for JMSQueueB
 * 
 * @author RaviBabu Vutla
 * @since 2020-10-24
 */

@Component
public class QueueAReceiver {

	private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	QueueBPublisher queueBPublisher;
	@Autowired
	XmlMapper xmlMapper;

	/**
	 * Listener for JMSQueueA that reads the XML messages in Queue and forwards them
	 * to another Publisher function for JMSQueueB
	 * 
	 * @param xml
	 * @param message
	 */
	@JmsListener(destination = HandleQueueService.QUEUE_A, containerFactory = "connectionFactory")
	public void receiveToQueueA(String xml, Message message) {
		logger.info("INFO: Original message to QueueA: " + xml);

		YTVideoMetaData ytVideoMetaData = new YTVideoMetaData();
		try {
			ytVideoMetaData = xmlMapper.readValue(xml, YTVideoMetaData.class);
			
			logger.info("INFO: From QueueA: " + ytVideoMetaData);
			logger.debug("DEBUG: Data being send to QueueBPublisher");
			queueBPublisher.processNSendMessage(ytVideoMetaData);
			
		} catch (JsonProcessingException e) {
			
			logger.error("Error: Error in parsing XML");
		}

	}
}