package ddwu.mobile.final_project.ma01_20170993.MemoryRegistration;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.InputStream;

import ddwu.mobile.final_project.ma01_20170993.FileManager.ImageFileManager;
import ddwu.mobile.final_project.ma01_20170993.R;

public class UpdateMemoryActivity extends AppCompatActivity implements Button.OnTouchListener {

    EditText etTitle;
    EditText etMemo;
    Button btnDate;
    Button btnRotate;
    ImageView imageView;

    Bitmap img;
    Bitmap changeImg;
    MemoryDBHelper helper;
    MemoryDto dto;
    private ImageFileManager imageFileManager = null;
    private static final int PICTURE_RESULT_CODE = 0;

    // 이미지 선택 여부
    int flag = 0;
    //각도
    int an = 0;
    // 이미지 변환 여부
    int original_flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_memory);

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
        btnDate.setOnClickListener(mDateListener);

        helper = new MemoryDBHelper(this);
        imageFileManager = new ImageFileManager(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
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
                flag = 1;
                imageView.setImageBitmap(bitmap);
            }else{
                imageView.setImageResource(R.drawable.photo2);
            }
        }else{
            imageView.setImageResource(R.drawable.photo2);
        }

        // 이미지 뷰 클릭 : 갤러리에서 사진 가져오기
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICTURE_RESULT_CODE);
            }
        });

        btnRotate = findViewById(R.id.btnRotate);
        btnRotate.setOnTouchListener(this);

        flag = 0;
        original_flag = 0;
    }

    // 날짜 버튼 클릭 : datePicker
    Button.OnClickListener mDateListener = new Button.OnClickListener(){
        public void onClick(View v){
            new DatePickerDialog(UpdateMemoryActivity.this, R.style.DialogTheme, mDateSetListener, Integer.parseInt(btnDate.getText().toString().split("-")[0]), Integer.parseInt(btnDate.getText().toString().split("-")[1]) - 1, Integer.parseInt(btnDate.getText().toString().split("-")[2])).show();
        }
    };

    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            btnDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
        }
    };

    // 툴바 : 오른쪽에 저장 버튼 표시
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_save, menu);
        return true;
    }

    // 뷰의 크기에 맞춰 이미지 줄이기
    public Bitmap getResizedBitmap(Bitmap bm, int layoutWidth, int layoutHeight){
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) layoutWidth) / width;
        float scaleHeight = ((float) layoutHeight) / height;
        Matrix matrix = new Matrix();

        if(width > layoutWidth && height > layoutHeight){
            matrix.postScale(scaleWidth, scaleHeight);
        }else if(width > layoutWidth){
            matrix.postScale(scaleWidth, scaleWidth);
        }else if(height > layoutHeight){
            matrix.postScale(scaleHeight, scaleHeight);
        }else{
            return bm;
        }

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                flag = 1;
                an = 0;
                try {
                    if (dto.getImage() != null) {
                        String fileName = Uri.parse(dto.getImage()).getLastPathSegment();
                        File file = new File(UpdateMemoryActivity.this.getFilesDir() + "/" + fileName);
                        file.delete();
                    }
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    img = BitmapFactory.decodeStream(in);
                    in.close();
                    img = getResizedBitmap(img, imageView.getWidth(), imageView.getHeight());
                    imageView.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    // 이미지 회전
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.toolbar_save:
                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues row = new ContentValues();
                row.put(MemoryDBHelper.COL_TITLE, etTitle.getText().toString());
                row.put(MemoryDBHelper.COL_DATE, btnDate.getText().toString());
                row.put(MemoryDBHelper.COL_MEMO, etMemo.getText().toString());

                if(original_flag == 1){
                    String fileName = Uri.parse(dto.getImage()).getLastPathSegment();
                    File file = new File(UpdateMemoryActivity.this.getFilesDir() + "/" + fileName);
                    file.delete();
                }

                if(flag == 1 || original_flag == 1) {
                    String fileName = imageFileManager.saveBitmapToInternal(((BitmapDrawable) imageView.getDrawable()).getBitmap());
                    String path = getFilesDir().getPath() + "/" + fileName;
                    row.put(MemoryDBHelper.COL_IMAGE, path);
                }

                String whereClause  = "_id=?";
                String[] whereArgs = new String[]{Long.toString(dto.getId())};
                db.update(MemoryDBHelper.TABLE_NAME, row, whereClause, whereArgs);

                helper.close();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "OK");
                setResult(RESULT_OK, resultIntent);
                finish();
                break;
            case android.R.id.home:
                // DB 데이터 업데이트 작업 취소
                Intent resultIntent2 = new Intent();
                resultIntent2.putExtra("result", "CANCEL");
                setResult(RESULT_OK, resultIntent2);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Button i = null;
        int tmp = -1;
        switch (v.getId()) {
            case R.id.btnRotate:
                i = btnRotate;
                tmp = 0;
                break;
        }
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                i.getBackground().setColorFilter(0x66ffffff, PorterDuff.Mode.SRC_ATOP);
                break;
            case MotionEvent.ACTION_UP:
                i.getBackground().clearColorFilter();
                if(tmp == 0) {
                    if(flag == 0 && dto.getImage() != null){
                        original_flag = 1;
                        String filePath = imageFileManager.getSavedPathFromInternal(dto.getImage());
                        img = BitmapFactory.decodeFile(filePath);
                    }
                    if(flag == 1 || (flag == 0 && dto.getImage() != null)) {
                        an += 90;
                        an = an == 360? 0: an;
                        changeImg = rotateImage(img, an);
                        imageView.setImageBitmap(changeImg);
                    }
                }
                break;
        }
        return true;
    }
}
