package RecommendationAPC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TagSelector {
	private ArrayList<Tag> tags;
	private int numTrackInPlaylist;
	
	public TagSelector(){
		tags = new ArrayList<Tag>();
		numTrackInPlaylist = 0;
	}
	public void clearSelector(){
		tags.clear();
		numTrackInPlaylist = 0;
	}
	public void addTag(String tagName){
		Tag newTag = new Tag (tagName.toLowerCase().replace("'","''"));
		if (!tags.contains(newTag) && !tagName.equals(""))	{
			tags.add(newTag);
		}else{
			for (int i = 0; i < tags.size(); i++){
				if (tags.get(i).getName().equals(tagName)) {
					tags.get(i).incrementCount();
					break;
				}
			}
			
		}
	}
	/**
	 * Incrementa il numero di tracce considerate per il calcolo della percentuale
	 */
	public void incrementNumTrack(){
		numTrackInPlaylist++;
	}
	/**
	 * Setta il numero di tracce considerate per il calcolo della pecentuale
	 * @param numTrack
	 */
	public void setNumTrack(int numTrack){
		numTrackInPlaylist = numTrack;
	}
	
	/**
	 * Calcola la percentuale per ogni tag.
	 * La percentuale è calcolata fra il numero di tracce in cui un certo tag è presente sul numero di tracce complessivo impostato.
	 */
	public void computePerc(){
		int numTags = tags.size();
		for (int i = 0; i<numTags; i++){
			int count = tags.get(i).getCount();
			double num = (double)count/(double)numTrackInPlaylist;
			double perc = num*(double)100;
			tags.get(i).setPerc(perc);			
		}
		
	}
	
	public void sortTags(){
		tags.sort(new Comparator<Tag>() {
			@Override
			public int compare(Tag o1, Tag o2) {
				if (o1.getPerc() < o2.getPerc()) {
					return -1;
				}else if (o1.getPerc() == o2.getPerc()) {
					return 0;
				}else{
					return 1;
				}
			}
			
		});
		Collections.reverse(tags);
	}
	
	public ArrayList<Tag> getTopTags(int threshold){
		ArrayList <Tag> returnTags = new ArrayList <Tag>();
		for (int i = 0; i< tags.size(); i++){
			Tag t = tags.get(i);
			if (t.getPerc() >= threshold) returnTags.add(t);
		}
		return returnTags;
	}
	
	public static String[] splitTag(String tags){
		if (tags == null) return new String[0];
		else return tags.split(",");
	}
	
	
}
