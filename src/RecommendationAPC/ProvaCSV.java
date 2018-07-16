package RecommendationAPC;

import java.io.FileWriter;
import java.io.IOException;

public class ProvaCSV {
	public static void main(String args[]) throws IOException{
		FileWriter f = new FileWriter("/media/mynewdrive/SHARED/prova.csv");
		f.append("id,name,surname\n");
		f.append("1,giorgio,bho");
		f.flush();
		f.close();
	}
	

}
