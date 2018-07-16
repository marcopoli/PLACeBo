package ItemsEnchantment;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;

public class SelectTest {

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		DbAccess.initConnection();
		java.sql.Connection con = DbAccess.getConnection();
		String queryGetNumber ="select distinct count(*) from playlist_track where playlist_id = ?";
		String queryGetPlaylist ="SELECT * FROM spotify_mpd.playlist_track where playlist_id = ?";
		String querySetTest = "update playlist_track set test = 1 where track_id = ? and playlist_id = ? ";
		PreparedStatement stPlaylist = con.prepareStatement(queryGetPlaylist);
		BufferedReader reader = new BufferedReader(new FileReader("/media/mynewdrive/SHARED/completed_playlist.json"));
		String page = "";
	    String line = "";
	    while ((line = reader.readLine()) != null) {
	       page = page+ line;
	    }
	    reader.close();
		JSONObject doc = new JSONObject (page);
		JSONArray playlistArray = doc.getJSONArray("playlists");		
		Iterator<Object> iteratorPlaylist = playlistArray.iterator();
		Iterator <Object> tempIterator = playlistArray.iterator();
		String stringForQuery = "(";
		PreparedStatement updateTest = con.prepareStatement(querySetTest);
		while (tempIterator.hasNext()){
			JSONObject pl =(JSONObject)tempIterator.next();
			if (tempIterator.hasNext()){
				stringForQuery = stringForQuery + pl.get("playlist_id") + ",";
			}else{
				stringForQuery = stringForQuery + pl.get("playlist_id") +")";
			}
			
		}
		//stringForQuery = stringForQuery + ")";
		String queryCountPlaylist = "select count(*) from playlist_track where track_id = ? and playlist_id in " + stringForQuery;
		//iteratorPlaylist.next();
		System.out.println(queryCountPlaylist);
		PreparedStatement st2 = con.prepareStatement(queryCountPlaylist);
		
		while (iteratorPlaylist.hasNext()){
			
			JSONObject playlist = (JSONObject)iteratorPlaylist.next();
			int playlistId = playlist.getInt("playlist_id");
			
			
			//PreparedStatement stNumber = con.prepareStatement(queryGetNumber);
			//stNumber.setString(1,String.valueOf(playlistId));
			//ResultSet rsNum = stNumber.executeQuery();
			//rsNum.next();
			//int countPlaylist ;= rsNum.getInt("count");
			
			ResultSet trackRes = null;
				
			
			stPlaylist.setString(1, String.valueOf(playlistId));
			trackRes = stPlaylist.executeQuery();
			trackRes.last();
			int dim = trackRes.getRow();
			
			System.out.println("DIMENSION "+dim);
			int numTest =(int)Math.round(0.2 * dim);
			System.out.println("DA TROVARE: "+numTest);
			int testSelected = 0;//contatore 
			
			
			trackRes.beforeFirst();
			
			ResultSet rs2 = null;
			int count;
			int threshold = 1;
			ArrayList <String> founds = new ArrayList<String>();
			while(testSelected < numTest){
				trackRes.next();
				if (trackRes.isAfterLast() && threshold == -1){
					break;
				}
				if (trackRes.isAfterLast() && threshold == 1){
					trackRes.first();
					threshold = -1;
				}
				
				
				//System.out.println(trackRes.getRow());
				String trackId = trackRes.getString("track_id");
				
				st2.setString(1, trackId);
				rs2 = st2.executeQuery();
				rs2.next();
				count = rs2.getInt("count(*)");
				System.out.println("PLAYLIST "+playlistId);
				System.out.println("TRACCIA "+trackId);
				if (count > threshold && !founds.contains(trackId)){
					System.out.println("trovato");
					testSelected++;
					founds.add(trackId);
					updateTest.setString(1, trackId);
					updateTest.setString(2, String.valueOf(playlistId));
					updateTest.executeUpdate();
					System.out.println("trovato");
				} ;				
				
			}
				
				/*
				int min = 1;
				int max = dim -1;
				boolean isNew = false;
				int posTrack=0;
				while(!isNew){
					posTrack = (int)(Math.random() * ((max - min) + 1)) + min;
					if (!posTest.contains(posTrack)) isNew = true;
				}
				
				for (int currentRow = 1; currentRow <= posTrack; currentRow ++ ){
					trackRes.next();
				}
				String trackId = trackRes.getString("track_id");
				System.out.println("PLAYLIST "+playlistId);
				System.out.println("TRACCIA "+trackId);
				stCount = con.prepareStatement(queryCountPlaylist);
				stCount.setString(1,trackId);
				rs = stCount.executeQuery();
				rs.next();
				int countPlylist = rs.getInt(1);
				int threshold=0;
				if (failCount > 300){
					threshold = -1;
				}else threshold = 1;
				
				if (countPlylist > threshold) {
					testSelected++;
					posTest.add(posTrack);
					System.out.println("trovato " + posTrack);
					failCount=0;
				}else failCount++;
				
				
				
			}
			
		}*/
			
	}
		con.close();

}
}
