package zhulei.com.stone.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import zhulei.com.stone.R;
import zhulei.com.stone.adapter.TabMainAdapter;
import zhulei.com.stone.entity.Message;
import zhulei.com.stone.manager.UserManager;

/**
 * Created by zhulei on 16/6/6.
 */
public class TabMainFragment extends Fragment{

    @BindView(R.id.list_container)
    SwipeRefreshLayout mListContainer;
    @BindView(R.id.list_content)
    RecyclerView mListContent;
    @BindView(R.id.empty_content)
    TextView mEmpty;

    private ArrayList<Message> mListData = new ArrayList<>();
    private TabMainAdapter mTabMainAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_tab, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mListContainer.setColorSchemeColors(R.color.colorPrimaryDark);
        mListContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListData(0, 10);
            }
        });

        mListContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mTabMainAdapter = new TabMainAdapter(getContext(), mListData);
        mListContent.setAdapter(mTabMainAdapter);
        getListData(0 ,10);
    }

    public static TabMainFragment newInstance(){
        TabMainFragment instance = new TabMainFragment();
        return instance;
    }

    private void getListData(int skip, int limit){
        if (!UserManager.instance().hasLogin()){
            Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            if (mListContainer.isRefreshing()){
                mListContainer.setRefreshing(false);
            }
            return;
        }
        BmobQuery<Message> query = new BmobQuery<>();
        query.include("user");
        query.setLimit(100);//TODO
        query.setSkip(skip);
        query.findObjects(getContext(), new FindListener<Message>() {
            @Override
            public void onSuccess(List<Message> list) {
                if (getActivity() != null && isVisible()){
                    if (mListContainer.isRefreshing()){
                        mListContainer.setRefreshing(false);
                    }
                    if (list.isEmpty()){
                        mListContent.setVisibility(View.GONE);
                        mEmpty.setVisibility(View.VISIBLE);
                    }else {
                        mListContent.setVisibility(View.VISIBLE);
                        mEmpty.setVisibility(View.GONE);
                        mListData.clear();
                        mListData.addAll(list);
                        mTabMainAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                if (getActivity() != null && isVisible()){
                    if (mListContainer.isRefreshing()){
                        mListContainer.setRefreshing(false);
                    }
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}