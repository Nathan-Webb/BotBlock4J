[BotBlock]: https://botblock.org
[api]: https://botblock.org/api/docs

[BotBlockAPI]: https://github.com/Nathan-Webb/BotBlock4J/blob/master/src/main/java/com/andre601/botblock4j/BotBlockAPI.java

# BotBlock4J

API wrapper for [BotBlock], a site that removes the hassle from managing multiple Discord bot list APIs.  
You can view the full list of Discord bot list APIs that BotBlock, and by extension this wrapper, supports on the [BotBlock API docs][api].

This Wrapper is now continued and updated by Andre601 after it was created and maintained by Nathan-Webb

## Install
You have two options for implementing the API.

### Gradle
```gradle
repositories {
    maven { url = "https://jitpack.io" }
}

dependencies {
 compile group: 'com.github.andre601', name: 'BotBlock4J', version: '2.0.0'
}
```

### Maven
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.andre601</groupId>
  <artifactId>BotBlock4J</artifactId>
  <version>2.0.0</version>
</dependency>
```

## Usage
Here are some examples of the Wrapper in action.  
Note that I use an instance of JDA (`jda`) as an example here, but you could also use an instance of `ShardManager`.

### Creating an instance of BotBlockAPI
You first have to create a new Instance of the class [`BotBlockAPI`][BotBlockAPI].  
This will be used later.

You can create an instance by using `BotBlockAPI.Builder()` like this:  
```java
/*
 * We create a new instance of BotBlockAPI by using BotBlockAPI.Builder here.
 *
 * Note that you need to use BotBlockAPI.Builder()#disableJDARequirement() when not providing a JDA instance.
 */
BotBlockAPI api = new BotBlockAPI.Builder()
    .addAuthToken("lbots.org", "MyS3cretT0k3n")
    .setJDA(jda)
    .build();
```

#### BotBlockAPI.Builder methods
The Builder comes with a lot of different methods that you can use.

##### addAuthToken
> **Requires**: `String, String`

This method is required!  
Through this method can you set a Site (First String) and the corresponding token (Second String) for posting the Guilds.

##### setAuthTokens
> **Requires**: `Map<String, String>`

This method directly sets the Map with the informations.  
It will override any entry that was made with [`addAuthToken(String, String)`](#addAuthToken)

##### setAutoPost
> **Requires**: `Boolean`

Set if the Wrapper should start a Executor for auto-posting stats every x minutes. Default is `false`.  
You need to call `RequestHandler#startAutoPost(BotBlockAPI)` after that to start auto-posting.

##### setUpdateInterval
> **Requires**: `Integer`

Set the delay in minutes in which the guilds should be posted. Default is `30`.  
The method throws an `IllegalArgumentException` when the provided Integer is less than 1.  
This is only needed when you want to auto-post the Guilds.

##### disableJDARequirement
> **Requires**: `Boolean`

Set if an instance of JDA or ShardManager are required. True means it's *NOT* required. Default is `false`.

##### setJDA
> **Requires**: `JDA instance`

Sets the JDA instance that should be used.  
This method is required when [`disableJDARequirement(Boolean)`](#disableJDARequirement) is false and [`setShardManager(ShardManager)`](#setShardManager) isn't used.  
This will set `disableJDARequirement(Boolean)` to false when set.  
The JDA instance will be ignored on auto-posting when `setShardManager(ShardManager)` is used.

##### setShardManager
> **Requires**: `ShardManager instance`

Sets the ShardManager that should be used.  
This method is required when [`disableJDARequirement(Boolean)`](#disableJDARequirement) is false and [`setJDA(JDA)`](#setJDA) isn't used.  
This will set `disableJDARequirement(Boolean)` to false when set.  
The ShardManager will be prioritized over JDA meaning that if you set both, the shardManager will be used on auto-post.

##### build
> **Requires**: `-`

Builds the actual BotBlockAPI instance with the previously set values.

This will throw an `IllegalStateException` when both JDA and ShardManager aren't set AND [`disableJDARequirement(Boolean)`](#disableJDARequirement) isn't used.

### Post Guilds
You have multiple ways available to post Guild counts.

#### Auto-posting
This lets you post the guilds each X minutes, without the need of setting up an own Scheduler.  
The delay will be defined through [`setUpdateInterval(Integer)`](#setUpdateInterval) and is by default 30 minutes.

**Auto-Posting can only be done with an instance of JDA or ShardManager set!**  
```java
/*
 * Creating an instance of RequestHandler first.
 */
RequestHandler handler = new RequestHandler();

/*
 * RequestHandler#startAutoPost requires an instance of BotBlockAPI which we made above
 */
handler.startAutoPost(api);

/*
 * To stop the autoposting do we call RequestHandler#stopAutoPost();
 */
handler.stopAutoPost();
```

#### Manual posting
You can also manually post Guilds if you want.  
There are four different methods of posting Guilds. 2 for posting with JDA or ShardManager and 2 for without them.  
```java
/*
 * Creating an instance of RequestHandler first.
 */
RequestHandler handler = new RequestHandler();

/*
 * Using JDA.
 */
handler.postGuilds(jda, api);

/*
 * Using ShardManager.
 */
handler.postGuilds(shardManager, api);

/*
 * Providing bot id (String) and guilds (Integer) separate.
 */
handler.postGuilds("1234567890", 100, api);

/*
 * Providing bot id (Long) and guilds (Integer) separate.
 */
handler.postGuilds(1234567890L, 100, api);
```

The methods above can throw the following Exceptions:
- `IOException`  
When the connection was closed, cancelled or malformed in a way (e.g. receiving an empty ResponseBody)
- `RatelimitedException`  
Thrown when the Bot (ID and/or IP) got ratelimited by BotBlock (Site returned code 429)

## Links
- [BotBlock Website][BotBlock]
- [API Docs][api]
