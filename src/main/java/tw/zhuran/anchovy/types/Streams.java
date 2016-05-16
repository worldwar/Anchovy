package tw.zhuran.anchovy.types;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;

import java.io.IOException;
import java.io.InputStream;

public class Streams {
    public static byte read(InputStream stream) throws IOException {
        return (byte) stream.read();
    }

    public static byte[] read(InputStream stream, int length) throws IOException {
        byte[] bytes = new byte[length];
        stream.read(bytes, 0, length);
        return bytes;
    }

    public static short readShort(InputStream stream) throws IOException {
        return Shorts.fromByteArray(read(stream, 2));
    }

    public static int readInt(InputStream stream) throws IOException {
        return Ints.fromByteArray(read(stream, 4));
    }

    public static long readLong(InputStream stream) throws IOException {
        return Longs.fromByteArray(read(stream, 8));
    }
}
