import java.util.ArrayList;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {
  private MinPQ<SearchNode> mPQ;
  private MinPQ<SearchNode> mTwinPQ;
  private boolean mSolvable;
  private int mMoves;
  private ArrayList<Board> mSolution;
  private SearchNode mInit;

  public Solver(Board initial)
  {
    if (initial == null)
      throw new NullPointerException("null");
    mInit = new SearchNode(initial, null);
    mSolution = new ArrayList<Board>();
    int capacity = initial.hamming() + initial.manhattan();
    mPQ = new MinPQ<SearchNode>(capacity, new NodeComparator());
    mPQ.insert(mInit);

    Board twin = initial.twin();
    mTwinPQ = new MinPQ<SearchNode>(capacity, new NodeComparator());
    mTwinPQ.insert(new SearchNode(initial.twin(), null));

    startSolve();
  }

  private class SearchNode
  {
    private Board board;
    private int moves;
    private SearchNode previous;

    public SearchNode(Board b, SearchNode p)
    {
      if (p == null)
        moves = 0;
      else
        moves = p.moves + 1;
      previous = p;
      board = b;
    }
    public int manhattan()
    {
      return board.manhattan() + moves;
    }
    public int hamming()
    {
      return board.hamming() + moves;
    }
    Board getBoard()
    {
      return board;
    }
    SearchNode getPrevious()
    {
      return previous;
    }
  }

  private void collectSolution(SearchNode goal)
  {
    mSolvable = true;
    SearchNode current = goal;
    while (current.getBoard() != mInit.getBoard()) {
      mSolution.add(0, current.getBoard());
      current = current.getPrevious();
    }
    mSolution.add(0, mInit.getBoard());
  }

  private void startSolve()
  {
    boolean stop = false;
    mSolvable = false;
    if (mInit.getBoard().isGoal()) {
      collectSolution(mInit);
      return;
    } else if (mInit.getBoard().twin().isGoal())
      return;
    while (!stop) {
      // StdOut.println("mPQ size=" + mPQ.size());
      if (mPQ.isEmpty() && mTwinPQ.isEmpty())
        return;
      // StdOut.println("manhattan=" + mPQ.min().manhattan() + " moves=" + mPQ.min().moves());
      // StdOut.println(mPQ.min().toString());
      SearchNode goal = Next(mPQ);
      if (goal != null) {
        collectSolution(goal);
        stop = true;
      }
      else {
        SearchNode twinGoal = Next(mTwinPQ);
        if (twinGoal != null) {
          stop = true;
        }
      }
    }
  }

  // return goal if success
  private SearchNode Next(MinPQ<SearchNode> queue)
  {
    if (queue.isEmpty())
      return null;
    SearchNode current = queue.min();
    SearchNode prev = current.getPrevious();
    queue.delMin();
    for (Board neighbor : current.getBoard().neighbors()) {
      if (neighbor.isGoal())
        return new SearchNode(neighbor, current);
      else if (prev == null || !neighbor.equals(prev.getBoard())) {
        queue.insert(new SearchNode(neighbor, current));
      } else {
        // already executed.
      }
    }
    return null;
  }

  private static class NodeComparator implements Comparator<SearchNode>
  {
    public int compare(SearchNode o1, SearchNode o2)
    {
      if (o1.manhattan() != o2.manhattan())
        return o1.manhattan() - o2.manhattan();
      return o1.hamming() - o2.hamming();
    }
  }

  public boolean isSolvable()
  {
    return mSolvable;
  }

  public int moves()
  {
    return mSolution.size() - 1;
  }

  public Iterable<Board> solution()
  {
    if (!mSolvable)
      return null;
    return mSolution;
  }

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
    /*
    StdOut.println("No solution possible");
    Board initial2 = new Board(blocks);
    HashSet<Board> set = new HashSet<Board>();
    set.add(initial);
    assert initial.hashCode() == initial2.hashCode();
    assert initial.equals(initial2);
    assert set.size() == 1;
    assert set.contains(initial2);
    */
    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
      StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution())
        StdOut.println(board);
    }
  }
}
