package org.itzheng.netload;

import android.graphics.Color;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.widget.ImageView;

import org.itzheng.net.image.helper.ImageLoaderHelper;
import org.itzheng.net.image.proxy.impl.glide.GlideImageLoader;

public class MainActivity extends AppCompatActivity {
    ImageView ivImage;
    String imageUrl = "https://image.baidu.com/search/down?tn=download&word=download&ie=utf8&fr=detail&url=https%3A%2F%2Ftimgsa.baidu.com%2Ftimg%3Fimage%26quality%3D80%26size%3Db9999_10000%26sec%3D1525861617232%26di%3Df8f4ddec2069ef00d984360ca05806de%26imgtype%3D0%26src%3Dhttp%253A%252F%252Fi4.download.fd.pchome.net%252Fg1%252FM00%252F12%252F04%252FoYYBAFZS2uaIR_NiADNPLO6oiewAACx_gAAyJkAM09E943.jpg&thumburl=https%3A%2F%2Fss1.bdstatic.com%2F70cFvXSh_Q1YnxGkpoWK1HF6hhy%2Fit%2Fu%3D3252027710%2C3930353505%26fm%3D27%26gp%3D0.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivImage = findViewById(R.id.ivImage);
        testImageLoader();
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

}
