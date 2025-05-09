package dataStructure;

public class HashTable<E> {
    private final int TABLE_SIZE = 10;
    private final Object[] table = new Object[TABLE_SIZE];


    private int hash(E e){
        int i = e.hashCode();
        return i % TABLE_SIZE;
    }

    public void insert(E object){
        int i = hash(object);
        table[i]=object;
    }

    public boolean contains(E e){
        int hash = hash(e);
        return table[hash]== e;
    }
}
