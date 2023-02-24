import java.util.ArrayList;

// allows you to return multiple types of values
// needed to keep track of each individual gesture, its corresponding points, its score in relation to the gesture that is being tested
public class CustomReturnType {

    public final String gesture;
    public final double score;
    public final ArrayList<Point> points;
    public final int gestureNumber;

    //creating constructors with different args

    public CustomReturnType(String gesture, double score) {
        this.gesture = gesture;
        this.score = score;
        this.points = null;
        this.gestureNumber = 0;
    }

    public CustomReturnType(String gesture, ArrayList<Point> points) {
        this.gesture = gesture;
        this.score = 0;
        this.points = points;
        this.gestureNumber = 0;
    }

    public CustomReturnType(String gesture, double score, int gestureNumber) {
        this.gesture = gesture;
        this.score = score;
        this.points = null;
        this.gestureNumber = gestureNumber;
    }

    public CustomReturnType(String gesture, ArrayList<Point> points, int gestureNumber) {
        this.gesture = gesture;
        this.score = 0;
        this.points = points;
        this.gestureNumber = gestureNumber;
    }
}