package space.cc.com.fragmenttest.activity.media;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.activity.BaseActivity;

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
    /*此处使用应用关联缓存存放图片 android6.0开始 读写SD卡被列为了危险权限
   任何直接存储到sd卡的操作都需要申请运行时权限  使用应用关联目录则可以跳过这一步

     * */
        File outPutImage = new File(getExternalCacheDir(), "out_put_image.jpg");
        try {
            if (outPutImage.exists()) {
                outPutImage.delete();
            }
            outPutImage.createNewFile();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
/*
            低于android7.0的设备需要用Uri的fromFile方法将File对象转换为Uri对象
            Uri对象标识着output_image.jpg这张图片的本地真实路径
            7.0以后直接使用本地真实路径被认为是不安全的 所以通过getUriForFile
            获取一个转换过的不是本地真实路径但可以关联到真实路径的Uri对象
            总之一切为了更加安全
*/
            imageUri = FileProvider.getUriForFile(CameraAlbumActivity.this,
                    getString(R.string.fileProvider), outPutImage);
        } else {
            imageUri = Uri.fromFile(outPutImage);
        }
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

                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4以及以上版本使用这个方法处理图片
                        handleImageOnKitKat(data);

                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = "";
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                toastSimple("media.documents");
                //解析出数字格式的id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //contnet类型普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //file类型直接获取图片路径
            imagePath = uri.getPath();
        }
        //根据图片路径展示图片
        displayImage(imagePath);

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
  /**
     * @description  获取图片路径
     * @author CF
     * created at 2018/11/26/026  0:11
     */
    private String getImagePath(Uri uri, String selection) {
        String path = "";
        //通过Uri和selection来获取真实图片路径
        Cursor cursor=getContentResolver().query(uri,null,selection,
                null,null);
        if(cursor!=null){
            if(cursor.moveToNext()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            }
            cursor.close();
        }
        return path;
    }
  /**
     * @description
     * @author CF
     * created at 2018/11/26/026  0:15
     */
    private void handleImageBeforeKitKat(Intent data) {
        toastSimple(" handleImageBeforeKitKat");

        Uri uri=data.getData();
           String imagePath=getImagePath(uri,null);
           displayImage(imagePath);

    }


}
