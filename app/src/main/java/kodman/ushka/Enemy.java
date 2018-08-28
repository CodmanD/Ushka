package kodman.ushka;

import android.graphics.RectF;
import android.util.Log;

import java.util.Random;

/**
 * Created by android on 05.10.2017.
 */
public class Enemy extends Thread
{

    private static final String TAG="-----------ENEMY--------";

    private boolean isRun=true;
    private RectF R;
    public int delay=50;
    public int status=0;
    //
    public static int bossChildren=100;

    //public Bitmap bmpEnemy;
    Random rand =new Random();
    public final static int WIDTH_Level_1=100;
    public final static int HEIGHT_Level_1=60;
    public final static int WIDTH_Level_2=130;//size horizonal
    public final static int HEIGHT_Level_2=70;//size vertical
    public final static int WIDTH_Level_3=200;//size horizonal
    public final static int HEIGHT_Level_3=100;
    public final static int WIDTH_Level_4=400;//size horizonal
    public final static int HEIGHT_Level_4=200;
    public final static int WIDTH_Level_5=70;//size horizonal
    public final static int HEIGHT_Level_5=50;
    public final static int WIDTH_Level_6=130;//size horizonal
    public final static int HEIGHT_Level_6=70;


   //задержка появления врагов
   public  static int DELAY_FIRE=1000;
    public  static int DELAY_LEVEL_3=30000;//30000
    public  static int DELAY_LEVEL_2=10000;
    public  static int DELAY_LEVEL_1=1000;
    public  static int DELAY_LEVEL_4=5000;
    public  static int DELAY_LEVEL_5=500;
    public  static int DELAY_LEVEL_6=500;
    public static long addLevel1;
    public static long addLevel2;
    public static long addLevel3;
    public static long addLevel4;
    public static long addLevel5;
    public static long addLevel6;


    public static boolean  bossTime=false;
    public static boolean  bossIn=false;
   // public long lastDraw;

    public static int field;
    public int level=1;
    public int armor=10;


    public int kX=0;
    public int kY=0;

    private static int cnt=0;
    public long timeIn;//через сколько пора стрелять

    public static void  setLevel(int i)
    {
        switch(i)
        {
            case 1:
                Fire.delay=100;
                 DELAY_FIRE=800;
                DELAY_LEVEL_1=500;
                 DELAY_LEVEL_3=30000;//30000
                 DELAY_LEVEL_2=10000;

                 DELAY_LEVEL_4=5000;
                 DELAY_LEVEL_5=200;
                 DELAY_LEVEL_6=500;
                break;
            case 2:
                Enemy.DELAY_LEVEL_1=3000;
                Enemy.DELAY_LEVEL_2=1000;
                Enemy.DELAY_LEVEL_3=10000;
                break;
            case 3:
                Enemy.DELAY_LEVEL_2=1000;
                Enemy.DELAY_LEVEL_3=2000;
                break;
            case 4:
                Enemy.bossTime=true;
                break;
            case 5:break;
            case 6:
                Enemy.DELAY_LEVEL_1=1000;
                Enemy.DELAY_LEVEL_2=2000;
                Enemy.DELAY_LEVEL_3=5000;
                Enemy.DELAY_LEVEL_6=3000;
                break;
        }
    }

    public Enemy(RectF r,int level,long curTime)
    {
        this.R=r;
        this.level=level;
         //   this.lastDraw=curTime;

        switch(this.level)
        {
            case 1:
                kY= rand.nextInt(2) +1;
                kX=0;
                this.armor=3;
                break;
            case 2:
                kY=rand.nextInt(3) +2;
                cnt++;
                kX=rand.nextInt(3) +2;
                if(cnt%2!=0)

                    kX*=-1;

                this.armor=3;
                break;
            case 3:
                timeIn=curTime;
                cnt++;
                kX=rand.nextInt(3) +2;
                Log.d(TAG, "cnt=" + cnt);
                if(cnt%2!=0)

                    kX*=-1;

                kY=rand.nextInt(4) +3;
                this.armor=5;
                break;
            case 4://boss
              //  timeIn=curTime;
                cnt++;

                Log.d(TAG, "cnt=" + cnt);
                if(cnt%2==0)
                    kX=5;
                else
                    kX=-5;

                kY=3;
                this.armor=200;
                break;
            case 5://boosChildren
               // timeIn=curTime;
                cnt++;
                Log.d(TAG, "cnt=" + cnt);
                if(cnt%2==0)
                    kX=rand.nextInt(3)+1;
                else
                    kX=(rand.nextInt(5)+1)*(-1);

                kY=rand.nextInt(3)+1;;
                this.armor=2;
                break;
            case 6://boosChildren
               // timeIn=curTime;
                cnt++;
                Log.d(TAG, "cnt=" + cnt);
                if(cnt%2==0)
                    kX=rand.nextInt(5)+1;
                else
                    kX=(rand.nextInt(5)+1)*(-1);

                kY=5;
                this.armor=5;
                break;
        }
        // this.bmpEnemy=b;
        delay= rand.nextInt(150)+1;//задержка с которой приближаются враги
    }

    public RectF getRectf()
    {
        return R;
    }

    public   void setRunStop()
    {
        isRun=false;
    }



    @Override
    public void run()
    {
        while(isRun)
        {

            try
            {
                //  Log.d("----------Thread:-",""+Thread.currentThread());
                Thread.sleep(delay);
                int k = rand.nextInt(10) * 3;
                String str = "K=" + k + " Last Y=" + R.top + "  bottom=" + R.bottom;
                R.top += k;
                R.bottom += k;
            }
            catch(InterruptedException iex)
            {
                //Log.d("---END WORK-------Thread:-",""+Thread.currentThread());
                break;
            }
        }

        //float x1=r.bottom+rand.nextInt(0)*3;
        //str+=" nov y="+r.top+" botoom="+r.bottom;
    }

}

