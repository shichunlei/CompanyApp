package com.cells.companyapp.view;

import scl.leo.library.dialog.AlertDialog;
import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.JsonUtil;
import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.Collection;
import com.cells.companyapp.been.News;
import com.cells.companyapp.been.Result;
import com.cells.companyapp.utils.AppConfig;
import com.cells.companyapp.utils.DBUtils;
import com.cells.companyapp.utils.HttpUtils;

public class NewsInfoActivity extends BaseActivity {

	private static final String TAG = "NewsInfoActivity";

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView back;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.img_windows_info)
	private ImageView image;
	@ViewInject(id = R.id.tv_content)
	private TextView content;

	@ViewInject(id = R.id.img_like)
	private ImageView imgLike;
	@ViewInject(id = R.id.tv_like_count)
	private TextView like_count;

	@ViewInject(id = R.id.img_comment)
	private ImageView imgComment;
	@ViewInject(id = R.id.tv_comment_count)
	private TextView comment_count;

	@ViewInject(id = R.id.img_collection)
	private ImageView imgCollect;
	@ViewInject(id = R.id.img_share)
	private ImageView imgShare;

	@ViewInject(id = R.id.ll_share, click = "share")
	private LinearLayout llShare;
	@ViewInject(id = R.id.ll_collection, click = "collect")
	private LinearLayout llCollect;
	@ViewInject(id = R.id.rl_like, click = "like")
	private RelativeLayout rlLike;
	@ViewInject(id = R.id.rl_comment, click = "comment")
	private RelativeLayout rlComment;

	private int id;
	private String _name;
	private int user_id;
	private int status = 0;

	/** 点赞状态 */
	private boolean like = false;
	/** 收藏状态 */
	private boolean collection = false;

	private CircularProgressDialog loading;

	private News news = new News();

	private Collection collection_ = new Collection();

	Result result = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_info);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		id = getIntExtra("id");
		_name = getStringExtra("name");

		tvTitle.setText(_name);
		back.setImageResource(R.drawable.icon_back);

		status = (Integer) SPUtils.get(context, "login_status", 0,
				AppConfig.LOGIN_STATUS_DATA);
		user_id = (Integer) SPUtils.get(context, "id", 0,
				AppConfig.LOGIN_INFO_DATA);

		// 初始化收藏，判断该篇博客是否已被收藏
		DBUtils dbUtil = new DBUtils(context);
		if (dbUtil.queryByIdAndType(id, 1).getCollection_id() == id) {
			imgCollect.setImageResource(R.drawable.icon_del_collection);
			collection = true;
		} else {
			imgCollect.setImageResource(R.drawable.icon_add_collection);
			collection = false;
		}

		loading = CircularProgressDialog.show(context);
		loading.show();
		getNowsInfo(id, user_id);
	}

	/**
	 * 得到企业视窗板块文章内容详情
	 * 
	 * @param id
	 * @param status
	 * @param user_id
	 */
	private void getNowsInfo(int id, int user_id) {
		AjaxParams params = new AjaxParams();
		if (status == 1) {
			params.put("user_id", user_id + "");
		} else if (status == 2) {
			params.put("manager_id", user_id + "");
		} else if (status == 0) {
			if (user_id != 0) {
				params.put("user_id", user_id + "");
			}
		}
		params.put("id", id + "");

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + HttpUtils.GET_NEWS, params,
				new AjaxCallBack<Object>() {

					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
					}

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						String str = t.toString();
						Log.i(TAG, str);
						news = (News) JsonUtil.fromJson(str, News.class);
						loading.dismiss();

						comment_count.setText(news.getComment_count() + "");
						like_count.setText(news.getVote_count() + "");

						if (news.isLike_status()) {
							like = true;
							imgLike.setImageResource(R.drawable.icon_like);
						} else {
							like = false;
							imgLike.setImageResource(R.drawable.icon_unlike);
						}

						FinalBitmap fb = FinalBitmap.create(context);
						fb.display(image, news.getImage());
						if (null != news.getContent()) {
							CharSequence richText = Html.fromHtml(news
									.getContent());
							content.setText(richText);
						}
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

	/**
	 * 返回
	 * 
	 * @param v
	 */
	public void back(View v) {
		finish();
	}

	/**
	 * 
	 * 分享
	 * 
	 * @param v
	 */
	public void share(View v) {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(_name);
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://www.baidu.com");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("内容分享");
		// 设置一个用于截屏分享的View
		View windowView = getWindow().getDecorView();
		oks.setViewToShare(windowView);
		// 获取一张网络图片
		// oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath(/sdcard/test.jpg);// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://money.bmob.cn");
		// oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("分享应用");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://www.baidu.com");
		// 设置自定义的外部回调
		// oks.setCallback(new OneKeyShareCallback());
		oks.setSilent(false);
		// 通过OneKeyShareCallback来修改不同平台分享的内容
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
			public void onShare(Platform platform,
					cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
				if (!platform.getName().equals("ShortMessage")) {
					paramsToShare.setText("内容分享");
					paramsToShare
							.setImageUrl("http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg");
				}
			}
		});
		// 启动分享GUI
		oks.show(this);
	}

	/**
	 * 收藏
	 * 
	 * @param v
	 */
	public void collect(View v) {

	}

	public void like(View v) {
		if (status == 0) {
			user_id = 1;
		}
		if (like) {
			loading.show();
			delLike(id, user_id);
		} else {
			loading.show();
			addLike(id, user_id);
		}
	}

	/**
	 * 点赞
	 * 
	 * @param id
	 * @param user_id
	 */
	private void addLike(final int id, final int user_id) {
		AjaxParams params = new AjaxParams();
		params.put("id", id + "");
		if (status == 1 || status == 0) {
			params.put("user_id", user_id + "");
		} else if (status == 2) {
			params.put("manager_id", user_id + "");
		}

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.post(HttpUtils.ROOT_URL + HttpUtils.WINDOW_LIKE, params,
				new AjaxCallBack<Object>() {

					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
					}

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						String str = t.toString();
						Log.i(TAG, str);

						result = new Result();
						result = (Result) JsonUtil.fromJson(str, Result.class);

						if (result.isStatus()) {
							getNowsInfo(id, user_id);
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						loading.dismiss();
						if (t != null) {
							showToast("加载失败，请稍后再试！");
						}
						super.onFailure(t, errorNo, strMsg);
					}
				});
	}

	/**
	 * 取消赞
	 * 
	 * @param id
	 * @param user_id
	 */
	private void delLike(final int id, final int user_id) {
		AjaxParams params = new AjaxParams();
		params.put("id", id + "");
		if (status == 1 || status == 0) {
			params.put("user_id", user_id + "");
		} else if (status == 2) {
			params.put("manager_id", user_id + "");
		}

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.post(HttpUtils.ROOT_URL + HttpUtils.WINDOW_UNLIKE, params,
				new AjaxCallBack<Object>() {

					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
					}

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						String str = t.toString();
						Log.i(TAG, str);
						result = new Result();
						result = (Result) JsonUtil.fromJson(str, Result.class);
						if (result.isStatus()) {
							getNowsInfo(id, user_id);
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						loading.dismiss();
						if (t != null) {
							showToast("加载失败，请稍后再试！");
						}
						super.onFailure(t, errorNo, strMsg);
					}
				});
	}

	/**
	 * 评论
	 * 
	 * @param v
	 */
	public void comment(View v) {
		if (status == 0) {
			showDialog();
		} else {
			Bundle bundle = new Bundle();
			bundle.putInt("id", id);
			bundle.putString("type", "news");

			openActivity(CommentActivity.class, bundle, false);
		}
	}

	private void showDialog() {
		new AlertDialog(context)
				.builder()
				.setTitle(getString(R.string.remind))
				.setMsg(getString(R.string.dialog_title))
				.setPositiveButton(getString(R.string.personal_login),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								openActivity(PersonalLoginActivity.class, false);
							}
						})
				.setNegativeButton(getString(R.string.company_login),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								openActivity(CompanyLoginActivity.class, false);
							}
						}).show();
	}

	protected void onStop() {
		super.onStop();
		ShareSDK.stopSDK();
	}
}
