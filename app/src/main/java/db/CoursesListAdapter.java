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
 * Created by audreycelia on 22.04.17.
 */

public class CoursesListAdapter extends BaseAdapter {

        private ArrayList<Course> listData;
        private LayoutInflater layoutInflater;
        private ArrayList<Course> copie;



        public CoursesListAdapter(Context aContext, ArrayList<Course> listData) {
            this.listData = listData;
            layoutInflater = LayoutInflater.from(aContext);
            copie = new ArrayList<Course>();
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


            CoursesListAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.row_course, null);
                holder = new CoursesListAdapter.ViewHolder();
                holder.header = (TextView) convertView.findViewById(R.id.tv_course_header);

                holder.shift = (TextView) convertView.findViewById(R.id.tv_course_date);
                holder.courseName = (TextView) convertView.findViewById(R.id.tv_course_name);
                convertView.setTag(holder);
            } else {
                holder = (CoursesListAdapter.ViewHolder) convertView.getTag();
            }

            holder.shift.setText(listData.get(position).getStart()+" " + listData.get(position).getEnd());
            holder.shift.setBackgroundColor(listData.get(position).getColor());
            holder.courseName.setText(listData.get(position).getName());

            //test if show header
            String actualDay = listData.get(position).getDay();
            String previousDay = null;

            if(position > 0)
            {
                previousDay = listData.get(position-1).getDay();
            }

            if(previousDay == null || !previousDay.equals(actualDay) ) {
                holder.header.setVisibility(View.VISIBLE);
                switch (listData.get(position).getDay()){
                    case "Monday":
                        holder.header.setText(R.string.monday);
                        break;
                    case "Tuesday":
                        holder.header.setText(R.string.tuesday);
                        break;
                    case "Wednesday":
                        holder.header.setText(R.string.wednesday);
                        break;
                    case "Thursday":
                        holder.header.setText(R.string.thursday);
                        break;
                    case "Friday":
                        holder.header.setText(R.string.friday);
                        break;
                    case "Saturday":
                        holder.header.setText(R.string.saturday);
                        break;
                    case "Sunday":
                        holder.header.setText(R.string.sunday);
                        break;
                }
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
            for (Course searchCourse : copie) {
                if (searchCourse.getName().toLowerCase().contains(charText)) {
                    listData.add(searchCourse);
                }
            }
        }
        notifyDataSetChanged();
    }


    static class ViewHolder {
    TextView header;
    TextView shift;
    TextView courseName;



}
}
