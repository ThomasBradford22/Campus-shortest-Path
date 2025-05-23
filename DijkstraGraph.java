// --== CS400 Fall 2024 File Header Information ==--
// Name: Thomas Bradford
// Email: tsbradford@wisc.edu
// Lecturer: Dahl
// Notes to Grader: I was a little confused with the map and implementing hashmaps because we havent
// learned them yet so I just gave it my best shot.
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * This class extends the BaseGraph data structure with additional methods for computing the total
 * cost and list of node data along the shortest path connecting a provided starting to ending
 * nodes. This class makes use of Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number> extends BaseGraph<NodeType, EdgeType>
    implements GraphADT<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode contains data about one
   * specific path between the start node and another node in the graph. The final node in this path
   * is stored in its node field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referened by the predecessor field (this field is
   * null within the SearchNode containing the starting node in its node field).
   *
   * SearchNodes are Comparable and are sorted by cost so that the lowest cost SearchNode has the
   * highest priority within a java.util.PriorityQueue.
   */
  protected class SearchNode implements Comparable<SearchNode> {
    public Node node;
    public double cost;
    public SearchNode predecessor;

    public SearchNode(Node node, double cost, SearchNode predecessor) {
      this.node = node;
      this.cost = cost;
      this.predecessor = predecessor;
    }

    public int compareTo(SearchNode other) {
      if (cost > other.cost)
        return +1;
      if (cost < other.cost)
        return -1;
      return 0;
    }
  }

  /**
   * Constructor that sets the map that the graph uses.
   */
  public DijkstraGraph() {
    super(new HashtableMap<>());
  }

  /**
   * This helper method creates a network of SearchNodes while computing the shortest path between
   * the provided start and end locations. The SearchNode that is returned by this method is
   * represents the end of the shortest path that is found: it's cost is the cost of that shortest
   * path, and the nodes linked together through predecessor references represent all of the nodes
   * along that shortest path (ordered from end to start).
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException when no path from start to end is found or when either start or
   *                                end data do not correspond to a graph node
   */
  protected SearchNode computeShortestPath(NodeType start, NodeType end) {

    // Make sure that both start and end nodes exist
    if (!containsNode(start) || !containsNode(end)) {
      throw new NoSuchElementException("Start or end nodes could not be found");
    }

    // set up priority queue, map, start and end nodes
    PriorityQueue<SearchNode> queue = new PriorityQueue<>();
    MapADT<Node, Double> shortestPathCosts = new HashtableMap<>();
    Node startNode = nodes.get(start);
    Node endNode = nodes.get(end);

    // add the first SearchNode node to the queue with a null value and set the maps first value
    queue.add(new SearchNode(startNode, 0.0, null));

    // while loop until the queue is empty
    while (!queue.isEmpty()) {
      SearchNode current = queue.poll();
     
      // check if we have visted the node before
      if(shortestPathCosts.containsKey(current.node) && shortestPathCosts.get(current.node) <= current.cost) {
        continue;
      }
      
      //mark current node as visted
      shortestPathCosts.put(current.node, current.cost);
      // if we are at the last node then we will return it
      if (current.node.equals(endNode)) {
        return current;
      }
      // loop through each of the edges 
      for(Edge edge : current.node.edgesLeaving) {
        Node neighbor = edge.successor;
        double newCost = current.cost + edge.data.doubleValue();
        
        // if the shortest path of costs has the neighbor or shortestOathCost's neibor is greater 
        // than the new cost then put the neighbor with the newCost in the map and and the edge to 
        // the queue
        if(!shortestPathCosts.containsKey(neighbor) || newCost < shortestPathCosts.get(neighbor)) {
          //shortestPathCosts.put(neighbor,  newCost);
          queue.add(new SearchNode(neighbor, newCost, current));
        }
      }
    }
    // Throw NoSuchElemntException if no path can be found
    throw new NoSuchElementException("No path found from start to end node.");
  }

  /**
   * Returns the list of data values from nodes along the shortest path from the node with the
   * provided start value through the node with the provided end value. This list of data values
   * starts with the start value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shorteset path. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end) {
 // create a search node to contain our path and our path that we will add to
    SearchNode endNode = computeShortestPath(start, end);
    LinkedList<NodeType> path = new LinkedList<>();
    
    // while the node isn't null keep getting its predecessor and add it to the list
    SearchNode current = endNode;
    while(current != null) {
      path.addFirst(current.node.data);
      current = current.predecessor;
    }
    return path;
  }

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest path freom the node
   * containing the start data to the node containing the end data. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   */
  public double shortestPathCost(NodeType start, NodeType end) {
    
    // return the cost of the shortest path
    SearchNode endNode = computeShortestPath(start, end);
    return endNode.cost;
  }
}
