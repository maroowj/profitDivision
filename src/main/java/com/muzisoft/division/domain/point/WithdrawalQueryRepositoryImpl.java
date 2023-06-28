package com.muzisoft.division.domain.point;

import com.muzisoft.division.domain.common.enums.ProfitLogType;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.withdrawal.WithdrawalCondition;
import com.muzisoft.division.web.api.dto.admin.withdrawal.WithdrawalListPerUserResponse;
import com.muzisoft.division.web.api.dto.admin.withdrawal.WithdrawalListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.ProfitStatusFilter;
import com.muzisoft.division.web.api.dto.common.enums.WithdrawalSearchType;
import com.muzisoft.division.web.api.dto.common.enums.WithdrawalStatusCheck;
import com.muzisoft.division.web.api.dto.common.enums.WithdrawalStatusCheckUser;
import com.muzisoft.division.web.api.dto.users.withdrawal.UserWithdrawalListResponse;
import com.muzisoft.division.web.api.dto.users.withdrawal.WithdrawalTypeCondition;
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
import java.util.List;


import static com.muzisoft.division.domain.point.QWithdrawal.withdrawal;
import static com.muzisoft.division.domain.profit.QProfitDetailsLog.profitDetailsLog;
import static com.muzisoft.division.domain.user.QUser.user;
import static com.muzisoft.division.domain.user.QUserDetails.userDetails;

public class WithdrawalQueryRepositoryImpl extends QuerydslRepositorySupport implements WithdrawalQueryRepository {

    private final JPAQueryFactory queryFactory;

    public WithdrawalQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Withdrawal.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<WithdrawalListResponse> withdrawalList(Pageable pageable, WithdrawalCondition withdrawalCondition, CommonCondition condition) {
        JPAQuery<WithdrawalListResponse> query = queryFactory.select(Projections.constructor(WithdrawalListResponse.class,
                        userDetails.seq,
                        withdrawal.seq,
                        userDetails.membershipNumber,
                        user.userId,
                        userDetails.name,
                        withdrawal.totalBalance,
                        withdrawal.amount,
                        withdrawal.bankName,
                        withdrawal.accountNumber,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", withdrawal.createdAt),
                        withdrawal.accepted,
                        withdrawal.source,
                        Expressions.constant("-")
                ))
                .from(withdrawal)
                .leftJoin(userDetails).on(withdrawal.userDetails.eq(userDetails))
                .leftJoin(user).on(user.userDetails.eq(userDetails))
                .where(
                        searchBySearchTypeAndKeyword(withdrawalCondition.getQueryType(), condition.getQuery()),
                        searchByWithdrawalStatus(withdrawalCondition.getWithdrawalStatusCheck()),
                        dateGoe(condition.getDateFrom()),
                        dateLt(condition.getDateTo())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            if (o.getProperty().equals("userId")) {
                PathBuilder<? extends User> pathBuilder = new PathBuilder<User>(user.getType(), user.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            } else if (o.getProperty().equals("name") || o.getProperty().equals("membershipNumber")) {
                PathBuilder<? extends UserDetails> pathBuilder = new PathBuilder<UserDetails>(userDetails.getType(), userDetails.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            } else {
                PathBuilder<? extends Withdrawal> pathBuilder = new PathBuilder<Withdrawal>(withdrawal.getType(), withdrawal.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
        }

        QueryResults<WithdrawalListResponse> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<WithdrawalListPerUserResponse> withdrawalListPerUser(Pageable pageable, UserDetails userDetails) {
        JPAQuery<WithdrawalListPerUserResponse> query = queryFactory.select(Projections.constructor(WithdrawalListPerUserResponse.class,
                        withdrawal.seq,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", withdrawal.createdAt),
                        withdrawal.totalBalance,
                        withdrawal.amount,
                        withdrawal.accepted,
                        withdrawal.source,
                        Expressions.constant("-")
                ))
                .from(withdrawal)
                .where(
                        withdrawal.userDetails.eq(userDetails)
                );

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder<? extends Withdrawal> pathBuilder = new PathBuilder<Withdrawal>(withdrawal.getType(), withdrawal.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<WithdrawalListPerUserResponse> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<UserWithdrawalListResponse> userWithdrawalList(Pageable pageable, WithdrawalTypeCondition withdrawalTypeCondition, CommonCondition condition, UserDetails userDetails) {
        JPAQuery<UserWithdrawalListResponse> query = queryFactory.select(Projections.constructor(UserWithdrawalListResponse.class,
                        Expressions.constant(0),
                        withdrawal.seq,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %T')", withdrawal.createdAt),
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %T')", withdrawal.lastModifiedAt),
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %T')", withdrawal.requestedAt),
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %T')", withdrawal.acceptedAt),
                        withdrawal.amount,
                        withdrawal.totalBalance,
                        withdrawal.accepted,
                        withdrawal.comment
                ))
                .from(withdrawal)
                .where(
                        withdrawal.source.eq(0),
                        withdrawal.userDetails.eq(userDetails),
                        searchByWithdrawalStatusType(withdrawalTypeCondition.getType()),
                        dateGoe(condition.getDateFrom()),
                        dateLt(condition.getDateTo())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder<? extends Withdrawal> pathBuilder = new PathBuilder<Withdrawal>(withdrawal.getType(), withdrawal.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<UserWithdrawalListResponse> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<Withdrawal> amountPerYear(UserDetails userDetails, Date firstDay) {
        return queryFactory.select(
                        withdrawal
                )
                .from(withdrawal)
                .where(
                        withdrawal.userDetails.eq(userDetails),
                        withdrawal.accepted.eq(1),
                        withdrawal.source.eq(0),
                        dateGoe(firstDay)
                )
                .fetch();
    }

    private Predicate searchByWithdrawalStatus(WithdrawalStatusCheck withdrawalStatusCheck) {
        BooleanBuilder builder = new BooleanBuilder();
        switch (withdrawalStatusCheck) {
            case WHOLE:
                return null;
            case WAIT:
                builder.and(withdrawal.accepted.eq(0));
                break;
            case ACCEPT:
                builder.and(withdrawal.accepted.eq(1));
                break;
            case REFUSE:
                builder.and(withdrawal.accepted.eq(2));
                break;
        }
        return builder;
    }


    private Predicate searchBySearchTypeAndKeyword(WithdrawalSearchType queryType, String query) {
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

    private Predicate searchByWithdrawalStatusType(WithdrawalStatusCheckUser type) {
        BooleanBuilder builder = new BooleanBuilder();
        switch (type) {
            case WHOLE:
//                builder.and(withdrawal.accepted.eq(1).or(withdrawal.accepted.eq(0)));
                return null;
            case ACCEPT:
                builder.and(withdrawal.accepted.eq(1));
                break;
            case REFUSE:
                builder.and(withdrawal.accepted.eq(2));
                break;
        }
        return builder;
    }

    private BooleanExpression dateGoe(Date dateFrom) {
        return dateFrom != null ? withdrawal.createdAt.goe(dateFrom) : null;
    }

    private BooleanExpression dateLt(Date dateTo) {
        return dateTo != null ? withdrawal.createdAt.lt(dateTo) : null;
    }
}
