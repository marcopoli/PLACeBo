package ItemsEnchantment;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.google.api.services.youtube.YouTube.Captions.Download;

import io.indico.api.text.Emotion;
import io.indico.api.utils.IndicoException;

public class GetInfo {

	public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException, SAXException, DataNotFoundException, UnsupportedOperationException, IndicoException, ClassNotFoundException, SQLException {
		
		String query ="select track_uri, track_name, artist_name from track where topPos = -1 limit 30647, 40000 ";
		String update = "update track set topPos = ?, weeksInRanking = ? where track_uri = ?";
		DbAccess.initConnection();
		java.sql.Connection con =  DbAccess.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		
		int i=0;
		while (rs.next()){
			String uri = rs.getString("track_uri");
			String title = rs.getString("track_name");
			String artist = rs.getString("artist_name");
			System.out.println(title+ " "+ artist);
			
			int top = 0;
			int weeks = 0;
			try{
				ExtractChart e = new ExtractChart(title, artist);
				top = e.getTopPeak();
				weeks = e.getWeekInChart();
				PreparedStatement st2 = con.prepareStatement(update);
				st2.setString(3,uri);
				st2.setInt(1, top);
				st2.setInt(2, weeks);	
				st2.execute();
			}catch (NullPointerException ex){
				PreparedStatement st2 = con.prepareStatement(update);
				st2.setString(3,uri);
				st2.setInt(1, -1);
				st2.setInt(2, 0);	
				st2.execute();
				i++;
				System.out.println(i+ " non trovato");
				continue;
			}
			
			PreparedStatement st2 = con.prepareStatement(update);
			st2.setString(3,uri);
			st2.setInt(1,top);
			st2.setInt(2, weeks);	
			i++;
			System.out.println(i+ " trovato");
			
			
		}
		

	}
	
		/**
		ExtractChart e = new ExtractChart("mercy ", "the ghost inside");
		try{
			
		}
		e.getTopPeak();
		e.getWeekInChart();
		**/
}
