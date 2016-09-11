
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

    private int n;         // number of elements on queue
    private Node first;    // beginning of queue
    private Node last;     // end of queue

    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

   public Deque()
   {
     n = 0;
     first = null;
     last = null;
   }

   public boolean isEmpty()
   {
     return n == 0;
   }

   public int size()
   {
     return n;
   }

   public void addFirst(Item item)
   {
     if (item == null)
       throw new java.lang.NullPointerException("addFirst null");
     Node node = new Node();
     node.item = item;
     node.next = first;
     node.prev = null;
     if (isEmpty()) {
       last = node;
       first = node;
     } else {
       Node oldFirst = first;
       first = node;
       oldFirst.prev = first;
     }
     n++;
   }

   public void addLast(Item item)
   {
     if (item == null)
       throw new java.lang.NullPointerException("addLast null");
     Node node = new Node();
     node.item = item;
     node.next = null;
     node.prev = last;
     if (isEmpty()) {
       first = node;
       last = node;
     } else {
       last.next = node;
       last = node;
     }
     n++;
   }

   public Item removeFirst()
   {
     if (isEmpty())
       throw new java.util.NoSuchElementException("remoteFirst");
     Node oldFirst = first;
     first = first.next;
     if (first != null)
       first.prev = null;
     n--;
     if (isEmpty())
       last = first;
     return oldFirst.item;
   }

   public Item removeLast()
   {
     if (isEmpty())
       throw new java.util.NoSuchElementException("remoteFirst");
    Node oldLast = last;
    last = oldLast.prev;
    if (last != null)
      last.next = null;
    n--;
    if (isEmpty())
      first = last;
    return oldLast.item;
   }

   public Iterator<Item> iterator()
   {
     return new DequeIterator();
   }

    // an iterator, doesn't implement remove() since it's optional
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new java.lang.UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

   public static void main(String[] args)
   {
     Deque<String> deque = new Deque<String>();
     deque.addFirst("1");
     deque.removeLast();
     for (String i : deque)
       StdOut.println(i + " ");
     StdOut.println("----");
     deque.addFirst("1");
     deque.addFirst("2");
     deque.removeFirst();
     deque.addLast("3");
     deque.removeLast();
     for (String i : deque)
       StdOut.println(i + " ");
     StdOut.println("----");
     deque.addLast("4");
     for (String i : deque)
       StdOut.println(i + " ");
     StdOut.println("----");
   }
}
