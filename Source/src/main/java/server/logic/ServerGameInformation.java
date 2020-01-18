package server.logic;

import java.util.HashMap;
import java.util.List;

import map.Point;
import map.TerrainType;

public class ServerGameInformation {
	
	private String gameID;
	private Player player1;
	private Player player2;
	private String gameStateID;
	private boolean majorChange;
	private HashMap<Point, TerrainType> halfMap1;
	private HashMap<Point, TerrainType> halfMap2;
	private Point player1Position;
	private Point player2Position;
	private Point player1FortPostion;
	private Point player2FortPostion;
	private HashMap<Point, TerrainType> fullMap;
	
	public String getGameID() {
		return gameID;
	}

	public void setGameID(String gameID) {
		this.gameID = gameID;
	}
	
	public Player getPlayer(List<Player> playerList, String playerId) {
		for(int i=0; i<playerList.size();i++) {
			if(playerList.get(i).getId().equals(playerId)) {
				return playerList.get(i);
			}
		}
		return null;
	}

	public boolean isPlayerValid(String playerId) {
		if((player1 != null && playerId.equals(player1.getId()) || (player2 != null && playerId.equals(player2.getId())))) return true;
		return false;
	}
	
	public boolean isPlayerTurn(String playerId) {
		if(player1 != null && playerId.equals(player1.getId()) && player1.isYourTurn()) return true;
		if(player2 != null && playerId.equals(player2.getId()) && player2.isYourTurn()) return true;
		return false;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public String getGameStateID() {
		return gameStateID;
	}

	public void setGameStateID(String gameStateID) {
		this.gameStateID = gameStateID;
	}

	public boolean isMajorChange() {
		return majorChange;
	}

	public void setMajorChange(boolean majorChange) {
		this.majorChange = majorChange;
	}

	public HashMap<Point, TerrainType> getHalfMap1() {
		return halfMap1;
	}

	public void setHalfMap1(HashMap<Point, TerrainType> map) {
		this.halfMap1 = map;
	}

	public HashMap<Point, TerrainType> getHalfMap2() {
		return halfMap2;
	}

	public void setHalfMap2(HashMap<Point, TerrainType> halfMap2) {
		this.halfMap2 = halfMap2;
	}

	public HashMap<Point, TerrainType> getFullMap() {
		return fullMap;
	}

	public void setFullMap(HashMap<Point, TerrainType> fullMap) {
		this.fullMap = fullMap;
	}

	public Point getPlayer1Position() {
		return player1Position;
	}

	public void setPlayer1Position(Point player1Position) {
		this.player1Position = player1Position;
	}

	public Point getPlayer2Position() {
		return player2Position;
	}

	public void setPlayer2Position(Point player2Position) {
		this.player2Position = player2Position;
	}

	public Point getPlayer1FortPostion() {
		return player1FortPostion;
	}

	public void setPlayer1FortPostion(Point player1FortPostion) {
		this.player1FortPostion = player1FortPostion;
	}

	public Point getPlayer2FortPostion() {
		return player2FortPostion;
	}

	public void setPlayer2FortPostion(Point player2FortPostion) {
		this.player2FortPostion = player2FortPostion;
	}

}
