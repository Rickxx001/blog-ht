package com.stx.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    FAILURE(500,"操作失败"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    NICKNAME_EXIST(508,"昵称已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    CONTENT_NOTNULL(506,"评论为空"),
    FILE_TYPE_ERROR(507,"文件格式错误"),
    NICKNAME_NOTNULL(509,"昵称必须填写"),
    PASSWORD_NOTNULL(510,"密码不能伟空"),
    EMAIL_NOTNULL(511,"邮箱不能为空"),
    USER_ISNOT(512,"用户不存在"),
    USER_ERROR(513,"用户操作可疑"),
    ARTICLE_NOTNULL(514,"同学写点什么吧"),
    EXCE_ERROR(515,"导出错误"),
    MENU_ERROR(516,"修改菜单错误，不可以把当前菜单设置为父菜单"),
    MENU_DEL_ERROR(517,"存在子菜单，不可删除" ),
    UPDATE_ADMIN_ERROR(518,"超级管理员不可删除");


    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
