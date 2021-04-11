package ddwu.mobile.final_project.ma01_20170993.MemoryRegistration;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.storage.ImageUploadResponse;
import com.kakao.util.helper.log.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ddwu.mobile.final_project.ma01_20170993.FileManager.ImageFileManager;
import ddwu.mobile.final_project.ma01_20170993.R;

public class ViewMemoryActivity extends AppCompatActivity implements Button.OnTouchListener{

    TextView etTitle;
    TextView etMemo;
    Button btnDate;
    ImageView imageView;
    Button btnShare;
    Button btnUpdate;

    MemoryDBHelper helper;
    MemoryDto dto;

    private ImageFileManager imageFileManager = null;

    private static final int UPDATE_RESULT_CODE = 2;

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memory);

        // 툴바
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        etTitle = findViewById(R.id.editTitle);
        etMemo = findViewById(R.id.editMemo);
        btnDate = findViewById(R.id.btnDate);
        imageView = findViewById(R.id.imageView);
        etMemo.setMovementMethod(new ScrollingMovementMethod());

        helper = new MemoryDBHelper(this);
        imageFileManager = new ImageFileManager(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + MemoryDBHelper.TABLE_NAME + " where _id=" + intent.getStringExtra("id"), null);
        dto = new MemoryDto();
        while(cursor.moveToNext()){
            dto.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            dto.setTitle(cursor.getString(cursor.getColumnIndex(MemoryDBHelper.COL_TITLE)));
            dto.setMemo(cursor.getString(cursor.getColumnIndex(MemoryDBHelper.COL_MEMO)));
            dto.setDate(cursor.getString(cursor.getColumnIndex(MemoryDBHelper.COL_DATE)));
            dto.setImage(cursor.getString(cursor.getColumnIndex(MemoryDBHelper.COL_IMAGE)));
        }
        cursor.close();
        helper.close();

        etTitle.setText(dto.getTitle());
        etMemo.setText(dto.getMemo());
        btnDate.setText(dto.getDate());
        if(dto.getImage() != null){
            Bitmap bitmap = imageFileManager.getSavedBitmapFromInternal(dto.getImage());
            if(bitmap != null){
                imageView.setImageBitmap(bitmap);
            }else{
                imageView.setImageResource(R.drawable.photo2);
            }
        }else{
            imageView.setImageResource(R.drawable.photo2);
        }


        btnShare = findViewById(R.id.btnShare);
        btnUpdate = findViewById(R.id.btnUpdateMemory);
        btnShare.setOnTouchListener(this);
        btnUpdate.setOnTouchListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UPDATE_RESULT_CODE:
                    int flag = (data.getStringExtra("result").equals("OK")) ? 0 : 1;
                    if (flag == 0) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("result", "OK");
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Intent resultIntent2 = new Intent();
                        resultIntent2.putExtra("result", "CANCEL");
                        setResult(RESULT_OK, resultIntent2);
                        finish();
                    }

                    break;
            }
        }
    }

    // 툴바 버튼 클릭
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* 네트워크 관련 메소드 */
    /* 네트워크 환경 조사 */
    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Button i = null;
        int tmp = -1;
        switch (v.getId()) {
            case R.id.btnShare:
                i = btnShare;
                tmp = 0;
                break;
            case R.id.btnUpdateMemory:
                i = btnUpdate;
                tmp = 1;
                break;
        }
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                i.getBackground().setColorFilter(0x66ffffff, PorterDuff.Mode.SRC_ATOP);
                break;
            case MotionEvent.ACTION_UP:
                i.getBackground().clearColorFilter();
                if(tmp == 0) {
                    if(!isOnline()){
                        Toast.makeText(ViewMemoryActivity.this, "인터넷을 연결해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        File imageFile = null;
                        if (dto.getImage() != null)
                            imageFile = new File(imageFileManager.getSavedPathFromInternal(dto.getImage()));
                        else {
                            imageFile = new File(imageFileManager.getSavedPathFromInternal("noImage"));
                        }
                        KakaoLinkService.getInstance().uploadImage(this, false, imageFile, new ResponseCallback<ImageUploadResponse>() {
                            @Override
                            public void onFailure(ErrorResult errorResult) {
                                Logger.e(errorResult.toString());
                            }

                            @Override
                            public void onSuccess(ImageUploadResponse result) {
                                FeedTemplate params = FeedTemplate
                                        .newBuilder(ContentObject.newBuilder(dto.getTitle().toString(),
                                                result.getOriginal().getUrl(),
                                                LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                                        .setMobileWebUrl("https://developers.kakao.com").build())
                                                .setDescrption(dto.getDate())
                                                .build())
                                        .build();

                                Map<String, String> serverCallbackArgs = new HashMap<String, String>();
                                serverCallbackArgs.put("user_id", "${current_user_id}");
                                serverCallbackArgs.put("product_id", "${shared_product_id}");

                                KakaoLinkService.getInstance().sendDefault(ViewMemoryActivity.this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        Logger.e(errorResult.toString());
                                    }

                                    @Override
                                    public void onSuccess(KakaoLinkResponse result) {
                                        // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
                                    }
                                });
                            }
                        });
                    }
                } else if(tmp == 1){
                    Intent intent = new Intent(ViewMemoryActivity.this, UpdateMemoryActivity.class);
                    intent.putExtra("id", id);
                    startActivityForResult(intent, UPDATE_RESULT_CODE);
                }
                break;
        }
        return true;
    }
}
