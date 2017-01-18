import java.util.Random;

/*
 * Rules to "Avalon" may be found here --> http://upload.snakesandlattes.com/rules/r/ResistanceAvalon.pdf
 * WHICH FUNCTIONS WORK?
 * 	randomInt, printer, main, beginGame, generate7Player, generateSinglePlayer
 * WHAT IN THE CLASS WORKS?
 * 	all of em, to do operations on the array use the changeArray funct in the genericPlayer class
 * 	
 * 	A lot of the code is just comments
 * -- info -- means strikethrough, idea was had but not expanded / dont like anymore
 * 
 * TODO
 * 	Finish Some of turn3Choose, All of turn4Choose, turn5choose
 * 	Start rejection method
 * 	Implement a method that handles the increase or decrease of suspect arrays
 * 		-> Method would be called many times in most cases where an important decision is made 
 * 	Implement the thing for choosing Merlin / Mordred in the end
 * 	Implement the kings token thing (use the index of the arrays?)
 * 
 * NOTES --> 10/10 to reduce code duplication, make the turn functions call a choosing thing depending
 * 			on number of victories
 * 			Example) if(badGuyWins == 0) badNoWins(); else if (badGuyWins == 2) callInMerlinDotJpeg();
 * 		--> Do not forget to reset your playersChosen array after each decision goes through
 * 
 * NOTABLE ERRORS
 * 		Currently in turn2Choose an error has been brought up where the decision of choosing
 * 		the same people as last -successful- turn may be funky
 * 
 * CHANGELOG
 * 		
 */

public class game {
	// Array below is meant to store all of the player objects
	static genericPlayer playerz[] = new genericPlayer[7]; // Should this be static? else gives an error
	static int playersChosen[] = new int[7]; // a "1" in the array means that person at index was chosen
	final static int turns = 5;
	static int goodGuyWins = 0; // Number of times good guys have won
	static int badGuyWins = 0; // Number of times bad guys have won
	static int x = 0;
	static int takenUp[] = new int[7];
	// All helper functions are up here

	/**
	 * Generates a random number given a max and a min The purpose of this is to
	 * assign a "number" to each player. If that number is taken up , then it
	 * goes to the next one
	 * 
	 * @param min
	 *            for current (7 player) array purposes should be "0"
	 * @param max
	 *            for current (7 player) array purposes should be "6"
	 * @return
	 */
	public static int randomInt(int min, int max) {
		Random rand = new Random();
		int randomNum;
		// We need it to execute at least once
		do {
			randomNum = rand.nextInt((max - min) + 1) + min;
		} while (takenUp[randomNum] != 0); // While you have not found something
											// taken up
		takenUp[randomNum] = 1;// The array now has something in it and that
								// index is used
		return randomNum;
	}

	/**
	 * This is just to print results for testing things
	 */
	public static void printer() {
		System.out.println("Testing changing things like character and array");
		playerz[0].setCharacter("HELLOOO");
		int changedArray[] = { 1, 2, 3, 4, 5, 6, 7 };
		playerz[0].setSuspects(changedArray);
		// How to do operations on the array? make a function in genericPlayer
		playerz[0].changeArray(3, 38);
		playerz[0].printCharacterAspects();
	}

	public static void main(String args[]) {
		beginGame(7);
	}

	/**
	 * This will begin the game and assign the roles for now, will always be
	 * called with "7"
	 * 
	 * @param numberOfPlayers
	 *            --> Players in the game
	 */
	public static void beginGame(int numberOfPlayers) {
		if (numberOfPlayers == 7) {
			generate7Player();
		} // End if for seven players
	} // End begin game

