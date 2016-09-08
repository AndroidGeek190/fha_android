package com.erginus.fithealthy.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.erginus.fithealthy.R;
import com.erginus.fithealthy.model.TrackingProgressModel;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by paramjeet on 13/6/15.
 */
public class TrackingProgressAdapter extends BaseAdapter {
    public  int count=1;
    private List<TrackingProgressModel> tracking;
    private Context context;

    public TrackingProgressAdapter(Context context, List<TrackingProgressModel> discsn) {

        this.context = context;
        this.tracking = discsn;
    }


    @Override
    public int getCount() {
        return tracking.size();
    }

    @Override
    public Object getItem(int position) {
        return tracking.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tracking.indexOf(tracking.get(position));
    }

    public class ViewHolder
    {
        TextView trckngdate,weight,bmi, bdyfat;
        ImageView image,arrow ;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){
            convertView=inflater.inflate(R.layout.list_tracking_progress_item, parent, false);
        }

        holder=new ViewHolder();
        holder.trckngdate = (TextView) convertView.findViewById(R.id.txtvw_date_trck);
        holder.image = (ImageView) convertView.findViewById(R.id.image_crcl_trck);
        holder.weight = (TextView) convertView.findViewById(R.id.txtvw_kg);
        holder.bmi = (TextView) convertView.findViewById(R.id.txtvw_bmi_no);
        holder.bdyfat= (TextView) convertView.findViewById(R.id.txtvw_prcnt);
        holder.arrow=(ImageView)convertView.findViewById(R.id.bckarrwimage);
        holder.trckngdate.setText(tracking.get(position).getTrackingDate());
        Picasso.with(context).load(tracking.get(position).getImage()).into(holder.image);
        holder.weight.setText(tracking.get(position).getWeight());
        holder.bmi.setText(tracking.get(position).getBmi());
        holder.bdyfat.setText(tracking.get(position).getBodyFat());
        return convertView;
    }

}
