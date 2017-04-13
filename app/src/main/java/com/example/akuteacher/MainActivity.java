package com.example.akuteacher;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    String number;
    private QRBeanAdapter mAdapter;
    private List<QRBean> mlist;
    private TextView stunumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, "f9fd3e06a0ee9b9d702db043e81e7393");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mlist = new ArrayList<>();//数据源
        mAdapter = new QRBeanAdapter(this, mlist);
        final ListView listList = (ListView) findViewById(R.id.lv_main);
        listList.setAdapter(mAdapter);
        stunumber = (TextView) findViewById(R.id.tv_stunumber);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflate = LayoutInflater.from(MainActivity.this);
                View viewDialog = inflate.inflate(R.layout.settime, null);
                final EditText classname = (EditText) viewDialog.findViewById(R.id.et_class_name);
                builder.setView(viewDialog);
                builder.setTitle("查询");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String rq_classname = classname.getText().toString();
                        findclassname(rq_classname);
                        listList.setAdapter(mAdapter);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });
    }

    private void findclassname(String classname) {
        BmobQuery<QRBean> query = new BmobQuery<QRBean>();
        query.addWhereEqualTo("studentclass", classname);
        query.setLimit(100);
        query.findObjects(this, new FindListener<QRBean>() {
            @Override
            public void onSuccess(List<QRBean> list) {
                Toast.makeText(MainActivity.this, "查询成功,数据数目为" + list.size(), Toast.LENGTH_SHORT).show();
                number = String.valueOf(list.size());
                stunumber.setText(number);
                mlist.clear();
                mlist.addAll(list);//添加进数据源
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MainActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //时间查询语句**********************************************************************************************************************************
    private void findresult(String rq_date) {
        BmobQuery<QRBean> query = new BmobQuery<QRBean>();
        List<BmobQuery<QRBean>> and = new ArrayList<BmobQuery<QRBean>>();
        //大于00:00:00
        BmobQuery<QRBean> q1 = new BmobQuery<QRBean>();
        String start = rq_date + " 00:00:00";//"2015-05-01 00:00:00"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(date));
        and.add(q1);

        //小于23:59:59
        BmobQuery<QRBean> q2 = new BmobQuery<QRBean>();
        String end = rq_date + " 23:59:59";//"2015-05-01 23:59:59"
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = sdf1.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q2.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(date1));
        and.add(q2);
        //添加复合与查询
        query.and(and);
        query.findObjects(this, new FindListener<QRBean>() {

            @Override
            public void onSuccess(List<QRBean> list) {
                Toast.makeText(MainActivity.this, "查询成功人数:" + list.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MainActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //************************************************************************************************************************


}
