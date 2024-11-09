package store.constant

object ViewConstant {
    const val OUTPUT_GREETINGS = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n"
    const val OUTPUT_INVENTORY = "%s %s원 %s개 %s"
    const val OUTPUT_PROMOTION_ADDITIONAL_CONFIRMATION = "\n현재 %S은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"
    const val OUTPUT_PROMOTION_EXCLUSION_CONFIRMATION = "\n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"
    const val OUTPUT_MEMBERSHIP_DISCOUNT_CONFIRMATION = "\n멤버십 할인을 받으시겠습니까? (Y/N)"

    const val INPUT_GUIDE = "\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"
}