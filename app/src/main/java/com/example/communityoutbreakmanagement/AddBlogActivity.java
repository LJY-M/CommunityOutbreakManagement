package com.example.communityoutbreakmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AddBlogActivity extends AppCompatActivity
        implements View.OnClickListener , RadioGroup.OnCheckedChangeListener {

    private static final String TAG = AddBlogActivity.class.getSimpleName();

    private String[] identityInformation;

    private Button mCancelButton;
    private Button mCommitButton;

    private EditText mTitleEditText;
    private EditText mContentEditText;

    private RadioGroup mLabelRadioGroup;
    private RadioButton mRadioButton;

    private String mLabelString = "";

//    private View uploadImagesView;

//    LinearLayout imagesGroupLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);

        Intent intentThatStartedMultiFunctionActivity = getIntent();

        if (intentThatStartedMultiFunctionActivity != null) {
            if (intentThatStartedMultiFunctionActivity.hasExtra("identityAuthentication")) {
                identityInformation = intentThatStartedMultiFunctionActivity.getStringArrayExtra("identityAuthentication");
                Toast.makeText(AddBlogActivity.this,identityInformation[0] + identityInformation[1],Toast.LENGTH_SHORT).show();
                System.out.println(identityInformation[0] + identityInformation[1]);
            }
        }

        mCancelButton = findViewById(R.id.add_blog_cancel_button);
        mCancelButton.setOnClickListener(this);
        mCommitButton = findViewById(R.id.add_blog_commit_button);
        mCommitButton.setOnClickListener(this);

        mTitleEditText = findViewById(R.id.add_blog_title_edit_text);
        mContentEditText = findViewById(R.id.add_blog_content_edit_text);

        mLabelRadioGroup = findViewById(R.id.add_blog_label_radio_group);
        mLabelRadioGroup.setOnCheckedChangeListener(this);

//        imagesGroupLinearLayout = findViewById(R.id.add_blog_images_linear_layout);

//        LayoutInflater inflater = AddBlogActivity.this.getLayoutInflater();
//        uploadImagesView = inflater.inflate(R.layout.add_images_mode_dialog,null);
//
//        Button mTakeAPictureButton = uploadImagesView.findViewById(R.id.add_images_mode_take_a_picture);
//        mTakeAPictureButton.setOnClickListener(this);
//        Button mUploadFromAlbum = uploadImagesView.findViewById(R.id.add_images_mode_upload_from_album);
//        mUploadFromAlbum.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.add_blog_cancel_button:
                this.finish();
                break;
//            case R.id.add_blog_add_images_floating_action_button:
//                AlertDialog.Builder builder = new AlertDialog.Builder(AddBlogActivity.this);
//                builder.setTitle("图片上传方式").setView(uploadImagesView)
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                System.out.println("点击取消");
//                            }
//                });
//                builder.create().show();
//                break;
//            case R.id.add_images_mode_take_a_picture:
//                ImageView imageView = new ImageView(this);
//                DisplayMetrics displayMetrics = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//                float density = displayMetrics.density;
//
//                imageView.setLayoutParams(new LinearLayout.LayoutParams((int)(100 * density),(int)(100 * density)));
//
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,1);
//
//                imagesGroupLinearLayout.addView(imageView);
//
//                break;
//            case R.id.add_images_mode_upload_from_album:
//                break;
            case R.id.add_blog_commit_button:
                String blogTitle = "";
                String blogContent = "";
                blogTitle = mTitleEditText.getText().toString();
                blogContent = mContentEditText.getText().toString();
                if (blogTitle.isEmpty() || blogContent.isEmpty() || mLabelString.isEmpty()) {
                    Toast.makeText(this, "标题、内容、标签不能为空", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "标题、内容、标签不能为空");
                    break;
                }
                System.out.println(" 标题：" + blogTitle + "\n"
                        + " 内容：" + blogContent + "\n"
                        + " 标签：" + mLabelString);

                SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
                sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                Date date = new Date();// 获取当前时间
                String currentTime = sdf.format(date);

                Uri uri = CommunityBlogsContract.CommunityBlogsEntry.CONTENT_URI;

                ContentValues contentValues = new ContentValues();
                contentValues.put(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_HOUSE_NUMBER, identityInformation[0]);
                contentValues.put(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_RESIDENT_NAME, identityInformation[1]);
                contentValues.put(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_LABEL, mLabelString);
                contentValues.put(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_TITLE, blogTitle);
                contentValues.put(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_CONTENT, blogContent);
                contentValues.put(CommunityBlogsContract.CommunityBlogsEntry.COLUMN_BLOG_TIME, currentTime);

                Uri uriResult = getContentResolver().insert(uri, contentValues);
                if (uriResult != null) {
                    System.out.println(uri.toString());
                }
                this.finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int id = radioGroup.getCheckedRadioButtonId();
        mRadioButton = findViewById(id);
        mLabelString = mRadioButton.getText().toString();
        switch (id) {
            case R.id.add_blog_label_radio_notification:
                Log.i(TAG, "Clicked notification radio");
                break;
            case R.id.add_blog_label_radio_group_purchase:
                Log.i(TAG, "Clicked group purchase radio");
                break;
            case R.id.add_blog_label_radio_daily_news:
                Log.i(TAG, "Clicked daily news radio");
                break;
        }
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        String basePath = Environment.getExternalStorageDirectory().getPath();
//        String filePath = basePath + "/myImage";
//
//        if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//                Bundle bundle = data.getExtras();
//                Bitmap bitmap = (Bitmap) bundle.get("data");
//                FileOutputStream fos = null;
//                File file = new File(filePath);
//                file.mkdir();
//                String fileName = filePath + "/111.jpg";
//
//                try {
//                    fos = new FileOutputStream(fileName);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        fos.flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
////                mImageView.setImageBitmap(bitmap);
//            }
//        }
//    }


}
