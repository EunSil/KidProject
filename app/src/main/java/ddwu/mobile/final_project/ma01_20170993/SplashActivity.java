package ddwu.mobile.final_project.ma01_20170993;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import ddwu.mobile.final_project.ma01_20170993.R;
import ddwu.mobile.final_project.ma01_20170993.FindEvent.EventDto;
import ddwu.mobile.final_project.ma01_20170993.FindEvent.EventFindXmlParser;
import ddwu.mobile.final_project.ma01_20170993.FindEvent.EventXmlParser;

public class SplashActivity extends AppCompatActivity  {

    private static final String TAG = "";
    Handler handler = new Handler();

    String apiAddress;
    String query;
    int flag;
    EventXmlParser parser;
    EventFindXmlParser parser2;
    String t;
    ArrayList<EventDto> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flag = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date time = new Date();
        t = format.format(time);
        parser = new EventXmlParser();
        parser2 = new EventFindXmlParser();
        results = new ArrayList<EventDto>();

        if(isOnline()) {
            apiAddress = getResources().getString(R.string.api_url4);
            query = "&eventEndDate=&areaCode=1&sigunguCode=&cat1=&cat2=&cat3=&listYN=N&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=A&eventStartDate=" + t;
            new EventAsyncTask().execute(apiAddress + query);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Boolean wantToCloseDialog = isOnline();
                    if(wantToCloseDialog) {
                        dialog.dismiss();
                        apiAddress = getResources().getString(R.string.api_url4);
                        query = "&eventEndDate=&areaCode=1&sigunguCode=&cat1=&cat2=&cat3=&listYN=N&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=A&eventStartDate=" + t;
                        new EventAsyncTask().execute(apiAddress + query);
                    }
                }
            });
        }
    }

    class EventAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(SplashActivity.this, "Wait", "Downloading...");
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
            if(flag == 0){
                int cnt = parser.parse(result);      // 파싱 수행
                query = "&eventEndDate=&areaCode=1&sigunguCode=&cat1=&cat2=&cat3=&listYN=Y&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=A&eventStartDate=" + t + "&numOfRows=" + cnt;
                new EventAsyncTask().execute(apiAddress + query);
                flag =1;
            }else{
                results = parser2.parse(result);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putParcelableArrayListExtra("eventList", results);
                startActivity(intent);
                finish();
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

    /* 네트워크 관련 메소드 */
    /* 네트워크 환경 조사 */
    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
