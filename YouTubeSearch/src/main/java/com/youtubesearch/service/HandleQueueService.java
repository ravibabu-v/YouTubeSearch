package com.youtubesearch.service;

import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.jms.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.converter.Jackson2XmlMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.youtubesearch.web.model.YTVideoMetaData;

/**
 * This program provides a method to Publish XML messages to JMSQueue
 * 
 * @author RaviBabu Vutla
 * @version 1.0
 * @since 2020-10-24
 */

@Service
@ComponentScan("com.youtubesearch")
public class HandleQueueService {

	private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	public static final String QUEUE_A = "JMSQueueA";
	public static final String QUEUE_B = "JMSQueueB";

	@Autowired
	private JmsTemplate jmsTemplate;

	@Bean
	public XmlMapper getXMLMapper() {
		return new XmlMapper();
	}

	@Bean // Serialize message content to XML
	public Jackson2XmlMessageConverter jacksonJmsMessageConverter() {
		return new Jackson2XmlMessageConverter();
	}

	@Bean // Container for the JMSListener
	public JmsListenerContainerFactory<?> connectionFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		// provide default to the factory, including the message converter
		configurer.configure(factory, connectionFactory);

		return factory;
	}

	/**
	 * Prepare a customer Object and convert to XML to publish to JMSQueueA
	 * 
	 * @input Iterator of YouTube SearchResult
	 */
	public void publishToJMSQueueA(List<YTVideoMetaData> allVideos) {
		
		for(YTVideoMetaData ytVideoMetaData : allVideos) {
				logger.debug("DEBUG: Publishing Video Details: " + ytVideoMetaData);

				String xml = "";
				try {
					xml = getXMLMapper().writeValueAsString(ytVideoMetaData);
					logger.debug("DEBUG: Publishing to JMS Queue-A.");
					jmsTemplate.convertAndSend(QUEUE_A, xml);
				} catch (Exception e1) {
					logger.error("Error: InternalError in parsing XML:"+ e1.getMessage());
				}
			}
		}
}
