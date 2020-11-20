package com.youtubesearch.controller;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.youtubesearch.service.HandleQueueService;
import com.youtubesearch.service.YTSearchService;
import com.youtubesearch.web.model.SearchKey;
import com.youtubesearch.web.model.YTSearchResponse;
import com.youtubesearch.web.model.YTVideoMetaData;

/**
 * Controller receives the HTTP GetRequest as below
 * URL: http://localhost:8080/searchStore?keyWord=<input> 
 * 
 * This program calls the SearchService to retrieve the videos from Youtube
 * And then the data is passed to QueueHandlerService to publish into JMSQueueA
 * 
 * @author RaviBabu Vutla
 * @version 1.0
 * @since 2020-10-24
 */

@RestController
@ComponentScan("com.youtubesearch")
public class ProjectYTController {

	private static final String ERROR_NO_VIDEOS_FOUND_MSG = "No Videos Found";
	private static final String INTERNAL_ERROR_MSG = "No Videos Found";
	private static final String SUCCESS_MSG = "Completed Successfully";
	@Autowired
	private YTSearchService ytFetchService;

	@Autowired
	private HandleQueueService queueService;

	@Autowired
	SearchKey searchKey;

	private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	// Rest-API for handling the search Requests
	@GetMapping(value = "/searchStore")
	public ResponseEntity<YTSearchResponse> searchStore(@RequestParam(required = false) String keyWord) {

		logger.info("INFO: Searching YouTube With " + keyWord);
		searchKey.setKeyWord(keyWord);

		try {
			// Retrieve the Videos using the given KeyWord
			List<YTVideoMetaData> allVideos = ytFetchService.searchYTVideos(searchKey.getKeyWord());
			logger.debug("DEBUG: FInished Searching YouTube...");

			if (!allVideos.isEmpty()) {
				// Passing data to QueueService handler for publishing forward
				queueService.publishToJMSQueueA(allVideos);
			} else {
				logger.error("Error: No Videos Found...");
				return new ResponseEntity<YTSearchResponse>(new YTSearchResponse(ERROR_NO_VIDEOS_FOUND_MSG),
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Error: InternalError...:"+ e.getMessage());
			return new ResponseEntity<YTSearchResponse>(new YTSearchResponse(INTERNAL_ERROR_MSG),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// Return OK
		logger.info("Info: process completed.");
		return new ResponseEntity<YTSearchResponse>(new YTSearchResponse(SUCCESS_MSG), HttpStatus.OK);
	}

}
