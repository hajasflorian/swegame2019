package client.main;

import client.ClientApplication;

public class Main {

	public static void main(String[] args) {

		String serverBaseUrl = args[1];
		String gameId = args[2];
		
		ClientApplication client = new ClientApplication(serverBaseUrl, gameId);
		client.start();

	}

}
