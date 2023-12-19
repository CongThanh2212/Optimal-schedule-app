package firebase.gopool.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import firebase.gopool.R;
import firebase.gopool.RidesActivity;

public class LeaveRideDialog extends Dialog implements
        View.OnClickListener  {

    private static final String TAG = "LeaveRideDialog";
    public Context c;
    public Dialog d;
    private EditText confirmDialog, cancelDialog;

    private int rideId;
    private boolean isFrequent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_leave_ride);
        cancelDialog = (EditText) findViewById(R.id.dialogCancel);
        confirmDialog = (EditText) findViewById(R.id.dialogConfirm);
        cancelDialog.setOnClickListener(this);
        confirmDialog.setOnClickListener(this);
    }

    public LeaveRideDialog(Context a, int rideId, boolean isFrequent) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.rideId = rideId;
        this.isFrequent = isFrequent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialogConfirm:
                leaveRide();
                dismiss();
                ((Activity) c).finish();
                Intent intent = new Intent(c, RidesActivity.class);
                ((Activity) c).startActivity(intent);
                break;
            case R.id.dialogCancel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private void leaveRide(){
        // API leave ride
    }

}
