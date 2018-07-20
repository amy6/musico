package example.com.musico.data;

import java.io.Serializable;

public class MusicItem implements Serializable {

    private int imageId;
    private String songName;
    private String artistName;
    private int songId;

    MusicItem(int albumImageId, String songName, String artistName, int songId) {
        this.imageId = albumImageId;
        this.songName = songName;
        this.artistName = artistName;
        this.songId = songId;
    }

    MusicItem(int artistImageId, String artistName) {
        this.imageId = artistImageId;
        this.artistName = artistName;
    }

    public int getImageId() {
        return imageId;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getSongId() {
        return songId;
    }
}
