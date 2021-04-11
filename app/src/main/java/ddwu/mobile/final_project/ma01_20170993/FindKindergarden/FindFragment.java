package ddwu.mobile.final_project.ma01_20170993.FindKindergarden;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import ddwu.mobile.final_project.ma01_20170993.R;

public class FindFragment extends Fragment implements Button.OnTouchListener{

    ListView lvList;
    TextView tvNoFind;
    TextView tvIsFind;
    LinearLayout linearLayout;
    TextView tvRoute;
    Button btnRefresh;
    String apiAddress;

    String query;

    KindergardenAdapter adapter;
    ArrayList<KindergardenDto> resultList;
    KindergardenFindXmlParser parser;

    public static final String TAG = "FindFragment";
    private String etTarget = "";
    private String addr;

    public void setAddr(String addr){
        this.addr = addr;
    }

    public static FindFragment newInstance(){
        return new FindFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_fragment, null);

        lvList = view.findViewById(R.id.lvList);
        tvNoFind = view.findViewById(R.id.tvNoFind);
        tvIsFind = view.findViewById(R.id.tvIsFind);
        linearLayout = (LinearLayout) view.findViewById(R.id.layRoute);
        tvRoute = (TextView) view.findViewById(R.id.tvRoute);
        btnRefresh = (Button) view.findViewById(R.id.btnRefresh);

        btnRefresh.setOnTouchListener(this);

        resultList = new ArrayList();
        adapter = new KindergardenAdapter(getActivity(), R.layout.custom_listview_kindergarden, resultList);
        lvList.setAdapter(adapter);
        parser = new KindergardenFindXmlParser();

        if(etTarget != null) {
            apiAddress = getResources().getString(R.string.api_url);
            query = etTarget;
        }else{
            tvRoute.setText(addr);
            linearLayout.setVisibility(View.VISIBLE);
            apiAddress = getResources().getString(R.string.api_url3);
            query = addr.split(" > ")[1] + "/ / /" + addr.split(" > ")[0];
        }
        if(isOnline()) {
            try {
                new KindergardenAsyncTask().execute(apiAddress + URLEncoder.encode(query, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                    getActivity().finish();
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
                        try {
                            new KindergardenAsyncTask().execute(apiAddress + URLEncoder.encode(query, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isOnline()) {
                    Intent intent = new Intent(getActivity(), KindergardenViewActivity.class);
                    intent.putExtra("kindergarden", resultList.get(position));
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "인터넷을 연결해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void setEtTarget(String etTarget){
        this.etTarget = etTarget;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Button i = null;
        int tmp = -1;
        switch (v.getId()) {
            case R.id.btnRefresh:
                i = btnRefresh;
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
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    TabFragment1 fragmentHome = new TabFragment1("0", -1);
                    transaction.replace(R.id.tab1, fragmentHome).commitAllowingStateLoss();
                }
                break;
        }
        return true;
    }

    class KindergardenAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(getActivity(), "Wait", "Downloading...");
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

            resultList = parser.parse(result);      // 파싱 수행

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

    /* 네트워크 관련 메소드 */
    /* 네트워크 환경 조사 */
    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
