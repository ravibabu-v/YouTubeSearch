package com.youtubesearch.queue.events;

import java.lang.invoke.MethodHandles;

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
 * This is simple Listener Program for the JMSQueueB to probe the received messages
 * 
 * @author RaviBabu Vutla
 * @since 2020-10-24
 */

@Component
public class QueueBReceiver {

	private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    XmlMapper xmlMapper;

    /**
     * Receive the XML Message from JMSQueueB and display
     * @param xml
     */
    
    @JmsListener(destination = HandleQueueService.QUEUE_B, containerFactory = "connectionFactory")
    public void receiveToQueueB(String xml) {

        logger.info("INFO: message to QueueB: " + xml);
        
        YTVideoMetaData ytVideoMetaData = new YTVideoMetaData();
		try {
			ytVideoMetaData = xmlMapper.readValue(xml, YTVideoMetaData.class);
			logger.info("INFO: From QueueB: " + ytVideoMetaData);
		} catch (JsonProcessingException e) {
			logger.error("Error: Error in parsing XML");
		}
    }
}
