package ItemsEnchantment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyDownloadSongs {
	public static String getMp4FromYoutube(String name) throws IOException {
		try{
			String path =  "/media/mynewdrive/SHARED/song/";
			URL url = new URL("https://api-2.datmusic.xyz/search?q="+URLEncoder.encode(name,"UTF-8"));
		    HttpURLConnection huc = (HttpURLConnection) url.openConnection();
		    HttpURLConnection.setFollowRedirects(false);
		    huc.setConnectTimeout(15 * 1000);
		    huc.setRequestMethod("GET");
		    huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
		    huc.connect();
		    BufferedReader br = new BufferedReader(new InputStreamReader( huc.getInputStream()));
		    String page = "";
		    String line = "";
		    while ((line = br.readLine()) != null) {
		          page = page+ line;
		    }

			JSONObject doc = new JSONObject(page);
			JSONArray data = doc.getJSONArray("data");
			
			String down =(String) ((JSONObject)data.get(0)).get("download");
			//System.out.println("VKDownload..."+down);
			
			URLConnection conn = new URL(down).openConnection();
		    InputStream is2 = conn.getInputStream();
		    String completePath = path+ name.replace("/", "").replace(" ", "").replace("'", "").replace("?", "").replace("!", "").replace("%", "").replace("$", "") + ".mp3";
		    OutputStream outstream = new FileOutputStream(new File(completePath));
		    byte[] buffer = new byte[4096];
		    int len;
		    while ((len = is2.read(buffer)) > 0) {
		        outstream.write(buffer, 0, len);
		    }
		    outstream.close();
		    System.out.println("Download brano riuscito.");
		    return completePath;
		}catch(Exception e){
			System.out.println("Download brano non riuscito");
			System.out.println(e.getMessage());
			//e.printStackTrace();
			return "";
		}
		
	}

}
