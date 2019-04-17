package cs455.hadoop.wordcount;

import org.apache.avro.generic.GenericData;

import java.util.ArrayList;

public class Artist {
    public String id;
    public String name;
    public int songCount;
    public int hasLengthCount;
    public int hasLoudnessCount;
    public double loudness;
    public double fadedTime;
    public ArrayList<Song> songList;
    public Song average;

    public Artist(){
        songCount = 0;
        loudness = 0;
        fadedTime = 0;
        hasLengthCount = 0;
        hasLoudnessCount = 0;
    }
}
