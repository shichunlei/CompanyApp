package com.cells.companyapp.view;

import java.util.List;

import com.cells.companyapp.utils.*;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.base.BaseAdapterHelper;
import com.cells.companyapp.base.CommonAdapter;
import com.cells.companyapp.been.Collection;
import com.cells.companyapp.been.Comment;
import com.cells.companyapp.been.Culture;
import com.cells.companyapp.been.Result;
import com.cells.companyapp.utils.AppConfig;
import com.cells.companyapp.utils.DBUtils;
import com.cells.companyapp.utils.ApiUtils;
import com.cells.companyapp.widget.AlertDialog;
import com.cells.companyapp.widget.MyListView;
import com.google.gson.reflect.TypeToken;

public class CultrueInfoActivity extends BaseActivity {

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

	@ViewInject(id = R.id.listview)
	private MyListView listview;

	private List<Comment> comment;

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

	private Culture culture = new Culture();

	private Collection collection_ = new Collection();

	private Result result = null;

	/** 登录状态 */
	private int status = 0;
	/** 登录用户ID */
	private int user_id;

	private String title;

	private DBUtils dbUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_culture_info);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		dbUtil = new DBUtils(context);

		id = getIntExtra("id");
		type = getIntExtra("type");

		if (type == 1) {
			culture_info_type = ApiUtils.GET_HOPE;
			title = "愿景";
		} else if (type == 2) {
			culture_info_type = ApiUtils.GET_MISSION;
			title = "使命";
		} else if (type == 3) {
			culture_info_type = ApiUtils.GET_SPIRIT;
			title = "精神";
		} else if (type == 4) {
			culture_info_type = ApiUtils.GET_VALUE;
			title = "价值观";
		} else if (type == 5) {
			culture_info_type = ApiUtils.GET_MANAGEMENT;
			title = "经营方针";
		} else if (type == 6) {
			culture_info_type = ApiUtils.BRAND_EXPLAIN;
			title = "标志释义";
		}

		status = (Integer) SPUtils.get(context, "login_status", 0, AppConfig.LOGIN_STATUS_DATA);
		user_id = (Integer) SPUtils.get(context, "id", 0, AppConfig.LOGIN_INFO_DATA);

		tvTitle.setText(title);
		back.setImageResource(R.drawable.icon_back);

		// 初始化收藏，判断该篇博客是否已被收藏
		if (dbUtil.queryByIdAndType(id, type).getCollection_id() == id) {
			img_collection.setImageResource(R.drawable.icon_del_collection);
			collection = true;
		} else {
			img_collection.setImageResource(R.drawable.icon_add_collection);
			collection = false;
		}

		loading.show();
		getCultureInfo(id, type, user_id);

	}

	private void getCommentList(int id) {
		AjaxParams params = new AjaxParams();
		params.put("id", id);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(ApiUtils.TIME_OUT);
		fh.get(ApiUtils.ROOT_URL + ApiUtils.CULTURE_COMMENTS, params, new AjaxCallBack<Object>() {

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String str = t.toString();
				loading.dismiss();

				comment = (List<Comment>) JsonUtil.fromJson(str, new TypeToken<List<Comment>>() {
				});

				if (comment.size() > 0) {
					listview.setAdapter(new CommonAdapter<Comment>(context, R.layout.item_comment, comment) {

						@Override
						public void onUpdate(BaseAdapterHelper helper, Comment item, int position) {
							if (null != item.getUser_name()) {
								helper.setText(R.id.tv_name, item.getUser_name());
								helper.setHeadPicUrl(R.id.image_headpic, item.getUser_avatar());
							} else {
								helper.setText(R.id.tv_name, item.getManager_name());
								helper.setHeadPicUrl(R.id.image_headpic, item.getManager_avatar());
							}
							helper.setText(R.id.tv_body, item.getBody());
							helper.setText(R.id.tv_createdAt, item.getCreated_at());
						}
					});
				} else {

				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				loading.dismiss();
				if (t != null) {
					showToast("加载失败，请稍后再试！");
				}
				super.onFailure(t, errorNo, strMsg);
			}
		});
	}

	private void getCultureInfo(final int id, int type, int user_id) {
		AjaxParams params = new AjaxParams();
		if (status == 1) {
			params.put("user_id", user_id);
		} else if (status == 2) {
			params.put("manager_id", user_id);
		} else if (status == 0) {
			if (user_id != 0) {
				params.put("user_id", user_id);
			}
		}
		params.put("id", id);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(ApiUtils.TIME_OUT);
		fh.get(ApiUtils.ROOT_URL + culture_info_type, params, new AjaxCallBack<Object>() {

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String str = t.toString();
				culture = (Culture) JsonUtil.fromJson(str, Culture.class);
				loading.dismiss();

				tv_comment_count.setText(culture.getComment_count() + "");
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
				getCommentList(id);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
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
		params.put("id", id);
		if (status == 1 || status == 0) {
			params.put("user_id", user_id);
		} else if (status == 2) {
			params.put("manager_id", user_id);
		}

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(ApiUtils.TIME_OUT);
		fh.post(ApiUtils.ROOT_URL + ApiUtils.CULTURE_LIKE, params, new AjaxCallBack<Object>() {

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String str = t.toString();
				result = new Result();
				result = (Result) JsonUtil.fromJson(str, Result.class);

				if (result.isStatus()) {
					getCultureInfo(id, type, user_id);
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
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
		params.put("id", id);
		if (status == 1 || status == 0) {
			params.put("user_id", user_id);
		} else if (status == 2) {
			params.put("manager_id", user_id);
		}

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(ApiUtils.TIME_OUT);
		fh.post(ApiUtils.ROOT_URL + ApiUtils.CULTURE_UNLIKE, params, new AjaxCallBack<Object>() {

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String str = t.toString();
				result = new Result();
				result = (Result) JsonUtil.fromJson(str, Result.class);
				if (result.isStatus()) {
					getCultureInfo(id, type, user_id);
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
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
			showDialog();
		} else {

		}
	}

	private void showDialog() {
		new AlertDialog(context).builder().setTitle(getString(R.string.remind))
				.setMsg(getString(R.string.dialog_title))
				.setPositiveButton(getString(R.string.personal_login), new OnClickListener() {
					@Override
					public void onClick(View v) {
						openActivity(PersonalLoginActivity.class, false);
					}
				}).setNegativeButton(getString(R.string.company_login), new OnClickListener() {
					@Override
					public void onClick(View v) {
						openActivity(CompanyLoginActivity.class, false);
					}
				}).show();
	}

	public void collection(View v) {
		collection_.setCollection_id(culture.getId());
		collection_.setComment_count(culture.getComment_count());
		collection_.setContent(culture.getContent());
		collection_.setCreated_at("");
		collection_.setImage(culture.getLogo());
		collection_.setName(culture.getName());
		collection_.setType(type);
		collection_.setLike_count(culture.getVote_count());

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
