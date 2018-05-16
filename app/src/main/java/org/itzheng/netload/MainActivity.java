package org.itzheng.netload;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.itzheng.net.http.helper.HttpFullHelper;
import org.itzheng.net.http.proxy.callback.impl.HttpRequestCallback;
import org.itzheng.net.http.proxy.callback.impl.HttpCallback;
import org.itzheng.net.http.proxy.impl.okhttp.OkHttpHttpFull;
import org.itzheng.net.image.ImageUtils;
import org.itzheng.net.image.helper.ImageLoaderHelper;
import org.itzheng.net.image.proxy.impl.glide.GlideImageLoader;
import org.itzheng.net.thread.ThreadUtils;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    ImageView ivImage;
    //    String imageUrl = "https://image.baidu.com/search/down?tn=download&word=download&ie=utf8&fr=detail&url=https%3A%2F%2Ftimgsa.baidu.com%2Ftimg%3Fimage%26quality%3D80%26size%3Db9999_10000%26sec%3D1525861617232%26di%3Df8f4ddec2069ef00d984360ca05806de%26imgtype%3D0%26src%3Dhttp%253A%252F%252Fi4.download.fd.pchome.net%252Fg1%252FM00%252F12%252F04%252FoYYBAFZS2uaIR_NiADNPLO6oiewAACx_gAAyJkAM09E943.jpg&thumburl=https%3A%2F%2Fss1.bdstatic.com%2F70cFvXSh_Q1YnxGkpoWK1HF6hhy%2Fit%2Fu%3D3252027710%2C3930353505%26fm%3D27%26gp%3D0.jpg";
    String imageUrl = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_ca79a146.png";
    String url1 = "https://github.com/itzheng/RingModule/blob/master/README.md";
    String url2 = "https://github.com/itzheng/ActivityModule/blob/master/README.md";
    String url3 = "https://github.com/itzheng/BaseUtils/blob/master/README.md";
    TextView tvRunSteps;
    String postUrl = "http://192.168.4.127:8081/api/db_query";
    String getUrl = "http://v.juhe.cn/toutiao/index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivImage = findViewById(R.id.ivImage);
        tvRunSteps = findViewById(R.id.tvRunSteps);
//        ThreadUtils.execute(new Runnable() {
//            @Override
//            public void run() {
////                testCallback();
//            }
//        });
        testCallback();
        testRun();
        testGetRequest();
        testImages();
//        testImageLoader();
        testPostRequest();

    }

    private void testGetRequest() {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "4c2fb4a2fe6938af1e5bed8dfe3eda88");
        params.put("type", "top");
        HttpFullHelper.getInstance().get(getUrl)//.addParam("type", "top").addParam("key", "4c2fb4a2fe6938af1e5bed8dfe3eda88")
                .addParams(params)
                .setHttpCallBack(new HttpRequestCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d(TAG, "onSuccess: " + response);
                    }

                    @Override
                    public void onError(String error, Exception e) {
                        Log.w(TAG, "onError: " + error);

                    }
                }).exec();
    }

    private void testPostRequest() {
        String json = "{\n" +
                "\"datasource\": \"dsERP\",\n" +
                " \"list\": [\n" +
                "   {\n" +
                "      \"id\": \"dsERP\",\n" +
                "       \"sql\": \"select count(*) from Nfirm where  (1=1) and (1=1)\"\n" +
                "     }\n" +
                "   ],\n" +
                "   \"response\": \"json\"\n" +
                " }";
//        json = null;
        HttpFullHelper.init(new OkHttpHttpFull());
        HttpFullHelper.getInstance().post(postUrl).addJson(json).setHttpCallBack(new HttpRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "onSuccess: " + response);
            }

            @Override
            public void onError(String error, Exception e) {
                Log.w(TAG, "onError: " + error);
            }
        }).exec();
    }

    private void testCallback() {
        HttpFullHelper.init(new OkHttpHttpFull());
        HttpFullHelper.getInstance().get(imageUrl).setHttpCallBack(new HttpRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "onSuccess: " + response);
            }

            @Override
            public void onError(String error, Exception e) {
                Log.e(TAG, "onError: " + error);
            }

            @Override
            public void onCancel(String error) {
                Log.d(TAG, "onCancel: ");
            }
        }).setTag(MainActivity.this).exec();
        HttpFullHelper.getInstance().get(url2).setHttpCallBack(new HttpRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "onSuccess: " + response);
            }

            @Override
            public void onError(String error, Exception e) {
                Log.e(TAG, "onError: " + error);
            }

        }).exec();

        HttpFullHelper.getInstance().get(url3).setHttpCallBack(new HttpRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "onSuccess: " + response);
            }

            @Override
            public void onError(String error, Exception e) {
                Log.e(TAG, "onError: " + error);
            }
        }).exec();
        ThreadUtils.executeDelayed(new Runnable() {
            @Override
            public void run() {
                HttpFullHelper.getInstance().cancel(MainActivity.this);
            }
        }, 100);


    }

    SensorManager mSensorManager;
    Sensor mStepCount;
    Sensor mStepDetector;

    private void testRun() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //单次有效计步
        mStepCount = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        //系统计步累加值
        mStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        mSensorManager.registerListener(this, mStepDetector, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepCount, SensorManager.SENSOR_DELAY_FASTEST);
        tvRunSteps.setText("mStepCount==null " + (mStepCount == null) + " , mStepDetector ==null " + (mStepDetector == null));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this, mStepDetector);
            mSensorManager.unregisterListener(this, mStepCount);
        }

    }

    private static final String TAG = "MainActivity";

    private void testImages() {
        HttpFullHelper.getInstance().get(imageUrl)
                .setHttpCallBack(new HttpRequestCallback() {

                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = ImageUtils.Bytes2Bimap(bytes);
//                        设置到bitmap里面去，加载网络获取的图片
                        ivImage.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(String error, Exception e) {
                        Log.e(TAG, "onError: " + error);
                    }

                    @Override
                    public void onCancel(String error) {

                    }

                }).exec();
    }


    private void testImageLoader() {
        CircularProgressDrawable drawable = new CircularProgressDrawable(getApplicationContext());
        drawable.setColorSchemeColors(Color.RED, Color.BLACK, Color.YELLOW);
        drawable.setStrokeWidth(20);
        drawable.setArrowEnabled(true);
        drawable.setCenterRadius(750 / 3 - 20);
//        drawable.setBounds(50, 50, 50, 50);
        drawable.start();
        ImageLoaderHelper.init(new GlideImageLoader().setDefPlace(drawable));
        ImageLoaderHelper.getInstance().error(new DrawerArrowDrawable(this)).place(drawable).load(imageUrl).into(ivImage);
//        ImageLoaderHelper.getInstance().with(this).load(imageUrl).into(ivImage);
//        ImageLoaderHelper.getInstance().load(R.drawable.ic_launcher_background).into(ivImage);
    }

    int count;

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "SensorEvent:" + event);

        if (event.values[0] == 1) {
            count++;
        } else {
            count = (int) event.values[0];
        }
        tvRunSteps.setText("运动步数：" + count);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "Sensor:" + sensor + " ,accuracy:" + accuracy);
    }
}
