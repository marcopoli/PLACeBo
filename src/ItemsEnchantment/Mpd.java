package ItemsEnchantment;

public class Mpd {
	private Info info;
	private Playlist playlists[];
	
	public int getNumPlaylists() {
		return this.playlists.length;		
	}
	
	public Info getInfo() {
		return this.info;
	}
	public Playlist[] getPlaylist() {
		return this.playlists;
	}

}
