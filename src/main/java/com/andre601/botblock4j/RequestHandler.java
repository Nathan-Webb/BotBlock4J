package com.andre601.botblock4j;

import com.andre601.botblock4j.exceptions.RatelimitedException;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import okhttp3.*;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class RequestHandler {
    private final String POST_URL = "https://botblock.org/api/count";

    private final OkHttpClient CLIENT = new OkHttpClient();
    private final MediaType JSON = MediaType.get("application/json");

    /**
     * Posts the Guilds from the provided {@link net.dv8tion.jda.core.JDA JDA instance} to the BotBlock-API.
     * <br>If the provided JDA is sharded (Has ShardInfo) then the shards ID and the total shard count will be posted too.
     *
     * @param jda
     *        The {@link net.dv8tion.jda.core.JDA JDA instance} to use.
     * @param botBlockAPI
     *        An instance of {@link com.andre601.botblock4j.BotBlockAPI BotBlockAPI}.
     *        <br>{@link com.andre601.botblock4j.BotBlockAPI.Builder BotBlockAPI.Builder} can be used to create an instance.
     */
    public void postGuilds(JDA jda, BotBlockAPI botBlockAPI) throws IllegalAccessException, IOException, RatelimitedException{
        if(!ObjectUtils.allNotNull(jda))
            throw new IllegalAccessException("JDA may not be null!");

        JSONObject json = new JSONObject()
                .put("server_count", jda.getGuildCache().size())
                .put("bot_id", jda.getSelfUser().getId());

        if(jda.getShardInfo() != null)
            json.put("shard_id", jda.getShardInfo().getShardId())
                    .put("shard_count", jda.getShardInfo().getShardTotal());

        botBlockAPI.getAuthTokens().forEach(json::put);

        performRequest(json);
    }

    /**
     * Posts the Guilds from the provided {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager} to the BotBlock-API.
     *
     * @param shardManager
     *        The {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager} to use.
     * @param botBlockAPI
     *        An instance of {@link com.andre601.botblock4j.BotBlockAPI BotBlockAPI}.
     *        <br>{@link com.andre601.botblock4j.BotBlockAPI.Builder BotBlockAPI.Builder} can be used to create an instance.
     */
    public void postGuilds(ShardManager shardManager, BotBlockAPI botBlockAPI) throws IllegalAccessException, IOException, RatelimitedException{
        if(!ObjectUtils.allNotNull(shardManager))
            throw new IllegalAccessException("ShardManager may not be null!");

        JSONObject json = new JSONObject()
                .put("server_count", shardManager.getGuildCache().size())
                .put("bot_id", shardManager.getShardById(0).getSelfUser().getId())
                .put("shard_count", shardManager.getShardsTotal());

        List<Integer> shards = new ArrayList<>();
        for(JDA jda : shardManager.getShards())
            shards.add((int)jda.getGuildCache().size());

        json.put("shards", new JSONArray(Arrays.deepToString(shards.toArray())));

        botBlockAPI.getAuthTokens().forEach(json::put);

        performRequest(json);
    }

    /**
     * Posts the provided Guilds for the provided Bot ID to the BotBlock-API.
     * <br>Consider using {@link #postGuilds(JDA, BotBlockAPI)} or {@link #postGuilds(ShardManager, BotBlockAPI)} instead.
     *
     * @param botId
     *        The ID of the bot.
     * @param guilds
     *        The guilds to post.
     * @param botBlockAPI
     *        An instance of {@link com.andre601.botblock4j.BotBlockAPI BotBlockAPI}.
     *        <br>{@link com.andre601.botblock4j.BotBlockAPI.Builder BotBlockAPI.Builder} can be used to create an instance.
     *
     * @see #postGuilds(String, int, BotBlockAPI) for the full method.
     */
    public void postGuilds(Long botId, int guilds, BotBlockAPI botBlockAPI) throws IOException, RatelimitedException{
        postGuilds(Long.toString(botId), guilds, botBlockAPI);
    }

    /**
     * Posts the provided Guilds for the provided Bot ID to the BotBlock-API.
     * <br>Consider using {@link #postGuilds(JDA, BotBlockAPI)} or {@link #postGuilds(ShardManager, BotBlockAPI)} instead.
     *
     * @param botId
     *        The ID of the bot.
     * @param guilds
     *        The guilds to post.
     * @param botBlockAPI
     *        An instance of {@link com.andre601.botblock4j.BotBlockAPI BotBlockAPI}.
     *        <br>{@link com.andre601.botblock4j.BotBlockAPI.Builder BotBlockAPI.Builder} can be used to create an instance.
     */
    public void postGuilds(String botId, int guilds, BotBlockAPI botBlockAPI) throws IOException, RatelimitedException{
        JSONObject json = new JSONObject()
                .put("server_count", guilds)
                .put("bot_id", botId);

        botBlockAPI.getAuthTokens().forEach(json::put);

        performRequest(json);
    }

    private void performRequest(JSONObject json) throws IOException, RatelimitedException{
        RequestBody requestBody = RequestBody.create(JSON, json.toString());

        Request request = new Request.Builder()
                .url(POST_URL)
                .addHeader("User-Agent", json.getString("bot_id"))
                .addHeader("Content-Type", "application/json") // Some sites require this in the header.
                .post(requestBody)
                .build();

        try(Response response = CLIENT.newCall(request).execute()){
            if(!response.isSuccessful()){
                if(response.body() == null)
                    throw new IOException("Received empty response-body from BotBlock.");

                if(response.code() == 429)
                    throw new RatelimitedException(response.body().string());

                throw new IOException("Couldn't post Guilds to BotBlock-API!");
            }

            if(response.body() == null)
                throw new IOException("Received empty response-body from BotBlock.");

            JSONObject responseJson = new JSONObject(response.body());
            if(!responseJson.get("failure").toString().equals("[]")){
                JSONObject failure = responseJson.getJSONObject("failure");
                List<String> sites = new ArrayList<>();

                for(String key : failure.keySet()){
                    try{
                        JSONArray failedList = failure.getJSONArray(key);
                        sites.add(String.format(
                                "Site name: %s, Error Code: %d, Error Message: %s",
                                key,
                                failedList.getInt(0),
                                failedList.getString(1)
                        ));
                    }catch(JSONException ex){
                        Map<String, Object> notFound = failure.toMap();
                        sites.add(String.format(
                                "Errors: %s",
                                notFound.toString()
                        ));
                    }
                }
                throw new IOException(String.format(
                        "One or multiple requests to post guild counts failed! Response: %s",
                        String.join(", ", sites)
                ));
            }
        }
    }

}
