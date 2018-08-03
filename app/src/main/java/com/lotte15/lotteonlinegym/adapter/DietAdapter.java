package com.lotte15.lotteonlinegym.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lotte15.lotteonlinegym.R;
import com.lotte15.lotteonlinegym.type.DietItem;

import java.util.ArrayList;
import java.util.List;

public class DietAdapter  extends ArrayAdapter<DietItem> {

    private Context context;
    private LayoutInflater inflater;
    private int resource;
    public List<DietItem> dietItems = new ArrayList<DietItem>();

    public DietAdapter(Context c, int resource, ArrayList<DietItem> items){
        super(c, resource, items);
        this.context = c;
        this.resource = resource;
        this.inflater = LayoutInflater.from(c);
        dietItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            holder.foodTitle = (TextView) convertView.findViewById(R.id.food_title);
            holder.foodDescription = (TextView) convertView.findViewById(R.id.food_description);
            holder.foodImage = (ImageView) convertView.findViewById(R.id.food_image);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        DietItem diet = getItem(position);

        holder.foodTitle.setText(diet.foodTitle);
        holder.foodDescription.setText(diet.foodDescription);

        Glide.with(context)
                .load(R.drawable.img_symbol_logo)
                .apply(new RequestOptions().circleCrop())
                .into(holder.foodImage);


        return convertView;
    }

    static class ViewHolder{
        ImageView foodImage;
        TextView foodTitle;
        TextView foodDescription;
    }

}
