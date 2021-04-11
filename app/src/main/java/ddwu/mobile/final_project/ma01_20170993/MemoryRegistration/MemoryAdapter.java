package ddwu.mobile.final_project.ma01_20170993.MemoryRegistration;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ddwu.mobile.final_project.ma01_20170993.FileManager.ImageFileManager;
import ddwu.mobile.final_project.ma01_20170993.R;

public class MemoryAdapter extends CursorAdapter {

    int layout;
    LayoutInflater inflater;
    Cursor cursor;
    private ImageFileManager imageFileManager = null;

    public MemoryAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, c, flags);
        this.layout = layout;
        this.imageFileManager = new ImageFileManager(context);
        inflater = (LayoutInflater)context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        cursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tvName = view.findViewById(R.id.tvDisease);
        viewHolder.tvDate = view.findViewById(R.id.tvVaccination);
        viewHolder.imageView = view.findViewById(R.id.imageView);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();

        viewHolder.tvName.setText(cursor.getString(cursor.getColumnIndex(MemoryDBHelper.COL_TITLE)));
        viewHolder.tvDate.setText(cursor.getString(cursor.getColumnIndex(MemoryDBHelper.COL_DATE)));

        String image = cursor.getString(cursor.getColumnIndex(MemoryDBHelper.COL_IMAGE));
        if(image != null) {
            Bitmap bitmap = imageFileManager.getSavedBitmapFromInternal(image);
            if(bitmap != null){
                viewHolder.imageView.setImageBitmap(bitmap);
            }else{
                viewHolder.imageView.setImageResource(R.drawable.photo2);
            }
        }else{
            viewHolder.imageView.setImageResource(R.drawable.photo2);
        }
    }

    static class ViewHolder {
        public TextView tvName = null;
        public TextView tvDate = null;
        public ImageView imageView = null;
    }

}
