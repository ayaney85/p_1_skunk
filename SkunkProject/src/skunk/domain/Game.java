package skunk.domain;


import java.util.ArrayList;


public class Game 
{
	
	// Constants
	private static final int CONSTANT_MAX_NUMBER_PLAYERS = 30;
	private static final int ERROR_INVALID_PLAYER_NUMBER = -1;
	private static final int PENALTY_OF_LOOSING = 5;
	private static final int PENALTY_ZERO_SCORE = 10;
	private static int TARGET_SCORE = 100;
	
	
	private boolean StartGame = false;
	private boolean EndGame = false;
	
	
	private int iNumOfPlayers;
	private int iWinnerIndex = -1;
	private String[] NameOfPlayers;
	public ArrayList<Player> players = new ArrayList<Player>();
	

	private static final String RULES = 
			"DIRECTIONS FOR PLAYING:\r\n" + 
	        "\r\n" + 
			"The object of the game is to accumulate a score of 100 points or more. A score is made by rolling the dice\r\n" +
			"and combining the points on the two dice." + "For example: A 4 and 5 would be 9 points - if the player decides\r\n" +
			"to take another roll of the dice and turns up a 3 and 5 (8 points), he would then have an accumulated total of 17\r\n" +
			"points for the two rolls. The player has the privilege of continuing to shake to increase his score or of passing the\r\n" +
			"dice to wait for the next series, thus preventing the possibility of rolling a Skunk and losing his score.\r\n" + 
			"\r\n" + 
			"PENALTIES:\r\n" + 
			"\r\n" + 
			"A skunk in any series voids the score for that series only and draws a penalty of 1 chip placed in the \"kitty,\" and loss of dice.\r\n" + 
			"A skunk and a deuce voids the score for that series only and draws a penalty of 2 chips placed in the \"kitty,\" and loss of dice.\r\n" +  
			"TWO skunks void the ENTIRE accumulated score and draws a penalty of 4 chips placed in the \"kitty,\" and loss of dice. Player must again\r\n" +
			"start to score from scratch.\r\n" + 
			"\r\n" + 
			"ADDITIONAL RULES:\r\n" + 
			"\r\n" + 
			"Any number can play. [Assume at least two players!] The suggested number of chips to start is 50.There are sufficient chips in the box to allow\r\n" +
			"8 players to start with 50 chips by placing a par value of \"one\" on white chips, 5 for 1 on red chips and 10 for 1 on the blue chips.\r\n" + 
			"\r\n" + 
			"The first player to accumulate a total of 100 or more points can continue to score as many points over 100 as he believes is needed to win.\r\n" +
			"When he decides to stop, his total score is the \"goal.\" Each succeeding player receives one more chance to better the goal and end the game.\r\n" + 
			"The winner of each game collects all chips in \"kitty\" and in addition five chips from each losing player or 10 chips from any player without a score.\r\n";
	
	// Class objects
	private SkunkUI ui;
	private Turn turn;
	
	public Player activePlayer;
	public Kitty kittyObj;
	
	
	//**********************************************************
	
	// Game Constructor 
	public Game() 
	{
		ui = new SkunkUI();
		this.NameOfPlayers = new String[CONSTANT_MAX_NUMBER_PLAYERS];
		this.players = new ArrayList<Player>();
		turn = new Turn( );
	}

	//**********************************************************
	
	public String getGameStatus()
	{
		if (this.StartGame == true) 
		{
			return "Skunk game has started.";
		}
		else 
		{
			return "Skunk game has NOT started.";
		}
	}
	
	//**********************************************************
	
	public void startGame()
	{
		this.StartGame = true;
	}
	
	public boolean endGame()
	{
		return this.EndGame = false;
	}

	//**********************************************************