	/**
	 * Generate the 7 players afaik this function looks long, but is ok For now,
	 * do not worry about it being 25+ lines long, possible split up where you
	 * determine where the people are positioned but we should know them anyways
	 * Possible bug --> having themselves as a 100 -not as a -1- may lead to
	 * funny results ie)Players deducing that they themselves, are actually evil
	 * when they are not
	 */
	public static void generate7Player() {
		/*
		 * Set their positions, important as bad guys must know who is on their
		 * side Merlin must know the identity of the bad guys Percival must know
		 * the identity of MORGANA, and Merlin
		 */
		int baddiePosn, mordredPosn, morganaPosn, merlinPosn, percivalPosn, goodiePosn, goodie2Posn;
		// Idea is to generate baddies before making Merlin so he knows who they are,
		// And generate Merlin before Percival
		baddiePosn = randomInt(0, 6);
		mordredPosn = randomInt(0, 6);
		morganaPosn = randomInt(0, 6);
		merlinPosn = randomInt(0, 6);
		percivalPosn = randomInt(0, 6);
		goodiePosn = randomInt(0, 6);
		goodie2Posn = randomInt(0, 6);
		// Have the default array
		int sussArray[] = { 100, 100, 100, 100, 100, 100, 100 };
		// Generate the players
		// Bad guys use the alliance attribute to tell each other apart
		generateSinglePlayer("Bad1", 1, sussArray, baddiePosn);
		generateSinglePlayer("Mordred", 1, sussArray, mordredPosn);
		generateSinglePlayer("Morgana", 1, sussArray, morganaPosn);
		generateSinglePlayer("Good1", 0, sussArray, goodiePosn);
		generateSinglePlayer("Good2", 0, sussArray, goodie2Posn);
		// Change up the array according to Percival / Merlin
		// Merlin knows who's bad --> may refuse to put them on the last position
		int merlinArray[] = { 100, 100, 100, 100, 100, 100, 100 };
		merlinArray[baddiePosn] = -100;
		merlinArray[mordredPosn] = -100;
		merlinArray[morganaPosn] = -100;
		generateSinglePlayer("Merlin", 0, merlinArray, merlinPosn);
		// Reset the array for Percival (does not know who's bad, just who's Morgana / Merlin)
		int perciArray[] = { 100, 100, 100, 100, 100, 100, 100 };
		perciArray[merlinPosn] = -150; // Percival does not know which is bad
		perciArray[morganaPosn] = -150; // ^
		generateSinglePlayer("Percival", 0, perciArray, percivalPosn);

		System.out.println("Succesfully generated all characters\nCharacters are:");
		for (int i = 0; i < 7; i++) {
			playerz[i].printCharacterAspects();
		}
		printer();
		// goThroughTurns();
	} // End beginGame7Player

	/**
	 * Called by the methods that need the players to be generated
	 * ie)generate7Player Creates them and puts them in an array
	 * 
	 * @param name
	 *            --> Name of the Player
	 * @param identity
	 *            --> 0 = good, 1 = bad
	 * @param sussArray
	 *            --> Suspect Array
	 * @param playerPosn
	 *            --> Where they are "seated"
	 */
	public static void generateSinglePlayer(String name, int identity, int[] suspArray, int playerPosn) {
		genericPlayer player = new genericPlayer(name, identity, suspArray);
		playerz[playerPosn] = player;
		// System.out.println("Generated player" + playerPosn);
	}

	/**
	 * Handles the bulk by going through the turns of the game There are 5 turns
	 * and on the 4th turn baddies need 2 wins Should this be in a for loop?
	 * 
	 * MUST IMPLEMENT CASE WHERE THEY REJECT -->if its the last person, kings
	 * counter blah blah need a while loop for the select players process
	 */
	public static void goThroughTurns() {
		int whoIsKing = 0; // Number for who is KING
		boolean noReject = false;
		for (int turnCount = 1; turnCount <= turns; turnCount++) { // 1,2,3,4,5
			while (noReject == false) {
				selectPlayers(whoIsKing, turnCount);
				// Step 1 select the players to go on the mission
				// Step 2 Reject players etc influence stuff
				noReject = rejection(10); // 10 is just a filler number

				if (whoIsKing == 6) { 
					// If somehow nobody can agree on a team comp (2 healers pls)
					whoIsKing = 0; // Reset King powers to first one
					// In this case, I'd say that if this is reached, then an
					// infinite loop may occur
					System.out.println("This really should not be reached...");
					System.exit(0); // Get outta here
				}
				whoIsKing++; // If not out of the loop, then put the next person
								// as the King
			} // End the rejection loop
				// Step 3 check if fail or pass influence things IF TURN 4 needs
				// two fails
			if (goodGuyWins == 3) { // If good guy count == 3 --> chooseMerlin
				System.out.println("Good guys win! Bad must snipe Merlin");
				chooseMerlin();
			} else if (badGuyWins == 3) { // else if bad guy count == 3 -->
											// chooseMordred
				System.out.println("Bad guys win! Good must snipe Mordred");
				chooseMordred();
			}
		} // End turns loop
	} // End goThroughTurns

