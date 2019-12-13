package ki;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import map.Point;
import map.TerrainType;

public class MoveAlgo {

	private HashMap<Node, TerrainType> algoMap;
	private Node node;
	private int width = 0;
	private int height = 0;

	public MoveAlgo(HashMap<Point, TerrainType> map) {
		map.forEach((k, v) -> {
			if(width < k.getY()) {
				width = k.getY();
			}
			if(height < k.getX()) {
				height = k.getX();
			}
			if (!v.equals(TerrainType.Water)) {
				node = new Node(k.getX(), k.getY());
				algoMap.put(node, v);
				if (v.equals(TerrainType.Mountain)) {
					node.setMOVE_COST(20);
				}
			}
		});
		
		map.forEach((k, v) -> {
			if (!v.equals(TerrainType.Water)) {
//				if(k.getX()-1 > width.)
			}
		});
	}

	// https://github.com/eugenp/tutorials/tree/master/algorithms-miscellaneous-2/src/main/java/com/baeldung/algorithms/ga/dijkstra
	public Graph calculateShortestPathFromSource(Graph graph, Node source) {
		source.setDistance(0);

		// nodes discovered but not visited
		List<Node> settledNodes = new LinkedList<Node>();

		// nodes not discovered and not visited
		List<Node> unsettledNodes = new LinkedList<Node>();

		unsettledNodes.add(source);

		while (unsettledNodes.size() != 0) {
			Node currentNode = getLowestDistanceNode(unsettledNodes);
			unsettledNodes.remove(currentNode);
//			settledNodes.add(currentNode);
//			getNeighbours(currentNode, unsettledNodes);
			for (Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
				Node adjacentNode = adjacencyPair.getKey();
				Integer edgeWeight = adjacencyPair.getValue();
				if (!settledNodes.contains(adjacentNode)) {
					calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
					unsettledNodes.add(adjacentNode);
				}
			}
			settledNodes.add(currentNode);
		}
		return graph;
	}

	private Node getLowestDistanceNode(List<Node> unsettledNodes) {
		Node lowestDistanceNode = null;
		int lowestDistance = Integer.MAX_VALUE;
		for (Node node : unsettledNodes) {
			int nodeDistance = node.getDistance();
			if (nodeDistance < lowestDistance) {
				lowestDistance = nodeDistance;
				lowestDistanceNode = node;
			}
		}
		return lowestDistanceNode;
	}

	private void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
		Integer sourceDistance = sourceNode.getDistance();
		if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
			evaluationNode.setDistance(sourceDistance + edgeWeigh);
			LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
			shortestPath.add(sourceNode);
			evaluationNode.setShortestPath(shortestPath);
		}
	}

	public Node getNode(int x, int y) {
		Node node = null;
		Set<Entry<Node, TerrainType>> entrySet = algoMap.entrySet();

		for (Entry<Node, TerrainType> pointTerrainTypeEntry : entrySet) {
			if (pointTerrainTypeEntry.getKey().getX() == x && pointTerrainTypeEntry.getKey().getY() == y) {
				node = pointTerrainTypeEntry.getKey();
			}
		}

		return node;
	}

//	public void getNeighbours(Node currentNode, List<Node> unsettledNodes) {
//		List<Node> neighbours = new LinkedList<Node>();
//		int x = currentNode.getX();
//		int y = currentNode.getY();
//
//		Node neighbour;
//
//		// check right node
//		if (x > 0) {
//			neighbour = getNode(x + 1, y);
//			if (neighbour != null && algoMap.get(neighbour) != TerrainType.Water
//					&& !unsettledNodes.contains(neighbour)) {
//				neighbours.add(neighbour);
//			}
//
//		}
//		
//		// check right node
//		if (x > 0) {
//			neighbour = getNode(x + 1, y);
//			if (neighbour != null && algoMap.get(neighbour) != TerrainType.Water
//					&& !unsettledNodes.contains(neighbour)) {
//				neighbours.add(neighbour);
//			}
//
//		}
//		// check right node
//		if (x > 0) {
//			neighbour = getNode(x + 1, y);
//			if (neighbour != null && algoMap.get(neighbour) != TerrainType.Water
//					&& !unsettledNodes.contains(neighbour)) {
//				neighbours.add(neighbour);
//			}
//
//		}
//	}

}