	/*Each Game ends with one or more players scoring >=100 (needs to be done); 
	 need clarification on how the game exits 

	Followed by a final set of Turns giving all non-100 players a final chance to 
	increase their score (needs to be done) 
	
	The first player to accumulate a total of 100 or more points can continue to 
	score as many points over 100 as he believes is needed to win. When he decides to stop, 
	his total score is the �goal.� Each succeeding player receives one more chance to 
	better the goal and end the game.  
	
	The winner of each game collects all chips in "kitty" and in addition five chips 
	from each losing player or 10 chips from any player without a score. 
	12/8/2020: Keep this requirement within Player class.  */
	
	
	public boolean run() 
	{
		boolean bStatus = false;
		
		// Ask for Game Rules
		
		askGameRules();
		
		// Ask for number of players.
		askAndParse_NumberOfPlayers();
		
		// If the number of players entered in invalid, try again.
		int iStatus = validateNumberOfPlayers(this.iNumOfPlayers);
	
		
		while( iStatus < 0 ) 
		{
			askAndParse_NumberOfPlayers();
			iStatus = validateNumberOfPlayers( this.iNumOfPlayers );
		} 
		
		// Ask for player's name, can add try and catch here.
		savePlayerNamesInArray();
		
		//For P1.2: Just one Player.
		//One complete interactive turn of skunk with one human player.
		boolean bContinue = true;
		int iTurnCount = 0;
		
		do
		{
			//Check how many numbers of players
			//If only one player left, stop the game.
			int iNumOfPlayer = get_Num_of_Players_in_the_game();
			if( iNumOfPlayer < 2 )
			{
				ui.printLine( "Not enough Players in the game. Game OVER..." );
				bContinue = false;
				break;
			}
			
			
			for( int iPlayer=0; iPlayer<this.iNumOfPlayers; iPlayer++ )
			{
				iStatus = turn.playTurn(players.get(iPlayer), iPlayer);
				//ui.printLine( "Turn is OVER." );
			}
			
			ui.printLine( "********** Turn: " + (iTurnCount+1) +"**********" );
			for( int iPlayer=0; iPlayer<this.iNumOfPlayers; iPlayer++ )
			{
				turn.printOverAllScore(players.get(iPlayer));

				int iChips = players.get(iPlayer).getPlayerChipCount();
				
				//If player has zero or negative chips do not let them roll the dice.
				//Their score will remain.
				if( ( iChips <= 0 ) && players.get(iPlayer).is_bIsPlayerInTheGame() ) 
				{
					ui.printLine( "Player: " + players.get(iPlayer).getPlayerName() + " zero or negative chips count. Taking this player out of the game." );

					players.get(iPlayer).set_bIsPlayerInTheGame( false );
					
					bContinue = true;
					break;
				}
					
			}
			ui.printLine( "*********************************\n" );
			
			//
			//Check score and chips count
			//
			this.iWinnerIndex = find_MaxScoreIndex();
			
			if( this.iWinnerIndex > -1 )
			{
				update_ChipsFromKitty();
			
				update_ChipsFrom_OtherPlayers();
			}
			
			//
			// Give option to continue the game.
			//
			if( bContinue )
			{
				String strTemp = ui.printLineRead_Yes_No("Do you want to play one more Turn?" );
							
				if( strTemp.trim().equalsIgnoreCase("y") )
				{
					bContinue = true;
				}
				else if( strTemp.trim().equalsIgnoreCase("n") )
				{
					ui.printLine( "User entered No. Exiting game." );
					bContinue = false;
					break;
				}
				else
				{
					ui.printLine( "invalid reponse. Exiting game." );
					bContinue = false;
					break;
				}
			}
			
			
			iTurnCount++;
		} while( bContinue );
		
		//Print score 
		
		
		bStatus = (iStatus == 0) ? false: true; 
			
		return bStatus;
	}


	//**********************************************************
	
	private int get_Num_of_Players_in_the_game() 
	{
		int iPlayerCount = 0;
		
		for( int iPlayer=0; iPlayer<this.iNumOfPlayers; iPlayer++ )
		{
			if( players.get(iPlayer).is_bIsPlayerInTheGame() )
				iPlayerCount++;
		}
		
		return iPlayerCount;
	}
	
	//**********************************************************
	
	public void askAndParse_NumberOfPlayers() 
	{
		String strTemp;

		try
		{
			strTemp = ui.printLineReadResponse( "Enter number of players?" );
			this.iNumOfPlayers = Integer.parseInt( strTemp );
		}
		catch( Exception e )
		{
			ui.printLine( "invalid iNumOfPlayers entered" );
		}
	}

	//**********************************************************
	// Game will communicate with controller and get the number of players.
	// Set the parsed number of players in the Player class.
	//**********************************************************

	public int validateNumberOfPlayers(int iNumOfPlayers)
	{
		if( iNumOfPlayers <= 0 )
		{
			ui.printLine( "invalid iNumOfPlayers: " + iNumOfPlayers + "\nEnter number of players greater than zero?\n" );
			return ERROR_INVALID_PLAYER_NUMBER;
		}
		
		return 0;
	}
	
	//**********************************************************
	// Game will communicate with controller and get the names of players.
	// Set the names of the players in the Player class.
	//**********************************************************
		
	public void savePlayerNamesInArray() 
	{
		String strTemp;
		for ( int iii=0; iii < this.iNumOfPlayers; iii++ )
		{
			try
			{
				strTemp = ui.printLineReadNames( iii, "\nEnter Name of Player " + (iii+1) + ": " );
				this.NameOfPlayers[iii] = strTemp;
				this.players.add(new Player(strTemp, iii ));
			}
			catch( IllegalArgumentException e)
			{
				//Empty string entered or user hit return key.
				ui.printLine( "invalid Name entered." );
			}
			//catch( Exception e )
			//{
			//	//Any unexpected exception
			//	ui.printLine( "unexpected exception." );
			//	ui.printLine( e.toString() );
			//}
		}
	}

