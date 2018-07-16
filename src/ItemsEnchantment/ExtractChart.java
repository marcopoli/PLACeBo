package ItemsEnchantment;
import java.io.IOException;
import java.text.Normalizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractChart {
	private org.jsoup.nodes.Document doc;
	private static String link = "https://acharts.co/search/";
	
	private  String unaccent(String src) {
		return Normalizer
				.normalize(src, Normalizer.Form.NFD)
				.replaceAll("[^\\p{ASCII}]", "");
	}
	public ExtractChart(String title, String artist) throws IOException{
		org.jsoup.nodes.Document searchDoc = Jsoup.connect(link+title.replace(" ","%20").replaceAll ("[ \\p{Punct}]", "")).get();
		Element songsTable = searchDoc.getElementById("songs_sheet");
		Elements tableRows = songsTable.getElementsByTag("tr");
		int sizeTable = tableRows.size();
		for (int i = 0; i<sizeTable; i++){
			Element row = tableRows.get(i);
			Element central = row.getElementsByClass("cPrinciple").get(0);
			Element artistName = central.getElementsByTag("span").get(0);
			String artistString = artistName.text();
			if (unaccent(artistString.toLowerCase()).contains(unaccent(artist.toLowerCase()))){
				//System.out.println("https://acharts.co"+central.getElementsByTag("a").get(0).attr("href"));
				doc = Jsoup.connect("https://acharts.co"+central.getElementsByTag("a").get(0).attr("href")).get();
				break;
			}			
		}		
	}
	
	public int getTopPeak(){
		try{
			Element per = doc.getElementById("PerformanceTable");
			Element body = per.getElementsByTag("tbody").get(0);
			Elements rows = body.getElementsByTag("tr");
			int size = rows.size();
			for (int i = 0; i< size;i++){
				String chart = rows.get(i).getElementsByClass("cPrinciple").get(0).getElementsByTag("a").text();
				if(chart.equals("US Singles Top 100")){
					System.out.println("TopPos trovata");
					return Integer.valueOf(rows.get(i).getElementsByClass("cNum").get(0).text().split(" ")[0].replace("#",""));				
				}
			}
			return -1;
		}catch(Exception e){
			return -1;
		}
		
		
	}
	
	public int getWeekInChart(){
		try{
			Element per = doc.getElementById("PerformanceTable");
			Element body = per.getElementsByTag("tbody").get(0);
			Elements rows = body.getElementsByTag("tr");
			int size = rows.size();
			for (int i = 0; i< size;i++){
				String chart = rows.get(i).getElementsByClass("cPrinciple").get(0).getElementsByTag("a").text();
				if(chart.equals("US Singles Top 100")){
					System.out.println("week trovata");
					return Integer.valueOf(rows.get(i).getElementsByTag("td").get(4).text());				
				}
			}
			return 0;
		}catch(Exception e){
			return 0;
		}
		
		
	}
	

}
