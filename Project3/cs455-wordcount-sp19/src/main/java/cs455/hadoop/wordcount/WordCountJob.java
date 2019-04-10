package cs455.hadoop.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * This is the main class. Hadoop will invoke the main method of this class.
 */
public class WordCountJob {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();

            /*
            conf.set("yarn.application.classpath",
                    "{{HADOOP_CONF_DIR}},{{HADOOP_COMMON_HOME}}/share/hadoop/common/*,{{HADOOP_COMMON_HOME}}/share/hadoop/common/lib/*,"
                            + " {{HADOOP_HDFS_HOME}}/share/hadoop/hdfs/*,{{HADOOP_HDFS_HOME}}/share/hadoop/hdfs/lib/*,"
                            + "{{HADOOP_MAPRED_HOME}}/share/hadoop/mapreduce/*,{{HADOOP_MAPRED_HOME}}/share/hadoop/mapreduce/lib/*,"
                            + "{{HADOOP_YARN_HOME}}/share/hadoop/yarn/*,{{HADOOP_YARN_HOME}}/share/hadoop/yarn/lib/*");
            */

            // Give the MapRed job a name. You'll see this name in the Yarn webapp.
            Job job = Job.getInstance(conf, "word count");
            // Current class.
            job.setJarByClass(WordCountJob.class);
            // Mapper
            job.setMapperClass(WordCountMapper.class);
            // Combiner. We use the reducer as the combiner in this case.
            job.setCombinerClass(WordCountReducer.class);
            // Reducer
            job.setReducerClass(WordCountReducer.class);
            // Outputs from the Mapper.
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);
            // Outputs from Reducer. It is sufficient to set only the following two properties
            // if the Mapper and Reducer has same key and value types. It is set separately for
            // elaboration.
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
            // path to input in HDFS
            FileInputFormat.addInputPath(job, new Path(args[0]));
            // path to output in HDFS
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            // Block until the job is completed.
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

    }
}
