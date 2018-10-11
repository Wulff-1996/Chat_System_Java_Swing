package Server;

import java.util.Timer;
import java.util.TimerTask;

public class HeartBeatListener implements Runnable
{
    private Timer timer;
    private int timeLeft;
    private final int countDownTime = 65; //  client IMAV in every minute, plus network delay

    //  task info
    private static final int DELAY = 1000;  //  delay and period set to 1000 -> updates every second
    private static final int PERIOD = 1000;

    //  running status
    private boolean isClientAlive = true;
    private ClientHandler clientHandler; //  needs fields and methods from clientHandler

    public HeartBeatListener(ClientHandler clientHandler)
    {
        this.clientHandler = clientHandler;
    }

    //  count down timeLeft,
    // if leftTime < 1 then -> remove client from list,
    // send update list to active clients and close clients connection
    private synchronized int countDown()
    {
        if (this.timeLeft <= 1)
        {
            //  quit by no heartbeat
            //  client has not send heartBeat
            Server.getServerWindow().writeToInfobox("No Heart Beat from client: " + this.clientHandler.getCLIENTSOCKET());

            //  remove client from clientlist
            Server.removeClientBySocket(this.clientHandler.getCLIENTSOCKET());

            //  send updated list to all connected clients
            this.clientHandler.sendListToClients();

            //  if heartBeat thread has not received a heartBeat
            this.clientHandler.closeClientConnection();

            //  shut down this thread
            this.timer.cancel();
        }
        return this.timeLeft--;
    }

    @Override
    public void run()
    {
        this.timer = new Timer();
        this.timeLeft = this.countDownTime; //  timer timeLeft

        //  create task
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if (isClientAlive)
                {
                    //  reset timer
                    timeLeft = countDownTime;

                    //  set isClientAlive = false, wait for new heartBeat
                    isClientAlive = false;
                }
                //  count down time
                countDown();
            }
        }, this.DELAY, this.PERIOD);
    }

    //  allow the clienthandler to set isClientAlive when receiving IMAV from client
    public void setIsClientAlive(boolean isClientAlive)
    {
        this.isClientAlive = isClientAlive;
    }
}