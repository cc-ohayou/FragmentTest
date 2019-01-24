package space.cc.com.fragmenttest.task;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.net.URL;

import space.cc.com.fragmenttest.domain.util.StringUtils;

public class ChangeViewBackgroundTask extends AsyncTask<String, Void, Drawable> {
    private View view;
    private String imgName;

    public ChangeViewBackgroundTask(View view, String imgName) {
        super();
        this.view = view;
        this.imgName = imgName;
    }

    @Override
    protected Drawable doInBackground(String... urls) {
        return loadImageFromNetwork(urls[0]);
    }

    @Override
    protected void onPostExecute(Drawable result) {
        if (result != null) {
            view.setBackground(result);
        }
    }

    private Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            if(StringUtils.isEmpty(imageUrl)){
                return null;
            }
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(
                    new URL(imageUrl).openStream(), imgName);
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }

        return drawable;
    }
}
