package com.muzisoft.division.domain.dues;

import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.dues.DuesCondition;
import com.muzisoft.division.web.api.dto.admin.dues.DuesListResponse;
import com.muzisoft.division.web.api.dto.admin.member.WaitingListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.DuesPaidCheck;
import com.muzisoft.division.web.api.dto.common.enums.DuesSearchType;
import com.muzisoft.division.web.api.dto.common.enums.UserSearchType;
import com.muzisoft.division.web.api.dto.common.enums.UserWithdrawalType;
import com.muzisoft.division.web.api.dto.users.dues.UserDuesListResponse;
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

import static com.muzisoft.division.domain.dues.QDues.dues;
import static com.muzisoft.division.domain.dues.QDuesMonth.duesMonth;
import static com.muzisoft.division.domain.user.QUser.user;
import static com.muzisoft.division.domain.user.QUserDetails.userDetails;

public class DuesQueryRepositoryImpl extends QuerydslRepositorySupport implements DuesQueryRepository{

    private final JPAQueryFactory queryFactory;

    public DuesQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Dues.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public int duesTotalCount(DuesMonth duesMonth) {
        return queryFactory.select(
                        dues.count().intValue()
                )
                .from(dues)
                .where(
                        dues.duesMonth.eq(duesMonth)
                )
                .fetchOne();
    }

    @Override
    public int duesPaidCount(DuesMonth duesMonth) {
        return queryFactory.select(
                        dues.count().intValue()
                )
                .from(dues)
                .where(
                        dues.duesMonth.eq(duesMonth),
                        dues.paymentState.eq(2)
                )
                .fetchOne();
    }

    @Override
    public int duesUnpaidCount(DuesMonth duesMonth) {
        return queryFactory.select(
                        dues.count().intValue()
                )
                .from(dues)
                .where(
                        dues.duesMonth.eq(duesMonth),
                        dues.paymentState.ne(2)
                )
                .fetchOne();
    }

