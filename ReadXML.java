import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ReadXML {
    public static CustomReturnType Read(String filename) {
        // creating the input file
        File inputFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            // parse the xml to read contents
            doc = dBuilder.parse(inputFile);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();

        String name = root.getAttribute("Name");

        // extracting the gesture name
        String gesture = name.substring(0, name.length() - 2);

        NodeList nList = doc.getElementsByTagName("Point");

        ArrayList<Point> points = new ArrayList<>();

        // adding all the points to the points arrayList
        for (int i = 0; i < nList.getLength(); i++) {
            points.add(new Point(Double.parseDouble(nList.item(i).getAttributes().getNamedItem("X").getNodeValue()), Double.parseDouble(nList.item(i).getAttributes().getNamedItem("Y").getNodeValue())));
        }

        return new CustomReturnType(gesture, points);
    }
}