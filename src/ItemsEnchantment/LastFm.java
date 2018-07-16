package ItemsEnchantment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

public class LastFm {
   public static String getTopTags(String song, String author) throws IOException{
	   //Application name	Marco Polignano
	   //API key	0381be1d08ec6cb8533f172d94d85d96
	   //Shared secret	105187364ad8b6bdb19629b298b9a5ff
	   //Registered to	kramLast
	   
	try{   
		String key ="0381be1d08ec6cb8533f172d94d85d96";
		URL url = new URL( "http://ws.audioscrobbler.com/2.0/?method=track.gettoptags&artist="+URLEncoder.encode(author,"UTF-8")+
			   "&track="+URLEncoder.encode(song,"UTF-8")+"&api_key="+key+"&format=json");
		  
		  System.out.println("Sto cercando i tag LastFm");
	     // InputStream is = url.openStream();  // throws an IOException

	     HttpURLConnection huc = (HttpURLConnection) url.openConnection();
	     HttpURLConnection.setFollowRedirects(false);
	     huc.setConnectTimeout(15 * 1000);
	     huc.setRequestMethod("GET");
	     huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
	     huc.connect();
	      
	     BufferedReader br = new BufferedReader(new InputStreamReader( huc.getInputStream()));

	     String page = "";
	     String line = "";
	     while ((line = br.readLine()) != null) {
	          page = page+ line;
	      }

		JSONObject doc = new JSONObject(page);
	   
		JSONArray tags = doc.getJSONObject("toptags").getJSONArray("tag");
		String generi = "";
		
		for(int i = 0; i < tags.length(); i++){
			JSONObject item = tags.getJSONObject(i);
			String genre = item.getString("name");
			if(i==0){
				generi = genre;
			}else{
				generi = generi + ","+genre;
			}
		}
	   System.out.println("Tag trovati.");
	   return generi;
	}catch(Exception e){System.out.println("Tag non trovati!!");return "";}
	
   }
   
   
}
