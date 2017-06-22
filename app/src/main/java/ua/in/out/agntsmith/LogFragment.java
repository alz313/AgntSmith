package ua.in.out.agntsmith;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnCommandSendListener}
 * interface.
 */
public class LogFragment extends Fragment {

    ArrayAdapter<String> mHistoryAdapter;
    ListView mHistoryListView;
    List<String> mJgroupsMessages = new ArrayList<>();

    public LogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_list, container, false);

        mHistoryListView = (ListView) view.findViewById(R.id.list_log);
        mHistoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mJgroupsMessages);
        mHistoryListView.setAdapter(mHistoryAdapter);

        return view;
    }

    public void addMessage(String message) {
        mJgroupsMessages.add(message);

        Runnable run = new Runnable() {
            public void run() {
                mHistoryAdapter.notifyDataSetChanged();
            }
        };
        getActivity().runOnUiThread(run);

    }


}
