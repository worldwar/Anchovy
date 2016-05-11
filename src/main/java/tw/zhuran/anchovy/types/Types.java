package tw.zhuran.anchovy.types;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;

import java.math.BigInteger;

public class Types {
    public static final byte FORMAT_CODE_NULL = 0x40;
    public static final byte FORMAT_CODE_BOOLEAN = 0x56;
    public static final byte FORMAT_CODE_TRUE = 0x41;
    public static final byte FORMAT_CODE_FALSE = 0x42;
    public static final byte FORMAT_CODE_UBYTE = 0x50;
    public static final byte FORMAT_CODE_USHORT = 0x60;
    public static final byte FORMAT_CODE_UINT = 0x70;
    public static final byte FORMAT_CODE_SMALLUINT = 0x52;
    public static final byte FORMAT_CODE_UINT0 = 0x43;
    public static final byte FORMAT_CODE_ULONG = (byte)0x80;
    public static final byte FORMAT_CODE_SMALLULONG = (byte)0x53;
    public static final byte FORMAT_CODE_ULONG0 = (byte)0x44;
    public static final byte FORMAT_CODE_BYTE = (byte)0x51;
    public static final byte FORMAT_CODE_SHORT = (byte)0x61;
    public static final byte FORMAT_CODE_INT = (byte)0x71;
    public static final byte FORMAT_CODE_LONG = (byte)0x81;
    public static final byte FORMAT_CODE_FLOAT = (byte)0x72;
    public static final byte FORMAT_CODE_DOUBLE = (byte)0x82;
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
            case FORMAT_CODE_UINT: return Integer.toUnsignedLong(Ints.fromBytes(bytes[1], bytes[2], bytes[3], bytes[4]));
            case FORMAT_CODE_SMALLUINT: return Byte.toUnsignedInt(bytes[1]);
            case FORMAT_CODE_UINT0: return 0;
            case FORMAT_CODE_ULONG: return new BigInteger(1, Lists.copy(bytes, 1, 8));
            case FORMAT_CODE_SMALLULONG: return Byte.toUnsignedInt(bytes[1]);
            case FORMAT_CODE_ULONG0: return 0;
            case FORMAT_CODE_BYTE: return bytes[1];
            case FORMAT_CODE_SHORT: return Shorts.fromBytes(bytes[1], bytes[2]);
            case FORMAT_CODE_INT: return Ints.fromBytes(bytes[1], bytes[2], bytes[3], bytes[4]);
            case FORMAT_CODE_LONG: return Longs.fromByteArray(Lists.copy(bytes, 1, 8));
            case FORMAT_CODE_FLOAT: return Float.intBitsToFloat(Ints.fromByteArray(Lists.copy(bytes, 1, 4)));
            case FORMAT_CODE_DOUBLE: return Double.longBitsToDouble(Longs.fromByteArray(Lists.copy(bytes, 1, 8)));
            default: return null;
        }
    }
}
