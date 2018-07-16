package ItemsEnchantment;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ExtractMainEmotion {
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		DbAccess.initConnection();
		Connection con = DbAccess.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select track_uri, anger_value,fear_value, joy_value, sadness_value, suprise_value from track where anger_value is not null");
		int i = 1;
		while(rs.next()){
			String uri = rs.getString("track_uri");
			Double anger = rs.getDouble("anger_value");			
			Double fear = rs.getDouble("fear_value");
			Double joy = rs.getDouble("joy_value");
			Double sadness = rs.getDouble("sadness_value");
			Double suprise = rs.getDouble("suprise_value");
			HashMap<Integer,Double> values = new HashMap<Integer,Double>();
			values.put(1, anger);
			values.put(2,fear);
			values.put(3,joy);
			values.put(4, sadness);
			values.put(5, suprise);	
			Integer keyMax = Collections.max(values.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
			System.out.println(i+": main emotion"+keyMax);
			Statement st2 = con.createStatement();
			st2.executeUpdate("update track set main_emotion = "+keyMax+" where track_uri = '"+uri+"'");
			i++;		
		}
		con.close();
	
	}
	
}
