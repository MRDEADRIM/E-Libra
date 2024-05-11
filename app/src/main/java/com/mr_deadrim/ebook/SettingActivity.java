package com.mr_deadrim.ebook;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.transition.TransitionManager;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class SettingActivity extends AppCompatActivity {
    private View dialogView;
    ConstraintLayout arrow,arrow2,arrow3,arrow4;
    ConstraintLayout hiddenView,hiddenView2,hiddenView3,hiddenView4;
    CardView cardView,cardView2,cardView3,cardView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_setting);


        cardView = findViewById(R.id.base_cardview);
        arrow = findViewById(R.id.fixed_layout);
        hiddenView = findViewById(R.id.hidden_view);

        cardView2 = findViewById(R.id.base_cardview2);
        arrow2 = findViewById(R.id.fixed_layout2);
        hiddenView2 = findViewById(R.id.hidden_view2);


        cardView3 = findViewById(R.id.base_cardview3);
        arrow3 = findViewById(R.id.fixed_layout3);
        hiddenView3 = findViewById(R.id.hidden_view3);

        cardView4 = findViewById(R.id.base_cardview4);
        arrow4 = findViewById(R.id.fixed_layout4);
        hiddenView4 = findViewById(R.id.hidden_view4);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hiddenView.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(cardView,
                            new AutoTransition());
                    hiddenView.setVisibility(View.GONE);
//                    arrow.setImageResource(android.R.drawable.arrow_down_float);
                }

                else {

                    TransitionManager.beginDelayedTransition(cardView,
                            new AutoTransition());
                    hiddenView.setVisibility(View.VISIBLE);
//                    arrow.setImageResource(android.R.drawable.arrow_up_float);
                }
            }
        });


        arrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hiddenView2.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(cardView2,
                            new AutoTransition());
                    hiddenView2.setVisibility(View.GONE);
//                    arrow.setImageResource(android.R.drawable.arrow_down_float);
                }

                else {

                    TransitionManager.beginDelayedTransition(cardView2,
                            new AutoTransition());
                    hiddenView2.setVisibility(View.VISIBLE);
//                    arrow.setImageResource(android.R.drawable.arrow_up_float);
                }
            }
        });


        arrow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hiddenView3.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(cardView2,
                            new AutoTransition());
                    hiddenView3.setVisibility(View.GONE);
//                    arrow.setImageResource(android.R.drawable.arrow_down_float);
                }

                else {

                    TransitionManager.beginDelayedTransition(cardView3,
                            new AutoTransition());
                    hiddenView3.setVisibility(View.VISIBLE);
//                    arrow.setImageResource(android.R.drawable.arrow_up_float);
                }
            }
        });


        arrow4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hiddenView4.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(cardView2,
                            new AutoTransition());
                    hiddenView4.setVisibility(View.GONE);
//                    arrow.setImageResource(android.R.drawable.arrow_down_float);
                }

                else {

                    TransitionManager.beginDelayedTransition(cardView3,
                            new AutoTransition());
                    hiddenView4.setVisibility(View.VISIBLE);
//                    arrow.setImageResource(android.R.drawable.arrow_up_float);
                }
            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        MenuItem item1 = menu.findItem(R.id.item1);
        MenuItem item2 = menu.findItem(R.id.item2);
        MenuItem item3 = menu.findItem(R.id.item3);


        item2.setEnabled(false);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==R.id.item1){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            finish();
        }
        if(item.getItemId()==R.id.item2){
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            finish();
        }
        if(item.getItemId()==R.id.item3){
            exit();
        }
        return super.onOptionsItemSelected(item);
    }

    public void exit(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this);
        dialogView = getLayoutInflater().inflate(R.layout.exit_dialog, null);
        Button cancelButton = dialogView.findViewById(R.id.button2);
        Button exitButton = dialogView.findViewById(R.id.button5);
        alert.setView(dialogView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        cancelButton.setOnClickListener(v1 -> alertDialog.dismiss());
        exitButton.setOnClickListener(v12 -> {
            finishAffinity();
        });
        alertDialog.show();
    }

}