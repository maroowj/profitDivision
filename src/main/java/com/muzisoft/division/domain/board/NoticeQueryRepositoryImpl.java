package com.muzisoft.division.domain.board;

import com.muzisoft.division.domain.manager.ManagerUsers;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.notice.NoticeListResponse;
import com.muzisoft.division.web.api.dto.admin.popup.PopupCondition;
import com.muzisoft.division.web.api.dto.admin.popup.PopupListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.NoticeSearchFilter;
import com.muzisoft.division.web.api.dto.common.enums.NoticeSearchType;
import com.muzisoft.division.web.api.dto.common.enums.PopupExposureType;
import com.muzisoft.division.web.api.dto.users.notice.NoticeCondition;
import com.muzisoft.division.web.api.dto.users.notice.NoticeListForUserResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Date;
import java.util.List;

import static com.muzisoft.division.domain.board.QNotice.notice;
import static com.muzisoft.division.domain.board.QPopup.popup;
import static com.muzisoft.division.domain.manager.QManagerUsers.managerUsers;
import static com.muzisoft.division.domain.user.QUser.user;
import static com.muzisoft.division.domain.user.QUserDetails.userDetails;

public class NoticeQueryRepositoryImpl extends QuerydslRepositorySupport implements NoticeQueryRepository {

    private final JPAQueryFactory queryFactory;

    public NoticeQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Notice.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<NoticeListResponse> noticeList(Pageable pageable, CommonCondition condition) {
        JPAQuery<NoticeListResponse> query = queryFactory.select(Projections.constructor(NoticeListResponse.class,
                        notice.noticeNo,
                        notice.seq,
                        notice.title,
                        userDetails.name,
                        managerUsers.nickname,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %T')", notice.createdAt)
                ))
                .from(notice)
                .leftJoin(userDetails).on(notice.writer.eq(userDetails))
                .leftJoin(user).on(user.userDetails.eq(userDetails))
                .leftJoin(managerUsers).on(managerUsers.user.eq(user))
                .where(
                        notice.title.contains(condition.getQuery()).or(notice.contents.contains(condition.getQuery()))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            if (o.getProperty() == "name") {
                PathBuilder<? extends UserDetails> pathBuilder = new PathBuilder<UserDetails>(userDetails.getType(), userDetails.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }else if (o.getProperty() == "nickname") {
                PathBuilder<? extends ManagerUsers> pathBuilder = new PathBuilder<ManagerUsers>(managerUsers.getType(), managerUsers.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }else {
                PathBuilder<? extends Notice> pathBuilder = new PathBuilder<Notice>(notice.getType(), notice.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
        }

        QueryResults<NoticeListResponse> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<NoticeListForUserResponse.NoticeList> noticeListForUser() {
        return queryFactory.select(Projections.constructor(NoticeListForUserResponse.NoticeList.class,
                        notice.seq,
                        notice.noticeNo,
                        notice.fixed,
                        notice.title,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", notice.createdAt),
                        Expressions.constant("-")
                ))
                .from(notice)
                .where(
                        notice.fixed.isTrue()
                )
                .orderBy(notice.createdAt.desc())
                .fetch();
    }

    @Override
    public Page<NoticeListForUserResponse.NormalList> normalListForUser(Pageable pageable, NoticeCondition noticeCondition, CommonCondition condition) {
        JPAQuery<NoticeListForUserResponse.NormalList> query = queryFactory.select(Projections.constructor(NoticeListForUserResponse.NormalList.class,
                        notice.seq,
                        notice.noticeNo,
                        notice.fixed,
                        notice.title,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", notice.createdAt),
                        Expressions.constant("-")
                ))
                .from(notice)
                .where(
                        notice.fixed.isFalse(),
                        searchByQueryTypeAndQuery(noticeCondition.getQueryType(), condition.getQuery())
//                        filterBySearchType(noticeCondition.getSearchType())
                );

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder<? extends Notice> pathBuilder = new PathBuilder<Notice>(notice.getType(), notice.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<NoticeListForUserResponse.NormalList> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private Predicate searchByQueryTypeAndQuery(NoticeSearchType noticeSearchType, String query) {
        BooleanBuilder builder = new BooleanBuilder();
        switch (noticeSearchType) {
            case WHOLE:
                builder.and(notice.title.contains(query).or(notice.contents.contains(query)));
                break;
            case TITLE:
                builder.and(notice.title.contains(query));
                break;
            case CONTENTS:
                builder.and(notice.contents.contains(query));
                break;
        }
        return builder;
    }

    private Predicate filterBySearchType(NoticeSearchFilter noticeSearchFilter) {
        BooleanBuilder builder = new BooleanBuilder();
        switch (noticeSearchFilter) {
            case WHOLE:
                return null;
            case NOTICE:
                builder.and(notice.fixed.isTrue());
                break;
            case NORMAL:
                builder.and(notice.fixed.isFalse());
                break;
        }
        return builder;
    }

    private BooleanExpression dateGoe(Date dateFrom) {
        return dateFrom != null ? notice.createdAt.goe(dateFrom) : null;
    }

    private BooleanExpression dateLt(Date dateTo) {
        return dateTo != null ? notice.createdAt.lt(dateTo) : null;
    }
}
