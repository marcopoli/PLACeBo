package ItemsEnchantment;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class prova {

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("/media/mynewdrive/SHARED/completed_playlist.json"));
		String page = "";
	    String pageLine = "";
	    while ((pageLine = reader.readLine()) != null) {
	       page = page+ pageLine;
	    }
	    reader.close();
		JSONObject doc = new JSONObject (page);
		JSONArray playlistArray = doc.getJSONArray("playlists");
		String querySection = " and playlist_id not in (", playlist;
		Iterator<Object> it = playlistArray.iterator();
		while(it.hasNext()){
			
			System.out.println(it.next().toString().replace("}", "").split(":")[1]);
		}

	}

}
