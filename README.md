# Lorem Ipsum [![CircleCI](https://circleci.com/gh/Brookke/Lorem-Ipsum.svg?style=svg&circle-token=88a08119c648799c8c1241249131701e1298e614)](https://circleci.com/gh/Brookke/Lorem-Ipsum)
Visit out website at http://www.lihq.me, or read the documentation at http://docs3.lihq.me

MIRCH - Murder In Ron Cooke is a top down 2D dynamically generated point and click RPG murder mystery game. An evening swarve has been taking place at the Ron Cooke Hub, and a guest is dead. You have been brought in to determine exactly what has occurred.

## How to Play The Game

To play the game, download the executable .jar file from the assessment 3 part of the webpage - http://www.lihq.me .
Remember that the .jar file AND the database db.db file need to be in the same directory.

## Editing the game

### What you need
Before you can start working on the game you need to ensure you have Java installed and the latest JDK, you can get java here:

https://java.com/en/download/

and the JDK here:

http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

You also need the Intellij IDEA IDE, you can get this from here:

https://www.jetbrains.com/idea/#chooseYourEdition

The free version is good enough although the paid version is obtainable for free for students.

### Languages and Libraries

The game has been written in Java using the libGdx 1.9.6 library for graphics drawing.

### Import the project
To import the project:
1. Clone the GitHub repository on to your local documents.
2. Open up IntelliJ and select import project, then find where you cloned the project to and select the build.gradle file in the game folder. Click ok.
3. Next uncheck 'Create separate module per source set', click ok again.
4. IntelliJ may ask you to set up the JDK, if it does this simply navigate to where the JDK is on your computer.

It will then import the game, this may take some time.

One common problem is to do with a missing JDK, This is a solution :
Configure -> Project Defaults -> Project Structure then add your JDK in Platform Settings -> SDKs. Some other problems have solutions [here](https://github.com/libgdx/libgdx/wiki/Gradle-and-Intellij-IDEA).

### Edit the game
You can now edit the game, we recommend making a new branch, then make changes on that branch. You can use GitHub desktop or equivalent to commit the changes to your branch and then use sync to upload those changes. When you are ready submit a pull request and have someone check it.

### Run the game
You can run by first building as described below and then simply clicking run.

### Building the project
To build the game use the built in run configuration *Desktop* in the same way that you run tests.

## Testing
This project is tested using JUnit. Tests are located within the `/game/src/tests` directory. For test documentation, please see https://github.com/junit-team/junit4/wiki

### Running Tests
For every commit CircleCI runs all the included tests, however we recomend that you run test locally too before committing.

### Adding Tests
- Create new class for tests under `/game/src/tests` When naming the class end the name with `_Test` for consistency e.g. `Player_Test`
- This class should extend `GameTester` this initialises the backend of the game so that test run correctly.
- Import `org.junit.Test`
- Write a test function using assertions, and use `@Test` decorator above it
- See this page for examples of assertions: https://github.com/junit-team/junit4/wiki/assertions
- Run your tests locally and see if they pass!

### CircleCI Test Results
After tests have run the results are displayed in the "Test Summary" tab on CircleCI.

If the tests have failed and no test summary is provided, this normally means that the code doesn't compile, or there is a problem with the test code. To gather more information, scroll down to read the console output from when the tests were run.
