package com.carbeauty.camera;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.R;
import com.carbeauty.order.HeaderActivity;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.tutk.IOTC.AVIOCTRLDEFs;
import com.tutk.IOTC.Camera;
import com.tutk.IOTC.Monitor;
import com.tutk.IOTC.Packet;

/**
 * Created by Administrator on 2015/11/13.
 */
public class VideoInfoActivity extends HeaderActivity implements com.tutk.IOTC.IRegisterIOTCListener{
    private  int objectId;
    private Monitor monitor;
    private MyCamera camera;
    private String message;

    private int mVideoWidth=0;
    private int mVideoHeight=0;
    KProgressHUD progressHUD;
    boolean isLandScreen=true;
    public VideoInfoActivity(){

    }

    private Handler videoHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");
            String requestDevice = bundle.getString("requestDevice");

            switch (msg.what) {
                case com.tutk.IOTC.Camera.CONNECTION_STATE_CONNECT_FAILED:
                    message="视频连接错误";
                    break;

                case com.tutk.IOTC.Camera.CONNECTION_STATE_CONNECTED:
                    message="视频已连接";
                    progressHUD.dismiss();
                    break;

                case com.tutk.IOTC.Camera.CONNECTION_STATE_CONNECTING:
                    message="视频连接中...";
                    break;

                case com.tutk.IOTC.Camera.CONNECTION_STATE_DISCONNECTED:
                    message="视频连接已经断开";
                    break;

                case com.tutk.IOTC.Camera.CONNECTION_STATE_TIMEOUT:
                    message="连接超时";

                    break;

                case com.tutk.IOTC.Camera.CONNECTION_STATE_UNKNOWN_DEVICE:
                    break;

                case com.tutk.IOTC.Camera.CONNECTION_STATE_UNSUPPORTED:
                    break;

                case com.tutk.IOTC.Camera.CONNECTION_STATE_WRONG_PASSWORD:
                    message="密码错误";
                    break;
                case AVIOCTRLDEFs.IOTYPE_USER_IPCAM_LISTWIFIAP_RESP:

                    int cnt = Packet.byteArrayToInt_Little(data, 0);
                    int size = AVIOCTRLDEFs.SWifiAp.getTotalSize();


                    if (cnt > 0 && data.length >= 40) {

                        int pos = 4;

                        for (int i = 0; i < cnt; i++) {

                            byte[] ssid = new byte[32];
                            System.arraycopy(data, i * size + pos, ssid, 0, 32);

                            byte mode = data[i * size + pos + 32];
                            byte enctype = data[i * size + pos + 33];
                            byte signal = data[i * size + pos + 34];
                            byte status = data[i * size + pos + 35];


                            if (status == 1) {

                                message=getString(ssid)+" connected";


                            } else if (status == 2) {

                                message=getString(ssid)+" wifi wrong password";

                            } else if (status == 3) {

                                message=getString(ssid)+" wifi weak singal";

                            } else if (status == 4) {

                                message=getString(ssid)+" wifi ready";

                            }
                            Toast.makeText(VideoInfoActivity.this, message + "*wifi* status " + status, Toast.LENGTH_LONG).show();
                        }
                    }


                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        System.out.println(" newConfig" +newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE){
            Log.v("VideoInfoActivity","横屏");
            isLandScreen=true;
            setContentView(R.layout.videocamera_land);
        }else{
            Log.v("VideoInfoActivity","竖屏");
            isLandScreen=false;
            setContentView(R.layout.videocamera);
        }




        Log.e("" + getClass(), "普顺达视频....");

        initCustomActionBar();
        rightBtn.setVisibility(View.GONE);

        monitor  = (Monitor) findViewById(R.id.monitor);
        monitor.setPTZ(false);

        progressHUD= KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("视频加载中...")
                .setAnimationSpeed(1)
                .setDimAmount(0.3f)
                .show();



        connectVideoToMonitor("MyVideo", getIntent().getStringExtra("uid"),
                getIntent().getStringExtra("account"), getIntent().getStringExtra("password"));

        if(isLandScreen){
            mActionbar.hide();
        }else{
            mActionbar.show();
        }
    }




    private  void  connectVideoToMonitor(String name,String uid,String account,String pass){
        if(uid==null||uid.trim().equals("")){
            camera = new MyCamera("Camera", "PRAD13G9VNCB43AT111A","admin","admin");
        }else{
            camera = new MyCamera(name, uid,account,pass);
        }
        Camera.init();
        //8YM2LT63DMWXPBUG111A
        camera.registerIOTCListener(this);
        monitor.attachCamera(camera, 0);
        // Init and connect to camera
        camera.connect(camera.getUID());
        camera.start(0, "admin", camera.getPassword());
        camera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL, com.tutk.IOTC.AVIOCTRLDEFs.IOTYPE_USER_IPCAM_GETAUDIOOUTFORMAT_REQ, com.tutk.IOTC.AVIOCTRLDEFs.SMsgAVIoctrlGetAudioOutFormatReq.parseContent());
        camera.startShow(0, true, true);


