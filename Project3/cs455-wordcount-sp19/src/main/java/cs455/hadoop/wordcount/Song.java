package cs455.hadoop.wordcount;

public class Song {
    public String id;
    public String title;
    public double loudness;
    public String artistId;
    public double hotness;
    public double fadingTime;
    public double length;
    public double dancability;
    public double energetic;
    public double startTime;
    public double pitch;
    public double timbre;
    public double maxLoud;
    public double maxLoudTime;
    public double StartLoudness;
    public int flag;

    public String toString(){
        return id + " " + title + " " + loudness + " " + artistId + " " + hotness + " " + fadingTime +
                " " + length + " " + dancability + " " + energetic + " " + startTime + " " + pitch + " " + timbre + " " +
                maxLoud + " " + maxLoudTime + " " + StartLoudness;
    }

    public Song(){
        id = null;
        title = null;
        loudness = Double.MAX_VALUE;
        artistId = null;
        hotness = Double.MAX_VALUE;
        fadingTime= Double.MAX_VALUE;
        length= Double.MAX_VALUE;
        dancability= Double.MAX_VALUE;
        energetic= Double.MAX_VALUE;
        startTime= Double.MAX_VALUE;
        pitch= Double.MAX_VALUE;
        timbre= Double.MAX_VALUE;
        maxLoud= Double.MAX_VALUE;
        maxLoudTime= Double.MAX_VALUE;
        StartLoudness= Double.MAX_VALUE;
        flag = 0;
    }

    public void setId(String id) {
        try {
            this.id = id;
        }
        catch (Exception e){

        }
    }

    public void setLoudness(String loudness) {
        try {
            this.loudness = Double.parseDouble(loudness);

        } catch (Exception e) {

        }
    }

    public void setHotness(String hotness) {
        try {
            this.hotness = Double.parseDouble(hotness);

        } catch (Exception e) {

        }
    }

    public void setFadingTime(String fadingTime) {
        try {
            this.fadingTime = Double.parseDouble(fadingTime);

        } catch (Exception e) {

        }
    }

    public void setLength(String length) {
        try {
            this.length = Double.parseDouble(length);

        } catch (Exception e) {

        }
    }

    public void setDancability(String dancability) {
        try {
            this.dancability = Double.parseDouble(dancability);

        } catch (Exception e) {

        }
    }

    public void setEnergetic(String energetic) {
        try {
            this.energetic = Double.parseDouble(energetic);

        } catch (Exception e) {

        }
    }

    public void setStartTime(String startTime) {
        try {
            this.startTime = Double.parseDouble(startTime);

        } catch (Exception e) {

        }
    }

    public void setPitch(String pitch) {
        try {
            this.pitch = Double.parseDouble(pitch);

        } catch (Exception e) {

        }
    }

    public void setTimbre(String timbre) {
        try {
            this.timbre = Double.parseDouble(timbre);

        } catch (Exception e) {

        }
    }

    public void setMaxLoud(String maxLoud) {
        try {
            this.maxLoud = Double.parseDouble(maxLoud);

        } catch (Exception e) {

        }
    }

    public void setMaxLoudTime(String maxLoudTime) {
        try {
            this.maxLoudTime = Double.parseDouble(maxLoudTime);

        } catch (Exception e) {

        }
    }

    public void setStartLoudness(String startLoudness) {
        try {
            StartLoudness = Double.parseDouble(startLoudness);

        } catch (Exception e) {

        }
    }
}
