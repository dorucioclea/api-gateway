package com.t1t.apim.rest.impl.util;

import com.t1t.apim.beans.search.SearchCriteriaBean;
import com.t1t.apim.beans.search.SearchCriteriaFilterBean;
import com.t1t.apim.beans.search.SearchCriteriaFilterOperator;
import com.t1t.apim.exceptions.InvalidSearchCriteriaException;
import com.t1t.apim.exceptions.i18n.Messages;

import java.util.HashSet;
import java.util.Set;

/**
 * Some utility methods related to searches and search criteria.
 */
public final class SearchCriteriaUtil {
    
    public static final Set<SearchCriteriaFilterOperator> validOperators = new HashSet<>();
    static {
        validOperators.add(SearchCriteriaFilterOperator.eq);
        validOperators.add(SearchCriteriaFilterOperator.gt);
        validOperators.add(SearchCriteriaFilterOperator.gte);
        validOperators.add(SearchCriteriaFilterOperator.lt);
        validOperators.add(SearchCriteriaFilterOperator.lte);
        validOperators.add(SearchCriteriaFilterOperator.neq);
        validOperators.add(SearchCriteriaFilterOperator.like);
        validOperators.add(SearchCriteriaFilterOperator.bool_eq);
    }

    /**
     * Validates that the search criteria bean is complete and makes sense.
     * @param criteria the search criteria
     * @throws InvalidSearchCriteriaException when the search criteria is not valid
     */
    public static final void validateSearchCriteria(SearchCriteriaBean criteria) throws InvalidSearchCriteriaException {
        if (criteria.getPaging() != null) {
            if (criteria.getPaging().getPage() < 1) {
                throw new InvalidSearchCriteriaException(Messages.i18n.format("SearchCriteriaUtil.MissingPage")); //$NON-NLS-1$
            }
            if (criteria.getPaging().getPageSize() < 1) {
                throw new InvalidSearchCriteriaException(Messages.i18n.format("SearchCriteriaUtil.MissingPageSize")); //$NON-NLS-1$
            }
        }
        int count = 1;
        for (SearchCriteriaFilterBean filter : criteria.getFilters()) {
            if (filter.getName() == null || filter.getName().trim().length() == 0) {
                throw new InvalidSearchCriteriaException(Messages.i18n.format("SearchCriteriaUtil.MissingSearchFilterName", count)); //$NON-NLS-1$
            }
            if (filter.getValue() == null || filter.getValue().trim().length() == 0) {
                throw new InvalidSearchCriteriaException(Messages.i18n.format("SearchCriteriaUtil.MissingSearchFilterValue", count)); //$NON-NLS-1$
            }
            if (filter.getOperator() == null || !validOperators.contains(filter.getOperator())) {
                throw new InvalidSearchCriteriaException(Messages.i18n.format("SearchCriteriaUtil.MissingSearchFilterOperator", count)); //$NON-NLS-1$
            }
            count++;
        }
        if (criteria.getOrderBy() != null && (criteria.getOrderBy().getName() == null || criteria.getOrderBy().getName().trim().length() == 0)) {
            throw new InvalidSearchCriteriaException(Messages.i18n.format("SearchCriteriaUtil.MissingOrderByName")); //$NON-NLS-1$
        }
    }
}
