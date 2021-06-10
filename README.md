# Starboard
A text-based console game written in JAVA. A player starts from the bridge in the starship and needs to find a way to the pod to escape from the alien chase.   

**Authors**: Wenhao, Yichun, Julian

## How to start
1. Clone the repo
2. Add dependencies
    - jackson-core: 2.7.4
    - jackson-databind: 2.7.4
    
3. Build the project and run it.

## V1.0.0 features - 06/07/2021
1. show status - show a player where he/she is, what items in the room, what items he/she has, and where he/she can go
2. show commands - show a player commands he/she can use
3. show inventory - show a player items he/she has
4. traver rooms - a player can traverse rooms to find their destination
5. user inputs validation - commands are case-insensitive and handle invalid user inputs
6. support synonyms words for action verbs

## V1.0.1 features - 06/10/2021     
1. battle mode
   - player can use weapons
   - player can use healing items
   - player can randomly escape from battle
   - aliens randomly show up in the room
2. players can pickup items from the containers in the room and store it in player's inventory
3. players can drop items to the containers in the room
4. user inputs validation v2 - supports more commands

