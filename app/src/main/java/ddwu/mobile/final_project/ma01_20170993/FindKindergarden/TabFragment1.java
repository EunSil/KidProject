package ddwu.mobile.final_project.ma01_20170993.FindKindergarden;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ddwu.mobile.final_project.ma01_20170993.R;

public class TabFragment1 extends Fragment implements  Button.OnTouchListener{

    List<String> cityDatas;
    String sindex;
    int p;
    LinearLayout linearLayout;
    TextView tvRoute;
    Button btnRefresh;

    public TabFragment1 (String sindex, int p){
        this.sindex = sindex;
        this.p = p;
    }

    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_tab_fragment1, null);

        linearLayout = (LinearLayout) view.findViewById(R.id.layRoute);
        tvRoute = (TextView) view.findViewById(R.id.tvRoute);
        btnRefresh = (Button) view.findViewById(R.id.btnRefresh);

        btnRefresh.setOnTouchListener(this);

        cityDatas = new ArrayList<String>();
        cityDatas = Arrays.asList(getGuNames());
        String[] index = sindex.split(" > ");

        if(index.length == 1 && !index[0].equals("0")) {
            cityDatas = Arrays.asList(getCityNames(p));
            tvRoute.setText(sindex);
            linearLayout.setVisibility(View.VISIBLE);
        }

        if(index.length != 2) {
            ListAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.area_list_item, R.id.button, cityDatas);

            GridView gridView = (GridView) view.findViewById(R.id.gridview);
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((FindKindergardenActivity)getActivity()).hideKeyboard();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    if(sindex.equals("0")){
                        sindex = getGuNames()[position];
                    }else{
                        sindex += " > " + cityDatas.get(position);
                    }

                    TabFragment1 fragmentHome = new TabFragment1(sindex, position);
                    transaction.replace(R.id.tab1, fragmentHome).commitAllowingStateLoss();
                }
            });
        }else{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            FindFragment findFragment = new FindFragment();
            findFragment.setAddr(sindex);
            findFragment.setEtTarget(null);
            transaction.replace(R.id.tab1, findFragment).commitAllowingStateLoss();
        }

        return view;
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
                    ((FindKindergardenActivity)getActivity()).hideKeyboard();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    TabFragment1 fragmentHome = new TabFragment1("0", -1);
                    transaction.replace(R.id.tab1, fragmentHome).commitAllowingStateLoss();
                }
                break;
        }
        return true;
    }

    public String[] getGuNames(){
        String[] guNames = {"강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"};
        return guNames;
    }

    public String[] getCityNames(int index){
        String[] cityNames = null;
        if(index == 0){
            cityNames = new String[]{"강남대로", "개포로", "광평로", "남부순환로", "논현로", "도곡로", "도산대로", "밤고개로", "봉은사로", "삼성로", "선릉로", "압구정로", "양재대로", "언주로", "역삼로", "영동대로", "일원로", "자곡로", "테헤란로", "학동로", "헌릉로"};
        }else if(index == 1){
            cityNames = new String[]{"강동대로", "고덕로", "구천면로", "동남로", "명일로", "상암로", "상일로", "성내로", "성안로", "아리수로", "암사13길", "암사19길", "암사길", "양재대로", "올림픽로", "진황도로", "천중로", "천호대로", "천호옛6길", "천호옛길", "풍산로", "풍성로"};
        }else if(index == 2){
            cityNames = new String[]{"4.19로", "노해로", "덕릉로", "도봉로", "삼각산로", "삼양로", "솔매로", "솔샘로", "수유로", "숭인로", "오패산로", "오현로", "월계로", "인수봉로", "한천로"};
        }else if(index == 3){
            cityNames = new String[]{"가로", "강서로", "개화동로", "곰달래로", "공항대로", "국회대로", "금낭화로", "까치산로", "남부순환로", "등촌로", "마곡동로", "마곡서1로", "마곡서로", "마곡중앙\n10로", "마곡중앙1로", "마곡중앙3로", "마곡중앙로", "방화대로", "송정로", "수명로", "양천로", "우장산로", "우현로", "월정로", "초록마을로", "초원로", "하늘길", "허준로", "화곡로"};
        }else if(index == 4){
            cityNames = new String[]{"과천대로", "관악로", "관천로", "광신길", "구암5길", "구암길", "국회단지\n10길", "국회단지\n9길", "낙성대로", "낙성대역\n14길", "낙성대역길", "난곡로", "난우10가길", "난향5길", "남부순환로", "남현1가길", "남현2길", "남현길", "당공2가길", "당곡6길", "당곡길", "대학10길", "대학14길", "대학18길", "대학7길", "문성로", "미성3길", "미성7길", "미성길", "법원단지\n12길", "법원단지\n20길", "법원단지2길", "법원단지\n5가길", "법원단지5길", "법원단지길", "보라매로", "복은2길", "복은길", "봉천로", "서림1길", "서원3길", "서원6길", "성현로", "솔밭로", "승방3길", "시흥대로", "신림로", "신사로", "신우길", "신원로", "쑥고개로", "양녕로", "양산길", "양지12길", "양지13길", "은천로", "인헌12길", "인헌17길", "인헌6길", "인헌길", "장군봉2길", "장군봉6길", "장군봉7길", "장군봉길", "조원로", "참숯5길", "청룡10길", "청룡16길", "청룡4길", "청룡8길", "청룡길", "청림3길", "청림5길", "태흥길", "행운10길", "행운1길", "행운1라길", "행운1마길", "호암로"};
        }else if(index == 5) {
            cityNames = new String[]{"광나루로", "광장로", "구의강변로", "구의로", "군자로", "긴고랑로", "능동로", "답십리로", "동일로", "뚝섬로", "면목로", "아차산로", "영화사로", "용마산로", "워커힐로", "자양로", "자양번영로", "천호대로"};
        }else if(index == 6){
            cityNames = new String[]{"가마산로", "개봉로", "경서로", "경인로", "고척로", "공원로", "광남로", "구로", "구일로", "금오로", "남부순환로", "도림로", "디지털로", "벚꽃로", "부일로", "새말로", "서해안로", "시흥대로", "신도림로", "연동로", "오류로", "오리로", "우마1다길", "중앙로", "천왕로", "항동로"};
        }else if(index == 7){
            cityNames = new String[]{"가산디지털\n1로", "가산디지털\n2로", "가산로", "금하로", "남부순환로", "독산로", "두산로", "디지털로", "문성로", "범안로", "벚꽃로", "시흥대로", "탑골로", "한내로"};
        }else if(index == 8){
            cityNames = new String[]{"공릉로", "광운로", "노원로", "노해로", "누원로", "덕릉로", "동일로", "마들로", "상계로", "석계로", "섬밭로", "수락산로", "우이천로", "월계로", "중계로", "초안산로", "한글비석로", "화랑로"};
        }else if(index == 9){
            cityNames = new String[]{"노해로", "덕릉로", "도당로", "도봉로", "도봉산3길", "마들로", "방학로", "삼양로", "시루봉로", "우이천로", "해등로"};
        }else if(index == 10){
            cityNames = new String[]{"경동시장로", "경희대로", "고산자로", "난계로", "답십리로", "망우로", "무학로", "사가정로", "서울시립대로", "신이문로", "안암로", "약령서길", "약령시로", "왕산로", "외대역동로", "이문로", "장안벚꽃로", "장한로", "전농로", "정릉천동로", "제기로", "천장산로", "천호대로", "청계천로", "한빛로", "한천로", "홍릉로", "황물로", "회기로", "휘경로"};
        }else if(index == 11){
            cityNames = new String[]{"강남초등길", "관악로", "국사봉14길", "국사봉1길", "국사봉길", "남부순환로", "노량진로", "대방동15길", "대방동27길", "대방동길", "동작대로", "등용로", "만양로", "매봉로", "보라매로", "사당로", "상도로", "서달로", "성대로", "솔밭로", "시흥대로", "신대방13길", "신대방\n14가길", "신대방1가길", "신대방1길", "신대방2길", "신대방6길", "알마타길", "양녕로", "여의대방로", "장승배기로", "현충로", "흑석로", "흑석한강로"};
        }else if(index == 12){
            cityNames = new String[]{"대흥로", "도화2길", "도화4길", "도화길", "독막로", "동교로", "마포대로", "만리재로", "만리재옛길", "망원로", "매봉산로", "모래내로", "방울내로", "백범로", "삼개로", "상암산로", "새창로", "새터산16길", "새터산2길", "새터산길", "서강로", "성미산로", "성암로", "손기정로", "숭문6길", "숭문길", "신수로", "신촌로", "양화로", "양화진길", "연남로", "와우산로", "월드컵로", "월드컵북로", "잔다리로", "창전로", "토정로", "포은로", "효창목5길", "희우정로"};
        }else if(index == 13){
            cityNames = new String[]{"가재울로", "가재울미래로", "가좌로", "간호대로", "거북골로", "경기대로", "독립문공원길", "독립문로", "명지2길", "명지대3길", "명지대실", "모래내로", "문화촌길", "북아현호", "성산로", "세검정로", "세무서2길", "세무서5길", "세무서길", "수색로", "신촌로", "연대동문길", "연세로", "연희로", "응암로", "이화여대8길", "이화여대길", "증가로", "통일로", "포방터10길", "홍은중앙로", "홍제내2다길", "홍제내길", "홍제천로"};
        }else if(index == 14){
            cityNames = new String[]{"강남대로", "고무래로", "과천대로", "나루터로", "남부순환로", "논현로", "동광로", "동산로", "마방로", "매헌로", "명달로", "바우뫼로", "반포대로", "방배로", "방배선행길", "방배중앙로", "방배천로", "본마을길", "사임당로", "사평대로", "샘마을길", "서리풀4길", "서리풀6길", "서운로", "서초대로", "서초중앙로", "성촌6길", "성촌길", "신반포로", "양재대로", "언남16길", "언남9길", "염곡말길", "잠원로", "전원말길", "주흥13길", "주흥17길", "청계산로", "청두곶2길", "청두곶길", "태봉로", "헌릉로", "형촌길", "홍씨마을길", "효령로"};
        }else if(index == 15){
            cityNames = new String[]{"고산자로", "광나루로", "금호동1가\n금호로", "금호동1가\n독서당로", "금호동1가\n행당로", "금호동2가\n금호로", "금호동2가\n매봉18길", "금호동2가\n무수막길", "금호동3가\n금호산8길", "금호동3가\n금호산9길", "금호동3가\n금호산길", "금호동4가\n금호로", "금호동4가\n독서당로", "금호로", "금호산12길", "금호산2길", "금호산길", "난계로", "독서당로", "동일로", "동호로", "둘레13길", "둘레15길", "둘레3길", "둘레5길", "뚝섬로", "마장로", "매봉18길", "매봉길", "무수막길", "무학로", "무학봉\n15가길", "무학봉16길", "무학봉28길", "무학봉길", "사근동11길", "사근동길", "살곶이길", "상원길", "서울숲2길", "성덕정길", "성수동1가\n상원길", "성수동1가\n성덕정길", "성수동1가\n왕십리로", "성수동2가\n광나루로", "성수동2가\n뚝섬로", "성수동2가\n성수이로", "성수동2가\n성수일로", "성수이로", "성수일로", "송정12길", "송정18가길", "아차산로", "왕십리로", "용답25길", "용답29길", "용답중앙\n11다길", "용답중앙15길", "용답중앙길", "자동차시장길", "청계천로", "한림말길", "행당로"};
        }else if(index == 16){
            cityNames = new String[]{"고려대로", "길", "돌곶이로", "동소문동1가\n성북로", "동소문동7가\n아리랑로", "동소문로", "보국문로", "보문동2가\n보문로", "보문동3가\n보문사길", "보문동6가\n보문로", "보문동6가\n보문사길", "보문로", "보문사길", "북악산로", "삼선교로", "삼선동2가\n보문로", "삼선동3가\n삼선교로", "삼선동4가\n삼선교로", "삼양로", "서경로", "성북동1가\n성북로", "성북로", "솔샘로", "숭인로", "아리랑로", "안암동1가\n인촌로", "안암동3가\n고려대로", "안암동5가\n개운사길", "안암동5가\n안암로", "안암로", "오패산로", "월계로", "월곡로", "인촌로", "장월로", "장위로", "정릉로", "종암로", "지봉로", "창경궁로", "한천로", "화랑로", "흥천사길"};
        }else if(index == 17){
            cityNames = new String[]{"가락로", "강동대로", "거마로", "도곡로", "동남로", "마천로", "문정로", "바람드리13길", "바람드리8길", "바람드리길", "백제고분로", "법원로", "삼전로", "삼학사로", "새말로", "석촌호수로", "성내천로", "송이로", "송파대로", "양산로", "양재대로", "오금로", "올림픽로", "위례광장로", "위례성대로", "위례순환로", "잠실로", "정의로", "중대로", "충민로", "토성로", "풍성로", "한가람로"};
        }else if(index == 18){
            cityNames = new String[]{"가로", "곰달래로", "국회대로", "남부순환로", "목동남로", "목동동로", "목동로", "목동서로", "목동중앙남로", "목동중앙로", "목동중앙본로", "목동중앙북로", "목동중앙서로", "신목로", "신월로", "신정로", "신정이펜1로", "신정이펜2로", "신정중앙로", "안양천로", "오목로", "월정로", "은행정로", "중앙로", "지양로", "화곡로"};
        }else if(index == 19){
            cityNames = new String[]{"63로", "가마산로", "경인로", "국제금융로", "국회대로", "당산동2가\n당산로", "당산동3가\n국회대로", "당산동3가\n당산로", "당산동3가\n선유동1로", "당산동3가\n양산로", "당산동3가\n영신로", "당산동4가\n당산로", "당산동5가\n당산로", "당산동5가\n선유동2로", "당산동6가\n당산로", "당산로", "대림로", "대방천로", "도림로", "도림천로", "도신로", "도영로", "디지털로", "문래동3가\n경인로", "문래동3가\n당산로", "문래동3가\n문래로", "문래동4가\n도림로", "문래동4가\n선유로", "문래동5가\n선유로", "문래동6가\n문래로", "문래동6가\n선유로", "문래로", "버드나루로", "선유동2로", "선유로", "선유서로", "시흥대로", "신길", "신풍로", "양산로", "양평동1가\n문래북로", "양평동1가\n선유로", "양평동1가\n선유서로", "양평동1가\n영등포로", "양평동2가\n선유서로", "양평동2가\n영등포로", "양평동3가\n선유로", "양평동3가\n양산로", "양평동4가\n선유동2로", "양평동4가\n선유로", "양평동4가\n양평로", "양평동5가\n양평로", "양평동6가\n양평로", "양평로", "여의공원로", "여의나루로", "여의대로", "여의대방로", "여의동로", "영등포동7가\n국회대로", "영등포동7가\n영중로", "영등포동8가\n영중로", "영등포로", "영신로", "영중로", "은행로", "의사당대로"};
        }else if(index == 20){
            cityNames = new String[]{"녹사평대로", "대사관로", "독서당로", "두텁바위로", "만리재로", "백범로", "보광로", "새창로", "서빙고로", "소월로", "신흥로", "우사단로", "원효로", "이촌로", "이태원로", "장문로", "청파동2가\n청파로", "청파동3가\n원효로", "청파로", "한강로", "한남대로", "회나무로", "효창원로", "후암로"};
        }else if(index == 21){
            cityNames = new String[]{"가좌로", "갈현로", "녹번로", "백련산로", "북한산로", "불광로", "불광천길", "서오릉로", "수색로", "역말로", "연서로", "은평로", "은평터널로", "응암로", "증산로", "증산서길", "진관1로", "진관2로", "진관3로", "진관4로", "진흥로", "통일로"};
        }else if(index == 22){
            cityNames = new String[]{"경교장길", "김상옥로", "낙산4길", "낙산5길", "낙산길", "낙산성곽동길", "대학로", "돈화문로", "동망산길", "동숭3길", "명륜3가\n성균관로", "북촌로", "사직로", "삼봉로", "삼일대로", "삼청로", "성균관로", "세검정로", "세종대로", "세종로", "송월1길", "송월길", "숭인동길", "신문로", "옥인길", "우정국로", "율곡로", "이화장길", "인사동7길", "자하문로", "종로", "지봉로", "진흥로", "창경궁로", "창덕궁길", "창신4가길", "창신5길", "창신7가길", "창신길", "통일로", "평창길", "평창문화로", "필운대로", "혜화로", "홍지문2길", "효자로"};
        }else if(index == 23){
            cityNames = new String[]{"난계로", "남대문로", "다산로", "덕수궁길", "동호로", "마른내로", "마장로", "만리동2가\n만리재로", "만리재로", "삼일대로", "새문안로", "서소문로", "소파로", "수표로", "왕십리로", "을지로", "장충단로", "중림로", "청계천로", "청구로", "청파로", "태평로", "퇴계로", "회현동1가\n소공로"};
        }else if(index == 24){
            cityNames = new String[]{"겸재로", "공릉로", "답십리로", "동일로", "망우로", "면목로", "봉우재로", "봉화산로", "사가정로", "상봉로", "상봉중앙로", "송림길", "숙선옹주로", "신내로", "신내역로", "양원역로", "용마공원로", "용마산로", "중랑역로", "중랑천로"};
        }
        return cityNames;
    }
}
