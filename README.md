# PitcherCatcher
*An example of ping application*

----------

**How to build it:**

Using maven - **`mvn package`**

----------

**How to run it:**

On computer A (the pitcher)

> java -jar PitcherCatcher.jar -p -port 'port number' -mps 'messages per second' -size 'message length' 'hostname'

***Example:** java -jar PitcherCatcher.jar -p -port 8888 -mps 30 -size 1500 ComputerB*

On computer B (the catcher)
> java -jar PitcherCatcher.jar -c -bind 'address' -port 'port number'

***Example:** java -jar PitcherCatcher.jar -c -bind ComputerA -port 8888*

----------

**Convention**

Parameter | Description
:---: | :---:
`-p` | *Pitcher mode*
`-c` | *Catcher mode*
`-port <port>` | *(Pitcher) TCP socket port used for connecting <br/> (Catcher) TCP socket port used for listening*
`-bind <ip_address>` | *(Catcher) TCP socket bind address that will be used to run listen*
`-mps <rate>` | *(Pitcher) the speed of message sending expressed as 'messages per second' (default: 1)*
`-size <size>` | *(Pitcher) message length (minimum: 50, maximum: 3000, default: 300)*
`<hostname>` | *(Pitcher) the name of the computer which runs Catcher*