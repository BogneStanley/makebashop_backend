package cm.bognestanley.shop_backend.domain.user.criteria;

public record SearchUserCriteria(
        String query,
        String role) {
    public static SearchUserCriteria empty() {
        return new SearchUserCriteria(
                null,
                null);
    }
}
