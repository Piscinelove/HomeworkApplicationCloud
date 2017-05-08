package db;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.audreycelia.homeworkapp.R;

import java.util.ArrayList;

/**
 * Created by audreycelia on 23.04.17.
 */

public class HomeworksListAdapter extends BaseAdapter {

    private ArrayList<Homework> listData;
    private LayoutInflater layoutInflater;
    private ArrayList<Homework> copie;

    public HomeworksListAdapter(Context aContext, ArrayList<Homework> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
        copie = new ArrayList<Homework>();
        copie.addAll(listData);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        HomeworksListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_homework, null);
            holder = new HomeworksListAdapter.ViewHolder();
            holder.header = (TextView) convertView.findViewById(R.id.tv_homework_header);
            holder.date = (ImageView) convertView.findViewById(R.id.iv_homework_image);
            holder.homeworkName = (TextView) convertView.findViewById(R.id.tv_homework_name);
            convertView.setTag(holder);
        } else {
            holder = (HomeworksListAdapter.ViewHolder) convertView.getTag();
        }

        if(listData.get(position).isDone())
        {
            holder.date.setBackgroundColor(ContextCompat.getColor(convertView.getContext(),R.color.done));
            holder.date.setImageResource(R.drawable.ic_save);
        }
        else {
            holder.date.setBackgroundColor(ContextCompat.getColor(convertView.getContext(),R.color.colorAccent));
            holder.date.setImageResource(R.drawable.ic_todo);
        }

        holder.homeworkName.setText(listData.get(position).getName());

        //test if show header
        String actualDate = listData.get(position).getDeadline();
        String previousDate = null;

        if(position > 0)
        {
            previousDate = listData.get(position-1).getDeadline();
        }

        if(previousDate == null || !previousDate.equals(actualDate) ) {
            holder.header.setVisibility(View.VISIBLE);
            holder.header.setText(listData.get(position).getDeadline());
        }
        else
            holder.header.setVisibility(View.GONE);

        return convertView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase();
        listData.clear();
        if (charText.length() == 0) {
            listData.addAll(copie);
        } else {
            for (Homework searchHomework : copie) {
                if (searchHomework.getName().toLowerCase().contains(charText)) {
                    listData.add(searchHomework);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView header;
        ImageView date;
        TextView homeworkName;

    }
}
