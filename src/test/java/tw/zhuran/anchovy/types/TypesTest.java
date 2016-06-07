package tw.zhuran.anchovy.types;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.junit.Test;

import java.math.BigInteger;
import java.time.*;
import java.util.*;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

public class TypesTest {
    @Test
    public void shouldDecodeNullValue() {
        byte[] bytes = new byte[]{Types.FORMAT_CODE_NULL};
        assertThat(Types.decode(bytes), is(nullValue()));
    }

    @Test
    public void shouldDecodeBooleanValue() {
        byte[] falseBytes = new byte[]{0x56, 0x00};
        byte[] trueBytes = new byte[]{0x56, 0x01};
        byte[] falseValueBytes = new byte[]{0x42};
        byte[] trueValueBytes = new byte[]{0x41};
        assertThat(Types.decode(falseBytes), is((Object)Boolean.FALSE));
        assertThat(Types.decode(trueBytes), is((Object)Boolean.TRUE));
        assertThat(Types.decode(falseValueBytes), is((Object)Boolean.FALSE));
        assertThat(Types.decode(trueValueBytes), is((Object)Boolean.TRUE));
    }

    @Test
    public void shouldDecodeUbyteType() {
        byte[] ubyteBytes = new byte[]{0x50, 0x00};
        assertThat(Types.decode(ubyteBytes), is((Object)Integer.valueOf(0)));
        byte[] ubyteBytes56 = new byte[]{0x50, 0x38};
        assertThat(Types.decode(ubyteBytes56), is((Object)Integer.valueOf(56)));
        byte[] ubyteBytes156 = new byte[]{0x50, (byte)0x9C};
        assertThat(Types.decode(ubyteBytes156), is((Object)Integer.valueOf(156)));
    }

    @Test
    public void shouldDecodeUshortType() {
        byte[] ushortBytes = new byte[]{0x60, 0x00, 0x00};
        assertThat(Types.decode(ushortBytes), is((Object)Integer.valueOf(0)));
        byte[] ushortBytes56 = new byte[]{0x60, 0x00, 0x38};
        assertThat(Types.decode(ushortBytes56), is((Object)Integer.valueOf(56)));
        byte[] ushortBytes45678 = new byte[]{0x60, (byte)0xB2, (byte)0x6E};
        assertThat(Types.decode(ushortBytes45678), is((Object)Integer.valueOf(45678)));
    }

    @Test
    public void shouldDecodeUintType() {
        byte[] uintBytes = new byte[]{0x70, 0x00, 0x00, 0x00, 0x00};
        assertThat(Types.decode(uintBytes), is((Object)0L));
        byte[] uintBytes15 = new byte[]{0x70, 0x00, 0x00, 0x00, 0x0F};
        assertThat(Types.decode(uintBytes15), is((Object)15L));
        byte[] uintBytes2882400018 = new byte[]{0x70, (byte)0xAB, (byte)0xCD, (byte)0xEF, 0x12};
        assertThat(Types.decode(uintBytes2882400018), is((Object)2882400018L));

        byte[] smallUnitBytes = new byte[]{0x52, (byte)0xCD};
        assertThat(Types.decode(smallUnitBytes), is((Object)205));

        byte[] uint0Bytes = new byte[]{0x43};
        assertThat(Types.decode(uint0Bytes), is((Object)0));
    }

