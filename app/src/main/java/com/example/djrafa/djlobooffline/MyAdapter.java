package com.example.djrafa.djlobooffline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context c;
    ArrayList<String> numbers;
    final public ListItemClickListener monClickList;

    public MyAdapter(Context c, ArrayList<String> numbers, ListItemClickListener l) {
        this.c = c;
        this.numbers = numbers;
        monClickList = l;
    }

    public interface ListItemClickListener {

        void onListItemClick(int clickedItemIndex);


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.layout2, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //BIND DATA
        holder.nameTxt.setText(numbers.get(position));

    }


    @Override
    public int getItemCount() {
        return numbers.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView nameTxt;

        public MyViewHolder(View itemView) {
            super(itemView);

            nameTxt = (TextView) itemView.findViewById(R.id.layouts);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clicked_pos = getAdapterPosition();
            monClickList.onListItemClick(clicked_pos);
        }
    }

}