package store.view

import camp.nextstep.edu.missionutils.Console
import store.constant.ViewConstant

class InputView {
    fun readItem(): String {
        println(ViewConstant.INPUT_GUIDE)
        val input = Console.readLine()

        return input
    }
}