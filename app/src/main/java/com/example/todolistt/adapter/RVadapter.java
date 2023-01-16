package com.example.todolistt.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistt.R;
import com.example.todolistt.getsetgo.getset;
import com.example.todolistt.handler.DBHandler;

import java.util.ArrayList;

public class RVadapter extends RecyclerView.Adapter<RVadapter.ViewHolder> {


    private ArrayList<getset> taskList;
    ArrayList<getset> backup;
    private Context context;

    public RVadapter(ArrayList<getset> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
        backup = new ArrayList<>(taskList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        getset modal = taskList.get(position);
        holder.TaskTV.setText(modal.getItem());

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation!");
                builder.setMessage("Are you sure to delete "+modal.getItem()+" ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBHandler dbHandler = new DBHandler(context);
                        int result = dbHandler.deletion(modal.getItem());

                        if(result>0){
                            Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                            taskList.remove(modal);
                            notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(context,"Failed to delete",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("No",null);
                builder.show();
            }
        });
    }

    public void delete(int position){
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public Filter getFilter() {
        return filter;
    }
    //background thread
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            ArrayList<getset> filtereddata = new ArrayList<>();
            if(keyword.toString().isEmpty())
                filtereddata.addAll(backup);
            else{
                for(getset obj : backup){
                    if(obj.getItem().toString().toLowerCase().contains(keyword.toString().toLowerCase()))
                        filtereddata.add(obj);
                }
            }
            FilterResults results = new FilterResults();
            results.values=filtereddata;
            return results;
        }
        //main ui thread
        @Override
        protected void publishResults(CharSequence keyword, FilterResults results) {
            taskList.clear();
            taskList.addAll((ArrayList<getset>)results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView TaskTV;
        private RVadapter rVadapter;
        private ImageView del;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            TaskTV = itemView.findViewById(R.id.taskId);
            del = itemView.findViewById(R.id.del);
        }
    }
}
