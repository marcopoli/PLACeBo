package RecommendationAPC;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;

public class ClassificatorRandom {
	private static final String FILE_NAME = "/media/mynewdrive/SHARED/resultsRandom.csv";	
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
	
	
			DbAccess.initConnection();
			java.sql.Connection con = DbAccess.getConnection();
			
			BufferedReader reader = new BufferedReader(new FileReader("/media/mynewdrive/SHARED/completed_playlist.json"));
			String page = "";
		    String line = "";
		    while ((line = reader.readLine()) != null) {
		       page = page+ line;
		    }
		    reader.close();
		   
			JSONObject doc = new JSONObject (page);
			JSONArray playlistArray = doc.getJSONArray("playlists");		
			Iterator<Object> tempIterator = playlistArray.iterator();
			String query = "SELECT * FROM track join playlist_track on track.track_uri = playlist_track.track_id where playlist_id =? and test = 1";
			
			PreparedStatement pst = con.prepareStatement(query);
			FileWriter file = new FileWriter(FILE_NAME);
			file.close();
			file = new FileWriter(FILE_NAME,true);
			file.write("id,map@1,precision@1,recall@1,f1@1,map@5,precision@5,recall@5,f1@5,map@10,precision@10,recall@10,f1@10,map@20,precision@20,recall@20,f1@20,map@50,precision@50,recall@50,f1@50");
			file.flush();
			ResultSet randomTrack = con.createStatement().executeQuery("SELECT * FROM spotify_mpd.track where track.YouTubeView is not null order by rand() limit 50");
			randomTrack.last();
			int lengthRandom = randomTrack.getRow();
			ResultSet rs;
			ArrayList <String> testingTrack = new ArrayList<String>();
	 		while (tempIterator.hasNext()){//ciclo playlist
				String playlistId = tempIterator.next().toString().replace("}", "").split(":")[1];
				System.out.println("Playlist id : "+playlistId);
				pst.setString(1,playlistId);
				rs = pst.executeQuery();
				testingTrack.clear();
				while (rs.next()){
					testingTrack.add(rs.getString("track_uri"));
				}
				randomTrack.beforeFirst();
				double map1=0.0, precision1 = 0.0,recall1 = 0.0, f1at1=0.0,map5=0.0, precision5 = 0.0,recall5 = 0.0, f1at5=0.0,map10=0.0, precision10 = 0.0,recall10= 0.0, f1at10=0.0,map20=0.0, precision20 = 0.0,recall20 = 0.0, f1at20=0.0, map50=0.0, precision50 = 0.0,recall50 = 0.0, f1at50=0.0;
				int pos = 0;
				int find = 0;
				double map = 0.0;
				int testingSize = testingTrack.size();
				double changeRecall = 1.0/(double)testingSize;
				while (randomTrack.next()){//ciclo tracce random della playlist 
					pos++;
					if (testingTrack.contains(randomTrack.getString("track_uri"))) {
						find++;
						map += ((double)find/(double)(pos))*changeRecall;
					}
					if (pos == 1){
						precision1 = (double)find/(double)pos;
						recall1 = (double)find/(double)testingSize;
						if (precision1 != 0.0 && recall1 != 0.0)f1at1 = (2.0*(precision1*recall1))/(precision1+recall1);
						map1 = map;
					}
					if (pos == 5 ){
						precision5 = (double)find/(double)pos;
						recall5 = (double)find/(double)testingSize;
						if (precision5 != 0.0 && recall5 != 0.0)f1at5 = (2.0*(precision5*recall5))/(precision5+recall5);
						map5 = map;
					}
					if (pos == 10){
						precision10 = (double)find/(double)pos;
						recall10 = (double)find/(double)testingSize;
						if (precision10 != 0.0 && recall10 != 0.0)f1at10 = (2.0*(precision10*recall10))/(precision10+recall10);
						map10 = map;
					}
					if (pos == 20 ){
						precision20 = (double)find/(double)pos;
						recall20 = (double)find/(double)testingSize;
						if (precision20 != 0.0 && recall20 != 0.0)f1at20 = (2.0*(precision20*recall20))/(precision20+recall20);
						map20 = map;
					}
					if (pos == 50 ){
						precision50 = (double)find/(double)pos;
						recall50 = (double)find/(double)testingSize;
						if (precision50 != 0.0 && recall50 != 0.0)f1at50 = (2.0*(precision50*recall50))/(precision50+recall50);
						map50 = map;
					}
					
					
					}
				file.write("\n"+playlistId+","+map1+","+precision1+","+recall1+","+f1at1+","+map5+","+precision5+","+recall5+","+f1at5+","+map10+","+precision10+","+recall10+","+f1at10+","+map20+","+precision20+","+recall20+","+f1at20+","+map50+","+precision50+","+recall50+","+f1at50+","+","+find);
				file.flush();
							
				
				rs.close();
				}
	 		randomTrack.close();
		}
}
