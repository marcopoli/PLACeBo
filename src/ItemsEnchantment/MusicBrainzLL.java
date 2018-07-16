package ItemsEnchantment;
import org.json.JSONObject;

public class MusicBrainzLL {
	public static float getBpm(JSONObject json) {
		try{
			return Float.parseFloat(json.getJSONObject("rhythm").get("bpm").toString());
		}catch(Exception e){
			return -1;
		}
		
	}
	
	public static float getAverageLoudness(JSONObject json) {
		try{
			return Float.parseFloat(json.getJSONObject("lowlevel").get("average_loudness").toString());
		}catch(Exception e){
			return -1;
		}
	
	}
	
	public static int getChord(JSONObject json) {
		try{
			String chord = json.getJSONObject("tonal").getString("chords_key");
			int r;
			switch (chord) {
			case "A":
				r = 0;		
				break;
			case "B":
				r = 1;
				break;
			case "C":
				r = 2;
				break;
			case "D":
				r = 3;
				break;
			case "E":
				r = 4;
				break;
			case "F":
				r = 5;
				break;
			case "G":
				r = 6;
				break;
			default:
				r = -1;
				break;
			}
			return r;
		}catch(Exception e){
			return -1;
		}
		
	}
	
	public static int getScale(JSONObject json) {
		try{
			String scale = json.getJSONObject("tonal").getString("chords_scale");
			if (scale.equals("major")) return 0;
			else return 1;
		}catch (Exception e){
			return -1;
		}
		
	}
	
	public static float getTuningDiatonicStrength(JSONObject json) {
		try{
			return Float.parseFloat(json.getJSONObject("tonal").get("tuning_diatonic_strength").toString());
		}catch (Exception e){
			return -1;
		}
		
	}
	
	public static float getTuningEqualTemperedDeviation(JSONObject json) {
		try{
			return Float.parseFloat(json.getJSONObject("tonal").get("tuning_equal_tempered_deviation").toString());
		}catch (Exception e){
			return -1;
		}
		
		
	}
	
	public static float getTuningFrequency(JSONObject json) {
		try{
			return Float.parseFloat(json.getJSONObject("tonal").get("tuning_frequency").toString());
		}catch (Exception e){
			return -1;
		}
		
	}
	
	public static float getTuningNontemperedEnergyRatio(JSONObject json) {
		try{
			return Float.parseFloat(json.getJSONObject("tonal").get("tuning_nontempered_energy_ratio").toString());
		}catch (Exception e){
			return -1;
		}
		
	}
}



