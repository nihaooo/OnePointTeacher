package com.example.akuteacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by www10 on 2017/4/13.
 */

public class QRBeanAdapter extends BaseAdapter{
    private List<QRBean> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public QRBeanAdapter(Context context, List<QRBean> list) {
        mList = list;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.list_item,null);
            viewHolder.studentid = (TextView) convertView.findViewById(R.id.tv_studentid);
            viewHolder.classname = (TextView) convertView.findViewById(R.id.tv_classname);
            viewHolder.createdAt = (TextView) convertView.findViewById(R.id.tv_createdAt);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        QRBean qrBean = mList.get(position);
        viewHolder.studentid.setText(qrBean.getStudentid());
        viewHolder.classname.setText(qrBean.getStudentclass());
        viewHolder.createdAt.setText(qrBean.getCreatedAt());
        return convertView;
    }
    public class ViewHolder {
        public TextView studentid;
        public TextView classname;
        public TextView createdAt;
    }
}
