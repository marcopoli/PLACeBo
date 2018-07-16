package ItemsEnchantment;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import org.apache.bcel.generic.LUSHR;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.omt.lyrics.SearchLyrics;
import com.omt.lyrics.beans.Lyrics;
import com.omt.lyrics.beans.LyricsServiceBean;
import com.omt.lyrics.exception.SearchLyricsException;

public class GetLyrics {

	public static String getLyric(String song, String author) throws IOException{
		Elements lyric= null;
		try{
			String link = "http://lyrics.wikia.com/wiki/"+author.replace(" ", "_")+":"+song.replace(" ", "_");
			System.out.println(link);
			org.jsoup.nodes.Document doc =Jsoup.connect(link).get();
			lyric= doc.getElementsByClass("lyricbox");
			if (lyric.text().equals("")){
				Elements e = doc.getElementById("mw-content-text").getElementsByClass("redirectMsg").select("ul").select("li");
				Iterator<Element> i = e.iterator();
				Element el = i.next();
				//System.out.println(el.child(0).attr("href"));
				String linkRef = "http://lyrics.wikia.com"+el.child(0).attr("href");
				//System.out.println(linkRef);
				org.jsoup.nodes.Document docRef =Jsoup.connect(URLDecoder.decode(linkRef, "UTF-8")).get();
				lyric= docRef.getElementsByClass("lyricbox");
				//System.out.println(e.toString());
			}
			System.out.println("Testo trovato");
		}catch (Exception e){
			System.out.println("Testo non trovato!!!");
			
			return "lyric";
		}
		
		return lyric.text();
		
		

		
	}
	
}
