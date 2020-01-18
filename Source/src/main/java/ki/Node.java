package ki;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Node {
     
	// CODE TAKEN FROM START https://github.com/eugenp/tutorials/tree/master/algorithms-miscellaneous-2/src/main/java/com/baeldung/algorithms/ga/dijkstra

    private List<Node> shortestPath = new LinkedList<>();
     
    private Integer distance = Integer.MAX_VALUE;
     
    Map<Node, Integer> adjacentNodes = new HashMap<>();
    
    private int MOVE_COST = 10;
    
    int x;
	int y;
    
    public Node(int x, int y) {
    	this.x=x;
    	this.y=y;
    }
 
    public void addDestination(Node destination, int distance) {
    	
        adjacentNodes.put(destination, distance);
    }
    
    public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
  

    public Integer getDistance() {
    	return distance;
    }
	public void setDistance(int i) {
		this.distance = i;
	}

	public List<Node> getShortestPath() {
		return shortestPath;
	}

	public void setShortestPath(List<Node> shortestPath) {
		this.shortestPath = shortestPath;
	}

	public Map<Node, Integer> getAdjacentNodes() {
		return adjacentNodes;
	}

	public void setAdjacentNodes(Map<Node, Integer> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}

	public int getMOVE_COST() {
		return MOVE_COST;
	}

	public void setMOVE_COST(int mOVE_COST) {
		this.MOVE_COST = mOVE_COST;
	}
	
	// CODE TAKEN FROM END https://github.com/eugenp/tutorials/tree/master/algorithms-miscellaneous-2/src/main/java/com/baeldung/algorithms/ga/dijkstra

     
}
