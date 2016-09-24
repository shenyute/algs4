import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.ArrayList;

public class FastCollinearPoints {
  private ArrayList<LineSegment> mLines;
  public FastCollinearPoints(Point[] input) {
    if (input == null)
      throw new java.lang.NullPointerException("null array");
    for (int i = 0; i < input.length; i++) {
      if (input[i] == null)
        throw new java.lang.NullPointerException("null point");
    }
    Point[] points = Arrays.copyOf(input, input.length);
    Arrays.sort(points);
    for (int i = 1; i < points.length; i++) {
      if (points[i].compareTo(points[i-1]) == 0)
        throw new java.lang.IllegalArgumentException("repeated point");
    }
    mLines = new ArrayList<>();
    Point[] slopes = new Point[points.length - 1];
    // select base point and collect other points slope to base point.
    for (int i = 0; i < points.length; i++) {
      Point base = points[i];
      // copy points to slope array which will be sort according to slope to base
      // point.
      int idx = 0;
      for (int j = 0; j < points.length; j++) {
        if (i != j) {
          slopes[idx] = points[j];
          idx++;
        }
      }
      Arrays.sort(slopes, base.slopeOrder());
      for (int j = 0; j < slopes.length;) {
        double slope = base.slopeTo(slopes[j]);
        // find all equal slope adjacent
        int k = j + 1;
        for (; k < slopes.length && base.slopeTo(slopes[k]) == slope; k++)
          ;
        boolean ordered = true;
        if (k - j > 2) {
          /*
          StdOut.print("base " + base.toString() + "\n");
          for (int t = j; t < k; t++)
            StdOut.print(slopes[t].toString());
          StdOut.print("\n");
          */
          // more than 3 points are collinear to base
          // j .. k - 1 points are equal
          // make sure the segment is ordered
          if (base.compareTo(slopes[j]) > 0)
            ordered = false;
          for (int t = j; t < k && t + 1 < k; t++)
            if (slopes[t].compareTo(slopes[t+1]) > 0)
              ordered = false;
          if (ordered) {
            // StdOut.print("order!!!\n");
            mLines.add(new LineSegment(base, slopes[k-1]));
          }
        }
        j = k;
      }
    }
  }

  public int numberOfSegments() {
    return mLines.size();
  }

  public LineSegment[] segments() {
    LineSegment[] lines = new LineSegment[mLines.size()];
    for (int i = 0; i < mLines.size(); i++)
      lines[i] = mLines.get(i);
    return lines;
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
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
}
