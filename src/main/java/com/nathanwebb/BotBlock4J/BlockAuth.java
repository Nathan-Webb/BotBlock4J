package com.nathanwebb.BotBlock4J;


import java.util.HashMap;



public class BlockAuth {
    private HashMap<BotList, String> authHashMap = new HashMap<>();

    BlockAuth(){}


    /**
     * Sets the authorization token given a Bot List and the token associated with the
     * @param list Specified Bot list. If the value has already been set in the preceding code, the value will be overwritten.
     * @param authToken The token that you would normally put in your {@code Authorization} header if you were doing this manually.
     */
    public void setListAuthToken(BotList list, String authToken){
        authHashMap.put(list, authToken);
    }

    public HashMap<BotList, String> getAuthHashMap() {
        return authHashMap;
    }
}

/**
 * Covers all lists that are supported by the api.
 * Some sites found in {@code https://botblock.org/lists} do not have api to support, therefore they are not shown here.
 */
enum BotList {
    BOTLIST_SPACE("botlist.space"), BOTS_FOR_DISCORD("botsfordiscord.com"), BOTS_ON_DISCORD("botsondiscord.xyz"),
    DISCORD_BOATS_CLUB("discordboats.club"), DISCORD_BOTS("discordbots.org"), DISCORDBOT_WORLD("discordbot.world"),
    BOTS_DISCORD_PW("bots.discord.pw"), DISCORDBOTS_GROUP("discordbots.group"), DISCORD_SERVICES("discord.services");

    private final String url;

    BotList(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }
}
