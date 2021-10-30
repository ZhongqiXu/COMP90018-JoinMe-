package com.comp90018.JoinMe;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

import object.Activity;

public class CandidateList extends AppCompatActivity {
    NavigationBarView bottomNavigationView;
    Bundle bundle;
    Activity activity = new Activity();
    HashMap activityInfo;
    private String aId;

    public static String candidatesId[]= new String[]{}; // candidates_id
    public static String candidatesName[]= new String[]{};  // candidate_name

    public static String participatesId[]= new String[]{}; // participates_id
    public static String participatesName[]= new String[]{};  // participates_name

    public static String user_list_array[] = new String[]{};
    public static String user_id_array[] = new String[]{};

    private List<Map<String, Object>> mData;
    private List<Map<String, Object>> mData2;
    private int flag;

    private ListView candidateListView;
    private DatabaseReference databaseReference;
    private ArrayList<String> userList = new ArrayList<String>();
    private ArrayList<String> userIdList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_list);

        Bundle bundle = getIntent().getExtras();
        HashMap activityInfo = (HashMap) bundle.get("activityInfo");
        object.Activity activity = new Activity();
        activity.mapToActivity(activity, activityInfo);
        candidatesId = activity.getCandidates().toArray(new String[0]);
        participatesId = activity.getParticipants().toArray(new String[0]);
        aId = activity.getAid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                userList.add((String) map.get("userName"));
                userIdList.add((String) map.get("uid"));
                String[] can_list_temp = new String[userList.size()];
                String[] can_key_temp = new String[userIdList.size()];

                for (int i = 0; i < userList.size(); i++){
                    can_list_temp[i] = userList.get(i);
                }
                for (int i = 0; i < userIdList.size(); i++){
                    can_key_temp[i] = userIdList.get(i);
                }

                user_list_array = can_list_temp;
                user_id_array = can_key_temp;


//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // candidates
        String[] temp = new String[candidatesId.length];

        for (int i = 0; i < candidatesId.length; i++) {

            for (int j = 0; j< user_id_array.length;j++){
                if (candidatesId[i].equals(user_id_array[j])) {
                        temp[i] = user_list_array[j];
                }
            }
        }
        candidatesName = temp;

        mData = getData();
        ListView listView = (ListView) findViewById(R.id.listView);
        MyAdapter adapter = new MyAdapter(this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        // participants
        String[] temp2 = new String[participatesId.length];

        for (int i = 0; i < participatesId.length; i++) {

            for (int j = 0; j< user_id_array.length;j++){
                if (participatesId[i].equals(user_id_array[j])) {
                    temp2[i] = user_list_array[j];
                }
            }
        }
        participatesName = temp2;


        mData2 = getData2();
        ListView listView2 = (ListView) findViewById(R.id.list_view2);
        MyAdapter2 adapter2 = new MyAdapter2(this);
        listView2.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();


    }



    private List<Map<String, Object>> getData() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("activity");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i<candidatesId.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("candidatesName", candidatesName[i]);
            list.add(map);
   }

        return list;
}

    private List<Map<String, Object>> getData2() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("activity");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i<participatesId.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("participatesName", participatesName[i]);
            list.add(map);
        }

        return list;
    }



    public class MyAdapter extends BaseAdapter {

        private final LayoutInflater mInflater;

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
//                holder.info = (TextView) convertView.findViewById(R.id.info);
                holder.viewBtn = (Button) convertView.findViewById(R.id.view_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.title.setText((String) mData.get(position).get("candidatesName"));
//            holder.info.setText((String) mData.get(position).get("info"));
            holder.viewBtn.setTag(position);
            //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉
            holder.viewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirm(position);
                }
                // todo
            });

            //holder.viewBtn.setOnClickListener(MyListener(position));

            return convertView;
        }
    }

    public class MyAdapter2 extends BaseAdapter {

        private final LayoutInflater mInflater;

        public MyAdapter2 (Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData2.size();
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
            ViewHolder2 holder = null;
            if (convertView == null) {

                holder = new ViewHolder2();

                //可以理解为从vlist获取view  之后把view返回给ListView

                convertView = mInflater.inflate(R.layout.vlist_2, null);
                holder.title = (TextView) convertView.findViewById(R.id.title);
//                holder.info = (TextView) convertView.findViewById(R.id.info);
//                holder.viewBtn = (Button) convertView.findViewById(R.id.view_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder2) convertView.getTag();
            }

            holder.title.setText((String) mData2.get(position).get("participatesName"));
//            holder.info.setText((String) mData.get(position).get("info"));
//            holder.viewBtn.setTag(position);

            //holder.viewBtn.setOnClickListener(MyListener(position));

            return convertView;
        }
    }







        public final class ViewHolder {
            public TextView title;
//            public TextView info;
            public Button viewBtn;
        }

        public final class ViewHolder2 {
            public TextView title;
            //            public TextView info;
//            public Button viewBtn;
        }

        public void confirm(int position){

            ImageView img=new ImageView(CandidateList.this);
            img.setImageResource(R.drawable.b);
            new AlertDialog.Builder(this).setView(img)
                    .setTitle("Are you sure you would like to add ")
                    .setMessage(candidatesName[position]+" to the activity?")
                    .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();

            FirebaseDatabase.getInstance().getReference().child("activity").child(aId)
                    .child("candidates").child(candidatesId[position]).removeValue();

           databaseReference = FirebaseDatabase.getInstance().getReference().child("activity").child(aId);











        }




}

