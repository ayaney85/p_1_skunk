package skunk.domain;

public class Game {
	
	private boolean gameStarted = false;
	
	public String getGameStatus()
	{
		if (gameStarted == true) 
		{
			return "Skunk game has started.";
		}
		else 
		{
			return "Skunk game has NOT started.";
		}
		
	}
	
	public void gameStart()
	{
		this.gameStarted = true;
	}
	
	
	// Method to return total number of players. Need to follow up with Player class. 
	public int getPlayers()
	{
		return 0;
		
	}

	// Method to add Player to Game. 
	public void addPlayer(String string) {
		// TODO Auto-generated method stub
		
	}
	
	// Method to REMOVE Player from Game. 

	public void removePlayer(String string) {
		// TODO Auto-generated method stub
		// Need method from Player class to remove player. 
		
	}
	
	// Method to get Total Player count for Game

	public int getTotalPlayers() {
		// TODO Auto-generated method stub
		// Need method from Player class for total. 
		// return TotalPlayers
		return 0;
		
	}

}