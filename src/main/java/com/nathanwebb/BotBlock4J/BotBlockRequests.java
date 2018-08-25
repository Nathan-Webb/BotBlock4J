/*
 * Copyright 2018 Nathan Webb (nathanwgithub@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.nathanwebb.BotBlock4J;

import com.nathanwebb.BotBlock4J.exceptions.EmptyResponseException;
import com.nathanwebb.BotBlock4J.exceptions.FailedToSendException;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Contains all the methods for interacting with the BotBlock API.
 */
public class BotBlockRequests {
    private static String baseURL = "https://botblock.org/api/";

    /**
     * Posts the guild total given a Shard Manager. The User Agent will be the bot's name.
     * @param shardManager Manager used when sharding.
     * @param auth BlockAuth object with all necessary values.
     * @throws FailedToSendException If one or more lists returned errors when posting.
     * @throws EmptyResponseException If BotBlock api does something funny and returns an empty JSON body.
     * @throws IOException If the connection drops/is cancelled.
     */
    public static void postGuildsShardManager(ShardManager shardManager, BlockAuth auth) throws FailedToSendException, EmptyResponseException, IOException{
        String url = baseURL + "count";

        JSONObject data = new JSONObject();
        data.put("server_count", shardManager.getGuilds().size());
        data.put("bot_id", shardManager.getShardById(0).getSelfUser().getId());
        data.put("shard_count", shardManager.getShardsTotal());

        ArrayList<Integer> shardGuildCounts = new ArrayList<>();
        for(JDA jdaShard : shardManager.getShards()){
            shardGuildCounts.add(jdaShard.getGuilds().size());
        }
        data.put("shards", Arrays.deepToString(shardGuildCounts.toArray()));


        HashMap<BotList, String> authHashMap = auth.getAuthHashMap();
        authHashMap.forEach((botList, authToken) -> data.put(botList.toString(), authToken));

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), data.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("User-Agent", shardManager.getShardById(0).getSelfUser().getName())
                .build();
        postGuildRequest(request);
    }

    /**
     * Posts the guild total given a JDA instance. If the instance is one that is sharding,
     * it will send the current shard ID as well as the total number of shards. The User Agent will be the bot's name.
     * @param jda Instance that is used with the discord API.
     * @param auth BlockAuth object with all necessary values.
     * @throws FailedToSendException If one or more lists returned errors when posting.
     * @throws EmptyResponseException If BotBlock api does something funny and returns an empty JSON body.
     * @throws IOException If the connection drops/is cancelled.
     */
    public static void postGuildsJDA(JDA jda, BlockAuth auth) throws FailedToSendException, EmptyResponseException, IOException{
        String url = baseURL + "count";

        JSONObject data = new JSONObject();

        data.put("server_count", jda.getGuilds().size());
        data.put("bot_id", jda.getSelfUser().getId());
        if(jda.getShardInfo() != null){
            data.put("shard_id", jda.getShardInfo().getShardId());
            data.put("shard_count", jda.getShardInfo().getShardTotal());
        }
        HashMap<BotList, String> authHashMap = auth.getAuthHashMap();
        authHashMap.forEach((botList, authToken) -> data.put(botList.toString(), authToken));

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), data.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("User-Agent", jda.getSelfUser().getName())
                .build();
        postGuildRequest(request);
    }

    /**
     * Posts to the BotBlock API given a String User ID and integer representing the # of servers.
     * The User Agent is {@code Discord Bot (user-id)}
     * @param botId The ID of the bot you want to post data for.
     * @param servers Total amount of servers this bot is in.
     * @param auth BlockAuth object with all necessary values.
     * @throws FailedToSendException If one or more lists returned errors when posting.
     * @throws EmptyResponseException If BotBlock api does something funny and returns an empty JSON body.
     * @throws IOException If the connection drops/is cancelled.
     */
    public static void postGuilds(String botId, int servers, BlockAuth auth) throws FailedToSendException, EmptyResponseException, IOException{
        String url = baseURL + "count";

        JSONObject data = new JSONObject();

        data.put("server_count", servers);
        data.put("bot_id", botId);
        HashMap<BotList, String> authHashMap = auth.getAuthHashMap();
        authHashMap.forEach((botList, authToken) -> data.put(botList.toString(), authToken));

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), data.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("User-Agent", "Discord Bot (" + botId + ")")
                .build();
        postGuildRequest(request);
    }

    /**
     * Posts to the BotBlock API given a long User ID and integer representing the # of servers.
     * The User-Agent is {@code Discord Bot (user-id)}
     * @param botId The ID of the bot you want to post data for.
     * @param servers Total amount of servers this bot is in.
     * @param auth BlockAuth object with all necessary values.
     * @throws FailedToSendException If one or more lists returned errors when posting.
     * @throws EmptyResponseException If BotBlock api does something funny and returns an empty JSON body.
     * @throws IOException If the connection drops/is cancelled.
     */
    public static void postGuilds(long botId, int servers, BlockAuth auth) throws FailedToSendException, EmptyResponseException, IOException{
        String url = baseURL + "count";

        JSONObject data = new JSONObject();

        data.put("server_count", servers);
        data.put("bot_id", Long.toString(botId));
        HashMap<BotList, String> authHashMap = auth.getAuthHashMap();
        authHashMap.forEach((botList, authToken) -> data.put(botList.toString(), authToken));

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), data.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("User-Agent", "Discord Bot (" + Long.toString(botId) + ")")
                .build();
        postGuildRequest(request);
    }

    /**
     * This is just some boilerplate code to post the guild count given a {@code Request.}
     * @param request To be executed.
     * @throws FailedToSendException If one or more lists returned errors when posting.
     * @throws EmptyResponseException If BotBlock api does something funny and returns an empty JSON body.
     * @throws IOException If the connection drops/is cancelled.
     */
    private static void postGuildRequest(Request request) throws FailedToSendException, EmptyResponseException, IOException{
        Response response = new OkHttpClient().newCall(request).execute();
        response.close();
        ResponseBody responseBody = response.body();

        //check to make sure we actually got a response
        if(responseBody != null) {
            JSONObject responseObject = new JSONObject(responseBody.string());
            JSONObject failures = responseObject.getJSONObject("failure");

            if(!failures.isEmpty()){
                List<String> botLists = new ArrayList<>();
                for(String listFailureKey : failures.keySet()){
                    JSONArray failedListArray = failures.getJSONArray(listFailureKey);
                    botLists.add("List name: " + listFailureKey + " Error Code: " + failedListArray.getInt(0) + " Error Message: " + failedListArray.getString(1));
                }
                responseBody.close();
                throw new FailedToSendException(botLists);
            }
            responseBody.close();
        } else {
            throw new EmptyResponseException("Error when sending a request to BotBlock!");
        }
    }
}
