package com.technoworks.fusionmonitor;

/**
 * Created by Всеволод on 01.07.2014.
 */
public class SimulationThread extends Thread
{
    public static long OSCILLATOR_PERIOD_MILLIS = 1000;

    private LoggerList mLog;
    private double mOscillator;
    private long mStartTimestamp;
    private boolean mIsFinished;

    public SimulationThread(LoggerList log)
    {
        mLog = log;
        mOscillator = 0;
        mStartTimestamp = System.currentTimeMillis();
        mIsFinished = false;
        start();
    }

    public void finish()
    {
        mIsFinished = true;
    }

    public void run()
    {
        while(!mIsFinished)
        {
            mOscillator = Math.sin((System.currentTimeMillis() - mStartTimestamp)/((double) OSCILLATOR_PERIOD_MILLIS));
            Messaging.Vector vector = Messaging.Vector.newBuilder()
                    .setX(mOscillator)
                    .setY(mOscillator)
                    .setZ(mOscillator)
                    .build();
            Messaging.PIDValues pidValues = Messaging.PIDValues.newBuilder()
                    .setP(mOscillator)
                    .setI(mOscillator)
                    .setD(mOscillator)
                    .build();
            Messaging.Telemetry telemetry = Messaging.Telemetry.newBuilder()
                    .setTimestamp(System.currentTimeMillis())
                    .setTorques(vector)
                    .setAngularVelocity(vector)
                    .setPidX(pidValues)
                    .setPidY(pidValues)
                    .setPidZ(pidValues)
                    .setHeading(mOscillator)
                    .setControlHeading(mOscillator)
                    .setForce(mOscillator)
                    .setCorrectionX(mOscillator)
                    .setCorrectionY(mOscillator)
                    .build();
            mLog.offer(telemetry);
            try
            {
                sleep(97);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
