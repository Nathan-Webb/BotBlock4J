import com.nathanwebb.BotBlock4J.BlockAuth;
import com.nathanwebb.BotBlock4J.BotBlockRequests;
import com.nathanwebb.BotBlock4J.BotList;
import com.nathanwebb.BotBlock4J.exceptions.EmptyResponseException;
import com.nathanwebb.BotBlock4J.exceptions.FailedToSendException;
import com.nathanwebb.BotBlock4J.exceptions.RatelimitedException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class BotTests extends ListenerAdapter{
    private BlockAuth blockAuth;
    @Test
    public void testPostRequest(){
        try {
            BufferedReader discordTokenReader = new BufferedReader(new FileReader("testing" + File.separator + "blyat.txt"));
            String token = discordTokenReader.readLine();
            discordTokenReader.close();

            BufferedReader botListTokenReader = new BufferedReader(new FileReader("testing" + File.separator + "bfd_token.txt"));
            String bfdToken = botListTokenReader.readLine();
            botListTokenReader.close();

            JDABuilder builder = new JDABuilder(AccountType.BOT);
            builder.setToken(token);
            builder.addEventListener(this);

            blockAuth = new BlockAuth();
            blockAuth.setListAuthToken(BotList.BOTS_FOR_DISCORD, bfdToken);

            JDA jda = builder.buildBlocking();
            System.out.printf("Logged in as:\n%s (%s)\n", jda.getSelfUser().getName(), jda.getSelfUser().getId());


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        try {
            BotBlockRequests.postGuildsJDA(event.getJDA(), blockAuth);
            System.out.println("Guild Counts have been POSTED!");
        } catch (Exception | FailedToSendException | EmptyResponseException | RatelimitedException e){
            e.printStackTrace();
        }
    }
}
