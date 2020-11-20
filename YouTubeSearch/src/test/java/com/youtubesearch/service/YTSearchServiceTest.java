package com.youtubesearch.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import com.google.api.services.youtube.YouTube;

@ContextConfiguration
class YTSearchServiceTest {

	@Test
	public void testGetService() throws Exception {

		YouTube youtubeService = YTSearchService.getService();
		assertNotNull(youtubeService);
	}

}
