package ua.in.out.agntsmith;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import ua.vzaperti.matrix.net.MatrixEvent;

public class MainActivity extends AppCompatActivity implements OnCommandSendListener {
    private static final int FILE_SELECT_CODE = 0;

    LanCommunication<MatrixEvent> mLanCommunication;
    LanCommunication.MessageListener<MatrixEvent> mMessageListener;

    private CommandFragment mCommandFragment;
    private LogFragment mLogFragment;

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

        System.setProperty("java.net.preferIPv4Stack", "true");

        mCommandFragment = new CommandFragment();
        mLogFragment = new LogFragment();

        mMessageListener = new LanCommunication.MessageListener<MatrixEvent>() {
            @Override
            public void recieve(MatrixEvent message) {
                mLogFragment.addMessage(message.name());
            }
        };

        updateCommunication();

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mCommandFragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    mConfigPath = uri.getPath();

                    if (mConfigPath != null) {
                        // save new config file path
                        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.saved_config_path), mConfigPath);
                        editor.apply();

                        updateCommunication();
                    } else {
                        Toast.makeText(this, "Bad file path.\nTry other file Chooser!", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
            case R.id.menu_config_file:
                showFileChooser();
                return true;
            case R.id.menu_claster_name:
                final EditText taskEditText = new EditText(this);
                taskEditText.setText(mClusterName);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Cluster name")
                        .setMessage("Select cluster name")
                        .setView(taskEditText)
                        .setPositiveButton("Same", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mClusterName = String.valueOf(taskEditText.getText());
                                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(getString(R.string.saved_cluster_name), mClusterName);
                                editor.apply();

                                updateCommunication();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateCommunication() {
        if (mLanCommunication != null) {
            mLanCommunication.removeMessageListemer(mMessageListener);
        }

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        mConfigPath = sharedPref.getString(getString(R.string.saved_config_path), null);
        mClusterName = sharedPref.getString(getString(R.string.saved_cluster_name), null);

        mLanCommunication = new LanCommunication<>(mClusterName, mConfigPath);
        mLanCommunication.addMessageListemer(mMessageListener);
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/xml");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

}
