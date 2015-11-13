
package com.parse.carpool;

        import android.content.Context;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.RatingBar;
        import android.widget.TextView;

        import com.facebook.Profile;
        import com.facebook.login.widget.ProfilePictureView;
        import com.parse.integratingfacebooktutorial.R;
        import com.squareup.picasso.Picasso;

        import java.math.BigInteger;
        import java.security.MessageDigest;
        import java.util.List;

/**
 * Created by JUMRUS on 3/11/2558.
 */
public class ReviewListAdapter extends ArrayAdapter<Review> {
    private String mUserId;

    public ReviewListAdapter(Context context, String userId, List<Review> reviews) {
        super(context, 0, reviews);
        this.mUserId = userId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.review_list, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.raterTv = (TextView)convertView.findViewById(R.id.raterTv);
            holder.contentTv = (TextView)convertView.findViewById(R.id.contentTv);
            holder.ratingReview = (RatingBar)convertView.findViewById(R.id.ratingReview);
            holder.userProfileView = (ProfilePictureView)convertView.findViewById(R.id.userProfilePicture);
            convertView.setTag(holder);
        }
        final Review review = (Review)getItem(position);
        final ViewHolder holder = (ViewHolder)convertView.getTag();
        final boolean isMe = review.getUserId().equals(mUserId);
        // Show-hide image based on the logged-in user.
        // Display the profile image to the right for our user, left for other users.
//        holder.body.setText(message.getBody());
        holder.userProfileView.setProfileId(review.getUserFbId());
        holder.raterTv.setText(review.getRater());
        holder.contentTv.setText(review.getTextReview());
        holder.ratingReview.setRating(review.getRating());
        return convertView;
    }

    // Create a gravatar image based on the hash value obtained from userId
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

    final class ViewHolder {
        public TextView raterTv;
        public RatingBar ratingReview;
        public TextView contentTv;
        public ProfilePictureView userProfileView;
    }

}