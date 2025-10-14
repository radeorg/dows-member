package org.dows.member.admin;

import feign.RequestLine;
import org.dows.member.api.admin.AdminMemberInterestsApi;

public interface AdminMemberInterestsFeign extends AdminMemberInterestsApi {
    // 可根据需要添加 Feign 注解（此处保留接口继承关系以复用 API 定义）
    @RequestLine("GET /ping")
    default String ping() { return "ok"; }
}