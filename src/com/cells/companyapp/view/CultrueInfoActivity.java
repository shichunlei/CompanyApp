package com.cells.companyapp.view;

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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.Collection;
import com.cells.companyapp.been.Culture;
import com.cells.companyapp.been.Result;
import com.cells.companyapp.utils.AppConfig;
import com.cells.companyapp.utils.DBUtils;
import com.cells.companyapp.utils.HttpUtils;

public class CultrueInfoActivity extends BaseActivity {

	private static final String TAG = "CultrueInfoActivity";

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView back;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.img_culture_logo)
	private ImageView logo;
	@ViewInject(id = R.id.tv_culture_name)
	private TextView name;
	@ViewInject(id = R.id.img_culture_image)
	private ImageView image;
	@ViewInject(id = R.id.tv_culture_desc)
	private TextView content;

	@ViewInject(id = R.id.ll_like, click = "like")
	private LinearLayout ll_like;
	@ViewInject(id = R.id.ll_comment, click = "comment")
	private LinearLayout ll_comment;
	@ViewInject(id = R.id.ll_collection, click = "collection")
	private LinearLayout ll_collection;

	@ViewInject(id = R.id.img_like)
	private ImageView img_like;
	@ViewInject(id = R.id.img_comment)
	private ImageView img_comment;
	@ViewInject(id = R.id.img_collection)
	private ImageView img_collection;

	@ViewInject(id = R.id.tv_culture_comment_count)
	private TextView tv_comment_count;
	@ViewInject(id = R.id.tv_culture_like_count)
	private TextView tv_like_count;

	/** 点赞状态 */
	private boolean like = false;
	/** 收藏状态 */
	private boolean collection = false;

	/** 企业文化中心ID */
	private int id;
	/** 企业文化中心类型 */
	private int type;
	/** 企业文化中心类型详情接口 */
	private String culture_info_type = "";

	private CircularProgressDialog loading;

	private Culture culture = new Culture();

	private Collection collection_ = new Collection();

	Result result = null;

	/** 登录状态 */
	private int status = 0;
	/** 登录用户ID */
	private int user_id;

	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_culture_info);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		Bundle bundle = getIntent().getExtras();
		id = bundle.getInt("id");
		type = bundle.getInt("type");

		if (type == 1) {
			culture_info_type = HttpUtils.GET_HOPE;
			title = "愿景";
		} else if (type == 2) {
			culture_info_type = HttpUtils.GET_MISSION;
			title = "使命";
		} else if (type == 3) {
			culture_info_type = HttpUtils.GET_SPIRIT;
			title = "精神";
		} else if (type == 4) {
			culture_info_type = HttpUtils.GET_VALUE;
			title = "价值观";
		} else if (type == 5) {
			culture_info_type = HttpUtils.GET_MANAGEMENT;
			title = "经营方针";
		} else if (type == 6) {
			culture_info_type = HttpUtils.BRAND_EXPLAIN;
			title = "标志释义";
		}

		status = (Integer) SPUtils.get(context, "login_status", 0,
				AppConfig.LOGIN_STATUS_DATA);
		user_id = (Integer) SPUtils.get(context, "id", 0,
				AppConfig.LOGIN_INFO_DATA);

		tvTitle.setText(title);
		back.setImageResource(R.drawable.icon_back);

		// 初始化收藏，判断该篇博客是否已被收藏
		DBUtils dbUtil = new DBUtils(context);
		if (dbUtil.queryByIdAndType(id, 1).getCollection_id() == id) {
			img_collection.setImageResource(R.drawable.icon_del_collection);
			collection = true;
		} else {
			img_collection.setImageResource(R.drawable.icon_add_collection);
			collection = false;
		}

		loading = CircularProgressDialog.show(context);
		loading.show();
		getCultureInfo(id, type, user_id);
	}

	private void getCultureInfo(int id, int type, int user_id) {
		AjaxParams params = new AjaxParams();
		if (status == 1) {
			params.put("user_id", user_id + "");
		} else if (status == 2) {
			params.put("manager_id", user_id + "");
		}
		params.put("id", id + "");

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + culture_info_type, params,
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
						culture = (Culture) JsonUtil.fromJson(str,
								Culture.class);
						loading.dismiss();

						tv_comment_count.setText(culture.getComment_count()
								+ "");
						tv_like_count.setText(culture.getVote_count() + "");

						if (culture.isLike_status()) {
							like = true;
							img_like.setImageResource(R.drawable.icon_like);
						} else {
							like = false;
							img_like.setImageResource(R.drawable.icon_unlike);
						}

						FinalBitmap fb = FinalBitmap.create(context);
						fb.display(logo, culture.getLogo());
						if (null != culture.getName()) {
							name.setText(culture.getName());
						}
						fb.display(image, culture.getImage());
						if (null != culture.getContent()) {
							content.setText(culture.getContent());
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

	public void back(View v) {
		finish();
	}

	public void like(View v) {
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
		if (status == 1) {
			params.put("user_id", user_id + "");
		} else if (status == 2) {
			params.put("manager_id", user_id + "");
		} else {
			params.put("user_id", 1 + "");
		}

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.post(HttpUtils.ROOT_URL + HttpUtils.CULTURE_LIKE, params,
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
							like = true;
							img_like.setImageResource(R.drawable.icon_like);
							if (status == 1 || status == 2) {
								getCultureInfo(id, type, user_id);
							} else {
								loading.dismiss();
							}
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
		if (status == 1) {
			params.put("user_id", user_id + "");
		} else if (status == 2) {
			params.put("manager_id", user_id + "");
		} else {
			params.put("user_id", 1 + "");
		}

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.post(HttpUtils.ROOT_URL + HttpUtils.CULTURE_UNLIKE, params,
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
							like = false;
							img_like.setImageResource(R.drawable.icon_unlike);
							if (status == 1 || status == 2) {
								getCultureInfo(id, type, user_id);
							} else {
								loading.dismiss();
							}
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

	public void comment(View v) {
		if (status == 0) {
			showToast("请登录后进行评论！");
		} else {
			Bundle bundle = new Bundle();
			bundle.putInt("id", id);
			bundle.putString("type", "cultures");

			openActivity(CommentActivity.class, bundle, false);
		}
	}

	public void collection(View v) {
		collection_.setCollection_id(culture.getId());
		collection_.setComment_count(culture.getComment_count());
		collection_.setContent(culture.getContent());
		collection_.setCreated_at("2015-05-22");
		collection_.setImage(culture.getLogo());
		collection_.setName(culture.getName());
		collection_.setType(1);
		collection_.setType2(type);
		collection_.setLike_count(culture.getVote_count());

		DBUtils dbUtil = new DBUtils(context);
		if (collection) {
			if (dbUtil.deleteByIdAndType(id, 1)) {
				img_collection.setImageResource(R.drawable.icon_add_collection);
				collection = false;
			} else {
				showToast("取消收藏失败！");
			}
		} else {
			if (dbUtil.insert(collection_)) {
				img_collection.setImageResource(R.drawable.icon_del_collection);
				collection = true;
			} else {
				showToast("收藏失败！");
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}
