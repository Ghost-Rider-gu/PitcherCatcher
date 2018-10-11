# PitcherCatcher
*An example of ping application*

----------

**How to build it:**

Using maven - **`mvn package`**

----------

**How to run it:**

On computer A (the pitcher)

> java -jar PitcherCatcher.jar -p -port 'port number' -mps 'messages per second' -size 'message length' -host 'hostname'

***Example:** java -jar PitcherCatcher.jar -p -port 8888 -mps 30 -size 1500 -host ComputerB*

![pitcher](https://user-images.githubusercontent.com/1051058/46826207-16739300-cd9e-11e8-895c-461698d54a12.png)

On computer B (the catcher)
> java -jar PitcherCatcher.jar -c -bind 'address' -port 'port number'

***Example:** java -jar PitcherCatcher.jar -c -bind 192.168.0.1 -port 8888*

![catcher](https://user-images.githubusercontent.com/1051058/46826167-fcd24b80-cd9d-11e8-8204-c9095b28d89c.png)

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
`-host <hostname>` | *(Pitcher) the name of the computer which runs Catcher*