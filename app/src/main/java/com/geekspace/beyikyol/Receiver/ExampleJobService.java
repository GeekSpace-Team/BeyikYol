package com.geekspace.beyikyol.Receiver;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class ExampleJobService extends JobService {
    private static final String TAG="ExampleJobService";
    private boolean jobCanceled=false;
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG,"Job Started");
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(final JobParameters params){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<100;i++){
                    Log.d(TAG,"run"+i);
                   // Toast.makeText(ExampleJobService.this,"Run"+i,Toast.LENGTH_SHORT).show();
                    if(jobCanceled){
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "Job finished");
                jobFinished(params,false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job canceled before completion");
        jobCanceled=true;
        return true;
    }
}
