package ItemsEnchantment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONObject;

import com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.ANGLE_instanced_arrays;

import io.indico.api.text.Emotion;
import io.indico.api.utils.IndicoException;

public class MyExtractor {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		final String logfile = "/media/mynewdrive/SHARED/logs.txt";
		DbAccess.initConnection();
		java.sql.Connection con=DbAccess.getConnection();
		String query="select track_uri,track_name, artist_name from spotify_mpd.track join spotify_mpd.playlist_track  on spotify_mpd.track.track_uri = spotify_mpd.playlist_track.track_id where spotify_mpd.track.YouTubeView is null order by spotify_mpd.playlist_track.playlist_id ";
		Statement statement=con.createStatement();
		ResultSet rs=statement.executeQuery(query);
		int i=0;
		ArrayList<String> keys = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(new File("/media/mynewdrive/SHARED/indicoKey.txt")))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       keys.add(line);
		    }
		}
		int keyIndex = 0;
		while(rs.next()) {
			
			i++;			
			String link_yt,lyrics,lowLevel,pathSong,tags, iTunes_info, genre, date;
			boolean isExplicit;
			long YtView;
			int main_emotion, chord, scale, acoustic, aggressive, dancability, electronic, gender, happy, instrumental, rhythm, party, relaxed, sad, timbre, tonal, topPos, weeks;
			float bpm,loudness,diatonicStregth, frequency, tetd, energyRatio;
			String sql="update track set link_yt=?,lyrics=?, lowlevel=?,lastfm_tags=?,iTunes_info=?,genre=?, isExplicit=?, YouTubeView=?,anger_value=?, fear_value=?,joy_value=?,sadness_value=?,number_playlist=?,bpm=?, loudness=?,chord=?,scale=?,tuning_diatonic_strength=?,tuning_equal_tempere_deviation=?,tuning_frequency=?,dancability=?,gender=?,ismir_rhythm=?,acoustic=?,aggressive=?,electronic=?,happy=?,party=?,relaxed=?,sad=?,timbre=?,tonal=?,instrumental=?,topPos=?,weeksInRanking=?,DateTopRanking=? where track_uri=?";
			java.sql.PreparedStatement st=con.prepareStatement(sql);
			String spotifyId = rs.getString("track_uri").split(":") [2];
			String uri=rs.getString("track_uri");
			String title=rs.getString("track_name");
			String artist=rs.getString("artist_name");
			System.out.println(i+" "+title + " "+ artist);
			String queryForNumberPlaylist = "SELECT count(*) as number from playlist_track  where track_id = ?";
			java.sql.PreparedStatement nPlSt=con.prepareStatement(queryForNumberPlaylist);
			nPlSt.setString(1, uri);
			ResultSet rs2 = nPlSt.executeQuery();
			rs2.next();
			int numberInPlaylist = rs2.getInt("number");
			try {
				
				YoutubeLinker ytl=new  YoutubeLinker(title+ " " + artist);
				link_yt=ytl.getLink();
				YtView = ytl.getViewCount();
				lyrics=GetLyrics.getLyric(title, artist);
				tags=LastFm.getTopTags(title,artist);
				JSONObject iTunesDoc = iTunesApi.getITunesJSon(title, artist);
				iTunes_info=iTunesApi.getGeneralInfo(iTunesDoc );
				genre=iTunesApi.getMainGenre(iTunesDoc);
				MusicBrainzExtractor audio = new MusicBrainzExtractor(title, artist);
				JSONObject lowlevelJson = audio.getDocumentLL();
				JSONObject highlevelJson = audio.getDocumentHL();
				lowLevel = lowlevelJson.toString();
				YtView=ytl.getViewCount();
				isExplicit = iTunesApi.isExplicit(iTunesDoc);
				boolean keyIsValid = false;
				Map<Emotion,Double> emotions = null;
				while(!keyIsValid){
					try{
						emotions = ExtractEmotional.getIndicoEmotion(keys.get(keyIndex), lyrics);
						keyIsValid = true;
					}catch (IndicoException e) {
						keyIsValid = false;
						keyIndex++;
					}					
				}				
				//ExtractEmotional emotion = new ExtractEmotional(lyrics);
				//main_emotion = emotion.getMainEmotion();
				//ArrayList<Float> emotionsValue = emotion.getEmotionsValue();
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
				
				ExtractRanking rank = new  ExtractRanking(title, spotifyId );
				
				weeks = rank.getnumberOfWeek();
				topPos = rank.getTopPos();
				date = ExtractRanking.getDateTimeTopPos(rank.getObject(), topPos);
								
				con.setAutoCommit(false);
				if(date != null) st.setDate(37, Date.valueOf(date)); else st.setNull(37, java.sql.Types.DATE);
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
				st.setDouble(13,emotions.get(Emotion.Surprise));
				st.setInt(14, numberInPlaylist);
				st.setFloat(15, bpm);
				st.setFloat(16, loudness);
				st.setInt(17, chord);
				st.setInt(18, scale);
				st.setFloat(19, diatonicStregth);
				st.setFloat(20, tetd );
				st.setFloat(21, frequency);
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
				st.setString(38, uri);				
			}catch (Exception e) {
				e.printStackTrace();
				FileWriter w = new FileWriter(logfile,true);
				BufferedWriter b = new BufferedWriter (w);
				b.write(e.getMessage());
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
				FileWriter w = new FileWriter(logfile,true);
				BufferedWriter b = new BufferedWriter (w);
				b.write(e.getMessage());
				b.close();
				
			}
			
			}
		DbAccess.closeConnection();
			
		}
		
		

	}


