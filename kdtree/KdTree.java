import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {
  private Node mRoot;
  private int mSize;

  public KdTree() {
  }

  private class Node {
    public Point2D point;
    Node left;
    Node right;
    public Node(Point2D p) {
      point = p;
    }
  }

  public boolean isEmpty() {
    return mRoot == null;
  }

  public int size() {
    return mSize;
  }

  private Node insertNode(Node node, Node target, boolean useX) {
    if (node == null) {
      mSize++;
      return target;
    }
    // StdOut.println("node=" + node.point + " target=" + target.point);
    if (target.point.equals(node.point)) {
      return node;
    }
    if (useX) {
      if (target.point.x() < node.point.x())
        node.left = insertNode(node.left, target, false);
      else
        node.right = insertNode(node.right, target, false);
    } else {
      if (target.point.y() < node.point.y())
        node.left = insertNode(node.left, target, true);
      else
        node.right = insertNode(node.right, target, true);
    }
    return node;
  }

  public void insert(Point2D p) {
    if (p == null)
      throw new java.lang.NullPointerException("null");
    mRoot = insertNode(mRoot, new Node(p), true);
    // StdOut.println("");
  }

  private boolean searchNode(Node node, Point2D p, boolean useX) {
    if (node == null)
      return false;
    if (p.equals(node.point))
      return true;
    if (useX) {
      if (p.x() < node.point.x())
        return searchNode(node.left, p, false);
      else
        return searchNode(node.right, p, false);
    } else {
      if (p.y() < node.point.y())
        return searchNode(node.left, p, true);
      else
        return searchNode(node.right, p, true);
    }
  }

  public boolean contains(Point2D p) {
    if (p == null)
      throw new java.lang.NullPointerException("null");
    return searchNode(mRoot, p, true);
  }

  private void drawLine(Node node, RectHV rect, boolean useX) {
    if (node == null)
      return;

    StdDraw.setPenRadius(0.01);
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.point(node.point.x(), node.point.y());
    StdDraw.setPenRadius(0.005);
    assert node.point.x() >= rect.xmin() &&  node.point.x() <= rect.xmax();
    assert node.point.y() >= rect.ymin() &&  node.point.y() <= rect.ymax();
    if (useX) {
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.line(node.point.x(), rect.ymin(), node.point.x(), rect.ymax());
      drawLine(node.left, new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax()), false);
      drawLine(node.right, new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax()), false);
    } else {
      StdDraw.setPenColor(StdDraw.BLUE);
      StdDraw.line(rect.xmin(), node.point.y(), rect.xmax(), node.point.y());
      drawLine(node.left, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y()), true);
      drawLine(node.right, new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax()), true);
    }
  }

  public void draw() {
    drawLine(mRoot, new RectHV(0, 0, 1, 1), true);
  }

  private void collectPoints(Node node, RectHV area, boolean useX, RectHV rect, ArrayList<Point2D> points) {
    if (node == null)
      return;
    if (area.contains(node.point))
      points.add(node.point);
    RectHV left;
    RectHV right;
    boolean useXForNext = false;
    if (useX) {
      left = new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax());
      right = new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
    } else {
      left = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y());
      right = new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax());
      useXForNext = true;
    }
    if (area.intersects(left))
      collectPoints(node.left, area, useXForNext, left, points);
    if (area.intersects(right))
      collectPoints(node.right, area, useXForNext, right, points);
  }

  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null)
      throw new java.lang.NullPointerException("null");
    ArrayList<Point2D> array = new ArrayList<>();
    collectPoints(mRoot, rect, true, new RectHV(0, 0, 1, 1), array);
    return array;
  }

  private Point2D searchNearestNode(Node node, Point2D target, boolean useX, RectHV rect) {
    if (node == null)
      return null;
    double distance = target.distanceTo(node.point);
    RectHV left;
    RectHV right;
    boolean useXForNext = false;
    Point2D nearestPoint = node.point;

    if (useX) {
      left = new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax());
      right = new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
    } else {
      left = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y());
      right = new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax());
      useXForNext = true;
    }

    Node first;
    RectHV firstRect;
    Node next;
    RectHV nextRect;

    if (left.distanceTo(target) < right.distanceTo(target)) {
      first = node.left;
      firstRect = left;
      next = node.right;
      nextRect = right;
    } else {
      first = node.right;
      firstRect = right;
      next = node.left;
      nextRect = left;
    }

    Point2D firstNearest = searchNearestNode(first, target, useXForNext, firstRect);
    boolean keepSearchNext = true;
    if (firstNearest != null) {
      double minDistanceToFirst = firstNearest.distanceTo(target);
      if (minDistanceToFirst < distance) {
        nearestPoint = firstNearest;
        distance = minDistanceToFirst;
      }
      if (minDistanceToFirst < nextRect.distanceTo(target))
        keepSearchNext = false;
    }
    if (keepSearchNext) {
      Point2D nextNearest = searchNearestNode(next, target, useXForNext, nextRect);
      if (nextNearest != null) {
        double minDistanceToNext = nextNearest.distanceTo(target);
        if (minDistanceToNext < distance) {
          nearestPoint = nextNearest;
        }
      }
    }
    return nearestPoint;
  }

  public Point2D nearest(Point2D p) {
    if (p == null)
      throw new java.lang.NullPointerException("null");
    return searchNearestNode(mRoot, p, true, new RectHV(0, 0, 1, 1));
  }


  public static void main(String[] args) {
    KdTree kdtree = new KdTree();
    kdtree.insert(new Point2D(0.7, 0.2));
    kdtree.insert(new Point2D(0.5, 0.4));
    kdtree.insert(new Point2D(0.2, 0.3));
    kdtree.insert(new Point2D(0.4, 0.7));
    kdtree.insert(new Point2D(0.4, 0.7));
    assert kdtree.contains(new Point2D(0.4, 0.7)) == true;
    assert kdtree.contains(new Point2D(0.4, 0.2)) == false;
    StdOut.println("size=" + kdtree.size());
  }

}
