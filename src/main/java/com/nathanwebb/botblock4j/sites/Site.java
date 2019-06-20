package com.nathanwebb.botblock4j.sites;

/**
 * Enum containing all available Lists on BotBlock for easier use.
 *
 * @see com.nathanwebb.botblock4j.BlockAuth#setListAuthToken(Site, String)
 * @see com.nathanwebb.botblock4j.BlockAuth.Builder#addListAuthToken(Site, String)
 */
public enum Site {

    BOTLIST_SPACE        ("botlist.space"),
    BOTSFORDISCORD_COM   ("botsfordiscord.com"),
    BOTS_ONDISCORD_XYZ   ("bots.ondiscord.xyz"),
    DISCORDAPPS_DEV      ("discordapps.dev"),
    DISCORD_BOATS        ("discord.boats"),
    DISCORDBOTS_ORG      ("discordbots.org"),
    DISCORDBOTLIST_COM   ("discordbotlist.org"),
    DISCORDBOTREVIEWS_XYZ("discordbotreviews.xyz"),
    DISCORDBOT_WORLD     ("discordbot.world"),
    DISCORD_BOTS_GG      ("discord.bots.gg"),
    DISCORD_SERVICES     ("discord.services"),
    DISCORDSBESTBOTS_XYZ ("discordbestbots.xyz"),
    DISCORDBOTS_FUN      ("discordbots.fun"),
    DIVINEDISCORDBOTS_COM("divinediscordbots.com"),
    LBOTS_ORG            ("lbots.org"),
    WONDERBOTLIST_COM    ("wonderbotlist.com");

    private String link;

    Site(String link){
        this.link = link;
    }

    /**
     * Gives the link of a site.
     *
     * @return The link of a site.
     */
    public String getLink(){
        return link;
    }
}
