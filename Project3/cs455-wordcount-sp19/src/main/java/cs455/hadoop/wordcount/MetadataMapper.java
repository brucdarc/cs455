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
public class MetadataMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // tokenize into fields
        //seperate on comma only if not between doube quotes
        String[] lines = value.toString().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        try {
            // emit word, count pairs.
            long count = 0;
            if (!lines[8].isEmpty()) {

                context.write(new Text("Q1"), new Text("1," + lines[3] + "," + lines[7] + "," + lines[8] + "," + lines[9]));

            }
        }
        catch (Exception e){

        }


    }
}
