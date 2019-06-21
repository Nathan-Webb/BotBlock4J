/*
 * Copyright 2018 Nathan Webb (nathanwgithub@gmail.com), Andre601
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
package com.andre601.botblock4j;

import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public class BotBlockAPI {
    private Map<String, String> authTokens;

    private int updateInterval;
    private boolean jdaDisabled;
    private JDA jda;
    private ShardManager shardManager;

    /**
     * Creates a BotBlockAPI instance without JDA or ShardManager set.
     * <br>This whould be used when manually updating guilds.
     *
     * @param authTokens
     *        Map containing all the sites and their respective api-token.
     * @param updateInterval
     *        The time to wait when auto-post the Guilds.
     */
    public BotBlockAPI(Map<String, String> authTokens, int updateInterval){
        this.authTokens = authTokens;
        this.updateInterval = updateInterval;
        this.jdaDisabled = true;
        this.jda = null;
        this.shardManager = null;
    }

    /**
     * Creates a BotBlockAPI instance with a JDA instance set.
     *
     * @param authTokens
     *        Map containing all the sites and their respective api-token.
     * @param updateInterval
     *        The time to wait when auto-post the Guilds.
     * @param jda
     *        The instance of {@link net.dv8tion.jda.core.JDA JDA}.
     */
    public BotBlockAPI(Map<String, String> authTokens, int updateInterval, JDA jda){
        this.authTokens = authTokens;
        this.updateInterval = updateInterval;
        this.jdaDisabled = false;
        this.jda = jda;
        this.shardManager = null;
    }

    /**
     * Creates a BotBlockAPI instance with a ShardManager instance set.
     *
     * @param authTokens
     *        Map containing all the sites and their respective api-token.
     * @param updateInterval
     *        The time to wait when auto-post the Guilds.
     * @param shardManager
     *        The instance of the {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager}.
     */
    public BotBlockAPI(Map<String, String> authTokens, int updateInterval, ShardManager shardManager){
        this.authTokens = authTokens;
        this.updateInterval = updateInterval;
        this.jdaDisabled = false;
        this.jda = null;
        this.shardManager = shardManager;
    }

    /**
     * Gives the Map with the sites and their corresponding token.
     *
     * @return A Map containing the sites and their corresponding api-token.
     */
    public Map<String, String> getAuthTokens(){
        return authTokens;
    }

    /**
     * Returns if JDA (Either JDA or ShardManager) isn't required.
     *
     * @return True if JDA isn't required.
     */
    public boolean isJdaDisabled(){
        return jdaDisabled;
    }

    /**
     * Returns the current instance of {@link net.dv8tion.jda.core.JDA JDA}.
     *
     * @return Possibly-null instance of {@link net.dv8tion.jda.core.JDA JDA}.
     */
    public JDA getJDA(){
        return jda;
    }

    /**
     * Returns the current instance of the {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager}.
     *
     * @return Possibly-null instance of {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager}.
     */
    public ShardManager getShardManager(){
        return shardManager;
    }

    /**
     * Returns the update interval for {@link com.andre601.botblock4j.RequestHandler#startAutoPost(BotBlockAPI) RequestHandler#startAutoPost(BotBlockAPI)}.
     *
     * @return The update delay for the auto-posting.
     */
    public int getUpdateInterval(){
        return updateInterval;
    }

    /**
     * Builder class used to create an instance of {@link com.andre601.botblock4j.BotBlockAPI BotBlockAPI}.
     */
    public static class Builder{
        private Map<String, String> authTokens = new HashMap<>();

        private int updateInterval = 30;
        private boolean disabled = false;
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
            if(ObjectUtils.isEmpty(site) || ObjectUtils.isEmpty(token))
                throw new IllegalStateException("site and/or token may not be null!");

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
         * Sets the delay (in minutes) that the wrapper waits before posting the guild count again.
         * <br>This is used in {@link com.andre601.botblock4j.RequestHandler#startAutoPost(BotBlockAPI) RequestHandler#startAutoPost(BotBlockAPI)}
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
            if(updateInterval < 1)
                throw new IllegalArgumentException("updateInterval can't be lower than 1!");


            this.updateInterval = updateInterval;

            return this;
        }

        /**
         * Set if creating a {@link com.andre601.botblock4j.BotBlockAPI BotBlockAPI instance} requires either
         * {@link net.dv8tion.jda.core.JDA JDA} or {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager} to
         * be set.
         * <br>You have to use this when using either
         * {@link com.andre601.botblock4j.RequestHandler#postGuilds(Long, int, BotBlockAPI) postGuilds(Long, int, BotBlockAPI)} or
         * {@link com.andre601.botblock4j.RequestHandler#postGuilds(String, int, BotBlockAPI) postGuilds(String, int, BotBlockAPI)}
         *
         * <p><b>This will be set to false when you set a JDA or ShardManager instance after it!</b>
         *
         * @param  disabled
         *         The boolean to set if creating a BotBlockAPI instance requires JDA or ShardManager to be present.
         *         <br>True means that it does *NOT* require JDA or ShardManager.
         *
         * @return The Builder after the boolean was set.
         */
        public Builder disableJDARequirement(boolean disabled){
            this.disabled = disabled;

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
        public Builder setJDA(JDA jda){
            if(disabled)
                disabled = false;

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
            if(disabled)
                disabled = false;

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
                return new BotBlockAPI(authTokens, updateInterval, jda);
            else
            if(shardManager != null)
                return new BotBlockAPI(authTokens, updateInterval, shardManager);
            else
            if(disabled)
                return new BotBlockAPI(authTokens, updateInterval);
            else
                throw new IllegalStateException("Neither JDA nor ShardManager where set! Either set them or use disableJDARequirement");
        }
    }
}
