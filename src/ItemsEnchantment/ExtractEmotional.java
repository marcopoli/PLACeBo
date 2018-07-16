package ItemsEnchantment;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import net.sourceforge.htmlunit.corejs.javascript.ast.SwitchCase;
import net.sourceforge.htmlunit.corejs.javascript.xml.XMLObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.indico.Indico;
import io.indico.api.text.Emotion;
import io.indico.api.utils.IndicoException;

import java.util.HashMap;


public class ExtractEmotional {
	private  URL url;
	private String page;
	private String emotionFields;
	private final String urlString = "http://193.204.187.192:8080/MyEmotionsRest/webresources/service/getEmotion/";
	public ExtractEmotional(String text) throws  ParserConfigurationException, SAXException, IOException, InterruptedException {
		try{
			fetchInfo(text);
		}/*catch(IOException t){
			boolean available = false;
			while(!available){				
			    if(checkStatus(urlString+text) == 1) {
			    	available = true; 
			    	System.out.println("Il server è tornato disponibile!");
			    }
			    else {
			    	System.out.println("Server non disponibile!! Riproverò fra 10 minuti");
			    	TimeUnit.MINUTES.sleep(10);
			    	System.out.println("Nuova prova...");
			    
			    }			   
			}
			fetchInfo(text);
			System.out.println("Informazioni sulle emozioni del testo prese");
		}*/
		catch (Exception e){
			
			emotionFields = "{}";
			System.out.println("Informazioni sull'emozioni espresse dal testo non trovate!!");
		}
		

	}
	
	private int checkStatus(String url){
		try{
			HttpURLConnection connection = null;
			URL u = new URL(url);
		    connection = (HttpURLConnection) u.openConnection();
		    connection.setRequestMethod("GET");
		    connection.setConnectTimeout(5*1000);
		    connection.connect();
		    return 1;
		}catch(SocketTimeoutException e){
			return 0;
			
		}catch(IOException e){
			return  -1;
		}
		
		
	}
	
	private void fetchInfo(String text) throws  ParserConfigurationException, SAXException{
		text = text.replace("(", " ").replace(")", " ");
		boolean flag = false;
		while(!flag){
			try{
				url=new URL(urlString + text);
				HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			    HttpURLConnection.setFollowRedirects(false);
			    huc.setConnectTimeout(5 * 1000);
			    huc.setRequestMethod("GET");
			    
			    huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
			    huc.connect();
			    BufferedReader br = new BufferedReader(new InputStreamReader( huc.getInputStream()));
			    page = "";
			    String line = "";
			    while ((line = br.readLine()) != null) {
			        page = page+ line;
			    }
			    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			    
			    Document doc=dBuilder.parse(new InputSource(new StringReader(page)));
			    NodeList nList=doc.getElementsByTagName("result");
			    Node nNode=nList.item(0);
			    emotionFields=nNode.getChildNodes().item(1).getTextContent();
			    System.out.println("Informazione su emozione trovate.");
			    flag = true;
			}catch (IOException e ){
				e.printStackTrace();
				System.out.println("riprovo ad ottenere le emozioni");
				flag = false;
				continue;
			}
		
		}
		
	}
	
	public int getMainEmotion() throws ParserConfigurationException, SAXException, IOException {
		int returnIndex;
		try{
			String splits[]=emotionFields.split("\\[");
			switch (splits[0]) {
				case "anger ":
					returnIndex = 0;
					break;
				case "disgust ":
					returnIndex = 1;
					break;
				case "fear ":
					returnIndex = 2;
					break;
				case "joy ":
					returnIndex = 3;
					break;
				case "sadness ":
					returnIndex = 4;
					break;
				case "suprise ":
					returnIndex = 5;
					break;
				default:
					returnIndex = -1;
					break;
			}
		}catch (Exception e){
			returnIndex = -1;
		}		
		return returnIndex;
	}
	
	public ArrayList<Float> getEmotionsValue() {
		ArrayList<Float> values = new ArrayList<Float>();
		try{
			String splits[]=this.emotionFields.substring(this.emotionFields.indexOf("[")+1, this.emotionFields.lastIndexOf("]")).split(";");
			for (String s:splits) {
				values.add(Float.parseFloat(s.split(":")[1]));
			}
		}catch (Exception e){
			values.add((float) -1);
			values.add((float) -1);
			values.add((float) -1);
			values.add((float) -1);
			values.add((float) -1);
		}		
		return values;		
		
	}
	private static double defaultValue = 0.0;
	/**
	 * It uses Indico API to extract emotion from the song lyric
	 * 
	 * @param key Idico key
	 * @param lyric text to analyze
	 * @return a map within emotion values
	 * @throws IndicoException
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 * @throws IllegalKeyException 
	 */
	public static Map<Emotion,Double> getIndicoEmotion(String key, String lyric) throws IndicoException, UnsupportedOperationException, IOException, IllegalKeyException{
		Map <Emotion,Double> emotions = null;
		
		try{			
			if (lyric ==""){				
				throw new IllegalArgumentException("Il testo da analizzare è vuoto!!!");
			}
			Map params = new HashMap();
			params.put("threshold", 0.0);
			Indico indico = null;
			indico = new Indico(key);
		
			emotions = indico.emotion.predict(lyric, params).getEmotion();	
			System.out.println("Informazioni emozione testo trovate!");
			return emotions;
		}catch(IndicoException e){
			throw new IllegalKeyException("Chiave non valida");
		}catch(IllegalArgumentException i){
			emotions.put(Emotion.Anger, defaultValue);
			emotions.put(Emotion.Fear, defaultValue);
			emotions.put(Emotion.Joy, defaultValue);
			emotions.put(Emotion.Sadness, defaultValue);
			emotions.put(Emotion.Surprise,defaultValue);
			return emotions;
			
		}
		
		
		
	}
	
}
