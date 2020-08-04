package fpt.aptech.project4_android_app.features.Statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Order;

public class ListAdapter extends ArrayAdapter<Order> {

    public ListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public ListAdapter(@NonNull Context context, int resource, List<Order> orders) {
        super(context, resource, orders);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.details_completed_order, null);
        }
        Order order = getItem(position);
        if (order != null) {
            TextView tvRestaurant = v.findViewById(R.id.tvRestaurant);
            tvRestaurant.setText(order.getRestaurant().getName());
            TextView tvUserPhone = v.findViewById(R.id.tvUserPhone);
            tvUserPhone.setText(order.getUser().getPhone());
            TextView tvAmount = v.findViewById(R.id.tvAmount);
            tvAmount.setText(String.valueOf(order.getAmount()));
            ImageView imageStore = v.findViewById(R.id.imageStore);
            Picasso.get().load("http://2113a384170a.ngrok.io/public/image/"+order.getRestaurant().getAvatar()).into(imageStore);
        }
        return v;
    }
}
