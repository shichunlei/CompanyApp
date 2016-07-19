package com.cells.companyapp.view;

import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.base.BaseAdapterHelper;
import com.cells.companyapp.base.CommonAdapter;
import com.cells.companyapp.been.Comment;
import com.cells.companyapp.been.Result;
import com.cells.companyapp.utils.*;
import com.cells.companyapp.widget.CircularProgressDialog;
import com.google.gson.reflect.TypeToken;

public class CommentActivity extends BaseActivity {

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView back;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.xlistview)
	private ListView listview;

	@ViewInject(id = R.id.btn_send, click = "send")
	private Button send;
	@ViewInject(id = R.id.et_comment)
	private EditText etBody;

	private List<Comment> comment;

	private CircularProgressDialog loading;

	/**  */
	private int id;
	/**  */
	private String type;
	/**  */
	private String comment_list;
	/**  */
	private String add_comment;
	private int user_id;
	private int status;
	private String token;

	Result result = new Result();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		Bundle bundle = getIntent().getExtras();
		id = bundle.getInt("id");
		type = bundle.getString("type");
		if (type.equals("cultures")) {
			comment_list = HttpUtils.CULTURE_COMMENTS;
			add_comment = HttpUtils.CULTURE_ADD_COMMENT;
		} else if (type.equals("news")) {
			comment_list = HttpUtils.WINDOW_COMMENTS;
			add_comment = HttpUtils.WINDOW_ADD_COMMENT;
		}

		status = (Integer) SPUtils.get(context, "login_status", 0, AppConfig.LOGIN_STATUS_DATA);
		user_id = (Integer) SPUtils.get(context, "id", 0, AppConfig.LOGIN_INFO_DATA);
		token = (String) SPUtils.get(context, "token", "", AppConfig.LOGIN_INFO_DATA);

		tvTitle.setText("评论");
		back.setImageResource(R.drawable.icon_back);

		loading = CircularProgressDialog.show(context);

		loading.show();
		getCommentList(id, comment_list);
	}

	private void getCommentList(int id, String comment_list) {
		AjaxParams params = new AjaxParams();

		params.put("id", id);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + comment_list, params, new AjaxCallBack<Object>() {

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
								helper.setHeadPicUrl(context, R.id.image_headpic, item.getUser_avatar());
							} else {
								helper.setText(R.id.tv_name, item.getManager_name());
								helper.setHeadPicUrl(context, R.id.image_headpic, item.getManager_avatar());
							}
							helper.setText(R.id.tv_body, item.getBody());
							helper.setText(R.id.tv_createdAt, item.getCreated_at());
						}
					});
				} else {
					showToast("没有评论！");
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

	public void send(View v) {
		String body = etBody.getText().toString().trim();
		if (StringUtil.isEmpty(body)) {
			showToast("请输入评论内容！");
		} else {
			loading.show();
			sendComment(id, body, add_comment, user_id, token);
		}
	}

	private void sendComment(final int id, String body, String add_comment, int user_id, String token) {
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		params.put("content", body);
		if (status == 1) {
			params.put("user_id", user_id);
		} else if (status == 2) {
			params.put("manager_id", user_id);
		}
		params.put("auth_token", token);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.post(HttpUtils.ROOT_URL + add_comment, params, new AjaxCallBack<Object>() {

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String str = t.toString();
				result = (Result) JsonUtil.fromJson(str, Result.class);
				if (result.isStatus()) {
					etBody.setText("");
					getCommentList(id, comment_list);
				} else {
					loading.dismiss();
					showToast("faulse");
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
	 * 返回
	 * 
	 * @param v
	 */
	public void back(View v) {
		finish();
	}
}