    @Override
    public Page<DuesListResponse.DuesList> duesList(Pageable pageable, DuesCondition duesCondition, CommonCondition condition, DuesMonth reqDuesMonth) {

        JPAQuery<DuesListResponse.DuesList> query = queryFactory.select(Projections.constructor(DuesListResponse.DuesList.class,
                dues.seq,
                userDetails.membershipNumber,
                user.userId,
                userDetails.name,
                userDetails.mobile,
                dues.bankName,
                dues.accountNumber,
                duesMonth.amount,
                dues.amount.as("paidAmount"),
                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", dues.paidAt),
                dues.paymentState
                ))
                .from(dues)
                .leftJoin(duesMonth).on(dues.duesMonth.eq(duesMonth))
                .leftJoin(userDetails).on(dues.userDetails.eq(userDetails))
                .leftJoin(user).on(user.userDetails.eq(userDetails))
                .where(
                        duesMonth.eq(reqDuesMonth),
                        searchBySearchTypeAndKeyword(duesCondition.getQueryType(), condition.getQuery()),
                        searchByPayment(duesCondition.getPaidStatus())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for(Sort.Order o : pageable.getSort()) {
            if(o.getProperty().equals("userId")) {
                PathBuilder<? extends User> pathBuilder = new PathBuilder<User>(user.getType(), user.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }else if(o.getProperty().equals("name") || o.getProperty().equals("mobile") || o.getProperty().equals("membershipNumber")) {
                PathBuilder<? extends UserDetails> pathBuilder = new PathBuilder<UserDetails>(userDetails.getType(), userDetails.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }/*else if(o.getProperty().equals("amount")) {
                PathBuilder<? extends DuesMonth> pathBuilder = new PathBuilder<DuesMonth>(duesMonth.getType(), duesMonth.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }*/else {
                PathBuilder<? extends Dues> pathBuilder = new PathBuilder<Dues>(dues.getType(), dues.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
        }

        QueryResults<DuesListResponse.DuesList> result = query.fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public int duesWaitingCount() {
        return queryFactory.select(
                dues.count().intValue()
            )
                .from(dues)
                .where(
                        dues.paymentState.eq(1),
                        dues.read.isFalse()
                )
                .fetchOne();
    }

    @Override
    public Page<DuesListResponse.DuesList> duesWaitingList(Pageable pageable, DuesCondition duesCondition, CommonCondition condition) {
        JPAQuery<DuesListResponse.DuesList> query = queryFactory.select(Projections.constructor(DuesListResponse.DuesList.class,
                        dues.seq,
                        userDetails.membershipNumber,
                        user.userId,
                        userDetails.name,
                        userDetails.mobile,
                        dues.bankName,
                        dues.accountNumber,
                        duesMonth.amount,
                        dues.amount.as("paidAmount"),
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", dues.paidAt),
                        dues.paymentState
                ))
                .from(dues)
                .leftJoin(duesMonth).on(dues.duesMonth.eq(duesMonth))
                .leftJoin(userDetails).on(dues.userDetails.eq(userDetails))
                .leftJoin(user).on(user.userDetails.eq(userDetails))
                .where(
                        dues.paymentState.eq(1),
                        dues.read.isFalse(),
                        searchBySearchTypeAndKeyword(duesCondition.getQueryType(), condition.getQuery())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for(Sort.Order o : pageable.getSort()) {
            if(o.getProperty().equals("userId")) {
                PathBuilder<? extends User> pathBuilder = new PathBuilder<User>(user.getType(), user.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }else if(o.getProperty().equals("name") || o.getProperty().equals("mobile") || o.getProperty().equals("membershipNumber")) {
                PathBuilder<? extends UserDetails> pathBuilder = new PathBuilder<UserDetails>(userDetails.getType(), userDetails.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }/*else if(o.getProperty().equals("amount")) {
                PathBuilder<? extends DuesMonth> pathBuilder = new PathBuilder<DuesMonth>(duesMonth.getType(), duesMonth.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }*/else {
                PathBuilder<? extends Dues> pathBuilder = new PathBuilder<Dues>(dues.getType(), dues.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
        }

        QueryResults<DuesListResponse.DuesList> result = query.fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<UserDuesListResponse> userDuesList(Pageable pageable, CommonCondition condition, UserDetails userDetails) {
        JPAQuery<UserDuesListResponse> query = queryFactory.select(Projections.constructor(UserDuesListResponse.class,
                    Expressions.constant(0),
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", dues.paidAt),
                    dues.amount,
                    dues.comment,
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", dues.confirmedAt),
                    dues.paymentState,
                    dues.duesMonth.year,
                    dues.duesMonth.month
                ))
                .from(dues)
                .where(
                        dues.userDetails.eq(userDetails),
                        dateGoe(condition.getDateFrom()),
                        dateLt(condition.getDateTo())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for(Sort.Order o : pageable.getSort()) {
            PathBuilder<? extends Dues> pathBuilder = new PathBuilder<Dues>(dues.getType(), dues.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<UserDuesListResponse> result = query.fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private Predicate searchByPayment(DuesPaidCheck duesPaidCheck) {
        BooleanBuilder builder = new BooleanBuilder();
        switch (duesPaidCheck) {
            case WHOLE :
                return null;
            case UNPAID:
                builder.and(dues.paymentState.eq(0));
                break;
            case WAIT:
                builder.and(dues.paymentState.eq(1));
                break;
            case PAID:
                builder.and(dues.paymentState.eq(2));
                break;
        }
        return builder;
    }

    private Predicate searchBySearchTypeAndKeyword(DuesSearchType queryType, String query) {
        BooleanBuilder builder = new BooleanBuilder();
        switch (queryType) {
            case WHOLE :
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
        return dateFrom != null ? dues.createdAt.goe(dateFrom) : null;
    }

    private BooleanExpression dateLt(Date dateTo) {
        return dateTo != null ? dues.createdAt.lt(dateTo) : null;
    }
}
