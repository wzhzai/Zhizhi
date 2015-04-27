package com.cleverzone.zhizhi.comui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cleverzone.zhizhi.AddProductActivity;
import com.cleverzone.zhizhi.R;
import com.cleverzone.zhizhi.util.Const;

/**
 * Created by WANGZHENGZE on 2015/4/27.
 */
public class ClassifyChooseDialog extends AlertDialog {
    public ClassifyChooseDialog(Context context) {
        super(context);
    }

    protected ClassifyChooseDialog(Context context, int theme) {
        super(context, theme);
    }

    protected ClassifyChooseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private ListView mListView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        setContentView(R.layout.dialog_classify_choose);
        TextView title = (TextView) findViewById(R.id.dialog_classify_choose_title);
        title.setText(R.string.record_choose_classify_text);
        mListView = (ListView) findViewById(R.id.dialog_classify_choose_list);
        mListView.setAdapter(new ChooseAdapter());
        mListView.setOnItemClickListener(new AbsListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, AddProductActivity.class);
                intent.putExtra("which", position);
                mContext.startActivity(intent);
                dismiss();
            }
        });
    }

    private class ChooseAdapter extends BaseAdapter {

        private int[] mClassifies = Const.RECORD_CLASSIFIES_TEXT;

        @Override
        public int getCount() {
            return mClassifies.length;
        }

        @Override
        public Object getItem(int position) {
            return mClassifies[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_dialog_classify_choose_list, parent, false);
            }

            ImageView ivIcon = (ImageView) convertView.findViewById(R.id.item_classify_choose_iv);
            TextView tvItem = (TextView) convertView.findViewById(R.id.item_classify_choose_tv);
            ivIcon.setImageResource(Const.RECORD_CLASSIFIES_ICON[position]);
            tvItem.setText(mClassifies[position]);

            return convertView;
        }
    }
}
