package ItemsEnchantment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class iTunesApi {
	public static JSONObject getITunesJSon(String song, String author){
		JSONObject doc;
		try{  
			 URL url = new URL("http://itunes.apple.com/search?term="+URLEncoder.encode(author+" "+song,"UTF-8")+"&entity=song");
			 System.out.println("Sto cercando le informazioni su itunes...");
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

			doc = new JSONObject(page);
			System.out.println("info iTunes trovate.");
		}catch(Exception e){System.out.println("Informazioni Itunes non trovate!!");doc = new JSONObject("{}");}
		return doc;
	}
			
		
	  public static String getGeneralInfo(JSONObject doc) throws IOException{
		 try{  
			JSONArray results = doc.getJSONArray("results");
			String re = results.getJSONObject(0).toString();
			
		   return re;
		}catch(Exception e){return "";}

	   }
	  
	  public static String getMainGenre(JSONObject doc) throws IOException{
		  try{   
			  JSONArray results = doc.getJSONArray("results");
			  String re = results.getJSONObject(0).getString("primaryGenreName");
			  return re;
			   
				
			}catch(Exception e){return "";}
			
	  }
	  
	  public static boolean isExplicit(JSONObject doc) throws IOException{
		 boolean returnFlag;
		try {
			JSONArray results = doc.getJSONArray("results");
			String re = results.getJSONObject(0).getString("trackExplicitness");
			if (re.equals("notExplicit")) {
				returnFlag= false;
			}else {returnFlag= true;}					
		}catch(JSONException j) {
					returnFlag=false;
				}
				
			
			return returnFlag;
	  }
}
