package RecommendationAPC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class CFDatasetGenerator {
	private static final String  PATH = "/media/mynewdrive/SHARED/dataCF.data";
	private static final int MAX_POS = 250;
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader("/media/mynewdrive/SHARED/completed_playlist.json"));
		String page = "";
	    String line = "";
	    String querySection = "";
	    while ((line = reader.readLine()) != null) {
	       page = page+ line;
	    }
	    reader.close();
		JSONObject doc = new JSONObject (page);
		JSONArray playlistArray = doc.getJSONArray("playlists");		
		Iterator<Object> tempIterator = playlistArray.iterator();
		while (tempIterator.hasNext()){
			String s = tempIterator.next().toString().replace("}", "").split(":")[1];
			if (tempIterator.hasNext()) querySection += s +",";
			else querySection += s;
			
		}
		System.out.println(querySection);
		DbAccess.initConnection();
		Connection con = DbAccess.getConnection();
		Statement st = con.createStatement();
		Statement st2 = con.createStatement();
		ResultSet rs = st.executeQuery("select * from playlist_track where test = 0 and playlist_id in ("+querySection+")");
		FileWriter file = new FileWriter(PATH);
		ResultSet rs2;
		
		while (rs.next()){
			String playlistId = rs.getString("playlist_id");
			int pos = rs.getInt("pos");
			System.out.println("track_id:"+rs.getString("track_id"));
			rs2 = st2.executeQuery("select song_id from track where track_uri ='"+ rs.getString("track_id")+"'");
			rs2.next();	
			Long songId;
			try{
				songId = rs2.getLong("song_id");
			}catch(java.sql.SQLException e){
				System.out.println("NON TROVATO");
				continue;
			}
						
			pos++;
			double posValue = (double)1-((double)pos/(double)MAX_POS);
			file.write("\n"+playlistId+";"+songId+";"+posValue);	
			file.flush();
		}
		file.close();
		

	}

}
