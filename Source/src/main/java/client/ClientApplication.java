package client;

public class ClientApplication {

	private String baseUrl;
	private String gameId;

	public ClientApplication(String baseUrl, String gameId) {
		this.baseUrl = baseUrl;
		this.gameId = gameId;
	}

	public void start() {
		ClientLogic clientLogic = new ClientLogic();
		clientLogic.registerToGame(baseUrl, gameId);
	}

}
