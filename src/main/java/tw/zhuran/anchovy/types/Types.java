package tw.zhuran.anchovy.types;

import com.google.common.base.Charsets;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

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
    public static final byte FORMAT_CODE_CHAR = (byte)0x73;
    public static final byte FORMAT_CODE_TIMESTAMP = (byte)0x83;
    public static final byte FORMAT_CODE_UUID = (byte)0x98;
    public static final byte FORMAT_CODE_VBIN8 = (byte)0xa0;
    public static final byte FORMAT_CODE_VBIN32 = (byte)0xb0;
    public static final byte FORMAT_CODE_STR8UTF8 = (byte)0xa1;
    public static final byte FORMAT_CODE_STR32UTF8 = (byte)0xb1;
    public static final byte FORMAT_CODE_SYM8 = (byte)0xa3;
    public static final byte FORMAT_CODE_SYM32 = (byte)0xb3;
    public static final byte PAYLOAD_TRUE = 0x01;
    public static final byte PAYLOAD_FALSE = 0x00;

    public static final Charset CHARSET_UTF_32BE = Charset.forName("UTF-32BE");
    public static final Charset CHARSET_UTF_8 = Charsets.UTF_8;
    public static final Charset CHARSET_US_ASCII = Charsets.US_ASCII;
    public static final ZoneId ZONE_ID_UTC = ZoneId.of("UTC");

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
            case FORMAT_CODE_CHAR: return new String(Lists.copy(bytes, 1, 4), CHARSET_UTF_32BE);
            case FORMAT_CODE_TIMESTAMP:
                long epochMilli = Longs.fromByteArray(Lists.copy(bytes, 1, 8));
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZONE_ID_UTC);
            case FORMAT_CODE_UUID: return new UUID(
                    Longs.fromByteArray(Lists.copy(bytes, 1, 8)),
                    Longs.fromByteArray(Lists.copy(bytes, 9, 8)));
            case FORMAT_CODE_VBIN8:
                int length = Byte.toUnsignedInt(bytes[1]);
                return Lists.copy(bytes, 2, length);
            case FORMAT_CODE_VBIN32:
                long l = Integer.toUnsignedLong(Ints.fromByteArray(Lists.copy(bytes, 1, 5)));
                return Lists.copy(bytes, 5, (int)l);
            case FORMAT_CODE_STR8UTF8:
                int stringLength = Byte.toUnsignedInt(bytes[1]);
                return new String(Lists.copy(bytes, 2, stringLength), CHARSET_UTF_8);
            case FORMAT_CODE_STR32UTF8:
                long longStringLength = Integer.toUnsignedLong(Ints.fromByteArray(Lists.copy(bytes, 1, 5)));
                return new String(Lists.copy(bytes, 5, (int)longStringLength), CHARSET_UTF_8);
            case FORMAT_CODE_SYM8:
                int symbolLength = Byte.toUnsignedInt(bytes[1]);
                return new String(Lists.copy(bytes, 2, symbolLength), CHARSET_US_ASCII);
            case FORMAT_CODE_SYM32:
                long longSymbolLength = Integer.toUnsignedLong(Ints.fromByteArray(Lists.copy(bytes, 1, 5)));
                return new String(Lists.copy(bytes, 5, (int)longSymbolLength), CHARSET_US_ASCII);
            default: return null;
        }
    }
}
