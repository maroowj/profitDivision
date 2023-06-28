package com.muzisoft.division.domain.point;

import com.muzisoft.division.domain.dues.Dues;
import com.muzisoft.division.domain.dues.DuesMonth;
import com.muzisoft.division.domain.profit.ProfitDetails;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.interest.InterestListPerUserResponse;
import com.muzisoft.division.web.api.dto.admin.interest.InterestListResponse;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentCondition;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentListPerUserResponse;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentListResponse;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitListResponse;
import com.muzisoft.division.web.api.dto.common.BasicCondition;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.BasicSearchType;
import com.muzisoft.division.web.api.dto.common.enums.InvestmentSearchType;
import com.muzisoft.division.web.api.dto.common.enums.ProfitSearchType;
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
import static com.muzisoft.division.domain.point.QInterest.interest;
import static com.muzisoft.division.domain.point.QInterestDetails.interestDetails;
import static com.muzisoft.division.domain.user.QUser.user;
import static com.muzisoft.division.domain.user.QUserDetails.userDetails;


public class InterestQueryRepositoryImpl extends QuerydslRepositorySupport implements InterestQueryRepository{

    private final JPAQueryFactory queryFactory;

    public InterestQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Interest.class);
        this.queryFactory = queryFactory;
    }


    @Override
    public Page<InterestListResponse> interestList(Pageable pageable, CommonCondition cond) {
        JPAQuery<InterestListResponse> query = queryFactory.select(Projections.constructor(InterestListResponse.class,
                    interest.seq,
                    interest.interestRate,
                    interest.total,
                    interest.amount,
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", interest.createdAt)
                ))
                .from(interest)
                .where(
                        dateGoe(cond.getDateFrom()),
                        dateLt(cond.getDateTo())
                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
                ;

        for(Sort.Order o : pageable.getSort()) {
            PathBuilder<? extends Interest> pathBuilder = new PathBuilder<Interest>(interest.getType(), interest.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<InterestListResponse> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression dateGoe(Date dateFrom) {
        return dateFrom != null ? interest.createdAt.goe(dateFrom) : null;
    }

    private BooleanExpression dateLt(Date dateTo) {
        return dateTo != null ? interest.createdAt.lt(dateTo) : null;
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
}
