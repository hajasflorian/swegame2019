package ki;

import java.util.HashSet;
import java.util.Set;

public class Graph {
	
	// CODE TAKEN FROM START https://github.com/eugenp/tutorials/tree/master/algorithms-miscellaneous-2/src/main/java/com/baeldung/algorithms/ga/dijkstra

    private Set<Node> nodes = new HashSet<>();
     
    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }
 
    public Set<Node> getNodes(){
    	return nodes;
    }
    
    public void setNodes(Set<Node> nodes) {
    	this.nodes = nodes;
    }
    
	// CODE TAKEN FROM END https://github.com/eugenp/tutorials/tree/master/algorithms-miscellaneous-2/src/main/java/com/baeldung/algorithms/ga/dijkstra

}
