package helper;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comp90018.JoinMe.R;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> maintitle = new ArrayList<>();
    private ArrayList<String> subtitle = new ArrayList<>();

    public MyListAdapter(Activity context, ArrayList<String> maintitle, ArrayList<String> subtitle) {
        super(context, R.layout.activity_list_view, maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=subtitle;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.activity_list_view, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        titleText.setText(maintitle.get(position));
        subtitleText.setText(subtitle.get(position));

        return rowView;

    };
}
