package ma.ofppt.tdi201.quizly;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

class DialogLoading {
   private Activity activity;
   private AlertDialog dialog;
    DialogLoading(Activity myactivity){
        activity=myactivity;
    }


    void startdialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog,null));
        builder.setCancelable(true);
        dialog=builder.create();
        dialog.show();
    }
}