	/**
	 * Purpose of this function is to determine the players that will go on the
	 * quest Always chooses themselves at least (because I know I'm good, I
	 * choose me) --Should work in parallel with a "rejection" system-- nah
	 * 
	 * @param whosChoosing
	 *            --> Who is deciding on who goes on the mission
	 * @param whichTurn
	 *            --> Number of players chosen depends on which turn it is Turn
	 *            Number 1, 2, 3, 4, 5 7 Players --> 2, 3, 3, 4, 4
	 */
	public static void selectPlayers(int whosChoosing, int whichTurn) {
		if (whichTurn == 1) {
			System.out.println("Turn 1 Good:" + goodGuyWins + "\nBad:" + badGuyWins + "\nSelecting adventurers:");
			playersChosen[whosChoosing] = 1; // 1 Means chosen, also choose themselves
			System.out.println("Player " + whosChoosing + "has elected themselves for adventure!");
			// could call something here to see if anyone rejects but it's the
			// first round
			// turn1Choose(whosChoosing,7);
		} else if (whichTurn == 2) {
			System.out.println("Turn 2 Good:" + goodGuyWins + "\nBad:" + badGuyWins + "\nSelecting adventurers:");
			playersChosen[whosChoosing] = 1;
			System.out.println("Player " + whosChoosing + "has elected themselves for adventure!");
			// again, could call some sort of rejection function but it's the
			// second turn
			// turn2Choose(whosChoosing,7);
		} else if (whichTurn == 3) { // have to start checking which team is in
										// danger of winning
			System.out.println("Turn 3 Good:" + goodGuyWins + "\nBad:" + badGuyWins + "\nSelecting adventurers:");
			playersChosen[whosChoosing] = 1;
			System.out.println("Player " + whosChoosing + "has elected themselves for adventure!");
			/*
			 * call some sort of rejection function, ie) if good guys are going
			 * to win, baddies want to reject a good person (want at least one
			 * bad guy to ruin the lives) or if baddies about to win --> let
			 * Merlin try and convince
			 * 
			 * dont forget to change the "1" back to a zero if rejected by
			 * majority
			 */
			// turn3Choose(whosChoosing,7);
		} else if (whichTurn == 4) { // must check who's in danger of winning,
										// needs 2 defeats
			System.out.println("Turn 4 Good:" + goodGuyWins + "\nBad:" + badGuyWins + "\nSelecting adventurers:");
			playersChosen[whosChoosing] = 1;
			System.out.println("Player " + whosChoosing + "has elected themselves for adventure!");
			// same rejection idea as whichTurn 3
			// turn4Choose(whosChoosing,7);
		} else if (whichTurn == 5) { // must check who's in danger of winning,
										// needs 1 defeat
			System.out.println("Turn 5 Good:" + goodGuyWins + "\nBad:" + badGuyWins + "\nSelecting adventurers:");
			playersChosen[whosChoosing] = 1;
			System.out.println("Player " + whosChoosing + "has elected themselves for adventure!");
			// same rejection idea as whichTurn 3
			// turnChoose5(whosChoosing,7);
		}
	} // End selectPlayers

	/**
	 * Choose one more player
	 * 		Could put a seperate if for Merlin so that he chooses a goodie
	 * 			To build trust up with other goodies
	 * @param whoChooses
	 *            --> Whos choosin
	 * @param gameMode
	 *            --> 7 player game mode, 8 player etc doesnt matter for now
	 */
	public static void turn1Choose(int whoChooses, int gameMode) {
		if (playerz[whoChooses].getAlliance() == 0) { // Good guy chooses arbitrarily
			loop2Choose(1,whoChooses,0);
			// Choose one more player, probably chooses first one they run into
			// In the first turn, everyone claims they are good anyways
		} // End good guy statement
		else { // They are a baddie
			loop2ChooseBad(1,whoChooses,0);
			//Choose one other person, zero baddies
		} // End bad guy statment
	} // End turn1Choose

	/**
	 * Select two more players 
	 * @param whoChooses
	 * @param gameMode --> doesnt matter right now
	 */
	public static void turn2Choose(int whoChooses, int gameMode){
		// SO THAT the other statement is even reachable, for the FIRST TURN
		// bad guy may fail 1st just for giggles (probably a 70% chance) to create chaos
		// At this point, good and bad should be playing same strategy
			if(badGuyWins == 1){ // bad guys for some reason won first round?
				loop2Choose(2,whoChooses,99);
				
			} // End bad guys first victory conditional
			else{ // Good guys won blessed
				// Should theoretically choose the same people as last time
				loop2Choose(2,whoChooses,104);
				// Chooses the same people as the last mission -their suspect woulda increased
				/*
				 * POSSIBLE ERROR
				 * What happens if it's Merlin's / Percival's turn to choose???
				 * Their suspect arrays have negative numbers so what if those negative numbers
				 * Coincide with the people chosen last turn???
				 */
			}  
	} // End turn2Choose
	
