package fpt.aptech.project4_android_app.features.Order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Order;

public class CustomAdapter extends ArrayAdapter<Order> {


    public CustomAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public CustomAdapter(@NonNull Context context, int resource, List<Order> orders) {
        super(context, resource, orders);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.order_details, null);
        }
        Order order = getItem(position);
        if (order != null){
            TextView tvStoreName, tvAddress, tvPrice, tvUserPhone;
            ImageView tvImageStore;
            tvStoreName = v.findViewById(R.id.tvStoreName);
            tvAddress = v.findViewById(R.id.tvAddress);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvUserPhone = v.findViewById(R.id.tvUserPhone);
            tvImageStore = v.findViewById(R.id.tvImageStore);
            tvStoreName.setText(order.getRestaurant().getName());
            tvAddress.setText(order.getAddress());
            tvPrice.setText(String.valueOf(order.getAmount()));
            tvUserPhone.setText(order.getUser().getPhone());
            Picasso.get().load("http://1ec6fbf93c32.ngrok.io/public/image/"+order.getRestaurant().getAvatar()).into(tvImageStore);
        }
        return v;
    }
}
