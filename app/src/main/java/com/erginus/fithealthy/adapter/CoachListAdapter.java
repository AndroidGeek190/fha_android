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

import com.erginus.fithealthy.model.CoachesModel;
import com.erginus.fithealthy.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by paramjeet on 11/6/15.
 */
public class CoachListAdapter extends BaseAdapter {
    private List<CoachesModel> discussion;
    private final Context context;

    public CoachListAdapter(Context context, List<CoachesModel> discsn) {

        this.context = context;
        this.discussion = discsn;
    }

    @Override
    public int getCount() {
        return discussion.size();
    }

    @Override
    public Object getItem(int position) {
        return discussion.get(position);
    }

    @Override
    public long getItemId(int position) {
        return discussion.indexOf(discussion.get(position));
    }

    public class ViewHolder {
        TextView coachFname, coachLname;
        ImageView image;
        TextView description;
        TextView rating;
        RatingBar ratingBar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_coaches_item, parent, false);
        }


        holder = new ViewHolder();
        holder.coachFname = (TextView) convertView.findViewById(R.id.txtvw_frstName);
        holder.coachLname = (TextView) convertView.findViewById(R.id.txtvw_lastname);
        holder.image = (ImageView) convertView.findViewById(R.id.img_icon);
        holder.description = (TextView) convertView.findViewById(R.id.txtvw_splztn_descptn);
        holder.rating = (TextView) convertView.findViewById(R.id.txtcount);
        holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);

        holder.coachFname.setText(discussion.get(position).getFName());
        holder.coachLname.setText(discussion.get(position).getLName());
        Picasso.with(context).load(discussion.get(position).getImage()).into(holder.image);
        holder.description.setText(discussion.get(position).getDescription());
        holder.rating.setText("("+discussion.get(position).getRatingCount()+")");
        holder.ratingBar.setRating(Float.parseFloat(discussion.get(position).getRatingAverage()));
        return convertView;
    }
}