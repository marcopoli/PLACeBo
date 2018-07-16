package RecommendationAPC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import RecommendationAPC.ClassificatorKnn.Candidate;

public class ClassificatorKnn {	
	
	private static final String FILE_NAME = "/media/mynewdrive/SHARED/Knn3.csv";
	static class Candidate implements Comparable <Candidate>{
		final String trackId;
		double similarity;
		Candidate (String id){
			this.trackId = id;
			
		}
		@Override
		public int compareTo(Candidate c) {
			return Double.compare(this.similarity,c.similarity);			
		}
		/**
		 * Calcola la similarità del coseno tra due vettori.
		 * @param vectorA
		 * @param vectorB
		 */
		public void cosineSimilarity(double[] vectorA, double[] vectorB) {
			    double dotProduct = 0.0;
			    double normA = 0.0;
			    double normB = 0.0;
			    for (int i = 0; i < vectorA.length; i++) {
			        dotProduct += vectorA[i] * vectorB[i];
			        normA += Math.pow(vectorA[i], 2);
			        normB += Math.pow(vectorB[i], 2);
			    }   
			    this.similarity = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
			}
		}
		
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		FileWriter file = new FileWriter(FILE_NAME);
		file.close();
		file = new FileWriter(FILE_NAME,true);
		file.write("id,map@1,precision@1,recall@1,f1@1,map@5,precision@5,recall@5,f1@5,map@10,precision@10,recall@10,f1@10,map@20,precision@20,recall@20,f1@20,map@50,precision@50,recall@50,f1@50");
		file.flush();
		BufferedReader reader = new BufferedReader(new FileReader("/media/mynewdrive/SHARED/PlayLists2.json"));
		String page = "";
	    String line = "";
	    while ((line = reader.readLine()) != null) {
	       page = page+ line;
	    }
	    reader.close();
		JSONObject doc = new JSONObject (page);
		JSONArray playlistArray = doc.getJSONArray("playlists");		
		Iterator<Object> tempIterator = playlistArray.iterator();
		Iterator<Object> plIterator = playlistArray.iterator();
		DbAccess.initConnection();
		Connection con = DbAccess.getConnection();
		Statement st = con.createStatement();
		String queryAllItem = "Select  * from track join playlist_track on track.track_uri = playlist_track.track_id where YouTubeView is not null and playlist_id in (";
		while(tempIterator.hasNext()){
			Object next = tempIterator.next();
			if(tempIterator.hasNext()){
				queryAllItem += next.toString().replace("}", "").split(":")[1]+",";
			}else{
				queryAllItem += next.toString().replace("}", "").split(":")[1]+")";
			}	
			
		}
		System.out.println(queryAllItem);		
		ResultSet allItems = st.executeQuery(queryAllItem);
		System.out.println("Tracce selezionate");
		double[] a = new double[25];
		double[] b = new double[25];
		ArrayList<String> searchResultWithoutTrain = new ArrayList<String>();
				
		PreparedStatement st2 = con.prepareStatement("SELECT * FROM track join playlist_track on track.track_uri = playlist_track.track_id where playlist_id = ?");
		ResultSet rs;
		
		ArrayList<Candidate> candidatesItems;
		ArrayList<Candidate> allCandidates;
		ArrayList<String> testing;
		ArrayList<String> training;
		
