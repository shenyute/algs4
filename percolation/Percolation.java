import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private int positionToIndex(int i, int j)
  {
    assert i>0 && i <= mGridSize;
    assert j>0 && j <= mGridSize;
    return 1 + (i-1) * mGridSize + (j-1); // from 1~n*n
  }

  private boolean inside(int i, int j)
  {
    if (i < 1 || i > mGridSize || j < 1 || j > mGridSize)
      return false;
    return true;
  }

  private void connect(int p, int q)
  {
    mUnionFind.union(p, q);
    //System.out.println(p +"->" +q);
  }

  public Percolation(int n)               // create n-by-n grid, with all sites blocked
  {
    if (n < 0)
      throw new java.lang.IllegalArgumentException("");
    // index 0 and (n*n+1) are special site where
    // 0 connect to (row 0, column j) where j = 0~n-1
    // n*n+1  connect to (row n-1, column j) where j = 0~n-1
    mUnionFind = new WeightedQuickUnionUF(n*n + 2);
    mGridSize = n;
    // initialize all sites are disconnected.
    mSites = new boolean[n+2][n+2];
    for (int i = 0; i < n+2; i++)
      for (int j = 0; j < n+2; j++)
        mSites[i][j] = false;
    for (int i = 1; i <= mGridSize; i++) {
      // union 0 to 1~n
      int target = positionToIndex(1, i);
      connect(0, target);
      // union n*n+1 to n*n-n+1~n*n
      target = positionToIndex(mGridSize, i);
      connect(mGridSize*mGridSize+1, target);
    }
  }

  public void open(int i, int j)          // open site (row i, column j) if it is not open already
  {
    if (!inside(i, j))
      throw new java.lang.IndexOutOfBoundsException("");
    mSites[i][j] = true;
    int target = positionToIndex(i, j);
    if (inside(i-1, j) && isOpen(i-1, j))
      connect(positionToIndex(i-1, j), target);
    if (inside(i, j-1) && isOpen(i, j-1))
      connect(positionToIndex(i, j-1), target);
    if (inside(i, j+1) && isOpen(i, j+1))
      connect(positionToIndex(i, j+1), target);
    if (inside(i+1, j) && isOpen(i+1, j))
      connect(positionToIndex(i+1, j), target);
  }

  public boolean isOpen(int i, int j)     // is site (row i, column j) open?
  {
    if (!inside(i, j))
      throw new java.lang.IndexOutOfBoundsException("");
    return mSites[i][j];
  }

  public boolean isFull(int i, int j)     // is site (row i, column j) full?
  {
    if (!inside(i, j))
      throw new java.lang.IndexOutOfBoundsException("");
    int target = positionToIndex(i, j);
    return mUnionFind.connected(0, target) && mUnionFind.connected(target, mGridSize*mGridSize+1);
  }

  public boolean percolates()             // does the system percolate?
  {
    return mUnionFind.connected(0, mGridSize*mGridSize+1);
  }

  public static void main(String[] args)  // test client (optional)
  {
    Percolation percolation = new Percolation(3);
    percolation.open(1, 1);
    percolation.open(2, 1);
    percolation.open(2, 2);
    assert percolation.percolates() == false;
    assert percolation.isFull(1, 1) == false;
    assert percolation.isFull(2, 1) == false;
    assert percolation.isFull(2, 2) == false;
    assert percolation.isFull(3, 2) == false;
    percolation.open(3, 2);
    assert percolation.isFull(1, 1) == true;
    assert percolation.isFull(2, 1) == true;
    assert percolation.isFull(2, 2) == true;
    assert percolation.isFull(3, 2) == true;
    assert percolation.percolates() == true;
  }

  private WeightedQuickUnionUF mUnionFind;
  private int mGridSize;
  private boolean[][] mSites;
}
