package ua.in.out.agntsmith;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import ua.vzaperti.matrix.net.MatrixEvent;

public class MainActivity extends AppCompatActivity implements OnCommandSendListener, SharedPreferences.OnSharedPreferenceChangeListener {
    LanCommunication mLanCommunication;
    LanCommunication.MessageListener<MatrixEvent> mMessageListener;

    private CommandFragment mCommandFragment;
    private LogFragment mLogFragment;

    ProgressBar mProgressBar;

    SharedPreferences mSharedPref;

    String mConfigPath;
    String mClusterName;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_command:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCommandFragment).commit();
                    return true;
                case R.id.navigation_log:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mLogFragment).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.pg_progress);

        System.setProperty("java.net.preferIPv4Stack", "true");

        mCommandFragment = new CommandFragment();
        mLogFragment = new LogFragment();

        mMessageListener = new LanCommunication.MessageListener<MatrixEvent>() {
            @Override
            public void recieve(MatrixEvent message) {
                mLogFragment.addMessage(message.name());
            }
        };

        mSharedPref = getPreferences(Context.MODE_PRIVATE);
        mConfigPath = mSharedPref.getString(getString(R.string.pref_config_file_path_key), null);
        mClusterName = mSharedPref.getString(getString(R.string.pref_cluster_name_key), null);


        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mCommandFragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        new SetLanCommunication().execute(mClusterName, mConfigPath);
    }

    @Override
    public void onCommandSend(int item) {
        try {
            mLanCommunication.send(MatrixEvent.values()[item]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        mLanCommunication.removeMessageListemer(mMessageListener);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mConfigPath = mSharedPref.getString(getString(R.string.pref_config_file_path_key), null);
        mClusterName = mSharedPref.getString(getString(R.string.pref_cluster_name_key), null);

        new SetLanCommunication().execute(mClusterName, mConfigPath);
    }


    private class SetLanCommunication extends AsyncTask<String, Void, LanCommunication<LanCommunication.MessageListener>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            if (mLanCommunication != null) {
                mLanCommunication.removeMessageListemer(mMessageListener);
            }
        }

        @Override
        protected LanCommunication<LanCommunication.MessageListener> doInBackground(String... params) {
            return new LanCommunication<>(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(LanCommunication<LanCommunication.MessageListener> messageListenerLanCommunication) {
            super.onPostExecute(messageListenerLanCommunication);
            mLanCommunication = messageListenerLanCommunication;
            mLanCommunication.addMessageListemer(mMessageListener);

            mProgressBar.setVisibility(View.GONE);
        }
    }
}
