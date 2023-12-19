package firebase.gopool.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import firebase.gopool.AccountDriverActivity;
import firebase.gopool.R;
import firebase.gopool.TripNowActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationViewHelperDriver {

    public static void enableNavigation(final Context context, final BottomNavigationView view){

        view.setOnNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.menu_trip_now) {
                if (view.getSelectedItemId() != R.id.menu_trip_now) {
                    Intent intent = new Intent(context, TripNowActivity.class); //ACTIVITY_NUMBER = 0
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            } else if (view.getSelectedItemId() != R.id.menu_account_driver) {
                Intent intent = new Intent(context, AccountDriverActivity.class); //ACTIVITY_NUMBER = 1
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            return false;
        });
    }
}
