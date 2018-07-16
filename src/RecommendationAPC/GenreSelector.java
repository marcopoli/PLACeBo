package RecommendationAPC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.apache.xpath.operations.Equals;

public class GenreSelector {
	
	private class GenreInfo{
		public int occurences;
		public double score;
	}
	
	public ArrayList<String> getTopGenres(ArrayList<String> genres, double threshold){
		ArrayList<String> returnArray = new ArrayList<String>();
		HashMap<String, GenreInfo> temp = new HashMap<String,GenreInfo>();
		double total = genres.size();
		for (String s : genres){
			if (!temp.containsKey(s) && !s.equals("")){
				GenreInfo info = new GenreInfo() ;
				info.occurences = 1;
				temp.put(s, info);
			}else if (temp.containsKey(s) && !s.equals("")) {
				temp.get(s).occurences++;
			}
		}
		Set<String> keys =  temp.keySet();
		for (String s:keys ){
			double score;
			int occurences = temp.get(s).occurences;
			score = (occurences/total)*(double)100;
			temp.get(s).score = score;
			if (score >= threshold){
				returnArray.add(s);
			}
			
		}
		return returnArray;
		
		
		
		
	}
	
	
	
	
}
