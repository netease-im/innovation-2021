package com.ml.doctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.ml.doctor.R;
import com.ml.doctor.adapter.PatientListAdapter;
import com.ml.doctor.bean.PatientListBean;
import com.ml.doctor.network.NetworkApi;
import com.ml.doctor.network.NetworkManager;
import com.ml.doctor.utils.LocalShared;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {


    @BindView(R.id.list)
    RecyclerView list;
    private int limit=500;
    private int start_index=0,end_index=500;
    private static String TAG="MainActivity";
    private PatientListAdapter adapter;
    private List<PatientListBean> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setHat();
        getData(null);
        setClick();
    }

    private void setClick() {

    }

    private void setHat() {
        setTopTitle("陪玩列表");

    }

    @Override
    protected void onLeftViewClick() {
        finish();
    }

    //请求网络数据
    private void getData(String bname) {
//        showLoadingDialog();
//        NetworkApi.patientList(LocalShared.getInstance(this).getUserId(),bname, start_index, end_index,
//                new NetworkManager.SuccessCallback<List<PatientListBean>>() {
//            @Override
//            public void onSuccess(List<PatientListBean> response) {
//                start_index+=limit;
//                end_index+=limit;
//                Log.e(TAG,response.toString());
//                mData=response;
//                setAdapter(response);
//                hideLoadingDialog();
//            }
//        }, new NetworkManager.FailedCallback() {
//            @Override
//            public void onFailed(String message) {
//                Log.e(TAG,message);
//                hideLoadingDialog();
//            }
//        });
//
//        start_index+=limit;
//        end_index+=limit;
//        Log.e(TAG,response.toString());
        String s1 = "{\"exercise_habits\":\"1\",\"sfz\":\"sfz\",\"sex\":\"金牌大神\",\"eqid\":\"eqid\",\"smoke\":\"1\",\"doid\":\"doid\",\"weight\":\"70\",\"drink\":\"1\",\"xfid\":\"xfid\",\"bname\":\"IG.Rookie\",\"dz\":\"dz\",\"blood_type\":\"A\",\"tel\":\"tel\",\"mh\":\"S8全球总决赛冠军\",\"state\":\"state\",\"eating_habits\":\"1\",\"bid\":\"bid\",\"user_photo\":\"https://pics3.baidu.com/feed/b2de9c82d158ccbfe41874815ccf543bb0354186.jpeg?token=b8d087cb867bd3e63f3f1379557fc965&s=C8211D720865E686490CD9DD0300C0A3\",\"age\":\"服务439人\",\"categoryid\":\"categoryid\",\"height\":\"180\"}";
        String s2 = "{\"exercise_habits\":\"1\",\"sfz\":\"sfz\",\"sex\":\"金牌大神\",\"eqid\":\"eqid\",\"smoke\":\"1\",\"doid\":\"doid\",\"weight\":\"70\",\"drink\":\"1\",\"xfid\":\"xfid\",\"bname\":\"IG.Theshy\",\"dz\":\"dz\",\"blood_type\":\"A\",\"tel\":\"tel\",\"mh\":\"S8全球总决赛冠军\",\"state\":\"state\",\"eating_habits\":\"1\",\"bid\":\"bid\",\"user_photo\":\"https://pics1.baidu.com/feed/63d9f2d3572c11df338dc3bcb25d16d6f603c256.jpeg?token=cabd29d0fe7a77d40c67b807193d8136\",\"age\":\"服务336人\",\"categoryid\":\"categoryid\",\"height\":\"180\"}";
        String s3 = "{\"exercise_habits\":\"1\",\"sfz\":\"sfz\",\"sex\":\"金牌大神\",\"eqid\":\"eqid\",\"smoke\":\"1\",\"doid\":\"doid\",\"weight\":\"70\",\"drink\":\"1\",\"xfid\":\"xfid\",\"bname\":\"IG.Jackeylove\",\"dz\":\"dz\",\"blood_type\":\"A\",\"tel\":\"tel\",\"mh\":\"S8全球总决赛冠军\",\"state\":\"state\",\"eating_habits\":\"1\",\"bid\":\"bid\",\"user_photo\":\"https://bkimg.cdn.bcebos.com/pic/d009b3de9c82d158ccbf486eeb430ed8bc3eb035e491?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UyNzI=,g_7,xp_5,yp_5/format,f_auto\",\"age\":\"服务245人\",\"categoryid\":\"categoryid\",\"height\":\"180\"}";
        List<PatientListBean> response = Arrays.asList(new Gson().fromJson(s1, PatientListBean.class),
                                                       new Gson().fromJson(s2, PatientListBean.class),
                                                       new Gson().fromJson(s3, PatientListBean.class));
        mData=response;
        setAdapter(response);
        hideLoadingDialog();

    }

    private void setAdapter(List<PatientListBean> response) {
        list.setLayoutManager(new LinearLayoutManager(this));
//        list.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        list.setAdapter(adapter=new PatientListAdapter(R.layout.patient_item,response));

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //跳转到患者详情页
                startActivity(new Intent(MainActivity.this,MessageActivity.class)
                        .putExtra("data",mData.get(position)));
            }
        });
    }

    @Override
    protected void hideLeftImg(ImageView mLeftImage) {
        mLeftImage.setVisibility(View.GONE);
    }
}
