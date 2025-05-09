package dataStructure;

public class LinkedList<E> {
    private final Node<E> head = new Node<>(null);
    private int size = 0;

    public static void main(String[] args) {
        LinkedList<Object> list = new LinkedList<>();
        list.add(10, 0);
        list.add(30, 0);
        list.add(20, 1);
        System.out.println(list);
    }

    private static class Node<E> {
        private E element;
        private Node<E> next;

        public Node(E element) {
            this.element = element;
        }
    }

    public void add(E element, int index){
        Node<E> prev = head;   //先找到对应位置的前驱结点
        for (int i = 0; i < index; i++)
            prev = prev.next;
        Node<E> node = new Node<>(element);   //创建新的结点
        node.next = prev.next;   //先让新的节点指向原本在这个位置上的结点
        prev.next = node;   //然后让前驱结点指向当前结点
        size++;   //完事之后一样的，更新size
    }

    public E remove(int index){
        if(index < 0 || index > size - 1)   //同样的，先判断位置是否合法
            throw new IndexOutOfBoundsException("删除位置非法，合法的删除位置为：0 ~ "+(size - 1));
        Node<E> prev = head;
        for (int i = 0; i < index; i++)   //同样需要先找到前驱结点
            prev = prev.next;
        E e = prev.next.element;   //先把待删除结点存放的元素取出来
        prev.next = prev.next.next;  //可以删了
        size--;   //记得size--
        return e;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node<E> node = head.next;   //从第一个结点开始，一个一个遍历，遍历一个就拼接到字符串上去
        while (node != null) {
            builder.append(node.element).append("->");
            node = node.next;
        }
        return builder.toString();
    }


}
