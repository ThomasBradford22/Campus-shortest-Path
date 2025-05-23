import java.util.List;

public class Frontend implements FrontendInterface {
    private BackendInterface backend;
    public Frontend(BackendInterface backend) {
        this.backend = backend;
    }

    /***
     * Generates the html page for starting and end location
     * @return
     */
    @Override
    public String generateShortestPathPromptHTML() {
        return "<div>" +
                "<label for=\"start\"> Start Location:</label>" +
                "<input type=\"text\" id='start' name='start'>" +
                "<label for= \"end\" >End Location:</label>" +
                "<input type= \"text\" id='end' name='end'>" +
                "<button \"findShortestPath()\">Find Shortest Path</button>" +
                "</div>";
    }

    /***
     * Generates a response for html
     * @param start is the starting location to find a shortest path from
     * @param end is the destination that this shortest path should end at
     * @return html string with information inside of it
     */
    @Override
    public String generateShortestPathResponseHTML(String start, String end) {
        List<String> path = backend.findLocationsOnShortestPath(start, end);
        if (path == null || path.isEmpty()) {
            return "<p>No path found from " + start + " to " + end + ".</p>";
        }

        String html = "<p>Shortest path from " + start + " to " + end + ":</p><ol>";
        for (int i = 0; i < path.size(); i++) {
            html += "<li>" + path.get(i) + "</li>";
        }
        html += "</ol>";

        List<Double> times = backend.findTimesOnShortestPath(start, end);
        double totalTime = 0.0;
        for (int i = 0; i < times.size(); i++) {
            totalTime += times.get(i);
        }
        html += "<p>Total travel time: " + totalTime + " seconds</p>";

        return html;
    }


    /***
     * Generates the furthest destination prompt for a location
     * @return
     */
    @Override
    public String generateFurthestDestinationFromPromptHTML() {
        return "<div>" +
                "<label for=\"from\">Start Location:</label>" +
                "<input type=\"text\" id='from' name='from'>" +
                "<button  \"findFurthestDestination()\">Furthest Destination From</button>" +
                "</div>";
    }

    /***
     *
     * @param start is the starting location to find the furthest dest from
     * @return  html string with information inside of it
     */
@Override
public String generateFurthestDestinationFromResponseHTML(String start) {
    // Check if start is null or empty
    if (start == null || start.trim().isEmpty()) {
        return "<p>Please enter a start location.</p>";
    }

    try {
        String furthest = backend.getFurthestDestinationFrom(start);
        if (furthest == null) {
            return "<p>No destinations found from " + start + ".</p>";
        }

        // Start building the HTML output as a string
        String html = "<p>Furthest destination from " + start + " is " + furthest + ":</p><ol>";

        // Adds nodes on the path from start to furthest destination
        List<String> path = backend.findLocationsOnShortestPath(start, furthest);
        if (path == null || path.isEmpty()) {
            return html + "</ol><p>Unable to find the path.</p>";
        }

        for (int i = 0; i < path.size(); i++) {
            html += "<li>" + path.get(i) + "</li>";
        }
        html += "</ol>";

        return html;
    } catch (Exception e) {
        // Handle any exceptions that may occur
        return "<p>An error occurred: " + e.getMessage() + "</p>";
    }
}
}
