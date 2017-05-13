package cn.ilell.manage.versionupdate;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import cn.ilell.manage.R;
import cn.ilell.manage.utils.MyConfig;

import static cn.ilell.manage.utils.MyConfig.PACKAGE_NAME;

public class UpdateManager {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 可以更新 */
    private static final int CANUPDATE = 3;
    /* 不可以更新 */
    private static final int CAN_NOT_UPDATE = 4;
    /* 设置显示文件长度 */
    private static final int SET_FILELENGTRH = 5;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    private Context mContext;

    private TextView download1;
    private TextView download2;
    private TextView download3;
    private int fileLength;//文件长度
    private int count;//当前下载量

    DecimalFormat df = new DecimalFormat("#.0");

    /* 更新进度条 */
    private ProgressBar mProgress;
    //对话框
    private Dialog mDownloadDialog;
    //版本信息
    private JSONObject verinfo;
    //URL
    private String downloadUrl = null;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    download1.setText(df.format(count * 1.0 / 1024) + "kb");
                    download2.setText(df.format(count * 100.0 / fileLength) + "%");
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                case CANUPDATE:
                    //提示更新
                    showNoticeDialog();
                    break;
                case CAN_NOT_UPDATE:
                    //出现错误，不能更新
                    Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_LONG).show();
                    break;
                case SET_FILELENGTRH:
                    download3.setText(df.format(fileLength * 1.0 / 1024) + "kb");
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder json = new StringBuilder();
                    //获取网络最新版本
                    URL url = new URL(MyConfig.VERSIONUPDATE_URL);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    // 创建输入流
                    InputStream is = conn.getInputStream();
                    // 缓存
                    byte buf[] = new byte[256];
                    // 写入到文件中
                    int numread = 0;
                    while ((numread = is.read(buf)) > 0)
                        json.append(new String(buf, 0, numread));
                    is.close();
                    verinfo = new JSONObject(json.toString());
                    int serviceCode = verinfo.getInt("version");
                    // 获取当前软件版本
                    int versionCode = getVersionCode(mContext);
                    Log.i("abc", serviceCode + " " + versionCode);
                    if (serviceCode > versionCode) {
                        downloadUrl = verinfo.getString("url");
                        mHandler.sendEmptyMessage(CANUPDATE);
                        return;
                    }
                } catch (Exception e) {
                    Log.i("abc", e.getMessage());
                }
                mHandler.sendEmptyMessage(CAN_NOT_UPDATE);
            }
        }).start();
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
        }
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setMessage("检测到新版本，立即更新吗？");
        builder.setTitle("软件更新");
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("正在下载");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        download1 = (TextView) v.findViewById(R.id.download1);
        download2 = (TextView) v.findViewById(R.id.download2);
        download3 = (TextView) v.findViewById(R.id.download3);
        mProgress.setMax(100);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     *
     * @author coolszy
     * @date 2012-4-26
     * @blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    Log.i("abc", downloadUrl);
                    URL url = new URL(downloadUrl);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    fileLength = conn.getContentLength();
                    //设置显示文件长度
                    mHandler.sendEmptyMessage(SET_FILELENGTRH);

                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, verinfo.getString("name"));
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / fileLength) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }

    ;

    /**
     * 安装APK文件
     */
    private void installApk() {
        try {
            File apkfile = new File(mSavePath, verinfo.getString("name"));
            if (!apkfile.exists()) {
                return;
            }
            // 通过Intent安装APK文件
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
            mContext.startActivity(i);
        } catch (Exception e) {
        }
    }
}
