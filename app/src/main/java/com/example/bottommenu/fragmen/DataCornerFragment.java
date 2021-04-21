package com.example.bottommenu.fragmen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.bottommenu.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataCornerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataCornerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageButton buttonBackDataCorner;

    public DataCornerFragment() {
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
    public static DataCornerFragment newInstance(String param1, String param2) {
        DataCornerFragment fragment = new DataCornerFragment();
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
        View view=inflater.inflate(R.layout.fragment_data_corner,container,false);
        WebView wv_data_corner=view.findViewById(R.id.dataCorner);
        buttonBackDataCorner=(ImageButton) view.findViewById(R.id.buttonBackDataCorner);

        //backButtonHandler
        buttonBackDataCorner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        //Enable Javasript Webview
        wv_data_corner.getSettings().setJavaScriptEnabled(true);

        wv_data_corner.setWebViewClient(new WebViewClient());
        wv_data_corner.loadUrl("http://124.158.151.235:7777/elastic/");
        return view;
    }
}