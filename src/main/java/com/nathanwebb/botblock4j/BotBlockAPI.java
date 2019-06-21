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
package com.nathanwebb.botblock4j;

import com.nathanwebb.botblock4j.exceptions.EmptyResponseException;
import com.nathanwebb.botblock4j.exceptions.FailedToSendException;
import com.nathanwebb.botblock4j.exceptions.RateLimitedException;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main class that is what users should use to interact with the BotBlock API.
 */
public class BotBlockAPI {
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private BlockAuth blockAuth = new BlockAuth();
    private ShardManager shardManager;
    private JDA jda;
    private int updateInterval = 30;


    public BotBlockAPI(){}


    /**
     * @param shardManager
     *        An instance of the {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager}.
     */
    public BotBlockAPI(ShardManager shardManager){
        this.shardManager = shardManager;
    }


    /**
     * @param shardManager
     *        An instance of the {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager}.
     * @param startInterval
     *        Should the API start the timer to post guild count updates.
     * @param blockAuth
     *        An instance of {@link com.nathanwebb.botblock4j.BlockAuth BlockAuth}.
     */
    public BotBlockAPI(ShardManager shardManager, boolean startInterval, BlockAuth blockAuth){
        this.blockAuth = blockAuth;
        this.shardManager = shardManager;
        if(startInterval)
            startSendingGuildCounts();
    }

    /**
     * @param shardManager
     *        An instance of the {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager}.
     * @param startInterval
     *        Should the API start the timer to post guild count updates.
     * @param blockAuth
     *        An instance of {@link com.nathanwebb.botblock4j.BlockAuth BlockAuth}.
     * @param updateInterval
     *        Number of minutes the api should wait before posting guild count updates.
     */
    public BotBlockAPI(ShardManager shardManager, boolean startInterval, BlockAuth blockAuth, int updateInterval){
        this.blockAuth = blockAuth;
        this.shardManager = shardManager;
        this.updateInterval = updateInterval;
        if(startInterval)
            startSendingGuildCounts();
    }

    /**
     * @param jda
     *        An instance of the {@link net.dv8tion.jda.core.JDA JDA}.
     */
    public BotBlockAPI(JDA jda){
        this.jda = jda;
    }

    /**
     * @param jda
     *        An instance of {@link net.dv8tion.jda.core.JDA JDA}.
     * @param startInterval
     *        Should the API start the timer to post guild count updates.
     * @param blockAuth
     *        An instance of {@link com.nathanwebb.botblock4j.BlockAuth BlockAuth}.
     */
    public BotBlockAPI(JDA jda, boolean startInterval, BlockAuth blockAuth){
        this.jda = jda;
        this.blockAuth = blockAuth;
        if(startInterval)
            startSendingGuildCounts();
    }

    /**
     * @param jda
     *        An instance of {@link net.dv8tion.jda.core.JDA JDA}.
     * @param startInterval
     *        Should the API start the timer to post guild count updates.
     * @param blockAuth
     *        An instance of {@link com.nathanwebb.botblock4j.BlockAuth BlockAuth}.
     * @param updateInterval
     *        Number of minutes the api should wait before posting guild count updates.
     */
    public BotBlockAPI(JDA jda, boolean startInterval, BlockAuth blockAuth, int updateInterval){
        this.jda = jda;
        this.blockAuth = blockAuth;
        this.updateInterval = updateInterval;
        if(startInterval)
            startSendingGuildCounts();
    }

    /**
     * The BlockAuth should have at least one bot list added.
     *
     * @param blockAuth
     *        An instance of {@link com.nathanwebb.botblock4j.BlockAuth BlockAuth}.
     */
    public void setblockAuth(BlockAuth blockAuth){
        this.blockAuth = blockAuth;
    }

    /**
     * Sets the {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager} that will be used when sending requests.
     *
     * @param shardManager
     *        An instance of the {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager}.
     */
    public void setShardManager(ShardManager shardManager){
        this.shardManager = shardManager;
    }

    /**
     * Sets the {@link net.dv8tion.jda.core.JDA JDA} that will be used when sending requests.
     *
     * @param jda
     *        An instance of {@link net.dv8tion.jda.core.JDA JDA}.
     */
    public void setJda(JDA jda){
        this.jda = jda;
    }

    /**
     * Sets the delay between sending guild counts.
     *
     * @param updateInterval
     *        Interval between guild count updates in minutes.
     *
     * @throws IllegalArgumentException
     *         If the int is less than 1.
     */
    public void setUpdateInterval(int updateInterval) throws IllegalArgumentException{
        if(updateInterval < 1)
            throw new IllegalArgumentException("The amount of minutes between POST Requests must be at least 1.");

        this.updateInterval = updateInterval;
    }


    /**
     * Starts the guild counter.
     * <br>The wrapper prioritizes the {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager} over the
     * {@link net.dv8tion.jda.core.JDA JDA} when both are set.
     *
     * @throws IllegalStateException
     *         If neither a JDA instance nor a ShardManager are initialized.
     */
    public void startSendingGuildCounts() throws IllegalStateException{
        scheduler.scheduleAtFixedRate(() -> {
            if(shardManager !=null){
                try {
                    BotBlockRequests.postGuilds(shardManager, blockAuth);
                } catch (FailedToSendException | EmptyResponseException | RateLimitedException | IOException e) {
                    e.printStackTrace();
                }
            }else
            if(jda != null){
                try {
                    BotBlockRequests.postGuilds(jda, blockAuth);
                } catch (FailedToSendException | EmptyResponseException | RateLimitedException | IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new IllegalStateException("Neither ShardManager nor JDA instance was provided!");
            }
       }, updateInterval, updateInterval, TimeUnit.MINUTES);
    }

    /**
     * Shuts down the guild counter.
     */
    public void stopSendingGuildCounts(){
        scheduler.shutdown();
    }
}
