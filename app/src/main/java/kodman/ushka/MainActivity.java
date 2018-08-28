package kodman.ushka;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


@TargetApi(17)
public class MainActivity extends AppCompatActivity {


    private static boolean isGo=false;
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//---------Для отображения рекламы
       MobileAds.initialize(getApplicationContext(), "ca-app-pub-3917579650161866/9949160063");
       AdView adView=(AdView)this.findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        //Для полноэкранного изображения
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Без заголовка
        //   requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(isGo)
        {
            game = new Game(this);
            setContentView(game);
        }
    }

    public void btnClick(View view)
    {
        game = new Game(this);
        isGo=true;
        game.isEnd=false;
        setContentView(game);
    }

//
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//
//        if(game!=null&&game.GT!=null)
//        {
//           //   game.GT.start();
//            //game.GT.pauseOff();
//        }
//    }
/*
    @Override
    public void onPause()
    {
        super.onPause();
        if(game!=null&&game.GT!=null)
        {
            game.GT.pauseOn();
        }

    }
    */

    @Override
    public void onBackPressed()
    {
        Log.d("-----STOP THREAD 1", "");
        System.out.println("BackPress");
        if(game!=null&&game.GT!=null)
        {
            System.out.println("BackPress +stop Thread");
            Log.d("-----STOP THREAD Stop", "");
            game.GT.pauseOn();
        }

        AlertDialog.Builder builder= new AlertDialog.Builder(this,android.R.style.Theme_Holo_Light_Dialog);
        builder.setMessage(R.string.exit);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               // game.isEnd = true;
                finish();
                System.exit(0);
                return;
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            //    game.play();//.notify();
                //return;
                 if(game!=null&&game.GT!=null)
        {
            game.GT.pauseOff();
        }
            }
        });


        builder.show();

    }

    @Override
    public void onDestroy()
    {
         super.onDestroy();
        isGo=false;
        if(game==null)return;
        game.clearGame();
    }
  }

