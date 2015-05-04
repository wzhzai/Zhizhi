package com.cleverzone.zhizhi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cleverzone.zhizhi.bean.ProductBean;
import com.cleverzone.zhizhi.sqlite.DBManager;
import com.cleverzone.zhizhi.util.Const;
import com.cleverzone.zhizhi.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;


public class RecordItemDetailActivity extends BaseActivity {

    private static final String TAG = "RecordItemDetailActivity";

    private Context mContext;
    private String mMainClassify;
    private ExpandableListView mExpandableListView;
    private LinkedHashMap<String, ArrayList<ProductBean>> mAllProductInfoMap;
    private ArrayList<String> mGroupList;
    private int mWhich;
    private DetailAdapter mDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_item_detail);
        mContext = this;
        mWhich = getIntent().getIntExtra("which", 0);
        mMainClassify = mContext.getString(Const.RECORD_CLASSIFIES_TEXT[mWhich]);
        setTitle(R.mipmap.title_bar_icon_record);
        setLeftButtonHide();
        setRightButtonHide();
        mAllProductInfoMap = new LinkedHashMap<>();
        mGroupList = new ArrayList<>();
        initAllViews();

    }

    private void initData() {
        mAllProductInfoMap.clear();
        mAllProductInfoMap.putAll(DBManager.getInstance(mContext).getAllProductInfoByMainClassify(mMainClassify));
        mGroupList.clear();
        mGroupList.addAll(mAllProductInfoMap.keySet());
    }

    private void initAllViews() {
        mExpandableListView = (ExpandableListView) findViewById(R.id.record_detail_exp_list);
        mDetailAdapter = new DetailAdapter();
        mExpandableListView.setAdapter(mDetailAdapter);
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(mContext, AddProductActivity.class);
                intent.putExtra("which", mWhich);
                intent.putExtra("mode", AddProductActivity.MODE_SHOW);
                intent.putExtra("bean", mAllProductInfoMap.get(mGroupList.get(groupPosition)).get(childPosition));
                mContext.startActivity(intent);
                return true;
            }
        });
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                ImageView iv = (ImageView) v.findViewById(R.id.record_detail_group_arrow);
                if (parent.isGroupExpanded(groupPosition)) {
                    iv.setImageResource(R.mipmap.icon_arrow_right_white);
                } else {
                    iv.setImageResource(R.mipmap.icon_arrow_down_white);
                }
                return false;
            }
        });
    }

    private class DetailAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return mGroupList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mAllProductInfoMap.get(mGroupList.get(groupPosition)).size();
        }

        @Override
        public String getGroup(int groupPosition) {
            return mGroupList.get(groupPosition);
        }

        @Override
        public ProductBean getChild(int groupPosition, int childPosition) {
            return mAllProductInfoMap.get(mGroupList.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_record_detail_group, parent, false);
            }

            TextView tvGroupName = (TextView) convertView.findViewById(R.id.record_detail_group_text);
            tvGroupName.setText(getGroup(groupPosition));

            TextView tvRecentHint = (TextView) convertView.findViewById(R.id.record_detail_group_recent_hint);
            String hintDate = DBManager.getInstance(mContext).getRecentHintDateByMainAndSubClassify(mMainClassify, getGroup(groupPosition));
            int differentDay = Utils.getDayDifference(hintDate);
            tvRecentHint.setText(Html.fromHtml(mContext.getString(R.string.record_recent_hint_text, differentDay)));

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ProductBean bean = getChild(groupPosition, childPosition);

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_record_detail_child, parent, false);
            }

            TextView tvName = (TextView) convertView.findViewById(R.id.record_detail_child_tv_name);
            TextView tvPr = (TextView) convertView.findViewById(R.id.record_detail_child_tv_pr);
            TextView tvEx = (TextView) convertView.findViewById(R.id.record_detail_child_tv_ex);
            ImageView ivImage = (ImageView) convertView.findViewById(R.id.record_detail_child_iv);

            tvName.setText(bean.name);
            tvPr.setText(mContext.getString(R.string.add_product_pr_hint) + ": " + bean.prDate);
            tvEx.setText(mContext.getString(R.string.add_product_ex_hint) + ": " + bean.exDate);
            ImageLoader.getInstance().displayImage(bean.picPath, ivImage, Utils.getDefaultImageLoaderOptions());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        if (mGroupList.size() == 0) {
            finish();
        } else {
            mDetailAdapter.notifyDataSetChanged();
        }
    }

}
