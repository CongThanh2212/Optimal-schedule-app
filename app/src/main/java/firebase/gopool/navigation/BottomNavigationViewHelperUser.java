package firebase.gopool.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import firebase.gopool.AccountActivity;
import firebase.gopool.FrequentActivity;
import firebase.gopool.MapsActivity;
import firebase.gopool.R;
import firebase.gopool.RidesActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationViewHelperUser {

    public static void enableNavigation(final Context context, final BottomNavigationView view){

        view.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.menu_location:
                    if (view.getSelectedItemId() != R.id.menu_location) {
                        Intent intent = new Intent(context, MapsActivity.class); //ACTIVITY_NUMBER = 0
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                    break;
                case R.id.menu_frequent:
                    if (view.getSelectedItemId() != R.id.menu_frequent) {
                        Intent intent = new Intent(context, FrequentActivity.class); //ACTIVITY_NUMBER = 1
                        context.startActivity(intent);
                        if (view.getSelectedItemId() == R.id.menu_location) {
                            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }
                    }
                    break;
                case R.id.menu_rides:
                    if (view.getSelectedItemId() != R.id.menu_rides) {
                        Intent intent = new Intent(context, RidesActivity.class); //ACTIVITY_NUMBER = 2
                        context.startActivity(intent);
                        if (view.getSelectedItemId() == R.id.menu_account) {
                            ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        } else {
                            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }
                    break;
                default:
                    if (view.getSelectedItemId() != R.id.menu_account) {
                        Intent intent = new Intent(context, AccountActivity.class); //ACTIVITY_NUMBER = 3
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    break;
            }
            return false;
        });
    }
}
