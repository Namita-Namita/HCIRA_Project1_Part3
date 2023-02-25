import java.util.ArrayList;

public class Helper {

    public static ArrayList<CustomReturnType> MakeGesture(String user, String gestureType) {
        ArrayList<CustomReturnType> templates = new ArrayList<>();

        String filename = "";
        String gestureNumber = "";


        // iterate though the gesture types
        for (int i = 1; i <= 10; i++) {
            if (i != 10) {
                gestureNumber = "0" + i;
            } else {
                gestureNumber = Integer.toString(i);
            }

            // creating log file
            filename = "xml_logs/" + user + "/fast/" + gestureType + gestureNumber + ".xml";

            CustomReturnType temp = normalize (ReadXML.Read(filename));

            //storing the user gestures to the templates list
            templates.add(new CustomReturnType(temp.gesture, temp.points, Integer.parseInt(gestureNumber)));
        }
        return templates;
    }

    public static CustomReturnType normalize (CustomReturnType template) {
        PointProcessor pointProcessor = new PointProcessor();
        ArrayList<Point> resampledPoints = pointProcessor.resample(template.points);

        // Rotate the points accordingly
        // Get the centroid of the gesture drawn
        Point centroid = pointProcessor.centroid(resampledPoints);
        // Get the first poiint after resampling
        Point firstPoint = resampledPoints.get(0);
        // Calculate the slope to get the rotation angle
        double slope = Math.atan2((firstPoint.y - centroid.y), (firstPoint.x - centroid.x));
        // Rotate by function to rotate the points accordingl
        // ArrayList<Point> rotatedPoints = pointProcessor.rotateBy(resampledPoints,
        // -1*slope,centroid);
        ArrayList<Point> rotatedPoints = PointProcessor.rotateBy(resampledPoints, -1 * slope, centroid);

        // Scale the gesture
        pointProcessor.scale(rotatedPoints);

        // Translate the gesture
        ArrayList<Point> translatedPoints = pointProcessor.translate(rotatedPoints);

        // stores the normalized points and the gesture name in an object
        CustomReturnType normalizedPoints = new CustomReturnType(template.gesture, translatedPoints);

        return normalizedPoints;
    }
}