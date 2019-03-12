/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.kys_8.easyforest.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kys_8.easyforest.R;

public class GuideFragment extends Fragment {

    private AppCompatTextView sectionLabel;
    private AppCompatTextView sectionIntro;
    private ImageView sectionImg;

    private int page;

    // The fragment argument representing
    // the section number for this fragment.
    private static final String ARG_SECTION_NUMBER = "section_number";

    public GuideFragment() {}

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GuideFragment newInstance(int sectionNumber) {
        GuideFragment fragment = new GuideFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARG_SECTION_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);

        initViews(view);

        switch (page) {
            case 0:
                sectionImg.setBackgroundResource(R.mipmap.tree_guide);
//                sectionImg.setBackgroundResource(R.drawable.ic_beenhere_black_24dp);
                sectionLabel.setText("简单方便的林木查询");
                sectionIntro.setText("支持全国重点保护林木的搜索");
                break;
            case 1:
                sectionImg.setBackgroundResource(R.mipmap.map_guide);
//                sectionImg.setBackgroundResource(R.drawable.ic_camera_black_24dp);
                sectionLabel.setText("林木的地图分布");
                sectionIntro.setText("方便你快速的定位与详细资料的查看");
                break;
            case 2:
                sectionImg.setBackgroundResource(R.mipmap.cloud_guide);
//                sectionImg.setBackgroundResource(R.drawable.ic_notifications_black_24dp);
                sectionLabel.setText("随手拍照，轻松上传，智能识别");
                sectionIntro.setText("可以将图片上传到云数据库，用户可以查看分享，可以进行智能识别");
                break;
            default:
                break;
        }

        return view;
    }

    private void initViews(View view) {
        sectionLabel = view.findViewById(R.id.section_label);
        sectionIntro = view.findViewById(R.id.section_intro);
        sectionImg = view.findViewById(R.id.section_img);
    }

}
