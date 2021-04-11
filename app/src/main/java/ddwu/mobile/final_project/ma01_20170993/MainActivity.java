package ddwu.mobile.final_project.ma01_20170993;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ddwu.mobile.final_project.ma01_20170993.R;

public class MainActivity extends AppCompatActivity {

    float dp;

    Toolbar toolbar;
    TextView toolbar_text;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentHome fragmentHome = new FragmentHome();
    private FragmentFavorites fragmentFavorites = new FragmentFavorites();
    private FragmentMyroom fragmentMyroom = new FragmentMyroom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 툴바
        toolbar = findViewById(R.id.toolbar);
        toolbar_text = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 해상도
        dp = getResources().getDisplayMetrics().density;

        // Fragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.home:
                    transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();
                    toolbar_text.setBackgroundResource(R.drawable.title);
                    toolbar_text.getLayoutParams().width = (int)(100 * dp);
                    break;
                case R.id.myroom:
                    transaction.replace(R.id.frameLayout, fragmentMyroom).commitAllowingStateLoss();
                    toolbar_text.setBackgroundResource(R.drawable.title2);
                    toolbar_text.getLayoutParams().width = (int)(70 * dp);
                    break;
                case R.id.favorites:
                    transaction.replace(R.id.frameLayout, fragmentFavorites).commitAllowingStateLoss();
                    toolbar_text.setBackgroundResource(R.drawable.title8);
                    toolbar_text.getLayoutParams().width = (int)(90 * dp);
                    break;
            }
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
