# play-streaming-stock-scala
This is a simple application for watching a stock quote in real time. Scala, Play, and Akka-stream are used to implement this application and sbt helps to easily run my program. Yahoo Finance API is used to feed real time stock prices. http://financequotes-api.com


1. Download the template:

        git clone https://github.com/yest89/yahoo_stocks_scala.git
        
     Or, if you don't have git installed, download and unzip the files manually from [here](https://github.com/yest89/yahoo_stocks_scala/archive/master.zip).
       
2. To run your program, in SBT:

        cd play-streaming-stock-scala
        sbt run

3. Interact with UI:
   
       Go to http://localhost:9000/ 
   
   It has a really simple ui. A user can add symbol with the input text field and the add button. Once a user click the button, the stock quote card will be added.
   To delete the card, just click the Remove button.   
   Finally, the real time data is available during trading hours.

![Image description](https://github.com/yest89/yahoo_stocks_scala/blob/main/public/images/image.png)
