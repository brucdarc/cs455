package cs455.hadoop.wordcount;

import org.apache.avro.generic.GenericData;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.util.*;
import java.io.IOException;

/**
 * Reducer: Input to the reducer is the output from the mapper. It receives word, list<count> pairs.
 * Sums up individual counts per given word. Emits <word, total count> pairs.
 */
public class WordCountReducer extends Reducer<Text, Text, Text, Text> {
    Context context;

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        this.context = context;
        //if the key is for questions 1 through 6 send it off to be handled there
        if(key.toString().equals("Q1")) FirstPartReducerHelper.handleQ1to6(key, values, context);

        if(key.toString().equals("Q2")){
            // calculate the total count

        /*
        Have lists of songs and artists
        songs contain all the fields they need to answer the first few questions

        since there are two types of file inputs coming in, fill in the song fields when one of either message
        type comes in

        initialize artists with the info we have from metadata when they come in

        some artist fields will be filled out when we pass over the song dataset after everything is read in



         */
            ArrayList<Song> songs = new ArrayList<Song>();
            Map<String, Song> songsMap = new HashMap<String, Song>();
            ArrayList<Artist> artists = new ArrayList<Artist>();
            Map<String, Artist> artistsMap = new HashMap<String, Artist>();

            HashSet<String> sIds = new HashSet<String>();

            long errorCount = 0;
            long analCount = 0;
            long metaCount = 0;
            long failCount = 0;

            for(Text val : values){
                //complicated regex that splits on , but only if not between parenthesis
                String[] fields = val.toString().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

            /*
            handle analysis mapper input
             */
                if(fields[0].equals("0")){
                    analCount++;
                /*
                read all fields then construct song object with them

                parse the inputs, in a try catch in case fields dont parse correctly
                 */
                    try {
                        String songId = fields[1];



                        Song song = Parser.parseIntoSong(val.toString());

                        //context.write(new Text(songId), new Text(" 0"));
                        if(songsMap.keySet().contains(songId)){
                            //add fields to song instead if song is already been added to datastructure

                            //makes sure that we initialized the right fields if the metadata has already come in first
                            song = songsMap.get(songId);
                            //context.write(new Text(song + " ' "), new Text(songId));
                        }

                        song.setId(songId);
                        song.flag = 1;



                        //only add song to datastructures if it parses correctly
                        //check to make sure song isn't already in datastructures

                        //add to data structures if it was not already in there
                        if(!songsMap.keySet().contains(songId)){
                            songs.add(song);
                            songsMap.put(songId,song);
                            //context.write(new Text("adding to songs map: "), new Text("" + songsMap.get(songId).id));
                        }


                    }
                    catch (Exception e){
                        e.printStackTrace();
                        errorCount++;
                    }
                }

            /*
            handle metadata mapper input
             */
                if(fields[0].equals("1")){
                    metaCount++;
                    String artistid = fields[1];
                    String artistName = fields[2];
                    String songId = fields[3];

                    //context.write(new Text(songId + " "), new Text(artistid));

                /*
                we may have to grab and fill in on an already existing song object
                the same goes for the artist object, but we dont need to fill any fields if its already in
                 */
                    Song song = new Song();

                    //context.write(new Text(songId), new Text(" 1"));
                    if(songsMap.keySet().contains(songId)){
                        //add fields to song instead if song is already been added to datastructure

                        //makes sure that we initialized the right fields if the metadata has already come in first
                        song = songsMap.get(songId);
                        //context.write(new Text(song + " ' "), new Text(songId));
                    }
                    //initialize song fields
                    song.artistId = artistid;
                    song.id = songId;

                    if(!artistsMap.keySet().contains(artistid)){
                        //add the artist to data structures
                        Artist artist = new Artist();

                        artist.id = artistid;
                        artist.name = artistName;
                        //&&&&&&&&&&&&&&&&
                        artist.songList = new ArrayList<Song>();

                        //add to list and map the reference to the new artist object into the map
                        artists.add(artist);
                        artistsMap.put(artistid,artist);
                    }

                    if(!songsMap.keySet().contains(songId)){
                        songs.add(song);
                        songsMap.put(songId,song);
                        //context.write(new Text("adding to songs map2: "), new Text(songsMap.get(songId) + " " + songId));
                    }
                }

                for(int i = 0;i<songs.size();i++){
                    Song song = songs.get(i);
                    //context.write(new Text("" + song + " "), new Text(""+i));
                    Artist songArtist = new Artist();
                    String artistId = song.artistId;
                    songArtist = artistsMap.get(artistId);
                    //context.write(new Text("helper: "), new Text(artistId + " : " + songArtist));
                    songArtist.songList.add(song);
                }

                int counter = 0;

                for(Artist artist: artists){
                    artist.average = Parser.averageSongs(artist.songList);
                    if(artist.average == null) counter++;
                }

                context.write(new Text("null averages: "), new Text("" + counter));








                //context.write(key, val);

            }

        /*
        loop over all the songs and associate the data with the artists
        */





            //context.write(new Text("eyy"), new Text("lmao"));



        }

        if(key.toString().equals("E")) {
            for (Text val : values) {
                context.write(new Text(key), new Text(val));

            }
        }





    }
    /*

    public static Artist parseMeta(String val){
        String[] fields = val.toString().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String artistId = fields[1];
        String artistName = fields[2];
        String songId = fields[3];
        Artist res = new Artist();
        res.id = artistId;
        res.name = artistName;
        res.songids = new ArrayList<String>();
        res.songids.add(songId);
        return res;
    }
    */






}
