import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
  private Item[] mItems;
  private int mCapacity;
  private int mSize;

  public RandomizedQueue()
  {
    mItems = (Item []) new Object[2];
    mCapacity = 2;
    mSize = 0;
  }

  private void resize(int capacity)
  {
      assert capacity >= mSize;
      Item[] items = (Item[]) new Object[capacity];
      for (int i = 0; i < mSize; i++)
        items[i] = mItems[i];
      mItems = items;
      mCapacity = capacity;
  }

  public boolean isEmpty()
  {
    return mSize == 0;
  }
  public int size()
  {
    return mSize;
  }
  public void enqueue(Item item)
  {
    if (item == null)
      throw new java.lang.NullPointerException("enqueue null");
    if (mSize == mCapacity)
      resize(mCapacity * 2);
    mItems[mSize] = item;
    mSize++;
  }

  public Item dequeue()
  {
    if (isEmpty())
      throw new java.util.NoSuchElementException("dequeue empty");
    int target = StdRandom.uniform(mSize);
    Item item = mItems[target];
    mItems[target] = mItems[mSize-1];
    mSize--;
    if (mSize > 0 && mSize == mCapacity / 4)
      resize(mCapacity / 2);
    return item;
  }

  public Item sample()
  {
    if (isEmpty())
      throw new java.util.NoSuchElementException("sample empty");
    int target = StdRandom.uniform(mSize);
    return mItems[target];
  }

  public Iterator<Item> iterator()
  {
    return new RandomizedQueueIterator();
  }

  private class RandomizedQueueIterator implements Iterator<Item> {
    private int[] mIndex;
    private int mIndexSize;

    public RandomizedQueueIterator() {
      mIndex = new int [mSize];
      mIndexSize = mSize;
      for (int i = 0; i < mSize; i++)
        mIndex[i] = i;
    }

    public boolean hasNext()  {
      return mIndexSize > 0;
    }

    public void remove()      { throw new java.lang.UnsupportedOperationException();  }

    public Item next() {
      if (!hasNext()) throw new java.util.NoSuchElementException();
      int targetIndex = StdRandom.uniform(mIndexSize);
      int target = mIndex[targetIndex];
      Item item = mItems[target];
      mIndex[targetIndex] = mIndex[mIndexSize-1];
      mIndexSize--;
      return item;
    }
  }

  public static void main(String[] args)
  {
    RandomizedQueue<String> queue = new RandomizedQueue<String>();
    queue.enqueue("A");
    queue.enqueue("B");
    queue.enqueue("C");
    queue.enqueue("D");
    queue.enqueue("E");
    queue.enqueue("F");
    queue.enqueue("G");
    queue.enqueue("H");
    queue.enqueue("I");
    for (String i : queue)
      StdOut.println(i + " ");
  }
}
