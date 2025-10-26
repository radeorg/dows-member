package org.dows.member.biz.admin;

import com.mybatisflex.core.paginate.Page;
import org.dows.member.form.PageQuery;
import org.dows.member.request.admin.AdminMemberInstanceQueryRequest;
import org.dows.member.response.MemberInstanceGetResponse;

public interface AdminMemberInstanceHandler {

    Page<MemberInstanceGetResponse> query(PageQuery pageQuery, AdminMemberInstanceQueryRequest request);
}