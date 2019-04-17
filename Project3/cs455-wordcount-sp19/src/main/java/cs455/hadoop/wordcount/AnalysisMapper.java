package cs455.hadoop.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.lang.Double;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Mapper: Reads line by line, split them into words. Emit <word, 1> pairs.
 */
public class AnalysisMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // tokenize into fields
        //seperate on comma only if not between doube quotes
        String[] lines = value.toString().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        // emit word, count pairs.
        try {
            long count = 0;
            //if (!lines[2].isEmpty() && !lines[2].equals("song_hotttnesss")) {

                double endOfFadeIn = Double.parseDouble(lines[6]);
                double startOfFadeOut = Double.parseDouble(lines[13]);
                double songLength = Double.parseDouble(lines[5]);
                double fadingTime = endOfFadeIn + (songLength - startOfFadeOut);
                String startTime = lines[18];

                context.write(new Text("Q1"), new Text("0," + lines[1] + "," + lines[2] + "," + lines[10] + "," + fadingTime + "," + songLength + "," + lines[4] + "," + lines[7]));

            //}
        }
        catch(Exception e){
            context.write(new Text("Q1"), new Text("2," + e.toString()));
        }

        String[] segmentStarts = lines[18].split(" ");
        String[] segmentPitch = lines[20].split(" ");
        String[] segmentTimbre = lines[21].split(" ");
        String[] segmentLoudMax = lines[22].split(" ");
        String[] segmentLoudMaxTime = lines[23].split(" ");
        String[] segmentLoudAtStart = lines[24].split(" ");
        /*
        try to read in the duration
         */
        double duration = 0;
        String id = lines[1];
        try{
            duration = Double.parseDouble(lines[5]);
        }
        catch (Exception e){

        }



        String result = Parser.normalizeAndSendSegments(segmentStarts, segmentPitch, segmentTimbre, segmentLoudMax, segmentLoudMaxTime, segmentLoudAtStart, duration, id);
        context.write(new Text("Q2"), new Text(result));
        //Random r = new Randsom();
        //if(r.nextInt(1000) == 111) context.write(new Text("E"), new Text(lines[18] + "," + lines[20] + "," + lines[21] + "," + lines[22] + "," + lines[23] + "," + lines[23] + "," + lines[24]));


    }


    public static void main(String[] args){

    }
}
