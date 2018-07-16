package ItemsEnchantment;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/*
public class extractCategoriesLink {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException,
			InterruptedException, ClassNotFoundException, SQLException {

		ArrayList links = new ArrayList();//link all music dei brani del mood
		ArrayList names = new ArrayList();//nome mood

		org.jsoup.nodes.Document doc = Jsoup.connect("http://www.allmusic.com/moods").get();

		Elements items = doc.getElementsByTag("a");

		for (int i = 73; i < items.size() - 12; i++) {
			System.out.println(items.get(i).attr("href")+"/songs");
			links.add(items.get(i).attr("href")+ "/songs");
			names.add(items.get(i).text());
		}

		String[] joy = { "Bright", "Effervescent", "Gleeful", "Humorous", "Irreverent", "Party/Celebratory",
				"Rambunctious", "Raucous", "Rollicking", "Silly", "Sweet", "Whimsical", "Witty", "Happy", "Yearning",
				"Provocative", "Sleazy", "Euphoric", "Exciting", "Exciting", "Fun" };
		String[] romantic = { "Passionate", "Romantic", "Precious", "Reverent", "Sentimental", "Warm", "Erotic",
				"Sensual", "Magical", "Sexy", "Bittersweet", "Atmospheric", "Innocent", "Languid", "Lush" };
		String[] relaxed = { "Laidback/Mellow", "Gentle", "Refined/Mannered", "Reserved", "Restrained", "Calm/Peaceful",
				"Relaxed", "Soft/Quiet", "Spiritual", "Smooth", "Dreamy", "Meditative" };
		String[] sad = { "Bleak", "Distraught", "Somber", "Poignant", "Melancholy", "Plaintive", "Malevolent",
				"Negative", "Lonely", "Funereal", "Dark", "Macabre", "Mysterious", "Sad", "Unsettling" };
		String[] tense = { "Tense/Anxious", "Turbulent", "Tragic", "Suffocating", "Strong", "Striding", "Rhapsodic",
				"Intense", "Hypnotic", "Desperate", "Scary" };
		String[] angry = { "Acerbic", "Aggressive", "Bitter", "Fiery", "Nasty", "Outraged", "Rebellious", "Snide",
				"Provocative", "Aggressive", "Demonic", "Hostile", "Messy", "Reckless", "Uncompromising" };

		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayList joy_l = new ArrayList(Arrays.asList(joy));
		ArrayList romantic_l = new ArrayList(Arrays.asList(romantic));
		ArrayList relaxed_l = new ArrayList(Arrays.asList(relaxed));
		ArrayList sad_l = new ArrayList(Arrays.asList(sad));
		ArrayList tense_l = new ArrayList(Arrays.asList(tense));
		ArrayList angry_l = new ArrayList(Arrays.asList(angry));

		ArrayList joy_Songs_tile = new ArrayList();
		ArrayList joy_Songs_author = new ArrayList();
		ArrayList joy_Songs_links = new ArrayList();
		ArrayList joy_Songs_lyrics = new ArrayList();

		ArrayList romantic_Songs_tile = new ArrayList();
		ArrayList romantic_Songs_author = new ArrayList();
		ArrayList romantic_Songs_links = new ArrayList();
		ArrayList romantic_Songs_lyrics = new ArrayList();

		ArrayList relaxed_Songs_tile = new ArrayList();
		ArrayList relaxed_Songs_author = new ArrayList();
		ArrayList relaxed_Songs_links = new ArrayList();
		ArrayList relaxed_Songs_lyrics = new ArrayList();

		ArrayList sad_Songs_tile = new ArrayList();
		ArrayList sad_Songs_author = new ArrayList();
		ArrayList sad_Songs_links = new ArrayList();
		ArrayList sad_Songs_lyrics = new ArrayList();

		ArrayList tense_Songs_tile = new ArrayList();
		ArrayList tense_Songs_author = new ArrayList();
		ArrayList tense_Songs_links = new ArrayList();
		ArrayList tense_Songs_lyrics = new ArrayList();

		ArrayList angry_Songs_tile = new ArrayList();
		ArrayList angry_Songs_author = new ArrayList();
		ArrayList angry_Songs_links = new ArrayList();
		ArrayList angry_Songs_lyrics = new ArrayList();
		/*
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/allMusicMood","postgres",
				"89marcopoli");
				*/
