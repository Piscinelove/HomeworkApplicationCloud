package db;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rafael Peixoto on 22.04.2017.
 */

public class SpinnerTeachersListAdapter extends ArrayAdapter<Teacher> {
    private ArrayList<Teacher> listData;
    private Context context;

    public SpinnerTeachersListAdapter(Context context, int textViewResourceId, ArrayList<Teacher> listData) {
        super(context, textViewResourceId, listData);
        this.listData = listData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Teacher getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(listData.get(position).getFirstName()+" "+listData.get(position).getLastName());
        label.setTextSize(18);

        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(listData.get(position).getFirstName()+" "+listData.get(position).getLastName());
        label.setTextSize(18);
        return label;
    }
}

