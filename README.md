# BotBlock4J
[ ![Download](https://api.bintray.com/packages/nathan-webb/maven/BotBlock4J/images/download.svg) ](https://bintray.com/nathan-webb/maven/BotBlock4J/_latestVersion)

#### API wrapper for [BotBlock](https://botblock.org), a site that removes the hassle from managing multiple Discord bot list APIs. You can view the full list of Discord bot list APIs that BotBlock, and by extension this wrapper, supports on the [BotBlock API docs](https://botblock.org/api/docs#count).

##### The first argument passed into `BlockAuth.setListAuthToken()` should be the site ID from the [BotBlock docs](https://botblock.org/api/docs#count).

## Download

You have three options for implementing the API.

##### Gradle:


```text
repositories {
    jcenter()
}

dependencies {
 compile 'com.nathanwebb:BotBlock4J:LATEST-VERSION-HERE'
}
```

##### Maven:

```xml
<dependency>
  <groupId>com.nathanwebb</groupId>
  <artifactId>BotBlock4J</artifactId>
  <version>LATEST-VERSION-HERE</version>
  <type>pom</type>
</dependency>
```

##### Ivy:

```text
<dependency org='com.nathanwebb' name='BotBlock4J' rev='LATEST-VERSION-HERE'>
  <artifact name='BotBlock4J' ext='pom' ></artifact>
</dependency>
```

##### Other:

Alternatively, you could just head over to `https://bintray.com/nathan-webb/maven/BotBlock4J/_latestVersion`
and download the jar file yourself.

## Usage

##### Note: These snippets can also be used with an instance of `ShardManager` instead of a `JDA` instance.

#### "Set it and forget it."
```java
BlockAuth auth = new BlockAuth();
auth.setListAuthToken("botsfordiscord.com", "token-here");
//Add other lists as needed.
BotBlockAPI api = new BotBlockAPI(jda, false, auth); //This starts a timer that sends guild counts every 30 minutes.
api.setUpdateInterval(20); //if you wish to change the interval, you must first provide a false in the method above can do so with this method.
//...
api.stopSendingGuildCounts(); //stops the timer

//...

api.startSendingGuildCounts(); //starts it again
```

#### Send the Guild count manually.
```java
BlockAuth auth = new BlockAuth();
auth.setListAuthToken("botsfordiscord.com", "token-here");
//...
BotBlockRequests.postGuildsJDA(jda, blockAuth);
```

#### Send guild count without an instance.
```java
BlockAuth auth = new BlockAuth();
auth.setListAuthToken("botsfordiscord.com", "token-here");
//...
BotBlockRequests.postGuilds("bot-id-here", 100, auth);
```
