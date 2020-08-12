package com.teknosols.a3orrsy.other.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.teknosols.a3orrsy.R;

public class DialogUtils {
    public static AlertDialog getProgressDialog(Context context){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater =(LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.progress_layout, null);
        dialogBuilder.setView(dialogView);
//        ImageView spaceshipImage = (ImageView) dialogView.findViewById(R.id.image);
//        ImageView imagebg1= (ImageView) dialogView.findViewById(R.id.imagebg1) ;
//        ImageView imagebg2= (ImageView) dialogView.findViewById(R.id.imagebg2) ;

//        AlphaAnimation anim2 = new AlphaAnimation(0.3f, 0.7f);
//        anim2.setDuration(800);
//        anim2.setRepeatCount(Animation.INFINITE);
//        anim2.setRepeatMode(Animation.RESTART);
//
//        imagebg2.setAlpha(0.3f);
//        imagebg2.startAnimation(anim2);
//
//        AlphaAnimation anim1 = new AlphaAnimation(0.7f, 1.0f);
//        anim1.setDuration(800);
//        anim1.setRepeatCount(Animation.INFINITE);
//        anim1.setRepeatMode(Animation.RESTART);
//
//        imagebg1.setAlpha(0.7f);
//        imagebg1.startAnimation(anim1);


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        //alertDialog.show();

        return alertDialog;
    }

}
