# Snake

The classic snake game realized with Java Swing.

The game is built in MVC extended by an additional highscore handling database.

## Prerequisites

The applied technologies are the following:
- Java Version 8 Update 321
- Apache NetBeans IDE 12.6
- Apache Derby 10.14.2.0
- UMLet version 14.3.0

To use the local database feature:
1. Donwload Apache Derby.
2. In Apache NetBeans, select the Services page.
3. Right click on Databases and select New Connection.
4. At Driver select Java DB (Embedded) and add derby.jar.
   `<absolute_path_of_downloaded_apache_derby_folder>\db-derby-10.14.2.0-lib\lib\derby.jar`
5. Right click Java DB and select Create Database.
6. Create the database with the following attributes:
   | Attribute        | Value           |
   | ---------------- | --------------- |
   | Database Name    | snake_highscore |
   | User Name        | root            |
   | Password         | root            |
   | Confirm Password | root            |

Before running the project, start the Java DB server:
1. Select the Services page.
2. Right click on Java DB and select Start Server.

## Play the game

The game can be started by pressing the Space button. The snake can be controlled by the arrow keys or WASD.
The goal is to pick up as many apples as possible before the inevitable death of the snake.
The snake dies if it hits the border of the level, a rock, or itself.
Upon death the player can insert a name and the score is inserted into the highscores.
From the menu the player can start a new game, watch the highscores or the initial information or exit the game.

The following hotkeys are available:
Ctrl-N : new game
Ctrl-H: help
Ctrl-S: Highscores
Ctrl-E: Exit
