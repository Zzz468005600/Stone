package zhulei.com.stone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import zhulei.com.stone.R;
import zhulei.com.stone.event.Envents;
import zhulei.com.stone.manager.UserManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    LinearLayout mUserInfoLayout;
    TextView mLoginBtn;

    @OnClick(R.id.fab)
    public void onFabClicked(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //默认初始化
        Bmob.initialize(this, "e4d8d7779e054f3fd8b228f060ce1943");

        initView();
        EventBus.getDefault().register(this);
    }

    private void initView(){
        ButterKnife.bind(this);
        mLoginBtn = (TextView) mNavView.getHeaderView(0).findViewById(R.id.login_btn);
        mUserInfoLayout = (LinearLayout) mNavView.getHeaderView(0).findViewById(R.id.login_info_layout);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        setSupportActionBar(mToolBar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavView.setNavigationItemSelectedListener(this);
        refreshHeader();
    }

    private void refreshHeader() {
        if (UserManager.instance().hasLogin()){
            mUserInfoLayout.setVisibility(View.VISIBLE);
            ((TextView)mUserInfoLayout.findViewById(R.id.user_name)).setText(UserManager.instance().getUserName());
            ((TextView)mUserInfoLayout.findViewById(R.id.user_info)).setText(UserManager.instance().getPhoneNumber());
            mLoginBtn.setVisibility(View.GONE);
        }else {
            mLoginBtn.setVisibility(View.VISIBLE);
            mUserInfoLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            UserManager.instance().reset();
            refreshHeader();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe
    public void onEvent(Envents.LoginEvent event){
        refreshHeader();
    }
}
