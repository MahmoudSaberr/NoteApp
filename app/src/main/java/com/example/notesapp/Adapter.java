package com.example.notesapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>implements Filterable {
    Context context;
    Activity activity;
    ArrayList<Model> notesList;
    ArrayList<Model> newList;

    public Adapter(Context context, Activity activity, ArrayList<Model> notesList) {
        this.context = context;
        this.activity = activity;
        this.notesList = notesList;
        newList =new ArrayList<>(notesList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Model note = notesList.get(position);
        holder.title.setText(note.getTitle());
        holder.description.setText(note.getDescription());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateNotesActivity.class);
                intent.putExtra("title",notesList.get(position).getTitle());
                intent.putExtra("description",notesList.get(position).getDescription());
                intent.putExtra("id",notesList.get(position).getId());

                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //take a new list that will store the filtered items
            ArrayList<Model> filteredList = new ArrayList<>();

            //now if you will search something and the text is empty or length is equal to zero
            if(constraint==null||constraint.length()==0)
            {
                //then filtered List will contain all items from the new list
                filteredList.addAll(newList);
            }
            else
            {
                //take a string and store the search string to it
                String filterPattern = constraint.toString().toLowerCase().trim();

                //now loop for the whole new list
                for (Model item:newList)
                {
                    if(item.getTitle().toLowerCase().contains(filterPattern))
                    {
                        //that list item will be added to our filtered list
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        //now inside publish result method we will publish these results
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            notesList.clear(); //first clear the notes list
            notesList.addAll((Collection<? extends Model>) results.values);//add a new items
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,description;
        RelativeLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rv_tv_title);
            description = itemView.findViewById(R.id.rv_tv_description);
            layout = itemView.findViewById(R.id.rv_relative_layout);
        }
    }

    public ArrayList<Model> getList(){
        return notesList;
    }

    public void removeItem(int position)
    {
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Model item ,int position)
    {
        notesList.add(position,item);
        notifyItemInserted(position);
    }
}
