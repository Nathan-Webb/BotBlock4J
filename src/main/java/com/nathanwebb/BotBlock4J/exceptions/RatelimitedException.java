package com.nathanwebb.BotBlock4J.exceptions;

import org.json.JSONObject;

public class RatelimitedException extends BotBlockException{
    private String ratelimitedIp;
    private String ratelimitedRoute;
    private String ratelimitedBotId;
    private int secondsTilRetry;
    private long ratelimitReset;

    public RatelimitedException(String jsonRatelimit){
        JSONObject ratelimited = new JSONObject(jsonRatelimit);
        ratelimitedIp = ratelimited.getString("ratelimit_ip");
        ratelimitedRoute = ratelimited.getString("ratelimit_route");
        ratelimitedBotId = ratelimited.getString("ratelimit_bot_id");
        secondsTilRetry = ratelimited.getInt("retry_after");
        ratelimitReset = ratelimited.getLong("ratelimit_reset");

    }

    public String getRatelimitedIp() {
        return ratelimitedIp;
    }

    public String getRatelimitedRoute() {
        return ratelimitedRoute;
    }

    public String getRatelimitedBotId() {
        return ratelimitedBotId;
    }

    public int getSecondsTilRetry() {
        return secondsTilRetry;
    }

    @Override
    public String getMessage() {
        return "We have been ratelimited! Retry after: " + secondsTilRetry + "s";
    }

    public long getRatelimitReset() {
        return ratelimitReset;
    }
}