        //camera.startListening(0, false);

        // Because of AEC, we recommend you opening neither speaking nor listening function.
        // camera.startSpeaking(0);



        // If you wanna send some IOCtrls to camera, you may call camera.sendIOCtrl(...) after starting camera.
        // ex.
        //camera.sendIOCtrl(0, com.tutk.IOTC.AVIOCTRLDEFs.IOTYPE_USER_IPCAM_GETSTREAMCTRL_REQ, com.tutk.IOTC.AVIOCTRLDEFs.SMsgAVIoctrlGetStreamCtrlReq.parseContent(0));


    }

    private void sendPTZCmd(int ptz){
        camera.sendIOCtrl(0,
                com.tutk.IOTC.AVIOCTRLDEFs.IOTYPE_USER_IPCAM_PTZ_COMMAND,
                com.tutk.IOTC.AVIOCTRLDEFs.SMsgAVIoctrlPtzCmd.parseContent((byte) ptz, (byte) 8, (byte) 0, (byte) 0, (byte) 0, (byte) 0));

    }
    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void onPause() {
        super.onPause();
        // Deattach this camera.
        monitor.deattachCamera();
        // Unregister this camera.
        camera.unregisterIOTCListener(this);
        // Stop all actions of camera and then disconnect.
        camera.stopListening(0);
        camera.stopShow(0);
        camera.stop(0);
        camera.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Uninit IOTCAPIs finally.
        Camera.uninit();
    }

    @Override
    public void receiveFrameData(final Camera mCamera, int avChannel, Bitmap bmp) {

        if (mCamera == camera) {
            if (bmp.getWidth() != mVideoWidth
                    || bmp.getHeight() != mVideoHeight) {
                mVideoWidth = bmp.getWidth();
                mVideoHeight = bmp.getHeight();

                reScaleMonitor();
            }
        }

    }
    private void reScaleMonitor(){

        if(mVideoHeight == 0 || mVideoWidth == 0)
            return;

        System.out.println("reScaleMonitor  mVideoHeight "+mVideoHeight+" |  mVideoWidth "+mVideoWidth);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int screenWidth = size.x;
        final int screenHeight = size.y;


        final SurfaceView surfaceView;
        surfaceView = (SurfaceView) monitor;

        if(surfaceView == null )
            return;
          /**
           * portrait mode
           */
        if(screenHeight >= screenWidth) {

            surfaceView.getLayoutParams().width = screenWidth;
            surfaceView.getLayoutParams().height = (int) (screenWidth * mVideoHeight / (float)mVideoWidth);


        }

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                surfaceView.setLayoutParams(surfaceView.getLayoutParams());
            }
        });

    }

    @Override
    public void receiveFrameDataForMediaCodec(Camera camera, int i, byte[] bytes, int i1, int i2, byte[] bytes1, boolean b, int i3) {

    }

    @Override
    public void receiveFrameInfo(Camera camera, int i, long l, int i1, int i2, int i3, int i4) {
        //Log.v("video_frame", "receiveFrameInfo");

    }

    @Override
    public void receiveSessionInfo(Camera camera, int i) {
        Log.v("video_frame", "receiveSessionInfo  status:" + i);
		Message msg=new Message();
        msg.what=i;
        videoHandler.sendMessage(msg);

    }

    @Override
    public void receiveChannelInfo(Camera camera, int i, int i1) {
        Log.v("video_frame", "receiveChannelInfo");
    }

    @Override
    public void receiveIOCtrlData(final Camera camera, int sessionChannel, int avIOCtrlMsgType, byte[] data) {
        Log.v("video_frame", "receiveIOCtrlData sessionChannel " + sessionChannel + " : data " + data);
        Bundle bundle = new Bundle();
        bundle.putInt("sessionChannel", sessionChannel);
        bundle.putByteArray("data", data);

        Message msg = new Message();
        msg.what = avIOCtrlMsgType;
        msg.setData(bundle);
        videoHandler.sendMessage(msg);
    }
    /**
     * @param data
     * @return
     */
    private static String getString(byte[] data) {

        StringBuilder sBuilder = new StringBuilder();

        for (int i = 0; i < data.length; i++) {

            if (data[i] == 0x0)
                break;

            sBuilder.append((char) data[i]);
        }

        return sBuilder.toString();
    }

}
