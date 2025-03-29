package io.hhplus.tdd.point.user

interface UserPointRepository {

    fun findUserPointBy(id: Long): UserPoint

    fun upsert(userPoint: UserPoint): UserPoint

}