import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

import map.Point;
import map.TerrainType;

public class PlayerGameInformation {
	
	private Point playerPosition;
	private Point enemyPosition;
	private HashMap<Point, TerrainType> map;
	private String playerId;
	private String enemyId;
	
	private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener l) {
		//enables to register new listeners
		changes.addPropertyChangeListener(l);
	}

	public Point getPlayerPosition() {
		return playerPosition;
	}

	public void setPlayerPosition(Point playerPosition) {
		Point beforeChange = this.playerPosition;
		this.playerPosition = playerPosition;
		changes.firePropertyChange("PlayerPosition", beforeChange, playerPosition);

	}

	public Point getEnemyPosition() {
		return enemyPosition;
	}

	public void setEnemyPosition(Point enemyPosition) {
		Point beforeChange = this.enemyPosition;
		this.enemyPosition = enemyPosition;
		changes.firePropertyChange("EnemyPosition", beforeChange, enemyPosition);
	}

	public HashMap<Point, TerrainType> getMap() {
		return map;
	}

	public void setMap(HashMap<Point, TerrainType> map) {
		HashMap<Point, TerrainType> beforeChange = this.map;
		this.map = map;
		changes.firePropertyChange("Map", beforeChange, map);
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		String beforeChange = this.playerId;
		this.playerId = playerId;
		changes.firePropertyChange("PlayerId", beforeChange, playerId);
	}

	public String getEnemyId() {
		return enemyId;
	}

	public void setEnemyId(String enemyId) {
		String beforeChange = this.enemyId;
		this.enemyId = enemyId;
		changes.firePropertyChange("EnemyId", beforeChange, enemyId);
	}

}
