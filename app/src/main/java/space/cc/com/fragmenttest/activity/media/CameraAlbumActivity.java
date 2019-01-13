package space.cc.com.fragmenttest.activity.media;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.activity.BaseActivity;
import space.cc.com.fragmenttest.util.UtilBox;

public class CameraAlbumActivity extends BaseActivity implements View.OnClickListener {

    private final int TAKE_PHOTO = 1;
    private final int SELECT_LOCAL_IMAGE = 2;
    private ImageView picture;
    private Uri imageUri;
    private String cameraAction = "android.media.action.IMAGE_CAPTURE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_album);
        ImageButton takePhoto = findViewById(R.id.take_photo);
        ImageButton chooseFromAlbum = findViewById(R.id.choose_from_album);
        picture = findViewById(R.id.my_pic);
        //在程序中设置图标
        takePhoto.setImageDrawable(getResources().getDrawable(R.drawable.take_photo_32));
        takePhoto.setOnClickListener(this);
        chooseFromAlbum.setImageDrawable(getResources().getDrawable(R.drawable.album_48));
        chooseFromAlbum.setOnClickListener(this);
    }

    @Override
    public void requestPermission() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                takePhoto();

                break;
            case R.id.choose_from_album:
                choosePicFromAlbum();
            default:
                break;

        }
    }

    private void choosePicFromAlbum() {
        if (ContextCompat.checkSelfPermission(CameraAlbumActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraAlbumActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            toastSimple("choosePicFromAlbum openAlbum");
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_LOCAL_IMAGE);
    }

    private void takePhoto() {
        String picName="photo_out_put_image.jpg";
        imageUri=UtilBox.box().want.getFileUriByName(picName);

        Intent intent = new Intent(cameraAction);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    toastSimple("You denied the permission");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //显示拍摄的照片
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case SELECT_LOCAL_IMAGE:
                toastSimple("SELECT_LOCAL_IMAGE click success version="
                        +Build.VERSION.SDK_INT+",resultCode="+resultCode );
                String imagePath="";
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4以及以上版本使用这个方法处理图片
                        imagePath= UtilBox.box().want.handleImageOnKitKat(data);
                        //根据图片路径展示图片

                    } else {
                        imagePath=  UtilBox.box().want.handleImageBeforeKitKat(data);
                    }
                    displayImage(imagePath);
                }
        }
    }


  /**
     * @description 展示图片
     * @author CF
     * created at 2018/11/26/026  0:12
     */
    private void displayImage(String imagePath) {
        toastSimple("imagePath="+imagePath);
           if(imagePath!=null&&!"".equals(imagePath)){
               Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
               picture.setImageBitmap(bitmap);
           }else{
               toastSimple("failed to get image");
           }
    }


}
