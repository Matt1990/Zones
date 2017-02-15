package red.com.zones;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Binil Mathew on 04-Jan-17.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    String[] title;
    String[] address;
    String[] radius;
    Context context;
    Integer integer;


    public RecyclerViewAdapter(String[] parmtitle, String[] paramaddress, String[] paramradius, Context context ) {
        title = parmtitle;
        address = paramaddress;
        radius = paramradius;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewAddress;
        public TextView textViewRadius;
        public Switch aSwitchAlarm;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewAddress = (TextView) itemView.findViewById(R.id.textViewAddress);
            textViewRadius = (TextView) itemView.findViewById(R.id.textViewRadius);
            aSwitchAlarm = (Switch) itemView.findViewById(R.id.services_switch);
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_alarm, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.textViewTitle.setText(title[position]);
        holder.textViewAddress.setText(address[position]);
        holder.textViewRadius.setText(radius[position] + " M");
        holder.aSwitchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                NotificationCompat.Builder builder = (NotificationCompat.Builder)
                        new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_pin_drop_black_24dp)
                        .setContentTitle("Zones")
                        .setContentText("Location alarm is running.")
                                .setCategory(NotificationCompat.CATEGORY_SERVICE).setOngoing(true);
                NotificationManager notificationManager = (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (isChecked) {
                    integer = 100;
                    context.startService(new Intent(context, LocationServices.class));
                    Toast.makeText(context, "On", Toast.LENGTH_SHORT).show();
                    Log.e("Checked", "Yes");
                    notificationManager.notify(integer, builder.build());
                }
                if (!isChecked) {
                    context.stopService(new Intent(context, LocationServices.class));
                    Toast.makeText(context, "Off", Toast.LENGTH_SHORT).show();
                    Log.e("Checked", "No");
                    notificationManager.cancel(integer);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return title.length;
    }
}