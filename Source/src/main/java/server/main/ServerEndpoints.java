package server.main;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import server.exceptions.GenericExampleException;
import server.exceptions.InvalidGameIdException;
import server.exceptions.MapIsNotValidException;
import server.exceptions.PlayerIdException;
import server.logic.ServerLogic;

@Controller
@RequestMapping(value = "/games")
public class ServerEndpoints {

	private ServerLogic serverLogic = new ServerLogic();

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame() {

		UniqueGameIdentifier gameIdentifier = new UniqueGameIdentifier(serverLogic.createNewGame(5));
		System.out.println("New Game" + gameIdentifier.toString());
		return gameIdentifier;

	}

	@RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(@PathVariable String gameID,
			@Validated @RequestBody PlayerRegistration playerRegistration) {
		System.out.println("Register player " + gameID);
		if (serverLogic.isGameIdValid(gameID)) {
			UniquePlayerIdentifier newPlayerID = serverLogic.newPlayerCreation(gameID, playerRegistration);
			ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(newPlayerID);
			serverLogic.createGameStateId(gameID);
			return playerIDMessage;

		} else {
			throw new InvalidGameIdException("Name: InvalidGameIdException",
					"Message: The game ID used during the registration is not exists or not valid anymore!");
		}

	}

	@RequestMapping(value = "/{gameID}/halfmaps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<?> postHalfMap(@PathVariable String gameID, @Validated @RequestBody HalfMap halfMap) {
		System.out.println("Posting halfmap "+ gameID + " " + halfMap.getUniquePlayerID());
		if (serverLogic.isGameIdValid(gameID)) {
			if (serverLogic.isPlayerIdValid(gameID, halfMap.getUniquePlayerID())) {
				if(serverLogic.isMapValid(gameID, halfMap)) {
					serverLogic.togglePlayer(gameID);
					serverLogic.createGameStateId(gameID);
					ResponseEnvelope<?> halfMapResponse = new ResponseEnvelope<>();
					return halfMapResponse;
				} else {
					throw new MapIsNotValidException("MapIsNotValidException", "Map is not valid!");
				}
			} else {
				throw new PlayerIdException("PlayerIdException",
						"Player ID is not valid");
			}
		} else {
			throw new InvalidGameIdException("InvalidGameIdException",
					"The game ID used during the registration is not exists or not valid anymore!");
		}

	}

	@RequestMapping(value = "/{gameID}/states/{playerID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<GameState> getState(@PathVariable String gameID,
			@PathVariable String playerID) {
		System.out.println("State for: " + playerID);

		if (serverLogic.isGameIdValid(gameID)) {
			if (serverLogic.isPlayerIdValid(gameID, playerID)) {
				return new ResponseEnvelope<>(serverLogic.setUpGameState(gameID, playerID));
			} else {
				throw new PlayerIdException("PlayerIdException",
						"Player ID is not valid");
			}
		} else {
			throw new InvalidGameIdException("InvalidGameIdException",
					"The game ID used during the registration is not exists or not valid anymore!");
		}

	}

	@ExceptionHandler({ GenericExampleException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(GenericExampleException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());

		response.setStatus(HttpServletResponse.SC_OK); // reply with 200 OK as defined in the network documentation
		return result;
	}
}
