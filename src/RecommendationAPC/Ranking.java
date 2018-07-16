package RecommendationAPC;

import org.apache.bcel.generic.ISUB;

public class Ranking {
	
	public static double getRankingValue(long view, int playlistsNum, int weeks, int topPeak){
		double score;
		double viewValue =0.0;
		double plValue = 0.0;
		double weeksValue = 0.0;
		double peakValue = 0.0;
		if(view < 1000000) viewValue = 0.0;
		else if (view >=1000000 && view < 5000000) viewValue = 0.25;
		else if (view >= 5000000 && view < 100000000) viewValue = 0.5;
		else if(view  >= 100000000 && view < 1000000000) viewValue = 0.75;
		else viewValue = 1.0;
		
		if (weeks == 0) weeksValue = 0.0;
		else if(weeks > 0 && weeks <= 12) weeksValue = 0.25;
		else if (weeks > 12 && weeks <= 24) weeksValue =0.5;
		else if (weeks > 24 && weeks <=36) weeksValue = 0.75;
		else if (weeks > 36) weeksValue = 1.0;
		
		if (topPeak == -1) peakValue = 0.0;
		else if (topPeak > 75) peakValue = 0.25;
		else if (topPeak > 50 && topPeak <= 75) peakValue = 0.5;
		else if (topPeak > 25 && topPeak <= 50) peakValue = 0.75;
		else if (topPeak <= 25) peakValue =1.0;
		
		if (playlistsNum == 0) plValue = 0.0;
		else if (playlistsNum > 0 && playlistsNum <500) plValue = 0.25;
		else if (playlistsNum >= 500 && playlistsNum <1000) plValue = 0.5;
		else if (playlistsNum >=1000 && playlistsNum< 2000) plValue = 0.75;
		else if (playlistsNum >= 2000) plValue = 1.0;
		score = (viewValue + peakValue + plValue + weeksValue)/4.0;
		System.out.println(score);
		return score;
		
	}
}
