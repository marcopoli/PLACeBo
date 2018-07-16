package ItemsEnchantment;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import RecommendationAPC.Ranking;

public class ExtractPopolarityIndex {
	public static void main(String[] args) throws ClassNotFoundException, SQLException{	
		DbAccess.initConnection();
		Connection con = DbAccess.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select track_uri, YouTubeView, number_playlist, topPos, weeksInRanking from track where YouTubeView is not null and popolarity_index is null");
		int i = 1;
		while(rs.next()){
			Double score = Ranking.getRankingValue(rs.getLong("YouTubeView"),rs.getInt("number_playlist"), rs.getInt("weeksInRanking"), rs.getInt("TopPos"));
			Statement st2 = con.createStatement();
			st2.executeUpdate("update track set popolarity_index = "+score+" where track_uri = '"+rs.getString("track_uri")+"'");
			System.out.println(i);
			i++;
		}
		
	}
}
