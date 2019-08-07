package cash.app.com.mymvp.view;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cash.app.com.mymvp.BaseMvpActivity;
import cash.app.com.mymvp.LoginBean;
import cash.app.com.mymvp.R;
import cash.app.com.mymvp.UploadImgBean;
import cash.app.com.mymvp.contract.UserContract;
import cash.app.com.mymvp.net.HttpResult;
import cash.app.com.mymvp.net.LogUtils;
import cash.app.com.mymvp.presenter.BasePresenter;
import cash.app.com.mymvp.presenter.UserPresenter;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class UploadFileActivity extends BaseMvpActivity<UserPresenter> implements UserContract.IUserView {
    private Uri mUri;
    private Uri outUri;

    @Override
    protected UserPresenter createPresenter() {
        return new UserPresenter();
    }

    @Override
    public void onFail(String msg) {
        LogUtils.w("上传图片==============01================" + msg);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_upload_file;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        final RxPermissions rxPermissions = new RxPermissions(this);

        findViewById(R.id.tvUploadFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            Toast.makeText(UploadFileActivity.this, "请打开存储权限", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(intent, 1001);
                        }
                    }
                });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            mUri = data.getData();
            startActivityForResult(corpImg(), 1002);
        } else if (requestCode == 1002 && resultCode == RESULT_OK) {
            //裁剪后的 图片
            String path = outUri.getPath();
            mPresenter.uploadFile(new File[]{new File(path)}, new String[]{"headImg"});
        }
    }

    /**
     * 裁剪图片
     */
    private Intent corpImg() {

        //直接裁剪
        Intent intent = new Intent("com.android.camera.action.CROP");
//        if (type == CODE_TAKE_PHOTO) {
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        }
        //设置裁剪之后的图片路径文件
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "panshi");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String url = mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg";
        File cutfile = new File(url);
        if (cutfile.exists()) { //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
            cutfile.delete();
        }
        try {
            cutfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //初始化 uri
        Uri outputUri;
        outputUri = Uri.fromFile(cutfile);

        outUri = outputUri;
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", true);
        // aspectX,aspectY 是宽高的比例，这里设置正方形
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //设置要裁剪的宽高
        intent.putExtra("outputX", 320); //200dp
        intent.putExtra("outputY", 320);
        intent.putExtra("scale", true);
        //如果图片过大，会导致oom，这里设置为false
        intent.putExtra("return-data", false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && type == CODE_TAKE_PHOTO) {
//            File inputFile = new File(fileUrl);
//            intent.setDataAndType(getImageContentUri(inputFile), "image/*");
//        } else {
//            intent.setDataAndType(mUri, "image/*");
//        }
        intent.setDataAndType(mUri, "image/*");

        if (outputUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        }
        intent.putExtra("noFaceDetection", true);
        //压缩图片
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    @Override
    public void login(HttpResult<LoginBean> result) {

    }

    @Override
    public void uploadFile(HttpResult<UploadImgBean> result) {
        LogUtils.w("上传图片==========================" + result.getData().getHeadImg());
    }
}
