package RecommendationAPC;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.collect.Iterables;

public class ClassificatorNoLL {
	private static final String FILE_NAME = "/media/mynewdrive/SHARED/results_NoLL_Freq_RPrec.csv";	
	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
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
		String query = "SELECT * FROM track join playlist_track on track.track_uri = playlist_track.track_id where playlist_id =?";
		PreparedStatement pst = con.prepareStatement(query);
		FileWriter file = new FileWriter(FILE_NAME);
		file.close();
		file = new FileWriter(FILE_NAME,true);
		file.write("id,map@1,precision@1,recall@1,f1@1,map@5,precision@5,recall@5,f1@5,map@10,precision@10,recall@10,f1@10,map@20,precision@20,recall@20,f1@20,map@50,precision@50,recall@50,f1@50,find,Nascoste,R-Prec");
		file.flush();
					
		ArrayList<String> genres = new ArrayList<String>();
		ArrayList <Integer> chords = new ArrayList<Integer>();
		ArrayList<Integer> rhythm = new ArrayList<Integer>();
		ArrayList<Double> bpm = new ArrayList<Double>();
		ArrayList <Double> loudness = new ArrayList<Double>();
		ArrayList <Double> tds = new ArrayList<Double>();
		ArrayList <Double> tetd = new ArrayList<Double>();
		ArrayList <Double> tf = new ArrayList<Double>();
		ArrayList <Double> duration = new ArrayList<Double>();
		ArrayList <Integer> dancability = new ArrayList<Integer>();
		ArrayList <Integer> gender = new ArrayList<Integer>();
		ArrayList <Integer> acoustic = new ArrayList<Integer>();
		ArrayList <Integer> aggressive = new ArrayList<Integer>();
		ArrayList <Integer> electronic = new ArrayList<Integer>();
		ArrayList <Integer> happy = new ArrayList<Integer>();
		ArrayList <Integer> party = new ArrayList<Integer>();
		ArrayList <Integer> relaxed = new ArrayList<Integer>();
		ArrayList <Integer> sad= new ArrayList<Integer>();
		ArrayList <Integer> timbre= new ArrayList<Integer>();
		ArrayList <Integer> tonal = new ArrayList<Integer>();
		ArrayList <Integer> instrumental = new ArrayList<Integer>();
		ArrayList <Integer> explicit= new ArrayList<Integer>();
		ArrayList<Integer> mainEmotions = new ArrayList<Integer>()	;
		ArrayList<Integer> years = new ArrayList<Integer>();
		TagSelector tags = new TagSelector();
		ArrayList<String> testingSet = new ArrayList<String>();
		ArrayList<String> trainingSet = new ArrayList<String>();
		GeneralSelector<String> stringSelector = new GeneralSelector<String>();
		GeneralSelector<Integer> intSelector = new GeneralSelector<Integer>();
		ResultSet rs, rsSearch = null;
		ArrayList<String> searchResultWithoutTrain = new ArrayList<String>();
 		while (tempIterator.hasNext()){//ciclo playlist complete
			String playlistId = tempIterator.next().toString().replace("}", "").split(":")[1];
			System.out.println("Playlist id : "+playlistId);
			pst.setString(1,playlistId);
			rs = pst.executeQuery();
			genres.clear();
			chords.clear();
			rhythm.clear();
			bpm.clear();
			loudness.clear();
			tds.clear();
			tetd.clear();
			tf.clear();
			duration.clear();
			dancability.clear();
			gender.clear();
			acoustic.clear();
			aggressive.clear();
			electronic.clear();
			happy.clear();
			party.clear();
			relaxed.clear();
			sad.clear();
			timbre.clear();
			tonal.clear();
			instrumental.clear();
			explicit.clear();
			mainEmotions.clear();
			years.clear();
			tags.clearSelector();
			testingSet.clear();
			trainingSet.clear();
			
			while (rs.next()){//ciclo tracce playlist
				if (rs.getInt("test") == 0){
					trainingSet.add(rs.getString("track_uri"));
					double bpmValue = rs.getDouble("bpm");
					if (bpmValue != -1)bpm.add(bpmValue);
					double loudnessValue = rs.getDouble("loudness");
					if (loudnessValue != 0)loudness.add(loudnessValue);
					double tdsValue = rs.getDouble("tuning_diatonic_strength");
					if(tdsValue != -1)tds.add(tdsValue);
					double tetdValue = rs.getDouble("tuning_equal_tempere_deviation");
					if (tetdValue != -1) tetd.add(tetdValue);
					double frequency = rs.getDouble("tuning_frequency");
					if (frequency != -1)tf.add(frequency);
					double durationValue=(double)rs.getInt("duration_ms");
					if(durationValue != -1) duration.add(durationValue);
					String tagsValues = rs.getString("lastfm_tags");
					if (tagsValues != null){
						String[] tagsTrack = tagsValues.split(",");
						tags.incrementNumTrack();
						for(int i =0; i< tagsTrack.length; i++){
							tags.addTag(tagsTrack[i]);
						}
					}
					
					String genre = rs.getString("genre");
					genres.add(genre);
					int chord = rs.getInt("chord");
					chords.add(chord);
					int rhythmValue = rs.getInt("ismir_rhythm");
					rhythm.add(rhythmValue);
					int dance = rs.getInt("dancability");
					dancability.add(dance);
					int genderValue = rs.getInt("gender");
					gender.add(genderValue);
					int aco = rs.getInt("acoustic");
					acoustic.add(aco);
					int aggressiveValue = rs.getInt("aggressive");
					aggressive.add(aggressiveValue);
					int electronicValue = rs.getInt("electronic");
					electronic.add(electronicValue);
					int happyValue = rs.getInt("happy");
					happy.add(happyValue);
					int partyValue = rs.getInt("party");
					party.add(partyValue);
					int relax = rs.getInt("relaxed");
					relaxed.add(relax);
					int sadValue = rs.getInt("sad");
					sad.add(sadValue);
					int timbreValue = rs.getInt("timbre");
					timbre.add(timbreValue);
					int tonalValue = rs.getInt("tonal");
					tonal.add(tonalValue);
					int instrumentalValue = rs.getInt("instrumental");
					instrumental.add(instrumentalValue);
					int explicitValue = rs.getInt("isExplicit");
					explicit.add(explicitValue);
					int year = rs.getInt("releaseDate");
					if (year != 0) years.add(year);
					int mainEmotion = rs.getInt("main_emotion");
					if (mainEmotion != 0) mainEmotions.add(mainEmotion);					
				}else{
					testingSet.add(rs.getString("track_uri"));
				}
				
				
			}
			/*
			rs.close();
			Double bpms [] = new Double[bpm.size()];
			bpms = bpm.toArray(bpms);
			double coeffBpm = MathUtils.CoeffVar(bpms);
			String queryBpm = "";
			if (coeffBpm <= 0.5){
				double avg =MathUtils.avg(bpms);
				double sd = MathUtils.calculateSD(bpms);
				double min = avg-sd;
				double max = avg+sd;
				queryBpm = "bpm between "+ min +" and "+max+ " or bpm = -1";
			}
			
			String queryLoudness="";
			Double loud [] = new Double[bpm.size()];
			loud = loudness.toArray(loud);
			double coeffLoudness = MathUtils.CoeffVar(loud);
			if (coeffLoudness <= 0.5){
				double avg =MathUtils.avg(loud);
				double sd = MathUtils.calculateSD(loud);
				double min = avg-sd;
				double max = avg+sd;
				queryLoudness = "loudness between "+ min +" and "+max+" or loudness = -1";
			}
			
			String queryTDS="";
			Double tdss [] = new Double[tds.size()];
			tdss = tds.toArray(tdss);
			double coeffTds = MathUtils.CoeffVar(tdss);
			if (coeffTds <= 0.5){
				double avg = MathUtils.avg(tdss);
				double sd = MathUtils.calculateSD(tdss);
				double min = avg-sd;
				double max = avg+sd;
				queryTDS = "tuning_diatonic_strength between "+ min +" and "+max+ " or tuning_diatonic_strength = -1";
			}
			
			String queryTETD="";
			Double tetds [] = new Double[tetd.size()];
			tetds = tetd.toArray(tetds);
			double coeffTetds = MathUtils.CoeffVar(tetds);
			if (coeffTetds <= 0.5){
				double avg = MathUtils.avg(tetds);
				double sd = MathUtils.calculateSD(tetds);
				double min = avg-sd;
				double max = avg+sd;
				queryTETD = "tuning_equal_tempere_deviation between "+ min +" and "+max+ " or tuning_equal_tempere_deviation = -1";
			}
			
			String queryTf="";
			Double tfs [] = new Double[tf.size()];
			tfs = tf.toArray(tfs);
			double coeffTf = MathUtils.CoeffVar(tfs);
			if (coeffTf <= 0.5){
				double avg = MathUtils.avg(tfs);
				double sd = MathUtils.calculateSD(tfs);
				double min = avg-sd;
				double max = avg+sd;
				queryTf = "tuning_frequency between "+ min +" and "+max +" or tuning_frequency = -1";
			}
			
			String queryDuration="";
			Double durations [] = new Double[duration.size()];
			durations = duration.toArray(durations);
			double coeffDuration = MathUtils.CoeffVar(durations);
			if (coeffDuration <= 0.5){
				double avg = MathUtils.avg(durations);
				double sd = MathUtils.calculateSD(durations);
				double min = avg-sd;
				double max = avg+sd;
				queryDuration = "duration_ms between "+ min +" and "+max;
			}
			*/
			tags.computePerc();
			ArrayList<Tag> topTags = tags.getTopTags(30);
			String queryTags ="";
			int size = topTags.size();
			
			for (int i = 0; i < topTags.size(); i++){
				if (i == 0 && size > 0 ){
					queryTags += "lastfm_tags = '' or lastfm_tags like '%"+topTags.get(i).getName()+"%'";
				}else if(i != 0 && size > 0){
					queryTags += " or lastfm_tags like '%"+ topTags.get(i).getName()+"%'";
				}				
			}
			
			
			HashSet<String> topGenres = stringSelector.getTopItems(genres, 40);
			System.out.println("Genere "+genres);
			Iterator<String> it = topGenres.iterator();
			String queryGenre = "genre = ";
			while(it.hasNext()){
				String s = it.next();
				s = s.replace("'", "''");
				if (it.hasNext()){
					queryGenre += "'"+s+"' or genre =";
				}else{
					queryGenre += "'"+s+"' or genre = ''";
				}
			}
			
			
			HashSet<Integer> topEmotion = intSelector.getTopItems(mainEmotions,40);
			
			Iterator<Integer> itEmotion = topEmotion.iterator();
			String queryEmotions = "main_emotion = ";
			while(itEmotion.hasNext()){
				Integer s = itEmotion.next();
				if(itEmotion.hasNext()){
					queryEmotions += s + " or main_emotion = ";					
				}else{
					queryEmotions += s + " ";
				}
			}
			
			/*
			HashSet<Integer> topChord =  intSelector.getTopItems(chords, 40);
			Iterator<Integer> itChord = topChord.iterator();
			String queryChord = "chord = ";
			while(itChord.hasNext()){
				Integer s = itChord.next();
				if (itChord.hasNext()){
					queryChord += "'"+s.toString()+"' or chord = ";
				}else{
					queryChord += "'"+s.toString()+"' ";
				}
			}
			*/
		
			//System.out.println("ritmo "+ rhythm);
			HashSet<Integer> topRhythm = intSelector.getTopItems(rhythm, 40);
			Iterator<Integer> itRhythm = topRhythm.iterator();
			String queryRhythm = "ismir_rhythm = ";
			while(itRhythm.hasNext()){
				Integer s = itRhythm.next();
				if (itRhythm.hasNext()){
					queryRhythm += s+" or ismir_rhythm = ";
				}else{
					queryRhythm += " "+s+" ";
				}
			}
			
			
			HashSet<Integer> topDancability = intSelector.getTopItems(dancability, 80);
			System.out.println("Dancability "+dancability);
			Iterator<Integer> itDancability = topDancability.iterator();
			String queryDancability = "dancability = ";
			if(itDancability.hasNext()){
				queryDancability += itDancability.next().toString();
				
			}
			
			
			HashSet<Integer> topGender=intSelector.getTopItems(gender, 80);
			Iterator<Integer> itGender = topGender.iterator();
			String queryGender = "gender = ";
			if(itGender.hasNext()){
				queryGender += itGender.next().toString();
			}
			
			
			HashSet<Integer> topAcoustic = intSelector.getTopItems(acoustic, 80);
			Iterator<Integer> itAcoustic = topAcoustic.iterator();
			String queryAcoustic = "acoustic = ";
			if(itAcoustic.hasNext()){
				queryAcoustic += itAcoustic.next().toString();
			}
			
			
			HashSet<Integer> topAggressive =  intSelector.getTopItems(aggressive, 80);
			Iterator<Integer> itAggressive = topAggressive.iterator();
			String queryAggressive = "aggressive = ";
			if(itAggressive.hasNext()){
				queryAggressive += itAggressive.next().toString();
			}
			
			
			HashSet<Integer> topElectronic = intSelector.getTopItems(electronic, 80);
			Iterator<Integer> itElectronic = topElectronic.iterator();
			String queryElectronic = "electronic = ";
			if(itElectronic.hasNext()){
				queryElectronic += itElectronic.next().toString();
			}
			
			
			HashSet<Integer> topHappy = intSelector.getTopItems(happy, 80);
			Iterator<Integer> itHappy = topHappy.iterator();
			String queryHappy = "happy = ";
			if(itHappy.hasNext()){
				queryHappy += itHappy.next().toString();
			}
			
			
			HashSet<Integer> topParty = intSelector.getTopItems(party, 80);
			Iterator<Integer> itParty = topParty.iterator();
			String queryParty = "happy = ";
			if(itParty.hasNext()){
				queryParty += itParty.next().toString();
			}
			
			
			HashSet<Integer> topRelax = intSelector.getTopItems(relaxed, 80);
			Iterator<Integer> itRelax = topRelax.iterator();
			String queryRelax = "relaxed = ";
			if(itRelax.hasNext()){
				queryRelax += itRelax.next().toString();
			}
			
			
			HashSet<Integer> topSad =  intSelector.getTopItems(sad, 80);
			Iterator<Integer> itSad = topSad.iterator();
			String querySad = "sad = ";
			if(itSad.hasNext()){
				querySad += itSad.next().toString();
			}
			
			
			HashSet<Integer> topTimbre = intSelector.getTopItems(timbre, 80);
			Iterator<Integer> itTimbre = topTimbre.iterator();
			String queryTimbre = "timbre = ";
			if(itTimbre.hasNext()){
				queryTimbre += itTimbre.next().toString();
			}
			
			
			HashSet<Integer> topTonal = intSelector.getTopItems(tonal, 80);
			Iterator<Integer> itTonal = topTonal.iterator();
			String queryTonal = "tonal = ";
			if(itTonal.hasNext()){
				queryTonal += itTonal.next().toString();
			}
			
			
			HashSet<Integer> topInstrumental = intSelector.getTopItems(instrumental, 80);
			Iterator<Integer> itInstrumental = topInstrumental.iterator();
			String queryInstrumental = "instrumental = ";
			if(itInstrumental.hasNext()){
				queryInstrumental += itInstrumental.next().toString();
			}
			
			
			HashSet<Integer> topExplicit = intSelector.getTopItems(explicit, 80);
			Iterator<Integer> itExplicit = topExplicit.iterator();
			String queryExplicit = "isExplicit = ";
			if(itExplicit.hasNext()){
				queryExplicit += itExplicit.next().toString();
			}
			boolean yearsRangeValid = false;
			int minRange = 0, maxRange = 0;
			try{
				System.out.println("anni "+years.toString());
				int minYear = Collections.min(years);
				int maxYear = Collections.max(years);
				
				
				if ((maxYear - minYear) <= 20){
					yearsRangeValid = true;
					minRange = minYear - 5;
					maxRange = maxYear + 5;
				}
			}catch(NoSuchElementException e){
				yearsRangeValid = false;
			}
			
			
			String searchQuery = "select * from track where ";
			/*
			if (!queryBpm.equals("")) searchQuery += "("+queryBpm+")";
			if (!queryLoudness.equals("")) searchQuery += "("+queryLoudness+")";
			if (!queryTDS.equals("")) searchQuery +="("+queryTDS+")";
			if (!queryTETD.equals("")) searchQuery += "("+queryTETD+")";
			if (!queryTf.equals("")) searchQuery += "("+queryTf+")";
			if (!queryDuration.equals("")) searchQuery += "("+queryDuration+")";
			*/
			if (!topTags.isEmpty()) searchQuery += "("+queryTags+")";
			if (!topGenres.isEmpty())searchQuery += "("+queryGenre+")";
			if (!topEmotion.isEmpty()) searchQuery += "("+queryEmotions+")";
			//if (!topChord.isEmpty()) searchQuery += "(" + queryChord+")";
			if (!topRhythm.isEmpty())searchQuery += "("+ queryRhythm + ")";
			if (!topDancability.isEmpty())searchQuery += "("+queryDancability+")";
			if (!topGender.isEmpty()) searchQuery += "("+queryGender +")";
			if (!topAcoustic.isEmpty())searchQuery += "("+queryAcoustic+")";
			if (!topAggressive.isEmpty()) searchQuery += "("+queryAggressive+")";
			if (!topElectronic.isEmpty()) searchQuery += "("+queryElectronic+")";
			if (!topHappy.isEmpty()) searchQuery += "("+queryHappy+")";
			if (!topParty.isEmpty()) searchQuery += "("+queryParty + ")";
			if (!topRelax.isEmpty()) searchQuery += "("+queryRelax + ")";
			if (!topSad.isEmpty()) searchQuery += "("+querySad +")";
			if (!topTimbre.isEmpty()) searchQuery += "("+queryTimbre+")";
			if (!topTonal.isEmpty()) searchQuery += "("+queryTonal+")";
			if (!topInstrumental.isEmpty()) searchQuery += "("+queryInstrumental+")";
			if (!topExplicit.isEmpty()) searchQuery += "("+queryExplicit+")";
			if (yearsRangeValid) searchQuery += "( releaseDate between "+minRange +" and "+ maxRange+")";
			searchQuery += " order by frequency desc limit 50";
			searchQuery = searchQuery.replace(")(", ") and (");
			System.out.println(searchQuery);
			try{
				rsSearch = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(searchQuery);
			}catch(Exception e){
				continue;
			}
			
			searchResultWithoutTrain.clear();
			while(rsSearch.next()){
				if (!trainingSet.contains(rsSearch.getString("track_uri"))) searchResultWithoutTrain.add(rsSearch.getString("track_uri")); //cancella le tracce del training set. Queste non verranno considerate nella valutazione
			}
			//rsSearch.last();
			int resultSetSize = searchResultWithoutTrain.size();
			//rsSearch.beforeFirst();
			double map1=0.0, precision1 = 0.0,recall1 = 0.0, f1at1=0.0,map5=0.0, precision5 = 0.0,recall5 = 0.0, f1at5=0.0,map10=0.0, precision10 = 0.0,recall10= 0.0, f1at10=0.0,map20=0.0, precision20 = 0.0,recall20 = 0.0, f1at20=0.0, map50=0.0, precision50 = 0.0,recall50 = 0.0, f1at50=0.0;
			int pos = 0;
			int find = 0;
			double map = 0.0;
			int testingSize = testingSet.size();
			double changeRecall = 1.0/(double)testingSize;
			Iterator<String> itResult = searchResultWithoutTrain.iterator(); 
			double rPrec = 0.0;
			while(itResult.hasNext()){
				pos++;
				if (testingSet.contains(itResult.next())) {
					find++;
					map += ((double)find/(double)(pos))*changeRecall;	
				}					
				if (pos == testingSize) rPrec = (double)find/(double)testingSize;
				
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
							
		
		file.write("\n"+playlistId+","+map1+","+precision1+","+recall1+","+f1at1+","+map5+","+precision5+","+recall5+","+f1at5+","+map10+","+precision10+","+recall10+","+f1at10+","+map20+","+precision20+","+recall20+","+f1at20+","+map50+","+precision50+","+recall50+","+f1at50+","+find+","+testingSize +","+ rPrec);
		file.flush();
		rsSearch.close();
			
		}
 		file.close();
		
		
	}
	
}
