package cs455.hadoop.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Mapper: Reads line by line, split them into words. Emit <word, 1> pairs.
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // tokenize into words.
        String[] lines = value.toString().split(System.getProperty("line.separator"));
        // emit word, count pairs.
        long count = 0;
        for(String line : lines){
            if(count%10000==0) {
                context.write(new Text(line), new IntWritable(1));
            }
            count++;
        }
    }
}
