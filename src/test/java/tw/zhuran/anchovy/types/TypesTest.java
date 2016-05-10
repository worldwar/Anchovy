package tw.zhuran.anchovy.types;

import org.junit.Test;

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
}
