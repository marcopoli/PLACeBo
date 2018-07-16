package ItemsEnchantment;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MusicBrainzExtractor {
	
	private ArrayList<String> linksTrack;
	private JSONObject documentLL;
	private JSONObject documentHL;
	public MusicBrainzExtractor(String nameTrack, String nameArtist) throws IOException, SAXException, ParserConfigurationException, DataNotFoundException {
		String stringLink="https://musicbrainz.org/taglookup?tag-lookup.artist="+URLEncoder.encode(nameArtist,"UTF-8")+"&tag-lookup.release=&tag-lookup.tracknum=&tag-lookup.track="+URLEncoder.encode(nameTrack, "UTF-8")+"&tag-lookup.duration=&tag-lookup.filename=";
		System.out.println("Sto provando ad ottenere informazioni sull'audio...");
		linksTrack = new ArrayList<String>();   	    
	    org.jsoup.nodes.Document doc=Jsoup.connect(stringLink).get();
	    Elements items = doc.getElementsByTag("tr");
	    Iterator<Element> it=items.iterator();
	    try{
	    	if (!it.hasNext()) throw new DataNotFoundException("Dati su MusicBrainz Non Trovati");
	    	it.next();
	    	while (it.hasNext()) {
	    		Element currentElem = it.next();
	    		String score = currentElem.child(0).text();
	    		if (score.equals("100")) {
	    			//System.out.println(currentElem.child(2).child(0).attr("href").split("/")[2]);
	    			linksTrack.add(currentElem.child(2).child(0).attr("href").split("/")[2]);
	    		}
	    	}
	    
	    	documentLL = getData(false);
		    documentHL = getData(true);
	    }catch(DataNotFoundException e){
	    	System.out.println(e.getMessage());
	    	/*
	    	String path = MyDownloadSongs.getMp4FromYoutube(nameTrack + " "+ nameArtist);
	    	//if (path.equals("")) throw new DataNotFoundException("download brano non riuscito!");
	    	EssentiaExtractor essentia = new EssentiaExtractor();
	    	essentia.computeFeatures(path);
	    	this.documentLL = new JSONObject(essentia.getLowLevel()); 
	    	this.documentHL = new JSONObject(essentia.getHighLevel());
	    	*/
	    	this.documentHL = new JSONObject("{}");
	    	this.documentLL = new JSONObject ("{}");
	    	
	    }
	    
	}
	/*
	 * @param false for lowlevel feature, true for highlevel feature
	 */
	private JSONObject getData(boolean level) throws IOException, DataNotFoundException {
			JSONObject returnObj = null;	
			boolean findData=false;
			for (String s: this.linksTrack) {				
				URL url = new URL("https://acousticbrainz.org/api/v1/"+s+"/"+setLevel(level));
				HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			    HttpURLConnection.setFollowRedirects(false);
			    huc.setConnectTimeout(15 * 1000);
			    huc.setRequestMethod("GET");
			    huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
			    huc.connect();
			    try {
				    BufferedReader br = new BufferedReader(new InputStreamReader( huc.getInputStream()));
				    String page = "";
				    String line = "";
				    while ((line = br.readLine()) != null) {
				        page = page+ line;
				    }
				   // System.out.println("PAGE....."+page);
					JSONObject doc = new JSONObject(page);
					returnObj = doc;
					findData = true;
					System.out.println("Informazioni audio trovate: "+s);
					break;
			    }catch(FileNotFoundException n) {
			    	//System.out.println("informazioni non trovate in "+s);
			    	continue;
			    }		
									
			}
			if (findData == false) {
				throw new DataNotFoundException("Dati non trovati. Scaricherò il brano musicale per analizzarlo");
			}
			//System.out.println(returnObj);
			return returnObj;			
		}
	
	private String setLevel (boolean flag) {
		if (flag) return "high-level";
		else return "low-level";
	}
	
	public JSONObject getDocumentLL() {
		return this.documentLL;
	}
	
	public JSONObject getDocumentHL(){
		return this.documentHL;
	}
	
		
	}

