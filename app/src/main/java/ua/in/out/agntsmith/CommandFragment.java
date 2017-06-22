package ua.in.out.agntsmith;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ua.vzaperti.matrix.net.MatrixEvent;


public class CommandFragment extends Fragment {
    private OnCommandSendListener mListener;

    ArrayAdapter<String> mCommandAdapter;
    ListView mCommandListView;

    public CommandFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_command_list, container, false);

        mCommandListView = (ListView) view.findViewById(R.id.list_commands);

        List<String> jgroupsCommands = new ArrayList<>();
        MatrixEvent[] vals = MatrixEvent.values();
        for (MatrixEvent val : vals) {
            jgroupsCommands.add(val.name());
        }


        mCommandAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, jgroupsCommands);
        mCommandListView.setAdapter(mCommandAdapter);
        mCommandListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onCommandSend(position);
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCommandSendListener) {
            mListener = (OnCommandSendListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnCommandSendListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
