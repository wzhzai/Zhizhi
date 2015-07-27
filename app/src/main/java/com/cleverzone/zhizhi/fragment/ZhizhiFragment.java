package com.cleverzone.zhizhi.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cleverzone.zhizhi.R;
import com.cleverzone.zhizhi.comui.LoadingDialog;
import com.cleverzone.zhizhi.sqlite.DBManager;
import com.cleverzone.zhizhi.util.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ZhizhiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZhizhiFragment extends BaseFragment implements Handler.Callback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private ListView mListView;
    private View mHeaderView;
    public View.OnClickListener mOnAddProductClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Utils.addProductChooseClassify(mContext);
        }
    };
    public ArrayList<String> mTipContentList;
    private static final String TIP_URL = "http://59.67.152.71/wangzhengze/ZhizhiServer/GetNews.php";
    private Handler mHandler = new Handler(this);
    private ZhizhiListAdapter mZhizhiListAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;


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
        mTipContentList = new ArrayList<>();
        mListView = (ListView) view.findViewById(R.id.zhizhi_list);
        mListView.addHeaderView(initHeaderView());
        mZhizhiListAdapter = new ZhizhiListAdapter();
        mListView.setAdapter(mZhizhiListAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.zhizhi_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setTopHintInfo();
                startLoadTips();
            }
        });
    }


    public void startLoadTips() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                try {
                    HttpResponse response = client.execute(new HttpGet(TIP_URL));
                    String data = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("message");
                    mTipContentList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mTipContentList.add(jsonArray.getString(i));
                    }
                    mHandler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private View initHeaderView() {
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.zhizhi_list_hint_header, null);
        return mHeaderView;
    }

    private void setTopHintInfo() {
        TextView tvDay = (TextView) mHeaderView.findViewById(R.id.zhizhi_top_day_hint_tv_number);
        TextView tvHint = (TextView) mHeaderView.findViewById(R.id.zhizhi_top_day_hint_tv_normal);
        String hintDate = DBManager.getInstance(mContext).getRecentHintDate();
        if (TextUtils.isEmpty(hintDate)) {
            tvHint.setText(R.string.zhizhi_top_day_hint_no_product);
            tvDay.setVisibility(View.GONE);
            ((View)tvHint.getParent()).setOnClickListener(mOnAddProductClickListener);
        } else {
            tvHint.setText(R.string.zhizhi_top_day_hint_text);
            tvDay.setText(Utils.getDayDifference(hintDate) + "天");
            tvDay.setVisibility(View.VISIBLE);
            ((View)tvHint.getParent()).setOnClickListener(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setTopHintInfo();
        startLoadTips();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                updateMessageList();
                break;
        }
        return false;
    }

    private void updateMessageList() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mZhizhiListAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private class ZhizhiListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTipContentList.size();
        }

        @Override
        public String getItem(int position) {
            return mTipContentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_zhizhi, parent, false);
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

            TextView tvBubble = (TextView) convertView.findViewById(R.id.zhizhi_item_bubble);
            tvBubble.setText(getItem(position));


            return convertView;
        }
    }

}