	//**********************************************************
	//**********************************************************
	
	public int find_MaxScoreIndex()
	{
		int iCurrentScore = 0;
		int iMaxScore = 0;
		int iIndex = -1;
		
		for( int iPlayer=0; iPlayer<this.iNumOfPlayers; iPlayer++ )
		{
			iCurrentScore = players.get(iPlayer).getPlayerScore();
			
			if( iCurrentScore >= TARGET_SCORE )
			{
				if( iCurrentScore > iMaxScore )
				{
					iMaxScore = iCurrentScore;
					iIndex = iPlayer;
				}
			}
		}
		
		return iIndex;
	}
	
	//**********************************************************
	//**********************************************************
	
	public void update_ChipsFromKitty()
	{
		int iCurrentScore = 0;
		int iWinInd = this.iWinnerIndex;

		//Do it only one time. Get flag to check if it is first time. If false it is first time
		if( !players.get( iWinInd ).get_bUpdatedChips_FromKitty_Flag() )
		{
			iCurrentScore = players.get( iWinInd ).getPlayerScore();
			
			ui.printLine( players.get( iWinInd ).getPlayerName() + "'s is the WINNER with score: " + iCurrentScore );
			
			int iKittyChips = Kitty.get_iChips();
			players.get( iWinInd ).setPlayerChipCount( players.get( iWinInd ).getPlayerChipCount() + iKittyChips );
			Kitty.set_iChips( 0 );
			
			
			//Set flag to true.
			players.get( iWinInd ).set_bUpdatedChips_FromKitty_Flag(true);
			
			ui.printLine( players.get( iWinInd ).playerName + "'s gets chips from kitty: " +  iKittyChips );
		}		
	}
	
	//**********************************************************
	//**********************************************************
	
	public void update_ChipsFrom_OtherPlayers()
	{
		int iWinInd = this.iWinnerIndex;
		
		//Do it only one time. Get flag to check if it is first time. If false it is first time
		if( !players.get(iWinInd).get_bChipsFromPlayers_updated() )
		{
			//ui.printLine(players.get( iWinInd).getPlayerName() + "'s is the WINNER with score: " + players.get( iWinInd ).getPlayerScore() + " gets penalty chips from other players. " );
			ui.printLine(players.get( iWinInd).getPlayerName() + "'s gets penalty chips from other players. " );
					
			for( int iPlayer=0; iPlayer < this.iNumOfPlayers; iPlayer++ )
			{
				if( iPlayer != iWinInd )
				{
					if( players.get(iPlayer).getPlayerScore() <= 0 )
					{
						players.get(iPlayer).update_PlayerChipCount( -PENALTY_ZERO_SCORE );
						players.get(this.iWinnerIndex).update_PlayerChipCount( PENALTY_ZERO_SCORE );
					}
					else
					{
						players.get(iPlayer).update_PlayerChipCount( -PENALTY_OF_LOOSING );
						players.get(this.iWinnerIndex).update_PlayerChipCount( PENALTY_OF_LOOSING );
					}
					
					//Update the flag
					players.get(iWinInd).set_bChipsFromPlayers_updated(true);
				}
			}
			
			ui.printLine(players.get(iWinInd).playerName + "'s Total Chip count: " + players.get(iWinInd).getPlayerChipCount() );
			
		}
		
		ui.printLine( "\n*********************************" );
		ui.printLine( "********** WINNER:" + players.get(iWinInd).getPlayerName() + "**********" );
		for( int iPlayer=0; iPlayer<this.iNumOfPlayers; iPlayer++ )
		{
			turn.printOverAllScore(players.get(iPlayer));
		}
		ui.printLine( "*********************************" );
		ui.printLine( "*********************************\n" );
	}

	
	//**********************************************************
	// Return total players.
	
	public int getPlayers()
	{
		int total = players.size();
		return total;
	}

	//**********************************************************
	// Method to add Player to Game. 
	
	public void addPlayer(String string) 	
	{
		Player player = new Player(string);
		players.add(player);
	}
	
	//**********************************************************
	
	// Method to remove one Player from Game. 
	
	public void removeOnePlayer(int iIndex) 	
	{
		players.remove(iIndex);
	}
	
	//**********************************************************
		
	// Method to REMOVE all Players from Game. 
	
	public void removePlayers() 
	{
		players.clear();
	}
	
	//**********************************************************
	
	public void askGameRules()
	{
		String szUserResp = ui.printLineRead_Yes_No("\nDo you want to see the Skunk Game rules? y or n");
		szUserResp.trim();
		
		char chResp = szUserResp.toLowerCase().charAt(0);
		
		if( chResp == 'y' ) 
		{
		ui.printLine("--------------------------------------------------------------------------------------------------");
		ui.printLine(RULES);
		ui.printLine("--------------------------------------------------------------------------------------------------");
		}
	}
	
	//**********************************************************
	
}