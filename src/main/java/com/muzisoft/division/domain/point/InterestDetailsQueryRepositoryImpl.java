package com.muzisoft.division.domain.point;

import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.interest.InterestDetailsResponse;
import com.muzisoft.division.web.api.dto.admin.interest.InterestListPerUserResponse;
import com.muzisoft.division.web.api.dto.admin.interest.InterestListResponse;
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

import static com.muzisoft.division.domain.point.QInterest.interest;
import static com.muzisoft.division.domain.point.QInterestDetails.interestDetails;
import static com.muzisoft.division.domain.user.QUser.user;
import static com.muzisoft.division.domain.user.QUserDetails.userDetails;

public class InterestDetailsQueryRepositoryImpl extends QuerydslRepositorySupport implements InterestDetailsQueryRepository{

    private final JPAQueryFactory queryFactory;

    public InterestDetailsQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(InterestDetails.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<InterestListPerUserResponse> interestListByInterest(Pageable pageable, Interest interest, BasicCondition basicCondition, CommonCondition condition) {
        JPAQuery<InterestListPerUserResponse> query = queryFactory.select(Projections.constructor(InterestListPerUserResponse.class,
                        userDetails.seq,
                        userDetails.membershipNumber,
                        userDetails.name,
                        user.userId,
                        Expressions.constant("-"),
                        interestDetails.total,
                        interestDetails.amount
                ))
                .from(interestDetails)
                .leftJoin(userDetails).on(interestDetails.userDetails.eq(userDetails))
                .leftJoin(user).on(user.userDetails.eq(userDetails))
                .where(
                        interestDetails.interest.eq(interest),
                        searchBySearchTypeAndKeyword(basicCondition.getQueryType(), condition.getQuery())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                ;

        for(Sort.Order o : pageable.getSort()) {
            if(o.getProperty().equals("userId")) {
                PathBuilder<? extends User> pathBuilder = new PathBuilder<User>(user.getType(), user.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }else if(o.getProperty().equals("name") || o.getProperty().equals("membershipNumber") || o.getProperty().equals("investment")) {
                PathBuilder<? extends UserDetails> pathBuilder = new PathBuilder<UserDetails>(userDetails.getType(), userDetails.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }else {
                PathBuilder<? extends InterestDetails> pathBuilder = new PathBuilder<InterestDetails>(interestDetails.getType(), interestDetails.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
        }

        QueryResults<InterestListPerUserResponse> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<InterestDetailsResponse> interestDetails(Pageable pageable, UserDetails userDetails, CommonCondition condition) {
        JPAQuery<InterestDetailsResponse> query = queryFactory.select(Projections.constructor(InterestDetailsResponse.class,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", interestDetails.createdAt),
                        interestDetails.total,
                        interestDetails.amount
                ))
                .from(interestDetails)
                .where(
                        interestDetails.userDetails.eq(userDetails),
                        dateFrom(condition.getDateFrom()),
                        dateTo(condition.getDateTo())
                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
                ;

        for(Sort.Order o : pageable.getSort()) {
            PathBuilder<? extends InterestDetails> pathBuilder = new PathBuilder<InterestDetails>(interestDetails.getType(), interestDetails.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<InterestDetailsResponse> result = query.fetchResults();
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

    private BooleanExpression dateFrom(Date dateFrom) {
        return dateFrom != null ? interestDetails.createdAt.goe(dateFrom) : null;
    }

    private BooleanExpression dateTo(Date dateTo) {
        return dateTo != null ? interestDetails.createdAt.lt(dateTo) : null;
    }
}
