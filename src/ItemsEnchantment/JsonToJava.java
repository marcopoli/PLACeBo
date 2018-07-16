package ItemsEnchantment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.PreparedStatement;

public class JsonToJava {
	
	public static void main(String[] args) throws IOException,  ClassNotFoundException, SQLException {
		File dir =new File("/media/mynewdrive/SHARED/download/mpd/data");
		File paths[]=dir.listFiles();
		DbAccess.initConnection();
		java.sql.Connection con = DbAccess.getConnection();
		con.setAutoCommit(false);
		java.sql.PreparedStatement insert_playlist_track=null;
		java.sql.PreparedStatement insert_Playlist = null;
		java.sql.PreparedStatement insert_info = null;
		java.sql.PreparedStatement insert_tracks = null;
		int i = 0;
		for (File p:paths) {
			i++;	
			System.out.println("FILE: "+i+"/1000");
			insert_info=con.prepareStatement("insert into info_slice(slice,generated_on,version) values(?,?,?) on duplicate key update slice = slice");
			System.out.println(p);
			FileInputStream stream=new FileInputStream(p.toString());
			try {
				Reader reader = new InputStreamReader(stream,"UTF-8");
				Gson gson = new GsonBuilder().create();
				Mpd b = gson.fromJson(reader, Mpd.class);
				System.out.println("numero playlist: " + Integer.toString(b.getNumPlaylists()));
			
				int playlistCount=0;
		
				Info info=b.getInfo();
				Playlist pls[]= b.getPlaylist();
		
		
		insert_info.setString(1,info.getSlice());
		insert_info.setString(2, info.getGenerated());
		insert_info.setString(3, info.getVersion());
		insert_info.executeUpdate();
		for (Playlist pl:pls) {
			insert_playlist_track=con.prepareStatement("insert into playlist_track (playlist_id,track_id,pos) values (?,?,?) on duplicate key update playlist_id=playlist_id");
			insert_Playlist=con.prepareStatement("insert into playlist(pid,name,collaborative,modified_at,num_tracks,num_albums,num_followers,num_edits,duration_ms,num_artists) values(?,?,?,?,?,?,?,?,?,?) on duplicate key update pid = pid");

			playlistCount++;
			System.out.println(playlistCount+"/"+b.getNumPlaylists());
			//actualName=pl.getName();
			//System.out.println(pl.getPid()+" "+actualName);
			insert_Playlist.setString(2,pl.getName());
			insert_Playlist.setInt(1,pl.getPid());
			insert_Playlist.setInt(9, pl.getDuration());
			insert_Playlist.setBoolean(3,pl.isCollaborative());
			insert_Playlist.setInt(4,pl.getModifiedAt());
			insert_Playlist.setInt(5,pl.getNumTracks());
			insert_Playlist.setInt(6,pl.getNumAlbums());
			insert_Playlist.setInt(7,pl.getNumFollowers());
			insert_Playlist.setInt(10,pl.getNumArtists());
			insert_Playlist.setInt(8,pl.getNumEdits());
			insert_Playlist.executeUpdate();
			Track tracks[]=pl.getTracks();
			for (Track t:tracks) {
				insert_tracks = con.prepareStatement("insert into track (track_name,track_uri, artist_uri,album_uri,duration_ms, album_name, artist_name) values(?,?,?,?,?,?,?) on duplicate key update track_uri=track_uri");
				insert_tracks.setString(1, t.getTrackName());
				
				insert_tracks.setString(2,t.getTrackUri());
				insert_tracks.setString(3,t.getArtistUri());
				insert_tracks.setString(4, t.getAlbumUri());
				insert_tracks.setInt(5, t.getDuration());
				insert_tracks.setString(6,  t.getAlbumName());
				insert_tracks.setString(7,  t.getArtistName());
				insert_playlist_track.setInt(1, pl.getPid());				
				insert_playlist_track.setString(2, t.getTrackUri());				
				insert_playlist_track.setInt(3,t.getPos());
				
				insert_tracks.executeUpdate();
				insert_playlist_track.executeUpdate();
				
			}
					
			}
		}catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println("ERRORE VERIFICATO ");
			continue;
			
		} 		
		
			con.commit();
			System.out.println("***********COMMIT***********");
			p.delete();
		
		
	}
		
	con.commit();
	DbAccess.closeConnection();

}
}


