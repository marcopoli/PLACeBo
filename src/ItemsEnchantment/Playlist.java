package ItemsEnchantment;

public class Playlist {
	private String name;
	private String collaborative;
	private int pid;
	private int modified_at;
	private int num_tracks;
	private int num_albums;
	private int num_followers;
	private Track tracks[];
	private int num_edits;
	private int duration_ms;
	private int num_artists;
	
	public String getName() {
		return this.name;
	}
	public String getCollaborative(){
		return this.collaborative;
	}
	
	public boolean isCollaborative() {
		if (this.collaborative=="true") {
			return true;
		} else {
			return false;
		}
	}
	
	public int isCollaborativeInt() {
		if (this.collaborative=="true") {
			return 1;
		} else {
			return 0;
		}
	}
	
	public int getPid() {
		return this.pid;
	}
	
	public int getModifiedAt() {
		return this.modified_at;
	}
	
	public int getNumTracks() {
		return this.num_tracks;
	}
	
	public int getNumAlbums() {
		return this.num_albums;
	}
	
	public int getNumFollowers() {
		return this.num_followers;
	}
	
	public Track[] getTracks() {
		return this.tracks;
	}
	
	public int getNumEdits() {
		return this.num_edits;
	}
	
	public int getDuration() {
		return this.duration_ms;
	}
	
	public int getNumArtists() {
		return this.num_artists;
	}

}
