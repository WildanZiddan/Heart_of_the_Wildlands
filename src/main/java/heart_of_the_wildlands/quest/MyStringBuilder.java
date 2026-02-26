package heart_of_the_wildlands.quest;

public class MyStringBuilder {
    private char[] buffer;
    private int size;

    public MyStringBuilder() {
        buffer = new char[1000]; // You can adjust the size as needed
        size = 0;
    }

    public MyStringBuilder append(String str) {
        if (str == null) return this;
        ensureCapacity(size + str.length());
        for (int i = 0; i < str.length(); i++) {
            buffer[size++] = str.charAt(i);
        }
        return this;
    }

    public MyStringBuilder append(char c) {
        ensureCapacity(size + 1);
        buffer[size++] = c;
        return this;
    }

    public MyStringBuilder append(int num) {
        return append(Integer.toString(num));
    }

    public String toString() {
        return new String(buffer, 0, size);
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > buffer.length) {
            int newCapacity = Math.max(buffer.length * 2, minCapacity);
            char[] newBuffer = new char[newCapacity];
            for (int i = 0; i < size; i++) {
                newBuffer[i] = buffer[i];
            }
            buffer = newBuffer;
        }
    }
}
