package com.jinhong.jhtv.ui.fragment;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.blankj.utilcode.util.LogUtils;
import com.jinhong.jhtv.R;
import com.jinhong.jhtv.base.BaseFragment;
import com.jinhong.jhtv.model.MainListBean;
import com.jinhong.jhtv.ui.activity.CategoryActivity;
import com.jinhong.jhtv.utils.FocusUtils;
import com.jinhong.jhtv.utils.ImageUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author :  Jim
 * @date :  2019-07-25
 * @description :亲子教育页面
 */
public class EducationFragment extends BaseFragment {
    private String dataType;//activity传递过来的分类数据
    private RecyclerView recyclerView;
    private MutableLiveData<MainListBean> mMainListBean;


    public EducationFragment getInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putString("DATA", s);
        setArguments(bundle);
        return this;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initData() {
        //获取到activity传递过来的数据
        Bundle bundle = getArguments();
        if (bundle != null) {
            String data = bundle.getString("DATA");
        }


        mMainListBean = mCommonViewModel.getMainListBean("10014");


    }

    @Override
    protected void initView(View inflate) {
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);

        VirtualLayoutManager layoutManager = new VirtualLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

//        layoutManager.setReverseLayout(true);//等比划分

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(13, 14, 13, 14);
            }
        });

        mMainListBean.observe(this, new Observer<MainListBean>() {
            @Override
            public void onChanged(@Nullable MainListBean mainListBean) {
                setToyAdapter(layoutManager, mainListBean);
            }
        });


    }

    private void setToyAdapter(VirtualLayoutManager layoutManager, MainListBean mainListBean) {
        List<MainListBean.DataBean.PosterVosBean> posterVos = mainListBean.getData().getPosterVos();
        int size = posterVos.size();
        final List<LayoutHelper> helpers = new LinkedList<>();

//网格布局
        final GridLayoutHelper gridLayoutHelper0 = new GridLayoutHelper(2);

        gridLayoutHelper0.setItemCount(size <= 2 ? 2 : size);

        final GridLayoutHelper gridLayoutHelper1 = new GridLayoutHelper(4);

        gridLayoutHelper1.setItemCount(size >= 10 ? 8 : size - 2);

        helpers.add(gridLayoutHelper0);

        helpers.add(gridLayoutHelper1);

        layoutManager.setLayoutHelpers(helpers);


        recyclerView.setAdapter(
                new VirtualLayoutAdapter(layoutManager) {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ImageView imageView = new ImageView(getContext());
                        //加载参数
                        return new EducationViewHolder(imageView);
                    }

                    @Override
                    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                        ImageView imageView = (ImageView) holder.itemView;
                        try {
                            switch (position) {
                                default:
                                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(416, 318);
                                    holder.itemView.setLayoutParams(layoutParams);
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    ImageUtils.load(posterVos.get(position).getHomePosterPath(), (ImageView) holder.itemView);
                                    break;

                                case 0:
                                case 1:
                                    layoutParams = new ViewGroup.LayoutParams(858, 318);
                                    holder.itemView.setLayoutParams(layoutParams);
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    ImageUtils.load(posterVos.get(position).getHomePosterPath(), (ImageView) holder.itemView);
                                    break;


                                case 9:
                                    layoutParams = new ViewGroup.LayoutParams(416, 318);
                                    holder.itemView.setLayoutParams(layoutParams);
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    ImageUtils.load(R.drawable.iv_qinzi_more, (ImageView) holder.itemView);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("type", 1);
                                            startActivity(CategoryActivity.class, bundle);
                                        }
                                    });

                                    break;


                            }
                        } catch (Exception e) {
                            LogUtils.e(e);
                        }

                    }

                    @Override
                    public int getItemCount() {
                        List<LayoutHelper> helpers = getLayoutHelpers();
                        if (helpers == null) {
                            return 0;
                        }
                        int count = 0;
                        for (int i = 0, size = helpers.size(); i < size; i++) {
                            count += helpers.get(i).getItemCount();
                        }
                        return count;
                    }

                }

        );

    }


    class EducationViewHolder extends RecyclerView.ViewHolder {

        public EducationViewHolder(ImageView itemView) {
            super(itemView);
            //传入item对应的view
            itemView.setFocusable(true);

            FocusUtils.onFocusChange(itemView, R.drawable.shape_selector_border_corner_press, R.drawable.shape_selector_border_normal);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "v:" + v, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}


