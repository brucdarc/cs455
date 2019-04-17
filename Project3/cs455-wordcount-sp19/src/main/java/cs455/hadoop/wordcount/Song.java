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
    public double[] startTime;
    public double[] pitch;
    public double[] timbre;
    public double[] maxLoud;
    public double[] maxLoudTime;
    public double[] StartLoudness;
    public int flag;

    public String toString(){
        return id + " " + title + " " + loudness + " " + artistId + " " + hotness + " " + fadingTime +
                " " + length + " " + dancability + " " + energetic + " " + startTime + " " + pitch + " " + timbre + " " +
                maxLoud + " " + maxLoudTime + " " + StartLoudness;
    }

    public String getSegmentData(){
        String data = "";
        data += "Start Time: " + doubleArrayToString(startTime);
        data += "Pitch: " + doubleArrayToString(pitch);
        data += "Timbre: " + doubleArrayToString(timbre);
        data += "Max Loudness: " + doubleArrayToString(maxLoud);
        data += "Max Loudness Time: " + doubleArrayToString(maxLoudTime);
        data += "Loudness at Start: " + doubleArrayToString(StartLoudness);
        return data;
    }

    public String doubleArrayToString(double[] arr){
        String result = "";
        for(double ele: arr){
            result += ele + ", ";
        }
        result += "\n";
        return result;
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
}
