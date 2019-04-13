package cs455.hadoop.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

/**
 * Reducer: Input to the reducer is the output from the mapper. It receives word, list<count> pairs.
 * Sums up individual counts per given word. Emits <word, total count> pairs.
 */
public class WordCountReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // calculate the total count

        Map<String, String> songs = new HashMap<String, String>();

        for(Text val : values){
            String[] lines = val.toString().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

            if(lines[0].equals("0")){
                String songId = lines[1];

            }

            if(lines[0].equals("1")){
                String artistid = lines[1];
                String artistName = lines[2];
                String songId = lines[3];
                context.write(new Text("Songid: "), new Text(songId));
                songs.put(artistid,songId);
            }

            context.write(key, val);


        }


    }
}
