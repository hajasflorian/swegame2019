package player;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;

import ki.Graph;
import map.Point;
import map.TerrainType;

public class PlayerGameInformation {
	
	private Point myPosition = new Point(-1,-1,false);
	private Point enemyPosition = new Point(-1,-1,false);
	private Point myFortPosition = new Point(-1,-1,false);
	private Point enemyFortPosition = new Point(-1,-1,false);
	private Point treasurePosition = new Point(-1,-1,false);
	private HashMap<Point, TerrainType> map = new HashMap<Point, TerrainType>();
	private String playerId = null;
	private String enemyId = null;
	private boolean collectedTreasure = false;
	private ArrayList<Point> possibleTreasurePositions = new ArrayList<Point>();
	private ArrayList<Point> settledPositions = new ArrayList<Point>();
	private Graph graph = new Graph();

	private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener l) {
		//enables to register new listeners
		changes.addPropertyChangeListener(l);
	}

	public Point getMyPosition() {
		return myPosition;
	}

	public void setmyPosition(Point myPosition) {
		Point beforeChange = this.myPosition;
		this.myPosition = myPosition;
		changes.firePropertyChange("MyPosition", beforeChange, myPosition);

	}

	public Point getEnemyPosition() {
		return enemyPosition;
	}

	public void setEnemyPosition(Point enemyPosition) {
		Point beforeChange = this.enemyPosition;
		this.enemyPosition = enemyPosition;
		changes.firePropertyChange("EnemyPosition", beforeChange, enemyPosition);
	}
	
	public Point getMyFortPosition() {
		return myFortPosition;
	}

	public void setMyFortPosition(Point myFortPosition) {
		Point beforeChange = this.myFortPosition;
		this.myFortPosition = myFortPosition;
		changes.firePropertyChange("MyFortPosition", beforeChange, myFortPosition);
	}
	
	public Point getEnemyFortPosition() {
		return enemyFortPosition;
	}

	public void setEnemyFortPosition(Point enemyFortPosition) {
		Point beforeChange = this.enemyFortPosition;
		this.enemyFortPosition = enemyFortPosition;
		changes.firePropertyChange("EnemyFortPosition", beforeChange, enemyFortPosition);
	}

	public HashMap<Point, TerrainType> getMap() {
		return map;
	}

	public Point getTreasurePosition() {
		return enemyFortPosition;
	}

	public void setTreasurePosition(Point treasurePosition) {
		Point beforeChange = this.treasurePosition;
		this.treasurePosition = treasurePosition;
		changes.firePropertyChange("TreasurePosition", beforeChange, treasurePosition);
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
	
	public boolean isCollectedTreasure() {
		return collectedTreasure;
	}

	public void setCollectedTreasure(boolean collectedTreasure) {
		this.collectedTreasure = collectedTreasure;
	}
	
	public ArrayList<Point> getPossibleTreasurePositions() {
		return possibleTreasurePositions;
	}

	public void setPossibleTreasurePositions(ArrayList<Point> possibleTreasurePositions) {
		ArrayList<Point> before = this.possibleTreasurePositions;
		this.possibleTreasurePositions = possibleTreasurePositions;
		changes.firePropertyChange("PossibleTreasurePositions", before, possibleTreasurePositions);
	}

}
