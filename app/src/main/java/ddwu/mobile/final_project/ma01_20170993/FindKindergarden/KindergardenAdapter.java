package ddwu.mobile.final_project.ma01_20170993.FindKindergarden;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ddwu.mobile.final_project.ma01_20170993.R;

public class KindergardenAdapter extends BaseAdapter {

    public static final String TAG = "KindergardenAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<KindergardenDto> list;

    public KindergardenAdapter(Context context, int resource, ArrayList<KindergardenDto> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public KindergardenDto getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView with position : " + position);
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = view.findViewById(R.id.tvDisease);
            viewHolder.tvAddress = view.findViewById(R.id.tvAddress);
            viewHolder.tvNumber = view.findViewById(R.id.tvNumber);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        KindergardenDto dto = list.get(position);

        viewHolder.tvName.setText(dto.getName());
        if(dto.getAddress().indexOf("서울") == -1){
            dto.setAddress("서울특별시 " + dto.getDistrict() + " " + dto.getAddress());
        }
        viewHolder.tvAddress.setText(dto.getAddress());
        if(dto.getPresentNumber().equals("\n")) {
            dto.setPresentNumber("-");
            dto.setFixedNumber("-");
        }
        viewHolder.tvNumber.setText("현원 " + dto.getPresentNumber() + " / 정원 " + dto.getFixedNumber());
        return view;
    }

    public void setList(ArrayList<KindergardenDto> list) {
        this.list = list;
    }

//    ※ findViewById() 호출 감소를 위해 필수로 사용할 것
    static class ViewHolder {
        public TextView tvName = null;
        public TextView tvAddress = null;
        public TextView tvNumber = null;
    }
}
