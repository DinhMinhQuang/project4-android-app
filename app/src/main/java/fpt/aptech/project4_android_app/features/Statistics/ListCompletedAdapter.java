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

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Order;

public class ListCompletedAdapter extends ArrayAdapter<Order> {

    public ListCompletedAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public ListCompletedAdapter(@NonNull Context context, int resource, List<Order> orders) {
        super(context, resource, orders);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.details_completed_orders, null);
        }
        Order order = getItem(position);
        if (order != null) {
            String COUNTRY = "VN";
            String LANGUAGE = "vi";
            String str = NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(order.getAmount());
            TextView tvAddress = v.findViewById(R.id.tvAddress);
            tvAddress.setText(order.getAddress());
            TextView tvRestaurant = v.findViewById(R.id.tvStoreName);
            tvRestaurant.setText(order.getRestaurant().getName());
            TextView tvUserPhone = v.findViewById(R.id.tvUserPhone);
            tvUserPhone.setText(order.getUser().getPhone());
            TextView tvAmount = v.findViewById(R.id.tvPrice);
            tvAmount.setText(str);
            ImageView imageStore = v.findViewById(R.id.tvImageStore);
            Picasso.get().load("http://0b78bf0553f2.ngrok.io/public/image/"+order.getRestaurant().getAvatar()).into(imageStore);
        }
        return v;
    }
}
