package heart_of_the_wildlands.quest;

public class MyArrayList<T> {
    private Object[] data;
    private int size;

    public MyArrayList() {
        data = new Object[10]; // initial capacity
        size = 0;
    }

    public void add(T element) {
        if (size == data.length) {
            resize();
        }
        data[size++] = element;
    }

    public T get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return (T) data[index];
    }

    public void set(int index, T element) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        data[index] = element;
    }

    public int size() {
        return size;
    }

    private void resize() {
        Object[] newData = new Object[data.length * 2];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }
}
