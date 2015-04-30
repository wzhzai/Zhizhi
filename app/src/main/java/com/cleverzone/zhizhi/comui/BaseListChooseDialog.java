package com.cleverzone.zhizhi.comui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cleverzone.zhizhi.R;

/**
 * Created by wzhz on 15/4/30.
 */
public class BaseListChooseDialog extends AlertDialog {

    private ListView mListView;
    private Context mContext;
    private CharSequence mTitle;
    private String[] mListContents;
    private int[] mIcons;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private ChooseDialogAttrs mAttrs;

    protected BaseListChooseDialog(ChooseDialogAttrs attrs) {
        super(attrs.mContext);
        mAttrs = attrs;
        initAttrs();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_classify_choose);
        initAllViews();
    }

    private void initAttrs() {
        this.mContext = mAttrs.mContext;
        this.mTitle = mAttrs.mTitle;
        this.mListContents = mAttrs.mListContents;
        this.mOnItemClickListener = mAttrs.mOnItemClickListener;
        this.mIcons = mAttrs.mIcons;
    }

    private void initAllViews() {
        TextView title = (TextView) findViewById(R.id.dialog_classify_choose_title);
        title.setText(mTitle);
        mListView = (ListView) findViewById(R.id.dialog_classify_choose_list);
        mListView.setAdapter(new ChooseAdapter());
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOnItemClickListener.onItemClick(parent, view, position, id);
                dismiss();
            }
        };

        mListView.setOnItemClickListener(onItemClickListener);
    }

    private class ChooseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mListContents.length;
        }

        @Override
        public Object getItem(int position) {
            return mListContents[position];
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
            if (mIcons != null) {
                ivIcon.setVisibility(View.VISIBLE);
                ivIcon.setImageResource(mIcons[position]);
            } else {
                ivIcon.setVisibility(View.GONE);
            }
            tvItem.setText(mListContents[position]);

            return convertView;
        }
    }

    private static class ChooseDialogAttrs {
        public Context mContext;
        public CharSequence mTitle;
        public String[] mListContents;
        public int[] mIcons;
        public AdapterView.OnItemClickListener mOnItemClickListener;
    }

    public static class CBuilder {

        private final ChooseDialogAttrs P;

        public CBuilder(Context context) {
            P = new ChooseDialogAttrs();
            P.mContext = context;
        }

        public CBuilder setTitle(int resId) {
            P.mTitle = P.mContext.getString(resId);
            return this;
        }

        public CBuilder setTitle(CharSequence title) {
            P.mTitle = title;
            return this;
        }

        public CBuilder setContents(String[] contents) {
            P.mListContents = contents;
            return this;
        }

        public CBuilder setContents(int[] contents) {
            String[] strings = new String[contents.length];
            for (int i = 0; i < contents.length; i++) {
                strings[i] = P.mContext.getString(contents[i]);
            }
            P.mListContents = strings;
            return this;
        }

        public CBuilder setIcons(int[] icons) {
            P.mIcons = icons;
            return this;
        }

        public CBuilder setOnItemClickListener(AdapterView.OnItemClickListener listener) {
            P.mOnItemClickListener = listener;
            return this;

        }

        private BaseListChooseDialog create() {
            return new BaseListChooseDialog(P);
        }

        public BaseListChooseDialog show() {
            BaseListChooseDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

}