	/**
	 * Choose 2 other adventureres
	 * @param whosChooses --> who has kingsToken
	 * @param gameMode --> does nothing yet
	 */
	public static void turn3Choose(int whoChooses, int gameMode){
		// Third round so get ready
			if(badGuyWins == 2){ // bad guys verge of victory
				if(playerz[whoChooses].getAlliance() == 0){
					loop2Choose(2,whoChooses,55);
				}
				else if (playerz[whoChooses].getAlliance() == 1){ // Bad guy has to choose
						// Given how I choose to implement Merlin "ai" -not really ai tbh-
						// Whoever the bad guy chooses may not matter because Merlin may override it
						// So in this case, bad guy may choose other baddie (may or may not be Mordred)
						loop2ChooseBad(2,whoChooses,1);
						// Merlin should stop this mission unless Mordred is known for sure i guess
						
						}
					}
			else if (badGuyWins == 1){ // It's tied
				loop2Choose(2,whoChooses,101);
				// 101 kinda arbitrary, but still indicates trustworthiness as baddies
				// would have some sort of negative number
				// Even if a bad guy, still chooses good guys for "trust" building
				// The bad guy would fail it anyways
			}
			else{ /* badGuys have not won a single game
				  Should have a case for Merlin??? where he only selects goodies? They should all agree
				  If so then it's technically an insta victory for good guys as long as 
				  none of the good guys think Merlin is bad lul
				  */
				if (playerz[whoChooses].getCharacterN().equals("Merlin")){
					loop2Choose(2,whoChooses,0);
					// Select 2 players, 0 just in case for some reason Merlin suspects people
					// Baddies will always be < -100 so thats not a worry
				}// End Merlin ensuring victory
				else if (playerz[whoChooses].getAlliance() == 0){
					loop2Choose(2,whoChooses,120);
					// Select 2 players, 120 currently an arbitrary number for choosing who's in
				}
			}
	} // End turn3Choose
	
	/**
	 *  Choose 3 other adventurers
	 * @param whosChooses --> who has kingsToken
	 * @param gameMode --> does nothing yet
	 */
	public static void turn4Choose(int whoChooses, int gameMode){
		
	}
	
	/**
	 * Choose 3 other adventurers
	 * @param whosChooses --> who has kingsToken
	 * @param gameMode --> does nothing yet
	 */
	public static void turn5Choose(int whosChooses, int gameMode){
		
	}
	
	/**
	 * Purpose is to loop through the suspect array of who's choosing and choose
	 * the appropriate adventurers 
	 * @param numberToPick -> The amount of adventures to pick
	 * @param whoChooses -> Who has choice
	 * @param suspectAmount -> The allowable amount (or lack) of suspicion to go on the mission 
	 */
	public static void loop2Choose(int numberToPick, int whoChooses, int suspectAmount){
		int[] suspec = playerz[whoChooses].getSuspects(); // Pull out your suspect list
		int numberChosen = 0;
		for(int i = 0; i < 6; i++){
			if (numberChosen == numberToPick){
				break; // Get outta here you finished here
			}
			else if(i == whoChooses){
				continue; // You cannot select yourself (already have) to keep saying you good
			}
			else if (suspec[i] > suspectAmount){
				System.out.println("Player " + whoChooses + " has chosen player " + i 
						+ " for the adventure");
				playersChosen[i] = 1; // Select them
				numberChosen++; // Increment
			}
		}
	} // End loop2Choose
	
