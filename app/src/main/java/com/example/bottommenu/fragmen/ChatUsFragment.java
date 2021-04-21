package com.example.bottommenu.fragmen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.example.bottommenu.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatUsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageButton buttonBackChatUs;

    public ChatUsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatUsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatUsFragment newInstance(String param1, String param2) {
        ChatUsFragment fragment = new ChatUsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chat_us,container,false);
        WebView wv_davita=view.findViewById(R.id.wvChatUs);
        buttonBackChatUs=(ImageButton)view.findViewById(R.id.buttonBackChatUs);

        //backButtonHandler
        buttonBackChatUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
                //Enable Javasript Webview
        wv_davita.getSettings().setJavaScriptEnabled(true);

        wv_davita.setWebViewClient(new WebViewClient());
        wv_davita.loadUrl("https://webapps.bps.go.id/chat/index.php/idn/chat/startchat/(leaveamessage)/true/(theme)/2/(department)/243/(vid)/5e3098ba6610c6636ecc/(hash_resume)/56863_58d60d38e9c72b425769708a97c8c5ed8641102b/(er)/1?URLReferer=%2F%2Fjatim.bps.go.id%2F%23&tzuser=8");



        return view;
    }
}