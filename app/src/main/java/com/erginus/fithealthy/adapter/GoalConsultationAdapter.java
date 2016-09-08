package com.erginus.fithealthy.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.erginus.fithealthy.model.GoalConsultationModel;
import com.erginus.fithealthy.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by paramjeet on 3/6/15.
 */
public class GoalConsultationAdapter extends BaseAdapter {
    public  int count=1;
    private List<GoalConsultationModel> discussion;
    private Context context;

    public GoalConsultationAdapter(Context context, List<GoalConsultationModel> discsn) {

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
        TextView question;
        ImageView image ;
        TextView answer ;
        TextView no_of_ans ;
        TextView time;
        TextView read;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){
            convertView=inflater.inflate(R.layout.list_goal_consltn_item, parent, false);
        }
       holder=new ViewHolder();
        holder.question = (TextView) convertView.findViewById(R.id.txtvw_qstn);
        holder.image = (ImageView) convertView.findViewById(R.id.img_poster);
        holder.answer = (TextView) convertView.findViewById(R.id.txtvw_ans);
        holder.no_of_ans = (TextView) convertView.findViewById(R.id.txtvw_no_ans);
        holder.time = (TextView) convertView.findViewById(R.id.txtvw_time_no);
        holder.read=(TextView)convertView.findViewById(R.id.read_more);
        holder.question.setText(discussion.get(position).getTopic());
       Picasso.with(context).load(discussion.get(position).getImage()).into(holder.image);
       holder.answer.setText(discussion.get(position).getQuestion());
        holder.no_of_ans.setText(discussion.get(position).getQuesAnsCount()+" "+"answers-"+" ");
        holder.time.setText(discussion.get(position).getQuesTime());
        return convertView;
    }

}
