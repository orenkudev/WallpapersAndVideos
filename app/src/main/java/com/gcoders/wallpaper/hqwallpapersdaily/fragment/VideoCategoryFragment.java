package com.gcoders.wallpaper.hqwallpapersdaily.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gcoders.wallpaper.hqwallpapersdaily.BaseFragment;
import com.gcoders.wallpaper.hqwallpapersdaily.R;
import com.gcoders.wallpaper.hqwallpapersdaily.adapter.VideoOptionsAdapter;
import com.gcoders.wallpaper.hqwallpapersdaily.adapter.WallpaperOptionsAdapter;
import com.gcoders.wallpaper.hqwallpapersdaily.service.MyServiceManager;
import com.gcoders.wallpaper.hqwallpapersdaily.view.VideoLoadingActivity;
import com.gcoders.wallpaper.hqwallpapersdaily.view.custom.MyProgressDialog;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoCategoryFragment extends BaseFragment {

    public static String TAG = VideoCategoryFragment.class.getName();

    private String deviceParam;
    private Button click_me;
    private MyProgressDialog progressDialog;
    private EditText input_search_wallpapers;
    private String[] searchCloudStr;
    private List<String> categoryList;
    private RecyclerView recycler_view_images;
    private String category = "VIDEO";


    public VideoCategoryFragment() {
        //Empty Constructor for Fragment
    }

    public static VideoCategoryFragment newInstance(String deviceParam) {
        VideoCategoryFragment detailedFragment = new VideoCategoryFragment();

        Bundle bundle = new Bundle();
        bundle.putString("ARG1", deviceParam);
        detailedFragment.setArguments(bundle);

        return detailedFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && StringUtils.isNotBlank(savedInstanceState.getString("ARG1"))) {
            deviceParam = savedInstanceState.getString("ARG1");
        } else if (null != getArguments()) {
            deviceParam = getArguments().getString("ARG1");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View rootView = inflater.inflate(R.layout.fragment_video_category, container, false);
        bindViews(rootView, savedInstanceState);
        bindEvents();
        return rootView;
    }

    private void bindViews(View rootView, Bundle savedInstanceState) {
        // TextView about_app_text = findViewById(R.id.about_app_text);
        click_me = rootView.findViewById(R.id.click_me);
        recycler_view_images = rootView.findViewById(R.id.recycler_view_buttons);
        input_search_wallpapers = rootView.findViewById(R.id.input_search_wallpapers);
        searchCloudStr = getResources().getStringArray(R.array.wallpaper_categories);

        progressDialog = new MyProgressDialog(getContext());
        progressDialog.setCancelable(false);


        /*about_app_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), InfoActivity.class));
            }
        });*/
    }

    private void bindEvents() {

        VideoOptionsAdapter adapter = new VideoOptionsAdapter(getContext(), getButtonObjects(), new VideoOptionsAdapter.ButtonClick() {
            @Override
            public void onButtonClick(String text) {
                callService(text, category, getOkHttpClientObject(),
                        getMyServiceManagerObject());
            }
        });

        recycler_view_images.setLayoutManager(new StaggeredGridLayoutManager(4, RecyclerView.HORIZONTAL));
        recycler_view_images.setAdapter(adapter);


        input_search_wallpapers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    click_me.setEnabled(s.toString().length() > 0);
                    click_me.setAlpha(1.0f);
                } else {
                    click_me.setEnabled(false);
                    click_me.setAlpha(0.5f);
                }
            }
        });

        input_search_wallpapers.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    if (click_me.isEnabled()) {
                        callService(input_search_wallpapers.getText().toString().trim(), category, getOkHttpClientObject(),
                                getMyServiceManagerObject());

                    }
                    return true;
                }
                return false;
            }
        });

        click_me.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (StringUtils.isNotBlank(input_search_wallpapers.getText().toString())) {
                    callService(input_search_wallpapers.getText().toString().trim(), category, getOkHttpClientObject(),
                            getMyServiceManagerObject());
                }
            }
        });

    }

    private List<String> getButtonObjects() {
        if (categoryList == null) {
            return categoryList = new ArrayList<>(Arrays.asList(searchCloudStr));
        }
        return categoryList;
    }

    private void callService(String searchString, String category, OkHttpClient okHttpClient, MyServiceManager myServiceManager) {

        myServiceManager.callService(searchString, category, okHttpClient, new MyServiceManager.ServiceManagerCallBack() {
            @Override
            public void showLoading(final boolean flag) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (flag) {
                            progressDialog.show();
                        } else {
                            progressDialog.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onSuccess() {
                Intent intent = new Intent(getContext(), VideoLoadingActivity.class);
                startActivity(intent);
            }

            @Override
            public void showErrorMessage(final String errorMessage) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("deviceParam", deviceParam);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        clearSearchField();
    }


    private void clearSearchField() {
        if (null != input_search_wallpapers) {
            input_search_wallpapers.setText("");
        }
    }
}
