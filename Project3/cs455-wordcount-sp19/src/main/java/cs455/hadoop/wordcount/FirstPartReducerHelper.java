package cs455.hadoop.wordcount;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class FirstPartReducerHelper {

    public static void handleQ1to6(Text key, Iterable<Text> values, Reducer.Context context) throws IOException, InterruptedException{
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
                    String hotness = fields[2];
                    String loudness = fields[3];

                    //context.write(new Text(fields[2]), new Text(hotness + ""));
                    String fadingTime = fields[4];
                    String songLength = fields[5];
                    String dancability = fields[6];
                    String energy = fields[7];


                    Song song = new Song();

                    //context.write(new Text(songId), new Text(" 0"));
                    if(songsMap.keySet().contains(songId)){
                        //add fields to song instead if song is already been added to datastructure

                        //makes sure that we initialized the right fields if the metadata has already come in first
                        song = songsMap.get(songId);
                        //context.write(new Text(song + " ' "), new Text(songId));
                    }

                    song.setId(songId);
                    song.setLoudness(loudness);
                    song.setHotness(hotness);
                    song.setFadingTime(fadingTime);
                    song.setLength(songLength);
                    song.setDancability(dancability);
                    song.setEnergetic(energy);
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
                String songTitle = fields[4];

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
                song.title = songTitle;
                song.artistId = artistid;
                song.id = songId;

                if(!artistsMap.keySet().contains(artistid)){
                    //add the artist to data structures
                    Artist artist = new Artist();

                    artist.id = artistid;
                    artist.name = artistName;

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

            if(fields[0].equals("2")){
                //errorCount++;
            }

            if(!fields[0].equals("0") && !fields[0].equals("1") && !fields[0].equals("2")) failCount++;




            //context.write(key, val);

        }

        /*
        loop over all the songs and associate the data with the artists
        */
        Song hottest = null;
        double maxHot = 0;
        ArrayList<LengthWrapper> lengths = new ArrayList<LengthWrapper>();



        for(int i = 0;i<songs.size();i++){
            Song song = songs.get(i);
            //context.write(new Text("" + song + " "), new Text(""+i));
            Artist songArtist = new Artist();
            String artistId = song.artistId;
            songArtist = artistsMap.get(artistId);
            //context.write(new Text("helper: "), new Text(artistId + " : " + songArtist));
            try {
                songArtist.songCount++;
            }
            catch (Exception e){

            }
            if(song.flag == 1) {
                try {
                    if(song.loudness != Double.MAX_VALUE) {
                        songArtist.loudness += song.loudness;
                        songArtist.hasLoudnessCount++;
                    }
                    if(song.fadingTime != Double.MAX_VALUE) songArtist.fadedTime += song.fadingTime;
                    if(song.length != Double.MAX_VALUE) {
                        songArtist.hasLengthCount++;
                        lengths.add(new LengthWrapper(song.length, song.id, song.title));
                    }
                    //context.write(new Text(song.length + " " + song.id), new Text(new LengthWrapper(song.length, song.id).toString()));

                    if (song.hotness > maxHot && song.hotness != Double.MAX_VALUE) {
                        hottest = song;
                        maxHot = song.hotness;
                    }

                }
                //context.write(new Text("Hotness: "), new Text(" " + song.hotness));

                catch (NullPointerException e) {
                    //context.write(new Text("song read error"), new Text(song.artistId + " " + artistsMap.get(song.artistId)));
                }
            }

        }

            /*
            determine the artist with the most songs
             */
        Artist maxSongsArtist = null;
        int maxSongs = 0;
        Artist loudestArtist = null;
        double maxLoudness = 0;
        Artist fadestArtist = null;
        double maxTimeFaded = 0;

        for(Artist artist: artists){
            if(artist.songCount> maxSongs){
                maxSongsArtist = artist;
                maxSongs = artist.songCount;
                if(artist != null){
                    //context.write(new Text(artist.name), new Text("" + artist.songCount));
                }
                else{
                    //context.write(new Text("null artist"), new Text(""));
                }

            }
            //average the artist loudness
            artist.loudness = artist.loudness/artist.hasLoudnessCount;

            if (artist.loudness > maxLoudness && !artist.name.equals("artist_name")) {
                loudestArtist = artist;
                maxLoudness = artist.loudness;
            }

            if(artist.fadedTime > maxTimeFaded){
                fadestArtist = artist;
                maxTimeFaded = artist.fadedTime;
            }


        }


        Collections.sort(lengths);
        ArrayList<LengthWrapper> longests = new ArrayList<LengthWrapper>();
        longests.add(lengths.get(0));
        ArrayList<LengthWrapper> shortests = new ArrayList<LengthWrapper>();
        shortests.add(lengths.get(lengths.size() - 1));
        ArrayList<LengthWrapper> medians = new ArrayList<LengthWrapper>();
        medians.add(lengths.get(lengths.size() / 2));
        //use two medians splitting if even
        if (lengths.size() % 2 == 0) medians.add(lengths.get((lengths.size() - 1) / 2));

        for (LengthWrapper l : lengths) {
            if (longests.contains(l)) longests.add(l);
            if (shortests.contains(l)) shortests.add(l);
            if (medians.contains(l)) medians.add(l);
        }
        longests.remove(0);
        shortests.remove(0);
        medians.remove(0);
        if (lengths.size() % 2 == 0) medians.remove(0);


        context.write(new Text("Most songs: "), new Text(maxSongsArtist.name + " " + maxSongsArtist.songCount));

        try {
            context.write(new Text("Loudest: "), new Text(loudestArtist.name + " " + loudestArtist.loudness));
            context.write(new Text("Hottest: "), new Text(hottest.id + " " + hottest.title + " " + hottest.hotness));
            context.write(new Text("Most time fading: "), new Text(fadestArtist.name + " " + fadestArtist.fadedTime + " " + fadestArtist.songCount));
            context.write(new Text("Lengths data "), new Text(longests + " : " + shortests + " : " + medians));
        } catch (NullPointerException e) {
            context.write(new Text("null something in hottest"), new Text("" + hottest));
        } catch (Exception e) {
            context.write(new Text("When collecting final stuff had error"), new Text("fdsafasd"));
        }
        context.write(new Text("Songs not read: "), new Text("" + errorCount + " " + analCount + " " + metaCount + " " + failCount + " " + lengths.size()));





        //context.write(new Text("eyy"), new Text("lmao"));
    }
}
