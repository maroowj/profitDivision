package com.muzisoft.division.domain.profit;

import com.muzisoft.division.domain.board.Notice;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.profit.ExpectedProfitListResponse;
import com.muzisoft.division.web.api.dto.admin.profit.ExpectedProfitRequest;
import com.muzisoft.division.web.api.dto.admin.profit.MonthlyExpectedProfitsResponse;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitCondition;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.ProfitSearchType;
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

import static com.muzisoft.division.domain.board.QNotice.notice;
import static com.muzisoft.division.domain.profit.QExpectedProfit.expectedProfit;
import static com.muzisoft.division.domain.profit.QExpectedProfitDetails.expectedProfitDetails;
import static com.muzisoft.division.domain.user.QUser.user;
import static com.muzisoft.division.domain.user.QUserDetails.userDetails;

public class ExpectedProfitQueryRepositoryImpl extends QuerydslRepositorySupport implements ExpectedProfitQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ExpectedProfitQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ExpectedProfit.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<MonthlyExpectedProfitsResponse> monthlyExpectedProfitList(Pageable pageable, CommonCondition condition) {
        JPAQuery<MonthlyExpectedProfitsResponse> query = queryFactory.select(Projections.constructor(MonthlyExpectedProfitsResponse.class,
                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", expectedProfit.createdAt),
                expectedProfit.amount
                ))
                .from(expectedProfit)
                .where(
                        dateGoe(condition.getDateFrom()),
                        dateLt(condition.getDateTo())
                );

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder<? extends ExpectedProfit> pathBuilder = new PathBuilder<ExpectedProfit>(expectedProfit.getType(), expectedProfit.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<MonthlyExpectedProfitsResponse> result = query.fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression dateGoe(Date dateFrom) {
        return dateFrom != null ? expectedProfit.createdAt.goe(dateFrom) : null;
    }

    private BooleanExpression dateLt(Date dateTo) {
        return dateTo != null ? expectedProfit.createdAt.lt(dateTo) : null;
    }
}
