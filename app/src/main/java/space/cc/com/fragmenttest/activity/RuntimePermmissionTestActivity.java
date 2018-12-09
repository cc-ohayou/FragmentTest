package space.cc.com.fragmenttest.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import space.cc.com.fragmenttest.R;

public class RuntimePermmissionTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runtime_permmission_test);
        Button callPhone = findViewById(R.id.phone_call);
        callPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (ActivityCompat.checkSelfPermission(RuntimePermmissionTestActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RuntimePermmissionTestActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},1);
                    }else{
                        makePhoneCall();
                    }
            }
        });
    }

    @Override
    public void requestPermission() {

    }

    @SuppressLint("MissingPermission")
    private void makePhoneCall() {
        try{
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int reqCode,String[] permmissions,int[] grantResults){
        switch(reqCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    makePhoneCall();
                }else{
                    toastSimple("You denied the permmission!");
                }
        }
    }
}
