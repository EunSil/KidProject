package ddwu.mobile.final_project.ma01_20170993.FindKindergarden;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ddwu.mobile.final_project.ma01_20170993.R;

public class KindergardenViewActivity extends AppCompatActivity {

    public static final String TAG = "KindergardenView";

    TextView toolbarTitle;
    TextView tvType;
    TextView tvOpenDate;
    TextView tvPhone;
    TextView tvHome;
    TextView tvSpec;
    TextView tvIsCar;
    TextView tvRoomCnt;
    TextView tvPlayCnt;
    TextView tvCctvCnt;

    String apiAddress;
    String query;
    KindergardenDto kindergardenDto;

    DetailKindergardenXmlParser parser;

    private LocationManager locManager;
    private GoogleMap mGoogleMap;
    private MarkerOptions options;

    DetailKindergardenDto dto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kindergarden_view);

        // 툴바
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        Intent intent = getIntent();
        kindergardenDto = (KindergardenDto) intent.getSerializableExtra("kindergarden");
        toolbarTitle.setText(kindergardenDto.getName());

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);

        apiAddress = getResources().getString(R.string.api_url2);
        parser = new DetailKindergardenXmlParser();

        TextView tvName = (TextView) findViewById(R.id.tvDisease);
        tvName.setText(kindergardenDto.getName());
        TextView tvAddr = (TextView) findViewById(R.id.tvAddress);
        tvAddr.setText(kindergardenDto.getAddress());
        tvType = (TextView) findViewById(R.id.tvType);
        tvOpenDate = (TextView) findViewById(R.id.tvOpenDate);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvHome = (TextView) findViewById(R.id.tvHome);
        tvSpec = (TextView) findViewById(R.id.tvSpec);
        tvIsCar = (TextView) findViewById(R.id.tvIsCar);
        tvRoomCnt = (TextView) findViewById(R.id.tvRoomCnt);
        tvPlayCnt = (TextView) findViewById(R.id.tvPlayCnt);
        tvCctvCnt = (TextView) findViewById(R.id.tvCctvCnt);

        options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        options.title(kindergardenDto.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 여기
        query = "&arcode=" + kindergardenDto.getStcode().substring(0, 5) + "&stcode=" + kindergardenDto.getStcode();
        new DetailKindergardenAsyncTask().execute(apiAddress + query);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            LatLng currentLoc = new LatLng(kindergardenDto.getLatitude(), kindergardenDto.getLongitude());
            options.position(currentLoc);
            mGoogleMap.addMarker(options);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
        }
    };

    class DetailKindergardenAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(KindergardenViewActivity.this, "Wait", "Downloading...");
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
            Log.i(TAG, result);

            dto = parser.parse(result);      // 파싱 수행
            //여기
            if(dto != null){
                tvType.setText(dto.getTypeName());
                tvOpenDate.setText(dto.getOpenDate());
                tvPhone.setText(dto.getPhoneNumber());
                tvHome.setText(dto.getHomePage());
                tvSpec.setText(dto.getSpec());
                tvIsCar.setText(dto.getIsCar());
                tvRoomCnt.setText(dto.getCareRoomCnt() + "개 ( " + dto.getCareRoomSize() + "m²)");
                tvPlayCnt.setText(dto.getPlayCnt() + "개");
                tvCctvCnt.setText(dto.getCctvCnt() + "개");
            }else{
                progressDlg.dismiss();
                finish();
            }

            progressDlg.dismiss();
        }

        /* 네트워크 관련 메소드 */
        /* 네트워크 환경 조사 */
        private boolean isOnline() {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }

        /* URLConnection 을 전달받아 연결정보 설정 후 연결, 연결 후 수신한 InputStream 반환 */
        private InputStream getNetworkConnection(HttpURLConnection conn) throws Exception {

            // 클라이언트 아이디 및 시크릿 그리고 요청 URL 선언
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
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

    }
}
