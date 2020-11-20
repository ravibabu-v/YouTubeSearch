package com.youtubesearch.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Search;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.youtubesearch.exception.CustomException;
import com.youtubesearch.web.model.YTVideoMetaData;

/**
 * This program provides a method to search Videos based on keyWord It uses the
 * YouTube search service and Developer-APIKey to retrieve the results
 * 
 * @author RaviBabu Vutla
 * @version 1.0
 * @since 2020-10-24
 */

@Service
public class YTSearchService {

	private static final String PROPERTIES_FILENAME = "youtube.properties";
	//Temporarily using another key inside program due to quota exhaustion on the other
	//private static final String DEVELOPER_KEY = "";

	private static final String APPLICATION_NAME = "ProjectYT-Search YouTube";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	public static final Long MAX_RESULTS = (long) 50;
	//Setting lower limit to reduce quota usage
	public static final int MAX_SEARCH_LOOPS = 1;
	
	private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Load the properties file and retrieve the given Key
	 * @return the value of the Key Requested
	 */
	private String readSecretKey(String keyName) {

		Properties properties = new Properties();
		try {
			InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
			properties.load(in);
		} catch (IOException e) {
			logger.error("Error: There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : "
					+ e.getMessage());
			logger.error("Error: API Key is necessary to reach YouTube, Exit!");
			System.exit(1);
		}

		String apiKey = properties.getProperty(keyName);
		return apiKey;
	}

	/**
	 * Build and return an authorized API client service.
	 * 
	 * @return an authorized API client service
	 * @throws GeneralSecurityException, IOException
	 */
	public static YouTube getService() throws GeneralSecurityException, IOException {
		final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		return new YouTube.Builder(httpTransport, JSON_FACTORY, null).setApplicationName(APPLICATION_NAME).build();
	}

	/**
	 * Call function to create API service object. Define and execute API request.
	 * Print API response. Parse the input data to find the specific metadata -
	 * title and URL for each Video.
	 * 
	 * @throws GeneralSecurityException, IOException, GoogleJsonResponseException, CustomException
	 */
	public List<YTVideoMetaData> searchYTVideos(String keyWord)
			throws GeneralSecurityException, IOException, GoogleJsonResponseException, CustomException {

		List<YTVideoMetaData> allVideos = new ArrayList<YTVideoMetaData>();
		String nextToken = "";
		int searchLoops=0;

		YouTube youtubeService = getService();
		// Define and execute the API request
		YouTube.Search.List request = youtubeService.search().list("snippet");

		try {
			do {				 
				SearchListResponse response = request.setKey(this.readSecretKey("youtube.apikey"))
						.setMaxResults(MAX_RESULTS).setType("video").setQ(keyWord).execute();
				//Temporarily using another key inside program due to quota exhaustion
				//SearchListResponse response = request.setKey(DEVELOPER_KEY)
				//				.setMaxResults(MAX_RESULTS).setType("video").setQ(keyWord).execute();
						
				logger.debug("Debug: SearchResults: " + response);
				List<SearchResult> results = response.getItems();

				for (SearchResult singleVideo : results) {

					ResourceId rId = singleVideo.getId();
					// Required Metadata for video is present only if the Item is a Video
					if (rId.getKind().equals("youtube#video")) {

						// URL is inside Thumbnail object
						Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
						YTVideoMetaData ytVideoMetaData = new YTVideoMetaData(singleVideo.getId().getVideoId(),
								singleVideo.getSnippet().getTitle(), singleVideo.getSnippet().getDescription(),
								thumbnail.getUrl());
						logger.info("INFO: SearchResult Video: " + ytVideoMetaData);
						allVideos.add(ytVideoMetaData);
					}
				}
				nextToken = response.getNextPageToken();
				System.out.println("nextToken :  " + nextToken);
				searchLoops++;
			} while (nextToken != null & searchLoops < MAX_SEARCH_LOOPS);

			return allVideos;
			
		} catch (Exception e) {
			logger.error("Error: InternalError in reading the search results...:"+ e.getMessage());
			throw new CustomException("InternalError in reading the search results");
		}
	}

}