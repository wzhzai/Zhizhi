package com.cleverzone.zhizhi;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.cleverzone.zhizhi.bean.ProductBean;
import com.cleverzone.zhizhi.sqlite.DBManager;
import com.cleverzone.zhizhi.util.Const;
import com.cleverzone.zhizhi.util.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;


public class RecordItemDetail extends BaseActivity {

    private static final String TAG = "RecordItemDetail";

    private Context mContext;
    private String mMainClassify;
    private ExpandableListView mExpandableListView;
    private LinkedHashMap<String, ArrayList<ProductBean>> mAllProductInfoMap;
    private ArrayList<String> mGroupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_item_detail);
        mContext = this;
        mMainClassify = mContext.getString(Const.RECORD_CLASSIFIES_TEXT[getIntent().getIntExtra("which", 0)]);
        setTitle(R.mipmap.title_bar_icon_record);
        mAllProductInfoMap = DBManager.getInstance(mContext).getAllProductInfoByMainClassify(mMainClassify);
        mGroupList = new ArrayList<>(mAllProductInfoMap.keySet());
        initAllViews();

    }

    private void initAllViews() {
        mExpandableListView = (ExpandableListView) findViewById(R.id.record_detail_exp_list);
        mExpandableListView.setAdapter(new DetailAdapter());
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(mContext, "groupPosition = " + groupPosition + "; childPosition = " + childPosition
                        + "; id = " + id, Toast.LENGTH_SHORT).show();
                return true;
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

            tvName.setText(bean.name);
            tvPr.setText(mContext.getString(R.string.add_product_pr_hint) + ": " + bean.prDate);
            tvEx.setText(mContext.getString(R.string.add_product_ex_hint) + ": " + bean.exDate);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record_item_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
