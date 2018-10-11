package Client;

public class HeartBeat implements Runnable
{
    private Write write;

    public HeartBeat(Write write)
    {
        this.write = write;
    }

    @Override
    public void run()
    {
        while (true)
        {
            //  send heartBeat
            write.write(CLIENT_CMD.IMAV());

            //  wait to send next heartBeat
            try
            {
                //  send haertbeat every minute
                Thread.sleep(60000);

            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}