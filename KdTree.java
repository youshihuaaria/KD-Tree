
import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    private int size;
    private Node root;
    private final RectHV unit; // the unit rectangle
    
    private Node min; // used in nearest
    private double dist; // uses in nearest, should be initialized before each call

    private class Node {
        Point2D point;
        Node left, right;
        boolean direction; // true if it is vertical. Compare x-axis in this case
        RectHV rec; // the rectangle that point occupy

        Node(Point2D point, boolean direction, RectHV r) {
            this.point = point;
            this.left = null;
            this.right = null;
            this.direction = direction;
            this.rec = r;
        }
    }

    public KdTree() {
        // construct an empty set of points
        size = 0;
        root = null;
        min = root;
        dist = Double.POSITIVE_INFINITY;
        unit = new RectHV(0, 0, 1, 1);
    }

    public boolean isEmpty() {
        // is the set empty?
        return size == 0;
    }

    public int size() {
        // number of points in the set
        return size;
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException();
        }

        root = put(root, p, false, unit);
    }
    
    /**
    * Return the left or right sub rectangle of node n 
    *
    * @param  n  target node
    * @param  isLeft return left or bottom rectangle if true; right or upper rectangle otherwise.
    * @return      the sub rectangle of n
    */
    private RectHV subRect(Node n, boolean isLeft) {
        RectHV rectN = n.rec;
        RectHV sub = null;
        
        if (isLeft) {
            if (n.left != null) {
                return n.left.rec;
            }
            if (n.direction) {
                sub = new RectHV(rectN.xmin(), rectN.ymin(), n.point.x(), rectN.ymax());
            } else {
                sub = new RectHV(rectN.xmin(), rectN.ymin(), rectN.xmax(), n.point.y());
            }
        } else {
            if (n.right != null) {
                return n.right.rec;
            }
            if (n.direction) {
                sub = new RectHV(n.point.x(), rectN.ymin(), rectN.xmax(), rectN.ymax());
            } else {
                sub = new RectHV(rectN.xmin(), n.point.y(), rectN.xmax(), rectN.ymax());
            }
        }
        return sub;
    }

    /**
     * put nodes into the subtree of query node 
     *
     * @param  n  query node
     * @param  p  point to be added
     * @param  isVertical  true if n use x-coordinate
     * @param  r  the rectangle that n occupies
     * @return      a node contains p
     */
    private Node put(Node n, Point2D p, boolean isVertical, RectHV r) {
        if (n == null) {
            n = new Node(p, !isVertical, r);
            size++;
            return n;
        }
        
        if (n.point.equals(p)) {
            return n;
        }

        double cmp = 0;
        if (n.direction) {
            cmp = p.x() - n.point.x();
        } else {
            cmp = p.y() - n.point.y();
        }

        if (cmp >= 0) {
            n.right = put(n.right, p, !isVertical, subRect(n, false));
        } 
        
        if (cmp < 0) {
            n.left = put(n.left, p, !isVertical, subRect(n, true));
        } 

        return n;
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException();
        }

        Node n = root;
        return containsp(n, p);
    }
    
    private boolean containsp(Node n, Point2D p) {
        if (n == null) {
            return false;
        }
        
        if (n.point.equals(p)) {
            return true;
        }
        
        double cmp = 0;
        if (n.direction) {
            cmp = p.x() - n.point.x();
        } else {
            cmp = p.y() - n.point.y();
        }
        
        if (cmp >= 0) {
            return containsp(n.right, p);
        } else {
            return containsp(n.left, p);
        }
    }

    public void draw() {
        // draw all points to standard draw

    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        
        ArrayList<Point2D> lst = new ArrayList<Point2D>();

        Node r = root;
        rangeAux(r, rect, lst);
        return lst;
    }

    private void rangeAux(Node n, RectHV r, ArrayList<Point2D> lst) {
        if (n == null) {
            return;
        }
        
        if (r.contains(n.point)) {
            lst.add(n.point);
        }
        
        if (n.left != null && r.intersects(n.left.rec)) {
            rangeAux(n.left, r, lst);
        }
        
        if (n.right != null && r.intersects(n.right.rec)) {
            rangeAux(n.right, r, lst);
        }
    }


    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        this.min = null;
        this.dist = Double.POSITIVE_INFINITY;
        
        if (p == null) {
            throw new IllegalArgumentException();
        }
        
        if (root == null) {
            return null;
        }
        
        Node x = root;
        nn(x, p);

        return min.point;
    }
        
    private void nn(Node curr, Point2D p) {
        if (curr == null)
            return;
        
        double pointRecD = curr.rec.distanceSquaredTo(p);
        
        if (dist < pointRecD) {
            return;
        } 
            
        double currPD = curr.point.distanceSquaredTo(p);
        if (this.dist > currPD) {
            this.dist = currPD;
            this.min = curr;
        }
        
        if (curr.direction) {
            if (p.x() >= curr.point.x()) {
                nn(curr.right, p);
                nn(curr.left, p);
            } else {
                nn(curr.left, p);
                nn(curr.right, p);
            }
        } else {
            if (p.y() >= curr.point.y()) {
                nn(curr.right, p);
                nn(curr.left, p);
            } else {
                nn(curr.left, p);
                nn(curr.right, p); 
            }
        }
    }

    
}
