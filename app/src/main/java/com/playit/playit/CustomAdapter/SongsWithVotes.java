package com.playit.playit.CustomAdapter;

/**
 * Created by marcfernandezantolin on 12/5/15.
 */
public class SongsWithVotes {

    public String song;
    public String votes;
    public int havotat;
    public String id_user;
    public String tag;
    public int id_song;

    public SongsWithVotes(String song, String votes, int havotat, String id_user, String tag, int id_song) {
        this.song = song;
        this.votes = votes;
        this.havotat = havotat;
        this.id_user = id_user;
        this.tag = tag;
        this.id_song = id_song;
    }

}
