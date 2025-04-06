package io.hhplus.tdd.point.user

class UserPoint(
    val id: Long,
    var point: Long,
    val updateMillis: Long,
) {

    companion object {
        private const val MAX_POINT = 1000000000L
    }

    fun deductPoint(amount: Long) {
        if (isPointLessThan(amount)) {
            throw IllegalArgumentException("차감할 포인트가 없습니다.")
        }
        point -= amount
    }

    fun increasePoint(amount: Long) {
        if (isExceedMaxPoint(amount)) {
            throw IllegalArgumentException("포인트를 더 충전할 수 없습니다.")
        }
        point += amount
    }

    fun isPointLessThan(amount: Long) = point < amount

    fun isExceedMaxPoint(amount: Long) = point + amount > MAX_POINT

}
