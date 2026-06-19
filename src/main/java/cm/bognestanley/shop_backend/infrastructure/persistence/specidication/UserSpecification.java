package cm.bognestanley.shop_backend.infrastructure.persistence.specidication;

import org.springframework.data.jpa.domain.Specification;

import cm.bognestanley.shop_backend.domain.user.criteria.SearchUserCriteria;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.user.UserJpaEntity;

public class UserSpecification {

    public static Specification<UserJpaEntity> hasQuery(String queryWord) {
        return (root, query, builder) -> {
            if (queryWord == null || queryWord.isBlank()) {
                return null;
            }

            return builder.or( 
                builder.like(builder.lower(root.get("email")), "%" + queryWord.toLowerCase() + "%"),
                builder.like(builder.lower(root.get("firstName")), "%" + queryWord.toLowerCase() + "%"),
                builder.like(builder.lower(root.get("lastName")), "%" + queryWord.toLowerCase() + "%")
            );
        };
    }
    
    public static Specification<UserJpaEntity> hasRole(String role) {
        return (root, query, builder) -> {
            if (role == null || role.isBlank()) {
                return null;
            }

            return builder.like(builder.lower(root.get("role")), "%" + role.toLowerCase() + "%");
        };
    }

    public static Specification<UserJpaEntity> toSpecification(SearchUserCriteria criteria) {
        return Specification.where(hasQuery(criteria.query()))
                .and(hasRole(criteria.role()));
    }
    
}
