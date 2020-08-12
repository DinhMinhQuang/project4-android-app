package fpt.aptech.project4_android_app.features.Statistics;

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

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Order;

public class RatingAdapter extends SimpleAdapter {

    private Context mContext;
    public LayoutInflater inflater=null;

    public RatingAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.list_rating, null);
        }
        Map<String, Object> data = (Map<String, Object>) getItem(position);
        TextView comment = vi.findViewById(R.id.comment);
        comment.setText(String.valueOf(data.get("comment")));
        TextView stars = vi.findViewById(R.id.stars);
        stars.setText(String.valueOf(data.get("starts")));
        return vi;
    }
}
