package com.mumu.parallaxheader;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mumu.parallaxheader.util.DensityUtil;
import com.mumu.parallaxheader.util.StatusBarUtils;
import com.mumu.parallaxheader.view.PullToZoomListView;
import com.mumu.parallaxheader.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements PullToZoomListView.PullToZoomListViewListener, AbsListView.OnScrollListener {

    private Context mContext;
    private PullToZoomListView ptzlv_container;
    private RelativeLayout mRlTop;
    private TextView tv_nick;
    private ArrayAdapter<String> mAdapter;
    private RoundImageView iv_avatar;
    private ImageView iv_certified;
    private ImageButton ib_back;
    private ImageButton ib_notification;
    private ImageView mTopCerfitied;
    private ViewGroup.MarginLayoutParams mRlAvatarViewLayoutParams;
    private RoundImageView riv_avatar;
    private RelativeLayout mRlAvatarView;
    private ProgressBar pb_loading;
    private View mNavHeaderView;
    private View mHeaderView;

    private List<String> adapterData = new ArrayList<>();
    private boolean loadMore = false;
    private int mTopAlpha;
    private boolean mTopBgIsDefault = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        initData();

        StatusBarUtils.from(this)
                .setTransparentStatusbar(true)//状态栏是否透明
                .setTransparentNavigationbar(false)//Navigationbar是否透明
                .setActionbarView(mRlTop)//view是否透明
                .setLightStatusBar(false)//状态栏字体是否为亮色
                .process();
    }

    private void initData() {
        if (loadMore) {
            adapterData.add("Java");
            adapterData.add("Android");
            adapterData.add("IOS");
            adapterData.add("Php");
            adapterData.add("C");
            adapterData.add("C++");
            adapterData.add("C#");
            adapterData.add(".NET");
            adapterData.add("Ruby");
            adapterData.add("Python");
            adapterData.add("Go");
        } else {
            adapterData.add("Activity");
            adapterData.add("Service");
            adapterData.add("Content Provider");
            adapterData.add("Intent");
            adapterData.add("BroadcastReceiver");
            adapterData.add("ADT");
            adapterData.add("Sqlite3");
            adapterData.add("HttpClient");
            adapterData.add("DDMS");
            adapterData.add("Android Studio");
            adapterData.add("Fragment");
            adapterData.add("Loader");
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mHeaderView = View.inflate(mContext, R.layout.header_userinfo, null);
        mNavHeaderView = View.inflate(mContext, R.layout.header_nav_layout, null);
        riv_avatar = (RoundImageView) mHeaderView.findViewById(R.id.riv_avatar);
        ptzlv_container = (PullToZoomListView) findViewById(R.id.ptzlv_container);
        mRlTop = (RelativeLayout) findViewById(R.id.rl_top);
        tv_nick = (TextView) findViewById(R.id.tv_nick);
        iv_avatar = (RoundImageView) findViewById(R.id.iv_avatar);
        iv_certified = (ImageView) findViewById(R.id.iv_certified);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_notification = (ImageButton) findViewById(R.id.ib_notification);
        mRlAvatarView = (RelativeLayout) findViewById(R.id.rl_userinfo_top_avatar);
        mTopCerfitied = (ImageView) findViewById(R.id.iv_certified);
        mRlTop = (RelativeLayout) findViewById(R.id.rl_top);
        pb_loading = (ProgressBar) findViewById(R.id.progressBar);
        mRlAvatarViewLayoutParams = (ViewGroup.MarginLayoutParams) mRlAvatarView.getLayoutParams();


        //设置背景图
        ptzlv_container.getHeaderView().setImageResource(R.drawable.icon_scroller_header);
        ptzlv_container.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);
        //设置header大小
        ptzlv_container.setHeaderViewSize(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(mContext, 280));
        //设置headerView
        ptzlv_container.getHeaderContainer().addView(mHeaderView);
        ptzlv_container.setHeaderView();

        //添加headerView
        ptzlv_container.addHeaderView(mNavHeaderView);
        mAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, adapterData);
        //设置Adapter
        ptzlv_container.setAdapter(mAdapter);

        ptzlv_container.setOnScrollListener(this);
        //添加刷新监听和加载更多监听
        ptzlv_container.setPullToZoomListViewListener(this);

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    updateImageBgUI();
                    break;
                case 2:
                    updateImageBgUI();
                    mAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    pb_loading.setVisibility(View.INVISIBLE);
                    break;
                case 4:
                    loadMore = true;
                    initData();
                    ptzlv_container.setLoadFinish(true);
                    break;
            }
        }
    };


    /**
     * 更新图片背景
     */
    private void updateImageBgUI() {
        int[] location = new int[2];
        riv_avatar.getLocationOnScreen(location);
        if (location[1] < 255) {
            mTopBgIsDefault = false;
            //mRlAvatarView  visible
//            if (location[1] <= 46 && location[1] >= 0) {
//                mRlAvatarView.setVisibility(View.VISIBLE);
//                mRlAvatarViewLayoutParams.topMargin = DensityUtil.dp2px(mContext, location[1]);
//                mRlAvatarView.setLayoutParams(mRlAvatarViewLayoutParams);
//            } else {
//                // mRlAvatarView  marginTop > 46  invisible
//                if (mRlAvatarView.getVisibility() != View.VISIBLE) {
//                    mRlAvatarView.setVisibility(View.VISIBLE);
//                }
//                // out of screen
//                if (location[1] < 0 && ((ViewGroup.MarginLayoutParams) mRlAvatarView.getLayoutParams()).topMargin != 0) {
//                    mRlAvatarViewLayoutParams.topMargin = 0;
//                    mRlAvatarView.setLayoutParams(mRlAvatarViewLayoutParams);
//                }
//                // mRlAvatarView  marginTop > 46  invisible
//                if (location[1] > 46 && mRlAvatarView.getVisibility() != View.INVISIBLE) {
//                    mRlAvatarView.setVisibility(View.INVISIBLE);
//                }
//            }

            if (ptzlv_container.getFirstVisiblePosition() >= 1 || location[1] < 0) {
                if (mTopAlpha != 255) {
                    mRlTop.setBackgroundColor(Color.argb(255, 66, 66, 66));
                    mTopAlpha = 255;
                    ib_back.setImageResource(R.drawable.icon_back_bold_white);
                }
            } else {
                mTopAlpha = 255 - location[1];
                mRlTop.setBackgroundColor(Color.argb(mTopAlpha, 66, 66, 66));
                ib_back.setImageResource(R.drawable.icon_back_white);
            }
        } else {
            setDefaultImageBg();
        }
    }

    /**
     * 设置默认背景
     */
    private void setDefaultImageBg() {
        if (!mTopBgIsDefault) {
            mTopBgIsDefault = true;
            mRlTop.setBackgroundResource(R.drawable.bg_nav_panel);
            tv_nick.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    @Override
    public void onReload() {
        mHandler.removeMessages(3);
        pb_loading.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessageDelayed(3, 1000);
        Toast.makeText(mContext, "更新中...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMore() {
        mHandler.removeMessages(4);
        mHandler.sendEmptyMessageDelayed(4, 1000);
        Toast.makeText(mContext, "加载中,请稍后...", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            mHandler.removeMessages(1);
            mHandler.sendEmptyMessage(1);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mHandler.removeMessages(1);
        mHandler.sendEmptyMessage(1);
    }
}
