package ItemsEnchantment;

public class Track {

	private int pos;
	private String track_name;
	private String artist_name;
	private String track_uri;
	private String artist_uri;
	private int duration_ms;
	private String album_name;
	private String album_uri;
	
	public int getPos() {
		return this.pos;
	}
	
	public String getTrackName() {
		return this.track_name;
	}
	public String getArtistName() {
		return this.artist_name;
	}
	public String getTrackUri() {
		return this.track_uri;
	}
	public String getArtistUri() {
		return this.artist_uri;
	}
	public int getDuration() {
		return this.duration_ms;
	}
	public String getAlbumUri() {
		return this.album_uri;
	}
	public String getAlbumName() {
		return this.album_name;
	}
}
