package com.comp90018.JoinMe;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Map;

public class CandidateList extends AppCompatActivity {
    NavigationBarView bottomNavigationView;

    public static String title[]=new String[]{"菜名0","菜名1","菜名2","菜名3","菜名4","菜名5","菜名6","菜名7","菜名8","菜名9"};
    public static String info[]=new String[]{ "￥：28","￥：28","￥：28","￥：28","￥：28","￥：28","￥：28","￥：28","￥：28","￥：28",};

    private List<Map<String, Object>> mData;
    private int flag;

    private ListView candidateListView;
    private DatabaseReference databaseReference;
    private ArrayList<String> candidateList = new ArrayList<String>();
    private ArrayList<String> candidateIdList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_test);
        mData = getData();
        ListView listView = (ListView) findViewById(R.id.listView);
        MyAdapter adapter = new MyAdapter(this);
        listView.setAdapter(adapter);









//        databaseReference = FirebaseDatabase.getInstance().getReference().child("activity");
//        databaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
////                 TODO change to candidatelist (now use activity title )
//                @SuppressWarnings("unchecked")
//                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
//                candidateList.add((String) map.get("details"));
//                candidateIdList.add((String) dataSnapshot.getKey());
//
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i<title.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", title[i]);
            map.put("info", info[i]);
            list.add(map);
        }

        return list;
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {

                holder = new ViewHolder();

                //可以理解为从vlist获取view  之后把view返回给ListView

                convertView = mInflater.inflate(R.layout.vlist, null);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.info = (TextView) convertView.findViewById(R.id.info);
                holder.viewBtn = (Button) convertView.findViewById(R.id.view_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.title.setText((String) mData.get(position).get("title"));
            holder.info.setText((String) mData.get(position).get("info"));
            holder.viewBtn.setTag(position);
            //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉
//            holder.viewBtn.setOnClickListener(new View.OnClickListener() {
//                // todo
//            });

            //holder.viewBtn.setOnClickListener(MyListener(position));

            return convertView;
        }
    }

        public final class ViewHolder {
            public TextView title;
            public TextView info;
            public Button viewBtn;
        }




    }
