package ddwu.mobile.final_project.ma01_20170993.FindEvent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ddwu.mobile.final_project.ma01_20170993.FileManager.ImageFileManager;
import ddwu.mobile.final_project.ma01_20170993.R;
import ddwu.mobile.final_project.ma01_20170993.ViewFavorites.FavoritesDBHelper;

public class EventDetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView tvPlace;
    TextView tvTime;
    TextView tvHome;
    TextView tvAge;
    Button btnFavorites;

    EventDetailXmlParser parser;
    EventDetailDto detailDto;
    EventDto d;

    FavoritesDBHelper helper;
    private ImageFileManager imageFileManager = null;

    int menuflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        parser = new EventDetailXmlParser();

        // 툴바
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        d = (EventDto) i.getSerializableExtra("event");
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvTel = findViewById(R.id.tvTel);
        TextView tvTerm = findViewById(R.id.tvTerm);

        imageView = findViewById(R.id.imageView);
        tvPlace = findViewById(R.id.tvAddr);
        tvTime = findViewById(R.id.tvTime);
        tvHome = findViewById(R.id.tvHome);
        tvAge = findViewById(R.id.tvAge2);
//        btnFavorites = findViewById(R.id.addFavorite);

        tvTitle.setText(d.getTitle());
        tvTel.setText(d.getTel());
        String date = d.getEventstartdate();
        d.setEventstartdate(date.substring(0,4) + "년" + date.substring(4,6) + "월" + date.substring(6,8) + "일");
        date = d.getEventenddate();
        d.setEventenddate(date.substring(0,4) + "년" + date.substring(4,6) + "월" + date.substring(6,8) + "일");
        tvTerm.setText(d.getEventstartdate() + "~" + d.getEventenddate());

        ImageAsyncTask imgTask = new ImageAsyncTask();
        DataAsyncTask dataTask = new DataAsyncTask();
        String apiAddress = getResources().getString(R.string.api_url5);
        String query = "&contentTypeId=" + d.getContentTypeId() + "&contentId=" + d.getContentid() + "&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&introYN=Y";
        try{
            imgTask.execute(d.getFirstimage());
            dataTask.execute(apiAddress + query);
        }catch(Exception e){
            e.printStackTrace();
        }

        helper = new FavoritesDBHelper(this);
        imageFileManager = new ImageFileManager(this);
    }

    // 툴바 : 오른쪽에 저장 버튼 표시
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_star, menu);
        menuflag = -1;
        invalidateOptionsMenu();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.toolbar_star:
                if(menuflag == 0) {
                    SQLiteDatabase db = helper.getWritableDatabase();

                    ContentValues row = new ContentValues();
                    row.put(FavoritesDBHelper.COL_TITLE, detailDto.getTitle());
                    if (detailDto.getImage() != null) {
                        String fileName = imageFileManager.saveBitmapToInternal(((BitmapDrawable) imageView.getDrawable()).getBitmap());
                        String path = getFilesDir().getPath() + "/" + fileName;
                        row.put(FavoritesDBHelper.COL_IMAGE, path);
                    }
                    row.put(FavoritesDBHelper.COL_IMAGELINK, detailDto.getImage());
                    row.put(FavoritesDBHelper.COL_ADDR, detailDto.getAddr());
                    row.put(FavoritesDBHelper.COL_PLACE, detailDto.getPlace());
                    row.put(FavoritesDBHelper.COL_TEL, detailDto.getTel());
                    row.put(FavoritesDBHelper.COL_TIME, detailDto.getTime());
                    row.put(FavoritesDBHelper.COL_HOME, detailDto.getHome());
                    row.put(FavoritesDBHelper.COL_AGE, detailDto.getAge());
                    row.put(FavoritesDBHelper.COL_CONTENTID, detailDto.getContentId());
                    row.put(FavoritesDBHelper.COL_CONTENTTYPEID, detailDto.getContentTypeId());
                    row.put(FavoritesDBHelper.COL_OPENDATE, detailDto.getEventstartdate());
                    row.put(FavoritesDBHelper.COL_ENDDATE, detailDto.getEventenddate());

                    db.insert(FavoritesDBHelper.TABLE_NAME, null, row);
                    helper.close();

                    Toast.makeText(EventDetailActivity.this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    invalidateOptionsMenu();
                }else{
                    SQLiteDatabase db = helper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("select * from " + FavoritesDBHelper.TABLE_NAME + " where contentId=" + detailDto.getContentId(), null);
                   String imageUrl = null;
                    while(cursor.moveToNext()){
                        imageUrl = cursor.getString(cursor.getColumnIndex(FavoritesDBHelper.COL_IMAGE));
                    }
                    cursor.close();
                    helper.close();
                    if(imageUrl != null) {
                        String fileName = Uri.parse(imageUrl).getLastPathSegment();
                        File file = new File(getFilesDir() + "/" + fileName);
                        file.delete();
                    }
                    db = helper.getWritableDatabase();
                    String whereClause = "contentId=?";
                    String[] whereArgs = new String[]{String.valueOf(detailDto.getContentId())};
                    db.delete(FavoritesDBHelper.TABLE_NAME, whereClause, whereArgs);
                    helper.close();

                    Toast.makeText(EventDetailActivity.this, "즐겨찾기에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    invalidateOptionsMenu();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if(menuflag == -1) {
            if (!isValue()) {
                menu.findItem(R.id.toolbar_star).setIcon(R.drawable.btn_star_none);
                menuflag = 0;
            } else {
                menu.findItem(R.id.toolbar_star).setIcon(R.drawable.btn_star);
                menuflag = 1;
            }
        }else if(menuflag == 0){
            menuflag = 1;
            menu.findItem(R.id.toolbar_star).setIcon(R.drawable.btn_star);
        }else if(menuflag == 0){
            menuflag = 0;
            menu.findItem(R.id.toolbar_star).setIcon(R.drawable.btn_star_none);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean isValue(){
        boolean result = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + FavoritesDBHelper.TABLE_NAME + " where contentId=" + d.getContentid() + " and contTypeId=" + d.getContentTypeId(), null);
        if(cursor.moveToNext()){
            result = true;
        }
        cursor.close();
        helper.close();
        return result;
    }

    /* 행사 정보를 다운로드 후 텍스트뷰에 표시하는 AsyncTask */
    class DataAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(EventDetailActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String result = downloadContents(address);
            if (result == null) return "Error!";
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            detailDto = parser.parse(result);

            if(detailDto != null){
                detailDto.setTitle(d.getTitle());
                detailDto.setImage(d.getFirstimage());
                detailDto.setAddr(d.getAddr1());
                detailDto.setTel(d.getTel());
                detailDto.setContentId(d.getContentid());
                detailDto.setContentTypeId(d.getContentTypeId());
                detailDto.setEventstartdate(d.getEventstartdate());
                detailDto.setEventenddate(d.getEventenddate());

                tvPlace.setText(detailDto.getAddr() + " " + detailDto.getPlace());
                tvTime.setText(detailDto.getTime());
                tvHome.setText(detailDto.getHome());
                tvAge.setText(detailDto.getAge());
            }

            progressDlg.dismiss();
        }
    }

    /* 행사 이미지를 다운로드 후 이미지뷰에 표시하는 AsyncTask */
    class ImageAsyncTask extends AsyncTask<String, Integer, Bitmap> {

        String address;

        @Override
        protected Bitmap doInBackground(String... strings) {
            address = strings[0];
            Bitmap result = downloadImage(address);
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.noimage);
            }
        }
    }

    /* 네트워크 환경 조사 */
    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /* 주소(address)에 접속하여 문자열 데이터를 수신한 후 반환 */
    protected String downloadContents(String address) {
        HttpURLConnection conn = null;
        InputStream stream = null;
        String result = null;

        try {
            URL url = new URL(address);
            conn = (HttpURLConnection)url.openConnection();
            stream = getNetworkConnection(conn);
            result = readStreamToString(stream);
            if (stream != null) stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) conn.disconnect();
        }

        return result;
    }

    /* 주소(address)에 접속하여 비트맵 데이터를 수신한 후 반환 */
    protected Bitmap downloadImage(String address) {
        HttpURLConnection conn = null;
        InputStream stream = null;
        Bitmap result = null;

        try {
            URL url = new URL(address);
            conn = (HttpURLConnection)url.openConnection();
            stream = getNetworkConnection(conn);
            result = readStreamToBitmap(stream);
            if (stream != null) stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) conn.disconnect();
        }

        return result;
    }

    /* URLConnection 을 전달받아 연결정보 설정 후 연결, 연결 후 수신한 InputStream 반환 */
    private InputStream getNetworkConnection(HttpURLConnection conn) throws Exception {
        conn.setReadTimeout(3000);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + conn.getResponseCode());
        }

        return conn.getInputStream();
    }

    /* InputStream을 전달받아 문자열로 변환 후 반환 */
    protected String readStreamToString(InputStream stream){
        StringBuilder result = new StringBuilder();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String readLine = bufferedReader.readLine();

            while (readLine != null) {
                result.append(readLine + "\n");
                readLine = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    /* InputStream을 전달받아 비트맵으로 변환 후 반환 */
    protected Bitmap readStreamToBitmap(InputStream stream) {
        return BitmapFactory.decodeStream(stream);
    }
}
