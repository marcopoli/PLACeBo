package ItemsEnchantment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

public class ExtractRanking {
	private JSONObject jsonSong;
	
	public ExtractRanking(String nameSong, String spotifyID) throws IOException {
		try{
			JSONObject json;
			URL url = new URL("http://billboard.modulo.site/search/song?q="+URLEncoder.encode(nameSong, "UTF-8"));
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
			page = "{ \"results\":" + page + "}";
			
			json = new JSONObject(page);
			Iterator<Object> i = json.getJSONArray("results").iterator();
			
			String songId = json.getJSONArray("results").getJSONObject(0).getString("song_id");
			while (i.hasNext()) {
				JSONObject j = (JSONObject)i.next();
				String actualSpotifyId = j.getString("spotify_id");			
				if (actualSpotifyId.toString().equals(spotifyID) || spotifyID.equals("null")) songId = j.getString("song_id"); break;
			}
			
			URL urlSong = new URL(" http://billboard.modulo.site/music/song/" + URLEncoder.encode(songId, "UTF-8"));
			HttpURLConnection huc2 = (HttpURLConnection) urlSong.openConnection();
		    HttpURLConnection.setFollowRedirects(false);
		    huc2.setConnectTimeout(15 * 1000);
		    huc2.setRequestMethod("GET");
		    huc2.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
		    huc2.connect();
		    BufferedReader br2 = new BufferedReader(new InputStreamReader( huc2.getInputStream()));
			String pageSong = "";
			String lineSong = "";
			while ((lineSong = br2.readLine()) != null) {
			      pageSong = pageSong+ lineSong;
			}
			System.out.println("ranking trovato");
			jsonSong = new JSONObject(pageSong);
		}catch(Exception e) {System.out.println("Informazioni sul ranking non trovate!!");this.jsonSong = new JSONObject("{}");};
		
				
	}
	
	public int getTopPos() {
		ArrayList<Integer> listPos = new ArrayList<Integer>();
		try{
			Iterator<Object> rankIt = this.jsonSong.getJSONArray("rankings").iterator();
			
			while(rankIt.hasNext()) {
				JSONObject r = (JSONObject)rankIt.next();			
				listPos.add(Integer.parseInt(r.getString("rank")));
			}
		}catch(Exception e){System.out.println("Top posizione non trovata!!");return -1;}
		
		return Collections.max(listPos);
	}
	
	public JSONObject getObject() {
		return this.jsonSong;
	}
	
	public int getnumberOfWeek() {
		int weeks;
		try{
			weeks = this.jsonSong.getJSONArray("rankings").length();
		}catch(Exception e){System.out.println("informazione rank settimana non trovata!!");weeks=0;}
		return  weeks;
		
	}
	
	static String getDateTimeTopPos(JSONObject j, int pos) {
		String datetime = null;
		try{
			Iterator<Object> i = j.getJSONArray("rankings").iterator();
			while(i.hasNext()) {
				JSONObject jObj = (JSONObject)i.next();
				if(Integer.parseInt(jObj.getString("rank")) == pos) {
					datetime = jObj.getString("date"); 
					break;	
				}
			}
			
		}catch (Exception e){
			System.out.println("informazione data top pos non trovata!!");
			
		
		}
		return datetime;
		
	}
	
}