[badge]: https://api.bintray.com/packages/nathan-webb/maven/BotBlock4J/images/download.svg
[download]: https://bintray.com/nathan-webb/maven/BotBlock4J/_latestVersion

[BotBlock]: https://botblock.org
[api]: https://botblock.org/api/docs#count

[BlockAuth]: https://github.com/Nathan-Webb/BotBlock4J/blob/master/src/main/java/com/nathanwebb/BotBlock4J/BlockAuth.java

# BotBlock4J
[![badge]][download]

API wrapper for [BotBlock], a site that removes the hassle from managing multiple Discord bot list APIs.  
You can view the full list of Discord bot list APIs that BotBlock, and by extension this wrapper, supports on the [BotBlock API docs][api].

## Download
You have three options for implementing the API.

### Gradle
```gradle
repositories {
    jcenter()
}

dependencies {
 compile group: 'com.nathanwebb', name: 'BotBlock4J', version: 'LATEST-VERSION-HERE'
}
```

### Maven
```xml
<dependency>
  <groupId>com.nathanwebb</groupId>
  <artifactId>BotBlock4J</artifactId>
  <version>LATEST-VERSION-HERE</version>
  <type>pom</type>
</dependency>
```

### Ivy

```ivy
<dependency org='com.nathanwebb' name='BotBlock4J' rev='LATEST-VERSION-HERE'>
  <artifact name='BotBlock4J' ext='pom' ></artifact>
</dependency>
```

##### Other:

Alternatively, you could just head over to the [Bintray Site][download] and download the jar file yourself.

## Usage
**Note**: You can also provide an instance of `ShardManager` instead of `JDA`

### Creating a BlockAuth instance
We have to create a [BlockAuth] instance to provide it later for the post requests.

#### Using the Builder (Recommended)
You can use the BlockAuth.Builder() to create an instance easy.  
```java
/*
 * We can use BlockAuth.Builder() to add sites to the HashMap of lists
 *
 * You can use addListAuthToken(String, String) or addListAuthToken(Site, String)
 * and add as many as you like.
 *
 * Don't forget to call .build(); at the end to build the BlockAuth instance
 */
BlockAuth auth = new BlockAuth.Builder()
    .addListAuthToken("botsfordiscord.com", "MyS3cr3tT0k3n")
    .addListAuthToken(Site.LBOTS_ORG, "My0t3rS3cr3tT0k3n")
    .build();
```

#### Set manually
If you want to set it manually you can just use `setListAuthToken(String, String)` directly.  
```java
BlockAuth auth = new BlockAuth();

// We have to call this method for each list.
auth.setListAuthToken("botsfordiscord.com", "MyS3cr3tT0k3n");

// We can also use Site.SITENAME for convenience.
auth.setListAuthToken(Site.LBOTS_ORG, "My0t3rS3cr3tT0k3n");
```

### Posting stats
There are three different types of methods you can use, depending on your preferences.  
All methods require you to have the [BlockAuth instance](#creating-a-blockauth-instance) set up.

All shown examples use an BlockAuth called `auth` and a JDA instance called `jda` or ShardManager called `shardManager`.

#### Post every X minutes
You can let BotBlock4J post stats automatically every X minutes.  
```java
/*
 * Create an instance of BotBlockAPI.
 *
 * Default update interval is 30 minutes.
 */
BotBlockAPI api = new BotBlockAPI(jda, false, auth);

// Use this method to change the interval. This needs to be called before startSendingGuildCounts()
api.setUpdateInterval(10);

// Call this to start posting of the guild count.
api.startSendingGuildCounts();

// Call this to stop sending of the guild count. You can use startSendingGuildCounts() to start it again.
api.stopSendingGuildCounts();
```

#### Manually
If you want to send the guild count manually you can use one of those methods.  
```java
// Posts the guild count of the provided JDA instance
BotBlockRequests.postGuildsJDA(jda, auth);

// Posts the guild count of the provided ShardManager.
BotBlockRequests.postGuildsShardManager(shardManager, auth);
```

#### Sending without an instance
If you don't want to provide a JDA or ShardManager instance you can use one of those methods here:  
```java
int guilds = 100;

// Posts the guild count with the provided Bot id as String.
BotBlockRequests.postGuilds("yourBotId", guilds, auth);

// Posts the guild count with the provided Bot id as Long.
BotBlockRequests.postGuilds(1234567890L, guilds, auth);
```

### Errors
The above methods can throw those methods:  
- `FailedToSendException`  
Thrown when one or more sites returned errors while posting. 
- `EmptyResponseException`  
When the BotBlockAPI returns an empty JSON body.
- `RateLimitException`  
When the Request was ratelimited.
- `IOException`  
When the connection drops/was cancelled.
