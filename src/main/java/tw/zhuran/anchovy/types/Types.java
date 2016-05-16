package tw.zhuran.anchovy.types;

import com.google.common.base.Charsets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        try {
            return decode(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            return null;
        }
    }

    public static Object decode(InputStream stream) throws IOException {
        assert stream != null : "input of decode should not be null!";
        assert stream.available() != 0 : "input of decode should not be empty!";

        switch (Streams.read(stream)) {
            case FORMAT_CODE_NULL: return null;
            case FORMAT_CODE_BOOLEAN:
                switch (stream.read()) {
                    case PAYLOAD_FALSE: return false;
                    case PAYLOAD_TRUE: return true;
                }
            case FORMAT_CODE_TRUE: return true;
            case FORMAT_CODE_FALSE: return false;
            case FORMAT_CODE_UBYTE: return Byte.toUnsignedInt(Streams.read(stream));
            case FORMAT_CODE_USHORT: return Short.toUnsignedInt(Streams.readShort(stream));
            case FORMAT_CODE_UINT: return Integer.toUnsignedLong(Streams.readInt(stream));
            case FORMAT_CODE_SMALLUINT: return Byte.toUnsignedInt(Streams.read(stream));
            case FORMAT_CODE_UINT0: return 0;
            case FORMAT_CODE_ULONG: return new BigInteger(1, Streams.read(stream, 8));
            case FORMAT_CODE_SMALLULONG: return Byte.toUnsignedInt(Streams.read(stream));
            case FORMAT_CODE_ULONG0: return 0;
            case FORMAT_CODE_BYTE: return Streams.read(stream);
            case FORMAT_CODE_SHORT: return Streams.readShort(stream);
            case FORMAT_CODE_INT: return Streams.readInt(stream);
            case FORMAT_CODE_LONG: return Streams.readLong(stream);
            case FORMAT_CODE_FLOAT: return Float.intBitsToFloat(Streams.readInt(stream));
            case FORMAT_CODE_DOUBLE: return Double.longBitsToDouble(Streams.readLong(stream));
            case FORMAT_CODE_CHAR: return new String(Streams.read(stream, 4), CHARSET_UTF_32BE);
            case FORMAT_CODE_TIMESTAMP:
                long epochMilli = Streams.readLong(stream);
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZONE_ID_UTC);
            case FORMAT_CODE_UUID: return new UUID(Streams.readLong(stream), Streams.readLong(stream));
            case FORMAT_CODE_VBIN8:
                int length = Byte.toUnsignedInt(Streams.read(stream));
                return Streams.read(stream, length);
            case FORMAT_CODE_VBIN32:
                long l = Integer.toUnsignedLong(Streams.readInt(stream));
                return Streams.read(stream, (int)l);
            case FORMAT_CODE_STR8UTF8:
                int stringLength = Byte.toUnsignedInt(Streams.read(stream));
                return new String(Streams.read(stream, stringLength), CHARSET_UTF_8);
            case FORMAT_CODE_STR32UTF8:
                long longStringLength = Integer.toUnsignedLong(Streams.readInt(stream));
                return new String(Streams.read(stream, (int)longStringLength), CHARSET_UTF_8);
            case FORMAT_CODE_SYM8:
                int symbolLength = Byte.toUnsignedInt(Streams.read(stream));
                return new String(Streams.read(stream, symbolLength), CHARSET_US_ASCII);
            case FORMAT_CODE_SYM32:
                long longSymbolLength = Integer.toUnsignedLong(Streams.readInt(stream));
                return new String(Streams.read(stream, (int)longSymbolLength), CHARSET_US_ASCII);
            default: return null;
        }
    }
}
