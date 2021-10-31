package com.comp90018.JoinMe;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Intent;




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

public class ParticipantsList extends AppCompatActivity {
    NavigationBarView bottomNavigationView;
    Bundle bundle;
    Activity activity = new Activity();
    HashMap activityInfo;
    private String aId;

    public static String candidatesId[]= new String[]{}; // candidates_id
    public static String candidatesName[]= new String[]{};  // candidate_name
    private List<String> participants = new ArrayList<>(1);
    private List<String> candidates = new ArrayList<>(1);

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
    private String autoJoin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participants_list);

        Bundle bundle = getIntent().getExtras();
        HashMap activityInfo = (HashMap) bundle.get("activityInfo");
        object.Activity activity = new Activity();
        activity.mapToActivity(activity, activityInfo);
        autoJoin = String.valueOf(activity.isAutoJoin());

        if (autoJoin == "false" && activity.getCandidates() != null && !activity.getCandidates().isEmpty()){
            candidatesId = activity.getCandidates().toArray(new String[0]);}

        if (activity.getParticipants() != null && !activity.getParticipants().isEmpty()) {
            participatesId = activity.getParticipants().toArray(new String[0]);
        }
        aId = activity.getAid();

        if (activity.getParticipants() != null && !activity.getParticipants().isEmpty()) {
            participants = activity.getParticipants();
        }
        if (activity.getCandidates() != null && !activity.getCandidates().isEmpty()) {
            candidates= activity.getCandidates();
        }




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


//             adapter.notifyDataSetChanged();

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
        if (autoJoin == "false" && activity.getCandidates() != null && !activity.getCandidates().isEmpty()){
            String[] temp = new String[candidatesId.length];

            for (int i = 0; i < candidatesId.length; i++) {

                for (int j = 0; j< user_id_array.length;j++){
                    if (candidatesId[i].equals(user_id_array[j])) {
                        temp[i] = user_list_array[j];
                    }
                }
            }
            candidatesName = temp;

            databaseReference = FirebaseDatabase.getInstance().getReference().child("activity");
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for(int i=0;i<candidatesId.length;i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("candidatesName", candidatesName[i]);
                list.add(map);
            }

            mData = list;

//            ListView listView = (ListView) findViewById(R.id.listView);
//            MyAdapter adapter = new MyAdapter(this);
//            listView.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
        }


        // participants
        if (activity.getParticipants() != null && !activity.getParticipants().isEmpty()) {
            String[] temp2 = new String[participatesId.length];

            for (int i = 0; i < participatesId.length; i++) {

                for (int j = 0; j < user_id_array.length; j++) {
                    if (participatesId[i].equals(user_id_array[j])) {
                        temp2[i] = user_list_array[j];
                    }
                }
            }
            participatesName = temp2;

            databaseReference = FirebaseDatabase.getInstance().getReference().child("activity");
            List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
            for(int i=0;i<participatesId.length;i++){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("participatesName", participatesName[i]);
                list2.add(map);
            }
            mData2  = list2;

            ListView listView2 = (ListView) findViewById(R.id.list_view2);
            MyAdapter2 adapter2 = new MyAdapter2(this);
            listView2.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();

        }




    }




//    public class MyAdapter extends BaseAdapter {
//
//        private final LayoutInflater mInflater;
//
//        public MyAdapter(Context context) {
//            this.mInflater = LayoutInflater.from(context);
//        }
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return mData.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            // TODO Auto-generated method stub
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            // TODO Auto-generated method stub
//            return 0;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
////            ViewHolder holder = new ViewHolder();
////            if (convertView == null) {
////                //可以理解为从vlist获取view  之后把view返回给ListView
////                convertView = mInflater.inflate(R.layout.vlist, null);
////                holder.title = (TextView) convertView.findViewById(R.id.title);
////                holder.viewBtn = (Button) convertView.findViewById(R.id.view_btn);
////                convertView.setTag(holder);
////
////
////            }
////            else {
////                holder = (ViewHolder) convertView.getTag();
////            }
////            holder.title.setText((String) mData.get(position).get("candidatesName"));
////            holder.viewBtn.setTag(position);
////            holder.viewBtn.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    confirm(position);
////                }
////            });
////
////            //holder.viewBtn.setOnClickListener(MyListener(position));
////            return convertView;
//
//            convertView = mInflater.inflate(R.layout.vlist, null);
//            TextView title = (TextView) convertView.findViewById(R.id.title);
//            Button viewBtn = (Button) convertView.findViewById(R.id.view_btn);
//
//            title.setText((String) mData.get(position).get("candidatesName"));
//            viewBtn.setTag(position);
//            viewBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    confirm(position);
//                }
//            });
//
//            return convertView;
//
//
//        }
//    }

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
//            ViewHolder2 holder = new ViewHolder2();
//            if (convertView == null) {
//
//                //可以理解为从vlist获取view  之后把view返回给ListView
//                convertView = mInflater.inflate(R.layout.vlist_2, null);
//                holder.title = (TextView) convertView.findViewById(R.id.title);
//
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder2) convertView.getTag();
//            }
//
//            holder.title.setText((String) mData2.get(position).get("participatesName"));
//
//
//            return convertView;

            convertView = mInflater.inflate(R.layout.vlist_2, null);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            title.setText((String) mData2.get(position).get("participatesName"));
            return convertView;




        }
    }


//    public final class ViewHolder {
//        public TextView title;
//        public Button viewBtn;
//    }

    public final class ViewHolder2 {
        public TextView title;
    }

//    public void confirm(int position){
//
//        ImageView img=new ImageView(ParticipantsList.this);
//        new AlertDialog.Builder(this).setView(img)
//                .setTitle("Are you sure you would like to add ")
//                .setMessage(candidatesName[position]+" to the activity?")
//                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        participants.add(candidatesId[position]);
//                        candidates.remove(position);
//                        FirebaseDatabase.getInstance().getReference().child("activity").child(aId).child("participants").setValue(participants);
//                        FirebaseDatabase.getInstance().getReference().child("activity").child(aId).child("candidates").setValue(candidates);
//
//                        recreate();
//
//                    }
//                })
//                .show();
//
//
//
//
//    }


}



