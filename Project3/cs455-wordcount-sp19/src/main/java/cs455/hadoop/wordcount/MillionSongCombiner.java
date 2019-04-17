package cs455.hadoop.wordcount;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MillionSongCombiner extends Reducer<Text, Text, Text, Text>{

    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        if(key.toString().equals("Q1")){
            for(Text val: values)
            context.write(key,val);
        }





    }


}
