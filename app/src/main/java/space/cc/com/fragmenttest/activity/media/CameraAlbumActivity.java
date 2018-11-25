package space.cc.com.fragmenttest.activity.media;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.activity.BaseActivity;

public class CameraAlbumActivity extends BaseActivity implements View.OnClickListener {

    private final int TAKE_PHOTO=1;
    private final int SELECT_LOCAL_IMAGE=2;
    private ImageView picture;
    private Uri imageUri;
    private String cameraAction="android.media.action.IMAGE_CAPTURE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_album);
        ImageButton takePhoto = findViewById(R.id.take_photo);
        picture=findViewById(R.id.my_pic);
        //在程序中设置图标
        takePhoto.setImageDrawable(getResources().getDrawable(R.drawable.take_photo_32));
        takePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                /*此处使用应用关联缓存存放图片 android6.0开始 读写SD卡被列为了危险权限
               任何直接存储到sd卡的操作都需要申请运行时权限  使用应用关联目录则可以跳过这一步

                 * */
                File outPutImage = new File(getExternalCacheDir(),"out_put_image.jpg");
                try {
                     if(outPutImage.exists()){
                         outPutImage.delete();
                     }
                     outPutImage.createNewFile();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(Build.VERSION.SDK_INT>=24){
/*
                    低于android7.0的设备需要用Uri的fromFile方法将File对象转换为Uri对象
                    Uri对象标识着output_image.jpg这张图片的本地真实路径
                    7.0以后直接使用本地真实路径被认为是不安全的 所以通过getUriForFile
                    获取一个转换过的不是本地真实路径但可以关联到真实路径的Uri对象
                    总之一切为了更加安全
*/
                    imageUri = FileProvider.getUriForFile(CameraAlbumActivity.this,
                            getString(R.string.fileProvider),outPutImage);
                }else{
                    imageUri =Uri.fromFile(outPutImage);
                }
                Intent intent=new Intent(cameraAction);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
                break;
            default:
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            switch (requestCode){
                case TAKE_PHOTO:
                    if(resultCode==RESULT_OK){
                        try {
                        //显示拍摄的照片
                            Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver()
                                    .openInputStream(imageUri));
                            picture.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
        }

    }
}
