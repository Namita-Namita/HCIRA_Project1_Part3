import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

// a UiFrame class to instantiate a blank ‘canvas’ to the screen using GUI elements
//listen for mouse or touch events on the canvas and draw them as the user makes them
// allow the user to clear the canvas. 
class UiFrame extends JFrame implements MouseListener, MouseMotionListener, ActionListener{

    JButton button;
    Canvas canvas;
    int prevx, prevy;
    // ArrayList DS to store array of points.
    ArrayList<Point> points = new ArrayList<>();
    int i=0;
    
	// constructor to set up the frame with canvas and clear button
	UiFrame()
	{
		super("canvas");

		// create a empty canvas
		canvas = new Canvas(){
            public void paint(Graphics gr)
            {  
            }
        };
        // sets the frame to put canvas as a component
        setLayout(null);
        // sets size of the frame 500*500 square
		setSize(500, 500);
        // set background for the canvas with color gray
		canvas.setBackground(Color.gray);
        // add mouse listener and mouse motion listener for mouse actions
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        // set size of the canvas in the frame- 500*400
        canvas.setSize(500, 400);
		add(canvas);
        // Create a clear button labeled Clear
	    button = new JButton ("Clear");
        // Sets the background of the button to white.
	    button.setBackground (Color.white);
        // button setbounds function to set the sezi and placement of the button
        button.setBounds(180 , 400, 100, 50);
        add(button);
        // Add a listener for the button to listen to the button clicks
        button.addActionListener (this);
        setVisible(true);
	}
    // This method is required to implement the  
    // ActionListener interface. 
    public void actionPerformed (ActionEvent e)
    {
	    String str = e.getActionCommand();
        points.clear();
        if (str.equalsIgnoreCase ("Clear")) {
	    // Note: must call repaint() of canvas 
	    // to reset the background. 
	    canvas.setBackground (Color.gray);
	    canvas.repaint ();
	    }
    }
    // mouse listener  and mouse motion listener methods
    // mousepressed method to listen to the pressed action of the mouse.
    public void mousePressed(MouseEvent e)
    {
        Graphics gr = canvas.getGraphics();
 
        gr.setColor(Color.red);
 
        // get X and Y position in prevx and prevy variables.
        prevx = e.getX();
        prevy = e.getY();
        // Adds point prevx and prevy fetched to the list of Array points.
        points.add(new Point((double)prevx,(double)prevy));
        // draw a Oval at the point
        // prevx and prevy
        gr.fillOval(prevx, prevy, 10, 10);
    }
    // mousedragged method to listen to the dragged action of the mouse.
    public void mouseDragged(MouseEvent e)
    {
        Graphics gr = canvas.getGraphics();
        Graphics2D g2= (Graphics2D)gr;
        // stes the color of the gesture line drawn to red.
        gr.setColor(Color.red);
        // Sets the width of the line to draw.
        g2.setStroke(new BasicStroke(5));
        // get X and Y position
        int x, y;
        x = e.getX();
        y = e.getY();
        // Adds point X and Y fetched to the list of Array points.
        points.add(new Point((double)x,(double)y));

        // draw a line with the points where mouse is moved
        gr.drawLine(prevx, prevy, x, y);
        prevx=x;
        prevy=y;
        // System.out.println(xcord[i]+" "+ycord[i]+"\n");
    }
    public void mouseClicked(MouseEvent e)
    {
    }
 
    public void mouseMoved(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }
 
    public void mouseEntered(MouseEvent e)
    {
    }
    // mousereleased method to listen to the released action of the mouse.
    public void mouseReleased(MouseEvent e)
    {
        /**
         * start $1 algorithm to recognize the gesture and output the result
         * Steps involved for the reconition to happen
         * 1. Resample
         * 2. Rotate
         * 3. Scale
         * 5. Translate
         * 6. Matching
         * 7. O/P
         */
        long startTime = System.currentTimeMillis();
        PointProcessor pointProcessor = new PointProcessor();
        DollarOneRecognizer dollarOneRecognizer = new DollarOneRecognizer();

        // // Resampling the points
        ArrayList<Point> resampledPoints = pointProcessor.resample(points);

        // //Rotate the points accordingly
        // // Get the centroid of the gesture drawn
        Point centroid = pointProcessor.centroid(resampledPoints);
        // // Get the first poiint after resampling
        Point firstPoint = resampledPoints.get(0);
        // // Calculate the slope to get the rotation angle
        double slope = Math.atan2((firstPoint.y-centroid.y),(firstPoint.x-centroid.x));
        // // Rotate by function to rotate the points accordingl
        // //ArrayList<Point> rotatedPoints = pointProcessor.rotateBy(resampledPoints, -1*slope,centroid);
        ArrayList<Point> rotatedPoints = pointProcessor.rotateBy(resampledPoints, -1*slope,centroid);
        
        // //Scale the gesture
        pointProcessor.scale(rotatedPoints);

        // //Translate the gesture
        ArrayList<Point> translatedPoints = pointProcessor.translate(rotatedPoints);
        
        // //Matching
        String result = dollarOneRecognizer.recognize(translatedPoints);
        // // Displaying the match
        // //String[] str = result.split(" ");
        String[] str = result.split("-");
        System.out.println(result);
        // // Getting the match
        String templateName = str[0];
        // // Getting the score
        double score = Double.parseDouble(str[1]);
        long endTime = System.currentTimeMillis();
        // // Set the title to the match found and the score calculated.
        this.setTitle("Result: "+templateName+" ("+score+") in "+(endTime-startTime)+"ms");


        // //Draw the points
        Graphics gr = canvas.getGraphics();
        gr.setColor(Color.blue);
    }
 
    public static void main (String[] argv)
    {
        // creates a canvas object
        new UiFrame();
            
    }
}
