package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Details;
import com.example.chatapp.MessageModel;
import com.example.chatapp.R;
import com.example.chatapp.VideoActivity;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final Context context;
    private final String currentUserId;
    private final ArrayList<MessageModel> messageList;

    private String senderImgUrl;
    private String receiverImgUrl;

    private static final int SENT = 1;
    private static final int RECEIVED = 2;

    public MessageAdapter(
            Context context,
            ArrayList<MessageModel> messageList,
            String senderImgUrl,
            String receiverImgUrl,
            String currentUserId
    ) {
        this.context = context;
        this.messageList = messageList;
        this.senderImgUrl = senderImgUrl;
        this.receiverImgUrl = receiverImgUrl;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {

        MessageModel message = messageList.get(position);

        if (message.getSenderId() != null &&
                message.getSenderId().equals(currentUserId)) {

            return SENT;

        } else {
            return RECEIVED;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view;

        if (viewType == SENT) {

            view = LayoutInflater.from(context)
                    .inflate(R.layout.sender_chat_row, parent, false);

        } else {

            view = LayoutInflater.from(context)
                    .inflate(R.layout.receiver_chat_row, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        MessageModel message = messageList.get(position);

        String mediaType = message.getMediaType();
        String mediaUrl = message.getMediaUrl();

        Log.d("CHAT_MEDIA",
                "Position = " + position +
                        " | Type = " + mediaType +
                        " | URL = " + mediaUrl);

        /*
         * =========================================================
         * HIDE ALL MESSAGE CONTENT FIRST
         * =========================================================
         */

        if (holder.txtMessage != null) {
            holder.txtMessage.setVisibility(View.GONE);
        }

        if (holder.senderImageView != null) {
            holder.senderImageView.setVisibility(View.GONE);
        }

        if (holder.receiverImageView != null) {
            holder.receiverImageView.setVisibility(View.GONE);
        }

        if (holder.senderVideoContainer != null) {
            holder.senderVideoContainer.setVisibility(View.GONE);
        }

        if (holder.receiverVideoContainer != null) {
            holder.receiverVideoContainer.setVisibility(View.GONE);
        }


        /*
         * =========================================================
         * TEXT MESSAGE
         * =========================================================
         */

        if (mediaType == null || mediaType.trim().isEmpty()) {

            if (holder.txtMessage != null) {

                holder.txtMessage.setVisibility(View.VISIBLE);

                String text = message.getMessage();

                if (text != null) {
                    holder.txtMessage.setText(text);
                } else {
                    holder.txtMessage.setText("");
                }
            }
        }


        /*
         * =========================================================
         * IMAGE MESSAGE
         * =========================================================
         */

        else if (mediaType.equalsIgnoreCase("image")) {

            if (mediaUrl != null && !mediaUrl.trim().isEmpty()) {

                if (message.getSenderId() != null &&
                        message.getSenderId().equals(currentUserId)) {

                    // SENDER IMAGE

                    if (holder.senderImageView != null) {

                        holder.senderImageView.setVisibility(View.VISIBLE);

                        Glide.with(context)
                                .load(mediaUrl)
                                .centerInside()
                                .into(holder.senderImageView);
                    }

                } else {

                    // RECEIVER IMAGE

                    if (holder.receiverImageView != null) {

                        holder.receiverImageView.setVisibility(View.VISIBLE);

                        Glide.with(context)
                                .load(mediaUrl)
                                .centerInside()
                                .into(holder.receiverImageView);
                    }
                }
            }
        }


        /*
         * =========================================================
         * VIDEO MESSAGE
         * =========================================================
         */

        else if (mediaType.equalsIgnoreCase("video")) {

            if (mediaUrl != null && !mediaUrl.trim().isEmpty()) {

                // Generate Cloudinary thumbnail URL
                String thumbnailUrl = createCloudinaryThumbnail(mediaUrl);

                Log.d("VIDEO_THUMBNAIL",
                        "Video URL = " + mediaUrl);

                Log.d("VIDEO_THUMBNAIL",
                        "Thumbnail URL = " + thumbnailUrl);


                /*
                 * =================================================
                 * SENDER VIDEO
                 * =================================================
                 */

                if (message.getSenderId() != null &&
                        message.getSenderId().equals(currentUserId)) {

                    if (holder.senderVideoContainer != null &&
                            holder.senderVideoThumbnail != null) {

                        holder.senderVideoContainer
                                .setVisibility(View.VISIBLE);

                        Glide.with(context)
                                .load(thumbnailUrl)
                                .centerCrop()
                                .into(holder.senderVideoThumbnail);


                        holder.senderVideoContainer
                                .setOnClickListener(v -> {

                                    openVideo(mediaUrl);

                                });
                    }

                }


                /*
                 * =================================================
                 * RECEIVER VIDEO
                 * =================================================
                 */

                else {

                    if (holder.receiverVideoContainer != null &&
                            holder.receiverVideoThumbnail != null) {

                        holder.receiverVideoContainer
                                .setVisibility(View.VISIBLE);

                        Glide.with(context)
                                .load(thumbnailUrl)
                                .centerCrop()
                                .into(holder.receiverVideoThumbnail);


                        holder.receiverVideoContainer
                                .setOnClickListener(v -> {

                                    openVideo(mediaUrl);

                                });
                    }
                }
            }
        }


        /*
         * =========================================================
         * PROFILE IMAGE
         * =========================================================
         */

        if (message.getSenderId() != null &&
                message.getSenderId().equals(currentUserId)) {

            // SENDER PROFILE IMAGE

            if (holder.senderImage != null) {

                if (senderImgUrl == null ||
                        senderImgUrl.trim().isEmpty() ||
                        senderImgUrl.equals("default")) {

                    holder.senderImage
                            .setImageResource(R.drawable.name);

                } else {

                    Glide.with(context)
                            .load(senderImgUrl)
                            .placeholder(R.drawable.name)
                            .error(R.drawable.name)
                            .into(holder.senderImage);
                }
            }

        } else {

            // RECEIVER PROFILE IMAGE

            if (holder.receiverImage != null) {

                if (receiverImgUrl == null ||
                        receiverImgUrl.trim().isEmpty() ||
                        receiverImgUrl.equals("default")) {

                    holder.receiverImage
                            .setImageResource(R.drawable.name);

                } else {

                    Glide.with(context)
                            .load(receiverImgUrl)
                            .placeholder(R.drawable.name)
                            .error(R.drawable.name)
                            .into(holder.receiverImage);
                }
            }
        }
    }


    /*
     * =============================================================
     * OPEN VIDEO ACTIVITY
     * =============================================================
     */

    private void openVideo(String videoUrl) {

        if (videoUrl == null || videoUrl.trim().isEmpty()) {

            Log.e("VIDEO_ACTIVITY",
                    "Video URL is empty");

            return;
        }

        Intent intent = new Intent(context, VideoActivity.class);

        intent.putExtra(
                "videoUrl",
                videoUrl
        );

        context.startActivity(intent);
    }


    /*
     * =============================================================
     * CREATE CLOUDINARY VIDEO THUMBNAIL
     * =============================================================
     */

    private String createCloudinaryThumbnail(String videoUrl) {

        try {


            if (videoUrl.contains("/video/upload/")) {

                String thumbnailUrl = videoUrl
                        .replace(
                                "/video/upload/",
                                "/video/upload/so_0,w_600/"
                        );

                if (thumbnailUrl.toLowerCase().endsWith(".mp4")) {

                    thumbnailUrl = thumbnailUrl.substring(
                            0,
                            thumbnailUrl.length() - 4
                    ) + ".jpg";
                }

                return thumbnailUrl;
            }

        } catch (Exception e) {

            Log.e("VIDEO_THUMBNAIL",
                    "Thumbnail creation error",
                    e);
        }

        return videoUrl;
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setImageUrls(
            String senderImgUrl,
            String receiverImgUrl
    ) {

        this.senderImgUrl = senderImgUrl;
        this.receiverImgUrl = receiverImgUrl;

        notifyDataSetChanged();
    }


    /*
     * =============================================================
     * VIEWHOLDER
     * =============================================================
     */

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtMessage;

        ImageView receiverImage;
        ImageView senderImage;

        ImageView senderImageView;
        ImageView receiverImageView;

        View senderVideoContainer;
        View receiverVideoContainer;

        ImageView senderVideoThumbnail;
        ImageView receiverVideoThumbnail;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            txtMessage =
                    itemView.findViewById(R.id.txtMessage);

            receiverImage =
                    itemView.findViewById(R.id.receiverImage);

            senderImage =
                    itemView.findViewById(R.id.senderImage);

            senderImageView =
                    itemView.findViewById(R.id.senderImageView);

            receiverImageView =
                    itemView.findViewById(R.id.receiverImageView);

            senderVideoContainer =
                    itemView.findViewById(
                            R.id.senderVideoContainer
                    );

            receiverVideoContainer =
                    itemView.findViewById(
                            R.id.receiverVideoContainer
                    );

            senderVideoThumbnail =
                    itemView.findViewById(
                            R.id.senderVideoThumbnail
                    );

            receiverVideoThumbnail =
                    itemView.findViewById(
                            R.id.receiverVideoThumbnail
                    );
        }
    }
}