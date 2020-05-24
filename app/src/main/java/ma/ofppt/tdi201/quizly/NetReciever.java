package ma.ofppt.tdi201.quizly;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class NetReciever extends BroadcastReceiver {
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    @Override
    public void onReceive(final Context context, Intent intent) {
        connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);


        if(connectivityManager!=null){
            networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null && networkInfo.isConnected()){



            }else{
                    builder= new AlertDialog.Builder(context);
                final View view= LayoutInflater.from(context).inflate(R.layout.connection_detector_dialog,null);
                builder.setView(view);
                builder.setCancelable(false);
                builder.create();
                alertDialog=builder.show();
                view.findViewById(R.id.cnxTryAgain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if (connectivityManager!=null){
                            networkInfo=connectivityManager.getActiveNetworkInfo();
                            if (networkInfo!=null && networkInfo.isConnected()) {
                                alertDialog.dismiss();

                            }else{
                                builder= new AlertDialog.Builder(context);
                                final View view= LayoutInflater.from(context).inflate(R.layout.connection_detector_dialog,null);
                                builder.setView(view);
                                builder.setCancelable(false);
                                builder.create();
                                alertDialog=builder.show();
                                view.findViewById(R.id.cnxTryAgain).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                        if (connectivityManager!=null){
                                            networkInfo=connectivityManager.getActiveNetworkInfo();
                                            if (networkInfo!=null && networkInfo.isConnected()) {
                                                alertDialog.dismiss();
                                            }else{
                                                builder= new AlertDialog.Builder(context);
                                                final View view= LayoutInflater.from(context).inflate(R.layout.connection_detector_dialog,null);
                                                builder.setView(view);
                                                builder.setCancelable(false);
                                                builder.create();
                                                alertDialog=builder.show();
                                                view.findViewById(R.id.cnxTryAgain).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        alertDialog.dismiss();

                                                    }
                                                });

                                            }
                                        }


                                    }
                                });

                            }
                        }

                    }
                });


            }
        }

    }
}
