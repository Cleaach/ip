# Use of AI tools

I did not use AI until the very end of the iP, where I used Cursor to help me check for discrepancies and shortcomings in my code with respect to common coding standards, error handling and SLAP. 

Cursor helped me to shorten my methods by applying SLAP (particularly in moving most of the parsing logic from src/main/java/udin/Udin.java to src/main/java/udin/Parser.java) and removing duplicate blocks of code. Moreover, Cursor helped me generate more JUnit tests and assertions, since I started out with only a handful of them when I was writing them myself. And, finally, it also helped me resolve an error where the .txt storage file could not be detected if I ran Udin via the JAR file.

It was not perfect though; occasionally, `./gradlew run` did not build successfully after one or two prompts due to a small error which I had to trace back and debug myself. However, Cursor mostly improved my productivity by a large margin when doing humdrum work.