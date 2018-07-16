package ItemsEnchantment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import io.indico.api.text.Emotion;
import io.indico.api.utils.IndicoException;

public class MyExtractorMultithread implements Runnable {
	private Thread t;
	String name ;
	String offset;
	String limit;
	MyExtractorMultithread(String name, String offset, String limit){
		this.name = name;
		this.offset = offset;
		this.limit = limit;
		System.out.println("Avvio:"+name);
	}
	
	
	public void run(){
		System.out.println("Running " + name);
		try{
		Thread.sleep(100);
		
		final String logfile = "/media/mynewdrive/SHARED/logs.txt";
		DbAccess.initConnection();
		BufferedReader reader = new BufferedReader(new FileReader("/media/mynewdrive/SHARED/completed_playlist.json"));
		String page = "";
	    String pageLine = "";
	    while ((pageLine = reader.readLine()) != null) {
	       page = page+ pageLine;
	    }
	    reader.close();
		JSONObject doc = new JSONObject (page);
		JSONArray playlistArray = doc.getJSONArray("playlists");
		String querySection = " and playlist_id in (", playlist;
		Iterator<Object> it = playlistArray.iterator();
		querySection += it.next().toString().replace("}", "").split(":")[1]+" ";
		while(it.hasNext()){
			querySection += ", "+it.next().toString().replace("}", "").split(":")[1];
		}
		querySection += " ) ";
		
		java.sql.Connection con=DbAccess.getConnection();
		
		String query="select track_uri,track_name, artist_name from spotify_mpd.track join spotify_mpd.playlist_track  on spotify_mpd.track.track_uri = spotify_mpd.playlist_track.track_id where YouTubeView is  null " + querySection+"order by spotify_mpd.playlist_track.playlist_id limit "+offset+","+ limit ;
		System.out.println(query);
		Statement statement=con.createStatement();
		con.setAutoCommit(false);
		ResultSet rs=statement.executeQuery(query);
		int i=0;
		ArrayList<String> keys = new ArrayList<String>();
				
		int keyIndex = 0;
		while(rs.next()) {
			
			i++;
			
			String link_yt,lyrics = "",lowLevel,pathSong,tags, iTunes_info, genre, date;
			boolean isExplicit;
			long YtView;
			int main_emotion, chord, scale, acoustic, aggressive, dancability, electronic, gender, happy, instrumental, rhythm, party, relaxed, sad, timbre, tonal, topPos, weeks;
			float bpm,loudness,diatonicStregth, frequency, tetd, energyRatio;
			String sql="update track set link_yt=?,lyrics=?, lowlevel=?,lastfm_tags=?,iTunes_info=?,genre=?, isExplicit=?, YouTubeView=?,anger_value=?, fear_value=?,joy_value=?,sadness_value=?,suprise_value = ?,number_playlist=?,bpm=?, loudness=?,chord=?,scale=?,tuning_diatonic_strength=?,tuning_equal_tempere_deviation=?,tuning_frequency=?,dancability=?,gender=?,ismir_rhythm=?,acoustic=?,aggressive=?,electronic=?,happy=?,party=?,relaxed=?,sad=?,timbre=?,tonal=?,instrumental=?,topPos=?,weeksInRanking=? where track_uri=?";

			java.sql.PreparedStatement st=con.prepareStatement(sql);
			String spotifyId = rs.getString("track_uri").split(":") [2];
			String uri=rs.getString("track_uri");
			String title=rs.getString("track_name");
			String artist=rs.getString("artist_name");
			System.out.println(i+" "+title + " "+ artist);
			String queryForNumberPlaylist = "SELECT count(*) as number from playlist_track  where track_id = ?";
			java.sql.PreparedStatement nPlSt=con.prepareStatement(queryForNumberPlaylist);
			nPlSt.setString(1, uri);
			//System.out.println("sono prima");
			ResultSet rs2 = nPlSt.executeQuery();
			//System.out.println("sono dopo");
			rs2.next();
			int numberInPlaylist = rs2.getInt("number");
			try {
				
				YoutubeLinker ytl=new  YoutubeLinker(title+ " " + artist);
				link_yt=ytl.getLink();
				YtView = ytl.getViewCount();
				lyrics=GetLyrics.getLyric(WordUtils.capitalize(title), WordUtils.capitalize(artist));
				tags=LastFm.getTopTags(title,artist);
				JSONObject iTunesDoc = iTunesApi.getITunesJSon(title, artist);
				iTunes_info=iTunesApi.getGeneralInfo(iTunesDoc );
				genre=iTunesApi.getMainGenre(iTunesDoc);
				MusicBrainzExtractor audio = new MusicBrainzExtractor(title, artist);
				JSONObject lowlevelJson = audio.getDocumentLL();
				JSONObject highlevelJson = audio.getDocumentHL();
				lowLevel = audio.getDocumentLL().toString();
				
				isExplicit = iTunesApi.isExplicit(iTunesDoc);
				boolean keyIsValid = false;
				Map<Emotion,Double> emotions = null;
				while(!keyIsValid){
					String key=null;
					try{
						try (BufferedReader br = new BufferedReader(new FileReader(new File("/media/mynewdrive/SHARED/indicoKey.txt")))) {
						    String line;
						    while ((line = br.readLine()) != null) {
						       keys.add(line);		       
						    }
						    //System.out.println("-------------------------->"+keys.toString());
						}
						key = keys.get(keyIndex);
						emotions = ExtractEmotional.getIndicoEmotion(key, lyrics);
						keyIsValid = true;
					}catch (IllegalKeyException e) {
						//e.printStackTrace();						
						keyIsValid = false;
						keyIndex++;
					}					
				}						
				
				
				bpm = MusicBrainzLL.getBpm(lowlevelJson);
				loudness = MusicBrainzLL.getAverageLoudness(lowlevelJson);
				chord = MusicBrainzLL.getChord(lowlevelJson);
				scale = MusicBrainzLL.getScale(lowlevelJson);
				diatonicStregth = MusicBrainzLL.getTuningDiatonicStrength(lowlevelJson);
				frequency = MusicBrainzLL.getTuningFrequency(lowlevelJson);
				tetd = MusicBrainzLL.getTuningEqualTemperedDeviation(lowlevelJson);
				energyRatio = MusicBrainzLL.getTuningNontemperedEnergyRatio(lowlevelJson);
				acoustic = MusicBrainzHL.getAcoustic(highlevelJson);
				aggressive = MusicBrainzHL.getAggressive(highlevelJson);
				dancability = MusicBrainzHL.getDancability(highlevelJson);
				electronic = MusicBrainzHL.getElectronic(highlevelJson);
				gender = MusicBrainzHL.getGender(highlevelJson);
				happy = MusicBrainzHL.getHappy(highlevelJson);
				instrumental = MusicBrainzHL.getInstrumental(highlevelJson);
				rhythm = MusicBrainzHL.getIsmirRhythm(highlevelJson);
				party = MusicBrainzHL.getParty(highlevelJson);
				relaxed = MusicBrainzHL.getRelaxed(highlevelJson);
				sad = MusicBrainzHL.getSad(highlevelJson);
				timbre = MusicBrainzHL.getTimbre(highlevelJson);
				tonal = MusicBrainzHL.getTonal(highlevelJson);
				try{
					ExtractChart rank = new  ExtractChart(title,artist);
					
					weeks = rank.getWeekInChart();
					topPos = rank.getTopPeak();
				}catch(Exception e){
					weeks = 0;
					topPos = -1;
				}
								//date = ExtractRanking.getDateTimeTopPos(rank.getObject(), topPos);						
				
				//if(date != null) st.setDate(37, Date.valueOf(date)); else st.setNull(37, java.sql.Types.DATE);
				st.setString(1, link_yt);
				st.setString(2,lyrics);
				st.setString(3, lowLevel);
				st.setString(4,tags);
				st.setString(5,iTunes_info);
				st.setString(6,genre);
				st.setBoolean(7, isExplicit);
				st.setLong(8, YtView);
				st.setDouble(9, emotions.get(Emotion.Anger));
				st.setDouble(10, emotions.get(Emotion.Fear));
				st.setDouble(11, emotions.get(Emotion.Joy));
				st.setDouble(12, emotions.get(Emotion.Sadness));
				st.setDouble(13, emotions.get(Emotion.Surprise));				
				st.setInt(14, numberInPlaylist);
				st.setDouble(15, bpm);
				st.setDouble(16, loudness);
				st.setInt(17, chord);
				st.setInt(18, scale);
				st.setDouble(19, diatonicStregth);
				st.setDouble(20, tetd );
				st.setDouble(21, frequency);
				st.setInt(22, dancability);
				st.setInt(23,gender );
				st.setInt(24, rhythm);
				st.setInt(25,acoustic );
				st.setInt(26, aggressive);
				st.setInt(27,electronic);
				st.setInt(28, happy);
				st.setInt(29, party);
				st.setInt(30, relaxed );
				st.setInt(31,sad );
				st.setInt(32,timbre);
				st.setInt(33,tonal );
				st.setInt(34,instrumental);
				st.setInt(35, topPos);
				st.setInt(36, weeks);
				//st.setDate(39, Date.valueOf(date));
				st.setString(37, uri);					
			}catch (Exception e) {
				e.printStackTrace();
				FileWriter w = new FileWriter(logfile,true);
				BufferedWriter b = new BufferedWriter (w);
				b.write(title + " "+ artist+ ":\n"+ e.getMessage()+" \n Trace:\n"+" "+ e.getStackTrace()[0].toString()+"\n\n");
				b.close();
				//st.executeUpdate();
				if ((i % 10) == 0 || rs.isLast()) {					
					con.commit() ;
					System.out.println("commit");
				}
				continue;
			}	
			try{
				st.executeUpdate();	
				if ((i % 10) == 0 || rs.isLast() ) {
					System.out.println("commit");
					con.commit() ;
				}
			}catch(Exception e){
				e.printStackTrace();
				FileWriter wr = new FileWriter(logfile,true);
				BufferedWriter bf = new BufferedWriter (wr);
				bf.write(title + " "+ artist+ ": \n"+ e.getMessage()+" \n Trace:\n"+ e.getStackTrace()[0].toString()+"\n\n");
				bf.close();				
			}			
		}
		DbAccess.closeConnection();			
		}catch(ClassNotFoundException|SQLException| IOException | InterruptedException e){
			e.printStackTrace();
	}		
		
	}
	
	public void start () {
	      System.out.println("Starting thread "+ name );
	      if (t == null) {
	         t = new Thread (this);
	         t.start ();
	      }
	   }	
	
}


