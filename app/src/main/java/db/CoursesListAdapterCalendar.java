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

public class CoursesListAdapterCalendar extends BaseAdapter {

        private ArrayList<Course> listData;
        private LayoutInflater layoutInflater;



        public CoursesListAdapterCalendar(Context aContext, ArrayList<Course> listData) {
            this.listData = listData;
            layoutInflater = LayoutInflater.from(aContext);
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


            CoursesListAdapterCalendar.ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.row_course, null);
                holder = new CoursesListAdapterCalendar.ViewHolder();
                holder.header = (TextView) convertView.findViewById(R.id.tv_course_header);
                holder.shift = (TextView) convertView.findViewById(R.id.tv_course_date);
                holder.courseName = (TextView) convertView.findViewById(R.id.tv_course_name);
                convertView.setTag(holder);
            } else {
                holder = (CoursesListAdapterCalendar.ViewHolder) convertView.getTag();
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
                holder.header.setText(R.string.courses);
            }
            else
                holder.header.setVisibility(View.GONE);



            return convertView;
        }

static class ViewHolder {
    TextView header;
    TextView shift;
    TextView courseName;



}
}
