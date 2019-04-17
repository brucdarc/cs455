package cs455.hadoop.wordcount;

import org.apache.avro.generic.GenericData;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class Parser {


    protected static String normalizeAndSendSegments(String[] segmentStarts, String[] segmentPitch, String[] segmentTimbre,
                                            String[] segmentLoudMax, String[] segmentLoudMaxTime, String[] segmentLoudMaxAtStart, double duration, String id){
        //dont output song if duration was not filled
        if(duration == 0) return "";
        /*
        create a new normalized segment every 5 seconds
         */
        /*
        ArrayList<Double> newSegTimes = new ArrayList<Double>();
        ArrayList<Double> newSegPitches = new ArrayList<Double>();
        ArrayList<Double> newSegTimbres = new ArrayList<Double>();
        ArrayList<Double> newLoudnessAtStarts = new ArrayList<Double>();
        ArrayList<Double> newLoudnesses = new ArrayList<Double>();
        ArrayList<Double> newLoudnessTimes = new ArrayList<Double>();
         */
        String newSegTimes = "";
        String newSegPitches = "";
        String newSegTimbres = "";
        String newLoudnessAtStarts = "";
        String newLoudnesses = "";
        String newLoudnessTimes = "";
        int length = segmentStarts.length;

        int counter = 0;
        try {
            int segIndex = 0;
            //create new segment for every 5 seconds
            //do this so that segment averages get averaged with data from all the same times in songs
            for (double time = 5; time < duration && segIndex < length; time += 5) {
                System.out.println("time " + time + " segindex " + segIndex + " length " + length);

                newSegTimes += " " + (time);
                double segTime = Double.parseDouble(segmentStarts[segIndex]);
                //add the max loudness at start for new segment by the first index of those we are combining
                newLoudnessAtStarts += " " + (Double.parseDouble(segmentLoudMaxAtStart[segIndex]));
                int numberOfSegmentsAveraged = 0;
                double newPitch = 0;
                double newTimbre = 0;
                double maxLoudness = 0;// Double.MAX_VALUE * -1;
                double maxLoudTime = 0;
                //grab or average the fields for the new segment we need too
                while(time>segTime && segIndex < length){
                    numberOfSegmentsAveraged++;
                    //add up the total pitch and timbre to average later
                    newPitch += Double.parseDouble(segmentPitch[segIndex]);
                    newTimbre += Double.parseDouble(segmentTimbre[segIndex]);
                    //check if this segment if the current loudest
                    double segLoudness = Double.parseDouble(segmentLoudMax[segIndex]);
                    System.out.println("seg loud " + segLoudness + " maxloud " + maxLoudness);
                    System.out.println("loop condition " + (segLoudness > maxLoudness));
                    //if(segLoudness > maxLoudness) {
                        System.out.println("inside loop");
                        maxLoudness += segLoudness;
                        maxLoudTime += Double.parseDouble(segmentLoudMaxTime[segIndex]);

                    //}

                    segTime = Double.parseDouble(segmentStarts[segIndex]);
                    segIndex++;

                }
                //get the averages for pitch and timbre then add them to the new segments arrays
                newPitch /= numberOfSegmentsAveraged;
                newTimbre /= numberOfSegmentsAveraged;
                newSegPitches += " " + (newPitch);
                newSegTimbres += " " + (newTimbre);

                //add the loudness values
                maxLoudness /= numberOfSegmentsAveraged;
                maxLoudTime /= numberOfSegmentsAveraged;
                newLoudnesses += " " + (maxLoudness);
                System.out.println(maxLoudness);
                newLoudnessTimes += " " + (maxLoudTime);
                //System.out.println(maxLoudTime);
                //System.out.println("time " + time + " segindex " + segIndex + " length " + length);


            }

            return "0," + id + "," + newSegTimes + "," + newSegPitches + "," +newSegTimbres + "," + newLoudnesses + "," + newLoudnessTimes+ "," + newLoudnessAtStarts;






        }
        catch (Exception e){
            e.printStackTrace();
            return "unable to parse";
        }
    }

    public static Song parseIntoSong(String input){

            int count = 0;
            int errCount = 0;
            ArrayList<Song> songs = new ArrayList<Song>();

                Random r = new Random();
                count++;
                try {
                    String[] fields = input.toString().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    String id = fields[1];
                    //System.out.println(fields[1].split(" ")[3]);
                    double[] segTimes = convertToDoubleArray(fields[2].split(" "));
                    double[] segPitches = convertToDoubleArray(fields[3].split(" "));
                    double[] segTimbres = convertToDoubleArray(fields[4].split(" "));
                    double[] loudnesses = convertToDoubleArray(fields[5].split(" "));
                    double[] loudnessTimes = convertToDoubleArray(fields[6].split(" "));
                    double[] loudnessAtStarts = convertToDoubleArray(fields[7].split(" "));

                    Song currentSong = new Song();
                    currentSong.id = id;
                    currentSong.startTime = segTimes;
                    currentSong.pitch = segPitches;
                    currentSong.timbre = segTimbres;
                    currentSong.maxLoud = loudnesses;
                    currentSong.maxLoudTime = loudnessTimes;
                    currentSong.StartLoudness = loudnessAtStarts;
                    return currentSong;

                }
                catch (Exception e){
                    e.printStackTrace();
                    errCount++;
                    //context.write(new Text("Exception: "), new Text(e.toString()));
                }
                return null;


    }

    public static double[] convertToDoubleArray(String [] dubs) throws Exception{
        //System.out.println(dubs[0]);
        double[] result = new double[dubs.length];
        for(int i = 0; i<dubs.length; i++){
            if(!dubs[i].isEmpty()) {
                result[i] = Double.parseDouble(dubs[i]);
                //System.out.println(result[i]);

            }
        }
        return result;
    }

    public static void printDoubleArray(double[] in){
        for(double i:in){
            System.out.print(i + " ,");
        }
        System.out.println();
        System.out.println();
    }

    public static double fixNan(double d){
        if(Double.isNaN(d) || Double.isInfinite(d) || d==Double.NEGATIVE_INFINITY) return 0;
        return d;

    }

    public static Song averageSongs(ArrayList<Song> songs){
        Song average = new Song();

        /*
        find the average ammount of segments in a song
         */
        long segStartlen = 0;
        /*
        all this is unnessesary since all segment data should be same length

        long segPitchlen = 0;
        long segTimbrelen = 0;
        long segLoudlen = 0;
        long segLoudTimelen = 0;
        long segStartLoudlen = 0;
        */

        for(Song s: songs){
            if(s != null) {
                if (s.startTime != null) {
                    segStartlen += s.startTime.length;
                }
            }
        }
        segStartlen /= songs.size();


        /*
        initialize all fields of average song to be of average segment ammount
         */

        average.startTime = new double[(int)segStartlen];
        average.pitch = new double[(int)segStartlen];
        average.timbre = new double[(int)segStartlen];
        average.maxLoud = new double[(int)segStartlen];
        average.maxLoudTime = new double[(int)segStartlen];
        average.StartLoudness = new double[(int)segStartlen];

        long[] numberAveraged = new long[(int)segStartlen];

        for(Song s : songs){
            if(s != null) {
                for (int i = 0; i < average.startTime.length && i < s.startTime.length; i++) {
                    if (s.startTime != null) {
                        average.startTime[i] += fixNan(s.startTime[i]);
                        average.pitch[i] += fixNan(s.pitch[i]);
                        average.timbre[i] += fixNan(s.timbre[i]);
                        average.maxLoud[i] += fixNan(s.maxLoud[i]);
                        average.maxLoudTime[i] += fixNan(s.maxLoudTime[i]);
                        average.StartLoudness[i] += fixNan(s.StartLoudness[i]);

                        numberAveraged[i] = numberAveraged[i] + 1;

                    }
                }
            }
        }
        try {
            //context.write(new Text("Average sum segments: \n"), new Text("" + average.getSegmentData() + "\n"));
        }
        catch (Exception e){
            try {
                //context.write(new Text("Exception"), new Text());
            }
            catch (Exception r){

            }
        }
        for(int i = 0; i<average.startTime.length; i++) {
            try {
                average.startTime[i] /= (double) numberAveraged[i];
                average.pitch[i] /= (double) numberAveraged[i];
                average.timbre[i] /= (double) numberAveraged[i];
                average.maxLoud[i] /= (double) numberAveraged[i];
                average.maxLoudTime[i] /= (double) numberAveraged[i];
                average.StartLoudness[i] /= (double) numberAveraged[i];
            }
            catch (ArithmeticException e){
                System.out.println("OH GOD WHY");
            }
        }

        try {
            //context.write(new Text("Average song segments: \n"), new Text("" + average.getSegmentData() + "\n"));
        }
        catch (Exception e){
            try {
                //context.write(new Text("Exception"), new Text());
            }
            catch (Exception r){

            }
        }

        return average;
    }



    public static void main(String[] args){
        ArrayList<String> songDatas = new ArrayList<String>();
        System.out.println(args[0]);
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String line;
            while ((line = br.readLine()) != null) {
                songDatas.add(line);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        ArrayList<Song> songs = new ArrayList<Song>();
        for(String s : songDatas) {
            String[] fields = s.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            System.out.println("made it here2" + fields.length);
            if(fields.length == 7) {
                String[] s1 = fields[0].split(" ");
                String[] s2 = fields[1].split(" ");
                String[] s3 = fields[2].split(" ");
                String[] s4 = fields[3].split(" ");
                String[] s5 = fields[4].split(" ");
                String[] s6 = fields[5].split(" ");

                System.out.println("S1 : " + fields[0].substring(0, 30));
                System.out.println("S2 : " + fields[1].substring(0, 30));
                System.out.println("S3 : " + fields[2].substring(0, 30));
                System.out.println("S4 : " + fields[3].substring(0, 30));
                System.out.println("S5 : " + fields[4].substring(0, 30));
                System.out.println("S6 : " + fields[5].substring(0, 30));


                String result = normalizeAndSendSegments(s1, s2, s3, s4, s5, s6, 200, "adfedfcv");

                //System.out.println(result);

                Song song = parseIntoSong(result);

                songs.add(song);

                System.out.println("made it here");
                printDoubleArray(song.startTime);
                printDoubleArray(song.maxLoud);
                printDoubleArray(song.maxLoudTime);
                printDoubleArray(song.pitch);
                printDoubleArray(song.timbre);
                printDoubleArray(song.StartLoudness);

                //System.out.println(result);
                //System.out.println(song);
            }
        }

        System.out.println(songs.size());

        Song average = averageSongs(songs);

        System.out.println(average.getSegmentData());




    }

}
