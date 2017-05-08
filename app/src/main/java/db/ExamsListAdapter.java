package db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.audreycelia.homeworkapp.R;

import java.util.ArrayList;

/**
 * Created by audreycelia on 23.04.17.
 */

public class ExamsListAdapter extends BaseAdapter {

    private ArrayList<Exam> listData;
    private LayoutInflater layoutInflater;
    private ArrayList<Exam> copie;

    public ExamsListAdapter(Context aContext, ArrayList<Exam> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
        copie = new ArrayList<Exam>();
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

        ExamsListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_exam, null);
            holder = new ExamsListAdapter.ViewHolder();
            holder.header = (TextView) convertView.findViewById(R.id.tv_exam_header);
            holder.shift = (TextView) convertView.findViewById(R.id.tv_exam_date);
            holder.examName = (TextView) convertView.findViewById(R.id.tv_exam_name);
            convertView.setTag(holder);
        } else {
            holder = (ExamsListAdapter.ViewHolder) convertView.getTag();
        }

        holder.shift.setText(listData.get(position).getStart()+" " + listData.get(position).getEnd());
        holder.examName.setText(listData.get(position).getName());

        //test if show header
        String actualDate = listData.get(position).getDate();
        String previousDate = null;

        if(position > 0)
        {
            previousDate = listData.get(position-1).getDate();
        }

        if(previousDate == null || !previousDate.equals(actualDate) ) {
            holder.header.setVisibility(View.VISIBLE);
            holder.header.setText(listData.get(position).getDate());
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
            for (Exam searchExam : copie) {
                if (searchExam.getName().toLowerCase().contains(charText)) {
                    listData.add(searchExam);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView header;
        TextView shift;
        TextView examName;

    }
}
