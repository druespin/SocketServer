import java.util.TimerTask;

public class ConnectTimerTask extends TimerTask {

    private boolean stop;

    @Override
    public void run() {
        stop = true;

    }

    public boolean getStop()    {
        return stop;
    }
}
