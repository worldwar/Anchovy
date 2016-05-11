package tw.zhuran.anchovy.types;

import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
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
}
