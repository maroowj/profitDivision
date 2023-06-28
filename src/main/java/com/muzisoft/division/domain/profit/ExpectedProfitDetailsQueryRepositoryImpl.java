package com.muzisoft.division.domain.profit;

import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.member.UserListResponse;
import com.muzisoft.division.web.api.dto.admin.profit.ExpectedProfitListResponse;
import com.muzisoft.division.web.api.dto.admin.profit.ExpectedProfitRequest;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitCondition;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.DuesSearchType;
import com.muzisoft.division.web.api.dto.common.enums.ProfitSearchType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import static com.muzisoft.division.domain.profit.QExpectedProfitDetails.expectedProfitDetails;
import static com.muzisoft.division.domain.user.QUser.user;
import static com.muzisoft.division.domain.user.QUserDetails.userDetails;

public class ExpectedProfitDetailsQueryRepositoryImpl extends QuerydslRepositorySupport implements ExpectedProfitDetailsQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ExpectedProfitDetailsQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ExpectedProfitDetails.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<ExpectedProfitListResponse> expectedProfitList(Pageable pageable, ExpectedProfitRequest request, ProfitCondition profitCondition, CommonCondition condition, ExpectedProfit expectedProfit) {
        JPAQuery<ExpectedProfitListResponse> query = queryFactory.select(Projections.constructor(ExpectedProfitListResponse.class,
                    userDetails.membershipNumber,
                    userDetails.name,
                    user.userId,
                    expectedProfitDetails.userLevel,
                    expectedProfitDetails.userTotal,
                    expectedProfitDetails.amount
                ))
                .from(expectedProfitDetails)
                .leftJoin(userDetails).on(expectedProfitDetails.userDetails.eq(userDetails))
                .leftJoin(user).on(userDetails.eq(user.userDetails))
                .where(
                        expectedProfitDetails.expectedProfit.eq(expectedProfit),
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
                PathBuilder<? extends ExpectedProfitDetails> pathBuilder = new PathBuilder<ExpectedProfitDetails>(expectedProfitDetails.getType(), expectedProfitDetails.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
        }

        QueryResults<ExpectedProfitListResponse> result = query.fetchResults();
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
}
