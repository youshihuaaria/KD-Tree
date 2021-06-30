
import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private final SET<Point2D> s;
    
    public PointSET() {
        // construct an empty set of points 
        s = new SET<Point2D>();
    }
    
    public boolean isEmpty() {
        // is the set empty? 
        return s.isEmpty();
    }
    
    public int size() {
        // number of points in the set 
        return s.size();
    }
    
    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException();
        }
        
        s.add(p);
    }
        
    public boolean contains(Point2D p) {
        // does the set contain point p? 
        if (p == null) {
            throw new IllegalArgumentException();
        }
        
        return s.contains(p);
    }
    
    public void draw() {
        // draw all points to standard draw 
        for (Point2D p : s) {
            p.draw();
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary) 
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        
        ArrayList<Point2D> lst = new ArrayList<Point2D>();
        
        for (Point2D p : s) {
            if (rect.contains(p)) {
                lst.add(p);
            }
        }
        
        return lst;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null) {
            throw new IllegalArgumentException();
        }
        
        double dist = Double.POSITIVE_INFINITY;
        Point2D min = null;
        
        for (Point2D a : s) {
            if (a.distanceSquaredTo(p) < dist) {
                dist = a.distanceSquaredTo(p);
                min = a;
            }
        }
        
        return min;
    }
    

}