    @Test
    public void shouldDecodeUlongType() {
        byte[] ulongBytes = new byte[] {(byte)0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        assertThat(Types.decode(ulongBytes), is((Object)new BigInteger(new byte[]{0x00})));
        byte[] ulongBytes4096 = new byte[] {(byte)0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x10, 0x00};
        assertThat(((BigInteger)Types.decode(ulongBytes4096)).intValue(), is(4096));
        byte[] ulongBytes11150031900141442680 = new byte[] {(byte)0x80, (byte)0x9a, (byte)0xbc, (byte)0xde, (byte)0xf0, (byte)0x12, 0x34, 0x56, 0x78};
        assertThat(Types.decode(ulongBytes11150031900141442680), is((Object)new BigInteger("11150031900141442680")));

        byte[] smallulongBytes = new byte[] {(byte)0x53, (byte)0x9a};
        assertThat(Types.decode(smallulongBytes), is((Object)154));

        byte[] ulong0Bytes = new byte[] {(byte)0x44};
        assertThat(Types.decode(ulong0Bytes), is((Object)0));
    }

    @Test
    public void shouldDecodeByteType() {
        byte[] byteBytes = new byte[]{0x51, (byte)0x99};
        assertThat(Types.decode(byteBytes), is((Object)(byte)0x99));
    }

    @Test
    public void shouldDecodeShortType() {
        byte[] shortBytes = new byte[]{0x61, (byte)0x99, (byte)0x22};
        assertThat(Types.decode(shortBytes), is((Object)(short)0x9922));
    }

    @Test
    public void shouldDecodeIntType() {
        byte[] intBytes = new byte[]{0x71, (byte)0xdd, (byte)0x11, (byte)0x98, (byte)0x0C};
        assertThat(Types.decode(intBytes), is((Object)(int)0xdd11980c));
    }

    @Test
    public void shouldDecodeLongType() {
        byte[] longBytes = new byte[]{(byte)0x81, (byte)0xff, (byte)0xcc, (byte)0xdd, (byte)0x99, (byte)0x38, (byte)0x91, (byte)0x05, (byte)0x55};
        assertThat(Types.decode(longBytes), is((Object)(long)0xffccdd9938910555L));
    }

    @Test
    public void shouldDecodeFloatType() {
        byte[] floatBytes = new byte[]{(byte)0x72, (byte)0xa0, (byte)0xb1, (byte)0xc2, (byte)0xd3};
        assertThat(Types.decode(floatBytes), is((Object)Float.intBitsToFloat(Ints.fromByteArray(Lists.copy(floatBytes, 1, 4)))));
    }

    @Test
    public void shouldDecodeDoubleType() {
        byte[] doubleBytes = new byte[]{(byte)0x82, (byte)0xa0, (byte)0xb1, (byte)0xc2, (byte)0xd3, (byte)0xe4, (byte)0xf5, (byte)0x06, (byte)0x17};
        assertThat(Types.decode(doubleBytes), is((Object) Double.longBitsToDouble((Longs.fromByteArray(Lists.copy(doubleBytes, 1, 8))))));
    }

    @Test
    public void shouldDecodeCharType() {
        byte[] charBytes = new byte[]{(byte)0x73, (byte)0x00, (byte)0x00, (byte)0x67, (byte)0x31};
        assertThat(Types.decode(charBytes), is((Object) "朱"));
    }

    @Test
    public void shouldDecodeTimestampType() {
        byte[] timestampBytes = new byte[]{(byte)0x83, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.of("UTC"));
        assertThat(Types.decode(timestampBytes), is((Object) localDateTime));
        timestampBytes = new byte[]{(byte)0x83, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x54, (byte)0xa8, (byte)0xad, (byte)0xa5, (byte)0x68};
        localDateTime = LocalDateTime.of(2016, 5, 13, 5, 53, 53);
        assertThat(Types.decode(timestampBytes), is((Object)localDateTime));
    }

    @Test
    public void shouldDecodeUUIDType() {
        UUID uuid = UUID.fromString("5d4f0b64-76e6-4fc3-9dbb-a33387f9a105");
        byte[] uuidBytes = new byte[]{(byte)0x98, (byte)0x5d, (byte)0x4f, (byte)0x0b, (byte)0x64,
                (byte)0x76, (byte)0xe6, (byte)0x4f, (byte)0xc3,
                (byte)0x9d, (byte)0xbb, (byte)0xa3, (byte)0x33,
                (byte)0x87, (byte)0xf9, (byte)0xa1, (byte)0x05
        };
        assertThat(Types.decode(uuidBytes), is((Object)uuid));
    }

    @Test
    public void shouldDecodeBinaryType() {
        byte[] binaryBytes = new byte[]{(byte) 0xa0, (byte)0x05, (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04, (byte)0x05};
        assertArrayEquals((byte[])Types.decode(binaryBytes), Lists.copy(binaryBytes, 2, 5));
        binaryBytes = new byte[]{(byte) 0xb0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x05, (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04, (byte)0x05};
        assertArrayEquals((byte[])Types.decode(binaryBytes), Lists.copy(binaryBytes, 5, 5));
    }

    @Test
    public void shouldDecodeStringType() {
        byte[] stringBytes = new byte[]{(byte) 0xa1, (byte)0x06, (byte)0xE6, (byte)0x9C, (byte)0xB1, (byte)0xE7, (byte)0x84, (byte)0xB6};
        assertThat(Types.decode(stringBytes), is((Object) "朱然"));
        stringBytes = new byte[]{(byte) 0xb1, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x06, (byte)0xE6, (byte)0x9C, (byte)0xB1, (byte)0xE7, (byte)0x84, (byte)0xB6};
        assertThat(Types.decode(stringBytes), is((Object) "朱然"));
    }

    @Test
    public void shouldDecodeSymbolType() {
        byte[] symbolBytes = new byte[]{(byte) 0xa3, (byte)0x04, (byte)'f', (byte)'i', (byte)'s', (byte)'h'};
        assertThat(Types.decode(symbolBytes), is((Object) "fish"));
        symbolBytes = new byte[]{(byte) 0xb3, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x04, (byte)'f', (byte)'i', (byte)'s', (byte)'h'};
        assertThat(Types.decode(symbolBytes), is((Object) "fish"));
    }

    @Test
    public void shouldDecodeListType() {
        byte[] listBytes = new byte[]{(byte)0x45};
        assertThat(Types.decode(listBytes), is((Object) new ArrayList<Object>()));
        listBytes = new byte[]{(byte)0xc0, (byte)0x04, (byte)0x03, (byte)0x41, (byte)0x42, (byte)0x41};
        assertThat(Types.decode(listBytes), is((Object) com.google.common.collect.Lists.newArrayList(true, false, true)));
        listBytes = new byte[]{(byte)0xd0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x0f, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte) 0xb1, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x06, (byte)0xE6, (byte)0x9C, (byte)0xB1, (byte)0xE7, (byte)0x84, (byte)0xB6};
        assertThat(Types.decode(listBytes), is((Object) com.google.common.collect.Lists.newArrayList("朱然")));
    }

    @Test
    public void shouldDecodeMapType() {
        byte[] mapBytes = new byte[]{(byte)0xc1, (byte)0x0f, (byte)0x01, (byte) 0xa3, (byte)0x04, (byte)'n', (byte)'a', (byte)'m', (byte)'e', (byte) 0xa1, (byte)0x06, (byte)0xE6, (byte)0x9C, (byte)0xB1, (byte)0xE7, (byte)0x84, (byte)0xB6};
        Map<Object, Object> map = new LinkedHashMap<>();
        map.put("name", "朱然");
        assertMapThat((Map)Types.decode(mapBytes), map);
        mapBytes = new byte[]{(byte)0xd1,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x19,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02,
                (byte) 0xa3, (byte) 0x04, (byte) 'n', (byte) 'a', (byte) 'm', (byte) 'e',
                (byte) 0xa1, (byte) 0x06, (byte) 0xE6, (byte) 0x9C, (byte) 0xB1, (byte) 0xE7, (byte) 0x84, (byte) 0xB6,
                (byte) 0xa3, (byte) 0x03, (byte) 'a', (byte) 'g', (byte) 'e',
                (byte) 0x50, (byte) 0x1c
        };
        map = new LinkedHashMap<>();
        map.put("name", "朱然");
        map.put("age", 28);
        assertMapThat((Map)Types.decode(mapBytes), map);
    }

    @Test
    public void shouldDecodeArrayType() {
        byte[] arrayBytes = new byte[]{(byte)0xe0,
                (byte) 0x0b, (byte) 0x02,
                (byte) 0xa3,
                (byte) 0x04, (byte) 'n', (byte) 'a', (byte) 'm', (byte) 'e',
                (byte) 0x03, (byte) 'a', (byte) 'g', (byte) 'e'
        };
        assertThat(Types.decode(arrayBytes), is((Object) com.google.common.collect.Lists.newArrayList("name", "age")));
        arrayBytes = new byte[]{(byte)0xf0,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0e,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02,
                (byte) 0xa3,
                (byte) 0x04, (byte) 'n', (byte) 'a', (byte) 'm', (byte) 'e',
                (byte) 0x03, (byte) 'a', (byte) 'g', (byte) 'e'
        };
        assertThat(Types.decode(arrayBytes), is((Object) com.google.common.collect.Lists.newArrayList("name", "age")));
    }

    public void assertMapThat(Map a, Map b) {
        assertThat(mapEquals(a, b), is(true));
    }

    boolean mapEquals(Map a, Map b) {
        if (a == null) {
            return b == null;
        }
        if (b == null) {
            return false;
        }
        Iterator<Map.Entry> ai = a.entrySet().iterator();
        Iterator<Map.Entry> bi = b.entrySet().iterator();
        while (ai.hasNext() && bi.hasNext()) {
            Map.Entry ae = ai.next();
            Map.Entry be = bi.next();
            if (!(ae.getKey().equals(be.getKey()) && ae.getValue().equals(be.getValue()))) {
                return false;
            }
        }
        return !(ai.hasNext() || bi.hasNext());
    }
}
