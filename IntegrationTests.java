import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Integration tests for the Backend and Frontend classes.
 */
public class IntegrationTests {

  /**
   * Integration test for `generateShortestPathResponseHTML` to ensure the shortest path is
   * displayed correctly.
   */
  @Test
  public void IntegrationTest1() {
    // create a test graph and use it create a backend and frontend
    GraphADT<String, Double> testGraph = new DijkstraGraph<String, Double>();
    Backend backend = new Backend(testGraph);
    Frontend frontend = new Frontend(backend);

    // insert the nodes and edges for the test graph
    testGraph.insertNode("A");
    testGraph.insertNode("B");
    testGraph.insertNode("C");
    testGraph.insertEdge("A", "B", 10.0);
    testGraph.insertEdge("B", "C", 15.0);

    // create a string to receive the shortestPath generation
    String htmlResponse = frontend.generateShortestPathResponseHTML("A", "C");

    // test that his string contains the expected values 
    assertTrue(htmlResponse.contains("Shortest path from A to C"),
        "HTML response should contain the shortest path description.");
    assertTrue(htmlResponse.contains("<li>A</li>") && htmlResponse.contains("<li>B</li>")
        && htmlResponse.contains("<li>C</li>"), "HTML should list path nodes.");
    assertTrue(htmlResponse.contains("Total travel time: 25.0 seconds"),
        "HTML should include the correct total travel time.");
  }

  /**
   * Integration test for `generateFurthestDestinationFromResponseHTML` to ensure the furthest
   * destination is displayed correctly.
   */
  @Test
  public void IntegrationTest2() {
    // create a test graph and use it create a backend and frontend
    GraphADT<String, Double> testGraph = new DijkstraGraph<String, Double>();
    Backend backend = new Backend(testGraph);
    Frontend frontend = new Frontend(backend);

    // insert the nodes and edges for the test graph
    testGraph.insertNode("X");
    testGraph.insertNode("Y");
    testGraph.insertNode("Z");
    testGraph.insertEdge("X", "Y", 20.0);
    testGraph.insertEdge("X", "Z", 30.0);

    // create a string to receive the furthestDestination generation
    String htmlResponse = frontend.generateFurthestDestinationFromResponseHTML("X");
    
    // test that his string contains the expected values 
    assertTrue(htmlResponse.contains("Furthest destination from X is Z"),
        "HTML response should correctly identify the furthest destination.");
    assertTrue(htmlResponse.contains("<li>X</li>") && htmlResponse.contains("<li>Z</li>"),
        "HTML should include the path to the furthest destination.");
  }

  /**
   * Integration test for `loadGraphData` to ensure the graph data is properly loaded and reflected
   * in the list of locations.
   */
  @Test
  public void integrationTest3() {
    // create a test graph and use it create a backend
    GraphADT<String, Double> testGraph = new DijkstraGraph<String, Double>();
    Backend backend = new Backend(testGraph);
    
    // run a try to make sure that any errors from running the loadGraph data can be found
    boolean exception = false;
    try {
      // load the graph data
      backend.loadGraphData("campus.dot");
    } catch (Exception e) {
      // note that an exception was thrown
      exception = true;
    }
    // make sure that no exceptions were thrown
    assertEquals(false, exception, "No campus.dot file was found when it should have been");

    // get the list of all the locations and note some random test nodes
    List<String> locations = backend.getListOfAllLocations();
    List<String> expectedLocations =
        Arrays.asList("Memorial Union", "Science Hall", "Mosse Humanities Building");
    
    // for each expected node check if it is in the output
    for (int i = 0; i < expectedLocations.size(); i++) {
      assertTrue(locations.contains(expectedLocations.get(i)),
          "Locations from the dot file were not found");
    }
    
    // make sure all 160 nodes were found
    assertTrue(locations.size() == 160,
        "All locations from the dot file should be loaded correctly.");
  }

  /**
   * Integration test for `findLocationsOnShortestPath` to ensure correct path is returned when
   * called directly through Backend.
   */
  @Test
  public void IntegrationTest4() {
    // create a test graph and use it create a backend
    GraphADT<String, Double> testGraph = new DijkstraGraph<String, Double>();
    Backend backend = new Backend(testGraph);
    
    // insert a complex graph of nodes and edges into the graph
    testGraph.insertNode("A");
    testGraph.insertNode("B");
    testGraph.insertNode("C");
    testGraph.insertNode("D");
    testGraph.insertNode("E");
    testGraph.insertNode("F");
    testGraph.insertNode("G");
    testGraph.insertEdge("A", "C", 3.0);
    testGraph.insertEdge("A", "B", 4.0);
    testGraph.insertEdge("A", "F", 2.0);
    testGraph.insertEdge("B", "E", 4.0);
    testGraph.insertEdge("C", "F", 4.0);
    testGraph.insertEdge("C", "B", 3.0);
    testGraph.insertEdge("C", "D", 2.0);
    testGraph.insertEdge("D", "B", 2.0);
    testGraph.insertEdge("D", "E", 3.0);
    testGraph.insertEdge("E", "G", 2.0);
    testGraph.insertEdge("F", "D", 2.0);
    testGraph.insertEdge("F", "G", 8.0);

    // Test that the shortest path was accurately found in order
    List<String> path = backend.findLocationsOnShortestPath("A", "E");
    List<String> expectedPath = Arrays.asList("A", "F", "D", "E");
    assertEquals(expectedPath, path,
        "The shortest path should include the correct nodes in order.");
  }
}
