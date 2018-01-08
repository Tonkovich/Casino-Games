## Laundry List:
* Complete message passing and managing connections of clients
* Complete poker/slots logic
    * Card/Slot values
    * Logic for best hand
        * Splitting pot if equal values
    * etc..
* Test cases
    * Testing connection of Clients
* Add more...    

## Notes:
* Message passing will be done by standard Java I/O 
    * Utilizes SimpleJSON for encoding and decoding information
* All new and current games are stored in HashMap in [Games](https://github.com/Tonkovich/Casino-Games/blob/master/Server/src/Games.java)    
    * To act upon a game data we get the game in the game hashmap, 
    then use methods in a specific [game model](https://github.com/Tonkovich/Casino-Games/tree/master/Server/src/GameModels)
* The database will store the players money for offline purposes
    * Players bank will be updated upon completing a game
    * Used for getting players bank upon connecting to server
    
## Ideas:
* Client can create account and default cash of $500
* GUI for client
    * Buttons for betting, card display, game creation, game joining, and etc..
    
## Authors:
* Aaron Tonkovich
* Joshua Potrawski


    
    