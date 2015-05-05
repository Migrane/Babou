package musicplayer.mmad.paul.com.babou;


public class Song {

    /*private int id;
    private String preTitle, proTitle;

    public int getId(){return id;}

    public void setId(int i){this.id = i;}

    public String getPreTitle(){return  preTitle;}

    public void setPreTitle(String s){this.preTitle = s;}

    public String getProTitle(){return proTitle;}

    public void setProTitle(String s){this.proTitle = s;}
    */

    private long id;
    private String title;
    private String artist;

    public Song(long songID, String songTitle, String songArtist) {
        id=songID;
        title=songTitle;
        artist=songArtist;
    }

    public long getID(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getArtist(){
        return artist;
    }

}
