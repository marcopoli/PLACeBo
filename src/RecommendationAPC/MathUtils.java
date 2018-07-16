package RecommendationAPC;
import java.util.HashSet;
public class MathUtils {
	public static double calculateSD(Double numArray[])
    {
        double sum = 0.0, standardDeviation = 0.0;

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum/numArray.length;

        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/numArray.length);
    }
	
	public static double avg(Double numArray[]){
		double sum = 0.0;
		for(double num : numArray) {
            sum += num;
        }
		return sum/numArray.length;
		
	}
	
	public static double CoeffVar(Double numArray[]){
		return calculateSD(numArray)/avg(numArray);
		
	}
}
   

