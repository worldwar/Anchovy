package tw.zhuran.anchovy.types;

import com.google.common.primitives.Shorts;

public class Types {
    public static final byte FORMAT_CODE_NULL = 0x40;
    public static final byte FORMAT_CODE_BOOLEAN = 0x56;
    public static final byte FORMAT_CODE_TRUE = 0x41;
    public static final byte FORMAT_CODE_FALSE = 0x42;
    public static final byte FORMAT_CODE_UBYTE = 0x50;
    public static final byte FORMAT_CODE_USHORT = 0x60;
    public static final byte PAYLOAD_TRUE = 0x01;
    public static final byte PAYLOAD_FALSE = 0x00;

    public static Object decode(byte[] bytes) {
        assert bytes != null : "input of decode should not be null!";
        assert bytes.length != 0 : "input of decode should not be empty!";

        switch (bytes[0]) {
            case FORMAT_CODE_NULL: return null;
            case FORMAT_CODE_BOOLEAN:
                switch (bytes[1]) {
                    case PAYLOAD_FALSE: return false;
                    case PAYLOAD_TRUE: return true;
                }
            case FORMAT_CODE_TRUE: return true;
            case FORMAT_CODE_FALSE: return false;
            case FORMAT_CODE_UBYTE: return Byte.toUnsignedInt(bytes[1]);
            case FORMAT_CODE_USHORT: return Short.toUnsignedInt(Shorts.fromBytes(bytes[1], bytes[2]));
            default: return null;
        }
    }
}
