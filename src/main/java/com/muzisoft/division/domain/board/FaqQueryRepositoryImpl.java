package com.muzisoft.division.domain.board;

import com.muzisoft.division.domain.manager.ManagerUsers;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.board.BoardListResponse;
import com.muzisoft.division.web.api.dto.admin.faq.FaqListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.BoardSearchType;
import com.muzisoft.division.web.api.dto.users.board.BoardCondition;
import com.muzisoft.division.web.api.dto.users.board.BoardListForUserResponse;
import com.muzisoft.division.web.api.dto.users.faq.FaqListResponseForUser;
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

import static com.muzisoft.division.domain.board.QBoard.board;
import static com.muzisoft.division.domain.board.QFaq.faq;
import static com.muzisoft.division.domain.manager.QManagerUsers.managerUsers;
import static com.muzisoft.division.domain.user.QUser.user;
import static com.muzisoft.division.domain.user.QUserDetails.userDetails;

public class FaqQueryRepositoryImpl extends QuerydslRepositorySupport implements FaqQueryRepository {

    private final JPAQueryFactory queryFactory;

    public FaqQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Faq.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<FaqListResponse> faqList(Pageable pageable, CommonCondition condition) {
        JPAQuery<FaqListResponse> query = queryFactory.select(Projections.constructor(FaqListResponse.class,
                        Expressions.constant(0),
                        faq.seq,
                        faq.title,
                        userDetails.name,
                        managerUsers.nickname,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %T')", faq.createdAt)
                ))
                .from(faq)
                .leftJoin(userDetails).on(faq.writer.eq(userDetails))
                .leftJoin(user).on(user.userDetails.eq(userDetails))
                .leftJoin(managerUsers).on(managerUsers.user.eq(user))
                .where(
                        faq.title.contains(condition.getQuery()).or(faq.contents.contains(condition.getQuery()))
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
                PathBuilder<? extends Faq> pathBuilder = new PathBuilder<Faq>(faq.getType(), faq.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
        }

        QueryResults<FaqListResponse> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<FaqListResponseForUser> faqListForUser(Pageable pageable, CommonCondition condition) {
        JPAQuery<FaqListResponseForUser> query = queryFactory.select(Projections.constructor(FaqListResponseForUser.class,
                        faq.title,
                        faq.contents
                ))
                .from(faq)
                .where(
                        faq.title.contains(condition.getQuery()).or(faq.contents.contains(condition.getQuery()))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        QueryResults<FaqListResponseForUser> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }


    private Predicate searchByQueryTypeAndQuery(BoardSearchType boardSearchType, String query) {
        BooleanBuilder builder = new BooleanBuilder();
        switch (boardSearchType) {
            case WHOLE:
                builder.and(board.title.contains(query).or(board.contents.contains(query)));
                break;
            case TITLE:
                builder.and(board.title.contains(query));
                break;
            case CONTENTS:
                builder.and(board.contents.contains(query));
                break;
        }
        return builder;
    }

    /*private Predicate filterBySearchType(NoticeSearchFilter noticeSearchFilter) {
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
    }*/

    private BooleanExpression dateGoe(Date dateFrom) {
        return dateFrom != null ? board.createdAt.goe(dateFrom) : null;
    }

    private BooleanExpression dateLt(Date dateTo) {
        return dateTo != null ? board.createdAt.lt(dateTo) : null;
    }
}
