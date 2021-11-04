package com.comp90018.JoinMe;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
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

public class CandidateList extends AppCompatActivity {
    NavigationBarView bottomNavigationView;
    Bundle bundle;
    Activity activity = new Activity();
    HashMap activityInfo;
    private String aId;

    public  String candidatesId[]= new String[]{}; // candidates_id
    public  String candidatesId1[]= new String[]{}; // candidates_id
    public  String candidatesName[]= new String[]{};  // candidate_name
    public List<String> participants = new ArrayList<String>(1);
    public List<String> candidates = new ArrayList<String>(1);


    public static String participatesId[]= new String[]{}; // participates_id
    public static String participatesName[]= new String[]{};  // participates_name

    public static String user_list_array[] = new String[]{};
    public static String user_id_array[] = new String[]{};

    private List<Map<String, Object>> mData;
    private List<Map<String, Object>> mData2;
    private View convertView;

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
        setContentView(R.layout.candidate_list);

        Bundle bundle = getIntent().getExtras();
        HashMap activityInfo = (HashMap) bundle.get("activityInfo");
        object.Activity activity = new Activity();
        activity.mapToActivity(activity, activityInfo);
        autoJoin = String.valueOf(activity.isAutoJoin());
        aId = activity.getAid();



        if (activity.getParticipants() != null && !activity.getParticipants().isEmpty()) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("activity").child(aId).child("participants");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {

                    String value = dataSnapshot.getValue(String.class);
                    participants.add(value);
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

        }



        if (activity.getCandidates() != null && !activity.getCandidates().isEmpty()) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("activity").child(aId).child("candidates");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    @SuppressWarnings("unchecked")

                    String value = dataSnapshot.getValue(String.class);
                    candidates.add(value);
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

        }



        if (autoJoin == "false" && activity.getCandidates() != null && !activity.getCandidates().isEmpty()){
            candidatesId = activity.getCandidates().toArray(new String[0]);
            candidatesId1 = candidates.toArray(new String[0]);
        }


        if (activity.getParticipants() != null && !activity.getParticipants().isEmpty()) {
            participatesId = activity.getParticipants().toArray(new String[0]);
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

            ListView listView = (ListView) findViewById(R.id.listView);
            MyAdapter adapter = new MyAdapter(CandidateList.this, candidatesName);
            listView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(listView);
            adapter.notifyDataSetChanged();
        }

    }




    public class MyAdapter extends BaseAdapter {

        private final LayoutInflater mInflater;
        private Context context;
        private String[] candidatesName;

        public MyAdapter(Context context, String[] candidatesName) {
            this.mInflater = LayoutInflater.from(context);
            this.context = context;
            this.candidatesName = candidatesName;
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

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = mInflater.inflate(R.layout.vlist, null);
            convertView = inflater.inflate(R.layout.vlist, null);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            Button viewBtn = (Button) convertView.findViewById(R.id.view_btn);


//            title.setText((String) mData.get(position).get("candidatesName"));
            title.setText((String) candidatesName[position]);
            viewBtn.setTag(position);
            viewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirm(position);
                }
            });

            return convertView;


        }
    }


    public final class ViewHolder {
        public TextView title;
        public Button viewBtn;
    }



    public void confirm(int position){

        ImageView img=new ImageView(CandidateList.this);
        new AlertDialog.Builder(this).setView(img)
                .setTitle("Are you sure you would like to add ")
                .setMessage(candidatesName[position]+" to the activity?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        participants.add(candidatesId[position]);
                        candidates.remove(position);
                        FirebaseDatabase.getInstance().getReference().child("activity").child(aId).child("participants").setValue(participants);
                        FirebaseDatabase.getInstance().getReference().child("activity").child(aId).child("candidates").setValue(candidates);
                        recreate();

                    }
                })
                .show();

    }

    public void setListViewHeightBasedOnChildren(ListView activityListView) {
        ListAdapter listAdapter = activityListView.getAdapter();
        if (listAdapter == null) {
            //pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, activityListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = activityListView.getLayoutParams();
        params.height = totalHeight + (activityListView.getDividerHeight() * (listAdapter.getCount() - 1));
        activityListView.setLayoutParams(params);
        activityListView.requestLayout();
    }


}




