import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  // test whether it is percolates
  private WeightedQuickUnionUF mUnionFind;
  // test whether the site is connect to top row
  private WeightedQuickUnionUF mIsFull;
  private int mGridSize;
  private boolean[][] mSites;
  private boolean mIsPercolates;

  public Percolation(int n)
  {
    if (n <= 0)
      throw new java.lang.IllegalArgumentException("n is smaller than 0");
    // index 0 and (n*n+1) are special site where
    // 0 connect to (row 0, column j) where j = 0~n-1
    // n*n+1  connect to (row n-1, column j) where j = 0~n-1
    mUnionFind = new WeightedQuickUnionUF(n*n + 2);
    mIsFull = new WeightedQuickUnionUF(n*n + 1);
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
      target = positionToIndex(mGridSize, i);
      connect(mGridSize * mGridSize + 1, target);
    }
  }

  private int positionToIndex(int i, int j)
  {
    assert i > 0 && i <= mGridSize;
    assert j > 0 && j <= mGridSize;
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
    final int bottomSite = mGridSize * mGridSize + 1;
    // mIsFull only test whether top row sites can reach to the target site.
    if (p != bottomSite && q != bottomSite)
      mIsFull.union(p, q);
     // System.out.println(p +"->" +q);
  }

  public void open(int i, int j)
  {
    if (!inside(i, j))
      throw new java.lang.IndexOutOfBoundsException("");
    if (isOpen(i, j))
      return;
    assert !mSites[i][j];
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

  public boolean isOpen(int i, int j)
  {
    if (!inside(i, j))
      throw new java.lang.IndexOutOfBoundsException("");
    return mSites[i][j];
  }

  public boolean isFull(int i, int j)
  {
    if (!inside(i, j))
      throw new java.lang.IndexOutOfBoundsException("");
    int target = positionToIndex(i, j);
    return isOpen(i, j)
        && mIsFull.find(0) == mIsFull.find(target);
  }

  public boolean percolates()
  {
    if (mGridSize == 1)
      return isOpen(1, 1);
    if (mIsPercolates)
      return true;
    mIsPercolates = mUnionFind.connected(0, mGridSize * mGridSize + 1);
    return mIsPercolates;
  }

  public static void main(String[] args)
  {
    Percolation percolation = new Percolation(6);
    assert !percolation.isFull(1, 6);
    percolation.open(1, 6);
    assert !percolation.percolates();
    assert percolation.isFull(1, 6);
    assert percolation.isOpen(1, 6);
    percolation.open(2, 6);
    assert percolation.isOpen(2, 6);
    assert percolation.isFull(2, 6);
    assert !percolation.percolates();
    percolation.open(3, 6);
    assert percolation.isOpen(2, 6);
    assert percolation.isFull(2, 6);
    assert percolation.isOpen(3, 6);
    assert percolation.isFull(3, 6);
    assert !percolation.percolates();
    percolation.open(4, 6);
    assert !percolation.percolates();
    percolation.open(5, 6);
    assert !percolation.percolates();
    percolation.open(5, 5);
    assert !percolation.percolates();
    percolation.open(4, 4);
    assert !percolation.percolates();
    percolation.open(3, 4);
    assert !percolation.percolates();
    percolation.open(2, 4);
    assert !percolation.percolates();
    percolation.open(2, 3);
    assert !percolation.percolates();
    percolation.open(2, 2);
    assert !percolation.percolates();
    percolation.open(2, 1);
    assert !percolation.percolates();
    percolation.open(2, 1);
    assert !percolation.percolates();
    percolation.open(3, 1);
    assert !percolation.percolates();
    percolation.open(4, 1);
    assert !percolation.percolates();
    percolation.open(5, 1);
    assert !percolation.percolates();
    percolation.open(5, 4);
    assert percolation.isFull(1, 6);
    percolation.open(6, 1);
    assert percolation.isOpen(6, 1);
    assert percolation.isFull(6, 1);
    assert percolation.percolates();
    percolation.open(6, 3);
    assert !percolation.isFull(6, 3);

    Percolation percolation1 = new Percolation(1);
    assert !percolation1.isFull(1, 1);
    assert !percolation1.isOpen(1, 1);
    assert !percolation1.percolates();
    percolation1.open(1, 1);
    percolation1.open(1, 1);
    percolation1.open(1, 1);
    percolation1.open(1, 1);
    percolation1.open(1, 1);
    percolation1.open(1, 1);
    percolation1.open(1, 1);
    percolation1.open(1, 1);
    assert percolation1.isFull(1, 1);
    assert percolation1.isOpen(1, 1);
    assert percolation1.percolates();
  }
}
