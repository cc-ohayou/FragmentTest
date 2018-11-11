package space.cc.com.fragmenttest.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.domain.bizobject.FileManager;
import space.cc.com.fragmenttest.domain.util.StringUtil;

public class FileTestActivity extends BaseActivity {

    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_test);
        editText=findViewById(R.id.input01);
        String text = load(FileManager.DATA_TEST);
        if(!StringUtil.isEmpty(text)){
            editText.setText(text);
            editText.setSelection(text.length());
            Toast.makeText(this, "restoring succed",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText=editText.getText().toString();
        save(inputText, FileManager.DATA_TEST);
    }






}
