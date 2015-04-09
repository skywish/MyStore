package com.skywish.mystore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.skywish.mystore.model.Good;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by skywish on 2015/4/7.
 */
public class AddgoodActivity extends BaseActivity implements View.OnClickListener {

    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;

    private Button addButton;
    private Button doneButton;
    private ImageView pic;
    private Uri imageUri;
    private EditText edit_title;
    private EditText edit_describe;
    private EditText edit_price;
    private EditText edit_stock;
    private EditText edit_barcode;

    private File outputImage;
    private ProgressBar progressBar;

    String title = "";
    String describe = "";
    String price="";
    int stock=0;
    String barcode="";
    String username="";

    public static void activityStart(Context context, String data) {
        Intent intent = new Intent(context, AddgoodActivity.class);
        intent.putExtra("username", data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgood);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        edit_describe = (EditText) findViewById(R.id.et_adddes);
        edit_title = (EditText) findViewById(R.id.et_addtitle);
        edit_price = (EditText) findViewById(R.id.et_price);
        edit_stock = (EditText) findViewById(R.id.et_stock);
        edit_barcode = (EditText) findViewById(R.id.et_barcode);
        progressBar = (ProgressBar) findViewById(R.id.addgood_progress);

        addButton = (Button) findViewById(R.id.iv_addbutton);
        doneButton = (Button) findViewById(R.id.btn_done);
        doneButton.setOnClickListener(this);
        addButton.setOnClickListener(this);

        pic = (ImageView) findViewById(R.id.iv_pic);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_addbutton:
                outputImage = new File(Environment.getExternalStorageDirectory(),
                        "tempImage.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
                addButton.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_done:
                addGood();
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO); //启动裁减程序
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(imageUri));
                        pic.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void addGood() {
        title = edit_title.getText().toString();
        describe = edit_describe.getText().toString();
        price = edit_price.getText().toString();
        stock = Integer.parseInt(edit_stock.getText().toString());
        barcode = edit_barcode.getText().toString();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "请填写标题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(describe)) {
            Toast.makeText(this, "请填写描述", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "请填写价格", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edit_stock.getText().toString())) {
            Toast.makeText(this, "请填写库存", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pic.getDrawable() == null) {
            Toast.makeText(this, "至少有张照片吧！", Toast.LENGTH_SHORT).show();
            return;
        }

        //上传文件
        final BmobFile file = new BmobFile(outputImage);

        file.upload(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                Good good = new Good();
                if (!TextUtils.isEmpty(barcode)) {
                    good.setBarcode(barcode);
                }
                good.setTitle(title);
                good.setDescribe(describe);
                good.setPrice(price);
                good.setStock(stock);
                good.setBarcode(barcode);
                good.setPic(file);
                good.setLike(0);
                good.setSales(0);
                good.setUsername(username);
                good.save(AddgoodActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AddgoodActivity.this, "上传成功",
                                Toast.LENGTH_SHORT).show();
                        addButton.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(AddgoodActivity.this, "上传失败" + s,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProgress(Integer integer) {
                progressBar.setProgress(integer);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(AddgoodActivity.this, "上传失败" + s,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
