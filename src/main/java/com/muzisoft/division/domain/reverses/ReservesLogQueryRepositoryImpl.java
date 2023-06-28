package com.muzisoft.division.domain.reverses;

import com.muzisoft.division.domain.profit.Profit;
import com.muzisoft.division.domain.profit.ProfitDetails;
import com.muzisoft.division.domain.profit.ProfitDetailsQueryRepository;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitCondition;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitDetailsPerUser;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitListResponse;
import com.muzisoft.division.web.api.dto.admin.reserve.ReservesDetailsResponse;
import com.muzisoft.division.web.api.dto.admin.reserve.ReservesListResponse;
import com.muzisoft.division.web.api.dto.common.BasicCondition;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.BasicSearchType;
import com.muzisoft.division.web.api.dto.common.enums.ProfitSearchType;
import com.muzisoft.division.web.api.dto.users.reserve.UserReservesListResponse;
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

import static com.muzisoft.division.domain.profit.QProfitDetails.profitDetails;
import static com.muzisoft.division.domain.reverses.QReservesLog.reservesLog;
import static com.muzisoft.division.domain.user.QUser.user;
import static com.muzisoft.division.domain.user.QUserDetails.userDetails;

public class ReservesLogQueryRepositoryImpl extends QuerydslRepositorySupport implements ReservesLogQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ReservesLogQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ReservesLog.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<ReservesListResponse> reservesLogList(Pageable pageable, BasicCondition basicCondition, CommonCondition condition) {
        JPAQuery<ReservesListResponse> query = queryFactory.select(Projections.constructor(ReservesListResponse.class,
                        userDetails.seq,
                        userDetails.membershipNumber,
                        user.userId,
                        userDetails.name,
                        userDetails.mobile,
                        reservesLog.amount,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", reservesLog.createdAt),
                        reservesLog.content
                ))
                .from(reservesLog)
                .leftJoin(userDetails).on(reservesLog.userDetails.eq(userDetails))
                .leftJoin(user).on(userDetails.eq(user.userDetails))
                .where(
                        reservesLog.type.eq(1),
                        searchBySearchTypeAndKeyword(basicCondition.getQueryType(), condition.getQuery())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            if (o.getProperty().equals("userId")) {
                PathBuilder<? extends User> pathBuilder = new PathBuilder<User>(user.getType(), user.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            } else if (o.getProperty().equals("name") || o.getProperty().equals("membershipNumber") || o.getProperty().equals("mobile")) {
                PathBuilder<? extends UserDetails> pathBuilder = new PathBuilder<UserDetails>(userDetails.getType(), userDetails.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            } else {
                PathBuilder<? extends ReservesLog> pathBuilder = new PathBuilder<ReservesLog>(reservesLog.getType(), reservesLog.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
        }

        QueryResults<ReservesListResponse> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<ReservesDetailsResponse> reservesDetails(Pageable pageable, UserDetails userDetails, CommonCondition condition) {
        JPAQuery<ReservesDetailsResponse> query = queryFactory.select(Projections.constructor(ReservesDetailsResponse.class,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", reservesLog.createdAt),
                        reservesLog.amount,
                        reservesLog.residual,
                        reservesLog.type
                ))
                .from(reservesLog)
                .where(
                        reservesLog.userDetails.eq(userDetails),
                        reservesLog.type.ne(0),
                        dateGoe(condition.getDateFrom()),
                        dateLt(condition.getDateTo())
                );

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder<? extends ReservesLog> pathBuilder = new PathBuilder<ReservesLog>(reservesLog.getType(), reservesLog.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<ReservesDetailsResponse> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<UserReservesListResponse> userReservesList(Pageable pageable, CommonCondition condition, UserDetails userDetails) {
        JPAQuery<UserReservesListResponse> query = queryFactory.select(Projections.constructor(UserReservesListResponse.class,
                    Expressions.constant(0),
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", reservesLog.createdAt),
                    reservesLog.content,
                    reservesLog.type,
                    reservesLog.amount,
                    reservesLog.residual
                ))
                .from(reservesLog)
                .where(
                        reservesLog.userDetails.eq(userDetails),
                        dateGoe(condition.getDateFrom()),
                        dateLt(condition.getDateTo())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder<? extends ReservesLog> pathBuilder = new PathBuilder<ReservesLog>(reservesLog.getType(), reservesLog.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<UserReservesListResponse> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
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
        return dateFrom != null ? reservesLog.createdAt.goe(dateFrom) : null;
    }

    private BooleanExpression dateLt(Date dateTo) {
        return dateTo != null ? reservesLog.createdAt.lt(dateTo) : null;
    }
}
