package ddwu.mobile.final_project.ma01_20170993;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import ddwu.mobile.final_project.ma01_20170993.R;
import ddwu.mobile.final_project.ma01_20170993.FindEvent.EventDetailActivity;
import ddwu.mobile.final_project.ma01_20170993.FindEvent.EventDto;
import ddwu.mobile.final_project.ma01_20170993.FindEvent.FindEventActivity;
import ddwu.mobile.final_project.ma01_20170993.FindKindergarden.FindKindergardenActivity;
import ddwu.mobile.final_project.ma01_20170993.RankBook.RankBookActivity;
import ddwu.mobile.final_project.ma01_20170993.VaccinationCalendar.ScheduleActivity;

public class FragmentHome extends Fragment implements Button.OnTouchListener{

    ImageView img1;
    ImageView img2;
    RelativeLayout layout1;
    RelativeLayout layout2;
    ScrollView lvscroll;

    ArrayList<EventDto> eventList;

    Button btn;
    Button btnCard;
    Button btnBook;
    Button btnSchedule;
    Button btnMore;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        lvscroll = v.findViewById(R.id.lvscroll);

        btn = v.findViewById(R.id.btnFind);
        btn.setOnTouchListener(this);

        btnCard = v.findViewById(R.id.btnCard);
        btnCard.setOnTouchListener(this);

        btnBook = v.findViewById(R.id.btnBook);
        btnBook.setOnTouchListener(this);

        btnSchedule = v.findViewById(R.id.btnSchedule);
        btnSchedule.setOnTouchListener(this);

        Intent i = (getActivity()).getIntent();
        eventList = i.getParcelableArrayListExtra("eventList");

        TextView tvIsFind = v.findViewById(R.id.tvIsFind);
        TextView tvName1 = v.findViewById(R.id.tvName1);
        TextView tvName2 = v.findViewById(R.id.tvName2);
        TextView tvPlace1 = v.findViewById(R.id.tvPlace1);
        TextView tvPlace2 = v.findViewById(R.id.tvPlace2);
        img1 = v.findViewById(R.id.imageView1);
        img2 = v.findViewById(R.id.imageView2);
        layout1 = v.findViewById(R.id.layout1);
        layout2 = v.findViewById(R.id.layout2);
        LinearLayout layout3 = v.findViewById(R.id.layisFind);
        if(eventList.size() == 0){
            tvIsFind.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.VISIBLE);
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
        }
        else {
            tvIsFind.setVisibility(View.GONE);
            layout3.setVisibility(View.GONE);
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            tvName1.setText(eventList.get(0).getTitle());
            tvName2.setText(eventList.get(1).getTitle());
            tvPlace1.setText(eventList.get(0).getAddr1());
            tvPlace2.setText(eventList.get(1).getAddr1());
            layout1.setOnClickListener(clickListener);
            layout2.setOnClickListener(clickListener);
        }

        GetImageAsyncTask imgTask = new GetImageAsyncTask(1);
        GetImageAsyncTask imgTask2 = new GetImageAsyncTask(2);
        try{
            imgTask.execute(eventList.get(0).getFirstimage());
            imgTask2.execute(eventList.get(1).getFirstimage());
        }catch(Exception e){
            e.printStackTrace();
        }

        btnMore = v.findViewById(R.id.btnMore);
        btnMore.setOnTouchListener(this);

        SliderView sliderView = v.findViewById(R.id.imageSlider);

        SliderAdapterExample adapter = new SliderAdapterExample(getActivity());

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.DKGRAY);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        return v;
    }

    View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.layout1:
                    if(isOnline()) {
                        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                        intent.putExtra("event", (Serializable) eventList.get(0));
                        startActivity(intent);
                    }else{
                        Toast.makeText(getActivity(), "인터넷을 연결해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.layout2:
                    if(isOnline()) {
                        Intent intent2 = new Intent(getActivity(), EventDetailActivity.class);
                        intent2.putExtra("event", (Serializable) eventList.get(1));
                        startActivity(intent2);
                    }else{
                        Toast.makeText(getActivity(), "인터넷을 연결해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Button i = null;
        int tmp = -1;
        switch (v.getId()) {
            case R.id.btnFind:
                i = btn;
                tmp = 0;
                break;
            case R.id.btnCard:
                i = btnCard;
                tmp = 1;
                break;
            case R.id.btnBook:
                i = btnBook;
                tmp = 2;
                break;
            case R.id.btnSchedule:
                i = btnSchedule;
                tmp = 3;
                break;
            case R.id.btnMore:
                i = btnMore;
                tmp = 4;
                break;
        }
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lvscroll.requestDisallowInterceptTouchEvent(true);
                i.getBackground().setColorFilter(0x66ffffff, PorterDuff.Mode.SRC_ATOP);
                break;
            case MotionEvent.ACTION_UP:
                i.getBackground().clearColorFilter();
                lvscroll.requestDisallowInterceptTouchEvent(false);
                if(tmp == 0) {
                    Intent intent = new Intent(getActivity(), FindKindergardenActivity.class);
                    startActivity(intent);
                } else if(tmp == 1){
                    Intent intent = new Intent(getActivity(), CardActivity.class);
                    startActivity(intent);
                }else if(tmp == 2){
                    if(isOnline()) {
                        Intent intent = new Intent(getActivity(), RankBookActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getActivity(), "인터넷을 연결해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }else if(tmp == 3){
                    Intent intent = new Intent(getActivity(), ScheduleActivity.class);
                    startActivity(intent);
                }else if(tmp == 4){
                    Intent intent = new Intent(getActivity(), FindEventActivity.class);
                    intent.putParcelableArrayListExtra("eventList", eventList);
                    startActivity(intent);
                }
                break;
        }
        return true;
    }

    /* 행사 이미지를 다운로드 후 이미지뷰에 표시하는 AsyncTask */
    class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        String imageAddress;
        int index;

        public GetImageAsyncTask(int index) {
            this.index = index;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            imageAddress = params[0];
            Bitmap result = downloadImage(imageAddress);
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            /*작성할 부분*/
            /*네트워크에서 다운 받은 이미지 파일을 이미지뷰에 지정*/
            if(bitmap != null){
                switch (index) {
                    case 1:
                        img1.setImageBitmap(bitmap);
                        break;
                    case 2:
                        img2.setImageBitmap(bitmap);
                        break;
                }
            }else{
                switch (index) {
                    case 1:
                        img1.setImageResource(R.drawable.noimage);
                        break;
                    case 2:
                        img2.setImageResource(R.drawable.noimage);
                        break;
                }
            }
        }

        /* 주소를 전달받아 bitmap 다운로드 후 반환 */
        private Bitmap downloadImage(String address) {
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


        /* InputStream을 전달받아 비트맵으로 변환 후 반환 */
        private Bitmap readStreamToBitmap(InputStream stream) {
            return BitmapFactory.decodeStream(stream);
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
