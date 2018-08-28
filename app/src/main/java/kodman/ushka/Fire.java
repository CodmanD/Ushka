package kodman.ushka;

import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by android on 05.10.2017.
 */
public class Fire extends Thread
{
    public RectF R;

    public Bitmap bmpFire;
    public  double bulletFactor= 0;
    public double bulletStep= 40;

    public double kX=0;
    public double kY=0;
    public static int status=1;

    private static int queue=1;
    public static int  delay=0;

    public  int line=0;//в какую сторону летит
    public final static int WIDTH=45;//size horizonal
    public final static int HEIGHT=45;//size vertical

    public Fire(RectF r,Bitmap bmp)
    {
        this.R=r;
        if(queue==1)
            this.line=1;
        else
            this.line=2;
        if(queue==1)
            queue=2;
        else
            queue=1;
        this.bmpFire=bmp;

        switch(status)
        {
            case 1:
                if(line==1)
                    kX=0;
                else
                if(line==2)
                    kX=0;

                kY=-40;
                    bulletStep=100;

                delay=100;
                break;


            case 2:
                if(line==1)
                    kX=10;
                else
                if(line==2)
                    kX=-10;

                kY=-2;
                bulletStep=20;
                delay=100;
                break;

            case 3:
                if(line==1)
                    kX=2;
                else
                if(line==2)
                    kX=-2;

                kY=-40;
                bulletStep=20;

                delay=100;
                break;

        }
    }


    public RectF getRectF(){return this.R;}

    public void setStatus(int s)
    {
        status=s;
        switch(status)
        {
            case 1:
                bulletStep= 20;
                delay=100;
                break;
            case 2:
                bulletFactor= 1.7;
                bulletStep= 40;
                delay=200;
                break;
            case 3:
                break;
        }
    }

}
