# TeamFarce-MIRCH Game

##Introduction - Murder In Ron Cooke

MIRCH - Murder In Ron Cooke is a top down 2D dynamically generated point and click RPG murder mystery game. An evening swarve has been taking place at the Ron Cooke Hub, and a guest is dead. You have been brought in to determine exactly what has occurred.

##How to Play The Game

To play the game, download the executable .jar file from the executables page of the project webpage - https://teamfarce.github.io/MIRCH/.

##Languages and Libraries

The game has been written in Java using the libGdx 1.9.6 library for graphics drawing.

The website generator makes use of a Python 3 script and the Jinja2 module.

##How to edit and run the game from Eclipse.

First, fork the MIRCH repository from here and clone it to your computer. Open a terminal and cd into the MIRCH/game directory. Then run the command 'gradle build eclipse' - you will need to have gradle installed on your system for this to be successful. Now start eclipse (minimum version eclipse Neon) and select open project from directory. Open the MIRCH/game directory. You can now edit project files and run the program.

##Code layout and seperation of concerns

The program architecture is separated into two distinct areas - the representational front end module and logical back end module. The representational module controls drawing graphics to the game window and receiving/validating user inputs. Data is passed to the logical module for processing, and renderable objects are returned. The logical module is responsible the game generation and data storage. This seperation of concerns allows for a simplified more Agile development process and also eases the greater extensability and scalability of the game.

An extensive Object Orientated approach has been utilised throughout all modules to ensure the maintainability of structure and data integrity.

A specialised content management system provides for simplistic automated generation and maintainability of the website, disentangling the complexities of code documentation maintenance and assisting in the accountability and reporting of the project as a whole.

##Tech Support

Whilst the program is well documented throughout, we understand that it is sometimes easier to just ask someone for help than trawl through documentation. We are therefore available to offer unlimited help and for any aspect of the program, based on the area that each of us created.

* Scenario Generation, Database and Website - Peter Smith : ps1011@york.ac.uk
* Front End Graphics Generation and Input Processing - Jacob Unwin : jwu500@york.ac.uk
* Dialogue Tree and Related Systems - Khurram Liaqat : kl985@york.ac.uk
