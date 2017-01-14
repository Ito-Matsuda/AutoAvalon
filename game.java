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
 * TODO
 * 	Finish Some of turn3Choose, All of turn4Choose, turn5choose
 * 	Start rejection method
 * 	Implement a method that handles the increase or decrease of suspect arrays
 * 		-> Method would be called many times in most cases where an important decision is made 
 * 	Implement the thing for choosing Merlin / Mordred in the end
 * 	Implement the kings token thing (use the index of the arrays?)
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
				selectPlayers(whoIsKing, turnCount); // Step 1 select the
														// players to go on the
														// mission
				// Step 2 Reject players etc influence stuff
				noReject = rejection(10); // 10 is just a filler number

				if (whoIsKing == 6) { // If somehow nobody can agree on a team
										// comp (2 healers pls)
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
		int chosenP = 0;
		if (playerz[whoChooses].getAlliance() == 0) { // Good guy
			if (whoChooses != 6) { // Cant pick the next one if you are the last
				chosenP = whoChooses + 1; // First round, select somebody
				playersChosen[chosenP] = 1; // Now chosen
			} else { // You are the last player
				chosenP = whoChooses - 1;
				playersChosen[chosenP] = 1; // Now chosen
				System.out.println("Player " + whoChooses + " has chosen Player " + chosenP 
						+ " to adventure!");
			}
		} // End good guy statement
		else { // They are a baddie
			boolean done = false;
			int i = 0;
			while (done == false) { // Loop until you find a good guy
				if (playerz[i].getAlliance() == 0) { // Pick a good guy to get on their good side
					playersChosen[i] = 1;
					System.out.println("Player " + whoChooses + " has chosen Player " + i 
							+ " to adventure!");
					done = true; // get outta here
				}
				i++; // Move onto the next dude
			}
		} // End bad guy statment
	} // End turn1Choose

	/**
	 * Select two more players 
	 * @param whoChooses
	 * @param gameMode --> doesnt matter right now
	 */
	public static void turn2Choose(int whoChooses, int gameMode){
		int toChoose = 0;
		int chosenP =0;
		int[] suspec = playerz[whoChooses].getSuspects(); // Pull out your suspect list
		// Still only the second round so meh
		// Incorporate bad guys into same
		// SO THAT the other statement is even reachable, for the FIRST TURN
		// bad guy may fail 1st just for giggles (probably a 70% chance)
			if(badGuyWins == 1){ // bad guys for some reason won first round?
				for (int i = 0; i < 6; i++){
					if (toChoose == 2){
						break; // No need to choose any other players
					}
					else if (i == whoChooses){ // Still need to choose players 
						continue; // Can't choose yourself twice
					}
					else if (suspec[i] >= 100){ 
						/* Good guy or bad guy, they'll only "try" to choose good guys
						 * LOGIC for bad guy
						 * Only bad guy, will choose defeat putting baddies up 2-0
						 * Still builds some sort of trust
						 * Leads to commotion amongst other 2 players trying to defend themselves
						 */
						playersChosen[i] = 1; // Choose em
						System.out.println("Player " + whoChooses + " has chosen Player " + i 
								+ " to adventure!");
						toChoose ++; // Increment
					}
				}
			} // End bad guys first victory conditional
			else{ // Good guys won blessed
				// Should theoretically choose the same people as last time 
				for (int i = 0; i < 6; i++){
					if (toChoose == 2){ // 
						break; // No need to choose any other players
					}
					else if (i == whoChooses){ // Still need to choose players 
						continue; // Can't choose yourself twice
					}
					else if (suspec[i] > 101){ // Person is reasonably trustworthy
						// In this position, bad guy will lose here to get score to 1-1
						// UNLESS NEXT PERSON IS A BADDIE, then they will win
						// Next baddie selects same people, but then they lose
						// that "next" baddie now has trust
						// Here greater than 100 because the first round WILL build trust
						// ie) the playerz[whoChooses] suspect array will have the people
						// who went on the first mission (was a success) as a 110 or something
						playersChosen[i] = 1; // Choose em
						System.out.println("Player " + whoChooses + " has chosen Player " + i 
								+ " to adventure!");
						toChoose ++; // Increment
					}
				}
			}  
	} // End turn2Choose
	
	/**
	 * Choose 2 other adventureres
	 * @param whosChooses --> who has kingsToken
	 * @param gameMode --> does nothing yet
	 */
	public static void turn3Choose(int whoChooses, int gameMode){
		int toChoose = 0;
		int badGuyQuota = 0;
		int[] suspec = playerz[whoChooses].getSuspects(); // Pull out your suspect list
		// Third round so get ready
			if(badGuyWins == 2){ // bad guys verge of victory
				for (int i = 0; i < 6; i++){
					if (toChoose == 2){
						break; // No need to choose any other players
					}
					else if (i == whoChooses){ // Still need to choose players 
						continue; // Can't choose yourself twice
					}
					else if (suspec[i] >= 75 && playerz[whoChooses].getAlliance() == 0){ // Good guy decision 
						playersChosen[i] = 1; // Choose em
						System.out.println("Player " + whoChooses + " has chosen Player " + i 
								+ " to adventure!");
						toChoose ++; // Increment
					}
					else if (playerz[whoChooses].getAlliance() == 1){ // Bad guy has to choose
						// Given how I choose to implement Merlin "ai" -not really ai tbh-
						// Whoever the bad guy chooses may not matter because Merlin may override it
						// So in this case, bad guy may choose other baddie (not Mordred to hide him)
						if(playerz[i].getAlliance() == 1 && badGuyQuota == 0){ // Choose at least one baddie
							playersChosen[i] = 1; // Choose the baddie
							System.out.println("Player " + whoChooses + " has chosen Player " + i 
									+ " to adventure!");
							toChoose ++;
						}
						else { // Choose goodies
							playersChosen[i] = 1; // Choose em
							System.out.println("Player " + whoChooses + " has chosen Player " + i 
									+ " to adventure!");
							toChoose ++; // Increment
						// As of right now, this may end up with the baddie choosing 2 good guys
						// That is ok, because the bad guys ai will still -try to- fail it
						// Merlin should stop this mission unless Mordred is known i guess
						}
					}
				}
			} // End bad guys about to win
			else{ // Good guys won blessed
				// Should theoretically choose the same people as last time 
				for (int i = 0; i < 6; i++){
					if (toChoose == 2){ // 
						break; // No need to choose any other players
					}
					else if (i == whoChooses){ // Still need to choose players 
						continue; // Can't choose yourself twice
					}
					else if (suspec[i] > 101){ 
						playersChosen[i] = 1; // Choose em
						toChoose ++; // Increment
					}
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