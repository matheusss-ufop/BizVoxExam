package com.android.bizvoxexam;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ShotsListAdapter extends ArrayAdapter<ShotItem> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<ShotItem> data = new ArrayList<>();

    public ShotsListAdapter (Context context, int layoutResourceId, ArrayList<ShotItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) row.findViewById(R.id.title);
            holder.views_count = (TextView) row.findViewById(R.id.views_count);
            holder.created_at = (TextView) row.findViewById(R.id.created_at);
            holder.image = (ImageView) row.findViewById(R.id.imageView);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ShotItem item = data.get(position);
        holder.title.setText(item.getTitle());
        holder.views_count.setText(item.getViewsCount() + " views");
        holder.created_at.setText(item.getCreatedAt());
        holder.image.setImageBitmap(item.getImage());

        return row;
    }

    static class ViewHolder {
        TextView title;
        TextView views_count;
        TextView created_at;
        ImageView image;
    }


}