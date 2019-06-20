package com.andre601.botblock4j;

import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public class BotBlockAPI {
    private Map<String, String> authTokens = new HashMap<>();

    private boolean autoPost;
    private int updateInterval;
    private JDA jda;
    private ShardManager shardManager;

    public BotBlockAPI(){}

    public BotBlockAPI(Map<String, String> authTokens){
        this.authTokens = authTokens;
    }

    public BotBlockAPI(Map<String, String> authTokens, boolean autoPost, int updateInterval, JDA jda){
        this.authTokens = authTokens;
        this.autoPost = autoPost;
        this.updateInterval = updateInterval;
        this.jda = jda;
        this.shardManager = null;
    }

    public BotBlockAPI(Map<String, String> authTokens, boolean autoPost, int updateInterval, ShardManager shardManager){
        this.authTokens = authTokens;
        this.autoPost = autoPost;
        this.updateInterval = updateInterval;
        this.jda = null;
        this.shardManager = shardManager;
    }

    public static class Builder{
        private Map<String, String> authTokens = new HashMap<>();

        private boolean autoPost = false;
        private int updateInterval = 30;
        private JDA jda = null;
        private ShardManager shardManager = null;

        /**
         * Add a token to a corresponding site.
         * <br>You need to provide the site without {@code http(s)://} and without any subsites/domains.
         *
         * <p>A list of supported sites can be found <a href="https://botblock.org/api/docs#count">here</a>.
         *
         * @param  site
         *         The site you want to assign the token of.
         * @param  token
         *         The API token you get from a botlist to post the guild counts.
         *
         * @throws IllegalStateException
         *         When either the site or token is empty/null.
         *
         * @return The Builder after the token was added.
         */
        public Builder addAuthToken(String site, String token){
            if(!ObjectUtils.allNotNull(site, token))
                throw new IllegalStateException("site and token may not be null!");

            authTokens.put(site, token);

            return this;
        }

        /**
         * Sets the sites and their auth-tokens to the provided values.
         * <br><b>This will override all previously set values!</b>
         *
         * @param  authTokens
         *         The {@link java.util.Map Map<String, String>} containing the values.
         *
         * @throws IllegalStateException
         *         When the provided Map is empty.
         *
         * @return The Builder after the Map was set.
         *
         * @see #addAuthToken(String, String) for adding a single entry to the list.
         */
        public Builder setAuthTokens(Map<String, String> authTokens){
            if(authTokens.isEmpty())
                throw new IllegalStateException("the HashMap may not be empty!");

            this.authTokens = authTokens;

            return this;
        }

        /**
         * Sets if the Wrapper should auto-post the guild count to the BotBlock-API.
         * <br>Default is {@code false}.
         *
         * @param  autoPost
         *         The boolean. True to post automatically, false to not.
         *
         * @return The Builder after the boolean was set.
         */
        public Builder setAutoPost(boolean autoPost){
            this.autoPost = autoPost;

            return this;
        }

        /**
         * Sets the delay (in minutes) that the wrapper waits before posting the guild count again.
         * <br>This setting is ignored when {@link #setAutoPost(boolean)} is set to false.
         *
         * @param  updateInterval
         *         The interval in minutes in which the Wrapper waits before posting again.
         *
         * @throws IllegalArgumentException
         *         When the provided updateInterval is less than 1.
         *
         * @return The Builder after the interval was set.
         */
        public Builder setUpdateInterval(int updateInterval){
            if(updateInterval < 1){
                throw new IllegalArgumentException("updateInterval can't be lower than 1!");
            }

            this.updateInterval = updateInterval;

            return this;
        }

        /**
         * Sets the instance of {@link net.dv8tion.jda.core.JDA JDA} to use.
         * <br><b>This setting is ignored when a {@link #setShardManager(ShardManager) ShardManager} was set!</b>
         *
         * @param  jda
         *         The {@link net.dv8tion.jda.core.JDA JDA instance}.
         *
         * @return The Builder after JDA was set.
         */
        public Builder setJda(JDA jda){
            this.jda = jda;

            return this;
        }

        /**
         * Sets the instance of the {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager} to use.
         *
         * @param  shardManager
         *         The {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager instance}.
         *
         * @return The Builder after the ShardManager was set.
         */
        public Builder setShardManager(ShardManager shardManager){
            this.shardManager = shardManager;

            return this;
        }

        /**
         * Creates a new {@link com.andre601.botblock4j.BotBlockAPI BotBlockAPI}.
         *
         * @throws IllegalStateException
         *         When neither JDA nor ShardManager where defined.
         *
         * @return a new instance of {@link com.andre601.botblock4j.BotBlockAPI BotBlockAPI}.
         */
        public BotBlockAPI build(){
            if(jda != null)
                return new BotBlockAPI(authTokens, autoPost, updateInterval, jda);
            else
            if(shardManager != null)
                return new BotBlockAPI(authTokens, autoPost, updateInterval, shardManager);
            else
                throw new IllegalStateException("Neither JDA nor ShardManager where set!");
        }
    }
}
