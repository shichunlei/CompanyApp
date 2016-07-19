package com.cells.companyapp.view;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import com.cells.companyapp.utils.*;
import com.cells.companyapp.widget.CircularProgressDialog;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.Video;

public class VideoInfoActivity extends BaseActivity {

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView back;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.ll_layout)
	private LinearLayout layout;

	@ViewInject(id = R.id.img_video)
	private ImageView image;
	@ViewInject(id = R.id.tv_video_name)
	private TextView name;
	@ViewInject(id = R.id.tv_video_issue)
	private TextView issue;
	@ViewInject(id = R.id.tv_video_issue_date)
	private TextView date;

	@ViewInject(id = R.id.img_video_download, click = "download")
	private ImageView download;

	private Video video = new Video();

	private int id;
	private String _name;
	private String download_file;

	private int flog = 0;

	private CircularProgressDialog loading;

	private HttpHandler<File> handler;

	@ViewInject(id = R.id.tv_video_progress)
	private TextView progress;

	@ViewInject(id = R.id.progress_bar)
	private ProgressBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_info);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		id = getIntExtra("id");
		_name = getStringExtra("name");

		String file = AppConfig.DOWNLOAD_BOOK_PATH + "/" + _name + "/" + _name
				+ ".pdf";
		if (FileUtils.fileExists(file)) {
			download.setImageResource(R.drawable.icon_read);
			flog = 1;
		} else {
			download.setImageResource(R.drawable.icon_down);
			flog = 0;
		}

		bar.setIndeterminate(false);

		tvTitle.setText(_name);
		back.setImageResource(R.drawable.icon_back);

		loading = CircularProgressDialog.show(context);
		loading.show();
		getVideoInfo(id);
	}

	private void getVideoInfo(int id) {
		AjaxParams params = new AjaxParams();
		params.put("id", id + "");

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + HttpUtils.VIDEO_DETAIL, params,
				new AjaxCallBack<Object>() {

					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
					}

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						String str = t.toString();

						layout.setVisibility(View.VISIBLE);

						video = (Video) JsonUtil.fromJson(str, Video.class);

						loading.dismiss();

						if (null != video.getName()) {
							name.setText(video.getName());
						}
						if (null != video.getIssue_date()) {
							date.setText(video.getIssue_date());
						}
						if (null != video.getIssue()) {
							issue.setText(video.getIssue());
						}
						FinalBitmap fb = FinalBitmap.create(context);
						fb.display(image, video.getImage());
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						if (t != null) {
							showToast("加载失败，请稍后再试！");
							loading.dismiss();
						}
						super.onFailure(t, errorNo, strMsg);
					}
				});
	}

	public void download(View v) {
		if (flog == 1) {
			read();
		} else if (flog == 0) {
			down();
		}
	}

	private void read() {
		showToast("阅读PDF文件");
	}

	private void down() {
		download.setClickable(false);
		download_file = AppConfig.DOWNLOAD_BOOK_PATH + "/" + video.getName()
				+ ".zip";
		bar.setVisibility(View.VISIBLE);
		progress.setVisibility(View.VISIBLE);

//		String path = video.getDocument();
//		String lll = path.substring(path.lastIndexOf("/") + 1,
//				path.lastIndexOf("?"));

		FinalHttp fh = new FinalHttp();
		handler = fh.download(video.getDocument(), download_file,
				new AjaxCallBack<File>() {
					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);

						Double d_current = (((double) current / 1024) / 1024);
						Double d_count = (((double) count / 1024) / 1024);

						DecimalFormat df = new DecimalFormat("#.00");

						progress.setText(df.format(d_current) + "/"
								+ df.format(d_count) + " (M)");
						// 设置进度条最大值
						bar.setMax((int) (count / 1024));
						// 设置ProgressBar当前值
						bar.setProgress((int) (current / 1024));
					}

					@Override
					public void onSuccess(File t) {
						if (t != null) {
							handler.stop();
							boolean s = false;
							try {
								s = FileUtils.ZipInputStreamTest(
										AppConfig.DOWNLOAD_BOOK_PATH + "/"
												+ video.getName() + "/",
										download_file);

								if (s) {
									download.setImageResource(R.drawable.icon_read);
									flog = 1;
									bar.setVisibility(View.GONE);
									progress.setVisibility(View.GONE);
									download.setClickable(true);
									FileUtils.DeleteFile(download_file);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(Throwable t, int errorCode,
							String strMsg) {
						super.onFailure(t, errorCode, strMsg);
					}
				});
	}

	public void back(View v) {
		finish();
	}
}