	/**
	 * Choosing but for bad guys, bad guys may want to choose other bad guys for a variety of reasons
	 * 	1) Build trust with the good guy who also went on the adventure
	 *  2) Have the enemy look like the bad guy yada yada
	 * @param numberToPick -> Amount of adventurers to pick
	 * @param whoChooses -> Who has choice
	 * @param numBaddy -> Number of baddies they want to choose
	 */
	public static void loop2ChooseBad(int numberToPick, int whoChooses, int numBaddy){
		//This function may still elect to choose only good guys lul
		// int[] suspec = playerz[whoChooses].getSuspects(); useless because baddies go off Alliance
		int numberChosen = 0;
		int i = 0;
		int baddieChosen = 0;
		boolean baddiesChosen = false;
		while (baddiesChosen == false){
			// Check this first because if player wants to select no baddies this breaks immediately
			if (baddieChosen == numBaddy){
				baddiesChosen = true;
				break;
				// Get me outta here
			}
			if(i == whoChooses){
				i++; // Increment
				continue;
			}
			else if (playerz[i].getAlliance() == 0){
				i++; // Increment
				continue;
			}
			else if (playerz[i].getAlliance() == 1){
				// They want to choose baddies
				playersChosen[i] = 1;
				baddieChosen++;
				System.out.println("Player " + whoChooses + " has chosen player " + i 
						+ " for the adventure");
				numberChosen++;
				i++; // Increment
			}
		}
		// Choose the good guys
		for(i = 0; i < 6; i++){
			if (numberChosen == numberToPick){
				break; 
				// Exit the loop no more need to loop
			}
			if(i == whoChooses){
				continue;
				// Do not choose yourself
			}
			else if (playerz[i].getAlliance() == 1){
				// Do not want to choose baddies
				continue; // Skip over them
			}
			else if (playerz[i].getAlliance() == 0){
				playersChosen[i] = 1;
				numberChosen++;
				System.out.println("Player " + whoChooses + " has chosen player " + i 
						+ " for the adventure");
			}
		}
	} // End loop2ChooseBad
	
	/**
	 * Ideally, this would take an object / index (int), then cross check
	 * everyone else's "suspect" array to see if they allow it. Would also need
	 * to be seperated by an if / else (for good and bad) if(alliance == 0) -->
	 * do these things, else --> This is needed because they take seperate
	 * actions. Bad likes bad, good dislikes bad
	 * 
	 * @param toReject
	 *            --> The person in question
	 * @return --> True if player not rejected
	 */
	public static boolean rejection(int toReject) {
		// Before returning, should have an increment based on current
		// information
		return true; // If not rejected
	}

	/**
	 * Method currently used for the endgame where the losing team must snipe
	 * the enemy leader -Aside- this can also eventually be used for Lady of the
	 * Lake
	 * 
	 * @param playerToCheck
	 *            --> Passed in player
	 * @return A string to tell the other function the identity of the passed in
	 *         player
	 */
	public static String checkPlayer(int playerToCheck) {
		return playerz[playerToCheck].getCharacterN();
	} // End checkPlayer

	/*
	 * END GAME THINGS
	 */

	/**
	 * This is the final function, this will end the program. Called after one
	 * of the parties win and based on who won, will call out Merlin or Mordred
	 * 
	 * @param A
	 *            boolean that tells us who won true if good, false if baddies
	 */
	public static void endGame(boolean whoWon) {
		if (whoWon == true) { // Good guys won
			int whosThere = chooseMerlin(); // One last chance to snipe Merlin
			if (checkPlayer(whosThere).equals("Merlin")) {
				System.out.println("Bad guys win off assasinating Merlin!");
				System.out.println("Merlin was indeed player " + whosThere);
			} else { // They guessed wrong
				System.out.println("Bad guys could not kill Merlin \nGood guys win");
			}
		} // End good guys won conditional
		else { // Bad guys won
			int whosThere = chooseMordred();
			if (checkPlayer(whosThere).equals("Mordred")) {
				System.out.println("Good guys win off assasinating Mordred!");
				System.out.println("Mordred was indeed player " + whosThere);
			} else {
				System.out.println("Good guys could not kill Mordred \nBad guys win");
			}
		} // End bad guys win conditional
	} // End endGame

	/**
	 * This is the function to be executed iff the good guys win (ie three wins)
	 * Because this is simplified, it doesn't matter which bad guy "chooses"
	 * Merlin. Given the current way to determine who is Merlin, they should end
	 * up at the same result
	 * 
	 * @return an integer allowing a cross-check location in the genericPlayer
	 *         array
	 */
	public static int chooseMerlin() {
		// An integer to return to the function that called this one. Looks up
		// the int in the array
		int theDecision = 0;
		// Insert code here to have the bad guys choose Merlin
		// As of right now always guesses person 0
		return theDecision;
	} // End chooseMerlin

	/**
	 * @see chooseMerlin()
	 * @return an integer allowing a cross-check location in the genericPlayer
	 *         array
	 */
	public static int chooseMordred() {
		int theDecision = 0;
		// Insert code here to have the good guys try and snipe Mordred
		// As of right now always guesses player 0
		// In 20xx, all bad guys speak the same amount, so no real indication of
		// Mordred
		// It falls on a 50/50 where Percival knows who Mordred is but not
		// identity of other 2
		return theDecision;
	} // End chooseMordred
} // End game.java