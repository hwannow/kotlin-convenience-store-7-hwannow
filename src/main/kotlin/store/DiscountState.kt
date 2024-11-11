package store

enum class DiscountState {
    PROMOTION_APPLICABLE, // 프로모션이 적용됨
    PROMOTION_ADDITIONAL, // 상품을 추가할 수 있음
    PROMOTION_EXCLUSION, // 일부가 프로모션 적용이 안 됨
    PROMOTION_NOT_APPLICABLE, // 프로모션 할인 대상이 아님
    MEMBERSHIP_DISCOUNT, // 멤버십 할인을 받음
    NOT_MEMBERSHIP_DISCOUNT // 멤버십 할인을 받지 않음
}