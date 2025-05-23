import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Main class to run all of the backend commands
 */
public class Backend implements BackendInterface {

  // create a private graph variable
  private GraphADT<String, Double> graph;

  /*
   * Implementing classes should support the constructor below.
   * 
   * @param graph object to store the backend's graph data
   */
  public Backend(GraphADT<String, Double> graph) {
    this.graph = graph;
  }

  /**
   * Loads graph data from a dot file. If a graph was previously loaded, this method should first
   * delete the contents (nodes and edges) of the existing graph before loading a new one.
   * 
   * @param filename the path to a dot file to read graph data from
   * @throws IOException if there was any problem reading from this file
   */
  public void loadGraphData(String filename) throws IOException {

    // remove all data from the graph
    for (String node : graph.getAllNodes()) {
      graph.removeNode(node);
    }
    // try loop incase the file cannot be found
    try {
      // create a new file and put it into a scanner variable
      File file = new File(filename);
      @SuppressWarnings("resource")
      Scanner in = new Scanner(file);
      String line;

      // while the scanner has another line run the loop
      while (in.hasNextLine()) {

        // check if the next line is proper dot expression then divide up the strings to get the
        // values that we want and make them proper variables
        line = in.nextLine();
        if (line.contains("->")) {
          String[] parts = line.split("->");
          String node1 = parts[0].replaceAll("\"", "").trim();
          String part2 = parts[1];
            String[] node2AndTime = part2.split("seconds=");
            String node2 = node2AndTime[0].replaceAll("\"", "").replace("[", "").trim();
            double time = Double.parseDouble(node2AndTime[1].replaceAll("];", "").trim());
            
            // Insert the nodes then the edge
            if (!graph.containsNode(node1))
              graph.insertNode(node1);
            if (!graph.containsNode(node2))
              graph.insertNode(node2);
            graph.insertEdge(node1, node2, time);
          }
        }
      // catch an exception if the file cannot be found
    } catch (FileNotFoundException e) {
      throw new IOException("dot file could not be found");
    }
  }

  /**
   * Returns a list of all locations (node data) available in the graph.
   * 
   * @return list of all location names
   */
  public List<String> getListOfAllLocations() {
    return graph.getAllNodes();
  }

  /**
   * Return the sequence of locations along the shortest path from startLocation to endLocation, or
   * an empty list if no such path exists.
   * 
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list with the nodes along the shortest path from startLocation to endLocation, or an
   *         empty list if no such path exists
   */
  public List<String> findLocationsOnShortestPath(String startLocation, String endLocation) {

    // try to find the shortest path and return it
    try {
      return graph.shortestPathData(startLocation, endLocation);
      // if path is empty return null
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  /**
   * Return the walking times in seconds between each two nodes on the shortest path from
   * startLocation to endLocation, or an empty list of no such path exists.
   * 
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list with the walking times in seconds between two nodes along the shortest path from
   *         startLocation to endLocation, or an empty list if no such path exists
   */
  @SuppressWarnings("null")
  public List<Double> findTimesOnShortestPath(String startLocation, String endLocation) {

    // try to find the shortest path and return it
    try {
      List<String> path = findLocationsOnShortestPath(startLocation, endLocation);
      ArrayList<Double> costs = new ArrayList<Double>();
      for (int i = 1; i < path.size(); i++) {
        costs.add(graph.shortestPathCost(path.get(i - 1), path.get(i)));
      }
      return costs;
      // if path is empty return null
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  /**
   * Returns the most distant location (the one that takes the longest time to reach) when comparing
   * all shortest paths that begin from the provided startLocation.
   * 
   * @param startLocation the location to find the most distant location from
   * @return the most distant location (the one that takes the longest time to reach which following
   *         the shortest path)
   * @throws NoSuchElementException if startLocation does not exist, or if there are no other
   *                                locations that can be reached from there
   */
  public String getFurthestDestinationFrom(String startLocation) throws NoSuchElementException {

    // if the graph does not contain the node then throw a NoSuchElementException
    if (!graph.containsNode(startLocation))
      throw new NoSuchElementException("Start location not found");

    // create maxDistance and furthest location values
    double maxDistance = -1;
    String furthestLocation = null;

    // run a loop for every node get the location
    for (String location : graph.getAllNodes()) {
      if (!location.equals(startLocation)) {
        try {
          // find the shortest path cost to each node then keep the highest cost
          double distance = graph.shortestPathCost(startLocation, location);
          if (distance > maxDistance) {
            maxDistance = distance;
            furthestLocation = location;
          }

        } catch (NoSuchElementException e) {
          // Ignore unreachable nodes
        }
      }
    }
    // there is no reachable destination so throw exception
    if (furthestLocation == null)
      throw new NoSuchElementException("No reachable destinations found");

    return furthestLocation;
  }
}
