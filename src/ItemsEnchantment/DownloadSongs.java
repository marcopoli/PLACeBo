package ItemsEnchantment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.axet.vget.VGet;
import com.github.axet.vget.info.VGetParser;
import com.github.axet.vget.info.VideoFileInfo;
import com.github.axet.vget.info.VideoInfo;
import com.github.axet.vget.vhs.YouTubeInfo.YoutubeQuality;
import com.github.axet.vget.vhs.YouTubeMPGParser;
import com.github.axet.vget.vhs.YouTubeQParser;
import com.github.axet.wget.DirectMultipart;
import com.github.axet.wget.RetryWrap;
import com.github.axet.wget.SpeedInfo;
import com.github.axet.wget.info.BrowserInfo;
import com.github.axet.wget.info.DownloadInfo;
import com.github.axet.wget.info.URLInfo;

//CLASSE SOSTITUITA DA MyDownloadSongs

/*
public class DownloadSongs {
	VideoInfo videoinfo;

	static class VGetStatus implements Runnable {
		VideoInfo videoinfo;
		long last;

		Map<VideoFileInfo, SpeedInfo> map = new HashMap<VideoFileInfo, SpeedInfo>();

		public VGetStatus(VideoInfo i) {
			this.videoinfo = i;
		}

		public SpeedInfo getSpeedInfo(VideoFileInfo dinfo) {
			SpeedInfo speedInfo = map.get(dinfo);
			if (speedInfo == null) {
				speedInfo = new SpeedInfo();
				speedInfo.start(dinfo.getCount());
				map.put(dinfo, speedInfo);
			}
			return speedInfo;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}
	}

	public static String getMp4FromYoutube(String link, String name) {
		String path =  "C:\\Users\\salv9\\Desktop\\Nuova\\";//"/Users/kram/Desktop/Songs/";
		File f = new File(path+name.replace("/", "").replace(" ", "").replace("'", "")+".webm");
		File f1 = new File(path+name.replace("/", "").replace(" ", "").replace("'", "")+".mp3");
		File f2 = new File(path+name.replace("/", "").replace(" ", "").replace("'", "")+".mp4");
		if (f.exists() || f2.exists()) {
			System.out.println("skip download");
			return name.replace("/", "").replace(" ", "").replace("'", "")+".webm";
		} else if(f1.exists() ){return name.replace("/", "").replace(" ", "").replace("'", "")+".mp3";} else if(f2.exists()){return name.replace("/", "").replace(" ", "").replace("'", "")+".mp4";}else {
			
			try {
				DirectMultipart.THREAD_COUNT = 3;
				SpeedInfo.SAMPLE_LENGTH = 1000;
				SpeedInfo.SAMPLE_MAX = 20;
				BrowserInfo.USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36";
				DownloadInfo.PART_LENGTH = 5 * 1024 * 1024; // bytes
				URLInfo.READ_TIMEOUT = 5 * 1000; // milliseconds
				URLInfo.CONNECT_TIMEOUT = 5 * 1000; // milliseconds
				RetryWrap.RETRY_COUNT = 5; /// 5 times then fail or -1 for
											/// infinite
				RetryWrap.RETRY_DELAY = 3; // seconds between retries
				final AtomicBoolean stop = new AtomicBoolean(false);
				URL web = new URL(link);

				VGetParser user = null;

				// create proper html parser depends on url
				user = new YouTubeQParser(YoutubeQuality.p480);

				VideoInfo videoinfo = user.info(web);
				// String path = "/Users/kram/Desktop/Songs/";
				
				VGet v = new VGet(videoinfo);
				VGetStatus notify = new VGetStatus(videoinfo);

				v.extract();
				List<VideoFileInfo> list = videoinfo.getInfo();
				if (list != null) {
					int i = 0;
					for (VideoFileInfo d : list) {

						// [OPTIONAL] setTarget file for each download source
						// video/audio
						// use d.getContentType() to determine which or use
						// v.targetFile(dinfo, ext, conflict) to set name
						// dynamically or
						// d.targetFile = new File("/Downloads/CustomName.mp3");
						// to set file name manually.
						if (i < 0 && !d.getContentType().replace("/", "").replace("video", "").equals("mp4")) {
							System.out.println("YoutubeDownload..."+name.replace("/", "").replace(" ", "").replace("'", "") + "."
									+ d.getContentType().replace("/", "").replace("video", "").replace("audio", ""));
							URL website = d.getSource();
							ReadableByteChannel rbc = Channels.newChannel(website.openStream());
							FileOutputStream fos = new FileOutputStream(path
									+ name.replace("/", "").replace(" ", "").replace("'", "") + "."
									+ d.getContentType().replace("/", "").replace("video", "").replace("audio", ""));
							fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
							// d.targetFile = new
							// File("/Users/kram/Desktop/Songs/"+name+"."+d.getContentType());
							i++;
							fos.close();
							return name.replace("/", "").replace(" ", "").replace("'", "") + "."
									+ d.getContentType().replace("/", "").replace("video", "").replace("audio", "");

						}

					}
					if(i > -1){
						
						
						  URL url = new URL("https://api-2.datmusic.xyz/search?q="+URLEncoder.encode(name,"UTF-8"));
						  
						  
					     // InputStream is = url.openStream();  // throws an IOException

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
						System.out.println("VKDownload..."+down);
						
						URLConnection conn = new URL(down).openConnection();
					    InputStream is2 = conn.getInputStream();

					    OutputStream outstream = new FileOutputStream(new File(path
								+ name.replace("/", "").replace(" ", "").replace("'", "") + ".mp3"));
					    byte[] buffer = new byte[4096];
					    int len;
					    while ((len = is2.read(buffer)) > 0) {
					        outstream.write(buffer, 0, len);
					    }
					    return name.replace("/", "").replace(" ", "").replace("'", "") + ".mp3";
						
					}
				}

				/*
				 * VGet v = new VGet(new URL(link), new File(path));
				 */
/*
				// v.setTargetDir(new File(path));
				// v.download(user, stop, notify);
				// File a = new File("/Users/kram/Desktop/Songs/"+name+".mp4");
				// v.TargetFile(a);
				// v.download();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";

	}

}
*/