		while (plIterator.hasNext()){//scorro le playlist
			allCandidates = new ArrayList<Candidate>();
			String playlistId = plIterator.next().toString().replace("}", "").split(":")[1];
			System.out.println("Playlist id : " + playlistId);
			st2.setString(1, playlistId);
			rs = st2.executeQuery();//seleziono le tracce della playlist
			testing = new ArrayList<String>();
			training = new ArrayList<String>();	
			Set<Candidate> noDuplicate;
			while(rs.next()){	//scorro le tracce della playlist							
				if (rs.getInt("test") == 1) testing.add(rs.getString("track_uri"));
				else{
					candidatesItems = new ArrayList<Candidate>();
					training.add(rs.getString("track_uri"));
					a[0] = rs.getDouble("duration_ms");
					a[1]= rs.getDouble("isExplicit");
					a[2]= rs.getDouble("YouTubeView");
					a[3] = rs.getInt("main_emotion");
					a[4] = rs.getDouble("number_playlist");
					a[5] = rs.getDouble("loudness");
					a[6] = rs.getDouble("chord");
					a[7] = rs.getDouble("scale");
					a[8] = rs.getDouble("tuning_equal_tempere_deviation");
					a[9] = rs.getDouble("tuning_frequency");
					a[10] = rs.getDouble("dancability");
					a[11] = rs.getDouble("gender");
					a[12] = rs.getDouble("ismir_rhythm");
					a[13] = rs.getDouble("acoustic");
					a[14] = rs.getDouble("aggressive");
					a[15] = rs.getDouble("electronic");
					a[16] = rs.getDouble("electronic");
					a[17] = rs.getDouble("happy");
					a[18] = rs.getDouble("party");
					a[19] = rs.getDouble("relaxed");
					a[20] = rs.getDouble("sad");
					a[21] = rs.getDouble("timbre");
					a[22] = rs.getDouble("tonal");
					a[23] = rs.getDouble("instrumental");
					a[24] = rs.getDouble("weeksInRanking");
					allItems.beforeFirst();
					while(allItems.next()){//scorro tutte le tracce del dataset
						b[0] = allItems.getDouble("duration_ms");
						b[1]= allItems.getDouble("isExplicit");
						b[2]= allItems.getDouble("YouTubeView");
						b[3] = allItems.getDouble("main_emotion");
						b[4] = allItems.getDouble("number_playlist");
						b[5] = allItems.getDouble("loudness");
						b[6] = allItems.getDouble("chord");
						b[7] = allItems.getDouble("scale");
						b[8] = allItems.getDouble("tuning_equal_tempere_deviation");
						b[9] = allItems.getDouble("tuning_frequency");
						b[10] = allItems.getDouble("dancability");
						b[11] = allItems.getDouble("gender");
						b[12] = allItems.getDouble("ismir_rhythm");
						b[13] = allItems.getDouble("acoustic");
						b[14] = allItems.getDouble("aggressive");
						b[15] = allItems.getDouble("electronic");
						b[16] = allItems.getDouble("electronic");
						b[17] = allItems.getDouble("happy");
						b[18] = allItems.getDouble("party");
						b[19] = allItems.getDouble("relaxed");
						b[20] = allItems.getDouble("sad");
						b[21] = allItems.getDouble("timbre");
						b[22] = allItems.getDouble("tonal");
						b[23] = allItems.getDouble("instrumental");
						b[24] = allItems.getDouble("weeksInRanking");
						if (allItems.getString("track_uri") != rs.getString("track_uri")){
							Candidate c = new Candidate(allItems.getString("track_uri"));
							c.cosineSimilarity(a,b);
							candidatesItems.add(c);						
						}
						
					}
					noDuplicate = new HashSet<Candidate>(candidatesItems);
					candidatesItems = new ArrayList<Candidate>(noDuplicate);
					Collections.reverse(candidatesItems);//ordino i candidati in base alla loro distanza dalla traccia
					for (int i = 0; i < 3; i++){//seleziono i primi 10 candidati con similarità più alta
						allCandidates.add(candidatesItems.get(i));
					}
								
				}	
				
			}
			Collections.reverse(allCandidates);
			int numCandidate = allCandidates.size();
			//Iterator<Candidate> candidateIterator = candidatesItems.iterator();
			
			searchResultWithoutTrain.clear();
			for (int i = 0; i < numCandidate; i++ ){
				if(!training.contains(allCandidates.get(i).trackId)) searchResultWithoutTrain.add(allCandidates.get(i).trackId);
			}
						
			int resultSetSize = searchResultWithoutTrain.size();
			//rsSearch.beforeFirst();
			double map1=0.0, precision1 = 0.0,recall1 = 0.0, f1at1=0.0,map5=0.0, precision5 = 0.0,recall5 = 0.0, f1at5=0.0,map10=0.0, precision10 = 0.0,recall10= 0.0, f1at10=0.0,map20=0.0, precision20 = 0.0,recall20 = 0.0, f1at20=0.0, map50=0.0, precision50 = 0.0,recall50 = 0.0, f1at50=0.0;
			int pos = 0;
			int find = 0;
			double map = 0.0;
			int testingSize = testing.size();
			double changeRecall = 1.0/(double)testingSize;
			int resultsItemNum = searchResultWithoutTrain.size();
			//Iterator<String> secondCandidateIterator = searchResultWithoutTrain.iterator();
			//while(secondCandidateIterator.hasNext()){
			for(int i = 0;i < resultsItemNum && i < 50;i++){
				pos++;
				if (testing.contains(searchResultWithoutTrain.get(i))) {
					find++;
					map += ((double)find/(double)(pos))*changeRecall;	
				}					
								
				if (pos == 1){
					precision1 = (double)find/(double)pos;
					recall1 = (double)find/(double)testingSize;
					if (precision1 != 0.0 && recall1 != 0.0)f1at1 = (2.0*(precision1*recall1))/(precision1+recall1);
					map1 = map;
				}
				if (pos == 5 || (pos < 5 && pos == resultSetSize)){
					precision5 = (double)find/(double)pos;
					recall5 = (double)find/(double)testingSize;
					if (precision5 != 0.0 && recall5 != 0.0)f1at5 = (2.0*(precision5*recall5))/(precision5+recall5);
					map5 = map;
				}
				if (pos == 10|| (pos < 10 && pos == resultSetSize)){
					precision10 = (double)find/(double)pos;
					recall10 = (double)find/(double)testingSize;
					if (precision10 != 0.0 && recall10 != 0.0)f1at10 = (2.0*(precision10*recall10))/(precision10+recall10);
					map10 = map;
				}
				if (pos == 20 || (pos < 20 && pos == resultSetSize)){
					precision20 = (double)find/(double)pos;
					recall20 = (double)find/(double)testingSize;
					if (precision20 != 0.0 && recall20 != 0.0)f1at20 = (2.0*(precision20*recall20))/(precision20+recall20);
					map20 = map;
				}
				if (pos == 50 || (pos < 50 && pos == resultSetSize)){
					precision50 = (double)find/(double)pos;
					recall50 = (double)find/(double)testingSize;
					if (precision50 != 0.0 && recall50 != 0.0)f1at50 = (2.0*(precision50*recall50))/(precision50+recall50);
					map50 = map;
				}						
				
			}	
			file.write("\n"+playlistId+","+map1+","+precision1+","+recall1+","+f1at1+","+map5+","+precision5+","+recall5+","+f1at5+","+map10+","+precision10+","+recall10+","+f1at10+","+map20+","+precision20+","+recall20+","+f1at20+","+map50+","+precision50+","+recall50+","+f1at50+","+","+find+","+resultSetSize);
			file.flush();
			
		}
		file.close();
		con.close();
	}
}
