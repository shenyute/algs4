import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.ArrayList;

public class BruteCollinearPoints {
  private LineSegment[] mLines;
  private int mLineNum;

  public BruteCollinearPoints(Point[] points) {
    if (points == null)
      throw new java.lang.NullPointerException("null array");
    for (int i = 0; i < points.length; i++) {
      if (points[i] == null)
        throw new java.lang.NullPointerException("null point");
    }
    ArrayList<LineSegment> lines = new ArrayList<>();
    Arrays.sort(points);
    for (int i = 1; i < points.length; i++) {
      if (points[i] == points[i-1])
        throw new java.lang.IllegalArgumentException("repeated point");
    }
    mLineNum = 0;
    // run all combination
    for (int i = 0; i < points.length; i++)
      for (int j = i + 1; j < points.length; j++)
        for (int k = j + 1; k < points.length; k++)
          for (int l = k + 1; l < points.length; l++) {
            if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k])
                && points[i].slopeTo(points[k]) == points[i].slopeTo(points[l]))
              lines.add(new LineSegment(points[i], points[l]));
          }
    mLines = new LineSegment[lines.size()];
    mLineNum = lines.size();
    for (int i = 0; i < mLineNum; i++)
      mLines[i] = lines.get(i);
  }

  public int numberOfSegments() {
    return mLineNum;
  }

  public LineSegment[] segments() {
    return mLines;
  }

  public static void main(String[] args) {
    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
}
