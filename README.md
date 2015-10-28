# ProxyTester
A simple Java program to test http proxies!

## Works with HTTP proxies, not HTTPS or Socks.

### Usage
`java -jar proxytester.jar`

### Format
##### proxies.txt
```
ip:port
1.1.1.1:80
```

working.txt is also in this format.

### Possible issues
- Could not be able to write to the files.
- Timeout is set to 100ms, which could not be long enough.
- No configuration file.
