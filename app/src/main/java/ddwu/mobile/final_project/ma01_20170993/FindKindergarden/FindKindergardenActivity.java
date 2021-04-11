package ddwu.mobile.final_project.ma01_20170993.FindKindergarden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import ddwu.mobile.final_project.ma01_20170993.R;

public class FindKindergardenActivity extends AppCompatActivity implements Button.OnTouchListener{

    private FragmentManager fragmentManager = getSupportFragmentManager();
    EditText etTarget;
    LinearLayout laySearch;

    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    double latitude;
    double longitude;

    String apiAddress;
    String query;
    KindergardenAdapter adapter;
    ArrayList<KindergardenDto> resultList;
    KindergardenFindXmlParser parser;
    ListView lvList;
    TextView tvNoFind;
    TextView tvIsFind;
    TextView tvGuide;
    InputMethodManager imm;
    Button btnSearch;
    Button ShowLocationButton;
    TextView textview_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_kindergarden);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

        textview_address = (TextView)findViewById(R.id.textview);
        ShowLocationButton = (Button) findViewById(R.id.button);
        ShowLocationButton.setOnTouchListener(this);

        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnTouchListener(this);
        etTarget = (EditText) findViewById(R.id.etTarget);
        laySearch = (LinearLayout) findViewById(R.id.laySearch);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        etTarget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etTarget.getText().toString().equals("")){
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    TabFragment1 fragmentHome = new TabFragment1("0", -1);
                    transaction.replace(R.id.tab1, fragmentHome).commitAllowingStateLoss();
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // 툴바
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec ts1 = tabHost.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.tab1);
        ts1.setIndicator("지역별");
        tabHost.addTab(ts1);
        TabHost.TabSpec ts2 = tabHost.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.tab2);
        ts2.setIndicator("내위치");
        tabHost.addTab(ts2);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        TabFragment1 fragmentHome = new TabFragment1("0", -1);
        transaction.replace(R.id.tab1, fragmentHome).commitAllowingStateLoss();

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                imm.hideSoftInputFromWindow(etTarget.getWindowToken(), 0);
                Log.i("TAB CHANGED", tabId);
                if(tabId.equals("Tab Spec 2")){
                    laySearch.setVisibility(View.GONE);
                }else{
                    laySearch.setVisibility(View.VISIBLE);
                }
            }
        });

        lvList = (ListView) findViewById(R.id.lvList);
        tvNoFind = (TextView) findViewById(R.id.tvNoFind);
        tvIsFind = (TextView) findViewById(R.id.tvIsFind);
        tvGuide = (TextView) findViewById(R.id.tvGuide);

        resultList = new ArrayList();
        adapter = new KindergardenAdapter(this, R.layout.custom_listview_kindergarden, resultList);
        lvList.setAdapter(adapter);
        parser = new KindergardenFindXmlParser();

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isOnline()) {
                    Intent intent = new Intent(FindKindergardenActivity.this, KindergardenViewActivity.class);
                    intent.putExtra("kindergarden", resultList.get(position));
                    startActivity(intent);
                }else{
                    Toast.makeText(FindKindergardenActivity.this, "인터넷을 연결해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) {//위치 값을 가져올 수 있음
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(FindKindergardenActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(FindKindergardenActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission(){
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(FindKindergardenActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(FindKindergardenActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(FindKindergardenActivity.this, REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(FindKindergardenActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(FindKindergardenActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(FindKindergardenActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    public String getCurrentAddress( double latitude, double longitude) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(FindKindergardenActivity.this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }
        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견!\n위치서비스를 설정했는지 확인해주세요.", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FindKindergardenActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void hideKeyboard(){
        imm.hideSoftInputFromWindow(etTarget.getWindowToken(), 0);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Button i = null;
        int tmp = -1;
        switch (v.getId()) {
            case R.id.btnSearch:
                i = btnSearch;
                tmp = 0;
                break;
            case R.id.button:
                i = ShowLocationButton;
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
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    imm.hideSoftInputFromWindow(etTarget.getWindowToken(), 0);
                    if(etTarget.getText().toString().equals("")){
                        Toast.makeText(FindKindergardenActivity.this, "어린이집명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }else {
                        FindFragment findFragment = new FindFragment();
                        findFragment.setEtTarget(etTarget.getText().toString());
                        transaction.replace(R.id.tab1, findFragment).commitAllowingStateLoss();
                    }
                }else if(tmp == 1){
                    tvGuide.setVisibility(View.GONE);
                    gpsTracker = new GpsTracker(FindKindergardenActivity.this);
                    latitude = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();

                    String address = getCurrentAddress(latitude, longitude);
                    textview_address.setText(address);

                    if(address.length() >= 14 && address.substring(0, 11).equals("대한민국 서울특별시 ")){
                        query = " / / /" + address.substring(11, 14);
                    }else if(!address.equals("주소 미발견")){
                        Toast.makeText(FindKindergardenActivity.this, "서울시에 있는 어린이집만 표시 가능합니다.", Toast.LENGTH_LONG).show();
                        query = " / / /종로구";
                    }else{
                        query = " / / /종로구";
                    }
                    apiAddress = getResources().getString(R.string.api_url3);
                    if(isOnline()) {
                        try {
                            new KindergardenLocationAsyncTask().execute(apiAddress + URLEncoder.encode(query, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(FindKindergardenActivity.this);
                        builder.setTitle("인터넷 연결");
                        builder.setMessage("인터넷을 연결 후 확인을 눌러주세요.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Boolean wantToCloseDialog = isOnline();
                                if(wantToCloseDialog) {
                                    dialog.dismiss();
                                    try {
                                        new KindergardenLocationAsyncTask().execute(apiAddress + URLEncoder.encode(query, "UTF-8"));
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
    //                Toast.makeText(FindKindergardenActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
                }
                break;
        }
        return true;
    }

    class KindergardenLocationAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(FindKindergardenActivity.this, "Wait", "Downloading...");
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
            resultList = parser.parse(result);      // 파싱 수행

            double la;
            double lo;

            if(longitude != 0.0 || latitude != 0.0) {
                for (int i = 0; i < resultList.size(); i++) {
                    la = resultList.get(i).getLatitude();
                    lo = resultList.get(i).getLongitude();
                    double diff = getDistance(la, lo, latitude, longitude);
                    if (diff <= 500) {
                        continue;
                    } else {
                        resultList.remove(i);
                        i--;
                    }
                }
            }

            adapter.setList(resultList);    // Adapter 에 파싱 결과를 담고 있는 ArrayList 를 설정
            adapter.notifyDataSetChanged();

            tvNoFind.setText("검색결과 " + resultList.size() + "개");
            if(resultList.size() == 0){
                tvIsFind.setVisibility(View.VISIBLE);
            }
            else{
                tvIsFind.setVisibility(View.GONE);
            }

            progressDlg.dismiss();
        }

        /* URLConnection 을 전달받아 연결정보 설정 후 연결, 연결 후 수신한 InputStream 반환 */
        private InputStream getNetworkConnection(HttpURLConnection conn) throws Exception {

            // 클라이언트 아이디 및 시크릿 그리고 요청 URL 선언
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

    public double getDistance(double lat1 , double lng1 , double lat2 , double lng2 ){
        double distance;

        Location locationA = new Location("point A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lng1);

        Location locationB = new Location("point B");
        locationB.setLatitude(lat2);
        locationB.setLongitude(lng2);

        distance = locationA.distanceTo(locationB);

        return distance;
    }

    /* 네트워크 관련 메소드 */
    /* 네트워크 환경 조사 */
    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
