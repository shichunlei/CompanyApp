package com.cells.companyapp.utils;

/**
 * 网络接口辅助类
 * 
 * @author leo
 * 
 */
public class HttpUtils {

	/** 本地服务器地址 */
	// public static final String ROOT_URL = "http://10.0.1.140:3000";
	public static final String ROOT_URL = "http://101.200.174.126:15888";
	/** 超时默认时间10秒 */
	public static final int TIME_OUT = 10 * 1000;

	// User 用户
	/** POST /rest/user/register.json 个人用户注册 */
	public static final String REGISTER = "/rest/user/register.json";
	/** POST /rest/user/login.json 个人用户登录 */
	public static final String LOGIN = "/rest/user/login.json";
	/** PUT /rest/user/update_profile.json 个人用户资料修改 */
	public static final String UPDATE_PROFILE = "/rest/user/update_profile.json";
	/** PUT /rest/user/upload_avatar.json 个人用户修改头像 */
	public static final String UPLOAD_AVATAR = "/rest/user/upload_avatar.json";
	/** POST /rest/user/manager_login.json 企业用户登录 */
	public static final String MANAGER_LOGIN = "/rest/user/manager_login.json";

	// window 企业视窗
	/** GET /rest/window/headline.json 企业视窗-头条 */
	public static final String HEADLINE = "/rest/window/headline.json";
	/** GET /rest/window/activity.json 企业视窗-活动 */
	public static final String ACTIVITY = "/rest/window/activity.json";
	/** GET /rest/window/brand_story.json 企业视窗-企业微视 */
	public static final String BRAND_STORY = "/rest/window/brand_story.json";
	/** GET /rest/window/book.json 企业视窗-企业书籍 */
	public static final String BOOK = "/rest/window/book.json";
	/** GET /rest/window/culture_trip.json 企业视窗-文化之旅 */
	public static final String CULTURE_TRIP = "/rest/window/culture_trip.json";
	/** GET /rest/window/get_news.json 查看新闻详情 */
	public static final String GET_NEWS = "/rest/window/get_news.json";
	/** POST /rest/window/like.json 新闻--点赞 */
	public static final String WINDOW_LIKE = "/rest/window/like.json";
	/** POST /rest/window/unlike.json 新闻--取消赞 */
	public static final String WINDOW_UNLIKE = "/rest/window/unlike.json";
	/** GET /rest/window/comments.json 新闻评论列表 */
	public static final String WINDOW_COMMENTS = "/rest/window/comments.json";
	/** POST /rest/window/add_comment.json 发表新闻评论 */
	public static final String WINDOW_ADD_COMMENT = "/rest/window/add_comment.json";
	/** POST /rest/window/reply_comment.json 回复新闻评论 */
	public static final String WINDOW_REPLY_COMMENT = "/rest/window/reply_comment.json";
	/** DELETE /rest/window/destroy_comment.json 删除评论 */
	public static final String WINDOW_DESTYOY_COMMENT = "/rest/window/destroy_comment.json";

	// culture 企业文化中心
	/** GET /rest/culture/hopes.json 愿景 */
	public static final String HOPES = "/rest/culture/hopes.json";
	/** GET /rest/culture/get_hope.json 查看愿景详情 */
	public static final String GET_HOPE = "/rest/culture/get_hope.json";
	/** GET /rest/culture/missions.json 使命 */
	public static final String MISSIONS = "/rest/culture/missions.json";
	/** GET /rest/culture/get_mission.json 查看使命详情 */
	public static final String GET_MISSION = "/rest/culture/get_mission.json";
	/** GET /rest/culture/spirits.json 精神 */
	public static final String SPIRITS = "/rest/culture/spirits.json";
	/** GET /rest/culture/get_spirit.json 查看精神详情 */
	public static final String GET_SPIRIT = "/rest/culture/get_spirit.json";
	/** GET /rest/culture/values.json 价值观 */
	public static final String VALUES = "/rest/culture/values.json";
	/** GET /rest/culture/get_value.json 查看价值观详情 */
	public static final String GET_VALUE = "/rest/culture/get_value.json";
	/** GET /rest/culture/managements.json 经营方针 */
	public static final String MANAGEMENTS = "/rest/culture/managements.json";
	/** GET /rest/culture/get_management.json 查看经营方针详情 */
	public static final String GET_MANAGEMENT = "/rest/culture/get_management.json";
	/** GET /rest/culture/brand_explains.json 品牌释义 */
	public static final String BRAND_EXPLAINS = "/rest/culture/brand_explains.json";
	/** GET /rest/culture/brand_explain.json 查看品牌释义详情 */
	public static final String BRAND_EXPLAIN = "/rest/culture/brand_explain.json";
	/** POST /rest/culture/like.json 企业文化中心--点赞 */
	public static final String CULTURE_LIKE = "/rest/culture/like.json";
	/** POST /rest/culture/unlike.json 企业文化中心--取消赞 */
	public static final String CULTURE_UNLIKE = "/rest/culture/unlike.json";
	/** GET /rest/culture/comments.json 企业文化中心评论列表 */
	public static final String CULTURE_COMMENTS = "/rest/culture/comments.json";
	/** POST /rest/culture/add_comment.json 发表评论 */
	public static final String CULTURE_ADD_COMMENT = "/rest/culture/add_comment.json";
	/** POST /rest/culture/reply_comment.json 回复评论 */
	public static final String CULTURE_REPLY_COMMENT = "/rest/culture/reply_comment.json";

	// gallery 画廊
	/** GET /rest/gallery/galleries.json 企业文化画廊 */
	public static final String GALLERY = "/rest/gallery/galleries.json"; // page
	/** GET /rest/gallery/year_of_gallery.json 画廊分类信息 */
	public static final String YEAR_OF_GALLERY = "/rest/gallery/year_of_gallery.json"; // company_id
	/** GET /rest/gallery/get_gallery.json 画廊信息 */
	public static final String GET_GALLERY = "/rest/gallery/get_gallery.json"; // gallery_id

	// yelow 企业黄页
	/** GET /rest/yelow_page/yellow_pages.json 企业黄页 */
	public static final String YELOW_PAGE = "/rest/yelow_page/yellow_pages.json";
	/** GET /rest/yelow_page/page_detail.json 查看企业黄页详情 */
	public static final String PAGE_DETAIL = "/rest/yelow_page/page_detail.json";
	/** GET /rest/yelow_page/search_yellow_page.json 企业黄页搜索 */
	public static final String SEARCH_YELOW_PAGE = "/rest/yelow_page/search_yellow_page.json";

	// video 视频画刊
	/** GET /rest/video/videos.json 视频画刊 */
	public static final String VIDEOS = "/rest/video/videos.json";
	/** GET /rest/video/you_like.json 猜你喜欢 */
	public static final String YOU_LIKE = "/rest/video/you_like.json";
	/** GET /rest/video/video_detail.json 视频画刊详情 */
	public static final String VIDEO_DETAIL = "/rest/video/video_detail.json";

	/** 注册协议 */
	public static final String CLAUSE = "/clause.html";
}
