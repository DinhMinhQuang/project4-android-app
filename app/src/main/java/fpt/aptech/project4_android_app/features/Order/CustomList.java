package fpt.aptech.project4_android_app.features.Order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

import fpt.aptech.project4_android_app.R;

public class CustomList extends SimpleAdapter {

    private Context mContext;
    public LayoutInflater inflater=null;

    public CustomList(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.details_list_product, null);
        }
        Map<String, Object> data = (Map<String, Object>) getItem(position);
        TextView tvProductName = vi.findViewById(R.id.tvProductName);
        tvProductName.setText(String.valueOf(data.get("productName")));
        TextView tvProductPrice = vi.findViewById(R.id.tvProductPrice);
        tvProductPrice.setText(String.valueOf(data.get("price")).split("\\.")[0] +"đ");
        TextView tvQuantity = vi.findViewById(R.id.tvQuantity);
        tvQuantity.setText("x" +(data.get("quantity").toString().split("\\.")[0]));
        ImageView imgProductDetails = vi.findViewById(R.id.imgProductDetails);
        Picasso.get().load("http://2113a384170a.ngrok.io/public/image/" + data.get("image")).into(imgProductDetails);
        return vi;
    }
}
