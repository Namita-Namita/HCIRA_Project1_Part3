import java.util.ArrayList;
import java.util.List;

//method for the templates drawn to match the gestures
public class DollarOneRecognizer {
    private final int NumTemplates = 16;
    private final Point[][] templates = new Point[NumTemplates][];
    private final String[] templateNames = new String[NumTemplates];
    DollarOneRecognizer() {
        UniStroke uniStroke = new UniStroke();
        List<Gesture> template = uniStroke.template;
        int i = 0;
        // iterate through the gesture to create template
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
    // public String recognize(List<Point> points) {
    //     PointProcessor pointProcessor = new PointProcessor();
    //     double b = Double.POSITIVE_INFINITY;
    //     int t = 0;
    //     for (int i = 0; i < NumTemplates; i++) {
    //         // Finding the distance between templates and gesture drawn
    //         double d = pointProcessor.distanceAtBestAngle(points, templates[i]);
    //         // comparing the destance and deciding the match
    //         if (d < b) {
    //             b = d;
    //             t = i;
    //         }
    //         System.out.println(d+" "+templateNames[i]);
    //     }
    //     System.out.println();
    //     int size = 500;
    //     // Calculating the matching score to get the decision
    //     double score = 1 - (b/(0.5*Math.sqrt(size*size + size*size)));
    //     String templateName = templateNames[t];
    //     return templateName+"-"+score;
    // }

    public ArrayList<CustomReturnType> Recognize(CustomReturnType points, ArrayList<ArrayList<CustomReturnType>> templates) {

        ArrayList<CustomReturnType> nbestList = new ArrayList<>();

        // iterate through the templates
        for (int i = 0; i < templates.size(); i++) {
            double b = Double.POSITIVE_INFINITY;
            //int gestureNumber = 0;

            //iterate through each template
            for (int j = 0; j < templates.get(i).size(); j++) {
                double d = PointProcessor.distanceAtBestAngle(points.points, templates.get(i).get(j).points);
                // gestureNumber=j+1;
                if (d < b) {
                    b = d;
                    
                }
               
                double score = 1 - b / (.5 * Math.sqrt(250 * 250 + 250 * 250));
               // System.out.println(templates.get(i).get(j).gesture + "-"+score +"-"+ templates.get(i).get(j).gestureNumber);

                // adding to the nbestList gesture type, score and gesture number
                nbestList.add(new CustomReturnType(templates.get(i).get(j).gesture, Double.parseDouble(String.format("%.5f", score)), templates.get(i).get(j).gestureNumber));
            }

            // double score = 1 - b / (.5 * Math.sqrt(250 * 250 + 250 * 250));

            // // adding to the nbestList gesture type, score and gesture number
            // nbestList.add(new CustomReturnType(templates.get(i).get(0).gesture, score, gestureNumber));
        }

        // sorting before returining the list
        return PointProcessor.NBestListSort(nbestList);
    }    
}
