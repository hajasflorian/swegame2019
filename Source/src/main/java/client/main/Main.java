package client.main;

import client.ClientLogic;
import client.PlayerGameInformation;

public class Main {

	public static void main(String[] args) {

		String serverBaseUrl = args[1];
		String gameId = args[2];
		
		PlayerGameInformation playerGameInformation = new PlayerGameInformation();
		
		ClientLogic clientLogic = new ClientLogic(playerGameInformation);
		clientLogic.registerToGame(serverBaseUrl, gameId);

	}

}
