package space.cc.com.fragmenttest.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.domain.util.ToastUtils;

public class MapLocationActivity extends BaseActivity {

    public LocationClient locationClient;
    @BindView(R.id.position_txt)
    public TextView positionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {


        super.onCreate(savedInstanceState);
            ToastUtils.showDisplay("0");


        setContentView(R.layout.activity_map_location);
        ToastUtils.showDisplay("1");
        ButterKnife.bind(this);
            ToastUtils.showDisplay("2");

            locationClient = new LocationClient(getApplicationContext());
            locationClient.registerLocationListener(new MyLocationListener());
            ToastUtils.showDisplay("3");

            List<String> permissionList=new ArrayList<>();
        checkPermissonIfNoneAddToList(permissionList, Manifest.permission.ACCESS_FINE_LOCATION);
        checkPermissonIfNoneAddToList(permissionList,Manifest.permission.READ_PHONE_STATE);
        checkPermissonIfNoneAddToList(permissionList,Manifest.permission.WRITE_EXTERNAL_STORAGE);
            ToastUtils.showDisplay("4");

            if(!permissionList.isEmpty()){
             String[] permissions=permissionList.toArray((new String[permissionList.size()]));
             ActivityCompat.requestPermissions(MapLocationActivity.this,permissions,1);
         }else{
             requestLocation();
         }
         ToastUtils.showDisplay("5");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestLocation() {
        locationClient.start();
    }

    private void checkPermissonIfNoneAddToList(List<String> permissionList,String permission) {
        if(ContextCompat.checkSelfPermission(MapLocationActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(permission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        dealRequestResult(requestCode,grantResults);
    }

    private void dealRequestResultTwo(int requestCode, int[] grantResults) {
        switch(requestCode){
            case 1:
                if(grantResults.length>0){
                    for (int i = 0; i < grantResults.length; i++) {
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            ToastUtils.showDisplay("必须同意所有权限才可使用本程序");
                            finish();
                            return;
                        }
                    }
                  requestLocation();;
                }else{
                    ToastUtils.showDisplay("发生未知错误");
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void requestPermission() {

    }
    public class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder sb=new StringBuilder();
            sb.append("维度：").append(bdLocation.getLatitude()).append("\n");
            sb.append("经度：").append(bdLocation.getAltitude()).append("\n");
            sb.append("定位方式：");
            if(bdLocation.getLocType()==BDLocation.TypeGpsLocation){
                sb.append("GPS");
            }else if(bdLocation.getLocType()==BDLocation.TypeNetWorkLocation){
                sb.append("网络");
            }
            positionText.setText(sb);
        }
    }
}
