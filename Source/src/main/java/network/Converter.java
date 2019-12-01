package network;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import map.MapGenerator;
import map.Point;
import map.TerrainType;

public class Converter {
	
	private MapGenerator mapGenerator = new MapGenerator();

	
	public HalfMap convertMap(String playerId) {
		Collection<HalfMapNode> halfMapNodes = new HashSet<HalfMapNode>();
		HashMap<Point, TerrainType> map = mapGenerator.createMap();
		
		map.forEach((k, v) -> {
			switch (v) {
			case GRASS:
				halfMapNodes.add(new HalfMapNode(k.getX(), k.getY(), k.getFortPresent(), ETerrain.Grass));
				break;
			case WATER:
				halfMapNodes.add(new HalfMapNode(k.getX(), k.getY(), false, ETerrain.Water));
				break;
			case MOUNTAIN:
				halfMapNodes.add(new HalfMapNode(k.getX(), k.getY(), false, ETerrain.Mountain));
				break;
			default:
				break;
			}
		});
		
		HalfMap halfmap = new HalfMap(playerId, halfMapNodes);
		return halfmap;
	}

}
