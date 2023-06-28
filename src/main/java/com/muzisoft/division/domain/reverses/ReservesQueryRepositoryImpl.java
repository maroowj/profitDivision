package com.muzisoft.division.domain.reverses;

import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.reserve.ReservesDetailsResponse;
import com.muzisoft.division.web.api.dto.admin.reserve.ReservesListResponse;
import com.muzisoft.division.web.api.dto.common.BasicCondition;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.BasicSearchType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
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

import static com.muzisoft.division.domain.profit.QProfitDetails.profitDetails;
import static com.muzisoft.division.domain.reverses.QReserves.reserves;
import static com.muzisoft.division.domain.reverses.QReservesLog.reservesLog;
import static com.muzisoft.division.domain.user.QUser.user;
import static com.muzisoft.division.domain.user.QUserDetails.userDetails;

public class ReservesQueryRepositoryImpl extends QuerydslRepositorySupport implements ReservesQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ReservesQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Reserves.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public long totalSavedAmount(UserDetails userDetails) {
        return queryFactory.select(
                new CaseBuilder()
                        .when(reserves.savedAmount.sum().isNull())
                        .then(Expressions.constant(0))
                        .otherwise(reserves.savedAmount.sum())
        )
                .from(reserves)
                .where(
                        reserves.userDetails.eq(userDetails),
                        reserves.usable.isTrue()
                )
                .fetchOne();
    }

    @Override
    public long totalUsedAmount(UserDetails userDetails) {
        return queryFactory.select(
                        new CaseBuilder()
                                .when(reserves.usedAmount.sum().isNull())
                                .then(Expressions.constant(0))
                                .otherwise(reserves.usedAmount.sum())
                )
                .from(reserves)
                .where(
                        reserves.userDetails.eq(userDetails),
                        reserves.usable.isTrue()
                )
                .fetchOne();
    }



    private Predicate searchBySearchTypeAndKeyword(BasicSearchType queryType, String query) {
        BooleanBuilder builder = new BooleanBuilder();
        switch (queryType) {
            case WHOLE:
                builder.and(userDetails.name.contains(query).or(user.userId.contains(query)).or(userDetails.membershipNumber.contains(query)));
                break;
            case USER_NAME:
                builder.and(userDetails.name.contains(query));
                break;
            case USER_ID:
                builder.and(user.userId.contains(query));
                break;
            case MEMBERSHIP_NUMBER:
                builder.and(userDetails.membershipNumber.contains(query));
                break;
        }
        return builder;
    }

    private BooleanExpression dateGoe(Date dateFrom) {
        return dateFrom != null ? profitDetails.createdAt.goe(dateFrom) : null;
    }

    private BooleanExpression dateLt(Date dateTo) {
        return dateTo != null ? profitDetails.createdAt.lt(dateTo) : null;
    }
}
