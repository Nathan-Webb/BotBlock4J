package com.nathanwebb.BotBlock4J;

/**
 * Covers all lists that are supported by the api.
 * Some sites found in {@code https://botblock.org/lists} do not have api to support, therefore they are not shown here.
 */
public enum BotList {
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
