package com.andre601.botblock4j.exceptions;


import org.json.JSONObject;

public class RatelimitedException extends Throwable{

    private int delay;
    private String ip;
    private String route;
    private String id;

    public RatelimitedException(String response){
        JSONObject json = new JSONObject(response);

        this.delay = json.getInt("retry_after");
        this.ip = json.getString("ratelimit_ip");
        this.route = json.getString("ratelimit_route");
        this.id = json.getString("ratelimit_bot_id");
    }

    @Override
    public String getMessage(){
        return String.format(
                "Bot ID: %s (IP: %s) got ratelimited on route %s! You can try again in %d seconds!",
                id,
                ip,
                route,
                delay
        );
    }

}
