package cs455.hadoop.wordcount;

public class LengthWrapper implements Comparable{
    public double length;
    public String songid;
    public String name;

    public LengthWrapper(double l, String id, String name){
        length = l;
        songid = id;
        this.name = name;
    }

    public int compareTo(Object o){
        if(!(o instanceof LengthWrapper)) return -1;
        LengthWrapper other = (LengthWrapper)o;
        if(other.length > length) return 1;
        if(other.length == length) return 0;
        return -1;
    }

    public String toString(){
        return "Length: " + length + " Title: " + name;
    }
    public boolean equals(Object o){
        if(!(o instanceof LengthWrapper)) return false;
        LengthWrapper other = (LengthWrapper)o;
        if(other.length == length) return true;
        return false;
    }


}
