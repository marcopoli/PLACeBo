package RecommendationAPC;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Demo {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		/**
		DbAccess.initConnection();
		Connection con = DbAccess.getConnection();
		Statement st = con.createStatement();
		GeneralSelector<String> g = new GeneralSelector<String>();
		ResultSet rs = st.executeQuery("SELECT * FROM spotify_mpd.track join spotify_mpd.playlist_track on track.track_uri = playlist_track.track_id where  playlist_id = 2");
		ArrayList<String> s = new ArrayList<String>();
		while (rs.next()){
			String genre = rs.getString("genre");
			if (!genre.equals("")) 	s.add(genre);
			else continue;
			}	**/
		Integer i = new Integer(4);
		if(i.getClass().getName().equals("java.lang.Integer")){
			System.out.println("uguali");
		}
		
		
		
		}	

	}