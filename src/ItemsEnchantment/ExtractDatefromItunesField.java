package ItemsEnchantment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class ExtractDatefromItunesField {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
		
		String query = "SELECT track_uri, iTunes_info from track where YouTubeView is not null and iTunes_info <> '' and releaseDate is null";
		String update =  "update track set releaseDate =? where track_uri=?";
		
		DbAccess.initConnection();
		Connection con = DbAccess.getConnection();
		Statement st = con.createStatement();
		PreparedStatement updateSt = con.prepareStatement(update);
		ResultSet rs = st.executeQuery(query);
		String trackUri;
		String iTunesInfo;
		while (rs.next()){
			trackUri = rs.getString("track_uri");
			iTunesInfo = rs.getString("iTunes_info");
			
			JSONObject json =  (JSONObject) new JSONParser().parse(iTunesInfo);
			String date =(String)json.get("releaseDate");
			String year = date.split("-")[0];
			updateSt.setString(1, year);
			updateSt.setString(2, trackUri);
			updateSt.executeUpdate();				
		}
		
	}

}
