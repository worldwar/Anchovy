package tw.zhuran.anchovy.types;

public class Lists {
    public static byte[] copy(byte[] source, int start, int length) {
        assert source != null : "source should not be null!";
        assert source.length > start && start >= 0 : "start index should between 0 and length of source excluded!";
        assert length > 0 : "target length should be greater than 0!";
        int actualLength = source.length - start < length ? source.length - start : length;
        byte[] target = new byte[actualLength];
        System.arraycopy(source, start, target, 0, actualLength);
        return target;
    }
}
