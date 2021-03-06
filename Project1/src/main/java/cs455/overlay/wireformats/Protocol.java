package cs455.overlay.wireformats;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/*
This class is going to be the parent of all the different
possible protocols we implement.

Each protocol need to implement 3 things.
1. a marshal method that marshals that specific type of protocol
2. a constructor that demarshals a byte array to create an object
3. a constructor that allows construction of the object from parameters


 */
public abstract class Protocol implements Event{
    public int eventType;
    public byte[] eventData;

    public Protocol(){

    }

    public Protocol(byte[] eventData){
        this.eventData = eventData;
        eventType = eventData[0];
    }

    /*
    all protocols should be able to marshal themselves into a byte array
     */
    public abstract byte[] marshal() throws IOException;
    /*
    What follows are some methods for marshaling and demarshalling strings and ints. These methods
    are static and should be called by each protocol as it needs to marshal and demarshal data.


    this method uses a bytebuffer to simply turn an int into an array of bytes.
    Bytebuffer needs a size of a byte array to allocate in its constructor
    You can turn a bytebuffer into a byte array with the array() method
    putInt() writes that int to the byte buffer from the beginning.
     */

    public static byte[] marshalInt(int input){
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(input);
        return bb.array();
    }

    /*
    Demarshalling an int is also done with a byte buffer. I made two methods in case you want
    to specify where in the byte array the start of the int is located. you can wrap a byte
    buffer object around an existing byte array. You can then get an into out of the byte buffer
    with the getInt method, which can take a byte offset to tell the byte buffer where to begin
    the integer.
     */

    public static int demarshalInt(byte[] input){
        return demarshalInt(input, 0);
    }

    //another method for if you want to specify index
    public static int demarshalInt(byte[] input, int index){
        ByteBuffer bbuf = ByteBuffer.wrap(input);
        return bbuf.getInt(index);
    }



    public static byte[] marshalString(String input){
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + input.length());
        byteBuffer.putInt(input.length());
        byteBuffer.put(input.getBytes());
        return byteBuffer.array();
    }

    /*
    A version of demarshalling a string where you can dont have to specify an index and the program assumes you start
    from the beginning of the array
     */

    public static String demarshalString(byte[] input){
        return demarshalString(input,0);
    }

    /*
    The default bytebuffer getchar method tries to create a utf8 character. I'm using ascii, so I cast the each byte
    to a character directly instead.
     */
    public static String demarshalString(byte[] input, int startIndex){
        ByteBuffer bbuf = ByteBuffer.wrap(input);
        int length = bbuf.getInt(startIndex);

        String result = "";
        for(int i = 0; i<(length);i++){
            result += (char)(bbuf.get(startIndex+i+4));
        }
        return result;
    }

    public static void printByteArray(byte[] array){
        for(byte b:array){
            System.out.print(b + "\t");
        }
    }

    /*
    main method with some silly test code I used to learn about marshaling and demarshaling
     */

    public static void main(String[] argv){
        /*
        Integer nt = new Integer(5);
        byte[] result = BigInteger.valueOf(2147483).toByteArray();
        for(byte b:result){
            System.out.print("Byte: ");
            System.out.println(b + " ");
        }
        System.out.println();
        */

        /*
        int test = 334567654;
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(test);
        byte[] me = bb.array();
        for(byte b:me){
            System.out.println("Byte: "+b);
        }
        ByteBuffer cc = ByteBuffer.wrap(me);
        int result = bb.getInt(0);
        System.out.println(result);
        */
        String me = "hi Im bob";
        byte[] sbyte = me.getBytes();
        for(byte b: sbyte){
            System.out.println("byte: " + b);
        }


    }
}
