# Casino Games :moneybag:
In this distributed system, users have the ability to connect to any active casino server and search for active games.
Users can then join and play against other users (up to 4 players) for in game currency that is backed up to a database. 

Currently there is only Texas Hold'em but in the future there will be more multiplayer games and singleplayer games
such as slots. 

## Server
* Can tell whether or not a player is connected using a heartbeat.
* Stores and manages a thread pool of all active games to ensure multi-player.
* Uses a MySQL database for all player information (username, password, wallet[$])
* Parses/Uses JSON data for its messaging system between clients.
* Uses network port 12000 for DatagramSocket(UDP) messages.
* Uses local algorithm for determining winner of games.

## Client
* Modified GUI inspired by [Pathoschild's JPoker](https://github.com/Pathoschild/JPoker)
* Users can join, create, and leave games.
* Can register on the server in client.

## :bug: Bugs :bug:
* Graphical issues where user exits game and is met with black screen (just type 'r')
* Users can sometimes not create new games on exiting games

## :star2: Upcoming... :star2:
* Web-page for all active servers
* Add more server configurable options to config
* Combat firewall issues with certain machines
* Create real GUI using Java graphics
* Write more test cases (really)
* Implement timers for limit of user decisions
    
## Authors:
* Aaron Tonkovich
* Joshua Potrawski


    
    