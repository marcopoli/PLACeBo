package ItemsEnchantment;
import org.json.JSONObject;

public class MusicBrainzHL {
	/**
	 * 
	 * @return 0 if not dancability, 1 otherwise
	 */
	public static int getDancability(JSONObject json) {
		try{
			String value = json.getJSONObject("highlevel").getJSONObject("danceability").getString("value");
			if (value.equals("danceable")) return 1;
			else return 0;
		}catch(Exception e){return -1;}
		
	}
	/**
	 * 
	 * @param json
	 * @return return 0 for female, 1 for male
	 */
	public static int getGender(JSONObject json) {
		try{
			String value = json.getJSONObject("highlevel").getJSONObject("gender").getString("value");
			if (value.equals("female")) return 0;
			else return 1;
		}catch(Exception e){return -1;}
		
	}
	
	public static int getIsmirRhythm(JSONObject json) {
		try{
			String value = json.getJSONObject("highlevel").getJSONObject("ismir04_rhythm").getString("value");
			int valueReturn;
			switch (value) {
			case "ChaChaCha":
				valueReturn = 0;
				break;
			case "Jive":
				valueReturn = 1;
				break;
			case "Quickstep":
				valueReturn = 2;
				break;
			case "Rumba-American":
				valueReturn = 3;
				break;
			case "Rumba-International":
				valueReturn = 4;
				break;
			case "Rumba-Misc":
				valueReturn = 5;
				break;
			case "Samba":
				valueReturn = 6;
				break;
			case "Tango":
				valueReturn = 7;
				break;
			case "VienneseWaltz":
				valueReturn = 8;
				break;
			case "Waltz":
				valueReturn = 9;
				break;
			default:
				valueReturn =  -1;
				break;
			}
			return valueReturn;
		}catch (Exception e){return -1;}
		
	}
	public static int getAcoustic(JSONObject json) {
		try{
			String value = json.getJSONObject("highlevel").getJSONObject("mood_acoustic").getString("value");
			if (value.equals("acoustic")) return 0;
			else return 1;
		}catch(Exception e){return -1;}
		
	}
	
	public static int getAggressive(JSONObject json) {
		try{
			String value = json.getJSONObject("highlevel").getJSONObject("mood_aggressive").getString("value");
			if (value.equals("aggressive")) return 0;
			else return 1;
		}catch(Exception e){return -1;}
		
	}
	
	public static int getElectronic(JSONObject json) {
		try{
			String value = json.getJSONObject("highlevel").getJSONObject("mood_electronic").getString("value");
			if (value.equals("electronic")) return 0;
			else return 1;
		}catch(Exception e ){return -1;}
		
	}
	
	public static int getHappy(JSONObject json) {
		try{
			String value = json.getJSONObject("highlevel").getJSONObject("mood_happy").getString("value");
			if (value.equals("happy")) return 0;
			else return 1;
		}catch(Exception e){
			return -1;
		}
		
	}
	
	public static int getParty(JSONObject json) {
		try{
			String value = json.getJSONObject("highlevel").getJSONObject("mood_party").getString("value");
			if (value.equals("party")) return 0;
			else return 1;
		}catch(Exception e){
			return -1;
		}
		
	}
	
	public static int getRelaxed(JSONObject json) {
		try{
			String value = json.getJSONObject("highlevel").getJSONObject("mood_relaxed").getString("value");
			if (value.equals("relaxed")) return 0;
			else return 1;
		}catch(Exception e){
			return -1;
		}
		
	}
	
	public static int getSad(JSONObject json) {
		try{
			String value = json.getJSONObject("highlevel").getJSONObject("mood_sad").getString("value");
			if (value.equals("sad")) return 0;
			else return 1;
		}catch(Exception e){
			return -1;
		}
		
	}
	
	public static int getTimbre(JSONObject json) {
		try{
			String value = json.getJSONObject("highlevel").getJSONObject("timbre").getString("value");
			if (value.equals("bright")) return 0;
			else return 1;
		}catch(Exception e){
			return -1;
		}
		
	}
	
	public static int getTonal(JSONObject json) {
		try{
			String value = json.getJSONObject("highlevel").getJSONObject("tonal_atonal").getString("value");
			if (value.equals("atonal")) return 0;
			else return 1;
		}catch(Exception e){
			return -1;
		}
		
	}
	
	public static int getInstrumental(JSONObject json) {
		try{
			String value = json.getJSONObject("highlevel").getJSONObject("voice_instrumental").getString("value");
			if (value.equals("instrumental")) return 0;
			else return 1;
		}catch (Exception e  ){
			return -1;
		}
		
	}
}
