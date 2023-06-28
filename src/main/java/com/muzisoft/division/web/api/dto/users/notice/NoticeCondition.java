package com.muzisoft.division.web.api.dto.users.notice;

import com.muzisoft.division.utils.validation.Enum;
import com.muzisoft.division.web.api.dto.common.enums.BasicSearchType;
import com.muzisoft.division.web.api.dto.common.enums.NoticeSearchFilter;
import com.muzisoft.division.web.api.dto.common.enums.NoticeSearchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeCondition {

    @NotEmpty
    @Enum(enumClass = NoticeSearchType.class, ignoreCase = true, message = "전체, 제목, 내용 중에 하나이어야 합니다.")
    private String queryType;

    public NoticeSearchType getQueryType() {
        return NoticeSearchType.find(queryType);
    }

    @NotEmpty
    @Enum(enumClass = NoticeSearchFilter.class, ignoreCase = true, message = "전체, 공지, 일반 중에 하나이어야 합니다.")
    private String searchType;

    public NoticeSearchFilter getSearchType() {
        return NoticeSearchFilter.find(searchType);
    }
}
