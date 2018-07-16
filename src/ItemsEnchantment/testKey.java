package ItemsEnchantment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class testKey {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		ArrayList<String> keys = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(new File("/media/mynewdrive/SHARED/indicoKey.txt")))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       keys.add(line);
		    }
		}
		
		
		
		   
	}

}
