import java.lang.Math;
import java.lang.StringBuilder;
import java.lang.System;
import java.lang.NullPointerException;
import java.util.ArrayList;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Board {
  final private short[][] mBlocks;
  final private int mSize;
  private int mHamming;
  private int mManhattan;
  private int mBlankIndex;
  private String mString;
  private ArrayList<Board> mNeighbors;

  public Board(int[][] blocks)
  {
    if (blocks == null)
      throw new NullPointerException("null");
    mSize = blocks.length;
    mBlocks = new short [mSize][mSize];
    mBlankIndex = 0;
    // StringBuilder hash = new StringBuilder(3 * mSize * mSize);
    for (int i = 0; i < mSize; i++)
      for (int j = 0; j < mSize; j++) {
        mBlocks[i][j] = (short)blocks[i][j];
        // hash.append(blocks[i][j]);
        if (mBlocks[i][j] == 0) {
          mBlankIndex = i * mSize + j;
        }
      }
    calculateHamming();
    calculateManhattan();
  }
  private void calculateHamming()
  {
    mHamming = 0;
    for (int i = 0; i < mSize; i++)
      for (int j = 0; j < mSize; j++)
        if (mBlocks[i][j] != 0 && mBlocks[i][j] != mSize * i + j + 1)
          mHamming++;
  }
  private void calculateManhattan()
  {
    mManhattan = 0;
    for (int i = 0; i < mSize; i++)
      for (int j = 0; j < mSize; j++)
        if (mBlocks[i][j] != 0) {
          int target = mBlocks[i][j];
          int targetI = (target-1) / mSize;
          int targetJ = (target-1) % mSize;
          // StdOut.println("block: " + mBlocks[i][j] + " " + i + "," + j + " -> " + targetI + "," + targetJ);
          mManhattan += Math.abs(targetI - i) + Math.abs(targetJ - j);
        }
  }

  public int dimension()
  {
    return mSize;
  }
  public int hamming()
  {
    return mHamming;
  }
  public int manhattan()
  {
    return mManhattan;
  }
  public boolean isGoal()
  {
    return mHamming == 0 && mManhattan == 0;
  }

  private void swap(int[][] blocks, int i, int j, int k, int l)
  {
    int t = blocks[i][j];
    blocks[i][j] = blocks[k][l];
    blocks[k][l] = t;
  }

  public Board twin()
  {
    int [][] blocks = new int [mSize][mSize];
    for (int i = 0; i < mSize; i++)
      for (int j = 0; j < mSize; j++)
        blocks[i][j] = mBlocks[i][j];
    boolean swapped = false;
    int index = 0;
    while (!swapped) {
      if (blocks[index / mSize][index % mSize] != 0
          && blocks[(index+1) / mSize][(index+1) % mSize] != 0) {
        swap(blocks, index/mSize, index%mSize, (index+1)/mSize, (index+1)%mSize);
        swapped = true;
      }
      index++;
    }
    return new Board(blocks);
  }
  /*
  public String hash()
  {
    return mHash;
  }
  public int hashCode()
  {
    return mHash.hashCode();
  }
  */
  public boolean equals(Object y)
  {
    if (y == null)
      return false;
    if (y == this)
      return true;
    if (y.getClass() != this.getClass())
      return false;
    Board that = (Board)y;
    if (mSize != that.mSize)
      return false;
    if (mHamming != that.mHamming || mManhattan != that.mManhattan)
      return false;
    for (int i = 0; i < mSize; i++)
      for (int j = 0; j < mSize; j++)
        if (mBlocks[i][j] != that.mBlocks[i][j])
          return false;
    return true;
  }

  private int[][] cloneArray(short[][] src) {
    int length = src.length;
    int[][] target = new int[length][src[0].length];
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < length; j++)
        target[i][j] = src[i][j];
    }
    return target;
  }

  private ArrayList<Board> createNeighbors()
  {
    ArrayList<Board> neighbors = new ArrayList<Board>();
    int[][] blocks = cloneArray(mBlocks);
    for (int i = 0; i < 4; i++) {
      int blankI = mBlankIndex / mSize;
      int blankJ = mBlankIndex % mSize;
      int targetI = blankI;
      int targetJ = blankJ;
      switch (i) {
        case 0: // swap top
          targetI = blankI - 1;
          break;
        case 1: // swap bottom
          targetI = blankI + 1;
          break;
        case 2: // swap left
          targetJ = blankJ - 1;
          break;
        case 3: // swap right
          targetJ = blankJ + 1;
          break;
      }
      if (targetI >= 0 && targetI < mSize && targetJ >= 0 && targetJ < mSize) {
        swap(blocks, blankI, blankJ, targetI, targetJ);
        Board b = new Board(blocks);
        swap(blocks, blankI, blankJ, targetI, targetJ);
        neighbors.add(b);
      }
    }
    return neighbors;
  }

  public Iterable<Board> neighbors()
  {
    if (mNeighbors == null)
      mNeighbors = createNeighbors();
    return mNeighbors;
  }

  private String buildString()
  {
    StringBuilder tmp = new StringBuilder();
    tmp.append(mSize);
    tmp.append("\n");
    for (int i = 0; i < mSize; i++) {
      for (int j = 0; j < mSize; j++) {
        tmp.append(" ");
        tmp.append(mBlocks[i][j]);
      }
      tmp.append("\n");
    }
    return tmp.toString();
  }
  public String toString()
  {
    if (mString == null)
      mString = buildString();
    return mString;
  }

  /*
  public boolean isSolvable()
  {
    // http://www.cs.princeton.edu/courses/archive/fall12/cos226/assignments/8puzzle.html
    // TODO
    return false;
  }
  */
  public static void main(String[] args)
  {
    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] blocks = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);
    StdOut.println(initial.toString());
    StdOut.println("hamming: " + initial.hamming() + " manhattan: " + initial.manhattan());
    StdOut.println("isGoal: " + initial.isGoal());
    StdOut.println("neighbors:");
    for (Board neighbor : initial.neighbors())
      StdOut.println(neighbor.toString());
    StdOut.println("twin:");
    Board twin = initial.twin();
    StdOut.println(twin.toString());
  }
}