/*
		DbAccess.initConnection();
		Connection conn=DbAccess.getConnection();
		
		int k = 0;
		for (int i = 0; i < links.size(); i++) {
			//System.out.println("Category: "+i+" "+names.get(i));

			if (joy_l.contains(names.get(i))) {
				try{
				System.out.println("Links "+links.get(i));
				org.jsoup.nodes.Document doc1 = Jsoup.connect((String) links.get(i)).get();
				Elements items1 = doc1.getElementsByTag("tr");

				for (int j = 5; j < items1.size(); j++) {
					
					if (k <= -1) {
						System.out.println("Song "+k);
						k++; 
					} else {
						
						Elements titles = items1.get(j).getElementsByClass("title");
						String tit = "";
						String perf = "";
						try {
							System.out.println("joy");
							tit = titles.get(0).getElementsByTag("a").get(0).text();
							System.out.println(tit);
							joy_Songs_tile.add(tit);

						} catch (Exception e) {
							e.printStackTrace();
						}
						Elements perfs = items1.get(j).getElementsByClass("performer");
						try {
							perf = perfs.get(0).getElementsByTag("a").get(0).text();
							System.out.println(perf);
							joy_Songs_author.add(perf);
						} catch (Exception e) {
							e.printStackTrace();
						}

						String link = YoutubeLinker.getYoutubeLink(tit + " " + perf);
						joy_Songs_links.add(link);
						System.out.println("link: " + link);

						String lyric = GetLyrics.getLyric(tit, perf);
						System.out.println(lyric);
						String filename = DownloadSongs.getMp4FromYoutube(link, tit + "-" + perf);
						Thread.sleep(5000);
						System.out.println(filename); 						
						EssentiaExtractor ee = new EssentiaExtractor();
						String low = ee.getLowLevelFeatures(filename);
						
						String tags = LastFm.getTopTags(tit,perf);
						String sinfo = iTunesApi.getGeneralInfo(tit, perf);
						String sgenre = iTunesApi.getMainGenre(tit, perf);

						Statement stm = conn.createStatement();
						String query = "INSERT INTO songs (title,performer,link,lyrics,mood,filename,category,low_level,lastfm_tags,itunes_info,genre) VALUES ( ?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement st = conn.prepareStatement(query);
						st.setString(1, tit);
						st.setString(2, perf);
						st.setString(3, link);
						st.setString(4, lyric);
						st.setString(5, "joy");
						st.setString(6, filename);
						st.setString(7, (String) names.get(i));
						st.setString(8, low);
						st.setString(9, tags);
						st.setString(10, sinfo);
						st.setString(11, sgenre);
						st.execute();

						System.out.println("--------------------");
					}
				}
			}catch(Exception e){}
			}

			if (romantic_l.contains(names.get(i))) {
				try{
				org.jsoup.nodes.Document doc1 = Jsoup.connect((String) links.get(i)).get();
				Elements items1 = doc1.getElementsByTag("tr");
				System.out.println(links.get(i));
				for (int j = 5; j < items1.size(); j++) {
					if (k <= -1) {
						System.out.println("Song "+k);
						k++; 
					} else {
						Elements titles = items1.get(j).getElementsByClass("title");
						String tit = "";
						String perf = "";
						try {
							System.out.println("romantic");
							tit = titles.get(0).getElementsByTag("a").get(0).text();
							System.out.println(tit);
							romantic_Songs_tile.add(tit);
						} catch (Exception e) {
							e.printStackTrace();
						}
						Elements perfs = items1.get(j).getElementsByClass("performer");
						try {
							perf = perfs.get(0).getElementsByTag("a").get(0).text();
							System.out.println(perf);
							romantic_Songs_author.add(perf);
						} catch (Exception e) {
							e.printStackTrace();
						}

						String link = YoutubeLinker.getYoutubeLink(tit + " " + perf);
						romantic_Songs_links.add(link);
						System.out.println("link: " + link);

						String lyric = GetLyrics.getLyric(tit, perf);
						System.out.println(lyric);
						String filename = DownloadSongs.getMp4FromYoutube(link, tit + "-" + perf);
						Thread.sleep(5000);
						System.out.println(filename); 						
						EssentiaExtractor ee = new EssentiaExtractor();
						String low = ee.getLowLevelFeatures(filename);
						
						String tags = LastFm.getTopTags(tit,perf);
						String sinfo = iTunesApi.getGeneralInfo(tit, perf);
						String sgenre = iTunesApi.getMainGenre(tit, perf);

						Statement stm = conn.createStatement();
						String query = "INSERT INTO songs (title,performer,link,lyrics,mood,filename,category,low_level,lastfm_tags,itunes_info,genre) VALUES ( ?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement st = conn.prepareStatement(query);
						st.setString(1, tit);
						st.setString(2, perf);
						st.setString(3, link);
						st.setString(4, lyric);
						st.setString(5, "romantic");
						st.setString(6, filename);
						st.setString(7, (String) names.get(i));
						st.setString(8, low);
						st.setString(9, tags);
						st.setString(10, sinfo);
						st.setString(11, sgenre);
						st.execute();

						System.out.println("--------------------");
					}
				}
			}catch(Exception e){}
			}
			if (relaxed_l.contains(names.get(i))) {
				try{
				org.jsoup.nodes.Document doc1 = Jsoup.connect((String) links.get(i)).get();
				System.out.println(links.get(i));
				Elements items1 = doc1.getElementsByTag("tr");

				for (int j = 5; j < items1.size(); j++) {
					if (k <= -1) {
						System.out.println("Song "+k);
						k++; 
					} else {
						System.out.println(items1.get(j).toString());
						Elements titles = items1.get(j).getElementsByClass("title");

						String tit = "";
						String perf = "";

						try {
							System.out.println("relaxed");
							tit = titles.get(0).getElementsByTag("a").get(0).text();
							System.out.println(tit);
							relaxed_Songs_tile.add(tit);
						} catch (Exception e) {
							e.printStackTrace();
						}
						Elements perfs = items1.get(j).getElementsByClass("performer");
						try {
							perf = perfs.get(0).getElementsByTag("a").get(0).text();
							System.out.println(perf);
							relaxed_Songs_author.add(perf);
						} catch (Exception e) {
							e.printStackTrace();
						}

						String link = YoutubeLinker.getYoutubeLink(tit + " " + perf);
						relaxed_Songs_links.add(link);
						System.out.println("link: " + link);

						String lyric = GetLyrics.getLyric(tit, perf);
						System.out.println(lyric);
						String filename = DownloadSongs.getMp4FromYoutube(link, tit + "-" + perf);
						Thread.sleep(5000);
						System.out.println(filename); 						
						EssentiaExtractor ee = new EssentiaExtractor();
						String low = ee.getLowLevelFeatures(filename);
						
						String tags = LastFm.getTopTags(tit,perf);
						String sinfo = iTunesApi.getGeneralInfo(tit, perf);
						String sgenre = iTunesApi.getMainGenre(tit, perf);

						Statement stm = conn.createStatement();
						String query = "INSERT INTO songs (title,performer,link,lyrics,mood,filename,category,low_level,lastfm_tags,itunes_info,genre) VALUES ( ?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement st = conn.prepareStatement(query);
						st.setString(1, tit);
						st.setString(2, perf);
						st.setString(3, link);
						st.setString(4, lyric);
						st.setString(5, "relaxed");
						st.setString(6, filename);
						st.setString(7, (String) names.get(i));
						st.setString(8, low);
						st.setString(9, tags);
						st.setString(10, sinfo);
						st.setString(11, sgenre);
						st.execute();
						System.out.println("--------------------");
					}
				}
			}catch(Exception e){}

			}
			if (sad_l.contains(names.get(i))) {
				try{
				org.jsoup.nodes.Document doc1 = Jsoup.connect((String) links.get(i)).get();
				Elements items1 = doc1.getElementsByTag("tr");

				for (int j = 5; j < items1.size(); j++) {
					if (k <= -1) {
						System.out.println("Song "+k);
						k++; 
					} else {
						Elements titles = items1.get(j).getElementsByClass("title");
						String tit = "";
						String perf = "";

						try {
							System.out.println("sad");
							tit = titles.get(0).getElementsByTag("a").get(0).text();
							System.out.println(tit);
							sad_Songs_tile.add(tit);
						} catch (Exception e) {
							e.printStackTrace();
						}
						Elements perfs = items1.get(j).getElementsByClass("performer");
						try {
							perf = perfs.get(0).getElementsByTag("a").get(0).text();
							System.out.println(perf);
							sad_Songs_author.add(perf);
						} catch (Exception e) {
							e.printStackTrace();
						}

						String link = YoutubeLinker.getYoutubeLink(tit + " " + perf);
						sad_Songs_links.add(link);
						System.out.println("link: " + link);

						String lyric = GetLyrics.getLyric(tit, perf);
						System.out.println(lyric);
						String filename = DownloadSongs.getMp4FromYoutube(link, tit + "-" + perf);
						Thread.sleep(5000);
						System.out.println(filename); 						
						EssentiaExtractor ee = new EssentiaExtractor();
						String low = ee.getLowLevelFeatures(filename);
						
						String tags = LastFm.getTopTags(tit,perf);
						String sinfo = iTunesApi.getGeneralInfo(tit, perf);
						String sgenre = iTunesApi.getMainGenre(tit, perf);

						Statement stm = conn.createStatement();
						String query = "INSERT INTO songs (title,performer,link,lyrics,mood,filename,category,low_level,lastfm_tags,itunes_info,genre) VALUES ( ?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement st = conn.prepareStatement(query);
						st.setString(1, tit);
						st.setString(2, perf);
						st.setString(3, link);
						st.setString(4, lyric);
						st.setString(5, "sad");
						st.setString(6, filename);
						st.setString(7, (String) names.get(i));
						st.setString(8, low);
						st.setString(9, tags);
						st.setString(10, sinfo);
						st.setString(11, sgenre);
						st.execute();

						System.out.println("--------------------");
					}
				}
			}catch(Exception e){}
			}
			if (tense_l.contains(names.get(i))) {
				try{
				org.jsoup.nodes.Document doc1 = Jsoup.connect((String) links.get(i)).get();
				Elements items1 = doc1.getElementsByTag("tr");

				for (int j = 5; j < items1.size(); j++) {
					if (k <= -1) {
						System.out.println("Song "+k);
						k++; 
					} else {
						Elements titles = items1.get(j).getElementsByClass("title");

						String perf = "";
						String tit = "";

						try {
							System.out.println("tense");
							tit = titles.get(0).getElementsByTag("a").get(0).text();
							System.out.println(tit);
							tense_Songs_tile.add(tit);
						} catch (Exception e) {
							e.printStackTrace();
						}
						Elements perfs = items1.get(j).getElementsByClass("performer");
						try {
							perf = perfs.get(0).getElementsByTag("a").get(0).text();
							System.out.println(perf);
							tense_Songs_author.add(perf);
						} catch (Exception e) {
							e.printStackTrace();
						}

						String link = YoutubeLinker.getYoutubeLink(tit + " " + perf);
						tense_Songs_links.add(link);
						System.out.println("link: " + link);

						String lyric = GetLyrics.getLyric(tit, perf);
						System.out.println(lyric);
						String filename = DownloadSongs.getMp4FromYoutube(link, tit + "-" + perf);
						Thread.sleep(5000);
						System.out.println(filename); 						
						EssentiaExtractor ee = new EssentiaExtractor();
						String low = ee.getLowLevelFeatures(filename);
						
						String tags = LastFm.getTopTags(tit,perf);
						String sinfo = iTunesApi.getGeneralInfo(tit, perf);
						String sgenre = iTunesApi.getMainGenre(tit, perf);

						Statement stm = conn.createStatement();
						String query = "INSERT INTO songs (title,performer,link,lyrics,mood,filename,category,low_level,lastfm_tags,itunes_info,genre) VALUES ( ?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement st = conn.prepareStatement(query);
						st.setString(1, tit);
						st.setString(2, perf);
						st.setString(3, link);
						st.setString(4, lyric);
						st.setString(5, "tense");
						st.setString(6, filename);
						st.setString(7, (String) names.get(i));
						st.setString(8, low);
						st.setString(9, tags);
						st.setString(10, sinfo);
						st.setString(11, sgenre);
						st.execute();

						System.out.println("--------------------");
					}
				}
				}catch(Exception e){}
			}
			if (angry_l.contains(names.get(i))) {
				try{
				org.jsoup.nodes.Document doc1 = Jsoup.connect((String) links.get(i)).get();
				Elements items1 = doc1.getElementsByTag("tr");
				
				for (int j = 5; j < items1.size(); j++) {
					if (k <= -1) {
						System.out.println("Song "+k);
						k++; 
					} else {
						Elements titles = items1.get(j).getElementsByClass("title");
						String perf = "";
						String tit = "";
						try {
							System.out.println("angry");
							tit = titles.get(0).getElementsByTag("a").get(0).text();
							System.out.println(tit);
							angry_Songs_tile.add(tit);
						} catch (Exception e) {
							e.printStackTrace();
						}
						Elements perfs = items1.get(j).getElementsByClass("performer");
						try {
							perf = perfs.get(0).getElementsByTag("a").get(0).text();
							System.out.println(perf);
							angry_Songs_author.add(perf);
						} catch (Exception e) {
							e.printStackTrace();
						}

						String link = YoutubeLinker.getYoutubeLink(tit + " " + perf);
						angry_Songs_links.add(link);
						System.out.println("link: " + link);

						String lyric = GetLyrics.getLyric(tit, perf);
						System.out.println(lyric);
						String filename = DownloadSongs.getMp4FromYoutube(link, tit + "-" + perf);
						Thread.sleep(5000);
						//System.out.println(filename);
						
						EssentiaExtractor ee = new EssentiaExtractor();
						String low = ee.getLowLevelFeatures(filename);
						
						String tags = LastFm.getTopTags(tit,perf);
						String sinfo = iTunesApi.getGeneralInfo(tit, perf);
						String sgenre = iTunesApi.getMainGenre(tit, perf);

						Statement stm = conn.createStatement();
						String query = "INSERT INTO songs (title,performer,link,lyrics,mood,filename,category,low_level,lastfm_tags,itunes_info,genre) VALUES ( ?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement st = conn.prepareStatement(query);
						st.setString(1, tit);
						st.setString(2, perf);
						st.setString(3, link);
						st.setString(4, lyric);
						st.setString(5, "angry");
						st.setString(6, filename);
						st.setString(7, (String) names.get(i));
						st.setString(8, low);
						st.setString(9, tags);
						st.setString(10, sinfo);
						st.setString(11, sgenre);
						st.execute();
						System.out.println("--------------------");
					}
				}
				}catch(Exception e){}
			}
			

		}

	}

}
*/
