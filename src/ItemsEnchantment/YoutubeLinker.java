package ItemsEnchantment;
/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */


import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.GenericData;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Print a list of videos matching a search term.
 *
 * @author Jeremy Walker
 */
public class YoutubeLinker {
	private String videoId;
	private String link = "";

    /**
     * Define a global variable that identifies the name of a file that
     * contains the developer's API key.
     */
    private static final String PROPERTIES_FILENAME = "youtube.properties";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;

    /**
     * Initialize a YouTube object to search for videos on YouTube. Then
     * display the name and thumbnail image of each video in the result set.
     *
     * @param args command line args.
     */
    public  YoutubeLinker(String sera) {
        // Read the developer key from the properties file.
       
        try {
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-search-sample").build();

            // Prompt the user to enter a query term.
            String queryTerm = sera;

            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            // Set your developer key from the {{ Google Cloud Console }} for
            // non-authenticated requests. See:
            // {{ https://cloud.google.com/console }}

            search.setKey("AIzaSyAcRW1U6-fgmH6zs3CbgcpvX-cYyVQbIDQ");
            search.setQ(queryTerm);

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {

                Iterator iteratorSearchResults  = searchResultList.iterator();
                
                if (!iteratorSearchResults.hasNext()) {
                    System.out.println(" There aren't any results for your query.");
                }

                //while (iteratorSearchResults.hasNext()) {

                    SearchResult singleVideo = (SearchResult) iteratorSearchResults.next();
                    ResourceId rId = singleVideo.getId();
                    this.videoId=rId.getVideoId();

                    // Confirm that the result represents a video. Otherwise, the
                    // item will not contain a video ID.
                    if (rId.getKind().equals("youtube#video")) {
                    
                        Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                        String link = "https://www.youtube.com/watch?v="+rId.getVideoId();

                        this.link=link;
                       /*
                        System.out.println(" Video Id" + link);
                        System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                        System.out.println(" Thumbnail: " + thumbnail.getUrl());
                        System.out.println("\n-------------------------------------------------------------\n");
                        */
                    }
                }
          //  }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        
		
    }

    /*
     * Prompt the user to enter a query term and return the user-specified term.
     */
    private static String getInputQuery() throws IOException {

        String inputQuery = "";

        System.out.print("Please enter a search term: ");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        inputQuery = bReader.readLine();

        if (inputQuery.length() < 1) {
            // Use the string "YouTube Developers Live" as a default.
            inputQuery = "YouTube Developers Live";
        }
        return inputQuery;
    }

    /*
     * Prints out all results in the Iterator. For each result, print the
     * title, video ID, and thumbnail.
     *
     * @param iteratorSearchResults Iterator of SearchResults to print
     *
     * @param query Search query (String)
     */
    public String getvideoId() {
    	return this.videoId;
    }
    public String getLink() {
    	return this.link;
    }
    
    private BufferedReader getConnectionView() throws IOException{
    	boolean flag = false;
    	BufferedReader br = null;
    	while(!flag){
    		try{
    			URL url=new URL("https://www.googleapis.com/youtube/v3/videos?part=statistics&id="+this.videoId+"&key=AIzaSyCjXtwcYxYFcFAn6IJ0GEZkcBtFvgE7Khw");
    	    	HttpURLConnection huc = (HttpURLConnection) url.openConnection();
    		    HttpURLConnection.setFollowRedirects(false);
    		    huc.setConnectTimeout(15 * 1000);
    		    huc.setRequestMethod("GET");
    		    huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
    		    huc.connect();
    		    flag = true;
    		    br = new BufferedReader(new InputStreamReader( huc.getInputStream()));
    		}catch(SocketTimeoutException s){
    			flag = false;
    		}
    		
    	}
    	return br;
    	
	    
	     
    }
    
    
    public long getViewCount() { 
    	JSONObject doc= null;
    	try{
    		BufferedReader br = getConnectionView(); 

    	    String page = "";
    	    String line = "";
    	    while ((line = br.readLine()) != null) {
    	       page = page+ line;
    	    }

    	    doc = new JSONObject(page);
    	   
    		long countView = doc.getJSONArray("items").getJSONObject(0).getJSONObject("statistics").getLong("viewCount");
    		System.out.println("Visualizzazioni Youtube ottenute"+countView);
    		return countView;
    		//String down =(String) ((JSONObject)data.get(5)).get("viewCount");
    		//String down =(String) ((JSONObject)data.get(0)).get("download");cat
    	}catch(IOException| JSONException e){
    		//System.out.println(doc.toString());
    		
    		return 0;
    	}
	   
	    
    }
}
