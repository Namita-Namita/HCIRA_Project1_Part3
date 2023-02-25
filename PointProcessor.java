import java.util.ArrayList;
import java.util.List;

public class PointProcessor {
    private final int NumPoints = 64;

    // Method to resample the points fetched (templates and gestures)
    public ArrayList<Point> resample(ArrayList<Point> points) {
        double interval = pathLength(points) / (double) (NumPoints - 1);
        double D = 0.0;
        ArrayList<Point> newPoints = new ArrayList<>();
        newPoints.add(points.get(0));
        for (int i = 1; i < points.size(); i++) {
            Point currentPoint = points.get(i);
            Point previousPoint = points.get(i - 1);
            double d = currentPoint.distance(previousPoint);
            if (D + d >= interval) {
                double qx = previousPoint.x + ((interval - D) / d) * (currentPoint.x - previousPoint.x);
                double qy = previousPoint.y + ((interval - D) / d) * (currentPoint.y - previousPoint.y);
                Point point = new Point(qx, qy);
                newPoints.add(point);
                points.add(i, point);
                D = 0.0;
            } else {
                D += d;
            }
        }
        if (newPoints.size() == NumPoints - 1) {
            newPoints.add(points.get(points.size() - 1));
        }
        return newPoints;
    }

    // computes the 'distance' between two point paths by summing their
    // corresponding point distances.
    // assumes that each path has been resampled to the same number of points at the
    // same distance apart.
    public double pathLength(List<Point> points) {
        double d = 0;
        for (int i = 1; i < points.size(); i++) {
            d += points.get(i - 1).distance(points.get(i));
        }
        return d;
    }

    // Method to scale the points fetched (templates and gestures)
    public void scale(List<Point> points) {
        double[] dim = boundingBoxSize(points);
        double size = 500.0;
        for (Point point : points) {
            point.x = point.x * size / dim[0];
            point.y = point.y * size / dim[1];
        }
    }

    // Method to translate the points fetched (templates and gestures)
    public ArrayList<Point> translate(List<Point> points) {
        ArrayList<Point> translatedPoints = new ArrayList<>();
        Point centroid = centroid(points);
        for (Point p : points) {
            translatedPoints.add(new Point(p.x - centroid.x, p.y - centroid.y));
        }
        return translatedPoints;
    }

    // Method to get controid of the figure (templates and gestures)
    public Point centroid(List<Point> points) {
        int x = 0;
        int y = 0;
        for (Point point : points) {
            x += point.x;
            y += point.y;
        }
        return new Point(x / points.size(), y / points.size());
    }

    // boundingBoxsize method to generate a square to fit the gesture and templates
    private double[] boundingBoxSize(List<Point> points) {
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        for (Point point : points) {

            minX = Math.min(minX, point.x);
            minY = Math.min(minY, point.y);
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);

        }
        double width = maxX - minX;
        double height = maxY - minY;

        return new double[] { width, height };
    }

    // Method to rotate the points fetched by certain angle(templates and gestures)
    public static ArrayList<Point> rotateBy(List<Point> points, double angle, Point centroid) {
        ArrayList<Point> newPoints = new ArrayList<>();

        for (Point point : points) {
            int x = (int) ((point.x - centroid.x) * Math.cos(angle) - (point.y - centroid.y) * Math.sin(angle))
                    + (int) centroid.x;
            int y = (int) ((point.x - centroid.x) * Math.sin(angle) + (point.y - centroid.y) * Math.cos(angle))
                    + (int) centroid.y;
            newPoints.add(new Point(x, y));
        }
        return newPoints;
    }

    // Golden Section Search (GSS),
    // an efficient algorithm that finds the minimum value in a range using the
    // Golden Ratio φ=0.5(-1 + √5)
    public static double distanceAtBestAngle(List<Point> points, ArrayList<Point> T) {
        double a = -0.25 * Math.PI;
        double b = 0.25 * Math.PI;
        double delta = 0.5 * (-1 + Math.sqrt(5));
        double threshold = Math.toRadians(2);

        double x1 = delta * a + (1 - delta) * b;
        double x2 = (1 - delta) * a + delta * b;

        double f1 = distanceAtAngle(x1, points, T);
        double f2 = distanceAtAngle(x2, points, T);
        while (Math.abs(b - a) > threshold) {
            if (f1 < f2) {
                b = x2;
                x2 = x1;
                f2 = f1;
                x1 = delta * a + (1 - delta) * b;
                f1 = distanceAtAngle(x1, points, T);
            } else {
                a = x1;
                x1 = x2;
                f1 = f2;
                x2 = (1 - delta) * a + delta * b;
                f2 = distanceAtAngle(x2, points, T);
            }
        }
        return Math.min(f1, f2);
    }

    // method supporting GSS strategy
    private static double distanceAtAngle(double angle, List<Point> points, ArrayList<Point> T) {
        List<Point> newPoints = rotateBy(points, angle, new PointProcessor().centroid(points));
        return pathDistance(newPoints, T);
    }

    // computes the distance between two point from a and T paths by summing their
    // corresponding point distances.
    // private double pathDistance(List<Point> a) {
    // double d = 0;
    // for (int i = 0; i < Math.min(b.length,a.size()); i++) {
    // d += a.get(i)distance(a.get(i-1));
    // }
    // return d / Math.min(b.length,a.size());
    // }
    private static double pathDistance(List<Point> a, ArrayList<Point> b) {
        double d = 0;
        for (int i = 0; i < Math.min(b.size(), a.size()); i++) {
            d += distance(a.get(i), b.get(i));
        }
        return d / Math.min(b.size(), a.size());
    }

    // distance method to calculate distance between two points x and y
    private static double distance(Point a, Point b) {
        double dx = (a.x - b.x);
        double dy = (a.y - b.y);
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static ArrayList<CustomReturnType> NBestListSort(ArrayList<CustomReturnType> nbestList) {

        //creating an arrayList to store best gesture
        ArrayList<CustomReturnType> arrList = new ArrayList<>();

        while (!nbestList.isEmpty()) {
            int bestGesture = 0;
            double bestScore = Double.NEGATIVE_INFINITY;

            // iterating through the list for best score
            for (int i = 0; i < nbestList.size(); i++) {
                if (nbestList.get(i).score > bestScore) {
                    bestScore = nbestList.get(i).score;
                    bestGesture = i;
                }
            }

            // append the gesrure to the arrayList and remove from nbestList
            arrList.add(nbestList.get(bestGesture));

            nbestList.remove(bestGesture);
        }

        return arrList;
    }
}
