package ua.in.out.agntsmith;

import android.util.Log;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class LanCommunication<M> extends ReceiverAdapter {
    public static final String LOG_TAG = "ALZ313";
    private JChannel channel;

    private static String mConfigPath;

    private ArrayList<MessageListener<M>> listeners = new ArrayList<MessageListener<M>>();

    private static HashMap<String, LanCommunication<?>> openChannels = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> LanCommunication<T> getChannel(String channelName) {
        LanCommunication<T> ret = (LanCommunication<T>) openChannels.get(channelName);
        if (ret == null) {
            ret = new LanCommunication<>(channelName, mConfigPath);
            openChannels.put(channelName, ret);
        }
        return ret;
    }

    public LanCommunication(String groupName, String configPath) {
        mConfigPath  = configPath;
        try {
            channel=new JChannel(mConfigPath);
            channel.setReceiver(this);
            channel.connect(groupName);
            Log.v(LOG_TAG, "JGroups init: " + groupName);
        } catch (Exception e) {
            Log.v(LOG_TAG, "JGroups init failed ", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void receive(Message msg) {
        M obj = (M)msg.getObject();
        Log.v(LOG_TAG, "Message recieved: {}" + obj);

        for (MessageListener<M> listener: listeners) {
            listener.recieve(obj);
        }

    }

    public void send(M message) {
        try {
            Message m = new Message(null, null, message);
            channel.send(m);
            Log.v(LOG_TAG, "Message sent: {}" + m);
        } catch (Exception e) {
            Log.v(LOG_TAG, "Sending failed" + e);
        }
    }

    public static <T> void send(String channelName, T message) {
        getChannel(channelName).send(message);
    }


    public interface MessageListener<M> {
        public void recieve(M message);
    }

    public void addMessageListemer(MessageListener<M> listener) {
        listeners.add(listener);
    }
    public void removeMessageListemer(MessageListener<M> listener) {
        listeners.remove(listener);
    }

}
