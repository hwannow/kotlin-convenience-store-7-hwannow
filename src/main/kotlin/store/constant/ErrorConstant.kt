package store.constant

object ErrorConstant {
    const val ERROR_INPUT_FORMAT_MESSAGE = "[ERROR] 올바르지 않은 형식으로 입력했습니다."
    const val ERROR_INPUT_QUANTITY_NOT_INTEGER_MESSAGE = "[ERROR] 수량은 숫자여야 합니다."
    const val ERROR_INPUT_QUANTITY_IS_ZERO = "[ERROR] 1개 이상부터 구매 가능합니다."
    const val ERROR_INPUT_NON_EXISTENT_PRODUCT = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."
    const val ERROR_INPUT_PURCHASE_EXCEEDS_STOCK = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."
}