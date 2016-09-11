import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class Subset {
  public static void main(String[] args) {
    int k;
    try {
      k = Integer.parseInt(args[0]);
      boolean stop = false;
      RandomizedQueue<String> queue = new RandomizedQueue<String>();
      while (!stop) {
        try {
          String str = StdIn.readString();
          // StdOut.println("input: '" + str + "'");
          queue.enqueue(str);
        } catch (NoSuchElementException e) {
          stop = true;
        }
      }
      for (int i = 0; i < k; i++) {
        String str = queue.dequeue();
        StdOut.println(str);
      }
    } catch (NumberFormatException e) {
      StdOut.println("Argument must be an integer");
      return;
    }
  }
}
