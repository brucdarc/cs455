package cs455.hadoop.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.lang.Double;
import java.io.IOException;
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


    }
}
