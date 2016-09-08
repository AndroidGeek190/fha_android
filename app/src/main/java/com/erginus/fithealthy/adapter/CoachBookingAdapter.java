package com.erginus.fithealthy.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.erginus.fithealthy.R;
import com.erginus.fithealthy.model.MyBookingModel;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by paramjeet on 16/6/15.
 */
public class CoachBookingAdapter extends BaseAdapter {
    private List<MyBookingModel> myBooking;
    private final Context context;
    SimpleDateFormat dateFormat, format;
     public CoachBookingAdapter(Context context, List<MyBookingModel> booking) {

        this.context = context;
        this.myBooking = booking;
    }

    @Override
    public int getCount() {
        return myBooking.size();
    }

    @Override
    public Object getItem(int position) {
        return myBooking.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myBooking.indexOf(myBooking.get(position));
    }
    public class ViewHolder {
        TextView coachFname;
        ImageView image;
        TextView avail_for;
        TextView sdate, endate;

        TextView length;

         }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder = null;
       dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format=new SimpleDateFormat("dd-MM-yyyy");

        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.coach_list_mybooking_item,parent,false);

        }
        holder=new ViewHolder();
        holder.coachFname = (TextView) convertView.findViewById(R.id.tv_chname_mybkng);
        holder.image = (ImageView) convertView.findViewById(R.id.img_cir);
        holder.avail_for= (TextView) convertView.findViewById(R.id.txtvw_vdcall);
        holder.sdate = (TextView) convertView.findViewById(R.id.txtvw_gmt1);
        holder.endate = (TextView) convertView.findViewById(R.id.txtvw_gmt);
        holder.length=(TextView)convertView.findViewById(R.id.txtvw_no_hour);
        holder.coachFname.setText(myBooking.get(position).getFName()+" "+myBooking.get(position).getLName());
        String type=myBooking.get(position).getAvail_for();
        if(type.equals("1"))
        {
            holder.avail_for.setText("1-2-1 Chat");

        }
        if(type.equals("2"))
        {
            holder.avail_for.setText("1-2-1 Video Coaching");

        }
        if(type.equals("3"))
        {
            holder.avail_for.setText("Group Video Coaching");

        }
        Picasso.with(context).load(myBooking.get(position).getImage()).into(holder.image);
        holder.length.setText((myBooking.get(position).getLength())+" "+"Minutes");
        String date=myBooking.get(position).getStartDt();
        String edate=myBooking.get(position).getEndDt();

        holder.sdate.setText(date);
        holder.endate.setText(edate);
        return convertView;
    }


}