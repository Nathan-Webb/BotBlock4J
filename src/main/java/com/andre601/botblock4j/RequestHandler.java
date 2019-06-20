package com.andre601.botblock4j;

import net.dv8tion.jda.core.JDA;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.util.Map;

public class RequestHandler {

    public void postGuilds(String botId, int guilds, Map<String, String> authTokens, @Nullable JDA.ShardInfo shardInfo){
        JSONObject json = new JSONObject()
                .put("server_count", guilds)
                .put("bot_id", botId);

        if(shardInfo != null)
            json.put("shard_id", shardInfo.getShardId()).put("shard_count", shardInfo.getShardTotal());

        authTokens.forEach(json::put);
    }

}
