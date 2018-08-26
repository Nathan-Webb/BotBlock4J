# BotBlock4J

##### Api wrapper for `https://botblock.org`, a site that removes the hassle from managing multiple Discord List APIs 


## Download

You have two options for implementing the API. 


##### Gradle:

```text
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Nathan-Webb:BotBlock4J:master'
}
```


##### Maven:

```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

```xml
	<dependency>
	    <groupId>com.github.Nathan-Webb</groupId>
	    <artifactId>BotBlock4J</artifactId>
	    <version>master</version>
	</dependency>
```


##### Other:

Alternatively, you could just head over to `https://bintray.com/nathan-webb/maven/BotBlock4J` 
and download the jar file yourself.


## Usage

##### Note: These snippets can also be used with an instance of `ShardManager` instead of a `JDA` instance.

#### "Set it and forget it."
```java
BlockAuth auth = new BlockAuth();
auth.setListAuthToken(BotList.BOTS_FOR_DISCORD, "bfd-token-here");
//Add other lists as needed.
BotBlockAPI api = new BotBlockAPI(jda, true, auth); //This starts a 30 minute timer that send guild counts every 30 minutes.
api.setUpdateInterval(20); //if you wish to change the interval, you can do so with this method.
//...
api.stopSendingGuildCounts(); //stops the timer
//...
api.startSendingGuildCounts(); //starts it again
```


#### Send the Guild count manually.
```java
BlockAuth auth = new BlockAuth();
auth.setListAuthToken(BotList.BOTS_FOR_DISCORD, "bfd-token-here");
//...
BotBlockRequests.postGuildsJDA(jda, blockAuth);
```

#### Send guild count without an instance.
```java
BlockAuth auth = new BlockAuth();
auth.setListAuthToken(BotList.BOTS_FOR_DISCORD, "bfd-token-here");
//...
BotBlockRequests.postGuilds("bot-id-here", 100, auth);
```



