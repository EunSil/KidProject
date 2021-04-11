package ddwu.mobile.final_project.ma01_20170993.ViewFavorites;

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

public class FavoritesAdapter extends CursorAdapter {

    private Context context;
    int layout;
    LayoutInflater inflater;
    Cursor cursor;
    private ImageFileManager imageFileManager = null;

    public FavoritesAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
        this.layout = layout;
        this.imageFileManager = new ImageFileManager(context);
        inflater = (LayoutInflater)context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        cursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tvTitle = view.findViewById(R.id.tvDisease);
        viewHolder.tvAddr = view.findViewById(R.id.tvVaccination);
        viewHolder.imageView = view.findViewById(R.id.imageView);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();

        viewHolder.tvTitle.setText(cursor.getString(cursor.getColumnIndex(FavoritesDBHelper.COL_TITLE)));
        viewHolder.tvAddr.setText(cursor.getString(cursor.getColumnIndex(FavoritesDBHelper.COL_ADDR)));
        String image = cursor.getString(cursor.getColumnIndex(FavoritesDBHelper.COL_IMAGE));

        if(image != null) {
            Bitmap bitmap = imageFileManager.getSavedBitmapFromInternal(image);
            if(bitmap != null){
                viewHolder.imageView.setImageBitmap(bitmap);
            }else{
                viewHolder.imageView.setImageResource(R.drawable.noimage);
            }
        }else{
            viewHolder.imageView.setImageResource(R.drawable.noimage);
        }
    }

    static class ViewHolder {
        public TextView tvTitle = null;
        public TextView tvAddr = null;
        public ImageView imageView = null;
    }

}
