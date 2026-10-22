package adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.ChatsActivity;
import com.example.chatapp.Details;
import com.example.chatapp.R;
import com.example.chatapp.User;
import com.example.chatapp.listview;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<Details> detailsArrayList;
    static Context context;

    public RecyclerViewAdapter(Context context, ArrayList<Details> detailsArrayList) {
        this.context = context;
        this.detailsArrayList = detailsArrayList;
    }

    // Where to get the single card as viewholder object
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }
    // What will happen after we create the viewholder object
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Details details = detailsArrayList.get(position);

        if("default".equals(details.getImgUrl())){
            holder.profilePic.setImageResource(R.drawable.user);
        }
        else{
            Glide.with(context).load(details.getImgUrl()).into(holder.profilePic);
        }

        holder.name.setText(details.getName());

        String lastMessage = details.getLastMessage();
        Long time = details.getTime();

        if(lastMessage != null){
            holder.message.setText(lastMessage);
        }
        else{
            holder.message.setText("");
        }

        if(time != null){
            holder.time.setText(formatTime(time));
        }
        else{
            holder.time.setText("");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatsActivity.class);
                intent.putExtra("receiverId", details.getUid());
                context.startActivity(intent);
            }
        });
    }
    private String formatTime(Long time){
        if(time == null){
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(new Date(time));
    }
    // How many items?
    @Override
    public int getItemCount() {
        return detailsArrayList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView message, time;
        public ImageView profilePic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            profilePic = itemView.findViewById(R.id.profilePic);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
