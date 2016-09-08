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

import com.erginus.fithealthy.helper.GlobalPrefrences;
import com.erginus.fithealthy.helper.PrefsHelper;
import com.erginus.fithealthy.model.AnswersModel;
import com.erginus.fithealthy.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by paramjeet on 11/6/15.
 */
public class AnswersAdapter  extends BaseAdapter {
    private List<AnswersModel> discussion;
    private final Context context;
    GlobalPrefrences prefsHelper;

    public AnswersAdapter(Context context, List<AnswersModel> discsn) {

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

    public class ViewHolder
    {
        TextView usrNm,answr,rating, coachNm, count;
        ImageView image ;
        RatingBar ratingBar;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){
            convertView=inflater.inflate(R.layout.list_answers_item, parent, false);
        }
        prefsHelper=new GlobalPrefrences(context);
        holder=new ViewHolder();
        holder.usrNm= (TextView) convertView.findViewById(R.id.txtvw_ins_name);
        holder.coachNm= (TextView) convertView.findViewById(R.id.txttalk);

        holder.image = (ImageView) convertView.findViewById(R.id.imgvw_ans);
        holder.answr = (TextView) convertView.findViewById(R.id.txtvw_answrs);
        holder.rating = (TextView) convertView.findViewById(R.id.ratetxt1);
        holder.ratingBar=(RatingBar)convertView.findViewById(R.id.ratingBar2);
        holder.usrNm.setText(discussion.get(position).getFname()+" "+discussion.get(position).getLname());
        if(prefsHelper.getGroupSlug().equals("user")) {
            holder.coachNm.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.coachNm.setVisibility(View.GONE);
        }
        holder.coachNm.setText("Talk to" + " " + discussion.get(position).getFname() + " " + discussion.get(position).getLname());
        Picasso.with(context).load(discussion.get(position).getImage()).into(holder.image);
        holder.answr.setText(discussion.get(position).getAnswer());
        holder.rating.setText("(" + discussion.get(position).getRatingCount() + ")");
        holder.ratingBar.setRating(Float.parseFloat(discussion.get(position).getRatingAverage()));


        return convertView;
    }

}

