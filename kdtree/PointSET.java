import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class PointSET {
  private SET<Point2D> mPoints;

  public PointSET() {
    mPoints = new SET<Point2D>();
  }

  public boolean isEmpty() {
    return mPoints.isEmpty();
  }

  public int size() {
    return mPoints.size();
  }

  public void insert(Point2D p) {
    if (p == null)
      throw new java.lang.NullPointerException("null");
    mPoints.add(p);
  }

  public boolean contains(Point2D p) {
    if (p == null)
      throw new java.lang.NullPointerException("null");
    return mPoints.contains(p);
  }

  public void draw() {
    for (Point2D p : mPoints) {
      StdDraw.point(p.x(), p.y());
    }
  }

  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null)
      throw new java.lang.NullPointerException("null");
    ArrayList<Point2D> points = new ArrayList<Point2D>();
    for (Point2D p : mPoints) {
      if (rect.contains(p))
        points.add(p);
    }
    return points;
  }

  public Point2D nearest(Point2D target) {
    if (target == null)
      throw new java.lang.NullPointerException("null");
    Point2D min = null;
    double minDistance = 0;
    for (Point2D p : mPoints) {
      double distance = p.distanceTo(target);
      if (min == null || distance < minDistance) {
        min = p;
        minDistance = distance;
      }
    }
    return min;
  }


  public static void main(String[] args) {
    RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
    StdDraw.enableDoubleBuffering();
  }

}
