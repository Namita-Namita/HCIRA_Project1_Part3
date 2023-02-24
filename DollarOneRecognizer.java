import java.util.List;
//DollarOneRecognizer class to perform the matching of the templates with the gesture drawn
public class DollarOneRecognizer {
    private final int NumTemplates = 16;
    private final Point[][] templates = new Point[NumTemplates][];
    private final String[] templateNames = new String[NumTemplates];
    DollarOneRecognizer() {
        UniStroke uniStroke = new UniStroke();
        List<Gesture> template = uniStroke.template;
        int i = 0;
        // Getting the templated in a 2d array and template names in a string array
        for(Gesture g:template) {
            List<Point> points = g.points;
            int j = 0;
            templates[i] = new Point[points.size()];
            for(Point p:points)templates[i][j++] = p;
            templateNames[i] = g.name;
            i++;
        }
    }
    //Method to recognize the geture from the saved template
    public String recognize(List<Point> points) {
        PointProcessor pointProcessor = new PointProcessor();
        double b = Double.POSITIVE_INFINITY;
        int t = 0;
        for (int i = 0; i < NumTemplates; i++) {
            // Finding the distance between templates and gesture drawn
            double d = pointProcessor.distanceAtBestAngle(points, templates[i]);
            // comparing the destance and deciding the match
            if (d < b) {
                b = d;
                t = i;
            }
            System.out.println(d+" "+templateNames[i]);
        }
        System.out.println();
        int size = 500;
        // Calculating the matching score to get the decision
        double score = 1 - (b/(0.5*Math.sqrt(size*size + size*size)));
        String templateName = templateNames[t];
        return templateName+"-"+score;
    }
    
}
