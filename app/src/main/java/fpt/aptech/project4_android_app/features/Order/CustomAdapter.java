package fpt.aptech.project4_android_app.features.Order;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


import java.util.List;


import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Order;
import fpt.aptech.project4_android_app.features.TouchHelper.ItemTouchHelperAdapter;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private Context mContext;
    private List<Order> orders;
    ItemTouchHelper itemTouchHelper;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String PREFS = "PREFS";

    public CustomAdapter(Context mContext, List<Order> orders) {
        this.mContext = mContext;
        this.orders = orders;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View order_details = inflater.inflate(R.layout.order_details, null);
        ViewHolder viewHolder = new ViewHolder(order_details);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvStoreName.setText(order.getRestaurant().getName());
        holder.tvAddress.setText(order.getAddress());
        holder.tvFee.setText(order.getFee().toString() +"đ");
        holder.tvPrice.setText(order.getAmount().toString() +"đ");
        holder.tvUserPhone.setText(order.getUser().getPhone());
        Picasso.get().load("http://711c70445b85.ngrok.io/public/image/" + order.getRestaurant().getAvatar()).into(holder.tvImageStore);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Order fromOrder = orders.get(fromPosition);
        orders.remove(fromOrder);
        orders.add(toPosition, fromOrder);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        orders.remove(position);
        notifyItemRemoved(position);
    }

    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.itemTouchHelper = touchHelper;
    }

    public interface ItemClickListener {
        void onClick(View view, int position,boolean isLongClick);
    }

    public class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener, GestureDetector.OnGestureListener {
        TextView tvStoreName, tvAddress, tvPrice, tvUserPhone, tvFee;
        ImageView tvImageStore;
        GestureDetector gestureDetector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gestureDetector = new GestureDetector(itemView.getContext(), this);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvUserPhone = itemView.findViewById(R.id.tvUserPhone);
            tvImageStore = itemView.findViewById(R.id.tvImageStore);
            tvFee = itemView.findViewById(R.id.tvFee);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sp = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            edit = sp.edit();
            int position = getLayoutPosition();
            orders.get(position);
            edit.putString("orderId", orders.get(position).getId());
            edit.apply();
            view.getContext().startActivity(new Intent(view.getContext(), DetailsOrderActivity.class));
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            itemTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return true;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        }
    }
}
