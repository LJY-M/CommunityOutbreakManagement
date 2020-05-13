package com.example.communityoutbreakmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddBlogActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] identityInformation;

    private View uploadImagesView;

    LinearLayout imagesGroupLinearLayout;

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

        imagesGroupLinearLayout = findViewById(R.id.add_blog_images_linear_layout);

        LayoutInflater inflater = AddBlogActivity.this.getLayoutInflater();
        uploadImagesView = inflater.inflate(R.layout.add_images_mode_dialog,null);

        Button mTakeAPictureButton = uploadImagesView.findViewById(R.id.add_images_mode_take_a_picture);
        mTakeAPictureButton.setOnClickListener(this);
        Button mUploadFromAlbum = uploadImagesView.findViewById(R.id.add_images_mode_upload_from_album);
        mUploadFromAlbum.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.add_blog_cancel_button:
                this.finish();
                break;
            case R.id.add_blog_add_images_floating_action_button:
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBlogActivity.this);
                builder.setTitle("图片上传方式").setView(uploadImagesView)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("点击取消");
                            }
                });
                builder.create().show();
                break;
            case R.id.add_images_mode_take_a_picture:
                ImageView imageView = new ImageView(this);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                float density = displayMetrics.density;

                imageView.setLayoutParams(new LinearLayout.LayoutParams((int)(100 * density),(int)(100 * density)));

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);

                imagesGroupLinearLayout.addView(imageView);

                break;
            case R.id.add_images_mode_upload_from_album:
                break;
            case R.id.add_blog_commit_button:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String basePath = Environment.getExternalStorageDirectory().getPath();
        String filePath = basePath + "/myImage";

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                FileOutputStream fos = null;
                File file = new File(filePath);
                file.mkdir();
                String fileName = filePath + "/111.jpg";

                try {
                    fos = new FileOutputStream(fileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                mImageView.setImageBitmap(bitmap);
            }
        }
    }


}
