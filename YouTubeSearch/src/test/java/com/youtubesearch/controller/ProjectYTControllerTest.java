package com.youtubesearch.controller;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.youtubesearch.service.YTSearchService;
import com.youtubesearch.web.model.YTVideoMetaData;

@ContextConfiguration
class ProjectYTControllerTest {

	@Autowired 
	YTSearchService ytFetchService;
	
	@Test
	public void testSearchStore() throws Exception {
		
		//List<YTVideoMetaData> allVideos = ytFetchService.searchYTVideos("Telecom");
		//assertEquals(allVideos.size(),YTSearchService.MAX_RESULTS);	
	}
}
