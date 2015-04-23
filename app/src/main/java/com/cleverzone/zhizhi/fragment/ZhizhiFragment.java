package com.cleverzone.zhizhi.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.cleverzone.zhizhi.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ZhizhiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZhizhiFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZhizhiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ZhizhiFragment newInstance(String param1, String param2) {
        ZhizhiFragment fragment = new ZhizhiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ZhizhiFragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_zhizhi, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(R.id.zhizhi_list);
        listView.addHeaderView(initHeaderView());
        listView.setAdapter(new ZhizhiListAdapter());
    }

    private View initHeaderView() {
        return LayoutInflater.from(mContext).inflate(R.layout.zhizhi_list_hint_header, null);
    }

    private class ZhizhiListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_zhizhi_list, parent, false);
            }

            ImageView ivBigCycle = (ImageView) convertView.findViewById(R.id.zhizhi_item_big_cycle);
            ImageView ivLastBigCycle = (ImageView) convertView.findViewById(R.id.zhizhi_item_last_big_cycle);
            int paddingLeft = convertView.getPaddingLeft();
            int paddingRight = convertView.getPaddingRight();
            if (position == 0) {
                ivBigCycle.setVisibility(View.VISIBLE);
                convertView.setPadding(paddingLeft, mContext.getResources().getDimensionPixelSize(R.dimen.zhizhi_list_item_padding_left_right), paddingRight, 0);
            } else {
                ivBigCycle.setVisibility(View.GONE);
                convertView.setPadding(paddingLeft, 0, paddingRight, 0);
            }

            if (position == getCount() - 1) {
                ivLastBigCycle.setVisibility(View.VISIBLE);
            } else {
                ivLastBigCycle.setVisibility(View.GONE);
            }

            return convertView;
        }
    }

}
