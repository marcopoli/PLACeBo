package RecommendationAPC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GeneralSelector<T> {
	
	public HashSet<T> getTopItems(ArrayList<T> items, int threshold) {
		int itemsSize = items.size();
		double thresholdItems = (itemsSize/100.0)*threshold;
		HashSet<T> returnSet = new HashSet<T>();
		Iterator<T> i = items.iterator();
		while(i.hasNext()){
			T item = i.next();
			int occurences = Collections.frequency(items, item);
			if (occurences >= thresholdItems){
				if ((!item.getClass().getName().equals("java.lang.Integer")) || ((Integer)item != -1))
				returnSet.add(item);
			}
		}
		System.out.println(returnSet.toString());
		return returnSet;
	}
}
