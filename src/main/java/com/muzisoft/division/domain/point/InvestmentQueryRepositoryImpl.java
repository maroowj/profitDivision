package com.muzisoft.division.domain.point;

import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentCondition;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentListPerUserResponse;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.investment.UserInvestmentListResponse;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import com.muzisoft.division.web.api.dto.common.enums.InvestmentSearchType;


import java.util.Date;
import java.util.List;

import static com.muzisoft.division.domain.point.QInvestment.investment;
import static com.muzisoft.division.domain.user.QUser.user;
import static com.muzisoft.division.domain.user.QUserDetails.userDetails;


public class InvestmentQueryRepositoryImpl extends QuerydslRepositorySupport implements InvestmentQueryRepository{

    private final JPAQueryFactory queryFactory;

    public InvestmentQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Investment.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<InvestmentListResponse> investmentList(Pageable pageable, InvestmentCondition investmentCondition, CommonCondition condition) {
        JPAQuery<InvestmentListResponse> query = queryFactory.select(Projections.constructor(InvestmentListResponse.class,
                    userDetails.membershipNumber,
                    investment.seq,
                    userDetails.seq,
                    user.userId,
                    investment.name,
                    userDetails.mobile,
                    investment.bankName,
                    investment.accountNumber,
                    investment.amount,
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", investment.depositedAt),
                    investment.status
                ))
                .from(investment)
                .leftJoin(userDetails).on(investment.userDetails.eq(userDetails))
                .leftJoin(user).on(user.userDetails.eq(userDetails))
                .where(
                        searchBySearchTypeAndKeyword(investmentCondition.getQueryType(), condition.getQuery())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for(Sort.Order o : pageable.getSort()) {
            if(o.getProperty().equals("userId")) {
                PathBuilder<? extends User> pathBuilder = new PathBuilder<User>(user.getType(), user.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }else if(o.getProperty().equals("mobile") || o.getProperty().equals("membershipNumber")) {
                PathBuilder<? extends UserDetails> pathBuilder = new PathBuilder<UserDetails>(userDetails.getType(), userDetails.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }else {
                PathBuilder<? extends Investment> pathBuilder = new PathBuilder<Investment>(investment.getType(), investment.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
        }

        QueryResults<InvestmentListResponse> result = query.fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<InvestmentListPerUserResponse> investmentListPerUser(Pageable pageable, String userDetailsSeq) {
        JPAQuery<InvestmentListPerUserResponse> query = queryFactory.select(Projections.constructor(InvestmentListPerUserResponse.class,
                    investment.seq,
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", investment.depositedAt),
                    investment.total,
                    investment.amount,
                    investment.status
                ))
                .from(investment)
                .where(investment.userDetails.seq.eq(userDetailsSeq));

        for(Sort.Order o : pageable.getSort()) {
            PathBuilder<? extends Investment> pathBuilder = new PathBuilder<Investment>(investment.getType(), investment.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<InvestmentListPerUserResponse> result = query.fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<UserInvestmentListResponse> userInvestment(Pageable pageable, CommonCondition condition, UserDetails userDetails) {
        JPAQuery<UserInvestmentListResponse> query = queryFactory.select(Projections.constructor(UserInvestmentListResponse.class,
                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", investment.createdAt),
                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", investment.depositedAt),
                investment.amount,
                investment.status,
                investment.comment,
                Expressions.constant(0)
                ))
                .from(investment)
                .where(
                        investment.userDetails.eq(userDetails),
                        dateGoe(condition.getDateFrom()),
                        dateLt(condition.getDateTo())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for(Sort.Order o : pageable.getSort()) {
            PathBuilder<? extends Investment> pathBuilder = new PathBuilder<Investment>(investment.getType(), investment.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<UserInvestmentListResponse> result = query.fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private Predicate searchBySearchTypeAndKeyword(InvestmentSearchType queryType, String query) {
        BooleanBuilder builder = new BooleanBuilder();
        switch (queryType) {
            case WHOLE :
                builder.and(investment.name.contains(query).or(user.userId.contains(query)).or(userDetails.membershipNumber.contains(query)));
                break;
            case MEMBERSHIP_NUMBER:
                builder.and(userDetails.membershipNumber.contains(query));
                break;
            case USER_NAME:
                builder.and(investment.name.contains(query));
                break;
            case USER_ID:
                builder.and(user.userId.contains(query));
                break;
        }
        return builder;
    }

    private BooleanExpression dateGoe(Date dateFrom) {
        return dateFrom != null ? investment.createdAt.goe(dateFrom) : null ;
    }

    private BooleanExpression dateLt(Date dateTo) {
        return dateTo != null ? investment.createdAt.lt(dateTo) : null ;
    }
}
