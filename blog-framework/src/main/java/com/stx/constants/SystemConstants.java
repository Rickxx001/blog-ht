package com.stx.constants;

public class SystemConstants {
    /**
     *  文章是1 草稿  0正常发布
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    public static final int ARTICLE_STATUS_NORMAL = 0;

//
    public static final String STATUS_NORMAL="0";

//    友链审核通过
    public static final String LINK_STATUS_NORMAL="0";

//    请求头 中 token
    public static final String HEADER_TOKEN="token";

//    redis 中键值对名
    public static final String REDIS_LOGIN ="bloglogin:";
    public static final String ADMIN_LOGIN ="login:";

    public static final Integer COMMENT_ROOT_ID=-1;

    /**
     * @user 20600
     * @Description  评论类型  1友链  0文章
     * @Date  2022/11/23
     **/
    public static final String COMMENT_TYPE_LINK = "1";
    public static final String COMMENT_TYPE_ARTICLE = "0";
//     todo  oss 测试域名
    public static final String OSS_BEFORE ="http://rlt04xpuc.hn-bkt.clouddn.com/";

//    文章浏览量
    public static final String ARTICLE_VIEW="article:viewCount";
    public static final String LOGIN_ERROR = "用户不存在";

//    菜单类型 C 表示菜单  F ： 按钮  M 目录
    public static final String MENU = "C";
    public static final String BUTTON = "F";

//    管理员标识
    public static final String ADMIN = "1";
}
