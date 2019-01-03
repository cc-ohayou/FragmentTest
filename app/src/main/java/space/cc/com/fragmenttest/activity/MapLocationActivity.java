package space.cc.com.fragmenttest.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

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

    @BindView(R.id.bmapView)
    public MapView mapView;

    public BaiduMap baiduMap;
    private boolean isFirstLocate=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_map_location);
            ButterKnife.bind(this);
            locationClient = new LocationClient(getApplicationContext());
            locationClient.registerLocationListener(new MyLocationListener());
            List<String> permissionList = new ArrayList<>();
//            SDKInitializer.initialize(this);//放在MyApplication中完成
            checkPermissonIfNoneAddToList(permissionList, Manifest.permission.ACCESS_FINE_LOCATION);
            checkPermissonIfNoneAddToList(permissionList, Manifest.permission.READ_PHONE_STATE);
            checkPermissonIfNoneAddToList(permissionList, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            baiduMap=mapView.getMap();
            //我的位置标记显示的功能开启
            baiduMap.setMyLocationEnabled(true);
            //开启城市热力图 人群分布  可做一个自定义tab切换查看不同的地图形态
//            baiduMap.setBaiduHeatMapEnabled(true);
            //开启交通图
//            baiduMap.setTrafficEnabled(true);
            //普通地图
            baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            //卫星地图
//            baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            //空白地图 无地图瓦片,地图将渲染为空白地图。不加载任何图块，
            // 将不会使用流量下载基础地图瓦片图层。支持叠加任何覆盖物
//            baiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);


            if (!permissionList.isEmpty()) {
                String[] permissions = permissionList.toArray((new String[permissionList.size()]));
                ActivityCompat.requestPermissions(MapLocationActivity.this, permissions, 1);
            } else {
                requestLocation();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestLocation() {
        initLocation();
        locationClient.start();
    }

    private void initLocation() {
        LocationClientOption option=new LocationClientOption();
        //位置刷新间隔 5秒钟一次
        option.setScanSpan(5000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //虎丘详细的位置信息 必须连接网络 即便上面的定位模式设置为Device_Sensors 也照样会开启网路定位
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //必须停止 否则后台会严重消耗手机电量
        locationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    private void checkPermissonIfNoneAddToList(List<String> permissionList, String permission) {
        if (ContextCompat.checkSelfPermission(MapLocationActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(permission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        dealRequestResultTwo(requestCode, grantResults);
    }

      /**
         * @description 多个权限处理
         * @author CF
         * created at 2018/12/11/011  23:14
         */
    private void dealRequestResultTwo(int requestCode, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            ToastUtils.showDisplay("必须同意所有权限才可使用本程序");
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                    ;
                } else {
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

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder sb = new StringBuilder();
            sb.append("维度：").append(bdLocation.getLatitude()).append("\n");
            sb.append("经度：").append(bdLocation.getAltitude()).append("\n");
            sb.append("国家：").append(bdLocation.getCountry()).append("\n");
            sb.append("省：").append(bdLocation.getProvince()).append("\n");
            sb.append("市：").append(bdLocation.getCity()).append("\n");
            sb.append("区：").append(bdLocation.getDistrict()).append("\n");
            sb.append("街道：").append(bdLocation.getStreet()).append("\n");
            sb.append("定位方式：");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("GPS");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("网络");
            }
            positionText.setText(sb);

            if(bdLocation.getLocType() == BDLocation.TypeGpsLocation||
                    bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                navigateTo(bdLocation);
            }

        }
    }
  /**
     * @description 定位到当前位置
     * @author CF
     * created at 2018/12/11/011  23:11
     */
    private void navigateTo(BDLocation bdLocation) {
        if(isFirstLocate){
            LatLng nowLocationLL=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            MapStatusUpdate update= MapStatusUpdateFactory.newLatLng(nowLocationLL);
            baiduMap.animateMapStatus(update);
            update=MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate=false;
        }
        MyLocationData.Builder builder=new MyLocationData.Builder();
        builder.latitude(bdLocation.getLatitude());
        builder.longitude(bdLocation.getLongitude());
        MyLocationData locationData=builder.build();
        baiduMap.setMyLocationData(locationData);


    }



    /**
     * 设置底图显示模式
     *
     * @param view
     */
    public void setMapMode(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.normal:
                if (checked) {
                    baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                }
                break;
            case R.id.statellite:
                if (checked) {
                    baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置是否显示交通图
     *
     * @param view
     */
    public void setTraffic(View view) {
        baiduMap.setTrafficEnabled(((CheckBox) view).isChecked());
    }

    /**
     * 设置是否显示百度热力图
     *
     * @param view
     */
    public void setBaiduHeatMap(View view) {
        baiduMap.setBaiduHeatMapEnabled(((CheckBox) view).isChecked());
    }
}
