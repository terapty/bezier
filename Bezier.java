import java.util.ArrayList;
import java.util.List;

public class Bezier {
    
    private double curveSmoothness;
    
    public final static double DEFAULT_SMOOTHNESS = 0.1f;
    
    public static class Coordinate {
        private double x;
        private double y;
        
        public Coordinate() {
            x = 0;
            y = 0;
        }
        
        public Coordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        public double getX() {
            return x;
        }
        
        public double getY() {
            return y;
        }
    }
    
    public Bezier(double smoothness) {
        if (smoothness <= 0.0 || smoothness >= 1.0) {
            throw new AssertionError("Smoothness must be between 0 and 1 (both non-inclusive)");
        }
        curveSmoothness = smoothness;
    }
    
    public Bezier() {
        this(DEFAULT_SMOOTHNESS);
    }
    
    private Coordinate getQuadraticTPoint(Coordinate src, Coordinate ctrl1,
                                            Coordinate dst, double perc) {
        //formule : B(t) = P0(1-t)² + P1t2(1-t) + P2t²
        //P1,P2,P3 les points de contrôle, t le pourcentage ([0-1]) 
        double newPointX = (1 - perc) * (1 - perc) * src.getX() +
                2 * perc * (1 - perc) * ctrl1.getX()
                + perc * perc * dst.getX();
        double newPointY = (1 - perc) * (1 - perc) * src.getY() +
                2 * perc * (1 - perc) * ctrl1.getY() + perc * perc * dst.getY();
        return new Coordinate(newPointX, newPointY);
    }
    
    private Coordinate getCubicTPoint(Coordinate src, Coordinate ctrl1,
                                Coordinate ctrl2, Coordinate dst, double perc) {
        //formule : B(t) = P0(1-t)(cube) + P1*3t(1-t)² + P2*3t²(1-t) + P3*t(cube)
        //P1,P2,P3,P4 les points de contrôle, t le pourcentage ([0-1]) 
        double inverse = (1 - perc);
        double inverseSquared = inverse * inverse;
        double inverseCubed = inverseSquared * inverse;
        
        double newPointX = src.getX() * inverseCubed
                + ctrl1.getX() * 3 * perc * inverseSquared
                + ctrl2.getX() * 3 * perc * perc * inverse
                + dst.getX() * perc * perc * perc;
        double newPointY = src.getY() * inverseCubed
                + ctrl1.getY() * 3 * perc * inverseSquared
                + ctrl2.getY() * 3 * perc * perc * inverse
                + dst.getY() * perc * perc * perc;
        return new Coordinate(newPointX, newPointY);
    }
    
    public List<Coordinate> calculateCurve(List<Coordinate> l) {
        if (l == null) {
            throw new AssertionError("Provided list had no reference");
        }
        if (l.size() < 3) {
            return null;
        }
        Coordinate p1 = l.get(0);
        Coordinate p2 = l.get(1);
        Coordinate p3 = l.get(2);
        Coordinate p4 = l.size() == 4 ? l.get(3) : null;
        l = new ArrayList<Coordinate>();
        Coordinate currentPoint = p1;
        for (double i = 0; i < 1; i += curveSmoothness) {
            l.add(currentPoint);
            currentPoint = p4 == null ?
                    getQuadraticTPoint(p1, p2, p3, i) :
                    getCubicTPoint(p1, p2, p3, p4, i);
        }
        return l;
    }
}
