package au.edu.sydney.comp5216.easydiet.login;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import au.edu.sydney.comp5216.easydiet.R;


public class MainActivity extends AppCompatActivity {

    final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

//        if(true){
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            MainActivity.this.startActivity(intent);
//        }


    }
}
