package ItemsEnchantment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EssentiaExtractor {
	private String script="/media/mynewdrive/SHARED/essentia_svm_extractor/streaming_extractor_music";
	private String fileRes="/media/mynewdrive/SHARED/essentia_svm_extractor/out.txt";
	private String scriptHL = "/media/mynewdrive/SHARED/essentia_svm_extractor/streaming_extractor_music_svm";
	private String fileResHL = "/media/mynewdrive/SHARED/essentia_svm_extractor/outHL.txt";
	private String  profile = "/media/mynewdrive/SHARED/essentia_svm_extractor/profile";
	private String ll;
	private String hl;
	public EssentiaExtractor(){}
	
	public void computeFeatures(String filename){	
				
		try{
			Process process = new ProcessBuilder(script,filename, fileRes).start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				 System.out.println(line);
				}
			BufferedReader br2 = new BufferedReader(new FileReader(fileRes)); 
			StringBuilder sb = new StringBuilder();
			String line2 = br2.readLine();

		    while (line2 != null) {
		        sb.append(line2);
		        sb.append(System.lineSeparator());
		        line2 = br2.readLine();
		    }
		String everything = sb.toString();
		
		File f=new File(filename);
		f.delete();
		
		br2.close();
		ll = everything;
		hl = getHighLevelFeatures();
		}catch(Exception e){e.printStackTrace(); ll="{}"; hl = "{}";}
		
		
	}

	private  String getHighLevelFeatures(){	
		
		try{
			Process process = new ProcessBuilder(scriptHL,fileRes,this.fileResHL, profile).start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				 System.out.println(line);
				}
			BufferedReader br2 = new BufferedReader(new FileReader(fileResHL)); 
			StringBuilder sb = new StringBuilder();
			String line2 = br2.readLine();

		    while (line2 != null) {
		        sb.append(line2);
		        sb.append(System.lineSeparator());
		        line2 = br2.readLine();
		    }
		String everything = sb.toString();
		
		br2.close();
		return everything;
		}catch(Exception e){e.printStackTrace(); return"{}";}
		
		
	}
	
	public String getLowLevel(){
		return this.ll;
	}
	public String getHighLevel(){
		return this.hl;
	}
}
