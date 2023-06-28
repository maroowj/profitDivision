package com.muzisoft.division.domain.profit;

import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.profit.ExpectedProfitListResponse;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitCondition;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitDetailsPerUser;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.ProfitSearchType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.*;
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

import static com.muzisoft.division.domain.profit.QExpectedProfitDetails.expectedProfitDetails;
import static com.muzisoft.division.domain.profit.QProfitDetails.profitDetails;
import static com.muzisoft.division.domain.user.QUser.user;
import static com.muzisoft.division.domain.user.QUserDetails.userDetails;

public class ProfitDetailsQueryRepositoryImpl extends QuerydslRepositorySupport implements ProfitDetailsQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ProfitDetailsQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ProfitDetails.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<ProfitListResponse> profitList(Pageable pageable, ProfitCondition profitCondition, CommonCondition condition, Profit profit) {
        JPAQuery<ProfitListResponse> query = queryFactory.select(Projections.constructor(ProfitListResponse.class,
                    userDetails.seq,
                    userDetails.membershipNumber,
                    userDetails.name,
                    user.userId,
                    profitDetails.userLevel,
                    profitDetails.total,
                    profitDetails.amount
                ))
                .from(profitDetails)
                .leftJoin(userDetails).on(profitDetails.userDetails.eq(userDetails))
                .leftJoin(user).on(userDetails.eq(user.userDetails))
                .where(
                        profitDetails.profit.eq(profit),
                        searchBySearchTypeAndKeyword(profitCondition.getQueryType(), condition.getQuery())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for(Sort.Order o : pageable.getSort()) {
            if(o.getProperty().equals("userId")) {
                PathBuilder<? extends User> pathBuilder = new PathBuilder<User>(user.getType(), user.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }else if(o.getProperty().equals("name") || o.getProperty().equals("membershipNumber")){
                PathBuilder<? extends UserDetails> pathBuilder = new PathBuilder<UserDetails>(userDetails.getType(), userDetails.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }else {
                PathBuilder<? extends ProfitDetails> pathBuilder = new PathBuilder<ProfitDetails>(profitDetails.getType(), profitDetails.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
        }

        QueryResults<ProfitListResponse> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<ProfitDetailsPerUser> profitDetailsPerUser(Pageable pageable, CommonCondition condition, UserDetails userDetails) {
        JPAQuery<ProfitDetailsPerUser> query = queryFactory.select(Projections.constructor(ProfitDetailsPerUser.class,
                    profitDetails.amount,
                    profitDetails.total,
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", profitDetails.createdAt)
                ))
                .from(profitDetails)
                .where(
                        profitDetails.userDetails.eq(userDetails),
                        dateGoe(condition.getDateFrom()),
                        dateLt(condition.getDateTo())
                );

        for(Sort.Order o : pageable.getSort()) {
            PathBuilder<? extends ProfitDetails> pathBuilder = new PathBuilder<ProfitDetails>(profitDetails.getType(), profitDetails.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<ProfitDetailsPerUser> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private Predicate searchBySearchTypeAndKeyword(ProfitSearchType queryType, String query) {
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
