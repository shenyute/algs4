import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {

  public PercolationStats(int n, int trials)    // perform trials independent experiments on an n-by-n grid
  {
    if (n <= 0 || trials <= 0)
      throw new java.lang.IllegalArgumentException("");
    final int totalSites = n * n;
    mThreshold = new double [trials];
    mTrials = trials;
    for (int trial = 0; trial < trials; trial++) {
      Percolation percolation = new Percolation(n);
      int openSites = 0;
      while (!percolation.percolates()) {
        int i = StdRandom.uniform(n) + 1;
        int j = StdRandom.uniform(n) + 1;
        if (!percolation.isOpen(i, j)) {
          openSites++;
          percolation.open(i, j);
        }
      }
      mThreshold[trial] = 1.0 * openSites / totalSites;
      //System.out.println("frac=" + mThreshold[trial]);
    }
  }

  public double mean()                          // sample mean of percolation threshold
  {
    return StdStats.mean(mThreshold);
  }

  public double stddev()                        // sample standard deviation of percolation threshold
  {
    return StdStats.stddev(mThreshold);
  }

  public double confidenceLo()                  // low  endpoint of 95% confidence interval
  {
    double u = mean();
    double a = stddev();
    return u - 1.96 * a / Math.sqrt(mTrials);
  }

  public double confidenceHi()                  // high endpoint of 95% confidence interval
  {
    double u = mean();
    double a = stddev();
    return u + 1.96 * a / Math.sqrt(mTrials);
  }

  public static void main(String[] args)    // test client (described below)
  {
    try {
      int n = Integer.parseInt(args[0]);
      int trials = Integer.parseInt(args[1]);
      PercolationStats stats = new PercolationStats(n, trials);
      StdOut.println("mean                     = " + stats.mean());
      StdOut.println("stddev                   = " + stats.stddev());
      StdOut.println("95% confidence interval  = " + stats.confidenceLo() + ", " + stats.confidenceHi());
    } catch (NumberFormatException e) {
      StdOut.println("Argument must be an integer");
      return;
    }
  }

  private double[] mThreshold;
  private int mTrials;
}
