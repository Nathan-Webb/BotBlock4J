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
package com.nathanwebb.botblock4j.exceptions;

import org.json.JSONObject;

/**
 * Happens when we are ratelimited by the api.
 */
public class RatelimitException extends BotBlockException {
    private String ratelimitedIp;
    private String ratelimitedRoute;
    private String ratelimitedBotId;
    private int secondsTilRetry;
    private long ratelimitReset;

    public RatelimitException(String jsonRatelimit){
        JSONObject ratelimited = new JSONObject(jsonRatelimit);
        ratelimitedIp = ratelimited.getString("ratelimit_ip");
        ratelimitedRoute = ratelimited.getString("ratelimit_route");
        ratelimitedBotId = ratelimited.getString("ratelimit_bot_id");
        secondsTilRetry = ratelimited.getInt("retry_after");
        ratelimitReset = ratelimited.getLong("ratelimit_reset");

    }

    @Override
    public String getMessage() {
        return "We have been ratelimited! Retry after " + secondsTilRetry + "s";
    }

    /**
     * @return The IP that is causing the site to ratelimit your request.
     */
    public String getRatelimitedIp() {
        return ratelimitedIp;
    }

    /**
     * @return The route that is being used excessively.
     */
    public String getRatelimitedRoute() {
        return ratelimitedRoute;
    }

    /**
     * @return The ID of the bot that is causing the site to ratelimit your request.
     */
    public String getRatelimitedBotId() {
        return ratelimitedBotId;
    }

    /**
     * @return The amount of seconds before you can try to send a request again.
     */
    public int getSecondsTilRetry() {
        return secondsTilRetry;
    }


    /**
     * @return The epoch second when the api ratelimiting.
     */
    public long getRatelimitReset() {
        return ratelimitReset;
    }
}
